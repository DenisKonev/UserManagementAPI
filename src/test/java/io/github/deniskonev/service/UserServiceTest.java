package io.github.deniskonev.service;

import io.github.deniskonev.dto.UserRequestDTO;
import io.github.deniskonev.dto.UserResponseDTO;
import io.github.deniskonev.model.User;
import io.github.deniskonev.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static io.github.deniskonev.UserTestData.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

//TODO Пофиксить и оптимизировать тесты
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
    void testGetAllUsers() {
        when(userRepository.findAll()).thenReturn(Arrays.asList(userEntity));

        List<UserResponseDTO> result = userService.getAllUsers();

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualToComparingFieldByField(userResponseDTO);

        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testGetUserById_Found() {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(userEntity));

        Optional<UserResponseDTO> result = userService.getUserById(USER_ID);

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualToComparingFieldByField(userResponseDTO);

        verify(userRepository, times(1)).findById(USER_ID);
    }

    @Test
    void testGetUserById_NotFound() {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());

        Optional<UserResponseDTO> result = userService.getUserById(USER_ID);

        assertThat(result).isNotPresent();

        verify(userRepository, times(1)).findById(USER_ID);
    }

    @Test
    void testCreateUser() {
        User savedUser = createUser();
        savedUser.setId(USER_ID);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserResponseDTO result = userService.createUser(userRequestDTO);

        assertThat(result).isNotNull();
        assertThat(result).isEqualToComparingFieldByField(userResponseDTO);

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testUpdateUser_Found() {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(userEntity));
        when(userRepository.save(any(User.class))).thenReturn(userEntity);

        Optional<UserResponseDTO> result = userService.updateUser(USER_ID, userRequestDTO);

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualToComparingFieldByField(userResponseDTO);

        verify(userRepository, times(1)).findById(USER_ID);
        verify(userRepository, times(1)).save(userEntity);
    }

    @Test
    void testUpdateUser_NotFound() {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());

        Optional<UserResponseDTO> result = userService.updateUser(USER_ID, userRequestDTO);

        assertThat(result).isNotPresent();

        verify(userRepository, times(1)).findById(USER_ID);
        verify(userRepository, times(0)).save(any(User.class));
    }

    @Test
    void testDeleteUser_Found() {
        when(userRepository.existsById(USER_ID)).thenReturn(true);
        doNothing().when(userRepository).deleteById(USER_ID);

        boolean result = userService.deleteUser(USER_ID);

        assertThat(result).isTrue();

        verify(userRepository, times(1)).existsById(USER_ID);
        verify(userRepository, times(1)).deleteById(USER_ID);
    }

    @Test
    void testDeleteUser_NotFound() {
        when(userRepository.existsById(USER_ID)).thenReturn(false);

        boolean result = userService.deleteUser(USER_ID);

        assertThat(result).isFalse();

        verify(userRepository, times(1)).existsById(USER_ID);
        verify(userRepository, times(0)).deleteById(USER_ID);
    }
}
