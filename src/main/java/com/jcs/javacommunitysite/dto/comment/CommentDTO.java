package com.jcs.javacommunitysite.dto.comment;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record CommentDTO(
        UUID id,
        String userId,
        String postId,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
