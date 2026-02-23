package com.example.team31project2;

public class Employee {
    private int id;
    private String name;
    private String role;
    private String pinHash;

    public Employee(int id, String name, String role, String pinHash) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.pinHash = pinHash;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    public String getPinHash() {
        return pinHash;
    }

    public boolean isManager() {
        return "Manager".equalsIgnoreCase(role) || "manager".equals(role);
    }
}
