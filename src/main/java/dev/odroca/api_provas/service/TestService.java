package dev.odroca.api_provas.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.odroca.api_provas.dto.question.GetQuestionModelDTO;
import dev.odroca.api_provas.dto.question.QuestionAnswerModelDTO;
import dev.odroca.api_provas.dto.question.QuestionResultModelDTO;
import dev.odroca.api_provas.dto.test.AnswerTestRequestDTO;
import dev.odroca.api_provas.dto.test.AnswerTestResponseDTO;
import dev.odroca.api_provas.dto.test.DeleteTestResponseDTO;
import dev.odroca.api_provas.dto.test.TestForGetTestResponseDTO;
import dev.odroca.api_provas.dto.test.TestForGetTestsResponseDTO;
import dev.odroca.api_provas.entity.QuestionEntity;
import dev.odroca.api_provas.entity.TestEntity;
import dev.odroca.api_provas.exception.TestNotFoundException;
import dev.odroca.api_provas.exception.UnauthorizedException;
import dev.odroca.api_provas.exception.UserNotFoundException;
import dev.odroca.api_provas.mapper.QuestionMapper;
import dev.odroca.api_provas.repository.TestRepository;
import dev.odroca.api_provas.repository.UserRepository;
import dev.odroca.api_provas.utils.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;

@Service
@Transactional(readOnly = true)
public class TestService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TestRepository testRepository;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private CookieUtil cookieUtil;


    @Transactional
    public TestForGetTestsResponseDTO createTest(TestEntity test) {
        TestEntity saved = testRepository.save(test);
        
        int totalQuestions = saved.getQuestions().size();

        return new TestForGetTestsResponseDTO(saved.getId(), saved.getName(), totalQuestions);
    }

    @Transactional
    public DeleteTestResponseDTO deleteTest(UUID testId, HttpServletRequest request) {
        UUID userId = cookieUtil.getUserIdByJWT(request);

        int deleteRows = testRepository.deleteByIdAndUserId(testId, userId);
    
        if (deleteRows == 0) throw new UnauthorizedException();

        return new DeleteTestResponseDTO(testId.toString(), "Prova deletada com sucesso!");
    }
    
    @Transactional
    public AnswerTestResponseDTO answerTest(UUID testId, AnswerTestRequestDTO test) {

        TestEntity databaseTest = testRepository.findById(testId).orElseThrow(() -> new TestNotFoundException(testId));
        
        List<QuestionEntity> databaseQuestions = databaseTest.getQuestions();
        List<QuestionAnswerModelDTO> requestQuestions = test.getQuestions();

        int correctCount = databaseQuestions.size();

        List<QuestionResultModelDTO> questions = new ArrayList<>();

        Set<UUID> requestQuestionsId = requestQuestions.stream()
            .map(question -> question.questionId())
            .collect(Collectors.toSet());
        
        List<QuestionEntity> questionsAvailable = databaseQuestions.stream()
            .filter(question -> requestQuestionsId.contains(question.getId()))
            .collect(Collectors.toList());

        List<QuestionEntity> questionsWrong = databaseQuestions.stream()
            .filter(question -> !requestQuestionsId.contains(question.getId()))
            .collect(Collectors.toList());
            
        correctCount -= questionsWrong.size();

        for (QuestionAnswerModelDTO requestQuestion : requestQuestions) {
            for (QuestionEntity databaseQuestion : questionsAvailable) {
                if (requestQuestion.questionId().equals(databaseQuestion.getId())) {
                    
                    UUID correctOption = databaseQuestion.getOptions().stream()
                    .filter(option -> option.getIsCorrect())
                    .map(option -> option.getId())
                    .findFirst().orElse(null);

                    QuestionResultModelDTO question;

                    if (requestQuestion.selectedOptionId().equals(correctOption)) {
                        question = new QuestionResultModelDTO(
                            requestQuestion.questionId(),
                            requestQuestion.selectedOptionId(),
                            correctOption,
                            true
                        );
                    } else {
                        question = new QuestionResultModelDTO(
                            requestQuestion.questionId(),
                            requestQuestion.selectedOptionId(),
                            correctOption,
                            false
                        );
                        correctCount--;
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
                
                QuestionResultModelDTO question = new QuestionResultModelDTO(
                    wrongQuestion.getId(),
                    null,
                    correctOption,
                    false
                );
                
                questions.add(question);
                break;
            }
        }
 
        return new AnswerTestResponseDTO(questions, "Prova finalizada.", correctCount, databaseQuestions.size()-correctCount);
    }
    
    @Transactional
    public List<TestForGetTestsResponseDTO> getAllTestsForUser(HttpServletRequest request) {
        UUID userId = cookieUtil.getUserIdByJWT(request);

        userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException());

        List<TestEntity> testEntities = testRepository.findAllByUserId(userId);

        List<TestForGetTestsResponseDTO> response = testEntities.stream()
            .map(testEntity -> new TestForGetTestsResponseDTO(testEntity.getId(), testEntity.getName(), testEntity.getQuestions().size()))
            .collect(Collectors.toList());

        return  response; 
    }
    
    @Transactional
    public TestForGetTestResponseDTO getTest(UUID testId, HttpServletRequest request) {
        UUID userId = cookieUtil.getUserIdByJWT(request);

        TestEntity test = testRepository.findById(testId).orElseThrow(() -> new TestNotFoundException(testId));

        if (!test.getUserId().equals(userId)) throw new UnauthorizedException();

        List<GetQuestionModelDTO> questions = questionMapper.toDtoList(test.getQuestions());
        
        return new TestForGetTestResponseDTO(test.getName(), questions);
    }
    
}
