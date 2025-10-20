package dev.odroca.api_provas.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.odroca.api_provas.dto.question.QuestionAnswerModelDTO;
import dev.odroca.api_provas.dto.question.QuestionResultModelDTO;
import dev.odroca.api_provas.dto.test.AnswerTestRequestDTO;
import dev.odroca.api_provas.dto.test.AnswerTestResponseDTO;
import dev.odroca.api_provas.dto.test.CreateTestResponseDTO;
import dev.odroca.api_provas.dto.test.DeleteTestResponseDTO;
import dev.odroca.api_provas.entity.OptionEntity;
import dev.odroca.api_provas.entity.QuestionEntity;
import dev.odroca.api_provas.entity.TestEntity;
import dev.odroca.api_provas.exception.QuestionAnswerValidationException;
import dev.odroca.api_provas.exception.TestNotFoundException;
import dev.odroca.api_provas.repository.TestRepository;

@Service
@Transactional(readOnly = true)
public class TestService {

    @Autowired
    private TestRepository testRepository;

    @Transactional
    public CreateTestResponseDTO createTest(TestEntity test) {
        
        TestEntity saved = testRepository.save(test);
        
        return new CreateTestResponseDTO(saved.getId(), saved.getName());
    }

    @Transactional
    public DeleteTestResponseDTO deleteTest(UUID id) {
        
        if (!testRepository.existsById(id)) throw new TestNotFoundException(id);

        testRepository.deleteById(id);

        return new DeleteTestResponseDTO(id.toString(), "Prova deletada com sucesso!");
    }
    
    @Transactional
    public AnswerTestResponseDTO answerTest(UUID testId, AnswerTestRequestDTO test) {

        TestEntity databaseTest = testRepository.findById(testId).orElseThrow(() -> new TestNotFoundException(testId));
        
        List<QuestionEntity> databaseQuestions = databaseTest.getQuestions();
        List<QuestionAnswerModelDTO> requestQuestions = test.getQuestions();

        int score = databaseQuestions.size();

        List<QuestionResultModelDTO> questions = new ArrayList<>();

        for (int i = 0; i < databaseQuestions.size(); i++) {

            QuestionResultModelDTO question = new QuestionResultModelDTO();

            // Cada questão que veio do request
            // Tem somente o questionId e selectedOptionId
            QuestionAnswerModelDTO requestQuestion = requestQuestions.get(i);
            QuestionEntity databaseQuestion = databaseQuestions.get(i);

            question.setQuestionId(requestQuestion.getQuestionId());
            question.setSelectedOptionId(requestQuestion.getSelectedOptionId());

            // Buscar o correctOptionId da questionId
            UUID correctOptionId = databaseQuestion.getOptions().stream()
                    .filter(option -> option.getIsCorrect())
                    .map(option -> option.getId())
                    .findFirst()
                    .orElseThrow(() -> new QuestionAnswerValidationException(databaseQuestion.getId()));
            
            question.setCorrectOptionId(correctOptionId);

            if (requestQuestion.getSelectedOptionId().equals(correctOptionId)) {
                question.setIsCorrect(true);
            } else {
                question.setIsCorrect(false);
                score--;
            }

            questions.add(question);
            
        }

 
        return new AnswerTestResponseDTO(score, questions, "!");
    }
    
}
