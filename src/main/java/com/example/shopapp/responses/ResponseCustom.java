package com.example.shopapp.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Builder
public class ResponseCustom<T> {
    private final int status;
    private final String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    // For GET, POST
    public ResponseCustom(int status, String message) {
        this.status = status;
        this.message = message;
    }

    // For PUT, PATCH, DELETE
    public ResponseCustom(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }
}
