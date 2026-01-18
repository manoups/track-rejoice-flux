package com.breece.service.api;

import com.breece.service.api.model.Service;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.tracking.handling.HandleQuery;
import io.fluxzero.sdk.tracking.handling.Request;

import java.util.List;

public record GetServices() implements Request<List<Service>> {
}
