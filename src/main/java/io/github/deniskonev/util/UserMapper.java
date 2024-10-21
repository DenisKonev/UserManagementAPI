package io.github.deniskonev.util;

import io.github.deniskonev.dto.UserRequestDto;
import io.github.deniskonev.dto.UserResponseDto;
import io.github.deniskonev.model.User;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserMapper {

    /**
     * Преобразование UserRequestDTO в сущность User.
     *
     * @param userDTO данные из запроса
     * @return сущность User
     */
    public static User toEntity(UserRequestDto userDTO) {
        User user = new User();
        updateEntity(user, userDTO);
        return user;
    }

    /**
     * Преобразование сущности User в UserResponseDTO.
     *
     * @param user сущность User
     * @return DTO пользователя с ролями
     */
    public static UserResponseDto toDTO(User user) {
        UserResponseDto dto = new UserResponseDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        return dto;
    }

    /**
     * Обновление сущности User на основе данных из UserRequestDTO.
     *
     * @param user    существующая сущность User
     * @param userDTO данные из запроса
     */
    public static void updateEntity(User user, UserRequestDto userDTO) {
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
    }
}
