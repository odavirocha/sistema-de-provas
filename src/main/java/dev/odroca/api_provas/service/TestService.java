package dev.odroca.api_provas.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import dev.odroca.api_provas.dto.QuestionModelDTO;
import dev.odroca.api_provas.dto.QuestionResponseDTO;
import dev.odroca.api_provas.dto.QuestionsRequestDTO;
import dev.odroca.api_provas.dto.QuestionsResponseDTO;
import dev.odroca.api_provas.dto.TestResponseDTO;
import dev.odroca.api_provas.entity.OptionEntity;
import dev.odroca.api_provas.entity.QuestionEntity;
import dev.odroca.api_provas.entity.TestEntity;
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

    public TestResponseDTO createTest(TestEntity test) {

        TestEntity saved = testRepository.save(test);
        
        TestResponseDTO response = new TestResponseDTO();

        response.setTestId(saved.getId());
        response.setName(saved.getName());

        return response;
    }

    public QuestionResponseDTO createQuestion(UUID testId, QuestionModelDTO questionModel) {

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
        
        return new QuestionResponseDTO(
            saved.getId(),
            saved.getQuestion(),
            saved.getOptions().size(),
            correctionOptionId,
            "Questão criada com sucesso!"
        );
    }
    
    public QuestionsResponseDTO createQuestions(QuestionsRequestDTO questionsModel) {
        int totalQuestions = 0;

        // Transforma cada questão em uma entidade QuestionModelDTO
        for (QuestionModelDTO question : questionsModel.getQuestions()) {
            createQuestion(questionsModel.getTestId(), question);
            totalQuestions++;
        }

        return new QuestionsResponseDTO(questionsModel.getTestId(), totalQuestions, "Questões criadas com sucesso!");
    }

}
