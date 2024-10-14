package io.github.deniskonev.service;

import io.github.deniskonev.dto.ContactRequestDTO;
import io.github.deniskonev.dto.ContactResponseDTO;
import io.github.deniskonev.model.Contact;
import io.github.deniskonev.model.User;
import io.github.deniskonev.repository.ContactRepository;
import io.github.deniskonev.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ContactService {

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private UserRepository userRepository;

    public Contact saveOrUpdateContact(Long userId, ContactRequestDTO contactDTO) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            Optional<Contact> existingContactOptional = getContactByUserId(userId);
            if (existingContactOptional.isPresent()) {
                Contact existingContact = existingContactOptional.get();
                updateContactFromDTO(existingContact, contactDTO);
                return contactRepository.save(existingContact);
            } else {
                Contact newContact = convertToEntity(contactDTO);
                newContact.setUser(user);
                return contactRepository.save(newContact);
            }
        } else {
            throw new EntityNotFoundException("Пользователь не найден");
        }
    }

    public Optional<Contact> getContactByUserId(Long userId) {
        return Optional.ofNullable(contactRepository.findByUserId(userId));
    }

    public Contact updateContactPartially(Contact contact, ContactRequestDTO contactDTO) {
        if (contactDTO.getEmail() != null) {
            contact.setEmail(contactDTO.getEmail());
        }
        if (contactDTO.getPhoneNumber() != null) {
            contact.setPhoneNumber(contactDTO.getPhoneNumber());
        }
        return contactRepository.save(contact);
    }

    public Contact updateContact(Contact existingContact, ContactRequestDTO contactDTO) {
        updateContactFromDTO(existingContact, contactDTO);
        return contactRepository.save(existingContact);
    }

    public void deleteContactById(Long id) {
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Контакт c id:" + id + " не найден"));
        User user = contact.getUser();
        if (user != null) {
            user.setContact(null);
            userRepository.save(user);
        }
        contactRepository.delete(contact);
    }

    public Contact convertToEntity(ContactRequestDTO contactDTO) {
        Contact contact = new Contact();
        updateContactFromDTO(contact, contactDTO);
        return contact;
    }

    public ContactResponseDTO convertToResponseDTO(Contact contact) {
        ContactResponseDTO dto = new ContactResponseDTO();
        dto.setId(contact.getId());
        dto.setEmail(contact.getEmail());
        dto.setPhoneNumber(contact.getPhoneNumber());
        return dto;
    }

    public void updateContactFromDTO(Contact contact, ContactRequestDTO contactDTO) {
        contact.setEmail(contactDTO.getEmail());
        contact.setPhoneNumber(contactDTO.getPhoneNumber());
    }
}
