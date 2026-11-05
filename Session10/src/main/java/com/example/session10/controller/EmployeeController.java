package com.example.session10.controller;

import com.example.session10.model.Employee;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {

    @GetMapping
    public List<Employee> getAllEmployees() {

        return List.of(
                new Employee(1L, "Nguyễn Công Hưởng", 6000000.0),
                new Employee(2L, "Trần Văn Nam", 7500000.0),
                new Employee(3L, "Lê Thị Hoa", 8500000.0)
        );
    }
}