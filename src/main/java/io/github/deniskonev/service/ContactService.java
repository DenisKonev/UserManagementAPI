package io.github.deniskonev.service;

import io.github.deniskonev.dto.ContactDTO;
import io.github.deniskonev.model.Contact;
import io.github.deniskonev.model.User;
import io.github.deniskonev.repository.ContactRepository;
import io.github.deniskonev.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ContactService {

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private UserRepository userRepository;

    public Contact saveOrUpdateContact(Long userId, Contact contact) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            contact.setUser(user);

            Optional<Contact> existingContactOptional = getContactByUserId(userId);
            if (existingContactOptional.isPresent()) {
                Contact existingContact = existingContactOptional.get();
                existingContact.setEmail(contact.getEmail());
                existingContact.setPhoneNumber(contact.getPhoneNumber());
                return contactRepository.save(existingContact);
            } else {
                return contactRepository.save(contact);
            }
        } else {
            throw new RuntimeException("User not found");
        }
    }

    public Optional<Contact> getContactByUserId(Long userId) {
        return Optional.ofNullable(contactRepository.findByUserId(userId));
    }

    public Contact updateContactPartially(Contact contact, ContactDTO contactDTO) {
        if (contactDTO.getEmail() != null) {
            contact.setEmail(contactDTO.getEmail());
        }
        if (contactDTO.getPhoneNumber() != null) {
            contact.setPhoneNumber(contactDTO.getPhoneNumber());
        }
        return contactRepository.save(contact);
    }

    public Contact updateContact(Contact existingContact, ContactDTO contactDTO) {
        existingContact.setEmail(contactDTO.getEmail());
        existingContact.setPhoneNumber(contactDTO.getPhoneNumber());
        return contactRepository.save(existingContact);
    }

    public void deleteContactById(Long id) {
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contact not found"));
        User user = contact.getUser();
        user.setContact(null);
        userRepository.save(user);
        contactRepository.delete(contact);
    }
}
