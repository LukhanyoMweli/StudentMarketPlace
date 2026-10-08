package za.ac.cput.studentmarketplace.service;

import za.ac.cput.studentmarketplace.dto.AuthResponse;
import za.ac.cput.studentmarketplace.dto.LoginRequest;
import za.ac.cput.studentmarketplace.dto.RegisterRequest;
import za.ac.cput.studentmarketplace.model.Role;
import za.ac.cput.studentmarketplace.model.User;
import za.ac.cput.studentmarketplace.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public AuthResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            return new AuthResponse(false, "An account with this email already exists.", null, null);
        }

        Role role;
        try {
            role = Role.valueOf(request.getRole().toUpperCase());
        } catch (IllegalArgumentException e) {
            return new AuthResponse(false, "Invalid role selected.", null, null);
        }

        if (role == Role.ADMIN) {
            return new AuthResponse(false, "Admin accounts cannot self-register.", null, null);
        }

        String hashedPassword = passwordEncoder.encode(request.getPassword());

        User user = new User(
                request.getFullName(),
                request.getEmail(),
                hashedPassword,
                request.getPhoneNumber(),
                role
        );

        userRepository.save(user);

        return new AuthResponse(true, "Account created successfully.", user.getFullName(), user.getRole().name());
    }

    public AuthResponse login(LoginRequest request) {

        Role role;
        try {
            role = Role.valueOf(request.getRole().toUpperCase());
        } catch (IllegalArgumentException e) {
            return new AuthResponse(false, "Invalid role selected.", null, null);
        }

        Optional<User> found = userRepository.findByEmailAndRole(request.getEmail(), role);

        if (found.isEmpty()) {
            return new AuthResponse(false, "No matching " + role.name() + " account found.", null, null);
        }

        User user = found.get();

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return new AuthResponse(false, "Incorrect password.", null, null);
        }

        return new AuthResponse(true, "Login successful.", user.getFullName(), user.getRole().name());
    }
}
