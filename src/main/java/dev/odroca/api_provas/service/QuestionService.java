package dev.odroca.api_provas.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
import dev.odroca.api_provas.exception.QuestionNotFoundException;
import dev.odroca.api_provas.exception.TestNotFoundException;
import dev.odroca.api_provas.mapper.OptionMapper;
import dev.odroca.api_provas.mapper.QuestionMapper;
import dev.odroca.api_provas.repository.QuestionRepository;
import dev.odroca.api_provas.repository.TestRepository;

@Service
@Transactional(readOnly = true)
public class QuestionService {
    
    private final TestRepository testRepository;
    private final QuestionRepository questionRepository;
    private final OptionMapper optionMapper;
    private final QuestionMapper questionMapper;
    
    public QuestionService(TestRepository testRepository, QuestionRepository questionRepository, OptionMapper optionMapper, QuestionMapper questionMapper) {
        this.testRepository = testRepository;
        this.questionRepository = questionRepository;
        this.optionMapper = optionMapper;
        this.questionMapper = questionMapper;
    }

    @Transactional
    public CreateQuestionResponseDTO createQuestion(UUID testId, CreateQuestionModelDTO questionModel) {
        
        TestEntity test = testRepository.findById(testId).orElseThrow(() -> new TestNotFoundException(testId));
        
        QuestionEntity questionEntity = new QuestionEntity();
        questionEntity.setTest(test);
        questionEntity.setQuestion(questionModel.getQuestion());

        List<OptionEntity> optionEntities = optionMapper.toEntityList(questionModel.getOptions());
        optionEntities.forEach(option -> option.setQuestion(questionEntity));
        
        questionEntity.setOptions(optionEntities);
        QuestionEntity saved = questionRepository.save(questionEntity);
        
        UUID correctionOptionId = saved.getOptions().stream()
        .filter(option -> option.getIsCorrect())
        .findFirst()
        .map(option -> option.getId())
        .orElseThrow(() -> new CorrectOptionNotFoundException());
        
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
    public UpdateQuestionResponseDTO updateQuestion(UUID questionId, UpdateQuestionRequestDTO questionUpdate) {

        QuestionEntity question = questionRepository.findById(questionId).orElseThrow(() -> new QuestionNotFoundException(questionId));
        
        question.setQuestion(questionUpdate.getQuestion());

        List<OptionEntity> currentOptions = question.getOptions();

        // ficou muito fei esse fori
        for (int i = 0; i < currentOptions.size(); i++) {
            currentOptions.get(i).setValue(questionUpdate.getOptions().get(i).getValue());
            currentOptions.get(i).setIsCorrect(questionUpdate.getOptions().get(i).getIsCorrect());
        }
        
        QuestionEntity saved = questionRepository.save(question);

        saved.getOptions().stream()
        .filter(option -> option.getIsCorrect())
        .findFirst()
        .orElseThrow(() -> new CorrectOptionNotFoundException());

        return new UpdateQuestionResponseDTO(saved.getId(), "Questão alterada com sucesso!");
    }

    public List<GetQuestionModelDTO> getAllQuestionsForTest(UUID testId) {

        TestEntity test = testRepository.findById(testId).orElseThrow(() -> new TestNotFoundException(testId));
        
        List<GetQuestionModelDTO> questions = questionMapper.toDtoList(test.getQuestions());

        return questions;
    }

}
