package com.jcs.javacommunitysite.atproto.jetstream.handlers;

import com.jcs.javacommunitysite.atproto.AtUri;
import com.jcs.javacommunitysite.atproto.jetstream.JetstreamHandler;
import com.jcs.javacommunitysite.atproto.records.ForumIdentityRecord;

public class JetstreamForumIdentityHandler implements JetstreamHandler<ForumIdentityRecord> {
    @Override
    public void handleCreated(ForumIdentityRecord record) {
        System.out.println("FORUM IDENTITY CREATED:");
        System.out.println(record.getName());
        System.out.println(record.getDescription());
        System.out.println(record.getAccent());
        System.out.println(record.getAtUri());
    }

    @Override
    public void handleUpdated(ForumIdentityRecord record) {

    }

    @Override
    public void handleDeleted(AtUri<ForumIdentityRecord> atUri) {

    }
}
