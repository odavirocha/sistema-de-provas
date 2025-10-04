package dev.odroca.api_provas.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.odroca.api_provas.dto.test.CreateTestResponseDTO;
import dev.odroca.api_provas.entity.TestEntity;
import dev.odroca.api_provas.exception.TestNullNameException;
import dev.odroca.api_provas.repository.TestRepository;

@ExtendWith(MockitoExtension.class)
public class TestServiceTest {

    @Mock
    private TestRepository testRepository;

    @InjectMocks
    private TestService testService;
    
    @Test
    @DisplayName("Should create test successfully when everything is OK")
    void testCreateTest() {

        // Entidade do controller
        TestEntity entity = new TestEntity();
        entity.setName("Prova de teste 1");
        when(testRepository.save(any(TestEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CreateTestResponseDTO result = testService.createTest(entity);

        assertNotNull(result);
        assertEquals("Prova de teste 1", result.getName());
        verify(testRepository, times(1)).save(any(TestEntity.class));

    }

}
