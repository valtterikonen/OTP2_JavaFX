package com.example.easybankproject.db;


import com.example.easybankproject.models.BankAccount;
import com.example.easybankproject.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Optional;

@EnableJpaRepositories(basePackages = "com.example.easybankproject.db")
public interface BankAccountRepository extends JpaRepository<BankAccount, Integer> {
    Optional<BankAccount> findByUser(User user);

    Optional<BankAccount> findByBankAccountId(int bankAccountId);

}
