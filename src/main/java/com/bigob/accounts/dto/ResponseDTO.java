package com.bigob.accounts.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ResponseDTO<T> {

    private String statusCode;

    private String statusMessage;

    private T body;
}
