package io.github.deniskonev.controller;

import io.github.deniskonev.dto.ContactRequestDTO;
import io.github.deniskonev.dto.ContactResponseDTO;
import io.github.deniskonev.model.Contact;
import io.github.deniskonev.service.ContactService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static io.github.deniskonev.config.ApiConstants.BASE_API;
import static io.github.deniskonev.config.ApiConstants.CONTACTS;

@RestController
@RequestMapping(BASE_API + CONTACTS)
@Tag(name = "Contact Controller", description = "CRUD операции для контактной информации")
public class ContactController {

    @Autowired
    private ContactService contactService;

    @GetMapping("/{userId}")
    public ResponseEntity<ContactResponseDTO> getContactByUserId(@PathVariable Long userId) {
        Optional<Contact> contactOptional = contactService.getContactByUserId(userId);
        return contactOptional.map(contact -> ResponseEntity.ok(contactService.convertToResponseDTO(contact)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{userId}")
    public ResponseEntity<ContactResponseDTO> createOrUpdateContact(@PathVariable Long userId, @Valid @RequestBody ContactRequestDTO contactDTO) {
        try {
            Contact savedContact = contactService.saveOrUpdateContact(userId, contactDTO);
            ContactResponseDTO responseDTO = contactService.convertToResponseDTO(savedContact);
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<ContactResponseDTO> updateContactPartially(@PathVariable Long userId, @RequestBody ContactRequestDTO contactDTO) {
        try {
            Optional<Contact> contactOptional = contactService.getContactByUserId(userId);
            if (contactOptional.isPresent()) {
                Contact updatedContact = contactService.updateContactPartially(contactOptional.get(), contactDTO);
                ContactResponseDTO responseDTO = contactService.convertToResponseDTO(updatedContact);
                return ResponseEntity.ok(responseDTO);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{userId}")
    public ResponseEntity<ContactResponseDTO> updateContact(@PathVariable Long userId, @Valid @RequestBody ContactRequestDTO contactDTO) {
        try {
            Optional<Contact> contactOptional = contactService.getContactByUserId(userId);
            if (contactOptional.isPresent()) {
                Contact existingContact = contactOptional.get();
                Contact updatedContact = contactService.updateContact(existingContact, contactDTO);
                ContactResponseDTO responseDTO = contactService.convertToResponseDTO(updatedContact);
                return ResponseEntity.ok(responseDTO);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteContact(@PathVariable Long userId) {
        Optional<Contact> contactOptional = contactService.getContactByUserId(userId);
        if (contactOptional.isPresent()) {
            contactService.deleteContactById(contactOptional.get().getId());
            return ResponseEntity.noContent().build();
        } else {
            throw new EntityNotFoundException("Контакт у пользователя с id:" + userId + " не найден");
        }
    }
}
