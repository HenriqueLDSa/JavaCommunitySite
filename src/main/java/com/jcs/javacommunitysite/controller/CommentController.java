package com.jcs.javacommunitysite.controller;

import com.jcs.javacommunitysite.dto.comment.CommentDTO;
import com.jcs.javacommunitysite.dto.comment.CreateCommentRequest;
import com.jcs.javacommunitysite.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/comments")
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
    @PostMapping("/create")
    public ResponseEntity<?> createComment(@RequestBody CreateCommentRequest request) {
        try {
            CommentDTO created = commentService.createComment(request);
            return ResponseEntity.status(201).body(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}
