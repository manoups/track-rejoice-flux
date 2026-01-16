package com.breece.content.command.api;

import com.breece.common.model.ContentId;
import org.wildfly.common.annotation.NotNull;

public record CancelContent(@NotNull ContentId contentId) implements ContentUpdate {
}
