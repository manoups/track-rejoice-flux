package com.breece.trackrejoice.content;

import com.breece.trackrejoice.content.command.ContentUpdate;
import com.breece.trackrejoice.content.model.ContentId;
import org.wildfly.common.annotation.NotNull;

public record CancelContent(@NotNull ContentId contentId) implements ContentUpdate {
}
