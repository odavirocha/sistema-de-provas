package dev.odroca.api_provas.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.odroca.api_provas.dto.CreateTestResponseDTO;
import dev.odroca.api_provas.entity.TestEntity;
import dev.odroca.api_provas.repository.TestRepository;

@Service
@Transactional(readOnly = true)
public class TestService {
    private final TestRepository testRepository;

    public TestService(TestRepository testRepository) {
        this.testRepository = testRepository;
    }

    @Transactional
    public CreateTestResponseDTO createTest(TestEntity test) {
        
        TestEntity saved = testRepository.save(test);
        
        CreateTestResponseDTO response = new CreateTestResponseDTO();
        
        response.setTestId(saved.getId());
        response.setName(saved.getName());
        
        return response;
    }
    
    

}
