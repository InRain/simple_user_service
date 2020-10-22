package org.example.userservice.response;

import lombok.Data;

@Data
public class ResponseError {

    private String message;
    private String description;

    public ResponseError(String message, String description) {
        this.message = message;
        this.description = description;
    }
}
