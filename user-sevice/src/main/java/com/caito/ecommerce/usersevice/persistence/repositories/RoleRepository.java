package com.caito.ecommerce.usersevice.persistence.repositories;

import com.caito.ecommerce.usersevice.persistence.entities.Role;
import com.caito.ecommerce.usersevice.utils.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRole(RoleName role);
}
