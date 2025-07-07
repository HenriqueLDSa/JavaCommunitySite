package com.jcs.javacommunitysite.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import org.hibernate.Hibernate;

import java.util.Objects;
import java.util.UUID;

@Embeddable
public class PostCategoryId implements java.io.Serializable {
    private static final long serialVersionUID = 8353041658183645019L;
    @Column(name = "post_id", nullable = false)
    private UUID postId;

    @Column(name = "category_id", nullable = false)
    private UUID categoryId;

    public UUID getPostId() {
        return postId;
    }

    public void setPostId(UUID postId) {
        this.postId = postId;
    }

    public UUID getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(UUID categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        PostCategoryId entity = (PostCategoryId) o;
        return Objects.equals(this.postId, entity.postId) &&
                Objects.equals(this.categoryId, entity.categoryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(postId, categoryId);
    }

}