package dev.odroca.api_provas.service;

import dev.odroca.api_provas.dto.option.CreateOptionModelDTO;
import dev.odroca.api_provas.dto.question.CreateQuestionModelDTO;
import dev.odroca.api_provas.dto.questions.CreateQuestionsRequestDTO;
import dev.odroca.api_provas.dto.questions.CreateQuestionsResponseDTO;
import dev.odroca.api_provas.mapper.OptionMapper;
import dev.odroca.api_provas.repository.QuestionRepository;
import dev.odroca.api_provas.repository.TestRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateQuestionsTest {

    @Spy
    QuestionService questionService;

    @Test
    @DisplayName("Deve criar duas questões quando tudo estiver OK")
    void createQuestionsSuccessful() {
        int totalQuestions = 2;
        UUID testId = UUID.fromString("5e6863bc-4f69-4a95-b672-c41296ec95a2");

        Set<CreateOptionModelDTO> options = new HashSet<>();
        for (Integer i = 0; i < 5; i++) {
            Boolean isCorrect = (i == 4);
            options.add(new CreateOptionModelDTO(i.toString(), isCorrect));
        }

        Set<CreateQuestionModelDTO> questions = new HashSet<>();
        questions.add(new CreateQuestionModelDTO("1+1?", options));
        questions.add(new CreateQuestionModelDTO("6-4?", options));

        CreateQuestionsRequestDTO questionsModel = new CreateQuestionsRequestDTO(questions);

        // Não faço nada com o response de createQuestion
        doReturn(null).when(questionService).createQuestion(any(UUID.class), any(CreateQuestionModelDTO.class));

        CreateQuestionsResponseDTO result = questionService.createQuestions(testId, questionsModel);

        assertNotNull(result);
        assertNotNull(result.id());
        assertEquals(testId, result.id());
        assertEquals(totalQuestions, result.totalCreatedQuestions());

        // Verifica se realmente o looping de createQuestions chama createQuestion 2x
        verify(questionService, times(2)).createQuestion(eq(testId), any(CreateQuestionModelDTO.class));

    }

}
