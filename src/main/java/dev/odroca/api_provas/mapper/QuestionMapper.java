package dev.odroca.api_provas.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import dev.odroca.api_provas.dto.question.CreateQuestionModelDTO;
import dev.odroca.api_provas.dto.question.GetQuestionModelDTO;
import dev.odroca.api_provas.entity.QuestionEntity;

@Mapper(componentModel = "spring", uses = OptionMapper.class)
public interface QuestionMapper {
    
    @Mapping(target = "test", ignore = true)
    QuestionEntity toEntity(CreateQuestionModelDTO questionModelDTO);
    
    GetQuestionModelDTO toDto(QuestionEntity questionEntity);

    List<QuestionEntity> toEntityList(List<CreateQuestionModelDTO> createQuestionModelDTOList);

    List<GetQuestionModelDTO> toDtoList(List<QuestionEntity> questionEntityList);

}
