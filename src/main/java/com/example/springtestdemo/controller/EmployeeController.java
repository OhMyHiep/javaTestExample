package com.example.springtestdemo.controller;

import com.example.springtestdemo.domain.Employee;
import com.example.springtestdemo.domain.request.EmployeeRequest;
import com.example.springtestdemo.domain.response.AddEmployeeResponse;
import com.example.springtestdemo.domain.response.EmployeeResponse;
import com.example.springtestdemo.domain.response.ServiceStatus;
import com.example.springtestdemo.exceptions.InvalidTokenException;
import com.example.springtestdemo.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.security.InvalidParameterException;
import java.util.Optional;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    private EmployeeService employeeService;

    @Autowired
    public void setEmployeeService(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/{id}")
    public Employee getEmployee(@PathVariable Integer id) {
        return employeeService.getEmployeeById(id);
    }

    @GetMapping("/projectDetails")
    public EmployeeResponse getEmployeeWithProjects(@RequestHeader(value = "Authorization", required = false) Optional<String> token) {
        // assume passing data here is jwt token
        // need to do some decoding and get employeeId from the token
        if(!token.orElseThrow(InvalidTokenException::new).startsWith("Bearer ")){
            throw new InvalidTokenException();
        }
        String decodeId = token.get().substring(7);
        Integer id = Integer.parseInt(decodeId);
        return employeeService.getEmployeeWithProjects(id);
    }

    @PostMapping
    public AddEmployeeResponse addEmployee(@Valid @RequestBody EmployeeRequest employeeRequest) {
        //process the request, validate the request etc.
        Employee employee = Employee.builder().fname(employeeRequest.getFirstname())
                .lastname(employeeRequest.getLastname())
                .email(employeeRequest.getEmail())
                .build();
        Integer id = employeeService.addEmployee(employee);

        //use other information to call another service layer etc.
        //....

        return AddEmployeeResponse.builder()
                .id(id)
                .serviceStatus(ServiceStatus.builder().success(true).build())
                .build();
    }
}
