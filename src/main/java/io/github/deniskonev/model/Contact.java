package io.github.deniskonev.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Table(name = "contacts")
@Data
public class Contact {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @Column(nullable = false, unique = true)
    @NotNull
    @Email
    private String email;

    @Column(nullable = false)
    @NotNull
    @Size(min = 5, max = 20)
    private String phoneNumber;
}
