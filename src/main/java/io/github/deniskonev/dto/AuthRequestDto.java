package io.github.deniskonev.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AuthRequestDto {

    @NotNull
    @Size(min = 5, max = 20)
    @NotBlank(message = "Имя пользователя не может быть пустым")
    private String username;

    @NotNull
    @Size(min = 5, max = 20)
    @NotBlank(message = "Пароль не может быть пустым")
    private String password;
}
