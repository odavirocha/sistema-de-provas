package dev.odroca.api_provas.entity;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "option_table")
public class OptionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String value;

    @JoinColumn(name = "is_correct")
    private Boolean isCorrect;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "question_id")
    private QuestionEntity question;

    public OptionEntity() {
    }

    public OptionEntity(String value, Boolean isCorrect) {
        this.value = value;
        this.isCorrect = isCorrect;
    }

    public UUID getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    public Boolean getIsCorrect() {
        return isCorrect;
    }

    public QuestionEntity getQuestion() {
        return question;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setIsCorrect(Boolean isCorrect) {
        this.isCorrect = isCorrect;
    }

    public void setQuestion(QuestionEntity question) {
        this.question = question;
    }
    
}
