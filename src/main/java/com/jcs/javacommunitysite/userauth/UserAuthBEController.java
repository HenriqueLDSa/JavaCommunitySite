package com.jcs.javacommunitysite.userauth;

import com.jcs.javacommunitysite.atproto.AtprotoClient;
import com.jcs.javacommunitysite.atproto.service.AtprotoSessionService;
import com.jcs.javacommunitysite.atproto.exceptions.AtprotoUnauthorized;
import com.jcs.javacommunitysite.atproto.session.AtprotoJwtSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/authbe")
public class UserAuthBEController {

    private final AtprotoSessionService sessionService;

    public UserAuthBEController(AtprotoSessionService sessionService) {
        this.sessionService = sessionService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        try {
            String pdsHost = credentials.get("pdsHost");
            String handle = credentials.get("handle");
            String password = credentials.get("password");

            sessionService.createSession(pdsHost, handle, password);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "handle", handle,
                    "message", "Login successful"
            ));

        } catch (AtprotoUnauthorized e) {
            return ResponseEntity.status(401).body(Map.of(
                    "success", false,
                    "message", "Invalid credentials"
            ));
        } catch (IOException e) {
            return ResponseEntity.status(500).body(Map.of(
                    "success", false,
                    "message", "Connection error"
            ));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        sessionService.clearSession();
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Logged out successfully"
        ));
    }

    @GetMapping("/status")
    public ResponseEntity<?> getAuthStatus() {
        if (sessionService.isAuthenticated()) {
            return ResponseEntity.ok(Map.of(
                    "authenticated", true,
                    "handle", Objects.requireNonNull(sessionService.getCurrentSession().map(AtprotoJwtSession::getHandle).orElse(null))
            ));
        } else {
            return ResponseEntity.ok(Map.of("authenticated", false));
        }
    }
}