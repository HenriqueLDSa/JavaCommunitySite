package com.jcs.javacommunitysite.atproto.records;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.jcs.javacommunitysite.atproto.exceptions.AtprotoInvalidRecord;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;

public class ForumIdentityRecord extends AtprotoRecord {
    @Expose private String name;
    @Expose private String description;
    // @Expose private String logo = null;
    @Expose private Color accent = null;

    public ForumIdentityRecord(JsonObject json) {
        super(json);
    }

    public ForumIdentityRecord(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public ForumIdentityRecord(String name, String description, Color accent) {
        this.name = name;
        this.description = description;
        // this.logo = logo;
        this.accent = accent;
    }

    @Override
    public boolean isValid() {
        if (name == null) return false;
        if (description == null) return false;
        return true;
    }

    @Override
    public String getRecordCollection() {
        return "dev.fudgeu.experimental.atforumv1.forum.identity";
    }
}
