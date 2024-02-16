package com.web.finance.security.services;

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

    public Set<Operation> findOperationByAccountName (String accountName) {
        return operationRepository.findByAccountName(accountName);
    }

    public Set<Operation> findOperationByAccountId (Long accountId) {
        return operationRepository.findByAccountId(accountId);
    }
}
