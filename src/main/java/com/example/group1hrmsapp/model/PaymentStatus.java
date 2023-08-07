package com.example.group1hrmsapp.model;

/**
 * Enumeration representing the different states of a payment in the HRMS application.
 */
public enum PaymentStatus {

    /** Payment has been initiated but not yet processed. */
    PENDING,

    /** Payment is currently in the process of being completed. */
    PROCESSING,

    /** Payment has been successfully completed. */
    PAID
}
