package com.example.springtestdemo.controller;

import com.example.springtestdemo.domain.Employee;
import com.example.springtestdemo.domain.request.EmployeeRequest;
import com.example.springtestdemo.domain.response.AddEmployeeResponse;
import com.example.springtestdemo.domain.response.EmployeeResponse;
import com.example.springtestdemo.domain.response.ServiceStatus;
import com.example.springtestdemo.exceptions.DataNotFoundException;
import com.example.springtestdemo.service.EmployeeService;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(EmployeeController.class)
@ExtendWith(MockitoExtension.class)
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    EmployeeService employeeService;

    @BeforeEach
    public void init() {
    }

    @Test
    public void testGetEmployee_success() throws Exception {
        Employee mockEmployee = Employee.builder().id(1).fname("tracy").lastname("lan").email("tracy@gmail.com").build();
        when(employeeService.getEmployeeById(1)).thenReturn(mockEmployee);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/employee/{id}", "1")
                            .accept(MediaType.APPLICATION_JSON))
                            .andExpect(status().isOk())
                            .andReturn();
        Employee employee = new Gson().fromJson(result.getResponse().getContentAsString(), Employee.class);
        assertEquals(mockEmployee.toString(), employee.toString());
    }

    @Test
    public void testGetEmployee_whenEmployeeNotFound() throws Exception {
        when(employeeService.getEmployeeById(-1)).thenThrow(new DataNotFoundException("Employee Not Found"));
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/employee/{id}", "-1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        ServiceStatus serviceStatus = new Gson().fromJson(result.getResponse().getContentAsString(), ServiceStatus.class);
        assertFalse(serviceStatus.getSuccess());
        assertNotNull(serviceStatus.getErrorMessage());
    }

    @Test
    public void testGetEmployee_invalidUserInputRequest() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/employee/{id}", "a")
                            .accept(MediaType.APPLICATION_JSON))
                            .andExpect(status().isBadRequest())
                            .andReturn();
        ServiceStatus serviceStatus = new Gson().fromJson(result.getResponse().getContentAsString(), ServiceStatus.class);
        assertFalse(serviceStatus.getSuccess());
        assertNotNull(serviceStatus.getErrorMessage());
    }

    @Test
    public void testGetEmployeeWithProjects_WithValidToken() throws Exception {
        EmployeeResponse mockEmployeeResponse = EmployeeResponse.builder().empFullName("tracy lan")
                .serviceStatus(ServiceStatus.builder().success(true).build()).build();
        when(employeeService.getEmployeeWithProjects(1)).thenReturn(mockEmployeeResponse);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/employee/projectDetails")
                            .header("Authorization", "Bearer 1").accept(MediaType.APPLICATION_JSON))
                            .andExpect(status().isOk())
                            .andReturn();
        EmployeeResponse employeeResponse = new Gson().fromJson(result.getResponse().getContentAsString(), EmployeeResponse.class);

        assertEquals(mockEmployeeResponse.getEmpFullName(), employeeResponse.getEmpFullName());
        assertTrue(employeeResponse.getServiceStatus().getSuccess());
        assertNull(employeeResponse.getServiceStatus().getErrorMessage());
    }

    @Test
    public void testGetEmployeeWithProjects_WithNonBearerToken() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/employee/projectDetails")
                            .header("Authorization", "1"))
                            .andExpect(status().isForbidden()).andReturn();
        ServiceStatus serviceStatus = new Gson().fromJson(result.getResponse().getContentAsString(), ServiceStatus.class);
        assertFalse(serviceStatus.getSuccess());
        assertNotNull(serviceStatus.getErrorMessage());
    }


    @Test
    public void testGetEmployeeWithProjects_WithoutToken() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/employee/projectDetails"))
                            .andExpect(status().isForbidden())
                            .andReturn();
        ServiceStatus serviceStatus = new Gson().fromJson(result.getResponse().getContentAsString(), ServiceStatus.class);
        assertFalse(serviceStatus.getSuccess());
        assertNotNull(serviceStatus.getErrorMessage());
    }

    @Test
    public void testAddEmployee_success() throws Exception {
        EmployeeRequest employeeRequest = EmployeeRequest.builder().firstname("tracy").lastname("lan").email("tracy@gmail.com").build();

        when(employeeService.addEmployee(any())).thenReturn(1);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(employeeRequest))
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().is2xxSuccessful())
                        .andReturn();
        AddEmployeeResponse addEmployeeResponse = new Gson().fromJson(result.getResponse().getContentAsString(), AddEmployeeResponse.class);
        assertEquals(1, addEmployeeResponse.getId());
        assertTrue(addEmployeeResponse.getServiceStatus().getSuccess());
    }

    @Test
    public void testAddEmployee_failedWhenNoRequestBody() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/employee")
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest())
                        .andReturn();
        ServiceStatus serviceStatus = new Gson().fromJson(result.getResponse().getContentAsString(), ServiceStatus.class);
        assertFalse(serviceStatus.getSuccess());
        assertNotNull(serviceStatus.getErrorMessage());
    }

    @Test
    public void testAddEmployee_failedWhenInvalidEmployee() throws Exception {
        EmployeeRequest employeeRequest = EmployeeRequest.builder().firstname("tracy").lastname("lan").build();

        when(employeeService.addEmployee(any())).thenReturn(1);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(employeeRequest))
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest())
                        .andReturn();
        ServiceStatus serviceStatus = new Gson().fromJson(result.getResponse().getContentAsString(), ServiceStatus.class);
        assertFalse(serviceStatus.getSuccess());
        assertNotNull(serviceStatus.getErrorMessage());

    }
}
