package io.github.deniskonev.controller;

import io.github.deniskonev.dto.ContactDTO;
import io.github.deniskonev.model.Contact;
import io.github.deniskonev.service.ContactService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

import static io.github.deniskonev.config.ApiConstants.*;

@RestController
@RequestMapping(BASE_API + CONTACTS)
@Tag(name = "Contact Controller", description = "CRUD операции для контактной информации")
public class ContactController {

    @Autowired
    private ContactService contactService;

    @GetMapping("/{userId}")
    public ResponseEntity<Contact> getContactByUserId(@PathVariable Long userId) {
        Optional<Contact> contactOptional = contactService.getContactByUserId(userId);
        return contactOptional.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{userId}")
    public ResponseEntity<Contact> createOrUpdateContact(@PathVariable Long userId, @RequestBody Contact contact) {
        try {
            Contact savedContact = contactService.saveOrUpdateContact(userId, contact);
            return ResponseEntity.ok(savedContact);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Contact> updateContactPartially(@PathVariable Long userId, @Valid @RequestBody ContactDTO contactDTO) {
        try {
            Optional<Contact> contactOptional = contactService.getContactByUserId(userId);
            if (contactOptional.isPresent()) {
                Contact updatedContact = contactService.updateContactPartially(contactOptional.get(), contactDTO);
                return ResponseEntity.ok(updatedContact);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Contact> updateContact(@PathVariable Long userId, @Valid @RequestBody ContactDTO contactDTO) {
        try {
            Optional<Contact> contactOptional = contactService.getContactByUserId(userId);
            if (contactOptional.isPresent()) {
                Contact existingContact = contactOptional.get();
                Contact updatedContact = contactService.updateContact(existingContact, contactDTO);
                return ResponseEntity.ok(updatedContact);
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
            return ResponseEntity.notFound().build();
        }
    }
}
