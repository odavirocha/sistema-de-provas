package dev.odroca.api_provas.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import dev.odroca.api_provas.entity.OptionEntity;
import dev.odroca.api_provas.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.odroca.api_provas.dto.question.QuestionAnswerModelDTO;
import dev.odroca.api_provas.dto.question.QuestionResultModelDTO;
import dev.odroca.api_provas.dto.test.AnswerTestRequestDTO;
import dev.odroca.api_provas.dto.test.AnswerTestResponseDTO;
import dev.odroca.api_provas.dto.test.DeleteTestResponseDTO;
import dev.odroca.api_provas.dto.test.TestResponseDTO;
import dev.odroca.api_provas.entity.QuestionEntity;
import dev.odroca.api_provas.entity.TestEntity;
import dev.odroca.api_provas.repository.TestRepository;
import dev.odroca.api_provas.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional(readOnly = true)
@Slf4j
public class TestService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TestRepository testRepository;
    @Autowired
    private JwtDecoder jwtDecoder;


    @Transactional
    public TestResponseDTO createTest(TestEntity test) {
        TestEntity saved = testRepository.save(test);
        
        int totalQuestions = saved.getQuestions().size();

        return new TestResponseDTO(saved.getId(), saved.getName(), totalQuestions);
    }

    @Transactional
    public DeleteTestResponseDTO deleteTest(UUID id) {
        
        if (!testRepository.existsById(id)) throw new TestNotFoundException(id);

        testRepository.deleteById(id);

        return new DeleteTestResponseDTO(id.toString(), "Prova deletada com sucesso!");
    }
    
    @Transactional
    public AnswerTestResponseDTO answerTest(UUID testId, AnswerTestRequestDTO test) {
        test.questions().forEach(question -> {
            if (question.questionId() == null) throw new InvalidAttributeException("ID da questão inválido.");
            if (question.selectedOptionId() == null) throw new InvalidAttributeException("ID da opção inválido.");
        });

        TestEntity databaseTest = testRepository.findByIdWithQuestionsAndOptions(testId).orElseThrow(() -> new TestNotFoundException(testId));
        
        Set<QuestionEntity> databaseQuestions = databaseTest.getQuestions();
        List<QuestionAnswerModelDTO> requestQuestions = test.questions();

        int score = databaseQuestions.size();

        List<QuestionResultModelDTO> questions = new ArrayList<>();

        Set<UUID> requestQuestionsId = requestQuestions.stream()
            .map(question -> question.questionId())
            .collect(Collectors.toSet());
        
        List<QuestionEntity> questionsAvailable = databaseQuestions.stream()
            .filter(question -> requestQuestionsId.contains(question.getId()))
            .collect(Collectors.toList());

//      Perguntas não respondidas (não foram enviadas)
        List<QuestionEntity> wrongQuestions = databaseQuestions.stream()
            .filter(question -> !requestQuestionsId.contains(question.getId()))
            .collect(Collectors.toList());
            
        score -= wrongQuestions.size();

        for (QuestionAnswerModelDTO requestQuestion : requestQuestions) {
            for (QuestionEntity databaseQuestion : questionsAvailable) {
                if (requestQuestion.questionId().equals(databaseQuestion.getId())) {
                    
                    UUID correctOption = databaseQuestion.getOptions().stream()
                    .filter(option -> option.getIsCorrect())
                    .map(option -> option.getId())
                    .findFirst().orElse(null);

                    if (requestQuestion.selectedOptionId().equals(correctOption)) {
                        QuestionResultModelDTO question = new QuestionResultModelDTO(
                        requestQuestion.questionId(),
                        requestQuestion.selectedOptionId(),
                        correctOption,
                        true);
                        questions.add(question);
                        break;
                    }

                    QuestionResultModelDTO question = new QuestionResultModelDTO(
                    requestQuestion.questionId(),
                    requestQuestion.selectedOptionId(),
                    correctOption,
                    false);
                    questions.add(question);
                    score--;
                    break;
                }
            }
        }

        for (QuestionEntity wrongQuestion : wrongQuestions) {
            UUID correctId = wrongQuestion.getOptions().stream().filter(OptionEntity::getIsCorrect)
            .map(OptionEntity::getId).findFirst().orElseThrow(() -> new OptionNotFoundException("Nenhuma opção correta encontrada para a questão: " + wrongQuestion.getId()));
            QuestionResultModelDTO question = new QuestionResultModelDTO(
                wrongQuestion.getId(),
                null,
                correctId,
                false
            );
            questions.add(question);
        }

        return new AnswerTestResponseDTO(score, questions, "Prova finalizada.");
    }
    
    @Transactional
    public List<TestResponseDTO> getAllTestsForUser(UUID userId, HttpServletRequest request) {

        Cookie[] cookies = request.getCookies();
        
        UUID userIdFromJwt = null;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("accessToken".equals(cookie.getName())) {
                    userIdFromJwt = UUID.fromString(jwtDecoder.decode(cookie.getValue()).getSubject());
                    break;
                }
            }
        }

        if (!userId.equals(userIdFromJwt)) throw new UnauthorizedException();

        userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException());

        List<TestEntity> testEntities = testRepository.findAllByUserId(userId);

        List<TestResponseDTO> response = testEntities.stream()
            .map(testEntity -> new TestResponseDTO(testEntity.getId(), testEntity.getName(), testEntity.getQuestions().size()))
            .collect(Collectors.toList());

        return  response; 
    }
    
}
