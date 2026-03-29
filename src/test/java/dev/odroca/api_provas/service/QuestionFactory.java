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

        question1.setTest(test);
        question2.setTest(test);
        question3.setTest(test);

        Set<OptionEntity> options1 = OptionFactory.buildOptionsEntity(question1, "2", true, "3", false, "4", false);
        Set<OptionEntity> options2 = OptionFactory.buildOptionsEntity(question1, "2", false, "3", true, "4", false);
        Set<OptionEntity> options3 = OptionFactory.buildOptionsEntity(question1, "2", false, "3", false, "4", true);

        question1.setOptions(options1);
        question2.setOptions(options2);
        question3.setOptions(options3);

        questions.add(question1);
        questions.add(question2);
        questions.add(question3);
        return questions;
    }

    public static Set<QuestionEntity> buildQuestionEntityWithoutOptionCorrect(TestEntity test) {
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

        Set<OptionEntity> options1 = OptionFactory.buildOptionsEntity(question1, "2", false, "3", false, "4", false); // Nenhuma correta
        Set<OptionEntity> options2 = OptionFactory.buildOptionsEntity(question1, "2", false, "3", true, "4", false);
        Set<OptionEntity> options3 = OptionFactory.buildOptionsEntity(question1, "2", false, "3", false, "4", true);

        question1.setOptions(options1);
        question2.setOptions(options2);
        question3.setOptions(options3);

        questions.add(question1);
        questions.add(question2);
        questions.add(question3);
        return questions;
    }
}
