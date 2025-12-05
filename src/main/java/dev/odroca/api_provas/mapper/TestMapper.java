package dev.odroca.api_provas.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import dev.odroca.api_provas.entity.TestEntity;
import dev.odroca.api_provas.model.TestModelDTO;

@Mapper(componentModel = "spring", uses = QuestionMapper.class)
public interface TestMapper {
    
    @Mapping(target = "userId", ignore = true)
    TestEntity toEntity(TestModelDTO testModelDTO);

    TestModelDTO toDto(TestEntity testEntity);
    
    List<TestEntity> toEntityList(List<TestModelDTO> testModelDTOs);

    List<TestModelDTO> toDtoList(List<TestEntity> testEntities);
    
}
