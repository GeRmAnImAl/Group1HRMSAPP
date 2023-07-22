package com.example.group1hrmsapp.service;

import com.example.group1hrmsapp.model.EmployeeRecord;
import org.springframework.stereotype.Service;

@Service
public class HRMSFacade {
    private EmployeeRecordManager erm = new EmployeeRecordManager();

    public void createEmployee(int id, String name) {
        erm.createEmployeeRecord(id, name);
    }

    public EmployeeRecord readEmployee(int id) {
        return erm.readEmployeeRecord(id);
    }

    public void updateEmployee(int id, String name) {
        erm.updateEmployeeRecord(id, name);
    }

    public void deleteEmployee(int id) {
        erm.deleteEmployeeRecord(id);
    }

    // More methods for TimeOffManager, WorkedHoursManager and BenefitsManager
}
