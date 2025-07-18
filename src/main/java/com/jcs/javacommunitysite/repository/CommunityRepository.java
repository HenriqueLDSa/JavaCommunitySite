package com.jcs.javacommunitysite.repository;

import com.jcs.javacommunitysite.model.Community;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CommunityRepository extends JpaRepository<Community, Long> {
    boolean existsByName(String name);

    Optional<Community> findByName(String name);

    Optional<Community> findById(UUID id);
}