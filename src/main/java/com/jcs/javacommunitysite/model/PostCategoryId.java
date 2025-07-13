package com.jcs.javacommunitysite.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostCategoryId implements java.io.Serializable {
    private UUID postId;
    private UUID categoryId;
}
