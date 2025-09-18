package com.jcs.javacommunitysite.atproto.jetstream.handlers;

import com.google.gson.JsonObject;
import com.jcs.javacommunitysite.atproto.AtUri;
import com.jcs.javacommunitysite.atproto.jetstream.JetstreamHandler;
import com.jcs.javacommunitysite.atproto.records.PostRecord;

public class JetstreamPostHandler implements JetstreamHandler {
    @Override
    public void handleCreated(AtUri atUri, JsonObject recordJson) {
        PostRecord record = new PostRecord(atUri, recordJson);
        System.out.println("Post record received from AtProto!");
        System.out.println(" - Title: " + record.getTitle());
        System.out.println(" - Content: " + record.getContent());
        System.out.println(" - Category: " + record.getCategory());
        System.out.println(" - Forum: " + record.getForum());
        System.out.println(" - Created At: " + record.getCreatedAt());
        System.out.println(" - Updated At: " + record.getUpdatedAt());
    }

    @Override
    public void handleUpdated(AtUri atUri, JsonObject recordJson) {
        PostRecord record = new PostRecord(atUri, recordJson);
    }

    @Override
    public void handleDeleted(AtUri atUri) {

    }
}
