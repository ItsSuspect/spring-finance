package com.web.finance.security.services;

import com.web.finance.entities.Account;
import com.web.finance.entities.Operation;
import com.web.finance.repository.OperationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class OperationService {
    @Autowired
    OperationRepository operationRepository;

    public Set<Operation> findOperationByUsername (String username) {
        return operationRepository.findByUsername(username);
    }

    public void createOperation(String name, String category, int amount, String type, Account account) {
        Operation operation = new Operation(name, category, amount, type, account);
        operationRepository.save(operation);
    }

    public void deleteOperation(Long id) {
        operationRepository.deleteById(id);
    }
}
