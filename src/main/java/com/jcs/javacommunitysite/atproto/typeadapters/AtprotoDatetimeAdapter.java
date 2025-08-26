package com.jcs.javacommunitysite.atproto.typeadapters;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.time.Instant;

public class AtprotoDatetimeAdapter implements JsonSerializer<Instant> {
    @Override
    public JsonElement serialize(Instant instant, Type type, JsonSerializationContext jsonSerializationContext) {
        return jsonSerializationContext.serialize(instant.toString());
    }
}
