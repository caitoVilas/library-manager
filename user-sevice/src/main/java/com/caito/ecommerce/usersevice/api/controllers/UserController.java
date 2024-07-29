package com.caito.ecommerce.usersevice.api.controllers;

import com.caito.ecommerce.usersevice.api.models.requests.ChangePasswordRequest;
import com.caito.ecommerce.usersevice.api.models.requests.UserRequest;
import com.caito.ecommerce.usersevice.api.models.responses.UserResponse;
import com.caito.ecommerce.usersevice.services.contracts.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Library - User", description = "User management")
public class UserController {
    private final UserService userService;

    @PostMapping("/create")
    @Operation(summary = "Create a new user")
    @Parameter(name = "request", description = "user request details")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User created"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> createUser(@RequestBody UserRequest request) {
        log.info("--> POST / endpoint creating user controller");
        userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/all")
    @SecurityRequirement(name = "bearer")
    @Operation(summary = "Get all users")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Users found"),
            @ApiResponse(responseCode = "204", description = "No content"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<UserResponse>> getAll() {
        log.info("--> GET / endpoint getting all users controller");
        var users = userService.getAllUsers();
        if (users.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(users);
    }

    @GetMapping("/by-email")
    @SecurityRequirement(name = "bearer")
    @Operation(summary = "Get user by email")
    @Parameter(name = "email", description = "user email")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<UserResponse> getByEmail(@RequestParam String email) {
        log.info("--> GET / endpoint getting user by email controller");
        var user = userService.getUserByEmail(email);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{id}")
    @SecurityRequirement(name = "bearer")
    @Operation(summary = "Get user by id")
    @Parameter(name = "id", description = "user id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<UserResponse> getById(@PathVariable Long id) {
        log.info("--> GET / endpoint getting user by id controller");
        var user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/by-name")
    @SecurityRequirement(name = "bearer")
    @Operation(summary = "Get user by name")
    @Parameter(name = "name", description = "user name")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<UserResponse>> getByName(@RequestParam String name) {
        log.info("--> GET / endpoint getting user by name controller");
        var users = userService.getUserByName(name);
        return ResponseEntity.ok(users);
    }

    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "bearer")
    @Operation(summary = "Delete user by id")
    @Parameter(name = "id", description = "user id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User deleted"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        log.info("--> DELETE / endpoint deleting user controller");
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/activate")
    @Operation(summary = "Confirm registration")
    @Parameter(name = "token", description = "token")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Registration confirmed"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> confirmRegistration(@RequestParam String token) {
        log.info("--> PUT / endpoint confirming registration controller");
        userService.confirmRegistration(token);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/change-password")
    @SecurityRequirement(name = "bearer")
    @Operation(summary = "Change password")
    @Parameter(name = "request", description = "change password request details")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Password changed"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request) {
        log.info("--> PUT / endpoint changing password controller");
        userService.changePassword(request);
        return ResponseEntity.ok().build();
    }
}
