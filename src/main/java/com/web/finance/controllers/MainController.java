package com.web.finance.controllers;

import com.web.finance.entities.Account;
import com.web.finance.entities.Operation;
import com.web.finance.security.services.AccountService;
import com.web.finance.security.services.OperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Set;

@Controller
public class MainController {
    @Autowired
    AccountService accountService;

    @Autowired
    OperationService operationService;

    @GetMapping("/index")
    public String index(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Set<Account> accounts = accountService.findAccountsByUsername(username);
        Set<Operation> operations = operationService.findOperationByUsername(username);

        model.addAttribute("accounts", accounts);
        model.addAttribute("operations", operations);
        return "index";
    }
}
