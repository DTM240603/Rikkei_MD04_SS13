package ra.edu.hw.md04.ss13.service;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.server.ResponseStatusException;
import ra.edu.hw.md04.ss13.dto.request.RegisterRequest;
import ra.edu.hw.md04.ss13.dto.response.UserResponse;
import ra.edu.hw.md04.ss13.entity.User;
import ra.edu.hw.md04.ss13.repository.UserRepository;
import ra.edu.hw.md04.ss13.security.JwtUtil;

import java.util.Arrays;

import static org.mockito.Mockito.mock;

class UserServiceTest {
    private final UserRepository userRepository = mock(UserRepository.class);
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final JwtUtil jwtUtil = mock(JwtUtil.class);
    private final UserService userService = new UserService(userRepository, passwordEncoder, jwtUtil);

    @Test
    void registerStoresHashedPasswordAndReturnsSafeUserDetails() {
        RegisterRequest request = new RegisterRequest("alice", "plain-password", "USER");
        when(userRepository.existsByUsername("alice")).thenReturn(false);
        when(userRepository.save(org.mockito.ArgumentMatchers.any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        var response = userService.register(request);

        verify(userRepository).save(org.mockito.ArgumentMatchers.argThat(user ->
                !user.getPassword().equals("plain-password")
                        && passwordEncoder.matches("plain-password", user.getPassword())));
        assertEquals("alice", response.username());
        assertEquals("USER", response.role());
        assertTrue(response.id() != null && !response.id().isBlank());
        assertArrayEquals(
                new String[]{"id", "username", "role"},
                Arrays.stream(UserResponse.class.getRecordComponents())
                        .map(component -> component.getName())
                        .toArray(String[]::new));
    }

    @Test
    void registerRejectsAnExistingUsername() {
        when(userRepository.existsByUsername("alice")).thenReturn(true);

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> userService.register(new RegisterRequest("alice", "password", "USER")));

        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
    }
}
