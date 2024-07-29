package com.caito.ecommerce.usersevice.persistence.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "validation_token")
@NoArgsConstructor@AllArgsConstructor
@Getter@Setter
@Builder
public class ValidationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;
    private LocalDateTime expiryDate;
    private String email;
}
