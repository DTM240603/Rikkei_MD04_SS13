package ra.edu.hw.md04.ss13.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ra.edu.hw.md04.ss13.entity.User;

@Component
public class JwtUtil {
    private final SecretKey signingKey;
    private final long expiration;

    public JwtUtil(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") long expiration) {
        this.signingKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        if (expiration <= 0) {
            throw new IllegalArgumentException("JWT expiration must be positive");
        }
        this.expiration = expiration;
    }

    public String generateToken(User user) {
        Date now = new Date();
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("role", user.getRole())
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expiration))
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }
}
