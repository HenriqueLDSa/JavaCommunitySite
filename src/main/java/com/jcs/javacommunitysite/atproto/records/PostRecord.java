package com.jcs.javacommunitysite.atproto.records;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.jcs.javacommunitysite.atproto.AtUri;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class PostRecord extends AtprotoRecord {

    @Expose private String text;
    @Expose private Instant createdAt;
    @Expose private AtUri<ForumCategoryRecord> category;
    @Expose private String forum;  // should be a DID
    @Expose private List<String> tags;
    @Expose private AtUri<ReplyRecord> solution;

    public PostRecord(JsonObject json) {
        super(json);
        this.text = json.get("text").getAsString();
        this.createdAt = Instant.parse(json.get("createdAt").getAsString());
        this.category = new AtUri<>(json.get("category").getAsString());
        this.forum = json.get("forum").getAsString();
        this.tags = new ArrayList<>();
        json.get("tags").getAsJsonArray().forEach(tag -> this.tags.add(tag.getAsString()));
        this.solution = json.has("solution") ? new AtUri<>(json.get("solution").getAsString()) : null;
    }

    public PostRecord(String text, AtUri<ForumCategoryRecord> category, String forum) {
        this.text = text;
        this.createdAt = Instant.now();
        this.category = category;
        this.forum = forum;
        this.tags = new ArrayList<>();
    }

    public PostRecord(String text, AtUri<ForumCategoryRecord> category, String forum, List<String> tags) {
        this.text = text;
        this.createdAt = Instant.now();
        this.category = category;
        this.forum = forum;
        this.tags = tags;
    }

    @Override
    public boolean isValid() {
        if (text == null || text.isEmpty() || text.length() > 10000) return false;
        if (createdAt == null) return false;
        if (category == null) return false;
        if (forum == null) return false;
        if (tags == null || tags.stream().anyMatch(t -> t.length() > 25)) return false;
        return true;
    }

    @Override
    public String getRecordCollection() {
        return "dev.fudgeu.experimental.atforumv1.feed.post";
    }
}
