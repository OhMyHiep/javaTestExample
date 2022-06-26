package com.example.springtestdemo.domain.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServiceStatus {
    private Boolean success;
    private String errorMessage;
}
