package ra.edu.hw.md04.ss13.security;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.charset.StandardCharsets;
import javax.crypto.SecretKey;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import ra.edu.hw.md04.ss13.entity.User;

class JwtUtilTest {
    private static final String SECRET = "local-development-secret-key-must-be-at-least-32-bytes";

    @Test
    void tokenContainsUsernameRoleAndExpiration() {
        JwtUtil jwtUtil = new JwtUtil(SECRET, 3_600_000);
        User user = new User("user-id", "alice", "hashed-password", "ROLE_USER");

        String token = jwtUtil.generateToken(user);
        SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
        var claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        assertEquals("alice", claims.getSubject());
        assertEquals("ROLE_USER", claims.get("role", String.class));
        assertEquals(3_600_000L, claims.getExpiration().getTime() - claims.getIssuedAt().getTime());
    }
}
