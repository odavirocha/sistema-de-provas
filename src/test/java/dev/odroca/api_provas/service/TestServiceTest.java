package dev.odroca.api_provas.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import dev.odroca.api_provas.dto.question.QuestionAnswerModelDTO;
import dev.odroca.api_provas.dto.test.AnswerTestRequestDTO;
import dev.odroca.api_provas.dto.test.AnswerTestResponseDTO;
import dev.odroca.api_provas.entity.UserEntity;
import dev.odroca.api_provas.exception.InvalidAttributeException;
import dev.odroca.api_provas.exception.OptionNotFoundException;
import dev.odroca.api_provas.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.util.ReflectionTestUtils;

import dev.odroca.api_provas.dto.test.TestResponseDTO;
import dev.odroca.api_provas.dto.test.DeleteTestResponseDTO;
import dev.odroca.api_provas.entity.TestEntity;
import dev.odroca.api_provas.exception.TestNotFoundException;
import dev.odroca.api_provas.repository.TestRepository;

@ExtendWith(MockitoExtension.class)
public class TestServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TestRepository testRepository;

    @Mock
    private JwtDecoder jwtDecoder;

    @InjectMocks
    private TestService testService;
    
    @Test
    @DisplayName("Deve criar uma prova.")
    void testCreateSuccessTest() {
        
        // Entidade criada no controller
        TestEntity controllerEntity = new TestEntity();
        controllerEntity.setName("Prova de teste 1");
        
        // Entidade criada no service após salvar -- que vem como retorno do .save()
        TestEntity serviceEntity = new TestEntity();
        serviceEntity.setName(controllerEntity.getName());
        ReflectionTestUtils.setField(serviceEntity, "id", UUID.fromString("a35a647b-6a7d-4cdc-b92e-87c5be376ee7"));

        when(testRepository.save(any(TestEntity.class))).thenReturn(serviceEntity);
        TestResponseDTO result = testService.createTest(controllerEntity);

        assertNotNull(result);
        assertNotNull(result.getTestId());
        assertNotNull(result.getName());
        assertEquals(UUID.fromString("a35a647b-6a7d-4cdc-b92e-87c5be376ee7"), result.getTestId());
        assertEquals("Prova de teste 1", result.getName());

        verify(testRepository, times(1)).save(controllerEntity);

    }

    @Test
    @DisplayName("Deve deletar uma prova.")
    void testDeleteSuccessTest() {

        UUID id = UUID.fromString("a35a647b-6a7d-4cdc-b92e-87c5be376ee7");
        when(testRepository.existsById(id)).thenReturn(true);

        DeleteTestResponseDTO result = testService.deleteTest(id);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(id.toString(), result.getId()); // Deve ser string para serializar no JSON
        assertEquals("Prova deletada com sucesso!", result.getMessage());

        verify(testRepository, times(1)).existsById(id);
        verify(testRepository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("Deve retornar TestNotFoundException quando não achar a prova.")
    void testDeleteTestNotFoundExceptionTest() {
        UUID id = UUID.fromString("a35a647b-6a7d-4cdc-b92e-87c5be376ee7");
        when(testRepository.existsById(id)).thenReturn(false);

        assertThrows(TestNotFoundException.class, () -> testService.deleteTest(id));

        verify(testRepository, times(1)).existsById(id);
        verify(testRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("Deve corrigir uma prova.")
    void answerTestSuccessTest() {
        UserEntity user = UserFactory.buildUserEntity();
        UUID testId = UUID.fromString("e4d7425c-d89b-4483-b0a8-e53ade738603");
        TestEntity testEntity = TestFactory.buildTestEntity(user, testId);

        AnswerTestRequestDTO requestTest = TestFactory.buildRequestTest(testEntity);

        when(testRepository.findByIdWithQuestionsAndOptions(testId)).thenReturn(Optional.of(testEntity));

        AnswerTestResponseDTO response = testService.answerTest(testId, requestTest);

        assertEquals(3, response.getQuestions().size());
        assertEquals(3, response.getScore());
        assertEquals( "Prova finalizada.", response.getMessage());
    }

    @Test
    @DisplayName("Deve corrigir uma prova, com uma questão não respondida")
    void answerTestWithoutOneQuestionTest() {
        UserEntity user = UserFactory.buildUserEntity();
        UUID testId = UUID.fromString("e4d7425c-d89b-4483-b0a8-e53ade738603");
        TestEntity testEntity = TestFactory.buildTestEntity(user, testId);

        AnswerTestRequestDTO requestTest = TestFactory.buildRequestTestWithoutOneQuestion(testEntity);

        when(testRepository.findByIdWithQuestionsAndOptions(testId)).thenReturn(Optional.of(testEntity));

        AnswerTestResponseDTO response = testService.answerTest(testId, requestTest);

        assertEquals(2, response.getScore());
        assertEquals( "Prova finalizada.", response.getMessage());
    }

    @Test
    @DisplayName("Deve corrigir uma prova, com uma questão errada.")
    void answerTestWithWrongQuestionTest() {
        UUID testId = UUID.fromString("e4d7425c-d89b-4483-b0a8-e53ade738603");
        UserEntity user = UserFactory.buildUserEntity();
        TestEntity testEntity = TestFactory.buildTestEntity(user, testId);
        AnswerTestRequestDTO requestTest = TestFactory.buildRequestTestWithOneWrongQuestion(testEntity);

        when(testRepository.findByIdWithQuestionsAndOptions(testId)).thenReturn(Optional.of(testEntity));

        AnswerTestResponseDTO response = testService.answerTest(testId, requestTest);

        assertEquals(2, response.getScore());
        assertEquals("Prova finalizada.", response.getMessage());
    }

    @Test
    @DisplayName("Deve corrigir uma prova, com questionId que não existem.")
    void answerTestWhereAllQuestionsDoNotExistTest() {
        UUID testId = UUID.fromString("e4d7425c-d89b-4483-b0a8-e53ade738603");
        UserEntity user = UserFactory.buildUserEntity();
        TestEntity test = TestFactory.buildTestEntity(user, testId);
        AnswerTestRequestDTO requestTest = TestFactory.buildRequestTestWhereAllQuestionsDoNotExist(test);

        when(testRepository.findByIdWithQuestionsAndOptions(testId)).thenReturn(Optional.of(test));

        AnswerTestResponseDTO response = testService.answerTest(testId, requestTest);

        assertEquals(0, response.getScore());
    }

    @Test
    @DisplayName("Deve retornar InvalidAttributeException quando questionId for nulo")
    void answerTestInvalidAttributeExceptionForQuestionTest() {
        UUID testId = UUID.fromString("e4d7425c-d89b-4483-b0a8-e53ade738603");
        QuestionAnswerModelDTO nullQuestion = new QuestionAnswerModelDTO(null, UUID.fromString("de823a13-3c12-4cf7-b266-3d16abe98c94"));
        AnswerTestRequestDTO requestTest = new AnswerTestRequestDTO(List.of(nullQuestion));

        assertThrows(InvalidAttributeException.class, () -> testService.answerTest(testId, requestTest));
    }

    @Test
    @DisplayName("Deve retornar OptionNotFoundException quando não houver nenhuma opção correta para uma questão enviada")
    void answerTestOptionNotFoundExceptionSentQuestionTest() {
        UUID testId = UUID.fromString("e4d7425c-d89b-4483-b0a8-e53ade738603");
        UserEntity user = UserFactory.buildUserEntity();
        TestEntity test = TestFactory.buildTestEntityWithoutOptionCorrect(user, testId);
        AnswerTestRequestDTO requestTest = TestFactory.buildRequestTestWithoutOptionCorrect(test);

        when(testRepository.findByIdWithQuestionsAndOptions(testId)).thenReturn(Optional.of(test));

        assertThrows(OptionNotFoundException.class, () -> testService.answerTest(testId, requestTest));
    }

    @Test
    @DisplayName("Deve retornar OptionNotFoundException quando não houver nenhuma opção correta para uma questão não enviada")
    void answerTestOptionNotFoundExceptionNoSentQuestionTest() {
        UUID testId = UUID.fromString("e4d7425c-d89b-4483-b0a8-e53ade738603");
        UserEntity user = UserFactory.buildUserEntity();
        TestEntity test = TestFactory.buildTestEntityWithoutOptionCorrectNoSentQuestion(user, testId);
        AnswerTestRequestDTO requestTest = TestFactory.buildRequestTestWithoutOptionCorrectForQuestionNoSubmitted(test);

        when(testRepository.findByIdWithQuestionsAndOptions(testId)).thenReturn(Optional.of(test));

        assertThrows(OptionNotFoundException.class, () -> testService.answerTest(testId, requestTest));
    }

    @Test
    @DisplayName("Deve retornar InvalidAttributeException quando selectedOptionId for nulo")
    void answerTestInvalidAttributeExceptionForOptionTest() {
        UUID testId = UUID.fromString("e4d7425c-d89b-4483-b0a8-e53ade738603");
        QuestionAnswerModelDTO nullOption = new QuestionAnswerModelDTO(UUID.fromString("de823a13-3c12-4cf7-b266-3d16abe98c94"), null);
        AnswerTestRequestDTO requestTest = new AnswerTestRequestDTO(List.of(nullOption));

        assertThrows(InvalidAttributeException.class, () -> testService.answerTest(testId, requestTest));
    }

    @Test
    @DisplayName("Deve retornar TestNotFoundException quando não achar a prova")
    void answerTestTestNotFoundExceptionTest() {
        UUID testId = UUID.fromString("e4d7425c-d89b-4483-b0a8-e53ade738603");
        UserEntity user = UserFactory.buildUserEntity();
        TestEntity testEntity = TestFactory.buildTestEntity(user, testId);
        AnswerTestRequestDTO requestTest = TestFactory.buildRequestTest(testEntity);

        when(testRepository.findByIdWithQuestionsAndOptions(testId)).thenReturn(Optional.empty());

        assertThrows(TestNotFoundException.class, () -> testService.answerTest(testId, requestTest));
    }

    @Test
    @DisplayName("Deve retornar todas as provas de um usuário")
    void getAllTestsForUserSuccessTest() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        String cookieValue = "eyJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJodHRwczovL2xvY2FsaG9zdDo4MDgwL2F1dGgvbG9naW4iLCJzdWIiOiIzOTdlZWM3MS0zNjBkLTQ2MjYtODA1Yy02NjQwYmU2MzU2OTAiLCJyb2xlIjpbIlVTRVIiXSwiZXhwIjoxNzc0NzQ3MzQwLCJpYXQiOjE3NzQ3NDcwNDB9.WjFyEZPwc_gWBvfM7mtKZFRxvRG93ay3WLvZcfhUUQqLc3QcZAgtQGH4R4u_CZ_wnGeYlY3GT1rVBiFXRkhBKmn9eYzzll5imWXUtJGfcXum4x8CmQUZSGUTywXtEIWSbv31-11q9zDFtMuAMXsHOMBvnaKqTrDBYzDhpIshQFN41NdOdKCCu3CgAKWDebX9zTk6T70n4J85X5MloTrn6bbWl_EGLpRk-NqbTYIsv7Jreop7MouoeOaJmmfiIR5M2363KcVEowKFkncBIlYf_OK_kd2Jkq9AKgUZMglpwRYIHHPCVuKdd4MtFhs5hPyMh-NJcFwonB89oA0ZdMUbtg";
        request.setCookies(new Cookie("accessToken", cookieValue));

        UUID userId = UUID.fromString("397eec71-360d-4626-805c-6640be635690");
        UserEntity userEntity = UserFactory.buildUserEntity();

        UUID testId = UUID.fromString("e4d7425c-d89b-4483-b0a8-e53ade738603");
        List<TestEntity> testEntities = List.of(
            TestFactory.buildTestEntity(userEntity, testId)
        );

        Jwt jwt = Jwt.withTokenValue("accessToken")
        .header("alg", "RS256")
        .subject(userId.toString())
        .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
        when(testRepository.findAllByUserId(userId)).thenReturn(testEntities);
        when(jwtDecoder.decode(cookieValue)).thenReturn(jwt);

        List<TestResponseDTO> response = testService.getAllTestsForUser(userId, request);

        assertEquals(testEntities.get(0).getId(), response.get(0).getTestId());
        assertEquals(testEntities.get(0).getName(), response.get(0).getName());
        assertEquals(testEntities.get(0).getQuestions().size(), response.get(0).getTotalQuestions());
    }

}
