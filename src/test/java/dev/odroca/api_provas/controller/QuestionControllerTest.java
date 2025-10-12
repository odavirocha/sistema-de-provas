package dev.odroca.api_provas.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import dev.odroca.api_provas.dto.option.CreateOptionModelDTO;
import dev.odroca.api_provas.dto.question.CreateQuestionModelDTO;
import dev.odroca.api_provas.dto.question.CreateQuestionResponseDTO;
import dev.odroca.api_provas.dto.questions.CreateQuestionsRequestDTO;
import dev.odroca.api_provas.dto.questions.CreateQuestionsResponseDTO;
import dev.odroca.api_provas.entity.OptionEntity;
import dev.odroca.api_provas.entity.QuestionEntity;
import dev.odroca.api_provas.entity.TestEntity;
import dev.odroca.api_provas.exception.CorrectOptionNotFoundException;
import dev.odroca.api_provas.exception.TestNotFoundException;
import dev.odroca.api_provas.mapper.OptionMapper;
import dev.odroca.api_provas.repository.QuestionRepository;
import dev.odroca.api_provas.repository.TestRepository;
import dev.odroca.api_provas.service.QuestionService;

@ExtendWith(MockitoExtension.class)
public class QuestionControllerTest {

    @Spy
    @InjectMocks
    QuestionService questionService;

    @Mock
    TestRepository testRepository;

    @Mock
    QuestionRepository questionRepository;

    @Mock
    OptionMapper optionMapper;

    @Test
    @DisplayName("Deve criar uma questão quando tudo estiver OK.")
    void testCreateQuestionSuccessful() {

        UUID testId = UUID.fromString("5e6863bc-4f69-4a95-b672-c41296ec95a2");
        List<CreateOptionModelDTO> options = new ArrayList<>();
        CreateQuestionModelDTO question = new CreateQuestionModelDTO("Qual é a soma de 2+2?", options);
        
        for (Integer i = 0; i < 5; i++) {
            Boolean isCorrect = (i == 4);
            options.add(new CreateOptionModelDTO(i.toString(), isCorrect));
        }

        TestEntity testEntity = new TestEntity();
        when(testRepository.findById(testId)).thenReturn(Optional.of(testEntity));

        List<OptionEntity> optionEntities = options.stream().map(option -> {
            OptionEntity optionEntity = new OptionEntity();
            ReflectionTestUtils.setField(optionEntity, "id", UUID.randomUUID());
            optionEntity.setValue(option.getValue());
            optionEntity.setIsCorrect(option.getIsCorrect());
            return optionEntity;
        }).collect(Collectors.toList());
        when(optionMapper.toEntityList(options)).thenReturn(optionEntities);

        QuestionEntity questionEntity = new QuestionEntity();
        ReflectionTestUtils.setField(questionEntity, "id", testId);
        questionEntity.setQuestion("Qual é a soma de 2+2?");
        questionEntity.setOptions(optionEntities);
        when(questionRepository.save(any(QuestionEntity.class))).thenReturn(questionEntity);

        CreateQuestionResponseDTO result = questionService.createQuestion(testId, question);

        assertNotNull(result);
        assertEquals(questionEntity.getQuestion(), result.getQuestion());
        assertEquals(optionEntities.size(), result.getTotalOptions());
        verify(testRepository).findById(testId);
        verify(optionMapper).toEntityList(options);
        verify(questionRepository).save(any(QuestionEntity.class));

    }

    @Test
    @DisplayName("Deve retornar TestNotFoundException quando não achar o ID da prova no banco de dados.")
    void testCreateQuestionTestNotFoundException() {
        
        UUID testId = UUID.fromString("5e6863bc-4f69-4a95-b672-c41296ec95a2");
        List<CreateOptionModelDTO> options = new ArrayList<>();
        
        for (Integer i = 0; i < 5; i++) {
            Boolean isCorrect = (i == 4);
            options.add(new CreateOptionModelDTO(i.toString(), isCorrect));
        }

        CreateQuestionModelDTO question = new CreateQuestionModelDTO("Qual é a soma de 2+2", options);

        when(testRepository.findById(testId)).thenReturn(Optional.empty());

        assertThrows(TestNotFoundException.class, () -> {
            questionService.createQuestion(testId, question);
        });
        
        verify(testRepository).findById(testId);
        verifyNoMoreInteractions(optionMapper);
        verifyNoMoreInteractions(questionRepository);
    }

