package cc.gasches.geodata.dto;

import java.util.List;

import javax.validation.Valid;

import lombok.Data;

@Data
public class UpdateSectionRequest {

    private String name;

    @Valid
    private List<GeologicalClassDto> geologicalClasses;
}
