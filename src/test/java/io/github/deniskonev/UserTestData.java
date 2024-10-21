package io.github.deniskonev;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.deniskonev.dto.UserRequestDto;
import io.github.deniskonev.dto.UserResponseDto;
import io.github.deniskonev.model.User;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;

@UtilityClass
public class UserTestData {

    public static final Long USER_ID = 1L;
    public static final String USERNAME = "Иван Иванов";
    public static final String EMAIL = "ivan.ivanov@example.com";
    public static final String PASSWORD = "encrypted_password4";
    public static final String NEW_EMAIL = "ivan.new@example.com";
    public static final LocalDateTime CREATED_AT = LocalDateTime.now().minusDays(1);
    public static final LocalDateTime UPDATED_AT = LocalDateTime.now();
    public static final LocalDateTime FIXED_TIME = LocalDateTime.of(2024, 10, 20, 20, 34, 49, 724200700);

    public static User createUser() {
        User user = new User();
        user.setId(USER_ID);
        user.setUsername(USERNAME);
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setCreatedAt(CREATED_AT);
        user.setUpdatedAt(UPDATED_AT);
        return user;
    }

    public static UserRequestDto createUserRequestDTO() {
        UserRequestDto dto = new UserRequestDto();
        dto.setUsername(USERNAME);
        dto.setEmail(EMAIL);
        dto.setPassword(PASSWORD);
        return dto;
    }

    public static UserResponseDto createUserResponseDTO() {
        UserResponseDto dto = new UserResponseDto();
        dto.setId(USER_ID);
        dto.setUsername(USERNAME);
        dto.setEmail(EMAIL);
        dto.setCreatedAt(CREATED_AT);
        dto.setUpdatedAt(UPDATED_AT);
        return dto;
    }

    public static UserResponseDto createUpdatedUserResponseDTO() {
        UserResponseDto dto = new UserResponseDto();
        dto.setId(USER_ID);
        dto.setUsername(USERNAME);
        dto.setEmail(NEW_EMAIL);
        dto.setCreatedAt(CREATED_AT);
        dto.setUpdatedAt(UPDATED_AT);
        return dto;
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
