package dev.odroca.api_provas.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import dev.odroca.api_provas.dto.option.CreateOptionModelDTO;
import dev.odroca.api_provas.dto.option.GetOptionModelDTO;
import dev.odroca.api_provas.entity.OptionEntity;

@Mapper(componentModel = "spring")
public interface OptionMapper {

    @Mapping(target = "question", ignore = true)
    @Mapping(target = "id", ignore = true)
    OptionEntity toEntity(CreateOptionModelDTO createOptionModelDTO);

    GetOptionModelDTO toDto(OptionEntity optionEntity);

    List<OptionEntity> toEntityList(List<CreateOptionModelDTO> createOptionModelDTOList);

    List<GetOptionModelDTO> toDtoList(List<OptionEntity> optionEntityList);
    
}
