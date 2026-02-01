package dev.odroca.api_provas.service;

import java.util.Set;
import java.util.UUID;

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
import dev.odroca.api_provas.exception.QuestionNotFoundException;
import dev.odroca.api_provas.exception.TestNotFoundException;
import dev.odroca.api_provas.mapper.OptionMapper;
import dev.odroca.api_provas.mapper.QuestionMapper;
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
    private OptionMapper optionMapper;
    @Autowired
    private QuestionMapper questionMapper;


    @Transactional
    public CreateQuestionResponseDTO createQuestion(UUID testId, CreateQuestionModelDTO questionModel) {
        
        TestEntity test = testRepository.findById(testId).orElseThrow(() -> new TestNotFoundException(testId));
        
        QuestionEntity questionEntity = new QuestionEntity();
        questionEntity.setTest(test);
        questionEntity.setQuestion(questionModel.getQuestion());
        
        Set<OptionEntity> optionEntities = optionMapper.createDtoToEntityList(questionModel.getOptions());
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
    public CreateQuestionsResponseDTO createQuestions(UUID testId, CreateQuestionsRequestDTO questionsModel) {
        int totalQuestions = 0;
        
        // Transforma cada questão em uma entidade QuestionModelDTO
        for (CreateQuestionModelDTO question : questionsModel.getQuestions()) {
            createQuestion(testId, question);
            totalQuestions++;
        }
        
        return new CreateQuestionsResponseDTO(testId, totalQuestions, "Questões criadas com sucesso!");
    }
    
    @Transactional
    public UpdateQuestionResponseDTO updateQuestion(UUID questionId, UpdateQuestionRequestDTO requestQuestion) {

        QuestionEntity databaseQuestion = questionRepository.findByIdWithOptions(questionId).orElseThrow(() -> new QuestionNotFoundException(questionId));
        Set<OptionEntity> databaseOptions = databaseQuestion.getOptions();

        // Atualiza o enunciado
        databaseQuestion.setQuestion(requestQuestion.question());

        if (requestQuestion.options().stream().noneMatch(option -> option.isCorrect())) {
            throw new CorrectOptionNotFoundException();
        }

        long listLimit = requestQuestion.options().stream().filter(option -> option.isCorrect()).count();
        if (listLimit > 1) throw new MultipleCorrectOptionsException();

        for (UpdateOptionModelDTO requestOption : requestQuestion.options()) {
            for (OptionEntity databaseOption : databaseOptions) {
                if (databaseOption.getId().equals(requestOption.optionId())) {
                    databaseOption.setValue(requestOption.value());
                    databaseOption.setIsCorrect(requestOption.isCorrect());
                    break;
                }
            }
        }

        QuestionEntity saved = questionRepository.save(databaseQuestion);

        return new UpdateQuestionResponseDTO(saved.getId(), "Questão alterada com sucesso!");
    }

    public Set<GetQuestionModelDTO> getAllQuestionsForTest(UUID testId) {

        TestEntity test = testRepository.findById(testId).orElseThrow(() -> new TestNotFoundException(testId));
        Set<GetQuestionModelDTO> questions = questionMapper.toDtoList(test.getQuestions());

        return questions;
    }

}
