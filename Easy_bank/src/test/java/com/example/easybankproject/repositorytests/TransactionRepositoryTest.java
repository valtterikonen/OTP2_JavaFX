package com.example.easybankproject.repositorytests;

import com.example.easybankproject.db.TransactionRepository;
import com.example.easybankproject.models.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TransactionRepositoryTest {

    @Autowired
    private TransactionRepository transactionRepository;

    private Transaction transaction1;

    private Transaction transaction2;



    @BeforeEach
    public void setup() {
        transaction1 = new Transaction();
        transaction1.setSenderAccountId(1);
        transaction1.setReceiverAccountId(2);
        transaction1.setAmount(1);
        transaction1.setMessage("Test transaction 1");
        transactionRepository.save(transaction1);

        transaction2 = new Transaction();
        transaction2.setSenderAccountId(2);
        transaction2.setReceiverAccountId(1);
        transaction2.setAmount(1);
        transaction2.setMessage("Test transaction 2");
        transactionRepository.save(transaction2);
    }

    @Test
    public void testFindAllBySenderAccountIdOrReceiverAccountId() {
        List<Transaction> transactions = transactionRepository.findAllBySenderAccountIdOrReceiverAccountId(1, 2);
        System.out.println("Transactions: " + transactions);
        assertThat(transactions).isNotEmpty();
        assertThat(transactions.size()).isEqualTo(28);
        assertThat(transactions.get(0).getMessage()).isEqualTo("Payment");
        assertThat(transactions.get(1).getMessage()).isEqualTo("testi");
    }
}


