package com.breece.trackrejoice.content;

import com.breece.trackrejoice.content.command.CreateContent;
import com.breece.trackrejoice.content.model.Content;
import com.breece.trackrejoice.content.model.ContentId;
import com.breece.trackrejoice.content.model.ExtraDetails;
import com.breece.trackrejoice.content.query.GetContents;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.web.HandleGet;
import io.fluxzero.sdk.web.HandlePost;
import io.fluxzero.sdk.web.Path;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Path("/classifieds-ads")
public class ContentEndpoint {
    @HandlePost(value = {"","/"})
    ContentId createContent(ExtraDetails details) {
        var contentId = new ContentId();
        Fluxzero.sendCommandAndWait(new CreateContent(contentId, details));
        return contentId;
    }

    @HandleGet(value = {"","/"})
    List<Content> getUsers() {
        return Fluxzero.queryAndWait(new GetContents());
    }

}
