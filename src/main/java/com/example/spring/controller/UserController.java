package com.example.spring.controller;

import com.example.spring.model.User;
import com.example.spring.service.UserService;
import com.example.spring.validation.UserForm;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

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
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam(value = "sort", required = false, defaultValue = "ASC") String sort,
            @RequestParam(value = "sortBy", required = false, defaultValue = "id") String sortBy
    ) {
        Sort sortable = sort.equals("ASC") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page - 1, size, sortable);

        Page<User> userPage = userService.getUsers(username, pageable);
        int totalPages = userPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.range(1, totalPages + 1)
                    .boxed()
                    .toList();
            model.addAttribute("pageNumbers", pageNumbers);
        }
        model.addAttribute("userPage", userPage);
        model.addAttribute("searchKey", username);

        return "users-list";
    }

    @GetMapping("/add-user")
    public String showAddUserForm(
            Model model
    ) {
        model.addAttribute("userForm", new UserForm());
        return "add-user";
    }

    @PostMapping("/add-user")
    public String addUser(
            @Valid UserForm userForm,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return "add-user";
        }
        boolean isSuccessful = userService.addUser(userForm.toUser());
        if (!isSuccessful) {
            return "add-user";
        }
        return "redirect:/users";
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
