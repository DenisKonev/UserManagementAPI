package io.github.deniskonev.repository;

import io.github.deniskonev.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepository extends JpaRepository<Contact, Long> {
    Contact findByEmail(String email);
    Contact findByPhoneNumber(String phoneNumber);
}
