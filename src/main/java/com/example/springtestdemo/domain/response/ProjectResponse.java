package com.example.springtestdemo.domain.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProjectResponse {
    private ServiceStatus serviceStatus;
    private String name;
    private String leaderFullName;
}