package com.breece.app.web.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

public record UiUpdate(Type type, @JsonSerialize(using = ToStringSerializer.class) Long index, String id,
                       JsonNode patch) {
    public enum Type {
        Sighting, UserProfile, Content
    }
}
