package io.github.deniskonev.controller;

import io.github.deniskonev.dto.AuthRequestDto;
import io.github.deniskonev.exception.InvalidCredentialsException;
import io.github.deniskonev.security.JwtUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
@Tag(name = "Auth Controller", description = "Получение JWT токена")
public class AuthController {

    @Autowired
    private  JwtUtils jwtUtils;


    //TODO Аутентификация должным образом не происходит. Любой запрос, который проходит валидацию, может получить токен. Надо починить.
    @PostMapping("/authenticate")
    public ResponseEntity<Map<String, String>> authenticate(@Valid @RequestBody AuthRequestDto authRequestDto) {
        try {
            String jwt = jwtUtils.generateJwtToken(authRequestDto.getUsername());
            return ResponseEntity.ok(Collections.singletonMap("token", jwt));
        } catch (BadCredentialsException | UsernameNotFoundException e) {
            throw new InvalidCredentialsException("Неверные учетные данные");
        }
    }
}
