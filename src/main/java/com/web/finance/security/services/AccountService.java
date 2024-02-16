package com.web.finance.security.services;

import com.web.finance.entities.Account;
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
}
