package com.caito.ecommerce.usersevice.persistence.repositories;

import com.caito.ecommerce.usersevice.persistence.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);
    boolean existsByEmail(String email);
    List<UserEntity> findAllByFullNameContainingIgnoreCase(String username);
}