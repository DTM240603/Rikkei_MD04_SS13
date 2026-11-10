package ra.edu.hw.md04.ss13.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ra.edu.hw.md04.ss13.dto.request.LoginRequest;
import ra.edu.hw.md04.ss13.dto.response.LoginResponse;
import ra.edu.hw.md04.ss13.dto.request.RegisterRequest;
import ra.edu.hw.md04.ss13.dto.response.UserResponse;
import ra.edu.hw.md04.ss13.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(userService.login(request));
    }

    @GetMapping("/test-token")
    public ResponseEntity<String> generateTestToken(@RequestParam String username) {
        return ResponseEntity.ok(userService.generateToken(username));
    }
}
