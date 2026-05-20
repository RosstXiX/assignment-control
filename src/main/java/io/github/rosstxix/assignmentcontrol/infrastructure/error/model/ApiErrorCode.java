package io.github.rosstxix.assignmentcontrol.infrastructure.error.model;

import lombok.Getter;

@Getter
public enum ApiErrorCode {
    INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR"),
    VALIDATION_ERROR("VALIDATION_ERROR"),
    BAD_CREDENTIALS("BAD_CREDENTIALS"),
    TOKEN_EXPIRED("TOKEN_EXPIRED"),
    TOKEN_INVALID("TOKEN_INVALID"),
    TOKEN_MISSING("TOKEN_MISSING"),
    AUTHENTICATION_FAILED("AUTHENTICATION_FAILED"),
    ACCESS_DENIED("ACCESS_DENIED"),
    ENTITY_NOT_FOUND("ENTITY_NOT_FOUND"),
    INVALID_REQUEST_BODY("INVALID_REQUEST_BODY"),
    INVALID_JSON("INVALID_JSON"),
    INVALID_FORMAT("INVALID_FORMAT"),
    BUSINESS_ERROR("BUSINESS_ERROR");

    private final String value;

    ApiErrorCode(String value) {
        this.value = value;
    }
}
