package com.breece.coreapi.user.api.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record UserDetails(@NotBlank String name, String email) {
}
