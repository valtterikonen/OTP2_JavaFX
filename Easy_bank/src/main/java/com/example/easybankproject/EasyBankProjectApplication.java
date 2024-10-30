package com.example.easybankproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.util.Locale;

@SpringBootApplication
public class EasyBankProjectApplication {

    public static void main(String[] args) {
        Locale.setDefault(new Locale("en"));
        SpringApplication.run(EasyBankProjectApplication.class, args);
    }

}

