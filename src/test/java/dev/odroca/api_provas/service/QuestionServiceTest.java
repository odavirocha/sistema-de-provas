package dev.odroca.api_provas.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
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
import dev.odroca.api_provas.dto.option.UpdateOptionModelDTO;
import dev.odroca.api_provas.dto.question.CreateQuestionModelDTO;
import dev.odroca.api_provas.dto.question.CreateQuestionResponseDTO;
import dev.odroca.api_provas.dto.question.UpdateQuestionRequestDTO;
import dev.odroca.api_provas.dto.question.UpdateQuestionResponseDTO;
import dev.odroca.api_provas.dto.questions.CreateQuestionsRequestDTO;
import dev.odroca.api_provas.dto.questions.CreateQuestionsResponseDTO;
import dev.odroca.api_provas.entity.OptionEntity;
import dev.odroca.api_provas.entity.QuestionEntity;
import dev.odroca.api_provas.entity.TestEntity;
import dev.odroca.api_provas.exception.CorrectOptionNotFoundException;
import dev.odroca.api_provas.exception.MultipleCorrectOptionsException;
import dev.odroca.api_provas.exception.TestNotFoundException;
import dev.odroca.api_provas.mapper.OptionMapper;
import dev.odroca.api_provas.repository.QuestionRepository;
import dev.odroca.api_provas.repository.TestRepository;

@ExtendWith(MockitoExtension.class)
public class QuestionServiceTest {

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
    void createQuestionSuccessful() {

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
    void createQuestionTestNotFoundException() {
        
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
    @DisplayName("Deve retornar MultipleCorrectOptionsException quando há mais de uma resposta correta.")
    void createQuestionMultipleCorrectOptionsException() {

        UUID testId = UUID.randomUUID();
        List<CreateOptionModelDTO> options = new ArrayList<>();
        CreateQuestionModelDTO question = new CreateQuestionModelDTO( "Qual é a opção 2?", options);

        options.add(new CreateOptionModelDTO("Opção 1!", false));  
        options.add(new CreateOptionModelDTO("Opção 2!", true));
        options.add(new CreateOptionModelDTO("Opção 3!", true));


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

        assertThrows(MultipleCorrectOptionsException.class, () -> {
            questionService.createQuestion(testId, question);
        });

        verify(testRepository).findById(testId);
        verify(optionMapper).toEntityList(options);
        verifyNoMoreInteractions(questionRepository);
    }

    @Test
    @DisplayName("Deve retornar CorrectOptionNotFoundException quando não tiver uma resposta correta.")
    void createQuestionCorrectOptionNotFoundException() {

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
    void createQuestionsSuccessful() {
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
    void createQuestionsTestNotFoundException() {

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

        assertThrows(TestNotFoundException.class, () -> {
            questionService.createQuestions(questionsModel);
        });

    }

    @Test
    @DisplayName("Deve retornar MultipleCorrectOptionsException quando tiver mais de uma opção correta.")
    void createQuestionsMultipleCorrectOptionsException() {

        UUID testId = UUID.fromString("5e6863bc-4f69-4a95-b672-c41296ec95a2");
        
        List<CreateOptionModelDTO> optionsWrong = new ArrayList<>();
        optionsWrong.add(new CreateOptionModelDTO("Opção 1", true));
        optionsWrong.add(new CreateOptionModelDTO("Opção 2", true));
        optionsWrong.add(new CreateOptionModelDTO("Opção 3", false));
        
        List<CreateOptionModelDTO> optionsCorrect = new ArrayList<>();
        optionsCorrect.add(new CreateOptionModelDTO("Opção 1", true));
        optionsCorrect.add(new CreateOptionModelDTO("Opção 2", false));
        optionsCorrect.add(new CreateOptionModelDTO("Opção 3", false));
        

        List<CreateQuestionModelDTO> questions = new ArrayList<>();
        questions.add(new CreateQuestionModelDTO("Questão 1?", optionsWrong));
        questions.add(new CreateQuestionModelDTO("Questão 2?", optionsCorrect));

        CreateQuestionsRequestDTO questionsModel = new CreateQuestionsRequestDTO(testId, questions);

        assertThrows(MultipleCorrectOptionsException.class, () -> {
            questionService.createQuestions(questionsModel);
        });

    }

    @Test
    @DisplayName("Deve retornar CorrectOptionNotFoundException quando tiver uma resposta correta.")
    void createQuestionsCorrectOptionNotFoundException() {

        UUID testId = UUID.fromString("5e6863bc-4f69-4a95-b672-c41296ec95a2");

        TestEntity testEntity = new TestEntity();
        ReflectionTestUtils.setField(testEntity, "id", testId);
        when(testRepository.findById(testId)).thenReturn(Optional.of(testEntity));
        
        List<CreateOptionModelDTO> options = new ArrayList<>();
        for (Integer i = 0; i < 5; i++) {
            options.add(new CreateOptionModelDTO(i.toString(), false));
        }

        List<CreateQuestionModelDTO> questions = new ArrayList<>();
        questions.add(new CreateQuestionModelDTO("1+1?", options));
        questions.add(new CreateQuestionModelDTO("6-4?", options));

        CreateQuestionsRequestDTO questionsModel = new CreateQuestionsRequestDTO(testId, questions);

        assertThrows(CorrectOptionNotFoundException.class, () -> {
            questionService.createQuestions(questionsModel);
        });
        
    }

    @Test
    @DisplayName("Deve editar a questão quando tudo estiver OK.") 
    void updateQuestionSuccessful() {

        UUID questionId = UUID.fromString("e7baa643-6ee6-4ffc-b41b-4aa248b4c144");

        List<UpdateOptionModelDTO> options = new ArrayList<>();
        for (Integer i = 0; i < 5; i++) {
            Boolean isCorrect = (i == 4);
            UpdateOptionModelDTO optionEntity = new UpdateOptionModelDTO();
            optionEntity.setValue(i.toString());
            optionEntity.setIsCorrect(isCorrect);
            options.add(optionEntity);
        }

        UpdateQuestionRequestDTO question = new UpdateQuestionRequestDTO("2+2?", options);

        QuestionEntity questionEntity = new QuestionEntity();
        when(questionRepository.findById(questionId)).thenReturn(Optional.of(questionEntity));

        QuestionEntity questionUpdate = new QuestionEntity();
        ReflectionTestUtils.setField(questionUpdate, "id", UUID.fromString("e7baa643-6ee6-4ffc-b41b-4aa248b4c144"));
        when(questionRepository.save(any(QuestionEntity.class))).thenReturn(questionUpdate);

        UpdateQuestionResponseDTO result = questionService.updateQuestion(questionId, question);
        assertNotNull(result);
        assertEquals(questionId, result.getQuestionId());
        verify(questionRepository, times(1)).save(questionEntity);
    }

    @Test
    @DisplayName("Deve retornar QuestionNotFoundException quando não achar a resposta que as opções estão atribuidas.") 
    void updateQuestionQuestionNotFoundException() {

    }

    @Test
    @DisplayName("Deve retornar CorrectOptionNotFoundException quando não tiver uma resposta correta.") 
    void updateQuestionCorrectOptionNotFoundException() {

    }

    @Test
    @DisplayName("Deve retornar MultipleCorrectOptionsException quando existir mais uma resposta correta.") 
    void updateQuestionMultipleCorrectOptionsException() {

    }

    @Test
    void getAllQuestionsForTest() {

    }

}
