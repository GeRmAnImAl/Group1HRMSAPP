package com.example.group1hrmsapp.controller;

import com.example.group1hrmsapp.model.Employee;
import com.example.group1hrmsapp.service.PaymentService;
import com.example.group1hrmsapp.service.WorkedHoursService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class PaymentController {
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private WorkedHoursService workedHoursService;

    @GetMapping("/paymentsList")
    public String viewPaymentPage(Model model){
        Employee loggedInEmployee = paymentService.getLoggedInUser();

        model.addAttribute("listPayments", paymentService.getAllPayments());
        model.addAttribute("loggedInEmployee", loggedInEmployee);
        return "pending_payments";
    }

    @GetMapping("/processPayroll/{id}")
    public String processPayroll(@PathVariable Long id, Model model){
        boolean result = paymentService.processPayroll(id);
        model.addAttribute("message", result ? "Payment has been successfully processed" : "Failed to process the payment.");
        return "process_payment_result";
    }
}
