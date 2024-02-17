package com.web.finance.controllers;

import com.web.finance.entities.Account;
import com.web.finance.entities.Operation;
import com.web.finance.entities.User;
import com.web.finance.payload.request.AccountRequest;
import com.web.finance.payload.request.OperationRequest;
import com.web.finance.payload.request.SignInRequest;
import com.web.finance.payload.response.MessageResponse;
import com.web.finance.security.services.AccountService;
import com.web.finance.security.services.OperationService;
import com.web.finance.security.services.UserDetailsServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Controller
public class MainController {
    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    AccountService accountService;

    @Autowired
    OperationService operationService;

    @GetMapping("/index")
    public String getIndexPage(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Set<Account> accounts = accountService.findAccountsByUsername(username);
        Set<Operation> operations = operationService.findOperationByUsername(username);

        model.addAttribute("accounts", accounts);
        model.addAttribute("operations", operations);
        return "index";
    }

    @PostMapping("/createAccount")
    public ResponseEntity<?> createAccount(@Valid @RequestBody AccountRequest accountRequest) {
        if (accountRequest.getName() == null || accountRequest.getCurrency() == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Неверно заполненные поля"));
        }

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userDetailsService.findUserByUsername(username);

        accountService.createAccount(accountRequest.getName(), accountRequest.getAmount(), accountRequest.getCurrency(), user);

        return ResponseEntity.ok(new MessageResponse("Новый счет успешно создан"));
    }

    @PostMapping("/deleteAccount")
    public ResponseEntity<?> deleteAccount(@Valid @RequestBody Long id) {
        try {
            accountService.deleteAccount(id);
            return ResponseEntity.ok(new MessageResponse("Удаление успешно"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Ошибка при удаление счета: " + e.getMessage()));
        }
    }

    @PostMapping("/createOperation")
    public ResponseEntity<?> createOperation(@Valid @RequestBody OperationRequest operationRequest) {
        if (operationRequest.getName() == null || operationRequest.getCategory() == null || operationRequest.getType() == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Неверно заполненные поля"));
        }
        Account account = accountService.findAccountById(operationRequest.getAccountId());

        int amount;
        if (operationRequest.getType().equals("income")) {
            amount = account.getAmount() + operationRequest.getAmount();
            account.setAmount(amount);
        } else {
            amount = account.getAmount() - operationRequest.getAmount();
            account.setAmount(amount);
        }
        accountService.saveAccount(account);
        operationService.createOperation(operationRequest.getName(), operationRequest.getCategory(), operationRequest.getAmount(), operationRequest.getType(), account);
        return ResponseEntity.ok(new MessageResponse("Новая операция успешно создана"));
    }

    @PostMapping("/deleteOperation")
    public ResponseEntity<?> deleteOperation(@Valid @RequestBody Long id) {
        try {
            operationService.deleteOperation(id);
            return ResponseEntity.ok(new MessageResponse("Удаление успешно"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Ошибка при удаление операции: " + e.getMessage()));
        }
    }
}
