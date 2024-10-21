package io.github.deniskonev.controller;

import io.github.deniskonev.dto.UserRequestDto;
import io.github.deniskonev.dto.UserResponseDto;
import io.github.deniskonev.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static io.github.deniskonev.UserTestData.*;
import static io.github.deniskonev.config.ApiConstants.BASE_API;
import static io.github.deniskonev.config.ApiConstants.USERS;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private UserRequestDto invalidDTO;
    private UserResponseDto responseDTO;
    private UserResponseDto updatedResponseDTO;

    @BeforeEach
    void setUp() {
        responseDTO = createUserResponseDto();
        invalidDTO = new UserRequestDto();
        updatedResponseDTO = createUpdatedUserResponseDto();
    }

    @Test
    @DisplayName("Get all users")
    void testGetAllUsers() throws Exception {
        Mockito.when(userService.getAllUsers()).thenReturn(Collections.singletonList(createUserResponseDto()));

        mockMvc.perform(get(BASE_API + USERS)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(USER_ID))
                .andExpect(jsonPath("$[0].username").value(USERNAME))
                .andExpect(jsonPath("$[0].email").value(EMAIL));
    }

    @Test
    @DisplayName("Get user by id: Success")
    void testGetUserById_Found() throws Exception {
        Mockito.when(userService.getUserById(USER_ID)).thenReturn(Optional.of(createUserResponseDto()));

        mockMvc.perform(get(BASE_API + USERS + "/" + USER_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(USER_ID))
                .andExpect(jsonPath("$.username").value(USERNAME))
                .andExpect(jsonPath("$.email").value(EMAIL));
    }

    @Test
    @DisplayName("Get user by id: Not found")
    void testGetUserById_NotFound() throws Exception {
        Mockito.when(userService.getUserById(USER_ID)).thenReturn(Optional.empty());

        mockMvc.perform(get(BASE_API + USERS + "/" + USER_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Create user: Success")
    void testCreateUser_Success() throws Exception {
        Mockito.when(userService.createUser(any(UserRequestDto.class))).thenReturn(responseDTO);

        mockMvc.perform(post(BASE_API + USERS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(createUserRequestDto())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(USER_ID))
                .andExpect(jsonPath("$.username").value(USERNAME))
                .andExpect(jsonPath("$.email").value(EMAIL));
    }

    @Test
    @DisplayName("Create user: Validation error")
    void testCreateUser_ValidationError() throws Exception {
        mockMvc.perform(post(BASE_API + USERS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(invalidDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Update user: Success")
    void testUpdateUser_Success() throws Exception {
        Mockito.when(userService.updateUser(any(Long.class), any(UserRequestDto.class))).thenReturn(Optional.of(updatedResponseDTO));

        mockMvc.perform(put(BASE_API + USERS + "/" + USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(createUserRequestDto())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(USER_ID))
                .andExpect(jsonPath("$.username").value(USERNAME))
                .andExpect(jsonPath("$.email").value(NEW_EMAIL));
    }

    @Test
    @DisplayName("Update user: Not found")
    void testUpdateUser_NotFound() throws Exception {
        Mockito.when(userService.updateUser(any(Long.class), any(UserRequestDto.class))).thenReturn(Optional.empty());

        mockMvc.perform(put(BASE_API + USERS + "/" + USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(createUserRequestDto())))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Delete user: Success")
    void testDeleteUser_Success() throws Exception {
        Mockito.when(userService.deleteUser(USER_ID)).thenReturn(true);

        mockMvc.perform(delete(BASE_API + USERS + "/" + USER_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Delete user: Not found")
    void testDeleteUser_NotFound() throws Exception {
        Mockito.when(userService.deleteUser(USER_ID)).thenReturn(false);

        mockMvc.perform(delete(BASE_API + USERS + "/" + USER_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
