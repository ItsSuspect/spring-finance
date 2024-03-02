package com.web.finance.security.services;

import com.web.finance.entities.Account;
import com.web.finance.entities.Operation;
import com.web.finance.repository.OperationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class OperationService {
    @Autowired
    OperationRepository operationRepository;

    public List<Operation> findOperationByUsername (String username) {
        return operationRepository.findByUsername(username);
    }

    public Operation findOperationById(Long id) {
        return operationRepository.findOperationById(id);
    }

    public void createOperation(String name, String category, int amount, String type, Account account, Date date) {
        Operation operation = new Operation(name, category, amount, type, account, date);
        operationRepository.save(operation);
    }

    public void saveOperation(Operation operation) {
        operationRepository.save(operation);
    }

    public void deleteOperation(Long id) {
        operationRepository.deleteById(id);
    }
}
