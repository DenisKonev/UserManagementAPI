package io.github.deniskonev.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserPhotoDTO {

    @NotNull
    private String photoBase64;
}
