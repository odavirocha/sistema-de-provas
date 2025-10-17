package dev.odroca.api_provas.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.odroca.api_provas.dto.option.UpdateOptionModelDTO;
import dev.odroca.api_provas.dto.question.CreateQuestionModelDTO;
import dev.odroca.api_provas.dto.question.CreateQuestionResponseDTO;
import dev.odroca.api_provas.dto.question.GetQuestionModelDTO;
import dev.odroca.api_provas.dto.question.UpdateQuestionRequestDTO;
import dev.odroca.api_provas.dto.question.UpdateQuestionResponseDTO;
import dev.odroca.api_provas.dto.questions.CreateQuestionsRequestDTO;
import dev.odroca.api_provas.dto.questions.CreateQuestionsResponseDTO;
import dev.odroca.api_provas.entity.OptionEntity;
import dev.odroca.api_provas.entity.QuestionEntity;
import dev.odroca.api_provas.entity.TestEntity;
import dev.odroca.api_provas.exception.CorrectOptionNotFoundException;
import dev.odroca.api_provas.exception.MultipleCorrectOptionsException;
import dev.odroca.api_provas.exception.OptionNotFoundException;
import dev.odroca.api_provas.exception.QuestionNotFoundException;
import dev.odroca.api_provas.exception.TestNotFoundException;
import dev.odroca.api_provas.mapper.OptionMapper;
import dev.odroca.api_provas.mapper.QuestionMapper;
import dev.odroca.api_provas.repository.OptionRepository;
import dev.odroca.api_provas.repository.QuestionRepository;
import dev.odroca.api_provas.repository.TestRepository;

@Service
@Transactional(readOnly = true)
public class QuestionService {
    
    @Autowired
    private TestRepository testRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private OptionRepository optionRepository;
    @Autowired
    private OptionMapper optionMapper;
    @Autowired
    private QuestionMapper questionMapper;


    @Transactional
    public CreateQuestionResponseDTO createQuestion(UUID testId, CreateQuestionModelDTO questionModel) {
        
        TestEntity test = testRepository.findById(testId).orElseThrow(() -> new TestNotFoundException(testId));
        
        QuestionEntity questionEntity = new QuestionEntity();
        questionEntity.setTest(test);
        questionEntity.setQuestion(questionModel.getQuestion());
        
        List<OptionEntity> optionEntities = optionMapper.createDtoToEntityList(questionModel.getOptions());
        optionEntities.forEach(option -> option.setQuestion(questionEntity));
        
        if (optionEntities.stream().noneMatch(option -> option.getIsCorrect())) {
            throw new CorrectOptionNotFoundException();
        };
        
        long listOptionsSize = optionEntities.stream().filter(option -> option.getIsCorrect()).count();
        if ( listOptionsSize > 1 ) {
            throw new MultipleCorrectOptionsException();
        };
        
        questionEntity.setOptions(optionEntities);
        QuestionEntity saved = questionRepository.save(questionEntity);
        
        UUID correctionOptionId = saved.getOptions().stream()
        .filter(option -> option.getIsCorrect())
        .findFirst()
        .get()
        .getId();
        
        return new CreateQuestionResponseDTO(
            saved.getId(),
            saved.getQuestion(),
            saved.getOptions().size(),
            correctionOptionId,
            "Questão criada com sucesso!"
        );
    }
        
    @Transactional
    public CreateQuestionsResponseDTO createQuestions(CreateQuestionsRequestDTO questionsModel) {
        int totalQuestions = 0;
        
        // Transforma cada questão em uma entidade QuestionModelDTO
        for (CreateQuestionModelDTO question : questionsModel.getQuestions()) {
            createQuestion(questionsModel.getTestId(), question);
            totalQuestions++;
        }
        
        return new CreateQuestionsResponseDTO(questionsModel.getTestId(), totalQuestions, "Questões criadas com sucesso!");
    }
    
    @Transactional
    public UpdateQuestionResponseDTO updateQuestion(UUID questionId, UpdateQuestionRequestDTO requestQuestion) {

        QuestionEntity databaseQuestion = questionRepository.findById(questionId).orElseThrow(() -> new QuestionNotFoundException(questionId));
        
        // Atualiza o enunciado
        databaseQuestion.setQuestion(requestQuestion.getQuestion());

        if (requestQuestion.getOptions().stream().noneMatch(option -> option.getIsCorrect())) {
            throw new CorrectOptionNotFoundException();
        }

        long listLimit = requestQuestion.getOptions().stream().filter(option -> option.getIsCorrect()).count();
        if (listLimit > 1) {
            throw new MultipleCorrectOptionsException();
        }

        // Guarda somente os IDs de quem tem ID
        List<UUID> idsFromRequest = requestQuestion.getOptions().stream()
            .map(option -> option.getOptionId())
            .filter(id -> id != null)
            .collect(Collectors.toList());

        List<OptionEntity> optionsThatExist = optionRepository.findAllById(idsFromRequest);

        if (optionsThatExist.size() != idsFromRequest.size()) {
            throw new OptionNotFoundException();
        }
        
        // Entidades que não existem no banco de dados são armazenados
        List<OptionEntity> entitiesToDelete = databaseQuestion.getOptions().stream()
            .filter(option -> !idsFromRequest.contains(option.getId()))
            .collect(Collectors.toList());

        optionRepository.deleteAll(entitiesToDelete);

        databaseQuestion.getOptions().removeAll(entitiesToDelete);

        // Atualiza as questões que já existiam
        for (Integer i = 0; i < databaseQuestion.getOptions().size(); i++) {
            databaseQuestion.getOptions().get(i).setIsCorrect(requestQuestion.getOptions().get(i).getIsCorrect());
            databaseQuestion.getOptions().get(i).setValue(requestQuestion.getOptions().get(i).getValue());
        }

        List<OptionEntity> newOptions = requestQuestion.getOptions().stream()
            .filter(option -> option.getOptionId() == null)
            .map(option -> {
                OptionEntity optionEntity = new OptionEntity();
                optionEntity.setQuestion(databaseQuestion);
                optionEntity.setValue(option.getValue());
                optionEntity.setIsCorrect(option.getIsCorrect());
                return optionEntity;
            })
            .collect(Collectors.toList());

        databaseQuestion.getOptions().addAll(newOptions);

        List<UpdateOptionModelDTO> existingsIds = requestQuestion.getOptions().stream()
        .filter(option -> idsFromRequest.contains(option.getOptionId()))
        .collect(Collectors.toList());

        for (Integer i = 0; i < existingsIds.size(); i++) {
            databaseQuestion.getOptions().get(i).setValue(existingsIds.get(i).getValue());
            databaseQuestion.getOptions().get(i).setIsCorrect(existingsIds.get(i).getIsCorrect());
        }
        
        QuestionEntity saved = questionRepository.save(databaseQuestion);

        return new UpdateQuestionResponseDTO(saved.getId(), "Questão alterada com sucesso!");
    }

    public List<GetQuestionModelDTO> getAllQuestionsForTest(UUID testId) {

        TestEntity test = testRepository.findById(testId).orElseThrow(() -> new TestNotFoundException(testId));
        
        List<GetQuestionModelDTO> questions = questionMapper.toDtoList(test.getQuestions());

        return questions;
    }

}
