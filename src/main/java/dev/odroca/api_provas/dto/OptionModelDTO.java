package dev.odroca.api_provas.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class OptionModelDTO {
    String value;
    @JsonProperty("isCorrect") // por alguma caralha motivo o lombok tira o get e deixa somente isCorrect bugando o set do Jackson(deserializador JSON)
    boolean isCorrect;
}
