package com.jcs.javacommunitysite.atproto.records;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;

import java.util.Optional;

public abstract class AtprotoRecord {
    @Expose(serialize = false) private String atUri = null;

    public AtprotoRecord(JsonObject json) { }
    public AtprotoRecord() { }

    public Optional<String> getAtUri() {
        return Optional.ofNullable(atUri);
    }

    public void setAtUri(String atUri) {
        this.atUri = atUri;
    }

    public abstract boolean isValid();  // are the contents of this record valid?
    public abstract String getRecordCollection();
}
