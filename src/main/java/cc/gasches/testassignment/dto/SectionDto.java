package cc.gasches.testassignment.dto;

import java.util.List;

import lombok.Data;

@Data
public class SectionDto {

    private long id;
    private String name;
    private List<GeologicalClassDto> geologicalClasses;
}