    @Test
    @DisplayName("Deve retornar CorrectOptionNotFoundException quando não tiver uma resposta correta.")
    void testCreateQuestionCorrectOptionNotFoundException() {

        UUID testId = UUID.fromString("5e6863bc-4f69-4a95-b672-c41296ec95a2");
        List<CreateOptionModelDTO> options = new ArrayList<>();
        
        for (Integer i = 0; i < 5; i++) {
            options.add(new CreateOptionModelDTO(i.toString(), false));
        }

        CreateQuestionModelDTO question = new CreateQuestionModelDTO("Qual é a soma de 2+2?", options);

        TestEntity testEntity = new TestEntity();
        when(testRepository.findById(testId)).thenReturn(Optional.of(testEntity));

        List<OptionEntity> optionEntities = options.stream().map(option -> {
            OptionEntity optionEntity = new OptionEntity();
            ReflectionTestUtils.setField(optionEntity, "id", UUID.randomUUID());
            optionEntity.setValue(option.getValue());
            optionEntity.setIsCorrect(false);
            return optionEntity;
        }).collect(Collectors.toList());
        when(optionMapper.toEntityList(options)).thenReturn(optionEntities);
        
        assertThrows(CorrectOptionNotFoundException.class, () -> {
            questionService.createQuestion(testId, question);
        });
        
        verify(testRepository).findById(testId);
        verify(optionMapper).toEntityList(options);
        verifyNoInteractions(questionRepository);
    }

    @Test
    @DisplayName("Deve criar duas questões quando tudo estiver OK")
    void testCreateQuestionsSuccessful() {
        int totalQuestions = 2;
        UUID testId = UUID.fromString("5e6863bc-4f69-4a95-b672-c41296ec95a2");

        List<CreateOptionModelDTO> options = new ArrayList<>();
        for (Integer i = 0; i < 5; i++) {
            Boolean isCorrect = (i == 4);
            options.add(new CreateOptionModelDTO(i.toString(), isCorrect));
        }

        List<CreateQuestionModelDTO> questions = new ArrayList<>();
        questions.add(new CreateQuestionModelDTO("1+1?", options));
        questions.add(new CreateQuestionModelDTO("6-4?", options));
        
        CreateQuestionsRequestDTO questionsModel = new CreateQuestionsRequestDTO(testId, questions);

        // Não faço nada com o response de createQuestion
        doReturn(null).when(questionService).createQuestion(any(UUID.class), any(CreateQuestionModelDTO.class));
        CreateQuestionsResponseDTO result = questionService.createQuestions(questionsModel);

        assertNotNull(result);  
        assertNotNull(result.getId());
        assertNotNull(result.getTotalQuestions());
        assertEquals(testId, result.getId());
        assertEquals(totalQuestions, result.getTotalQuestions());

        // Verifica se realmente o looping de createQuestions chama createQuestion 2x
        verify(questionService, times(2)).createQuestion(eq(testId), any(CreateQuestionModelDTO.class));
        
    }

    @Test
    @DisplayName("Deve retornar TestNotFoundException quando não achar o ID da prova no banco de dados.")
    void testCreateQuestionsTestNotFoundException() {

    }

    @Test
    @DisplayName("Deve retornar CorrectOptionNotFoundException quando tiver uma resposta correta.")
    void testCreateQuestionsCorrectOptionNotFoundException() {

        List<CreateOptionModelDTO> options = new ArrayList<>();
        for (Integer i = 0; i < 5; i++) {
            options.add(new CreateOptionModelDTO(i.toString(), false));
        }

    }

    @Test
    void testEditQuestion() {

    }

    @Test
    void testGetAllQuestionsForTest() {

    }

}
