package com.example.group1hrmsapp.controller;

import com.example.group1hrmsapp.model.AppUser;
import com.example.group1hrmsapp.repository.UserRepository;
import com.example.group1hrmsapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Controller responsible for handling user-related actions.
 */
@Controller
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    /**
     * Handles the request to view the list of users.
     *
     * @param model the model to hold attributes for the view.
     * @return the name of the view displaying the list of users.
     */
    @GetMapping("/userList")
    public String viewUserPage(Model model) {
        model.addAttribute("listUsers", userService.getAllUsers());
        return "user_list";
    }

    /**
     * Displays the form to create a new user.
     *
     * @param model the model to hold attributes for the view.
     * @return the name of the view displaying the user creation form.
     */
    @GetMapping("/showNewUserForm")
    public String showNewUserForm(Model model){
        AppUser appUser = new AppUser();
        model.addAttribute("user", appUser);
        return "new_user";
    }

    /**
     * Handles the submission of the form to create a new user.
     *
     * @param appUser the user details to save.
     * @return redirect to the user list after successful save.
     */
    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute("user") AppUser appUser) {
        userService.saveUser(appUser);
        return "redirect:/userList";
    }

    /**
     * Displays the form to change the logged in user's password.
     *
     * @param model the model to hold attributes for the view.
     * @return the name of the view displaying the change password form.
     */
    @GetMapping("/showChangePasswordForm")
    public String showChangePasswordForm(Model model){
        model.addAttribute("currentPassword", "");
        model.addAttribute("newPassword", "");
        model.addAttribute("confirmPassword", "");
        return "change_password";
    }

    /**
     * Handles the password change request.
     *
     * @param currentPassword the current password of the user.
     * @param newPassword the new password.
     * @param confirmPassword the confirmation of the new password.
     * @param model the model to hold attributes for the view.
     * @return the name of the view displaying the result of the password change action.
     */
    @PostMapping("/changePassword")
    public String changePassword(@RequestParam("currentPassword") String currentPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 @RequestParam("confirmPassword") String confirmPassword, Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername = auth.getName();

        // Fetch the AppUser entity associated with the username
        AppUser loggedInUser = userRepository.findByUserName(loggedInUsername)
                .orElseThrow(() -> new RuntimeException("No user logged in"));
        String username = loggedInUser.getUserName();

        // Check if the new password and confirm password match
        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "New Password and Confirm Password do not match.");
            return "change_password";
        }

        // Validate password complexity requirements
        String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        if (!newPassword.matches(passwordPattern)) {
            model.addAttribute("error", "Password must be at least 8 characters in length, "
                    + "contain at least 1 uppercase letter, "
                    + "contain at least 1 lowercase letter, "
                    + "and contain at least 1 special symbol (@$!%*?&).");
            return "change_password";
        }

        boolean passwordChanged = userService.changePassword(username, currentPassword, newPassword);

        if (passwordChanged) {
            model.addAttribute("success", "Password successfully changed.");
        } else {
            model.addAttribute("error", "Unable to change password. Please check your current password.");
        }

        return "password_change_result";
    }

    /**
     * Handles the user deletion request.
     *
     * @param id the ID of the user to delete.
     * @return redirect to the user list after successful deletion.
     */
    @GetMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable(value = "id") String id) {
        this.userService.deleteUserById(id);
        return "redirect:/userList";
    }

}
