package io.github.deniskonev.dto;

import lombok.Data;

@Data
public class AuthRequestDto {
    private String username;
    private String password;
}
