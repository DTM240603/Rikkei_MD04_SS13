package ra.edu.hw.md04.ss13.service;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ra.edu.hw.md04.ss13.dto.request.LoginRequest;
import ra.edu.hw.md04.ss13.dto.request.RegisterRequest;
import ra.edu.hw.md04.ss13.dto.response.LoginResponse;
import ra.edu.hw.md04.ss13.dto.response.UserResponse;
import ra.edu.hw.md04.ss13.entity.User;
import ra.edu.hw.md04.ss13.repository.UserRepository;
import ra.edu.hw.md04.ss13.security.JwtUtil;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public UserResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists");
        }

        User user = new User(
                UUID.randomUUID().toString(),
                request.username(),
                passwordEncoder.encode(request.password()),
                request.role());
        User savedUser = userRepository.save(user);
        return new UserResponse(savedUser.getId(), savedUser.getUsername(), savedUser.getRole());
    }

    public String generateToken(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return jwtUtil.generateToken(user);
    }

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new BadCredentialsException("Bad credentials"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new BadCredentialsException("Bad credentials");
        }

        return new LoginResponse(jwtUtil.generateToken(user));
    }
}
