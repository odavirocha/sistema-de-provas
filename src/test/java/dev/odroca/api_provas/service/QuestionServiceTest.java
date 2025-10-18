package dev.odroca.api_provas.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
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
import dev.odroca.api_provas.dto.option.GetOptionModelDTO;
import dev.odroca.api_provas.dto.option.UpdateOptionModelDTO;
import dev.odroca.api_provas.dto.question.CreateQuestionModelDTO;
import dev.odroca.api_provas.dto.question.CreateQuestionResponseDTO;
import dev.odroca.api_provas.dto.question.GetQuestionModelDTO;
import dev.odroca.api_provas.dto.question.UpdateQuestionRequestDTO;
import dev.odroca.api_provas.dto.question.UpdateQuestionResponseDTO;
import dev.odroca.api_provas.dto.questions.CreateQuestionsRequestDTO;
import dev.odroca.api_provas.dto.questions.CreateQuestionsResponseDTO;
import dev.odroca.api_provas.entity.OptionEntity;
import dev.odroca.api_provas.entity.QuestionEntity;
import dev.odroca.api_provas.entity.TestEntity;
import dev.odroca.api_provas.exception.CorrectOptionNotFoundException;
import dev.odroca.api_provas.exception.MultipleCorrectOptionsException;
import dev.odroca.api_provas.exception.QuestionNotFoundException;
import dev.odroca.api_provas.exception.TestNotFoundException;
import dev.odroca.api_provas.mapper.OptionMapper;
import dev.odroca.api_provas.mapper.QuestionMapper;
import dev.odroca.api_provas.repository.OptionRepository;
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
    OptionRepository optionRepository;

    @Mock
    QuestionMapper questionMapper;

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
        when(optionMapper.createDtoToEntityList(options)).thenReturn(optionEntities);

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
        verify(optionMapper).createDtoToEntityList(options);
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
        when(optionMapper.createDtoToEntityList(options)).thenReturn(optionEntities);

        assertThrows(MultipleCorrectOptionsException.class, () -> {
            questionService.createQuestion(testId, question);
        });

        verify(testRepository).findById(testId);
        verify(optionMapper).createDtoToEntityList(options);
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
        when(optionMapper.createDtoToEntityList(options)).thenReturn(optionEntities);
        
        assertThrows(CorrectOptionNotFoundException.class, () -> {
            questionService.createQuestion(testId, question);
        });
        
        verify(testRepository).findById(testId);
        verify(optionMapper).createDtoToEntityList(options);
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

        // Parâmetro do updateQuestion
        UUID questionId = UUID.fromString("e7baa643-6ee6-4ffc-b41b-4aa248b4c144");

        // Parâmetro do updateQuestion
        UpdateQuestionRequestDTO questionUpdate = new UpdateQuestionRequestDTO("Enunciado: 1+1?", null);
                
        List<UUID> idsFromRequest = List.of(
            UUID.fromString("63d25ed5-f5f9-4a60-a5c0-3718bf9f9a03"),
            UUID.fromString("00b3841f-245f-44ce-9ac2-0cffd18e93ab"),
            UUID.fromString("4b8f5e4a-892e-41e2-ab58-b9e7dd907b70"),
            UUID.fromString("0034398d-c602-43ba-9aff-6f0081244b30"),
            UUID.fromString("3c9bac11-8019-4630-838a-b8621cb90686")
        );

        List<UpdateOptionModelDTO> optionsForQuestionUpdate = new ArrayList<>();

        for (Integer i = 0; i < 5; i++) {
            Boolean isCorrect = (i == 4);
            UpdateOptionModelDTO optionModelDTO = new UpdateOptionModelDTO();

            ReflectionTestUtils.setField(optionModelDTO, "optionId", idsFromRequest.get(i));
            optionModelDTO.setValue(i.toString());
            optionModelDTO.setIsCorrect(isCorrect);

            optionsForQuestionUpdate.add(optionModelDTO);
        }

        // Adiciona List<UpdateOptionModelDTO> optionsForQuestionUpdate dentro do campo options
        ReflectionTestUtils.setField(questionUpdate, "options", optionsForQuestionUpdate);

        // FindById(questionId)
        QuestionEntity databaseQuestion = new QuestionEntity();

        ReflectionTestUtils.setField(databaseQuestion, "id", questionId);

        List<OptionEntity> optionsForDatabaseQuestion = new ArrayList<>();

        for (Integer i = 0; i < 5; i++) {
            Boolean isCorrect = (i == 4);
            OptionEntity optionEntity = new OptionEntity();

            ReflectionTestUtils.setField(optionEntity, "id", idsFromRequest.get(i));
            optionEntity.setValue(i.toString());
            optionEntity.setIsCorrect(isCorrect);

            optionsForDatabaseQuestion.add(optionEntity);
        }

        List<OptionEntity> optionsThatExist = List.of(
            new OptionEntity(), 
            new OptionEntity(), 
            new OptionEntity(), 
            new OptionEntity() , 
            new OptionEntity()
        );
        
        // Adiciona List<OptionEntity> optionsForDatabaseQuestion dentro do campo options
        ReflectionTestUtils.setField(databaseQuestion, "options", optionsForDatabaseQuestion);
        
        when(questionRepository.findById(questionId)).thenReturn(Optional.of(databaseQuestion));
        when(optionRepository.findAllById(idsFromRequest)).thenReturn(optionsThatExist);
        when(questionRepository.save(databaseQuestion)).thenReturn(databaseQuestion);

        UpdateQuestionResponseDTO result = questionService.updateQuestion(questionId, questionUpdate);

        assertNotNull(result);
        assertEquals(questionId, result.getQuestionId());
        
        verify(optionRepository, never()).deleteAll(anyList()); // Requets sem nenhum nulo, nenhuma opção deve ser deletada
    }

    @Test
    @DisplayName("Deve dar sucesso quando enviar pelo menos um ID nulo no request.") 
    void updateQuestionQuestionSuccessfulIdNull() {

        UUID questionId = UUID.fromString("e7baa643-6ee6-4ffc-b41b-4aa248b4c144");

        UpdateQuestionRequestDTO questionUpdate = new UpdateQuestionRequestDTO("1+1?", null);

        List<UUID> idsFromRequest = List.of(
            UUID.fromString("63d25ed5-f5f9-4a60-a5c0-3718bf9f9a03"),
            UUID.fromString("00b3841f-245f-44ce-9ac2-0cffd18e93ab"),
            UUID.fromString("4b8f5e4a-892e-41e2-ab58-b9e7dd907b70"),
            UUID.fromString("0034398d-c602-43ba-9aff-6f0081244b30")
        );

        List<UpdateOptionModelDTO> optionsForQuestionUpdate = new ArrayList<>();

        for (Integer i = 0; i < 5; i++) {
            Boolean isCorrect = (i == 3);
            UpdateOptionModelDTO option = new UpdateOptionModelDTO();
            
            if (i < 4) {
                ReflectionTestUtils.setField(option, "optionId", idsFromRequest.get(i)); // Seta o ID ou nulo.
            } else {
                ReflectionTestUtils.setField(option, "optionId", null); // Seta o ID ou nulo.
            }

            option.setIsCorrect(isCorrect);
            option.setValue(i.toString());

            optionsForQuestionUpdate.add(option);
        }

        // Adiciona List<UpdateOptionModelDTO> optionsForQuestionUpdate dentro do campo options
        ReflectionTestUtils.setField(questionUpdate, "options", optionsForQuestionUpdate);

        QuestionEntity databaseQuestion = new QuestionEntity();
        
        ReflectionTestUtils.setField(databaseQuestion, "id", questionId);
        
        databaseQuestion.setQuestion("1+1");

        List<OptionEntity> entitesToDelete = new ArrayList<>();
        OptionEntity optionToDelete = new OptionEntity();

        ReflectionTestUtils.setField(optionToDelete, "id", UUID.fromString("3c9bac11-8019-4630-838a-b8621cb90686"));
        optionToDelete.setIsCorrect(false);
        optionToDelete.setValue("5");

        entitesToDelete.add(optionToDelete);

        List<OptionEntity> optionsThatExist = new ArrayList<>();

        for (Integer i = 0; i < 4; i++) {
            
            Boolean isCorrect = (i == 3 );
            OptionEntity option = new OptionEntity();

            ReflectionTestUtils.setField(option, "id", idsFromRequest.get(i));
            option.setIsCorrect(isCorrect);
            option.setValue(i.toString());
            
            optionsThatExist.add(option);
        }
        
        List<OptionEntity> databaseOptions = new ArrayList<>();
        
        databaseOptions.addAll(optionsThatExist);
        databaseOptions.add(optionToDelete);

        databaseQuestion.setOptions(databaseOptions);

        when(questionRepository.findById(questionId)).thenReturn(Optional.of(databaseQuestion));
        when(optionRepository.findAllById(idsFromRequest)).thenReturn(optionsThatExist);
        when(questionRepository.save(databaseQuestion)).thenReturn(databaseQuestion);

        UpdateQuestionResponseDTO result = questionService.updateQuestion(questionId, questionUpdate);

        assertNotNull(result);
        assertEquals(questionId, result.getQuestionId());
        
        verify(optionRepository).deleteAll(anyList());
    }

    @Test
    @DisplayName("Deve retornar QuestionNotFoundException quando não achar a resposta que as opções estão atribuidas.") 
    void updateQuestionQuestionNotFoundException() {

        UUID questionId = UUID.fromString("e7baa643-6ee6-4ffc-b41b-4aa248b4c144");
        
        UpdateQuestionRequestDTO questionRequestDTO = new UpdateQuestionRequestDTO();

        when(questionRepository.findById(questionId)).thenReturn(Optional.empty());

        assertThrows(QuestionNotFoundException.class, () -> {
            questionService.updateQuestion(questionId, questionRequestDTO);
        });
        
    }

    @Test
    @DisplayName("Deve retornar CorrectOptionNotFoundException quando não tiver nenhuma resposta correta.") 
    void updateQuestionCorrectOptionNotFoundException() {

        UUID questionId = UUID.fromString("e7baa643-6ee6-4ffc-b41b-4aa248b4c144");

        UpdateQuestionRequestDTO requestQuestion = new UpdateQuestionRequestDTO("1+1", null);

        List<UpdateOptionModelDTO> options = new ArrayList<>();

        for (Integer i = 0; i < 5; i++) {
            
            UpdateOptionModelDTO option = new UpdateOptionModelDTO();
            option.setIsCorrect(false);
            option.setValue(i.toString());
            options.add(option);

        }

        ReflectionTestUtils.setField(requestQuestion, "options", options);

        QuestionEntity questionEntity = new QuestionEntity();
        ReflectionTestUtils.setField(questionEntity, "id", questionId);
        questionEntity.setQuestion("2+2");
        questionEntity.setOptions(new ArrayList<>());

        when(questionRepository.findById(questionId)).thenReturn(Optional.of(questionEntity));
        
        assertThrows(CorrectOptionNotFoundException.class, () -> {
            questionService.updateQuestion(questionId, requestQuestion);
        });
    }

    @Test
    @DisplayName("Deve retornar MultipleCorrectOptionsException quando existir mais uma resposta correta.") 
    void updateQuestionMultipleCorrectOptionsException() {

        UUID questionId = UUID.fromString("e7baa643-6ee6-4ffc-b41b-4aa248b4c144");

        UpdateQuestionRequestDTO requestQuestion = new UpdateQuestionRequestDTO("1+1", null);
        
        List<UpdateOptionModelDTO> options = new ArrayList<>();

        for (Integer i = 0; i < 5; i++) {
            
            UpdateOptionModelDTO option = new UpdateOptionModelDTO();
            option.setIsCorrect(true);
            option.setValue(i.toString());
            options.add(option);

        }

        ReflectionTestUtils.setField(requestQuestion, "options", options);
        
        QuestionEntity questionEntity = new QuestionEntity();
        ReflectionTestUtils.setField(questionEntity, "id", questionId);
        questionEntity.setQuestion("1+2");
        questionEntity.setOptions(new ArrayList<>());

        when(questionRepository.findById(questionId)).thenReturn(Optional.of(questionEntity));
    
        assertThrows(MultipleCorrectOptionsException.class, () -> {
            questionService.updateQuestion(questionId, requestQuestion);
        });  
    }

    @Test
    void getAllQuestionsForTest() {

        UUID testId = UUID.fromString("e7baa643-6ee6-4ffc-b41b-4aa248b4c144");
        
        TestEntity databaseTest = new TestEntity();
        List<QuestionEntity> questions = new ArrayList<>();
        List<OptionEntity> options = new ArrayList<>();
        List<UUID> idsFromRequest = List.of(
            UUID.fromString("63d25ed5-f5f9-4a60-a5c0-3718bf9f9a03"),
            UUID.fromString("00b3841f-245f-44ce-9ac2-0cffd18e93ab"),
            UUID.fromString("4b8f5e4a-892e-41e2-ab58-b9e7dd907b70"),
            UUID.fromString("0034398d-c602-43ba-9aff-6f0081244b30"),
            UUID.fromString("3c9bac11-8019-4630-838a-b8621cb90686")
        );

        ReflectionTestUtils.setField(databaseTest, "id", testId);
        ReflectionTestUtils.setField(databaseTest, "name", "Prova de teste 01");
        ReflectionTestUtils.setField(databaseTest, "questions", questions);

        for (Integer i = 0; i < 5; i++) {

            OptionEntity optionEntity = new OptionEntity();
            Boolean isCorrect = (i == 4);
            optionEntity.setIsCorrect(isCorrect);
            optionEntity.setValue(i.toString());
            ReflectionTestUtils.setField(optionEntity, "id", idsFromRequest.get(i));

            options.add(optionEntity);
        }
        
        for (Integer i = 0; i < 2; i++) {

            QuestionEntity questionEntity = new QuestionEntity();
            questionEntity.setQuestion(i + " + " + i);
            questionEntity.setOptions(options);

            questions.add(questionEntity);
        }

        List<GetOptionModelDTO> optionsConverted = options.stream()
        .map(option -> {
            GetOptionModelDTO optionConverted = new GetOptionModelDTO();
            ReflectionTestUtils.setField(optionConverted, "id", option.getId());
            ReflectionTestUtils.setField(optionConverted, "value", option.getValue());
            ReflectionTestUtils.setField(optionConverted, "isCorrect", option.getIsCorrect());
            return optionConverted;
        }).collect(Collectors.toList());

        List<GetQuestionModelDTO> questionsConverted = questions.stream()
        .map(question -> {
            GetQuestionModelDTO questionConverted = new GetQuestionModelDTO();
            ReflectionTestUtils.setField(questionConverted, "id", question.getId());
            ReflectionTestUtils.setField(questionConverted, "question", question.getQuestion());
            ReflectionTestUtils.setField(questionConverted, "options", optionsConverted);
            return questionConverted;
        }).collect(Collectors.toList());


        when(testRepository.findById(testId)).thenReturn(Optional.of(databaseTest));
        when(questionMapper.toDtoList(questions)).thenReturn(questionsConverted);
        
        List<GetQuestionModelDTO> result = questionService.getAllQuestionsForTest(testId);

        assertEquals(questionsConverted, result);
    }

}
