package com.jcs.javacommunitysite.controller;

import com.google.gson.JsonObject;
import com.jcs.javacommunitysite.atproto.AtprotoSession;
import com.jcs.javacommunitysite.atproto.records.ForumGroupRecord;
import com.jcs.javacommunitysite.atproto.records.ForumIdentityRecord;
import com.jcs.javacommunitysite.atproto.records.PostRecord;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.awt.*;

@RestController
@RequestMapping("/api")
public class HeartbeatController {

    @GetMapping("/heartbeat")
    public ResponseEntity<?> heartbeat() {
        try {
            AtprotoSession session = AtprotoSession.fromCredentials("https://bsky.social/", "jcstest2.bsky.social", "PASS");
             PostRecord post = new PostRecord("Hello! TESTING 2");
            // JsonObject resp = session.createRecord(post);

            // ForumGroupRecord testGroup = new ForumGroupRecord("Test Group 1", "This is a test group");
            ForumIdentityRecord testForum = new ForumIdentityRecord("Test Forum 1", "This is a test forum", Color.ORANGE);
            JsonObject resp = session.createRecord(post);

            System.out.println(resp);

            return ResponseEntity.status(200).body("OK - " + testForum.getAtUri().orElse("No AtUri"));
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(500).body("BAD");
        }
    }
}



