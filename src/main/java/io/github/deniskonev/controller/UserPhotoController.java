package io.github.deniskonev.controller;

import io.github.deniskonev.dto.UserPhotoDTO;
import io.github.deniskonev.model.UserPhoto;
import io.github.deniskonev.service.UserPhotoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Base64;
import java.util.Optional;

import static io.github.deniskonev.config.ApiConstants.*;

@RestController
@RequestMapping(BASE_API + PHOTOS)
@Tag(name = "User Photo Controller", description = "CRUD операции для фотографий пользователей")
public class UserPhotoController {

    @Autowired
    private UserPhotoService userPhotoService;

    @GetMapping("/{userId}")
    public ResponseEntity<UserPhotoDTO> getPhotoByUserId(@PathVariable Long userId) {
        Optional<UserPhoto> photoOptional = userPhotoService.getPhotoByUserId(userId);
        if (photoOptional.isPresent()) {
            UserPhoto userPhoto = photoOptional.get();
            UserPhotoDTO userPhotoDTO = new UserPhotoDTO();
            String photoBase64 = Base64.getEncoder().encodeToString(userPhoto.getPhoto());
            userPhotoDTO.setPhotoBase64(photoBase64);
            return ResponseEntity.ok(userPhotoDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{userId}")
    public ResponseEntity<UserPhoto> createOrUpdatePhoto(@PathVariable Long userId, @Valid @RequestBody UserPhotoDTO userPhotoDTO) {
        try {
            byte[] photoBytes = Base64.getDecoder().decode(userPhotoDTO.getPhotoBase64());
            UserPhoto userPhoto = new UserPhoto();
            userPhoto.setPhoto(photoBytes);
            UserPhoto savedPhoto = userPhotoService.saveOrUpdatePhoto(userId, userPhoto);
            return ResponseEntity.ok(savedPhoto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserPhoto> updatePhoto(@PathVariable Long userId, @Valid @RequestBody UserPhotoDTO userPhotoDTO) {
        try {
            Optional<UserPhoto> photoOptional = userPhotoService.getPhotoByUserId(userId);
            if (photoOptional.isPresent()) {
                UserPhoto existingPhoto = photoOptional.get();
                UserPhoto updatedPhoto = userPhotoService.updatePhoto(existingPhoto, userPhotoDTO);
                return ResponseEntity.ok(updatedPhoto);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deletePhoto(@PathVariable Long userId) {
        Optional<UserPhoto> photoOptional = userPhotoService.getPhotoByUserId(userId);
        if (photoOptional.isPresent()) {
            userPhotoService.deletePhotoById(photoOptional.get().getId());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
