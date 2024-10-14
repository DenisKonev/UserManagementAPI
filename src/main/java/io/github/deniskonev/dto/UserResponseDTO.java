package io.github.deniskonev.dto;

import lombok.Data;

@Data
public class UserResponseDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String middleName;
    private String dateOfBirth;
}
