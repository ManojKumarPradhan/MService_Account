package com.bigob.accounts.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class ResponseError {

    private String apiPath;
    private HttpStatus statusCode;
    private String  errorMessage;
    private LocalDateTime errorTime;

}
