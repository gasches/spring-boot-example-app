package cc.gasches.testassignment.dto;

import java.util.List;

import lombok.Value;

@Value
public class SectionDto {

    long id;
    String name;
    List<GeologicalClassDto> geologicalClasses;
}
