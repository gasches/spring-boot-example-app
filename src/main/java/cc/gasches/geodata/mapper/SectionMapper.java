package cc.gasches.geodata.mapper;

import java.util.List;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import cc.gasches.geodata.dto.CreateSectionRequest;
import cc.gasches.geodata.dto.GeologicalClassDto;
import cc.gasches.geodata.dto.SectionDto;
import cc.gasches.geodata.model.GeologicalClass;
import cc.gasches.geodata.model.Section;

@Mapper(componentModel = "spring")
public interface SectionMapper {

    @Mapping(target = "id", ignore = true)
    Section requestToSection(CreateSectionRequest request);

    SectionDto sectionToDto(Section section);

    List<SectionDto> sectionsToDtoList(List<Section> sections);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "section", ignore = true)
    GeologicalClass dtoToGeologicalClass(GeologicalClassDto dto);

    GeologicalClassDto geologicalClassToDto(GeologicalClass geoClass);

    @AfterMapping
    default void callAfter(Object any, @MappingTarget Section section) {
        List<GeologicalClass> geologicalClasses = section.getGeologicalClasses();
        if (geologicalClasses != null && !geologicalClasses.isEmpty()) {
            for (GeologicalClass geoClass : geologicalClasses) {
                geoClass.setSection(section);
            }
        }
    }
}
