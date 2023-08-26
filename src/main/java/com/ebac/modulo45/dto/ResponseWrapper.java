package com.ebac.modulo45.dto;

import lombok.Data;

@Data
public class ResponseWrapper<T> {
    private boolean success;
    private String message;
    private ResponseEntity<T> responseEntity;
}
