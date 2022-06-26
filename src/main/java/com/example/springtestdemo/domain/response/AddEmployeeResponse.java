package com.example.springtestdemo.domain.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddEmployeeResponse {
    Integer id;
    ServiceStatus serviceStatus;
}
