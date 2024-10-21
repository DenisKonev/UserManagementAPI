package io.github.deniskonev.controller;

import io.github.deniskonev.dto.UserRequestDto;
import io.github.deniskonev.dto.UserResponseDto;
import io.github.deniskonev.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static io.github.deniskonev.config.ApiConstants.BASE_API;
import static io.github.deniskonev.config.ApiConstants.USERS;


@RestController
@RequestMapping(BASE_API + USERS)
@Tag(name = "User Controller", description = "CRUD операции для пользователей")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Получение списка всех пользователей.
     *
     * @return список UserResponseDTO
     */
    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        log.debug("Входящий запрос: GET /{}", BASE_API + USERS);
        List<UserResponseDto> userDTOs = userService.getAllUsers();
        log.debug("Возвращено {} пользователей", userDTOs.size());
        return ResponseEntity.ok(userDTOs);
    }

    /**
     * Получение пользователя по идентификатору.
     *
     * @param id идентификатор пользователя
     * @return UserResponseDTO или статус 404, если пользователь не найден
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {
        log.debug("Входящий запрос: GET /{}/{}", BASE_API + USERS, id);
        Optional<UserResponseDto> userDTOOptional = userService.getUserById(id);
        if (userDTOOptional.isPresent()) {
            log.debug("Пользователь с id {} найден", id);
            return ResponseEntity.ok(userDTOOptional.get());
        } else {
            log.debug("Пользователь с id {} не найден", id);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Создание нового пользователя.
     *
     * @param userDTO данные для создания пользователя
     * @return UserResponseDTO созданного пользователя
     */
    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserRequestDto userDTO) {
        log.debug("Входящий запрос: POST /{} с данными {}", BASE_API + USERS, userDTO);
        UserResponseDto responseDTO = userService.createUser(userDTO);
        log.debug("Пользователь создан с id {}", responseDTO.getId());
        return ResponseEntity.ok(responseDTO);
    }

    /**
     * Обновление существующего пользователя.
     *
     * @param id      идентификатор пользователя
     * @param userDTO новые данные пользователя
     * @return UserResponseDTO обновленного пользователя или статус 404, если пользователь не найден
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable Long id, @Valid @RequestBody UserRequestDto userDTO) {
        log.debug("Входящий запрос: PUT /{}/{} с данными {}", BASE_API + USERS, id, userDTO);
        Optional<UserResponseDto> updatedUserDTO = userService.updateUser(id, userDTO);
        if (updatedUserDTO.isPresent()) {
            log.debug("Пользователь с id {} успешно обновлен", id);
            return ResponseEntity.ok(updatedUserDTO.get());
        } else {
            log.debug("Пользователь с id {} не найден для обновления", id);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Удаление пользователя по идентификатору.
     *
     * @param id идентификатор пользователя
     * @return статус 204 если удален, 404 если не найден
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        log.debug("Входящий запрос: DELETE /{}/{}", BASE_API + USERS, id);
        boolean isDeleted = userService.deleteUser(id);
        if (isDeleted) {
            log.debug("Пользователь с id {} успешно удален", id);
            return ResponseEntity.noContent().build();
        } else {
            log.debug("Пользователь с id {} не найден для удаления", id);
            return ResponseEntity.notFound().build();
        }
    }
}
