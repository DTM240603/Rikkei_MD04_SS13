package ra.edu.hw.md04.ss13.service;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ra.edu.hw.md04.ss13.dto.request.RegisterRequest;
import ra.edu.hw.md04.ss13.dto.response.UserResponse;
import ra.edu.hw.md04.ss13.entity.User;
import ra.edu.hw.md04.ss13.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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
}
