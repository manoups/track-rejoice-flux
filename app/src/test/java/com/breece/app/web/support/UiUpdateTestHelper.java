package com.breece.app.web.support;

import com.breece.app.web.api.UiUpdate;
import com.breece.content.api.model.Content;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flipkart.zjsonpatch.JsonPatch;

public final class UiUpdateTestHelper {
    private UiUpdateTestHelper() {
    }

    public static Content extractContent(ObjectMapper objectMapper, String payload) throws Exception {
        UiUpdate update = objectMapper.readValue(payload, UiUpdate.class);
        JsonNode base = objectMapper.createObjectNode();
        JsonNode patched = JsonPatch.apply(update.getPatch(), base);
        return objectMapper.treeToValue(patched, Content.class);
    }
}
