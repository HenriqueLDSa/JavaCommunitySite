package com.jcs.javacommunitysite.controller;

import com.jcs.javacommunitysite.dto.community.CommunityDTO;
import com.jcs.javacommunitysite.dto.community.CreateCommunityRequest;
import com.jcs.javacommunitysite.dto.community.UpdateCommunityRequest;
import com.jcs.javacommunitysite.dto.post.PostDTO;
import com.jcs.javacommunitysite.service.CommunityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class CommunityController {

    CommunityService communityService;

    public CommunityController(CommunityService communityService) {
        this.communityService = communityService;
    }

    @PostMapping
    public ResponseEntity<?> createCommunity(@RequestBody CreateCommunityRequest request) {
        try {
            CommunityDTO created = communityService.createCommunity(request);
            return ResponseEntity.status(201).body(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        }
    }

    @PutMapping("/communities/{communityId}")
    public ResponseEntity<?> updateCommunity(@PathVariable UUID communityId, @RequestBody UpdateCommunityRequest request) {
        try {
            CommunityDTO updated = communityService.updateCommunity(request, communityId);
            return ResponseEntity.status(201).body(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        }
    }

    @DeleteMapping("/communities/{communityId}")
    public ResponseEntity<?> deleteCommunity(@PathVariable UUID communityId) {
        try {
            communityService.deleteCommunity(communityId);
            return ResponseEntity.status(201).body("Community deleted successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        }
    }

    @GetMapping("/communities/{communityId}")
    public ResponseEntity<?> getCommunityDetailsById(@PathVariable UUID communityId) {
        try {
            CommunityDTO communityReturned = communityService.getCommunityDetailsById(communityId);
            return ResponseEntity.status(201).body(communityReturned);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        }
    }

    @GetMapping("/communities/{communityId}/posts")
    public ResponseEntity<?> getAllCommunityPosts(@PathVariable UUID communityId){
        try {
            List<PostDTO> allCommunityPosts = communityService.getAllCommunityPosts(communityId);
            return ResponseEntity.status(201).body(allCommunityPosts);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        }
    }
}
