package com.web.finance.controllers;

import com.web.finance.entities.Account;
import com.web.finance.entities.Operation;
import com.web.finance.entities.User;
import com.web.finance.payload.request.AccountRequest;
import com.web.finance.payload.request.DeleteOperationRequest;
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

import java.util.*;

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
        List<Operation> operations = operationService.findOperationByUsername(username);

        operations.sort(Comparator.comparing(Operation::getDate).reversed());

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

        int amount = account.getAmount();
        if (operationRequest.getType().equals("income")) {
            amount += operationRequest.getAmount();
        } else {
            amount -= operationRequest.getAmount();
        }
        account.setAmount(amount);

        accountService.saveAccount(account);
        operationService.createOperation(operationRequest.getName(), operationRequest.getCategory(), operationRequest.getAmount(), operationRequest.getType(), account, operationRequest.getDate());
        return ResponseEntity.ok(new MessageResponse("Новая операция успешно создана"));
    }

    @PostMapping("/editOperation")
    public ResponseEntity<?> editOperation(@Valid @RequestBody OperationRequest operationRequest) {
        Account account = accountService.findAccountById(operationRequest.getAccountId());
        Operation operation = operationService.findOperationById(operationRequest.getOperationId());

        if (operation.getAmount() != operationRequest.getAmount() && !operation.getType().equals(operationRequest.getType())) {
            int oldAmount = operation.getAmount();
            int newAmount = operationRequest.getAmount();
            int amountDifference = newAmount - oldAmount;

            if (!operation.getType().equals(operationRequest.getType())) {
                if (operationRequest.getType().equals("income")) {
                    amountDifference += oldAmount * 2;
                } else {
                    amountDifference -= oldAmount * 2;
                }
                operation.setType(operationRequest.getType());
            }

            int accountAmount = account.getAmount();
            accountAmount += amountDifference;
            account.setAmount(accountAmount);
            operation.setAmount(operationRequest.getAmount());
        }
        else if (operation.getAmount() != operationRequest.getAmount()) {
            int amountDifference = operationRequest.getAmount() - operation.getAmount();
            if (operation.getType().equals("income")) {
                account.setAmount(account.getAmount() + amountDifference);
            } else {
                account.setAmount(account.getAmount() - amountDifference);
            }
            operation.setAmount(operationRequest.getAmount());
        }
        else if (!operation.getType().equals(operationRequest.getType())) {
            int amountDifference = operation.getAmount();

            if (operation.getType().equals("income")) {
                account.setAmount(account.getAmount() - amountDifference * 2);
            } else {
                account.setAmount(account.getAmount() + amountDifference * 2);
            }

            operation.setType(operationRequest.getType());
        }

        operation.setName(operationRequest.getName());
        operation.setCategory(operationRequest.getCategory());
        operation.setAccount(account);
        operation.setDate(operationRequest.getDate());

        accountService.saveAccount(account);
        operationService.saveOperation(operation);
        return ResponseEntity.ok(new MessageResponse("Операция успешно отредактирована"));
    }

    @PostMapping("/editAccount")
    public ResponseEntity<?> editAccount(@Valid @RequestBody AccountRequest accountRequest) {
        Account account = accountService.findAccountById(accountRequest.getAccountId());

        account.setName(accountRequest.getName());
        account.setAmount(accountRequest.getAmount());
        account.setCurrency(accountRequest.getCurrency());

        accountService.saveAccount(account);
        return ResponseEntity.ok(new MessageResponse("Счет успешно отредактирован"));
    }


    @PostMapping("/deleteOperation")
    public ResponseEntity<?> deleteOperation(@Valid @RequestBody DeleteOperationRequest deleteOperationRequest) {
        try {
            Operation operation = operationService.findOperationById(deleteOperationRequest.getId());
            Account account = operation.getAccount();

            int amount = account.getAmount();
            if (deleteOperationRequest.getType().equals("income")) {
                amount -= deleteOperationRequest.getAmount();
            } else {
                amount += deleteOperationRequest.getAmount();
            }
            account.setAmount(amount);

            accountService.saveAccount(account);
            operationService.deleteOperation(deleteOperationRequest.getId());
            return ResponseEntity.ok(new MessageResponse("Удаление успешно"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Ошибка при удаление операции: " + e.getMessage()));
        }
    }
}
