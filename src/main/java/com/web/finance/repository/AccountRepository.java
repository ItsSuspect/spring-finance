package com.web.finance.repository;

import com.web.finance.entities.Account;
import com.web.finance.entities.Operation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface AccountRepository extends JpaRepository<Operation, Long> {
    @Query("SELECT a FROM Account a JOIN a.user u WHERE u.username = :username")
    Set<Account> findByUsername(String username);
}
