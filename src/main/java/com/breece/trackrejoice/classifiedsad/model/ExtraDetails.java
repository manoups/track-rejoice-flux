package com.breece.trackrejoice.classifiedsad.model;

import com.breece.trackrejoice.payments.api.model.PaymentId;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.fluxzero.common.search.Facet;
import lombok.Getter;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "@class")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Pet.class, name = "Pet"),
        @JsonSubTypes.Type(value = Keys.class, name = "Keys")
})
@Getter
public abstract class ExtraDetails {
    PaymentId paymentId;
    @Facet
    String subtype;
}
