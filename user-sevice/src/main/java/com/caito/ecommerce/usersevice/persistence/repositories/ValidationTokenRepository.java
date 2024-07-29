package com.caito.ecommerce.usersevice.persistence.repositories;

import com.caito.ecommerce.usersevice.persistence.entities.UserEntity;
import com.caito.ecommerce.usersevice.persistence.entities.ValidationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ValidationTokenRepository extends JpaRepository<ValidationToken, Long> {
    Optional<ValidationToken> findByToken(String token);
}
