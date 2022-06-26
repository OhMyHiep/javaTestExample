package com.example.springtestdemo.service;

import com.example.springtestdemo.dao.EmployeeDAO;
import com.example.springtestdemo.domain.Employee;
import com.example.springtestdemo.domain.response.EmployeeResponse;
import com.example.springtestdemo.domain.response.ProjectResponse;
import com.example.springtestdemo.domain.response.ServiceStatus;
import com.example.springtestdemo.exceptions.DataCreationException;
import com.example.springtestdemo.exceptions.DataNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class EmployeeService {

    private EmployeeDAO employeeDAO;

    @Autowired
    public void setEmployeeDAO(EmployeeDAO employeeDAO) {
        this.employeeDAO = employeeDAO;
    }

    @Transactional
    public Employee getEmployeeById(int id) {
        return Optional.ofNullable(employeeDAO.getEmployeeById(id))
                .orElseThrow(() -> new DataNotFoundException("Employee Not Match"));
    }

    @Transactional
    public Integer addEmployee(Employee employee) {
        //check if employee email exists first, if yes, continue
        if(employeeDAO.checkExistingEmail(employee.getEmail())){
            throw new DataCreationException("Email Already Exists");
        }
        return employeeDAO.addEmployee(employee);
    }

    @Transactional
    public EmployeeResponse getEmployeeWithProjects(Integer id) {

        Optional<Employee> employeeOptional = employeeDAO.getEmployeeWithProjects(id);

        if (!employeeOptional.isPresent()) {
            return EmployeeResponse.builder()
                    .serviceStatus(ServiceStatus.builder()
                            .success(false)
                            .errorMessage("Employee Not Found").build())
                    .build();
        }

        Employee employee = employeeOptional.get();

        return EmployeeResponse.builder()
                .empFullName(employee.getFname() + " " + employee.getLastname())
                .contact(employee.getEmail())
                .projects(Optional.ofNullable(employee.getProjects())
                        .map(Collection::stream)
                        .orElseGet(Stream::empty)
                        .map(p -> ProjectResponse.builder()
                                .name(p.getName())
                                .leaderFullName(p.getLeader().getFname() + " " + p.getLeader().getLastname())
                                .build())
                        .collect(Collectors.toSet()))
                .serviceStatus(ServiceStatus.builder()
                        .success(true)
                        .build())
                .build();
    }

}
