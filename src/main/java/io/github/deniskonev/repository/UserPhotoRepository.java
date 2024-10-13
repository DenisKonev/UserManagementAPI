package io.github.deniskonev.repository;

import io.github.deniskonev.model.UserPhoto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPhotoRepository extends JpaRepository<UserPhoto, Long> {
    UserPhoto findByUserId(Long userId);
}
