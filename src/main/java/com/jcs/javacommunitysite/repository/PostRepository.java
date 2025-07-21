package com.jcs.javacommunitysite.repository;

import com.jcs.javacommunitysite.model.Category;
import com.jcs.javacommunitysite.model.Comment;
import com.jcs.javacommunitysite.model.Community;
import com.jcs.javacommunitysite.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Post> findById(UUID uuid);
    List<Post> findAllByCommunity(Community community);
}