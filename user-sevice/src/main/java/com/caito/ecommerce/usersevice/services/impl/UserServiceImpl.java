package com.caito.ecommerce.usersevice.services.impl;

import com.caito.ecommerce.usersevice.api.exceptions.customs.BadRequestException;
import com.caito.ecommerce.usersevice.api.exceptions.customs.NotFoundException;
import com.caito.ecommerce.usersevice.api.models.requests.ChangePasswordRequest;
import com.caito.ecommerce.usersevice.api.models.requests.UserRequest;
import com.caito.ecommerce.usersevice.api.models.responses.UserResponse;
import com.caito.ecommerce.usersevice.persistence.entities.UserEntity;
import com.caito.ecommerce.usersevice.persistence.entities.ValidationToken;
import com.caito.ecommerce.usersevice.persistence.repositories.RoleRepository;
import com.caito.ecommerce.usersevice.persistence.repositories.UserRepository;
import com.caito.ecommerce.usersevice.persistence.repositories.ValidationTokenRepository;
import com.caito.ecommerce.usersevice.services.contracts.EmailService;
import com.caito.ecommerce.usersevice.services.contracts.UserService;
import com.caito.ecommerce.usersevice.utils.constatnts.TokenConst;
import com.caito.ecommerce.usersevice.utils.constatnts.UserConstants;
import com.caito.ecommerce.usersevice.utils.enums.RoleName;
import com.caito.ecommerce.usersevice.utils.mappers.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.authentication.password.CompromisedPasswordDecision;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Pattern;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final ValidationTokenRepository validationTokenRepository;
    private final CompromisedPasswordChecker passwordChecker;

    @Override
    @Transactional
    public void createUser(UserRequest request) {
        log.info("--> Creating user service");
        this.validateUser(request);
        var role = roleRepository.findByRole(RoleName.ROLE_USER).orElseThrow(()->{
            log.error("--> ERROR: ".concat(UserConstants.ROLE_NOT_FOUND));
            return new NotFoundException(UserConstants.ROLE_NOT_FOUND);
        });
        var user = UserMapper.mapToEntity(request);
        user.setRoles(Set.of(role));
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
        this.sendNotification(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {
        log.error("--> Getting all users service");
        var users = userRepository.findAll();
        return users.stream().map(UserMapper::mapTODto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {
        log.info("--> Getting user by id service");
        var user = userRepository.findById(id).orElseThrow(()->{
            log.error("--> ERROR: ".concat(UserConstants.USER_NOT_FOUND));
            return new NotFoundException(UserConstants.USER_NOT_FOUND);
        });
        return UserMapper.mapTODto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserByEmail(String email) {
        log.info("--> Getting user by email service");
        var user = userRepository.findByEmail(email).orElseThrow(()->{
            log.error("--> ERROR: ".concat(UserConstants.USER_NOT_FOUND));
            return new NotFoundException(UserConstants.USER_NOT_FOUND);
        });
        return UserMapper.mapTODto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getUserByName(String name) {
        log.info("--> Getting user by name service");
        var users = userRepository.findAllByFullNameContainingIgnoreCase(name);
        if (users.isEmpty()){
            log.error("--> ERROR: ".concat(UserConstants.USER_NOT_FOUND));
            throw new NotFoundException(UserConstants.USER_NOT_FOUND);
        }
        return users.stream().map(UserMapper::mapTODto).toList();
    }


    @Override
    @Transactional
    public void deleteUser(Long id) {
        log.info("--> Deleting user service");
        var user = userRepository.findById(id).orElseThrow(()->{
            log.error("--> ERROR: ".concat(UserConstants.USER_NOT_FOUND));
            return new NotFoundException(UserConstants.USER_NOT_FOUND);
        });
        userRepository.delete(user);
    }

    @Override
    @Transactional
    public void confirmRegistration(String token) {
        log.info("--> Confirming registration service");
        var validationToken = validationTokenRepository.findByToken(token).orElseThrow(()->{
            log.error("--> ERROR: ".concat(TokenConst.TK_NOT_FOUND));
            return new NotFoundException(TokenConst.TK_NOT_FOUND);
        });
        var user = userRepository.findByEmail(validationToken.getEmail()).orElseThrow(()->{
            log.error("--> ERROR: ".concat(UserConstants.USER_NOT_FOUND));
            return new NotFoundException(UserConstants.USER_NOT_FOUND);
        });
        user.setEnabled(true);
        user.setCredentialsNonExpired(true);
        user.setAccountNonLocked(true);
        user.setAccountNonExpired(true);
        userRepository.save(user);
        validationTokenRepository.delete(validationToken);
        emailService.sendEmail(new String[]{user.getEmail()},
                "Account Activation - No Reply",
                "Your account has been activated successfully");
    }

    @Override
    @Transactional
    public void changePassword(ChangePasswordRequest request) {
        log.info("--> Changing password service");
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow(()->{
            log.error("--> ERROR: ".concat(UserConstants.USER_NOT_FOUND));
            return new NotFoundException(UserConstants.USER_NOT_FOUND);
        });
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())){
            log.error("--> ERROR: ".concat(UserConstants.USER_PASSWORD_INCORRECT));
            throw new BadRequestException(List.of(UserConstants.USER_PASSWORD_INCORRECT));
        }
        if (!request.getNewPassword().equals(request.getConfirmPassword())){
            log.error("--> ERROR: ".concat(UserConstants.USER_PASSWORD_NO_MATCH));
            throw new BadRequestException(List.of(UserConstants.USER_PASSWORD_NO_MATCH));
        }
        if (!this.validatePassword(request.getNewPassword())){
            log.error("--> ERROR: ".concat(UserConstants.USER_PASSWORD_INVALID));
            throw new BadRequestException(List.of(UserConstants.USER_PASSWORD_INVALID));
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }


    private void validateUser(UserRequest request) {
        log.info("--> validate user service...");
        List<String> errors = new ArrayList<>();
        //name validation
        if(!StringUtils.hasText(request.getFullName())) {
            log.error("--> ERROR: ".concat(UserConstants.USER_FULL_NAME_EMPTY));
            errors.add(UserConstants.USER_FULL_NAME_EMPTY);
        }
        //address validation
        if (!StringUtils.hasText(request.getAddress())){
            log.error("--> ERROR: ".concat(UserConstants.USER_ADDRESS_EMPTY));
            errors.add(UserConstants.USER_ADDRESS_EMPTY);
        }
        //telephone validation
        if (!StringUtils.hasText(request.getTelephone())){
            log.error("--> ERROR: ".concat(UserConstants.USER_TELEPHONE_EMPTY));
            errors.add(UserConstants.USER_TELEPHONE_EMPTY);
        }
        //email validation
        if (!StringUtils.hasText(request.getEmail())){
            log.error("--> ERROR: ".concat(UserConstants.USER_EMAIL_EMPTY));
            errors.add(UserConstants.USER_EMAIL_EMPTY);
        } else if (userRepository.existsByEmail(request.getEmail())) {
            log.error("--> ERROR: ".concat(UserConstants.USER_EMAIL_EXISTS));
            errors.add(UserConstants.USER_EMAIL_EXISTS);
        } else if (!this.validateEmail(request.getEmail())) {
            log.error("--> ERROR: ".concat(UserConstants.USER_EMAIL_INVALID));
            errors.add(UserConstants.USER_EMAIL_INVALID);
        }
        //password validation
        if (!StringUtils.hasText(request.getPassword())){
            log.error("--> ERROR: ".concat(UserConstants.USER_PASSWORD_EMPTY));
            errors.add(UserConstants.USER_PASSWORD_EMPTY);
        }else if (!request.getPassword().equals(request.getConfirmPassword())){
            log.error("--> ERROR: ".concat(UserConstants.USER_PASSWORD_NO_MATCH));
            errors.add(UserConstants.USER_PASSWORD_NO_MATCH);
        }else if (!this.validatePassword(request.getPassword())){
            log.error("--> ERROR: ".concat(UserConstants.USER_PASSWORD_INVALID));
            errors.add(UserConstants.USER_PASSWORD_INVALID);

        }
        CompromisedPasswordDecision decision = passwordChecker.check(request.getPassword());
        if (decision.isCompromised()){
            log.error("--> ERROR: ".concat(UserConstants.USER_PASSWORD_COMPROMISED));
            errors.add(UserConstants.USER_PASSWORD_COMPROMISED);
        }
        if (!errors.isEmpty())
            throw new BadRequestException(errors);
    }

    private boolean validateEmail(String email){
        Pattern PATTERN = Pattern.compile("^[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        return PATTERN.matcher(email).matches();
    }

    private boolean validatePassword(String password){
        final String PASS_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
        final Pattern PASS_PATTERN = Pattern.compile(PASS_REGEX);
        return PASS_PATTERN.matcher(password).matches();
    }

    private void sendNotification(UserEntity user){
        log.info("--> Sending notification to user...");
        var token = ValidationToken.builder()
                .token(UUID.randomUUID().toString())
                .expiryDate(LocalDateTime.now())
                .email(user.getEmail())
                .build();
        validationTokenRepository.save(token);
        Map<String, String> data = new HashMap<>();
        data.put("name", user.getFullName());
        data.put("token", token.getToken());
        emailService.sendEmailWithTemplate(new String[]{user.getEmail()},
                                         "Account Activation - No Reply",
                "templates/account-activation.html",
                                          data);
    }

}
