package io.github.deniskonev.controller;

import io.github.deniskonev.dto.UserRequestDTO;
import io.github.deniskonev.dto.UserResponseDTO;
import io.github.deniskonev.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static io.github.deniskonev.config.ApiConstants.BASE_API;
import static io.github.deniskonev.config.ApiConstants.USERS;

//TODO Добавить логирование
@RestController
@RequestMapping(BASE_API + USERS)
@Tag(name = "User Controller", description = "CRUD операции для пользователей")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        Optional<UserResponseDTO> userDTOOptional = userService.getUserById(id);
        return userDTOOptional.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserRequestDTO userDTO) {
        return ResponseEntity.ok(userService.createUser(userDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Long id, @Valid @RequestBody UserRequestDTO userDTO) {
        Optional<UserResponseDTO> updatedUserDTO = userService.updateUser(id, userDTO);
        return updatedUserDTO.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (userService.deleteUser(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
