package com.breece.trackrejoice.classifiedsad;

import com.breece.trackrejoice.classifiedsad.command.CreateClassifiedsAd;
import com.breece.trackrejoice.classifiedsad.model.ClassifiedsAd;
import com.breece.trackrejoice.classifiedsad.model.ClassifiedsAdId;
import com.breece.trackrejoice.classifiedsad.model.ExtraDetails;
import com.breece.trackrejoice.classifiedsad.query.GetClassifiedAds;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.web.HandleGet;
import io.fluxzero.sdk.web.HandlePost;
import io.fluxzero.sdk.web.Path;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Path("/classifieds-ads")
public class ClassifiedsAdEndpoint {
    @HandlePost(value = {"","/"})
    ClassifiedsAdId createClassifiedsAd(ExtraDetails details) {
        var classifiedsAdId = new ClassifiedsAdId();
        Fluxzero.sendCommandAndWait(new CreateClassifiedsAd(classifiedsAdId, details));
        return classifiedsAdId;
    }

    @HandleGet(value = {"","/"})
    List<ClassifiedsAd> getUsers() {
        return Fluxzero.queryAndWait(new GetClassifiedAds());
    }

}
