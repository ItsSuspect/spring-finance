package com.web.finance.repository;

import com.web.finance.entities.Operation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface OperationRepository extends JpaRepository<Operation, Long> {
    @Query("SELECT o FROM Operation o JOIN o.account a JOIN a.user u WHERE u.username = :username")
    Set<Operation> findByUsername(String username);
    Set<Operation> findByAccountName(String accountName);
    Operation findOperationById(Long accountId);
    void deleteById(Long id);
}
