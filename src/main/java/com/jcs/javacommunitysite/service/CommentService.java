package com.jcs.javacommunitysite.service;

import com.jcs.javacommunitysite.dto.comment.CommentDTO;
import com.jcs.javacommunitysite.dto.comment.CreateCommentRequest;
import com.jcs.javacommunitysite.dto.comment.UpdateCommentRequest;
import com.jcs.javacommunitysite.dto.post.PostDTO;
import com.jcs.javacommunitysite.model.Comment;
import com.jcs.javacommunitysite.model.Post;
import com.jcs.javacommunitysite.model.User;
import com.jcs.javacommunitysite.repository.CommentRepository;
import com.jcs.javacommunitysite.repository.PostRepository;
import com.jcs.javacommunitysite.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CommentService {

    private CommentRepository commentRepo;
    private UserRepository userRepo;
    private PostRepository postRepo;

    public CommentService(CommentRepository commentRepo, UserRepository userRepo, PostRepository postRepo) {
        this.commentRepo = commentRepo;
        this.userRepo = userRepo;
        this.postRepo = postRepo;
    }

    public CommentDTO createComment(CreateCommentRequest request, UUID userId, UUID postId){
        if (request.getContent() == null || request.getContent().isEmpty()) {
            throw new IllegalArgumentException("Comment content cannot be empty");
        }

        User commentUser = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Post commentPost = postRepo.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        Comment comment = Comment.builder()
                .user(commentUser)
                .post(commentPost)
                .content(request.getContent())
                .build();

        commentRepo.save(comment);

        return CommentDTO.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }

    public CommentDTO updateComment(UpdateCommentRequest request, UUID commentId){
        if (request.getContent() == null || request.getContent().isEmpty()) {
            throw new IllegalArgumentException("Comment content cannot be empty");
        }

        Comment commentToUpdate = commentRepo.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));

        commentToUpdate.setContent(request.getContent());

        commentRepo.save(commentToUpdate);

        return CommentDTO.builder()
                .content(commentToUpdate.getContent())
                .updatedAt(commentToUpdate.getUpdatedAt())
                .createdAt(commentToUpdate.getCreatedAt())
                .build();
    }

    public void deleteComment(UUID commentId) {

        Comment commentToDelete = commentRepo.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));

        if(commentToDelete != null){
            commentRepo.delete(commentToDelete);
        }
    }

    public CommentDTO getCommentDetailsById(UUID commentId){

        Comment comment = commentRepo.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));

        return CommentDTO.builder()
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }
}
