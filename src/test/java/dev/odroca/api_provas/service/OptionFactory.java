package dev.odroca.api_provas.service;

import dev.odroca.api_provas.entity.OptionEntity;
import dev.odroca.api_provas.entity.QuestionEntity;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.*;

public class OptionFactory {
    public static List<Set<OptionEntity>> buildOptionsEntity(QuestionEntity question1, QuestionEntity question2, QuestionEntity question3) {
        List<Set<OptionEntity>> allOptions = new ArrayList<>();

//        1+1
        Set<OptionEntity> options1 = new HashSet<>();
            OptionEntity option11 = new OptionEntity("2", true);
            OptionEntity option12 = new OptionEntity("3", false);
            OptionEntity option13 = new OptionEntity("4", false);

            ReflectionTestUtils.setField(option11, "id", UUID.randomUUID());
            ReflectionTestUtils.setField(option12, "id", UUID.randomUUID());
            ReflectionTestUtils.setField(option13, "id", UUID.randomUUID());

            option11.setQuestion(question1);
            option12.setQuestion(question1);
            option13.setQuestion(question1);

            options1.add(option11);
            options1.add(option12);
            options1.add(option13);

//        1+2
        Set<OptionEntity> options2 = new HashSet<>();
            OptionEntity option21 = new OptionEntity("2", false);
            OptionEntity option22 = new OptionEntity("3", true);
            OptionEntity option23 = new OptionEntity("4", false);

            ReflectionTestUtils.setField(option21, "id", UUID.randomUUID());
            ReflectionTestUtils.setField(option22, "id", UUID.randomUUID());
            ReflectionTestUtils.setField(option23, "id", UUID.randomUUID());

            option21.setQuestion(question2);
            option22.setQuestion(question2);
            option23.setQuestion(question2);

            options2.add(option21);
            options2.add(option22);
            options2.add(option23);

//        1+3
        Set<OptionEntity> options3 = new HashSet<>();
            OptionEntity option31 = new OptionEntity("2", false);
            OptionEntity option32 = new OptionEntity("3", false);
            OptionEntity option33 = new OptionEntity("4", true);

            ReflectionTestUtils.setField(option31, "id", UUID.randomUUID());
            ReflectionTestUtils.setField(option32, "id", UUID.randomUUID());
            ReflectionTestUtils.setField(option33, "id", UUID.randomUUID());

            option31.setQuestion(question3);
            option32.setQuestion(question3);
            option33.setQuestion(question3);

            options3.add(option31);
            options3.add(option32);
            options3.add(option33);

        allOptions.add(options1);
        allOptions.add(options2);
        allOptions.add(options3);

        return allOptions;
    }
}
