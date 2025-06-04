package com.imp.projectdbms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imp.projectdbms.model.Employee;
import com.imp.projectdbms.repository.EmployeeRepository;

@Service
public class EmployeeService {

	@Autowired
	private EmployeeRepository employeeRepository;
	
	public boolean managerExists(String managerName) {
	    return employeeRepository.managerExists(managerName);
	}
	
	public boolean employeeExists(int empId) {
	    return employeeRepository.employeeExists(empId);
	}


	public boolean saveEmployee(String name, int empId) {
	    return employeeRepository.saveEmployee(name, empId);
	}


	public List<Employee> getAllEmployees() {
		return employeeRepository.getAllEmployees();
	}

	public List<Employee> getVaderEmployees() {
		return employeeRepository.getVaderEmployee();
	}

	public List<Employee> getEmployeesByManager(String managerName) {
		return employeeRepository.getEmployeesByManager(managerName);
	}

	public List<Employee> getJacobTeam() {
		return employeeRepository.getJacobTeam();
	}

	public List<Employee> getManagerTeam(String managerName) {
		return employeeRepository.getManagerTeam(managerName);
	}

}
