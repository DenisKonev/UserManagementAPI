package io.github.deniskonev.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserDTO {

    @Size(max = 50)
    private String firstName;

    @Size(max = 50)
    private String lastName;

    @Size(max = 50)
    private String middleName;

    @Pattern(regexp = "\\d{2}-\\d{2}-\\d{4}")
    private String dateOfBirth;
}
