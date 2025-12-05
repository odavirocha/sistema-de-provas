package dev.odroca.api_provas.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import dev.odroca.api_provas.dto.test.DeleteTestResponseDTO;
import dev.odroca.api_provas.dto.test.TestForGetTestsResponseDTO;
import dev.odroca.api_provas.entity.TestEntity;
import dev.odroca.api_provas.exception.TestNotFoundException;
import dev.odroca.api_provas.repository.TestRepository;

@ExtendWith(MockitoExtension.class)
public class TestServiceTest {

    @Mock
    private TestRepository testRepository;
    
    @InjectMocks
    private TestService testService;
    
    @Test
    @DisplayName("Deve criar uma prova quando tudo estiver OK.")
    void testCreateTest() {
        
        // Entidade criada no controller
        TestEntity controllerEntity = new TestEntity();
        controllerEntity.setName("Prova de teste 1");
        
        // Entidade criada no service após salvar -- que vem como retorno do .save()
        TestEntity serviceEntity = new TestEntity();
        serviceEntity.setName(controllerEntity.getName());
        ReflectionTestUtils.setField(serviceEntity, "id", UUID.fromString("a35a647b-6a7d-4cdc-b92e-87c5be376ee7"));

        when(testRepository.save(any(TestEntity.class))).thenReturn(serviceEntity);
        TestForGetTestsResponseDTO result = testService.createTest(controllerEntity);

        assertNotNull(result);
        assertNotNull(result.testId());
        assertNotNull(result.name());
        assertEquals(UUID.fromString("a35a647b-6a7d-4cdc-b92e-87c5be376ee7"), result.testId());
        assertEquals("Prova de teste 1", result.name());

        verify(testRepository, times(1)).save(controllerEntity);

    }

    @Test
    @DisplayName("Deve deletar uma prova quando tudo estiver OK.")
    void testDeleteTest() {

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
    @DisplayName("Espera uma exceção quando não achar a prova.")
    void testDeleteExceptionTest() {

        UUID id = UUID.fromString("a35a647b-6a7d-4cdc-b92e-87c5be376ee7");
        when(testRepository.existsById(id)).thenReturn(false);

        assertThrows(TestNotFoundException.class, () -> testService.deleteTest(id));

        verify(testRepository, times(1)).existsById(id);
        verify(testRepository, never()).deleteById(any());
    }

}
