package com.breece.content.command.api;

import com.breece.coreapi.content.model.ContentId;
import org.wildfly.common.annotation.NotNull;

public record CompleteContent(@NotNull ContentId contentId) implements ContentUpdate {
}
