package io.github.deniskonev.controller;

import io.github.deniskonev.dto.AuthRequestDto;
import io.github.deniskonev.security.JwtUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
@Tag(name = "Auth Controller", description = "Получение JWT токена")
public class AuthController {

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/authenticate")
    public Map<String, String> authenticate(@RequestBody AuthRequestDto authRequestDto) {
        try {
            return Collections.singletonMap("token", jwtUtils.generateJwtToken(authRequestDto.getUsername()));
        } catch (AuthenticationException e) {
            throw new RuntimeException("Invalid credentials");
        }
    }
}
