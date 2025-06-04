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
    public ResponseEntity<?> getEmployeesByManager(
        @PathVariable
        @NotBlank(message = "Il nome del manager non può essere vuoto")
        @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Il nome del manager deve contenere solo lettere e spazi")
        String managerName) {

        // Controllo sul nome
        boolean exists = employeeService.managerExists(managerName);
        
        if (!exists) {
            // Caso 1: il manager esiste
            return ResponseEntity.status(404)
                .body("Errore: il manager \"" + managerName + "\" non esiste nel database.");
        }

        List<Employee> employees = employeeService.getEmployeesByManager(managerName);

        if (employees.isEmpty()) {
            // Caso 2: il manager esiste ma non ha dipendenti sotto
            return ResponseEntity.ok("Il manager \"" + managerName + "\" non ha attualmente dipendenti sotto di sé.");
        }

        // Caso 3: il manager esiste e ha almeno un dipendente sotto
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
    public ResponseEntity<?> getManagerTeam(
        @PathVariable
        @NotBlank(message = "Il nome del manager non può essere vuoto")
        @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Il nome del manager deve contenere solo lettere e spazi")
        String managerName) {

        // Controllo sul nome
        boolean exists = employeeService.managerExists(managerName);

        if (!exists) {
            // Caso 1: il manager non esiste
            return ResponseEntity.status(404)
                .body("Errore: il manager \"" + managerName + "\" non esiste nel database.");
        }

        List<Employee> employees = employeeService.getManagerTeam(managerName);

        if (employees.isEmpty()) {
            // Caso 2: il manager esiste ma non ha un team
            return ResponseEntity.ok("Il manager \"" + managerName + "\" non ha un team assegnato.");
        }

        // Caso 3: il manager esiste e ha un team di almeno una persona
        return ResponseEntity.ok(employees);
    }

}
