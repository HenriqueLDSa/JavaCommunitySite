package com.jcs.javacommunitysite.repository;

import com.jcs.javacommunitysite.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmailAndUsername(String email, String username);

    Optional<User> findByUsername(String username);
}