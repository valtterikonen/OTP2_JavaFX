package com.example.easybankproject.db;

import com.example.easybankproject.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    List<Transaction> findAllBySenderAccountIdOrReceiverAccountId(int senderAccountId, int receiverAccountId);
}

