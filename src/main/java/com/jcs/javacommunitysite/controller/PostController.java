package com.jcs.javacommunitysite.controller;

import com.jcs.javacommunitysite.dto.comment.CommentDTO;
import com.jcs.javacommunitysite.dto.post.CreatePostRequest;
import com.jcs.javacommunitysite.dto.post.PostDTO;
import com.jcs.javacommunitysite.dto.post.UpdatePostRequest;
import com.jcs.javacommunitysite.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class PostController {

    PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/communities/{communityId}/posts")
    public ResponseEntity<?> createPost(@PathVariable String communityId, @RequestBody CreatePostRequest request) {
        try {
            request.setCommunityId(communityId);
            PostDTO createdPost = postService.createPost(request);
            return ResponseEntity.status(201).body(createdPost);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        }
    }

    @PutMapping("/posts/{postId}")
    public ResponseEntity<?> updatePost(@PathVariable UUID postId, @RequestBody UpdatePostRequest request) {
        try {
            PostDTO updatedPost = postService.updatePost(request, postId);
            return ResponseEntity.status(201).body(updatedPost);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        }
    }

    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable UUID postId) {
        try {
            postService.deletePost(postId);
            return ResponseEntity.status(201).body("Post deleted successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        }
    }

    @GetMapping("/posts/{postId}")
    public ResponseEntity<?> getPostDetailsById(@PathVariable UUID postId){
        try {
            PostDTO postReturned = postService.getPostDetailsById(postId);
            return ResponseEntity.status(201).body(postReturned);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        }
    }

    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<?> getAllPostComments(@PathVariable UUID postId) {
        try {
            List<CommentDTO> allPostComments = postService.getAllPostComments(postId);
            return ResponseEntity.status(201).body(allPostComments);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        }
    }

    @GetMapping("/posts/{postId}/votes")
    public ResponseEntity<?> getAllPostVotes(@PathVariable UUID postId) {
        try {
            Map<String, Object> allPostVotes = postService.getAllPostVotes(postId);
            return ResponseEntity.status(201).body(allPostVotes);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        }
    }

}
