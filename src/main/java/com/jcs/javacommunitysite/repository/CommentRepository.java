package com.jcs.javacommunitysite.repository;

import com.jcs.javacommunitysite.model.Comment;
import com.jcs.javacommunitysite.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByPost(Post post);
    Optional<Comment> findById(UUID commentId);
}