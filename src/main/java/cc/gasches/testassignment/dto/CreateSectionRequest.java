package cc.gasches.testassignment.dto;

import java.util.List;

import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Data
public class CreateSectionRequest {

    @NotEmpty
    private String name;
    private List<GeologicalClassDto> geologicalClasses;
}
