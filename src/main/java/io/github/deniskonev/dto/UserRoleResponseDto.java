package io.github.deniskonev.dto;

import lombok.Data;

@Data
public class UserRoleResponseDto {
    private Long id;
    private Long roleId;
    private String name;
    private String description;
}
