package org.example.userservice.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties
@Data
public class ResponseMessage<T> {
    private boolean success;
    private T object;
    private List<ResponseError> errors = new ArrayList<>();

    public void putError(String message, String description){
        errors.add(new ResponseError(message,description));
    }
}
