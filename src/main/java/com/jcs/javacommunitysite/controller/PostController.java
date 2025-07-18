package com.jcs.javacommunitysite.controller;

import com.jcs.javacommunitysite.dto.community.CommunityDTO;
import com.jcs.javacommunitysite.dto.community.CreateCommunityRequest;
import com.jcs.javacommunitysite.dto.post.CreatePostRequest;
import com.jcs.javacommunitysite.dto.post.PostDTO;
import com.jcs.javacommunitysite.service.CommunityService;
import com.jcs.javacommunitysite.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @Operation(summary = "Create a new post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Post created successfully"),
    })
    @PostMapping("/create")
    public ResponseEntity<?> createPost(@RequestBody CreatePostRequest request) {
        try {
            PostDTO created = postService.createPost(request);
            return ResponseEntity.status(201).body(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        }
    }
}
