package dev.odroca.api_provas.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

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
import dev.odroca.api_provas.exception.TestNotFoundException;
import dev.odroca.api_provas.exception.UnauthorizedException;
import dev.odroca.api_provas.exception.UserNotFoundException;
import dev.odroca.api_provas.mapper.TestMapper;
import dev.odroca.api_provas.model.TestModelDTO;
import dev.odroca.api_provas.repository.TestRepository;
import dev.odroca.api_provas.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@Service
@Transactional(readOnly = true)
public class TestService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TestRepository testRepository;
    @Autowired
    private TestMapper testMapper;
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

        TestEntity databaseTest = testRepository.findById(testId).orElseThrow(() -> new TestNotFoundException(testId));
        
        List<QuestionEntity> databaseQuestions = databaseTest.getQuestions();
        List<QuestionAnswerModelDTO> requestQuestions = test.getQuestions();

        int score = databaseQuestions.size();

        List<QuestionResultModelDTO> questions = new ArrayList<>();

        Set<UUID> requestQuestionsId = requestQuestions.stream()
            .map(question -> question.getQuestionId())
            .collect(Collectors.toSet());
        
        List<QuestionEntity> questionsAvailable = databaseQuestions.stream()
            .filter(question -> requestQuestionsId.contains(question.getId()))
            .collect(Collectors.toList());

        List<QuestionEntity> questionsWrong = databaseQuestions.stream()
            .filter(question -> !requestQuestionsId.contains(question.getId()))
            .collect(Collectors.toList());
            
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
                    break;
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
                break;
            }
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
                    System.out.println("Cookie: " + cookie.getName() + " " + cookie.getValue());
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
