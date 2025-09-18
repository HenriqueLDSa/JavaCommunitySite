package com.jcs.javacommunitysite.atproto.typeadapters;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.jcs.javacommunitysite.atproto.AtUri;

import java.lang.reflect.Type;

public class AtprotoAtUriAdapter implements JsonSerializer<AtUri> {
    @Override
    public JsonElement serialize(AtUri tAtUri, Type type, JsonSerializationContext jsonSerializationContext) {
        return jsonSerializationContext.serialize(tAtUri.toString());
    }
}
