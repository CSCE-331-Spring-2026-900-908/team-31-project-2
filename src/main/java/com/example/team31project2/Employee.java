package com.example.team31project2;

/**
 * Represents an employee with an ID, name, role, and hashed PIN.
 *
 * @author Team-31
 */
public class Employee {
    private int id;
    private String name;
    private String role;
    private String pinHash;
       /*  
        @param id: gets the id of the employee  
        @param name: get name of employee 
        @param role: get the role
        @param pinHash: get the pin 
        @returns nothing
    */  
    public Employee(int id, String name, String role, String pinHash) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.pinHash = pinHash;
    }

    /*  
        @returns id
    */  
    public int getId() {
        return id;
    }

    /*  
        @returns name
    */  
    public String getName() {
        return name;
    }

    /*  
        @returns role
    */  
    public String getRole() {
        return role;
    }

    /*  
        @returns pin
    */  
    public String getPinHash() {
        return pinHash;
    }

    /*  
        @returns a boolean if it is a manager 
    */  
    public boolean isManager() {
        return "Manager".equalsIgnoreCase(role) || "manager".equals(role);
    }
}
