package io.github.deniskonev.service;

import io.github.deniskonev.dto.UserUpdateDTO;
import io.github.deniskonev.model.User;
import io.github.deniskonev.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    public User updateUserPartially(User user, UserUpdateDTO userUpdateDTO) {
        if (userUpdateDTO.getFirstName() != null) {
            user.setFirstName(userUpdateDTO.getFirstName());
        }
        if (userUpdateDTO.getLastName() != null) {
            user.setLastName(userUpdateDTO.getLastName());
        }
        if (userUpdateDTO.getMiddleName() != null) {
            user.setMiddleName(userUpdateDTO.getMiddleName());
        }
        if (userUpdateDTO.getDateOfBirth() != null) {
            LocalDate dateOfBirth = LocalDate.parse(userUpdateDTO.getDateOfBirth());
            user.setDateOfBirth(dateOfBirth);
        }
        return userRepository.save(user);
    }
}