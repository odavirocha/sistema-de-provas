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
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import dev.odroca.api_provas.dto.question.QuestionAnswerModelDTO;
import dev.odroca.api_provas.dto.question.QuestionResultModelDTO;
import dev.odroca.api_provas.dto.test.AnswerTestRequestDTO;
import dev.odroca.api_provas.dto.test.AnswerTestResponseDTO;
import dev.odroca.api_provas.entity.OptionEntity;
import dev.odroca.api_provas.entity.QuestionEntity;
import dev.odroca.api_provas.entity.UserEntity;
import dev.odroca.api_provas.exception.InvalidAttributeException;
import dev.odroca.api_provas.exception.OptionNotFoundException;
import dev.odroca.api_provas.exception.UserNotFoundException;
import dev.odroca.api_provas.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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
    @DisplayName("Teste sem questão respondida")
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
    @DisplayName("Deve retornar InvalidAttributeException quando questionId for nulo")
    void answerTestInvalidAttributeExceptionForQuestionTest() {
        UUID testId = UUID.fromString("e4d7425c-d89b-4483-b0a8-e53ade738603");
        QuestionAnswerModelDTO nullQuestion = new QuestionAnswerModelDTO(null, UUID.fromString("de823a13-3c12-4cf7-b266-3d16abe98c94"));
        AnswerTestRequestDTO requestTest = new AnswerTestRequestDTO(List.of(nullQuestion));

        assertThrows(InvalidAttributeException.class, () -> {
            testService.answerTest(testId, requestTest);
        });
    }

    @Test
    @DisplayName("Deve retornar InvalidAttributeException quando selectedOptionId for nulo")
    void answerTestInvalidAttributeExceptionForOptionTest() {
        UUID testId = UUID.fromString("e4d7425c-d89b-4483-b0a8-e53ade738603");
        QuestionAnswerModelDTO nullOption = new QuestionAnswerModelDTO(UUID.fromString("de823a13-3c12-4cf7-b266-3d16abe98c94"), null);
        AnswerTestRequestDTO requestTest = new AnswerTestRequestDTO(List.of(nullOption));

        assertThrows(InvalidAttributeException.class, () -> {
            testService.answerTest(testId, requestTest);
        });
    }

    @Test
    @DisplayName("Deve retornar TestNotFoundException quando não achar a prova")
    void answerTestTestNotFoundExceptionTest() {
        UUID testId = UUID.fromString("e4d7425c-d89b-4483-b0a8-e53ade738603");
        UserEntity user = UserFactory.buildUserEntity();
        TestEntity testEntity = TestFactory.buildTestEntity(user, testId);
        AnswerTestRequestDTO requestTest = TestFactory.buildRequestTest(testEntity);

        when(testRepository.findByIdWithQuestionsAndOptions(testId)).thenReturn(Optional.empty());

        assertThrows(TestNotFoundException.class, () -> {
            testService.answerTest(testId, requestTest);
        });
    }

}
