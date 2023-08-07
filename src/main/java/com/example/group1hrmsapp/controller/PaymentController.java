package com.example.group1hrmsapp.controller;

import com.example.group1hrmsapp.model.Employee;
import com.example.group1hrmsapp.service.PaymentService;
import com.example.group1hrmsapp.service.WorkedHoursService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Controller responsible for handling payment related actions.
 */
@Controller
public class PaymentController {
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private WorkedHoursService workedHoursService;

    /**
     * Handler for the GET request to view all pending payments.
     * @param model the model to hold attributes for the view.
     * @return the name of the view displaying the list of payments.
     */
    @GetMapping("/paymentsList")
    public String viewPaymentPage(Model model){
        Employee loggedInEmployee = paymentService.getLoggedInUser();

        model.addAttribute("listPayments", paymentService.getAllPayments());
        model.addAttribute("loggedInEmployee", loggedInEmployee);
        return "pending_payments";
    }

    /**
     * Process the payroll for an employee identified by the given ID.
     * @param id the ID of the employee whose payroll is to be processed.
     * @param model the model to hold attributes for the view.
     * @return the name of the view displaying the result of the payroll process.
     */
    @GetMapping("/processPayroll/{id}")
    public String processPayroll(@PathVariable Long id, Model model){
        boolean result = paymentService.processPayroll(id);
        model.addAttribute("message", result ? "Payment has been successfully processed" : "Failed to process the payment.");
        return "process_payment_result";
    }
}
