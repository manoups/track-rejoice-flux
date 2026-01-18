package com.breece.order.api.payment;


import com.breece.coreapi.authentication.RequiresRole;
import com.breece.coreapi.authentication.Role;
import jakarta.validation.constraints.NotBlank;

@RequiresRole(Role.ADMIN)
public record PaymentAccepted(@NotBlank String reference) {
}
