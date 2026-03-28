package dev.odroca.api_provas.service;

import dev.odroca.api_provas.entity.OptionEntity;
import dev.odroca.api_provas.entity.QuestionEntity;
import dev.odroca.api_provas.entity.TestEntity;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.*;

public class QuestionFactory {
    public static Set<QuestionEntity> buildQuestionEntity(TestEntity test) {
        Set<QuestionEntity> questions = new HashSet<>();

        QuestionEntity question1 = new QuestionEntity("1+1");
        QuestionEntity question2 = new QuestionEntity("1+2");
        QuestionEntity question3 = new QuestionEntity("1+3");

        ReflectionTestUtils.setField(question1, "id", UUID.randomUUID());
        ReflectionTestUtils.setField(question2, "id", UUID.randomUUID());
        ReflectionTestUtils.setField(question3, "id", UUID.randomUUID());

        question1.setTest(test);
        question2.setTest(test);
        question3.setTest(test);

        List<Set<OptionEntity>> options = OptionFactory.buildOptionsEntity(question1, question2, question3);

        question1.setOptions(options.get(0));
        question2.setOptions(options.get(1));
        question3.setOptions(options.get(2));

        questions.add(question1);
        questions.add(question2);
        questions.add(question3);
        return questions;
    }
}
