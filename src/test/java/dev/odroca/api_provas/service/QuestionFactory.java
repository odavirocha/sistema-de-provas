package dev.odroca.api_provas.service;

import dev.odroca.api_provas.entity.QuestionEntity;

import java.util.Set;

public class QuestionFactory {

    public static Set<QuestionEntity> buildQuestions() {
        return Set.of(
            new QuestionEntity("Enunciado: 1+1", OptionFactory.buildOptions())
        );
    }

}
