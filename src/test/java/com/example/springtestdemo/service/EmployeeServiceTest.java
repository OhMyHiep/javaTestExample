package com.example.springtestdemo.service;

import com.example.springtestdemo.dao.impl.EmployeeDAOImpl;
import com.example.springtestdemo.domain.Employee;
import com.example.springtestdemo.domain.Project;
import com.example.springtestdemo.domain.response.EmployeeResponse;
import com.example.springtestdemo.domain.response.ProjectResponse;
import com.example.springtestdemo.exceptions.DataCreationException;
import com.example.springtestdemo.exceptions.DataNotFoundException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.util.CollectionUtils;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.constraints.AssertTrue;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @InjectMocks
    EmployeeService employeeService;

    @Mock
    EmployeeDAOImpl employeeDAO;

    Employee mockEmployee;
    Set<Project> mockProjectSet;

    @BeforeEach
    void setup() {
        mockEmployee = Employee.builder().id(1).fname("tracy").lastname("lan").email("tracy@gmail.com").build();

        Employee mockLeader = Employee.builder().fname("leader").lastname("proj1").email("leader1@gmail.com").build();
        mockProjectSet = new HashSet<>(Arrays.asList(Project.builder().name("project 100").leader(mockLeader).build(),
                Project.builder().name("project 101").leader(mockLeader).build()));
    }


    @Test
    @DisplayName("get employee by id success scenario")
    void testGetEmployeeById_success() {
        when(employeeDAO.getEmployeeById(1)).thenReturn(mockEmployee);

        Employee employee = employeeService.getEmployeeById(1);
        assertEquals(mockEmployee, employee);
    }

    @Test
    void testGetEmployeeById_failed() {
        when(employeeDAO.getEmployeeById(-1)).thenReturn(null);
        assertThrows(DataNotFoundException.class, () -> employeeService.getEmployeeById(-1));
    }

    @Test
    void testAddEmployee_success() {
        when(employeeDAO.checkExistingEmail(mockEmployee.getEmail())).thenReturn(false);
        when(employeeDAO.addEmployee(mockEmployee)).thenReturn(1);
        assertEquals(1, employeeService.addEmployee(mockEmployee));
    }

    @Test
    void testAddEmployee_failedWhenEmailExists() {
        when(employeeDAO.checkExistingEmail(mockEmployee.getEmail())).thenReturn(true);

        Mockito.verify(employeeDAO, times(0)).addEmployee(mockEmployee);
        assertThrows(DataCreationException.class, () -> employeeService.addEmployee(mockEmployee));
    }

    @Test
    void testGetEmployeeWithProjects_EmployeeIsFoundWithProjects() {
        mockEmployee.setProjects(mockProjectSet);
        when(employeeDAO.getEmployeeWithProjects(1)).thenReturn(Optional.ofNullable(mockEmployee));

        EmployeeResponse employeeResponse = employeeService.getEmployeeWithProjects(1);

        assertEquals(mockEmployee.getFname() + " " + mockEmployee.getLastname(), employeeResponse.getEmpFullName());
        assertEquals(mockEmployee.getEmail(), employeeResponse.getContact());
        assertEquals(mockEmployee.getProjects().size(), employeeResponse.getProjects().size());
        assertTrue(employeeResponse.getServiceStatus().getSuccess());
        mockEmployee.setProjects(null);
    }

    @Test
    void testGetEmployeeWithProjects_EmployeeWithNoProject() {
        when(employeeDAO.getEmployeeWithProjects(1)).thenReturn(Optional.ofNullable(mockEmployee));

        EmployeeResponse employeeResponse = employeeService.getEmployeeWithProjects(1);
        assertEquals(mockEmployee.getFname() + " " + mockEmployee.getLastname(), employeeResponse.getEmpFullName());
        assertEquals(mockEmployee.getEmail(), employeeResponse.getContact());
        assertNotNull(employeeResponse.getProjects());
        assertTrue(employeeResponse.getServiceStatus().getSuccess());
    }

    @Test
    void testGetEmployeeWithProjects_EmployeeIsNotFound() {

        when(employeeDAO.getEmployeeWithProjects(-1)).thenReturn(Optional.empty());

        EmployeeResponse employeeResponse = employeeService.getEmployeeWithProjects(-1);
        assertNull(employeeResponse.getEmpFullName());
        assertFalse(employeeResponse.getServiceStatus().getSuccess());
        assertNotNull(employeeResponse.getServiceStatus().getErrorMessage());
    }
}