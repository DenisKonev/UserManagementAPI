package io.github.deniskonev.service;

import io.github.deniskonev.dto.UserRequestDTO;
import io.github.deniskonev.dto.UserResponseDTO;
import io.github.deniskonev.model.User;
import io.github.deniskonev.repository.UserRepository;
import io.github.deniskonev.util.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

//TODO Добавить логирование
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<UserResponseDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(UserMapper::toDTO)
                .toList();
    }

    public Optional<UserResponseDTO> getUserById(Long id) {
        return userRepository.findById(id)
                .map(UserMapper::toDTO);
    }

    public UserResponseDTO createUser(UserRequestDTO userDTO) {
        User user = UserMapper.toEntity(userDTO);
        LocalDateTime now = LocalDateTime.now();
        user.setCreatedAt(now);
        user.setUpdatedAt(now);
        User savedUser = userRepository.save(user);
        return UserMapper.toDTO(savedUser);
    }

    public Optional<UserResponseDTO> updateUser(Long id, UserRequestDTO userDTO) {
        return userRepository.findById(id)
                .map(user -> {
                    UserMapper.updateEntity(user, userDTO);
                    user.setUpdatedAt(LocalDateTime.now());
                    User updatedUser = userRepository.save(user);
                    return UserMapper.toDTO(updatedUser);
                });
    }

    public boolean deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
