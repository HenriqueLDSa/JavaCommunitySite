package com.jcs.javacommunitysite.atproto.records;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;

import java.time.Instant;

public class PostRecord extends AtprotoRecord {
    @Expose private String text = null;
    @Expose private Instant createdAt = null;

    public PostRecord(JsonObject json) {
        super(json);
    }

    public PostRecord(String text) {
        this.text = text;
        this.createdAt = Instant.now();
    }

    public PostRecord(String text, Instant createdAt) {
        this.text = text;
        this.createdAt = createdAt;
    }

    @Override
    public boolean isValid() {
        if (text == null) return false;
        if (createdAt == null) return false;
        return true;
    }

    @Override
    public String getRecordCollection() {
        return "app.bsky.feed.post";
    }
}
