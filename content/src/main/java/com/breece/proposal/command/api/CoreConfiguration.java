package com.breece.proposal.command.api;

import com.breece.content.api.model.Content;
import com.breece.proposal.command.api.model.LinkedSighting;
import com.breece.sighting.api.model.Sighting;
import io.fluxzero.sdk.configuration.FluxzeroBuilder;
import io.fluxzero.sdk.configuration.spring.FluxzeroCustomizer;
import io.fluxzero.sdk.persisting.caching.DefaultCache;
import org.springframework.stereotype.Component;

@Component
public class CoreConfiguration implements FluxzeroCustomizer {

    @Override
    public FluxzeroBuilder customize(FluxzeroBuilder builder) {
        return builder.withAggregateCache(Content.class, new DefaultCache(500))
                .withAggregateCache(Sighting.class, new DefaultCache(500))
                .withAggregateCache(LinkedSighting.class, new DefaultCache(250_000))
                .disableScheduledCommandHandler();
    }
}
