package io.github.deniskonev.util;

import io.github.deniskonev.dto.UserRequestDTO;
import io.github.deniskonev.dto.UserResponseDTO;
import io.github.deniskonev.model.User;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserMapper {

    public static User toEntity(UserRequestDTO userDTO) {
        User user = new User();
        updateEntity(user, userDTO);
        return user;
    }

    public static UserResponseDTO toDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        return dto;
    }

    public static void updateEntity(User user, UserRequestDTO userDTO) {
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
    }
}
