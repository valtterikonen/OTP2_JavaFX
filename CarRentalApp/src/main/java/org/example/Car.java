package org.example;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class Car {

    private int id;
    private String name;
    private int pricePerDay;
    private boolean rented;
    private ResourceBundle messages;

    public Car(int id, String name, int pricePerDay, boolean rented, Locale locale) {
        this.id = id;
        this.name = name;
        this.pricePerDay = pricePerDay;
        this.rented = rented;
        this.messages = ResourceBundle.getBundle("messages", locale);
        // Locale could be used for other localization purposes, e.g., localized messages
    }

    public int getId() {
        System.out.println(messages.getString("car.id"));
        return id;
    }

    public int setId(int id) {
        System.out.println(messages.getString("car.id"));
        return this.id = id;
    }

    public String getName() {
        System.out.println(messages.getString("car.name"));
        return name;
    }

    public String setName(String name) {
        System.out.println(messages.getString("car.name"));
        return this.name = name;
    }

    public int getPricePerDay() {
        System.out.println(messages.getString("car.pricePerDay"));
        return pricePerDay;
    }

    public int setPricePerDay(int pricePerDay) {
        System.out.println(messages.getString("car.pricePerDay"));
        return this.pricePerDay = pricePerDay;
    }

    public boolean isRented() {
        System.out.println(messages.getString("car.rented"));
        return rented;
    }

    public boolean setRented(boolean rented) {
        System.out.println(messages.getString("car.rented"));
        return this.rented = rented;
    }

    public String toString() {
        String format = messages.getString("car.toString");
        return MessageFormat.format(format, id, name, pricePerDay, rented);
    }
}
