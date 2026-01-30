package com.breece.content.command.api;

import com.breece.content.api.model.ContentId;
import org.wildfly.common.annotation.NotNull;

public record CompleteContent(@NotNull ContentId contentId) implements ContentUpdate {
}
