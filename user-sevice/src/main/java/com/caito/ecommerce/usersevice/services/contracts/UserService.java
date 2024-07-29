package com.caito.ecommerce.usersevice.services.contracts;

import com.caito.ecommerce.usersevice.api.models.requests.ChangePasswordRequest;
import com.caito.ecommerce.usersevice.api.models.requests.UserRequest;
import com.caito.ecommerce.usersevice.api.models.responses.UserResponse;

import java.util.List;

public interface UserService {
    void createUser(UserRequest request);
    List<UserResponse> getAllUsers();
    UserResponse getUserById(Long id);
    UserResponse getUserByEmail(String email);
    List<UserResponse> getUserByName(String name);
    void deleteUser(Long id);
    void confirmRegistration(String token);
    void changePassword(ChangePasswordRequest request);
}
