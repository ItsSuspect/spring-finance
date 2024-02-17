package com.web.finance.security.services;

import com.web.finance.entities.Account;
import com.web.finance.entities.User;
import com.web.finance.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class AccountService {
    @Autowired
    AccountRepository accountRepository;

    public Set<Account> findAccountsByUsername(String username) {
        return accountRepository.findByUsername(username);
    }

    public Account findAccountById(Long id) {
        return accountRepository.findAccountById(id);
    }

    public void createAccount(String name, int amount, String currency, User user) {
        Account account = new Account(name, amount, currency, user);
        accountRepository.save(account);
    }

    public void deleteAccount(Long id) {
        accountRepository.deleteById(id);
    }

    public void saveAccount(Account account) {
        accountRepository.save(account);
    }
}
