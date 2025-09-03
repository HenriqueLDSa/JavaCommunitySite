package com.jcs.javacommunitysite.atproto.jetstream;

import com.jcs.javacommunitysite.atproto.AtUri;
import com.jcs.javacommunitysite.atproto.records.AtprotoRecord;

public interface JetstreamHandler<T extends AtprotoRecord> {
    public void handleCreated(T record);
    public void handleUpdated(T record);
    public void handleDeleted(AtUri<T> atUri);
}
