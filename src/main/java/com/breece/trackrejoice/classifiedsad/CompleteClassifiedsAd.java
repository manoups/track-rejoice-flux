package com.breece.trackrejoice.classifiedsad;

import com.breece.trackrejoice.classifiedsad.command.ClassifiedsAdUpdate;
import com.breece.trackrejoice.classifiedsad.model.ClassifiedsAdId;
import org.wildfly.common.annotation.NotNull;

public record CompleteClassifiedsAd(@NotNull ClassifiedsAdId classifiedsAdId) implements ClassifiedsAdUpdate {
}
