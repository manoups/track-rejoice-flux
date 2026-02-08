package com.breece.coreapi.common;

public enum SightingEnum {
    DOG(SightingType.PET),
    CAT(SightingType.PET),

    KEYS(SightingType.ITEM),
    JEWELERY(SightingType.ITEM),
    CARD(SightingType.ITEM);

    private final SightingType type;

    SightingEnum(SightingType type) {
        this.type = type;
    }

    public SightingType type() {
        return type;
    }
}