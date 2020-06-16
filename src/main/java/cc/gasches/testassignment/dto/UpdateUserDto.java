package cc.gasches.testassignment.dto;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class UpdateUserDto {

    @NotBlank
    private String password;
}
