package com.breece.coreapi.facets;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record Pagination(@PositiveOrZero Integer page, @Positive Integer pageSize) {

}
