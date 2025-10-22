package dev.odroca.api_provas.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.swing.event.ListDataEvent;

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

        // Score com total de questões que existem, 100%
        int score = databaseQuestions.size();

        List<QuestionResultModelDTO> questions = new ArrayList<>();

        // IDs de questões do Request
        Set<UUID> requestQuestionsId = requestQuestions.stream()
            .map(question -> question.getQuestionId())
            .collect(Collectors.toSet());
        
        // Questões que existem no banco de dados e no request
        List<QuestionEntity> questionsAvailable = databaseQuestions.stream()
            .filter(question -> requestQuestionsId.contains(question.getId()))
            .collect(Collectors.toList());

        // Questões que não existem no request, mas existem no banco de dados
        List<QuestionEntity> questionsWrong = databaseQuestions.stream()
            .filter(question -> !requestQuestionsId.contains(question.getId()))
            .collect(Collectors.toList());
            
        // Diminui o score com base nas respostas erradas
        score -= questionsWrong.size();

        for (QuestionAnswerModelDTO requestQuestion : requestQuestions) {
            for (QuestionEntity databaseQuestion : questionsAvailable) {
                if (requestQuestion.getQuestionId().equals(databaseQuestion.getId())) {
                    
                    UUID correctOption = databaseQuestion.getOptions().stream()
                    .filter(option -> option.getIsCorrect())
                    .map(option -> option.getId())
                    .findFirst().orElse(null);

                    QuestionResultModelDTO question = new QuestionResultModelDTO();

                    question.setQuestionId(requestQuestion.getQuestionId());
                    question.setSelectedOptionId(requestQuestion.getSelectedOptionId());
                    question.setCorrectOptionId(correctOption);

                    if (requestQuestion.getSelectedOptionId().equals(correctOption)) {
                        question.setIsCorrect(true);
                    } else {
                        question.setIsCorrect(false);
                        score--;
                    }

                    questions.add(question);

                }
            }
        }

        for (QuestionEntity wrongQuestion : questionsWrong) {
            for (QuestionEntity databaseQuestion : questionsAvailable) {

                UUID correctOption = databaseQuestion.getOptions().stream()
                .filter(option -> option.getIsCorrect())
                .map(option -> option.getId())
                .findFirst().orElse(null);
                
                QuestionResultModelDTO question = new QuestionResultModelDTO();

                question.setQuestionId(wrongQuestion.getId());
                question.setSelectedOptionId(null);
                question.setCorrectOptionId(correctOption);
                question.setIsCorrect(false);
                
                questions.add(question);
            }
        }
 
        return new AnswerTestResponseDTO(score, questions, "!");
    }
    
}
