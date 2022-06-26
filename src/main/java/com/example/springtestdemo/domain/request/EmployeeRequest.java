package com.example.springtestdemo.domain.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
@Builder
public class EmployeeRequest {

    private String firstname;

    private String lastname;

    @NotBlank(message="email cannot be null")
    private String email;

    private String otherInfo1;

    private Boolean otherInfo2;
}
