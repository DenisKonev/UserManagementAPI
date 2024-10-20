package io.github.deniskonev.service;

import io.github.deniskonev.dto.UserRequestDTO;
import io.github.deniskonev.dto.UserResponseDTO;
import io.github.deniskonev.model.User;
import io.github.deniskonev.repository.UserRepository;
import io.github.deniskonev.util.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Получение списка всех пользователей.
     *
     * @return список UserResponseDTO
     */
    public List<UserResponseDTO> getAllUsers() {
        log.debug("Запрос на получение всех пользователей");
        List<User> users = userRepository.findAll();
        List<UserResponseDTO> userDTOs = users.stream()
                .map(UserMapper::toDTO)
                .toList();
        log.debug("Найдено {} пользователей", userDTOs.size());
        return userDTOs;
    }

    /**
     * Получение пользователя по идентификатору.
     *
     * @param id идентификатор пользователя
     * @return Optional с UserResponseDTO или пустой Optional, если пользователь не найден
     */
    @SuppressWarnings("LoggingSimilarMessage")
    public Optional<UserResponseDTO> getUserById(Long id) {
        log.debug("Запрос на получение пользователя с id {}", id);
        Optional<UserResponseDTO> userDTOOptional = userRepository.findById(id)
                .map(UserMapper::toDTO);
        if (userDTOOptional.isPresent()) {
            log.debug("Пользователь с id {} найден", id);
        } else {
            log.debug("Пользователь с id {} не найден", id);
        }
        return userDTOOptional;
    }

    /**
     * Создание нового пользователя.
     *
     * @param userDTO данные для создания пользователя
     * @return UserResponseDTO созданного пользователя
     */
    public UserResponseDTO createUser(UserRequestDTO userDTO) {
        log.debug("Запрос на создание пользователя: {}", userDTO);
        User user = UserMapper.toEntity(userDTO);
        LocalDateTime now = LocalDateTime.now();
        user.setCreatedAt(now);
        user.setUpdatedAt(now);
        User savedUser = userRepository.save(user);
        UserResponseDTO responseDTO = UserMapper.toDTO(savedUser);
        log.debug("Пользователь создан с id {}", responseDTO.getId());
        return responseDTO;
    }

    /**
     * Обновление существующего пользователя.
     *
     * @param id      идентификатор пользователя
     * @param userDTO новые данные пользователя
     * @return Optional с обновленным UserResponseDTO или пустой Optional, если пользователь не найден
     */
    public Optional<UserResponseDTO> updateUser(Long id, UserRequestDTO userDTO) {
        log.debug("Запрос на обновление пользователя с id {}: {}", id, userDTO);
        return userRepository.findById(id)
                .map(user -> {
                    UserMapper.updateEntity(user, userDTO);
                    user.setUpdatedAt(LocalDateTime.now());
                    User updatedUser = userRepository.save(user);
                    UserResponseDTO responseDTO = UserMapper.toDTO(updatedUser);
                    log.debug("Пользователь с id {} обновлен", id);
                    return responseDTO;
                });
    }

    /**
     * Удаление пользователя по идентификатору.
     *
     * @param id идентификатор пользователя
     * @return true если пользователь был удален, false если пользователь не найден
     */
    public boolean deleteUser(Long id) {
        log.debug("Запрос на удаление пользователя с id {}", id);
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            log.debug("Пользователь с id {} успешно удален", id);
            return true;
        }
        log.debug("Пользователь с id {} не найден для удаления", id);
        return false;
    }
}
