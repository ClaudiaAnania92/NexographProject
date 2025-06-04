package com.imp.projectdbms.controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.imp.projectdbms.model.Employee;
import com.imp.projectdbms.service.EmployeeService;
import com.imp.projectdbms.validation.AbstractValidationController;

import java.util.List;

@RestController
@RequestMapping("/employees")
@Validated
public class EmployeeController extends AbstractValidationController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/createEmployee")
    public ResponseEntity<String> createEmployee(
        @RequestParam String name,
        @RequestParam int empId) {

        ResponseEntity<String> nameValidation = validateName(name);
        if (nameValidation != null) {
            return nameValidation;
        }

        boolean exists = employeeService.employeeExists(empId);

        ResponseEntity<String> idValidation = validateEmployeeId(empId, exists);
        if (idValidation != null) {
            return idValidation;
        }

        boolean saved = employeeService.saveEmployee(name, empId);
        if (saved) {
            return ResponseEntity.ok("Employee creato con successo!");
        } else {
            return ResponseEntity
                .badRequest()
                .body("Errore: Salvataggio fallito per motivi interni.");
        }
    }

    @GetMapping("/getAll")
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
    public ResponseEntity<?> getEmployeesByManager(@PathVariable String managerName) {

        ResponseEntity<String> validationError = validateName(managerName);
        if (validationError != null) {
            return validationError;
        }

        boolean exists = employeeService.managerExists(managerName);
        if (!exists) {
            return ResponseEntity.status(404)
                .body("Errore: il manager \"" + managerName + "\" non esiste nel database.");
        }

        List<Employee> employees = employeeService.getEmployeesByManager(managerName);
        if (employees.isEmpty()) {
            return ResponseEntity.ok("Il manager \"" + managerName + "\" non ha attualmente dipendenti sotto di sé.");
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
    public ResponseEntity<?> getManagerTeam(@PathVariable String managerName) {

        ResponseEntity<String> validationError = validateName(managerName);
        if (validationError != null) {
            return validationError;
        }

        boolean exists = employeeService.managerExists(managerName);
        if (!exists) {
            return ResponseEntity.status(404)
                .body("Errore: il manager \"" + managerName + "\" non esiste nel database.");
        }

        List<Employee> employees = employeeService.getManagerTeam(managerName);
        if (employees.isEmpty()) {
            return ResponseEntity.ok("Il manager \"" + managerName + "\" non ha un team assegnato.");
        }

        return ResponseEntity.ok(employees);
    }
}
