package com.example.group1hrmsapp.service;

import com.example.group1hrmsapp.model.EmployeeRecord;

import java.util.HashMap;
import java.util.Map;

public class EmployeeRecordManager {
    private final Map<Integer, EmployeeRecord> employeeRecords = new HashMap<>();

    void createEmployeeRecord(int id, String name) {
        EmployeeRecord employeeRecord = new EmployeeRecord(id, name);
        employeeRecords.put(id, employeeRecord);
    }

    EmployeeRecord readEmployeeRecord(int id) {
        return employeeRecords.get(id);
    }

    void updateEmployeeRecord(int id, String name) {
        EmployeeRecord employeeRecord = employeeRecords.get(id);
        if(employeeRecord != null) {
            employeeRecord.setName(name);
        }
    }

    void deleteEmployeeRecord(int id) {
        employeeRecords.remove(id);
    }
}