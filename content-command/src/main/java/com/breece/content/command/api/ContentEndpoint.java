package com.breece.content.command.api;

import com.breece.coreapi.content.model.ContentId;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.web.HandlePost;
import io.fluxzero.sdk.web.Path;
import org.springframework.stereotype.Component;

@Component
@Path("/content")
public class ContentEndpoint {
    @HandlePost(value = {"","/"})
    ContentId createContent(CreateContent content) {
        var contentId = new ContentId();
        Fluxzero.sendCommandAndWait(new CreateContent(contentId, content.sightingDetails(), content.details()));
        return contentId;
    }
}
