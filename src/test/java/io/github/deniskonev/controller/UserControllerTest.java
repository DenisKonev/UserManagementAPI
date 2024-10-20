package io.github.deniskonev.controller;

import io.github.deniskonev.dto.UserRequestDTO;
import io.github.deniskonev.dto.UserResponseDTO;
import io.github.deniskonev.service.UserService;
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

    @Test
    void testGetAllUsers() throws Exception {
        Mockito.when(userService.getAllUsers()).thenReturn(Collections.singletonList(createUserResponseDTO()));
        mockMvc.perform(get(BASE_API + USERS)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(USER_ID))
                .andExpect(jsonPath("$[0].username").value(USERNAME))
                .andExpect(jsonPath("$[0].email").value(EMAIL));
    }

    @Test
    void testGetUserById_Success() throws Exception {
        Mockito.when(userService.getUserById(USER_ID)).thenReturn(Optional.of(createUserResponseDTO()));
        mockMvc.perform(get(BASE_API + USERS + "/" + USER_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(USER_ID))
                .andExpect(jsonPath("$.username").value(USERNAME))
                .andExpect(jsonPath("$.email").value(EMAIL));
    }

    @Test
    void testGetUserById_NotFound() throws Exception {
        Mockito.when(userService.getUserById(USER_ID)).thenReturn(Optional.empty());
        mockMvc.perform(get(BASE_API + USERS + "/" + USER_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateUser_Success() throws Exception {
        UserResponseDTO responseDTO = createUserResponseDTO();
        Mockito.when(userService.createUser(any(UserRequestDTO.class))).thenReturn(responseDTO);
        mockMvc.perform(post(BASE_API + USERS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(createUserRequestDTO())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(USER_ID))
                .andExpect(jsonPath("$.username").value(USERNAME))
                .andExpect(jsonPath("$.email").value(EMAIL));
    }

    @Test
    void testCreateUser_ValidationError() throws Exception {
        UserRequestDTO invalidDTO = new UserRequestDTO(); // Все поля null
        mockMvc.perform(post(BASE_API + USERS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(invalidDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateUser_Success() throws Exception {
        UserResponseDTO responseDTO = createUpdatedUserResponseDTO();
        Mockito.when(userService.updateUser(any(Long.class), any(UserRequestDTO.class))).thenReturn(Optional.of(responseDTO));
        mockMvc.perform(put(BASE_API + USERS + "/" + USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(createUserRequestDTO())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(USER_ID))
                .andExpect(jsonPath("$.username").value(USERNAME))
                .andExpect(jsonPath("$.email").value(NEW_EMAIL));
    }

    @Test
    void testUpdateUser_NotFound() throws Exception {
        Mockito.when(userService.updateUser(any(Long.class), any(UserRequestDTO.class))).thenReturn(Optional.empty());
        mockMvc.perform(put(BASE_API + USERS + "/" + USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(createUserRequestDTO())))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteUser_Success() throws Exception {
        Mockito.when(userService.deleteUser(USER_ID)).thenReturn(true);
        mockMvc.perform(delete(BASE_API + USERS + "/" + USER_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteUser_NotFound() throws Exception {
        Mockito.when(userService.deleteUser(USER_ID)).thenReturn(false);
        mockMvc.perform(delete(BASE_API + USERS + "/" + USER_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
