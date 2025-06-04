package com.imp.projectdbms.model;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Node("Employee")
public class Employee {

    @Id
    private int empId;
    private String name;

    public Employee() {
    }

    public Employee(int empId, String name) {
        this.empId = empId;
        this.name = name;
    }

    public int getEmpId() {
        return empId;
    }

    public void setEmpId(int empId) {
        this.empId = empId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
