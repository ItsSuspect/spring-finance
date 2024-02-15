package com.web.finance.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RequestController {
    @GetMapping("/auth/signIn")
    public String getSignIn() {
        return "authentication";
    }

    @GetMapping("/auth/signUp")
    public String GetSignUp() {
        return "registration";
    }

    @GetMapping("/index")
    public String index() {
        return "index";
    }
}
