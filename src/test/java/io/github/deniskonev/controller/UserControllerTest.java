package io.github.deniskonev.controller;

import io.github.deniskonev.dto.UserRequestDTO;
import io.github.deniskonev.dto.UserResponseDTO;
import io.github.deniskonev.model.User;
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
        Mockito.when(userService.getAllUsers()).thenReturn(Collections.singletonList(createUser()));
        Mockito.when(userService.convertToResponseDTO(any(User.class))).thenReturn(createUserResponseDTO());
        mockMvc.perform(get(BASE_API + USERS)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(USER_ID))
                .andExpect(jsonPath("$[0].firstName").value(FIRST_NAME))
                .andExpect(jsonPath("$[0].lastName").value(LAST_NAME))
                .andExpect(jsonPath("$[0].dateOfBirth").value(DATE_OF_BIRTH_STRING));
    }

    @Test
    void testGetUserById_Success() throws Exception {
        Mockito.when(userService.getUserById(USER_ID)).thenReturn(Optional.of(createUser()));
        Mockito.when(userService.convertToResponseDTO(any(User.class))).thenReturn(createUserResponseDTO());
        mockMvc.perform(get(BASE_API + USERS + "/" + USER_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(USER_ID))
                .andExpect(jsonPath("$.firstName").value(FIRST_NAME))
                .andExpect(jsonPath("$.lastName").value(LAST_NAME))
                .andExpect(jsonPath("$.dateOfBirth").value(DATE_OF_BIRTH_STRING));
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
        User user = createUser();
        Mockito.when(userService.convertToEntity(any(UserRequestDTO.class))).thenReturn(user);
        Mockito.when(userService.saveUser(any(User.class))).thenReturn(user);
        Mockito.when(userService.convertToResponseDTO(any(User.class))).thenReturn(createUserResponseDTO());
        mockMvc.perform(post(BASE_API + USERS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(createUserRequestDTO())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(USER_ID))
                .andExpect(jsonPath("$.firstName").value(FIRST_NAME))
                .andExpect(jsonPath("$.lastName").value(LAST_NAME))
                .andExpect(jsonPath("$.dateOfBirth").value(DATE_OF_BIRTH_STRING));
    }

    @Test
    void testCreateUser_ValidationError() throws Exception {
        mockMvc.perform(post(BASE_API + USERS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(new UserRequestDTO())))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateUser_Success() throws Exception {
        Mockito.when(userService.getUserById(USER_ID)).thenReturn(Optional.of(createUser()));
        Mockito.when(userService.saveUser(any(User.class))).thenReturn(createUser());
        Mockito.when(userService.convertToResponseDTO(any(User.class))).thenReturn(createUserResponseDTO());
        mockMvc.perform(put(BASE_API + USERS + "/" + USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(createUserRequestDTO())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(USER_ID))
                .andExpect(jsonPath("$.firstName").value(FIRST_NAME))
                .andExpect(jsonPath("$.lastName").value(LAST_NAME))
                .andExpect(jsonPath("$.dateOfBirth").value(DATE_OF_BIRTH_STRING));
    }

    @Test
    void testUpdateUser_NotFound() throws Exception {
        Mockito.when(userService.getUserById(USER_ID)).thenReturn(Optional.empty());
        mockMvc.perform(put(BASE_API + USERS + "/" + USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(createUserRequestDTO())))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateUserPartially_Success() throws Exception {
        UserRequestDTO userRequestDTO = new UserRequestDTO();
        userRequestDTO.setFirstName(NEW_FIRST_NAME);
        User updatedUser = createUser();
        updatedUser.setFirstName(NEW_FIRST_NAME);
        UserResponseDTO userResponseDTO = createUserResponseDTO();
        userResponseDTO.setFirstName(NEW_FIRST_NAME);
        Mockito.when(userService.getUserById(USER_ID)).thenReturn(Optional.of(createUser()));
        Mockito.doAnswer(invocation -> {
            User u = invocation.getArgument(0);
            UserRequestDTO dto = invocation.getArgument(1);
            if (dto.getFirstName() != null) {
                u.setFirstName(dto.getFirstName());
            }
            return null;
        }).when(userService).updateUserPartiallyFromDTO(any(User.class), any(UserRequestDTO.class));
        Mockito.when(userService.saveUser(any(User.class))).thenReturn(updatedUser);
        Mockito.when(userService.convertToResponseDTO(any(User.class))).thenReturn(userResponseDTO);
        mockMvc.perform(patch(BASE_API + USERS + "/" + USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(NEW_FIRST_NAME));
    }

    @Test
    void testUpdateUserPartially_NotFound() throws Exception {
        Mockito.when(userService.getUserById(USER_ID)).thenReturn(Optional.empty());
        mockMvc.perform(patch(BASE_API + USERS + "/" + USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(createUserRequestDTO())))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteUser_Success() throws Exception {
        Mockito.when(userService.getUserById(USER_ID)).thenReturn(Optional.of(createUser()));
        mockMvc.perform(delete(BASE_API + USERS + "/" + USER_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteUser_NotFound() throws Exception {
        Mockito.when(userService.getUserById(USER_ID)).thenReturn(Optional.empty());
        mockMvc.perform(delete(BASE_API + USERS + "/" + USER_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
