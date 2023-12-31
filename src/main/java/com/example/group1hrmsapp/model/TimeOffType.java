package com.example.group1hrmsapp.model;

/**
 * This enum represents the type of time off request in the HRMS (Human Resource Management System) app.
 *
 * <ul>
 *     <li>{@link #VACATION} - Time off for vacation.</li>
 *     <li>{@link #PTO} - Paid Time Off, general time off that is paid by the company.</li>
 *     <li>{@link #SICK} - Time off for employee's own illness.</li>
 * </ul>
 */
public enum TimeOffType {
    VACATION,
    PTO,
    SICK
}
