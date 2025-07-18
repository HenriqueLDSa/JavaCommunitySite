package com.jcs.javacommunitysite.repository;

import com.jcs.javacommunitysite.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Post> findById(UUID uuid);
}