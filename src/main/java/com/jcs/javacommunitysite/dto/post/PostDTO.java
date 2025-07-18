package com.jcs.javacommunitysite.dto.post;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record PostDTO(
        UUID id,
        String userId,
        String communityId,
        String title,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
