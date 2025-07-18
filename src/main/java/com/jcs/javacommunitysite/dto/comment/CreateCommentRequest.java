package com.jcs.javacommunitysite.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateCommentRequest {
    private String userId;
    private String postId;
    private String content;
}
