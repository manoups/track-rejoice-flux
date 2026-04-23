package com.breece.app.web.api;

public record SseEventData(ChangeType changeType, Object payload) {
    public enum ChangeType {
        INITIAL, CREATED, UPDATED, DELETED
    }
}
