package com.example.easybankproject.models;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;

    @Entity
    @Setter
    @Getter
    @Table(name = "bankaccount")
    public class BankAccount {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "bank_account_id")
        private int bankAccountId;

        @ManyToOne
        @JoinColumn(name = "user_id", nullable = false)
        private User user;

        private BigDecimal balance;

    }
