package io.github.deniskonev;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.deniskonev.dto.UserRequestDTO;
import io.github.deniskonev.dto.UserResponseDTO;
import io.github.deniskonev.model.User;
import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.Month;

@UtilityClass
public class UserTestData {

    public static final Long USER_ID = 1L;
    public static final String FIRST_NAME = "Иван";
    public static final String LAST_NAME = "Иванов";
    public static final String MIDDLE_NAME = "Иванович";
    public static final String NEW_FIRST_NAME = "Алексей";
    public static final LocalDate DATE_OF_BIRTH_LOCAL_DATE = LocalDate.of(1977, Month.DECEMBER, 12);
    public static final String DATE_OF_BIRTH_STRING = "1977-12-21";

    public static User createUser() {
        User user = new User();
        user.setId(1L);
        user.setFirstName(FIRST_NAME);
        user.setLastName(LAST_NAME );
        user.setMiddleName(MIDDLE_NAME);
        user.setDateOfBirth(DATE_OF_BIRTH_LOCAL_DATE);
        return user;
    }

    public static UserRequestDTO createUserRequestDTO() {
        UserRequestDTO dto = new UserRequestDTO();
        dto.setFirstName(FIRST_NAME);
        dto.setLastName(LAST_NAME );
        dto.setDateOfBirth(DATE_OF_BIRTH_STRING);
        return dto;
    }

    public static UserResponseDTO createUserResponseDTO() {
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setId(1L);
        userResponseDTO.setFirstName(FIRST_NAME);
        userResponseDTO.setLastName(LAST_NAME );
        userResponseDTO.setMiddleName(MIDDLE_NAME);
        userResponseDTO.setDateOfBirth(DATE_OF_BIRTH_STRING);
        return userResponseDTO;
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
