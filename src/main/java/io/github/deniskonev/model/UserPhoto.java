package io.github.deniskonev.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "user_photos")
@Data
public class UserPhoto {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    @JsonBackReference
    private User user;

    @Lob
    private byte[] photo;
}
