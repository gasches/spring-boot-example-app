package cc.gasches.testassignment.dto;

import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeologicalClassDto {

    @NotEmpty
    private String name;

    @NotEmpty
    private String code;
}
