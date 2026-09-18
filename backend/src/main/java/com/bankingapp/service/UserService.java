package com.bankingapp.service;

import com.bankingapp.dto.request.CreateUserRequest;
import com.bankingapp.dto.request.LoginRequest;
import com.bankingapp.dto.response.AuthResponse;
import com.bankingapp.dto.response.UserResponse;
import com.bankingapp.entity.User;
import com.bankingapp.exception.DuplicateResourceException;
import com.bankingapp.exception.ResourceNotFoundException;
import com.bankingapp.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public UserResponse createUser(CreateUserRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateResourceException("Username '" + request.getUsername() + "' is already taken");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email '" + request.getEmail() + "' is already registered");
        }

        // Note: Password hashing will be handled with Spring Security BCryptPasswordEncoder in future
        User user = new User();
        user.setUsername(request.getUsername().trim());
        user.setEmail(request.getEmail().trim().toLowerCase());
        user.setPasswordHash(request.getPassword()); // placeholder for hashed password
        user.setFullName(request.getFullName().trim());

        User saved = userRepository.save(user);
        return mapToUserResponse(saved);
    }

    @Transactional(readOnly = true)
    public UserResponse getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        return mapToUserResponse(user);
    }

    @Transactional(readOnly = true)
    public UserResponse getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
        return mapToUserResponse(user);
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        // Placeholder authentication lookup
        User user = userRepository.findByUsername(request.getUsernameOrEmail())
                .or(() -> userRepository.findByEmail(request.getUsernameOrEmail()))
                .orElseThrow(() -> new ResourceNotFoundException("Invalid username or password"));

        // Placeholder token generation (JWT will replace this)
        String mockToken = "mock-jwt-token-" + UUID.randomUUID();
        return new AuthResponse(mockToken, user.getId(), user.getUsername(), user.getEmail());
    }

    private UserResponse mapToUserResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFullName(),
                user.getCreatedAt()
        );
    }
}
