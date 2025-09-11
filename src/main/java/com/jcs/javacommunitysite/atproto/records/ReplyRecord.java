package com.jcs.javacommunitysite.atproto.records;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.jcs.javacommunitysite.atproto.AtUri;

import java.time.Instant;

import static com.jcs.javacommunitysite.JavaCommunitySiteApplication.addLexiconPrefix;

public class ReplyRecord extends AtprotoRecord {
    @Expose private String content;
    @Expose private Instant createdAt;
    @Expose private Instant updatedAt = null;
    @Expose private AtUri root;

    public ReplyRecord(AtUri atUri, JsonObject json) {
        super(atUri, json);
        this.content = json.get("content").getAsString();
        this.createdAt = Instant.parse(json.get("createdAt").getAsString());
        this.root = new AtUri(json.get("root").getAsString());
    }

    public ReplyRecord(String content, AtUri root) {
        this.content = content;
        this.createdAt = Instant.now();
        this.root = root;
    }

    @Override
    public boolean isValid() {
        if (content == null || content.isEmpty() || content.length() > 10000) return false;
        if (createdAt == null) return false;
        if (root == null) return false;
        return true;
    }

    @Override
    public String getRecordCollection() {
        return addLexiconPrefix("feed.reply");
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public AtUri getRoot() {
        return root;
    }

    public void setRoot(AtUri root) {
        this.root = root;
    }
}
