package dev.odroca.api_provas.service;

import dev.odroca.api_provas.entity.OptionEntity;
import dev.odroca.api_provas.entity.QuestionEntity;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.*;

public class OptionFactory {


    public static Set<OptionEntity> buildOptionsEntity(QuestionEntity question, String value1, Boolean isCorrect1, String value2, Boolean isCorrect2, String value3, Boolean isCorrect3) {
        Set<OptionEntity> options = new HashSet<>();

        OptionEntity option1 = new OptionEntity(value1, isCorrect1, question);
        OptionEntity option2 = new OptionEntity(value2, isCorrect2, question);
        OptionEntity option3 = new OptionEntity(value3, isCorrect3, question);

        ReflectionTestUtils.setField(option1, "id", UUID.randomUUID());
        ReflectionTestUtils.setField(option2, "id", UUID.randomUUID());
        ReflectionTestUtils.setField(option3, "id", UUID.randomUUID());

        options.add(option1);
        options.add(option2);
        options.add(option3);

        return options;
    }

}
