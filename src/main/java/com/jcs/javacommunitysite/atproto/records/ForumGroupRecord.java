package com.jcs.javacommunitysite.atproto.records;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.jcs.javacommunitysite.atproto.AtUri;

public class ForumGroupRecord extends AtprotoRecord {
    @Expose private String name;
    @Expose private String description;

    public ForumGroupRecord(AtUri<AtprotoRecord> atUri, JsonObject json) {
        super(atUri, json);
        this.name = json.get("name").getAsString();
        this.description = json.has("description") ? json.get("description").getAsString() : null;
    }

    public ForumGroupRecord(String name) {
        this.name = name;
    }

    public ForumGroupRecord(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @Override
    public boolean isValid() {
        if (name == null) return false;
        return true;
    }

    @Override
    public String getRecordCollection() {
        return "dev.fudgeu.experimental.atforumv1.forum.group";
    }
}
