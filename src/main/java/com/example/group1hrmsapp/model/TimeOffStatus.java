package com.example.group1hrmsapp.model;

/**
 * This enum represents the status of a time off request in the HRMS (Human Resource Management System) app.
 *
 * <ul>
 *     <li>{@link #PENDING} - The request has been made, but not yet reviewed.</li>
 *     <li>{@link #APPROVED} - The request has been reviewed and approved.</li>
 *     <li>{@link #REJECTED} - The request has been reviewed and rejected.</li>
 *     <li>{@link #CANCELLED} - The request has been cancelled by the employee.</li>
 * </ul>
 */
public enum TimeOffStatus {
    PENDING,
    APPROVED,
    REJECTED,
    CANCELLED
}
