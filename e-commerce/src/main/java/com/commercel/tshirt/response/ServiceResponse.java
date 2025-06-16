package com.commercel.tshirt.response;

import lombok.Getter;

import java.util.Collections;
import java.util.Map;

@Getter
public class ServiceResponse<T> {

    private final boolean success;
    private final T data;
    private final Map<String, String> errors;

    public ServiceResponse(T data) {
        this.success = true;
        this.data = data;
        this.errors = Collections.emptyMap();
    }

    public ServiceResponse(Map<String, String> errors) {
        this.success = false;
        this.data = null;
        this.errors = errors;
    }
}