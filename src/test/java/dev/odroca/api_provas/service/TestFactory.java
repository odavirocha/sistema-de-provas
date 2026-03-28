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
import java.util.stream.Collectors;

public class TestFactory {
    public static TestEntity buildTestEntity(UserEntity user, UUID testId) {
        TestEntity test = new TestEntity(user, "Prova de teste");
        ReflectionTestUtils.setField(test, "id", testId);
        test.setQuestions(QuestionFactory.buildQuestionEntity(test));
        return test;
    }

    public static AnswerTestRequestDTO buildRequestTest(TestEntity testEntity) {
        List<QuestionAnswerModelDTO> answerQuestions = testEntity.getQuestions().stream().map(question -> {
            UUID id = question.getId();
            UUID selectedOptionId = question.getOptions().stream()
            .filter(OptionEntity::getIsCorrect)
            .map(OptionEntity::getId).findFirst().orElseThrow(OptionNotFoundException::new);

            return new QuestionAnswerModelDTO(id, selectedOptionId);
        }).toList();

        return new AnswerTestRequestDTO(answerQuestions);
    }

    public static AnswerTestRequestDTO buildRequestTestWithoutOneQuestion(TestEntity testEntity) {
        List<QuestionAnswerModelDTO> answerQuestions = testEntity.getQuestions().stream().map(question -> {
            UUID id = question.getId();
            UUID selectedOptionId = question.getOptions().stream()
            .filter(OptionEntity::getIsCorrect)
            .map(OptionEntity::getId).findFirst().orElseThrow(OptionNotFoundException::new);

            return new QuestionAnswerModelDTO(id, selectedOptionId);
        }).collect(Collectors.toList());

        answerQuestions.remove(2);
        return new AnswerTestRequestDTO(answerQuestions);
    }

    public static AnswerTestRequestDTO buildRequestTestWithOneWrongQuestion(TestEntity testEntity) {
        List<QuestionAnswerModelDTO> answerQuestions = testEntity.getQuestions().stream().map(question -> {
            UUID id = question.getId();
            UUID selectedOptionId = question.getOptions().stream()
            .filter(OptionEntity::getIsCorrect)
            .map(OptionEntity::getId).findFirst().orElseThrow(OptionNotFoundException::new);

            return new QuestionAnswerModelDTO(id, selectedOptionId);
        }).collect(Collectors.toList());

        UUID wrongId = answerQuestions.get(2).questionId();
        answerQuestions.remove(2);
        QuestionAnswerModelDTO wrongQuestion = new QuestionAnswerModelDTO(wrongId, UUID.fromString("397eec71-360d-4626-805c-6640be635690"));
        answerQuestions.add(wrongQuestion);
        return new AnswerTestRequestDTO(answerQuestions);
    }
}
