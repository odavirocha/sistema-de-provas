package dev.odroca.api_provas.service;

import dev.odroca.api_provas.entity.TestEntity;
import dev.odroca.api_provas.entity.UserEntity;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.UUID;

public class TestFactory {
    public static TestEntity buildTestEntity(UserEntity user, UUID testId) {
        TestEntity test = new TestEntity(user, "Prova de teste");
        ReflectionTestUtils.setField(test, "id", testId);
        test.setQuestions(QuestionFactory.buildQuestionEntity(test));
        return test;
    }
}
