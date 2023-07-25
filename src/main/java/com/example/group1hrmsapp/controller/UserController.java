package com.example.group1hrmsapp.controller;

import com.example.group1hrmsapp.model.AppUser;
import com.example.group1hrmsapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        AppUser appUser = new AppUser();
        model.addAttribute("user", appUser);
        return "new_user";
    }

    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute("user") AppUser appUser) {
        userService.saveUser(appUser);
        return "redirect:/userList";
    }

    @GetMapping("/showUpdateUserForm/{id}")
    public String showUpdateUserForm(@PathVariable(value = "id") String id, Model model){
        AppUser appUser = userService.getUserById(id);
        model.addAttribute("user", appUser);
        return "update_user";
    }

    @GetMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable(value = "id") String id) {
        this.userService.deleteUserById(id);
        return "redirect:/userList";
    }

    /*@PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestParam("userName") String userName, @RequestParam("password") String password){
        AppUser appUser = userService.getUserById(userName);
        if(appUser == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
        if(!password.equals(appUser.getPassword())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Password");
        }
        String token = userService.generateJwtToken(appUser.getUserName());
        return ResponseEntity.ok(token);
    }*/
}
