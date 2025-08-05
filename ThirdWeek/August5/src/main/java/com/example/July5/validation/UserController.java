package com.example.July5.validation;

import jakarta.validation.Valid;
import lombok.Builder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UserController {



    @GetMapping
    public String userForm(Model model) {
        UserModel attr = UserModel.builder().build();
        model.addAttribute("user", attr);
        return "form";
    }

    @GetMapping("/show")
    public String showUserForm(@RequestParam String name,
                               @RequestParam String surname,
                               @RequestParam int age,
                               @RequestParam String email,
                               Model model) {
        UserModel attr = UserModel.builder()
                .name(name)
                .surname(surname)
                .age(age)
                .email(email)
                .build();
        model.addAttribute("user", attr);
        return "form";
    }

    @PostMapping
    public String submit(@Valid @ModelAttribute("user") UserModel user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "form";
        }
        model.addAttribute("user", user);
        return "result";
    }



}
