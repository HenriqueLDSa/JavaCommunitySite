package com.jcs.javacommunitysite.atproto.jetstream;

import com.google.gson.JsonObject;
import com.jcs.javacommunitysite.atproto.AtUri;
import com.jcs.javacommunitysite.atproto.records.AtprotoRecord;

public interface JetstreamHandler {
    public void handleCreated(AtUri<AtprotoRecord> atUri, JsonObject recordJson);
    public void handleUpdated(AtUri<AtprotoRecord> atUri, JsonObject updatedFields);
    public void handleDeleted(AtUri<AtprotoRecord> atUri);
}
