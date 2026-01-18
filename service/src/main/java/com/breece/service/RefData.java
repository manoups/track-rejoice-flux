package com.breece.service;

import com.breece.service.api.GetServices;
import com.breece.service.api.model.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.fluxzero.common.FileUtils;
import io.fluxzero.sdk.common.serialization.jackson.JacksonSerializer;
import io.fluxzero.sdk.tracking.handling.HandleQuery;
import io.fluxzero.sdk.tracking.handling.LocalHandler;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import static io.fluxzero.common.ObjectUtils.memoize;

@LocalHandler
@Slf4j
public class RefData {
    private final ObjectMapper objectMapper = JacksonSerializer.defaultObjectMapper;
    private final Supplier<List<Service>> services = memoize(this::loadServices);

//    private final Function<GetServices, Service> getServices = memoize(this::getServices);

//    private Object getServices() {
//    }


    @HandleQuery
    public List<Service> handle(GetServices request) {
        return services.get();
    }

    @SneakyThrows
    private List<Service> loadServices() {
        return objectMapper.readValue(
                loadFile("services.json"),
                objectMapper.getTypeFactory().constructType(com.breece.service.api.model.Service.class));
    }

    private String loadFile(String filename) {
        return FileUtils.loadFile(RefData.class, filename);
    }
}
