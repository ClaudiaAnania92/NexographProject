package com.imp.projectdbms.controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.imp.projectdbms.model.Employee;
import com.imp.projectdbms.service.EmployeeService;

import java.util.List;

@RestController
@RequestMapping("/employees")
@Validated
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/")
    public ResponseEntity<String> createEmployee(
        @RequestParam
        @NotBlank(message = "Il nome non può essere vuoto")
        @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Il nome deve contenere solo lettere e spazi")
        String name,

        @RequestParam
        @Positive(message = "L'id deve essere un numero positivo")
        int empId) {

        employeeService.saveEmployee(name, empId);
        return ResponseEntity.ok("Employee creato con successo!");
    }

    @GetMapping("/")
    public ResponseEntity<List<Employee>> getAllEmployees() {
        List<Employee> employees = employeeService.getAllEmployees();
        if (employees.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/vader")
    public ResponseEntity<List<Employee>> getVaderEmployees() {
        List<Employee> employees = employeeService.getVaderEmployees();
        if (employees.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/manager/{managerName}")
    public ResponseEntity<List<Employee>> getEmployeesByManager(
        @PathVariable
        @NotBlank(message = "Il nome del manager non può essere vuoto")
        @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Il nome del manager deve contenere solo lettere e spazi")
        String managerName) {

        List<Employee> employees = employeeService.getEmployeesByManager(managerName);
        if (employees.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/jacobTeam")
    public ResponseEntity<List<Employee>> getJacobTeam() {
        List<Employee> employees = employeeService.getJacobTeam();
        if (employees.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/managerTeam/{managerName}")
    public ResponseEntity<List<Employee>> getManagerTeam(
        @PathVariable
        @NotBlank(message = "Il nome del manager non può essere vuoto")
        @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Il nome del manager deve contenere solo lettere e spazi")
        String managerName) {

        List<Employee> employees = employeeService.getManagerTeam(managerName);
        if (employees.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(employees);
    }
}
