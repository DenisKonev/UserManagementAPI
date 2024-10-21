package io.github.deniskonev.service;

import io.github.deniskonev.dto.UserRequestDto;
import io.github.deniskonev.dto.UserResponseDto;
import io.github.deniskonev.dto.UserRoleResponseDto;
import io.github.deniskonev.exception.ExternalServiceException;
import io.github.deniskonev.model.User;
import io.github.deniskonev.repository.UserRepository;
import io.github.deniskonev.repository.UserRoleRepository;
import io.github.deniskonev.util.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static io.github.deniskonev.config.ApiConstants.EXTERNAL_ROLES_API;
import static io.github.deniskonev.util.RoleMapper.mapUserRequestDto;
import static io.github.deniskonev.util.RoleMapper.mapUserRoles;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository roleRepository;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * Получение списка всех пользователей.
     *
     * @return список UserResponseDTO
     */
    public List<UserResponseDto> getAllUsers() {
        log.debug("Запрос на получение всех пользователей");
        List<UserRoleResponseDto> rolesDto = getAllRoles();
        List<User> users = userRepository.findAll();
        List<UserResponseDto> userDTOs = users.stream()
                .map(UserMapper::toDTO)
                .toList();
        mapUserRoles(userDTOs, mapUserRequestDto(roleRepository.findAll()), rolesDto);
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
    public Optional<UserResponseDto> getUserById(Long id) {
        log.debug("Запрос на получение пользователя с id {}", id);
        Optional<UserResponseDto> userDTOOptional = userRepository.findById(id)
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
    public UserResponseDto createUser(UserRequestDto userDTO) {
        log.debug("Запрос на создание пользователя: {}", userDTO);
        User user = UserMapper.toEntity(userDTO);
        LocalDateTime now = LocalDateTime.now();
        user.setCreatedAt(now);
        user.setUpdatedAt(now);
        User savedUser = userRepository.save(user);
        UserResponseDto responseDTO = UserMapper.toDTO(savedUser);
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
    public Optional<UserResponseDto> updateUser(Long id, UserRequestDto userDTO) {
        log.debug("Запрос на обновление пользователя с id {}: {}", id, userDTO);
        return userRepository.findById(id)
                .map(user -> {
                    UserMapper.updateEntity(user, userDTO);
                    user.setUpdatedAt(LocalDateTime.now());
                    User updatedUser = userRepository.save(user);
                    UserResponseDto responseDTO = UserMapper.toDTO(updatedUser);
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

    /**
     * Получение всех ролей из  RoleManagementApi микросервиса.
     *
     * @return список RoleResponseDTO
     */
    public List<UserRoleResponseDto> getAllRoles() {
        log.debug("Запрос на получение ролей из микросервиса RoleManagementApi");
        UserRoleResponseDto[] externalRolesArray = restTemplate.getForObject(EXTERNAL_ROLES_API, UserRoleResponseDto[].class);

        if (externalRolesArray == null) {
            String errorMessage = "Результат запроса к RoleManagementApiA оказался null";
            log.error(errorMessage);
            throw new ExternalServiceException(errorMessage);
        }

        List<UserRoleResponseDto> externalRoles = Arrays.stream(externalRolesArray).toList();
        log.debug("Успешно получено {} ролей из микросервиса RoleManagementApi", externalRoles.size());
        return externalRoles;
    }
}
