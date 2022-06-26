package com.example.springtestdemo.domain.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmployeeResponse {
    private ServiceStatus serviceStatus;
    private String empFullName;
    private String contact;
    private Set<ProjectResponse> projects;
}
