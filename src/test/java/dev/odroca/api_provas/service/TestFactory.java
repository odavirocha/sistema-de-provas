package dev.odroca.api_provas.service;

import dev.odroca.api_provas.entity.TestEntity;

public class TestFactory {

    public static TestEntity buildTestEntity() {
        return new TestEntity(
            "Prova de teste",
            QuestionFactory.buildQuestions()
        );
    }

}
