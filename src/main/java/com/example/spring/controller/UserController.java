package com.example.spring.controller;

import com.example.spring.model.User;
import com.example.spring.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(path = "/users")
public class UserController {
    UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(path = {"", "/"})
    public String showUsers(
            Model model,
            @RequestParam(value = "username", required = false) String username
    ) {
        List<User> users = userService.getUsers(username);
        model.addAttribute("users", users);
        model.addAttribute("searchKey", username);
        return "users-list";
    }

    @GetMapping("/add-user")
    public String getAddUserForm() {
        return "add-user";
    }

    @PostMapping("/add-user")
    @ResponseBody
    public ResponseEntity<?> addUser(@RequestBody User user) {
        boolean result = userService.addUser(user);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/edit-user/{id}")
    public String getUserDetails(
            Model model,
            @PathVariable("id") long id
    ) {

        User userDetails = userService.getUserDetails(id);
        if (userDetails == null) {
            return "user-not-found";
        }
        model.addAttribute("userDetails", userDetails);
        model.addAttribute("result", null);
        return "edit-user";
    }

    @PostMapping("/edit-user/{id}")
    public String editUser(
            Model model,
            @ModelAttribute("userDetails") User updatedUser
    ) {
        model.addAttribute("result", updateUser(updatedUser));
        return "edit-user";
    }

    @PostMapping("/update-user")
    @ResponseBody
    public ResponseEntity<?> editUserForAjaxCall(
            @RequestBody User updatedUser
    ) {
        return ResponseEntity.ok().body(updateUser(updatedUser));
    }

    @PostMapping(path = "/delete-user")
    @ResponseBody
    public ResponseEntity<?> deleteUser(
            @RequestBody int id
    ) {
        boolean result = userService.deleteUser(id);
        return ResponseEntity.ok().body(result);
    }

    private Map<String, Object> updateUser(User updatedUser) {
        boolean isSuccessful = userService.editUser(updatedUser);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("isSuccessful", isSuccessful);
        resultMap.put("message", isSuccessful ? "Update Successfully!" : "Update Failed!");
        return resultMap;
    }
}
