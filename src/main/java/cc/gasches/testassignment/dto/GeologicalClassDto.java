package cc.gasches.testassignment.dto;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeologicalClassDto {

    @NotBlank
    private String name;

    @NotBlank
    private String code;
}
