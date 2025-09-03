package com.jcs.javacommunitysite.atproto.records;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.jcs.javacommunitysite.atproto.AtUri;

import java.time.Instant;

public class VoteRecord extends AtprotoRecord {
    @Expose private AtUri<PostRecord> root;
    @Expose private Instant createdAt;
    @Expose private int value;

    public VoteRecord(AtUri<AtprotoRecord> atUri, JsonObject json) {
        super(atUri, json);
        this.root = new AtUri<>(json.get("root").getAsString());
        this.createdAt = Instant.parse(json.get("createdAt").getAsString());
        this.value = json.get("value").getAsInt();
    }

    public VoteRecord(AtUri<PostRecord> root, int value) {
        this.root = root;
        this.createdAt = Instant.now();
        this.value = value;
    }

    @Override
    public boolean isValid() {
        if (root == null) return false;
        if (createdAt == null) return false;
        if (value != -1 && value != 1) return false;
        return true;
    }

    @Override
    public String getRecordCollection() {
        return "dev.fudgeu.experimental.atforumv1.feed.vote";
    }
}
