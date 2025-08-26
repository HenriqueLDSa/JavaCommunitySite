package com.jcs.javacommunitysite.atproto;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.jcs.javacommunitysite.atproto.records.AtprotoRecord;

import java.lang.reflect.Type;

public class AtUri<T extends AtprotoRecord> implements JsonSerializer<AtUri<T>> {

    private String atUri;
    private T cachedRecord;

    public AtUri(String atUri) {
        this.atUri = atUri;
    }

    public AtUri(String did, String collection, String record) {
        this.atUri = new StringBuilder()
                .append("at://")
                .append(did).append("/")
                .append(collection).append("/")
                .append(record)
                .toString();
    }

    public AtUri(T cachedRecord) {
        this.cachedRecord = cachedRecord;
        this.atUri = cachedRecord.getAtUri().orElseThrow();
    }

    @Override
    public JsonElement serialize(AtUri<T> tAtUri, Type type, JsonSerializationContext jsonSerializationContext) {
        return jsonSerializationContext.serialize(atUri);
    }

    public T fetch() {
        if (cachedRecord != null) {
            return cachedRecord;
        }

        return null; // TODO
    }
}
