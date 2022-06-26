package com.example.springtestdemo.dao.impl;

import com.example.springtestdemo.domain.Employee;
import com.example.springtestdemo.domain.Project;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles(value = "test")
@SpringBootTest
public class EmployeeDAOImplTest {

    @Autowired
    EmployeeDAOImpl employeeDAO;

    Employee mockEmployee;
    Employee mockEmployeeWithProjects;

    @BeforeEach
    public void setup() {
        Employee.EmployeeBuilder employeeBuilder = Employee.builder().fname("tracy").lastname("lan").email("tracy@gmail.com");
        mockEmployee = employeeBuilder.build();

        Employee mockLeader = Employee.builder().fname("leader").lastname("proj1").email("leader1@gmail.com").build();
        mockEmployeeWithProjects = employeeBuilder
                .projects(new HashSet<>(Arrays.asList(Project.builder().name("proj1").leader(mockLeader).build()))).build();
    }


    @Test
    @Transactional
    public void testGetEmployeeById_found() {
        Integer id = employeeDAO.addEmployee(mockEmployee);
        assertNotNull(id);
        mockEmployee.setId(id);
        assertEquals(mockEmployee, employeeDAO.getEmployeeById(id));
        mockEmployee.setId(null);
    }

    @Test
    @Transactional
    public void testGetEmployeeById_notFound() {
        assertNull(employeeDAO.getEmployeeById(-1));
    }

    @Test
    @Transactional
    public void testCheckExistingEmail_found() {
        employeeDAO.addEmployee(mockEmployee);
        assertTrue(employeeDAO.checkExistingEmail(mockEmployee.getEmail()));
    }

    @Test
    @Transactional
    public void testCheckExistingEmail_NotFound() {
        assertFalse(employeeDAO.checkExistingEmail(mockEmployee.getEmail()));
    }

    @Test
    @Transactional
    public void testGetEmployeeWithProjects_whenEmployeeWithProjects() {
        Integer id = employeeDAO.addEmployee(mockEmployeeWithProjects);
        mockEmployeeWithProjects.setId(id);
        assertEquals(mockEmployeeWithProjects, employeeDAO.getEmployeeWithProjects(id).orElseGet(null));
        mockEmployeeWithProjects.setId(null);
    }

    @Test
    @Transactional
    public void testGetEmployeeWithProjects_whenEmployeeNoProject() {
        Integer id = employeeDAO.addEmployee(mockEmployee);
        mockEmployee.setId(id);
        assertEquals(mockEmployee, employeeDAO.getEmployeeWithProjects(id).orElseGet(null));
        mockEmployee.setId(null);
    }

    @Test
    @Transactional
    public void testGetEmployeeWithProjects_whenEmployeeNotFound(){
        assertEquals(Optional.empty(), employeeDAO.getEmployeeWithProjects(-1));
    }
}
