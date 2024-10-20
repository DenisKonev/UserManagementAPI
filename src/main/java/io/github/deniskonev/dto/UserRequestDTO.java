package io.github.deniskonev.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRequestDTO {

    @NotNull
    @Size(max = 50)
    private String username;

    @NotNull
    @Email
    @Size(max = 100)
    private String email;

    @NotNull
    @Size(min = 8)
    private String password;
}
