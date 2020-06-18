package cc.gasches.geodata.dto;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class CreateSectionRequest {

    @NotBlank
    private String name;

    @Valid
    private List<GeologicalClassDto> geologicalClasses;
}
