package com.breece.content.command.api;

import com.breece.coreapi.content.model.ContentId;
import jakarta.validation.constraints.NotNull;

import java.time.Duration;

public record PublishContent(@NotNull ContentId contentId, Duration duration) {
}
