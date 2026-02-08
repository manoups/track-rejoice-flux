package com.breece.app.web;

import com.breece.content.api.model.Content;
import com.breece.content.api.model.ContentId;
import com.breece.content.command.api.CreateContent;
import com.breece.content.command.api.DeleteContent;
import com.breece.content.query.api.ContentDocument;
import com.breece.content.query.api.GetContent;
import com.breece.content.query.api.GetContents;
import com.breece.content.command.api.CreateContentDTO;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.web.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Path("/api/content")
public class ContentEndpoint {
    @HandlePost(value = {"","/"})
    ContentId createContent(CreateContentDTO content) {
        CreateContent command = new CreateContent(content);
        Fluxzero.sendCommandAndWait(command);
        return command.contentId();
    }

    @HandleGet(value = {"","/"})
    List<ContentDocument> getContents() {
        return Fluxzero.queryAndWait(new GetContents());
    }

    @HandleGet(value = {"{id}","{id}/"})
    Content getContent(@PathParam ContentId id) {
        return Fluxzero.queryAndWait(new GetContent(id));
    }

    @HandleDelete(value = {"{id}","{id}/"})
    ContentId deleteContent(@PathParam ContentId id) {
        Fluxzero.sendCommandAndWait(new DeleteContent(id));
        return id;
    }
}
