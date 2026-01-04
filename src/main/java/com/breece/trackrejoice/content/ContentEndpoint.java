package com.breece.trackrejoice.content;

import com.breece.trackrejoice.content.command.CreateContent;
import com.breece.trackrejoice.content.model.Content;
import com.breece.trackrejoice.content.model.ContentId;
import com.breece.trackrejoice.content.query.GetContent;
import com.breece.trackrejoice.content.query.GetContents;
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
        Fluxzero.sendCommandAndWait(new CreateContent(contentId, content.lostAt(), content.details()));
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
