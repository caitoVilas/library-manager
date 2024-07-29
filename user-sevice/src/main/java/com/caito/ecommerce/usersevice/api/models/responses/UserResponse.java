package com.caito.ecommerce.usersevice.api.models.responses;

import com.caito.ecommerce.usersevice.persistence.entities.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Set;

@NoArgsConstructor@AllArgsConstructor
@Data@Builder
public class UserResponse implements Serializable {
    private Long id;
    private String fullName;
    private String address;
    private String telephone;
    private String email;
    private Set<Role> roles;
}
