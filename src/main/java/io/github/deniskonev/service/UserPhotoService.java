package io.github.deniskonev.service;

import io.github.deniskonev.dto.UserPhotoDTO;
import io.github.deniskonev.model.User;
import io.github.deniskonev.model.UserPhoto;
import io.github.deniskonev.repository.UserPhotoRepository;
import io.github.deniskonev.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Optional;

@Service
public class UserPhotoService {

    @Autowired
    private UserPhotoRepository userPhotoRepository;

    @Autowired
    private UserRepository userRepository;

    public UserPhoto saveOrUpdatePhoto(Long userId, UserPhoto userPhoto) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            userPhoto.setUser(user);

            Optional<UserPhoto> existingPhotoOptional = getPhotoByUserId(userId);
            if (existingPhotoOptional.isPresent()) {
                UserPhoto existingPhoto = existingPhotoOptional.get();
                existingPhoto.setPhoto(userPhoto.getPhoto());
                return userPhotoRepository.save(existingPhoto);
            } else {
                return userPhotoRepository.save(userPhoto);
            }
        } else {
            throw new RuntimeException("User not found");
        }
    }

    public Optional<UserPhoto> getPhotoByUserId(Long userId) {
        return Optional.ofNullable(userPhotoRepository.findByUserId(userId));
    }

    public UserPhoto updatePhoto(UserPhoto existingPhoto, UserPhotoDTO userPhotoDTO) {
        byte[] photoBytes = Base64.getDecoder().decode(userPhotoDTO.getPhotoBase64());
        existingPhoto.setPhoto(photoBytes);
        return userPhotoRepository.save(existingPhoto);
    }

    public void deletePhotoById(Long id) {
        UserPhoto userPhoto = userPhotoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Photo not found"));
        User user = userPhoto.getUser();
        if (user != null) {
            user.setUserPhoto(null);
            userRepository.save(user);
        }

        userPhotoRepository.delete(userPhoto);
    }
}
