package dev.odroca.api_provas.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import dev.odroca.api_provas.dto.option.CreateOptionModelDTO;
import dev.odroca.api_provas.dto.option.GetOptionModelDTO;
import dev.odroca.api_provas.dto.option.UpdateOptionModelDTO;
import dev.odroca.api_provas.entity.OptionEntity;

@Mapper(componentModel = "spring")
public interface OptionMapper {

    @Mapping(target = "question", ignore = true)
    @Mapping(target = "id", ignore = true)
    OptionEntity createDtoToEntity(CreateOptionModelDTO createOptionModelDTO);
    
    GetOptionModelDTO entityToGetDto(OptionEntity optionEntity);
    
    @Mapping(target = "question", ignore = true)
    @Mapping(target = "id", ignore = true)
    OptionEntity updateDtoToEntity(UpdateOptionModelDTO optionModelDTO);
    
    @Mapping(target = "optionId", ignore = true)
    UpdateOptionModelDTO entityToUpdateOptionModelDTO(OptionEntity optionEntity);

    List<OptionEntity> createDtoToEntityList(List<CreateOptionModelDTO> createOptionModelDTOs);

    List<GetOptionModelDTO> entityToGetDtoList(List<OptionEntity> optionEntities);

    List<OptionEntity> updateDtoToEntityList(List<UpdateOptionModelDTO> optionModelDTOs);
    
    List<UpdateOptionModelDTO> entityUpdateOptionModelDTOList(List<OptionEntity> optionEntities);
}
