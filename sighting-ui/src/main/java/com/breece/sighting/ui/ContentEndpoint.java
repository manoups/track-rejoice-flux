package com.breece.sighting.ui;

import com.breece.content.command.api.CreateContent;
import com.breece.content.query.api.GetContent;
import com.breece.content.query.api.GetContents;
import com.breece.coreapi.content.model.Content;
import com.breece.coreapi.content.model.ContentId;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.web.HandleGet;
import io.fluxzero.sdk.web.HandlePost;
import io.fluxzero.sdk.web.Path;
import io.fluxzero.sdk.web.PathParam;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Path("/content")
public class ContentEndpoint {
    @HandlePost(value = {"","/"})
    ContentId createContent(CreateContent content) {
        var contentId = new ContentId();
        Fluxzero.sendCommandAndWait(new CreateContent(contentId, content.sightingDetails(), content.details()));
        return contentId;
    }

    @HandleGet(value = {"","/"})
    List<Content> getContents() {
        return Fluxzero.queryAndWait(new GetContents());
    }

    @HandleGet(value = {"{id}","{id}/"})
    Content getContent(@PathParam ContentId id) {
        return Fluxzero.queryAndWait(new GetContent(id));
    }

}
