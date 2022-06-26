package com.example.springtestdemo.dao;



import com.example.springtestdemo.domain.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeDAO {

    Employee getEmployeeById(Integer id);

    Integer addEmployee(Employee employee);

    Boolean checkExistingEmail(String email);

    Optional<Employee> getEmployeeWithProjects(Integer id);
}
