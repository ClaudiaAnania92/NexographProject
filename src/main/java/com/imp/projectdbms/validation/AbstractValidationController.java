package com.imp.projectdbms.validation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public abstract class AbstractValidationController {

	protected ResponseEntity<String> validateEmployeeId(int empId, boolean exists) {
	    if (empId <= 0) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	            .body("Errore: L'ID deve essere un numero positivo.");
	    }
	    if (exists) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	            .body("Errore: Employee con ID " + empId + " già esistente.");
	    }
	    return null;
	}


    protected ResponseEntity<String> validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Errore: Il nome non può essere vuoto.");
        }
        if (!name.matches("^[a-zA-Z\\s]+$")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Errore: Il nome deve contenere solo lettere e spazi.");
        }
        return null;
    }

}
