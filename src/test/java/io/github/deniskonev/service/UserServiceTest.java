package io.github.deniskonev.service;

import io.github.deniskonev.dto.UserRequestDTO;
import io.github.deniskonev.dto.UserResponseDTO;
import io.github.deniskonev.model.User;
import io.github.deniskonev.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static io.github.deniskonev.UserTestData.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User userEntity;
    private UserResponseDTO userResponseDTO;
    private UserRequestDTO userRequestDTO;

    @BeforeEach
    void setUp() {
        userEntity = createUser();
        userResponseDTO = createUserResponseDTO();
        userRequestDTO = createUserRequestDTO();
    }

    @Test
    @DisplayName("Get all users")
    void testGetAllUsers() {
        when(userRepository.findAll()).thenReturn(Collections.singletonList(userEntity));

        List<UserResponseDTO> result = userService.getAllUsers();

        assertThat(result).isNotNull()
                .hasSize(1);
        assertThat(result.getFirst()).usingRecursiveComparison()
                .isEqualTo(userResponseDTO);

        verify(userRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Get user by id: Success")
    void testGetUserById_Found() {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(userEntity));

        Optional<UserResponseDTO> result = userService.getUserById(USER_ID);

        assertThat(result).isPresent();
        assertThat(result.get()).usingRecursiveComparison()
                .isEqualTo(userResponseDTO);

        verify(userRepository, times(1)).findById(USER_ID);
    }

    @Test
    @DisplayName("Get user by id: Not found")
    void testGetUserById_NotFound() {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());

        Optional<UserResponseDTO> result = userService.getUserById(USER_ID);

        assertThat(result).isNotPresent();

        verify(userRepository, times(1)).findById(USER_ID);
    }

    @Test
    @DisplayName("Create user: Success")
    void testCreateUser() {
        User savedUser = createUser();
        savedUser.setId(USER_ID);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserResponseDTO result = userService.createUser(userRequestDTO);

        assertThat(result).isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(userResponseDTO);

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Update user: Success")
    void testUpdateUser_Found() {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(userEntity));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User userToSave = invocation.getArgument(0);
            userToSave.setUpdatedAt(FIXED_TIME);
            return userToSave;
        });

        UserResponseDTO expectedDTO = createUserResponseDTO();
        expectedDTO.setUpdatedAt(FIXED_TIME);

        Optional<UserResponseDTO> result = userService.updateUser(USER_ID, userRequestDTO);

        assertThat(result).isPresent();
        assertThat(result.get()).usingRecursiveComparison()
                .isEqualTo(expectedDTO);

        verify(userRepository, times(1)).findById(USER_ID);
        verify(userRepository, times(1)).save(userEntity);
    }

    @Test
    @DisplayName("Update user: Not found")
    void testUpdateUser_NotFound() {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());

        Optional<UserResponseDTO> result = userService.updateUser(USER_ID, userRequestDTO);

        assertThat(result).isNotPresent();

        verify(userRepository, times(1)).findById(USER_ID);
        verify(userRepository, times(0)).save(any(User.class));
    }

    @Test
    @DisplayName("Delete user: Success")
    void testDeleteUser_Found() {
        when(userRepository.existsById(USER_ID)).thenReturn(true);
        doNothing().when(userRepository).deleteById(USER_ID);

        boolean result = userService.deleteUser(USER_ID);

        assertThat(result).isTrue();

        verify(userRepository, times(1)).existsById(USER_ID);
        verify(userRepository, times(1)).deleteById(USER_ID);
    }

    @Test
    @DisplayName("Delete user: Not found")
    void testDeleteUser_NotFound() {
        when(userRepository.existsById(USER_ID)).thenReturn(false);

        boolean result = userService.deleteUser(USER_ID);

        assertThat(result).isFalse();

        verify(userRepository, times(1)).existsById(USER_ID);
        verify(userRepository, times(0)).deleteById(USER_ID);
    }
}
