package dev.odroca.api_provas.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import dev.odroca.api_provas.dto.QuestionModelDTO;
import dev.odroca.api_provas.dto.UpdateQuestionRequestDTO;
import dev.odroca.api_provas.dto.UpdateQuestionResponseDTO;
import dev.odroca.api_provas.dto.CreateQuestionResponseDTO;
import dev.odroca.api_provas.dto.CreateQuestionsRequestDTO;
import dev.odroca.api_provas.dto.CreateQuestionsResponseDTO;
import dev.odroca.api_provas.dto.CreateTestResponseDTO;
import dev.odroca.api_provas.entity.OptionEntity;
import dev.odroca.api_provas.entity.QuestionEntity;
import dev.odroca.api_provas.entity.TestEntity;
import dev.odroca.api_provas.exception.QuestionNotFoundException;
import dev.odroca.api_provas.exception.TestNotFoundException;
import dev.odroca.api_provas.repository.QuestionRepository;
import dev.odroca.api_provas.repository.TestRepository;

@Service
public class TestService {
    private final TestRepository testRepository;
    private final QuestionRepository questionRepository;

    public TestService(TestRepository testRepository, QuestionRepository questionRepository) {
        this.testRepository = testRepository;
        this.questionRepository = questionRepository;
    }

    public CreateTestResponseDTO createTest(TestEntity test) {

        TestEntity saved = testRepository.save(test);
        
        CreateTestResponseDTO response = new CreateTestResponseDTO();

        response.setTestId(saved.getId());
        response.setName(saved.getName());

        return response;
    }

    public CreateQuestionResponseDTO createQuestion(UUID testId, QuestionModelDTO questionModel) {

        TestEntity test = testRepository.findById(testId).orElseThrow(() -> new TestNotFoundException(testId));
        
        QuestionEntity questionEntity = new QuestionEntity();
        questionEntity.setTest(test);
        questionEntity.setQuestion(questionModel.getQuestion());

        List<OptionEntity> optionEntities = questionModel.getOptions().stream().map(option -> {
            OptionEntity optionEntity = new OptionEntity();
            optionEntity.setValue(option.getValue());
            optionEntity.setIsCorrect(option.getIsCorrect());
            optionEntity.setQuestion(questionEntity);
            return optionEntity;
        }).collect(Collectors.toList());
        
        questionEntity.setOptions(optionEntities);
        QuestionEntity saved = questionRepository.save(questionEntity);

        UUID correctionOptionId = saved.getOptions().stream()
        .filter(option -> option.getIsCorrect())
        .findFirst()
        .map(option -> option.getId())
        .orElse(null);
        
        return new CreateQuestionResponseDTO(
            saved.getId(),
            saved.getQuestion(),
            saved.getOptions().size(),
            correctionOptionId,
            "Questão criada com sucesso!"
        );
    }
    
    public CreateQuestionsResponseDTO createQuestions(CreateQuestionsRequestDTO questionsModel) {
        int totalQuestions = 0;

        // Transforma cada questão em uma entidade QuestionModelDTO
        for (QuestionModelDTO question : questionsModel.getQuestions()) {
            createQuestion(questionsModel.getTestId(), question);
            totalQuestions++;
        }

        return new CreateQuestionsResponseDTO(questionsModel.getTestId(), totalQuestions, "Questões criadas com sucesso!");
    }

    public UpdateQuestionResponseDTO updateQuestion(UUID questionId, UpdateQuestionRequestDTO questionUpdate) {

        QuestionEntity question = questionRepository.findById(questionId).orElseThrow(() -> new QuestionNotFoundException(questionId));

        


        return new UpdateQuestionResponseDTO();
    }

}
