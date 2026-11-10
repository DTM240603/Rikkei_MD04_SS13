package ra.edu.hw.md04.ss13.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;

class AuthExceptionHandlerTest {
    @Test
    void badCredentialsReturnsGenericUnauthorizedResponse() {
        var response = new AuthExceptionHandler()
                .handleBadCredentials(new BadCredentialsException("Bad credentials"));

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Bad credentials", response.getBody().get("error"));
    }
}
