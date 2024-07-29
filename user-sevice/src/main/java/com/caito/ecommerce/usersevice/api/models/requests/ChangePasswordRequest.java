package com.caito.ecommerce.usersevice.api.models.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor@AllArgsConstructor
@Data@Builder
public class ChangePasswordRequest implements Serializable {
    private String email;
    private String password;
    private String newPassword;
    private String confirmPassword;
}
