package com.example.spring.controller;

import com.example.spring.dto.AccountDTO;
import com.example.spring.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

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
        if (!bindingResult.hasErrors() && !accountForm.getPassword().equalsIgnoreCase(accountForm.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "", "Password not match");
        }
        // TODO: Finish registration process later
        // If have free time, continue sand castle!
        return "register";
    }
}
