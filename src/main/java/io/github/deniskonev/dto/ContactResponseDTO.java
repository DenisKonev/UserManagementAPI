package io.github.deniskonev.dto;

import lombok.Data;

@Data
public class ContactResponseDTO {

    private Long id;
    private String email;
    private String phoneNumber;
}
