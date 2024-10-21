package io.github.deniskonev.dto;

import lombok.Data;

@Data
public class UserRoleRequestDto {

    private Long userId;
    private Long roleId;
}
