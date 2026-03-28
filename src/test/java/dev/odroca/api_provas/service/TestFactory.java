package dev.odroca.api_provas.service;

import dev.odroca.api_provas.dto.question.QuestionAnswerModelDTO;
import dev.odroca.api_provas.dto.test.AnswerTestRequestDTO;
import dev.odroca.api_provas.entity.OptionEntity;
import dev.odroca.api_provas.entity.TestEntity;
import dev.odroca.api_provas.entity.UserEntity;
import dev.odroca.api_provas.exception.OptionNotFoundException;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.UUID;

public class TestFactory {
    public static TestEntity buildTestEntity(UserEntity user, UUID testId) {
        TestEntity test = new TestEntity(user, "Prova de teste");
        ReflectionTestUtils.setField(test, "id", testId);
        test.setQuestions(QuestionFactory.buildQuestionEntity(test));
        return test;
    }

    public static AnswerTestRequestDTO buildRequestTest(TestEntity godTest) {
        List<QuestionAnswerModelDTO> answerQuestions = godTest.getQuestions().stream().map(question -> {
            UUID id = question.getId();
            UUID selectedOptionId = question.getOptions().stream()
            .filter(OptionEntity::getIsCorrect)
            .map(OptionEntity::getId).findFirst().orElseThrow(OptionNotFoundException::new);

            return new QuestionAnswerModelDTO(id, selectedOptionId);
        }).toList();

        return new AnswerTestRequestDTO(answerQuestions);
    }
}
