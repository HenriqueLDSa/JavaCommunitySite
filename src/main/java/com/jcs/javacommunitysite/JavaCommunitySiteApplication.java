package com.jcs.javacommunitysite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

@SpringBootApplication
@RestController
public class JavaCommunitySiteApplication {

    public static void main(String[] args) {
        SpringApplication.run(JavaCommunitySiteApplication.class, args);
    }

    @GetMapping("/api/heartbeat")
    public ResponseEntity<String> heartbeat() {
        return ResponseEntity.ok("OK");
    }
}
