package com.jcs.javacommunitysite.controller;

import com.jcs.javacommunitysite.dto.comment.CommentDTO;
import com.jcs.javacommunitysite.dto.comment.CreateCommentRequest;
import com.jcs.javacommunitysite.dto.comment.UpdateCommentRequest;
import com.jcs.javacommunitysite.dto.post.PostDTO;
import com.jcs.javacommunitysite.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api")
public class CommentController {

    CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @Operation(summary = "Create a new comment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Comment created successfully"),
            @ApiResponse(responseCode = "404", description = "User or Post not found"),
    })
    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<?> createCommentOnPost(@RequestHeader UUID userId, @PathVariable UUID postId, @RequestBody CreateCommentRequest request) {
        try {
            CommentDTO commentCreated = commentService.createComment(request, userId, postId);
            return ResponseEntity.status(201).body(commentCreated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @PutMapping("/comments/{commentId}")
    public ResponseEntity<?> updateComment(@PathVariable UUID commentId, @RequestBody UpdateCommentRequest request){
        try {
            CommentDTO created = commentService.updateComment(request, commentId);
            return ResponseEntity.status(201).body(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        }
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable UUID commentId){
        try {
            commentService.deleteComment(commentId);
            return ResponseEntity.status(201).body("Comment deleted successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        }
    }


    @GetMapping("/comments/{commentId}")
    public ResponseEntity<?> getCommentDetailsById(@PathVariable UUID commentId){
        try {
            CommentDTO commentReturned = commentService.getCommentDetailsById(commentId);
            return ResponseEntity.status(201).body(commentReturned);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        }
    }
}
