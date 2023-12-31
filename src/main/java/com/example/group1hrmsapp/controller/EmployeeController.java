package com.example.group1hrmsapp.controller;

import com.example.group1hrmsapp.model.Employee;
import com.example.group1hrmsapp.model.SpecialType;
import com.example.group1hrmsapp.repository.EmployeeRepository;
import com.example.group1hrmsapp.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.beans.PropertyEditorSupport;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller class handling employee related HTTP requests.
 */
@Controller
public class EmployeeController {
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private EmployeeService employeeService;

    /**
     * Handler for the GET request to get all employees.
     * @param model the model to hold attributes for the view.
     * @return the name of the view.
     */
    @GetMapping("/employeeList")
    public String viewEmployeePage(Model model){
        model.addAttribute("listEmployees", employeeService.getAllEmployees());
        return "employee_list";
    }

    /**
     * Handler for the GET request to show form for a new employee.
     * @param model the model to hold attributes for the view.
     * @return the name of the view.
     */
    @GetMapping("/showNewEmployeeForm")
    public String showNewEmployeeForm(Model model){
        Employee employee = new Employee();
        List<Employee> allEmployees = employeeService.getAllEmployees();
        List<Employee> managerEmployees = allEmployees.stream()
                .filter(manager -> manager.getSpecialType() == SpecialType.MANAGER)
                .collect(Collectors.toList());

        model.addAttribute("managers", managerEmployees);
        model.addAttribute("employee", employee);
        return "new_employee";
    }

    /**
     * Handler for the POST request to save a new employee.
     * @param employee the employee to be saved.
     * @return the redirect URL.
     */
    @PostMapping("/saveEmployee")
    public String saveEmployee(@ModelAttribute("employee") Employee employee){

        employeeService.saveEmployee(employee);
        return "redirect:/employeeList";
    }

    /**
     * Handles the POST request for updating an employee's information.
     * @param employee the {@link Employee} object populated with the updated data from the form
     * @return a string indicating where the user should be redirected post-operation
     */
    @PostMapping("/updateEmployee")
    public String updateEmployee(@ModelAttribute("employee") Employee employee){

        employeeService.updateEmployee(employee);
        return "redirect:/employeeList";
    }

    /**
     * Handler for the GET request to show form for updating an employee.
     * @param id the ID of the employee to be updated.
     * @param model the model to hold attributes for the view.
     * @return the name of the view.
     */
    @GetMapping("/showUpdateEmployeeForm/{id}")
    public String showUpdateEmployeeForm(@PathVariable(value = "id") Long id, Model model){
        Employee employee = employeeService.getEmployeeById(id);
        List<Employee> allEmployees = employeeService.getAllEmployees();
        List<Employee> managerEmployees = allEmployees.stream()
                .filter(manager -> manager.getSpecialType() == SpecialType.MANAGER)
                .collect(Collectors.toList());

        model.addAttribute("managers", managerEmployees);
        model.addAttribute("employee", employee);
        return "update_employee";
    }

    /**
     * Handler for the GET request to delete an employee.
     * @param id the ID of the employee to be deleted.
     * @return the redirect URL.
     */
    @GetMapping("/deleteEmployee/{id}")
    public String deleteEmployee(@PathVariable(value = "id") long id){
        this.employeeService.deleteEmployeeById(id);
        return "redirect:/employeeList";
    }

    /**
     * Handler for the GET request to get a paginated list of employees.
     * @param pageNo the page number.
     * @param sortField the field to sort by.
     * @param sortDir the sort direction.
     * @param model the model to hold attributes for the view.
     * @return the name of the view.
     */
    @GetMapping("/page/{pageNo}")
    public String findPaginated(@PathVariable(value = "pageNo") int pageNo, @RequestParam("sortField") String sortField,
                                @RequestParam("sortDir") String sortDir, Model model){
        int pageSize = 5;
        Page<Employee> page = employeeService.findPaginated(pageNo, pageSize, sortField, sortDir);
        List<Employee> listEmployees = page.getContent();

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("listEmployees", listEmployees);
        return "employee_list";
    }

    /**
     * Initializes data binder for web requests, allowing for custom property editors.
     * <p>
     * This particular binder helps convert incoming string-based manager IDs into
     * actual {@link Employee} manager objects.
     * </p>
     *
     * @param binder the data binder used to bind web request parameters
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Employee.class, "manager", new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                Employee manager = null;
                if (text != null && !text.isEmpty()) {
                    Long id = Long.parseLong(text);
                    manager = employeeService.getEmployeeById(id);
                }
                setValue(manager);
            }
        });
    }

}
