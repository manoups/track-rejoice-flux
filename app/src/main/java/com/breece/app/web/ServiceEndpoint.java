package com.breece.app.web;

import com.breece.service.api.GetBasicService;
import com.breece.service.api.model.Service;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.web.HandleGet;
import io.fluxzero.sdk.web.Path;
import org.springframework.stereotype.Component;

@Component
@Path("/api/services")
public class ServiceEndpoint {

    @HandleGet(value = {"basic", "basic/"})
    Service getBasicService() {
        return Fluxzero.queryAndWait(new GetBasicService());
    }
}
