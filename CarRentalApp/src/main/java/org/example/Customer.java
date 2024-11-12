package org.example;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class Customer {

    private int id;
    private String name;
    private double balance;
    private Car carRented;
    private ResourceBundle messages;

    public Customer(int id, String name, double balance, Locale locale) {
        this.id = id;
        this.name = name;
        this.balance = balance;
        this.carRented = null;

        // Initialize the ResourceBundle with the locale
        this.messages = ResourceBundle.getBundle("messages", locale); // Load the appropriate messages file
    }

    public String toString() {
        // Ensure messages is not null
        if (messages == null) {
            throw new NullPointerException("ResourceBundle is not initialized.");
        }

        // Get the format string from the ResourceBundle
        String format = messages.getString("customer.toString");

        // Use MessageFormat to format the string with actual values
        return MessageFormat.format(format, id, name, balance, carRented != null ? carRented.getName() : "No car rented");
    }



    public int getId() {
        System.out.println(messages.getString("customer.id"));
        return id;
    }


    public int setId(int id) {
        System.out.println(messages.getString("customer.id"));
        return this.id = id;
    }

    public String getName() {
        System.out.println(messages.getString("customer.name"));
        return name;
    }

    public String setName(String name) {
        System.out.println(messages.getString("customer.name"));
        return this.name = name;
    }

    public double getBalance() {
        System.out.println(messages.getString("customer.balance"));
        return balance;
    }

    public double setBalance(double balance) {
        System.out.println(messages.getString("customer.balance"));
        return this.balance = balance;
    }

    public Car getCarRented() {
        System.out.println(messages.getString("customer.carRented"));
        return carRented;
    }

    public Car setCarRented(Car carRented) {
        System.out.println(messages.getString("customer.carRented"));
        return this.carRented = carRented;
    }


}
