package com.example.group1hrmsapp.controller;

import com.example.group1hrmsapp.model.User;
import com.example.group1hrmsapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/userList")
    public String viewUserPage(Model model) {
        model.addAttribute("listUsers", userService.getAllUsers());
        return "user_list";
    }

    @GetMapping("/showNewUserForm")
    public String showNewUserForm(Model model){
        User user = new User();
        model.addAttribute("user", user);
        return "new_user";
    }

    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute("user") User user) {
        userService.saveUser(user);
        return "redirect:/userList";
    }

    @GetMapping("/showUpdateUserForm/{id}")
    public String showUpdateUserForm(@PathVariable(value = "id") String id, Model model){
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        return "update_user";
    }

    @GetMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable(value = "id") String id) {
        this.userService.deleteUserById(id);
        return "redirect:/userList";
    }
}
