package io.github.deniskonev.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ContactDTO {

    @NotNull
    @Email
    private String email;

    @Size(min = 5, max = 20)
    private String phoneNumber;
}
