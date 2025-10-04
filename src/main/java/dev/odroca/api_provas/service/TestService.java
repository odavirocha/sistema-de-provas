package dev.odroca.api_provas.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.odroca.api_provas.dto.test.CreateTestResponseDTO;
import dev.odroca.api_provas.dto.test.DeleteTestResponseDTO;
import dev.odroca.api_provas.entity.TestEntity;
import dev.odroca.api_provas.exception.TestNotFoundException;
import dev.odroca.api_provas.repository.TestRepository;

@Service
@Transactional(readOnly = true)
public class TestService {

    @Autowired
    private TestRepository testRepository;

    @Transactional
    public CreateTestResponseDTO createTest(TestEntity test) {
        
        if (test.getName() == null) 
        
        TestEntity saved = testRepository.save(test);
        
        return new CreateTestResponseDTO(saved.getId(), saved.getName());
    }

    @Transactional
    public DeleteTestResponseDTO deleteTest(UUID id) {

        if (!testRepository.existsById(id)) throw new TestNotFoundException(id);

        testRepository.deleteById(id);

        return new DeleteTestResponseDTO(id, "Prova deletada com sucesso!");
    }
    
}
