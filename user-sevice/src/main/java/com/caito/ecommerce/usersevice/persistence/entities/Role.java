package com.caito.ecommerce.usersevice.persistence.entities;

import com.caito.ecommerce.usersevice.utils.enums.RoleName;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;


@Entity
@Table(name = "roles")
@NoArgsConstructor@AllArgsConstructor
@Getter@Setter
@Builder
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private RoleName role;

    @Override
    public String getAuthority() {
        return role.name();
    }
}
