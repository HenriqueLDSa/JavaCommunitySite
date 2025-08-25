package com.jcs.javacommunitysite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@SpringBootApplication
public class JavaCommunitySiteApplication {

    public static void main(String[] args) {
        SpringApplication.run(JavaCommunitySiteApplication.class, args);
    }

    @GetMapping("/api/heartbeat")
    public ResponseEntity<?> heartbeat() {
        return ResponseEntity.status(200).body("OK");
    }
}