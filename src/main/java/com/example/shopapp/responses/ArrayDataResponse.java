package com.example.shopapp.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.LinkedList;

@Data
@Builder
@AllArgsConstructor
public class ArrayDataResponse<T> {
    private final int status;
    private final String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Iterable<T> data = new LinkedList<>();
}
