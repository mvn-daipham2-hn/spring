package com.example.spring.controller;

import com.example.spring.dto.AccountDTO;
import com.example.spring.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {
    AccountService accountService;

    @Autowired
    public LoginController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/login")
    public String login(
    ) {
        return "login";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("accountForm", new AccountDTO());
        return "register";
    }

    @PostMapping("/register-process")
    public String register(
            @ModelAttribute("accountForm") @Valid AccountDTO accountForm,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return "register";
        } else if (!accountForm.getPassword().equalsIgnoreCase(accountForm.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "", "Password not match");
            return "register";
        }
        boolean isSuccessful = accountService.addAccount(accountForm);
        if (!isSuccessful) {
            bindingResult.rejectValue("username", "", "Your username already exists");
            return "register";
        }
        return "redirect:/login";
    }
}
