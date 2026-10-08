package za.ac.cput.studentmarketplace.controller;

import za.ac.cput.studentmarketplace.dto.AuthResponse;
import za.ac.cput.studentmarketplace.dto.LoginRequest;
import za.ac.cput.studentmarketplace.dto.RegisterRequest;
import za.ac.cput.studentmarketplace.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = userService.register(request);
        return response.isSuccess()
                ? ResponseEntity.ok(response)
                : ResponseEntity.badRequest().body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = userService.login(request);
        return response.isSuccess()
                ? ResponseEntity.ok(response)
                : ResponseEntity.status(401).body(response);
    }
}