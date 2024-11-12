package org.example;

import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

class CarRentalSystem {

    ArrayList<Car> cars = new ArrayList<Car>();
    ArrayList<Customer> customers = new ArrayList<Customer>();

    public void addCar(Car car) {
        cars.add(car);
    }

    public void removeCar(Car car) {
        cars.remove(car);
    }

    public void addCustomer(Customer customer) {
        customers.add(customer);
    }

    public void removeCustomer(Customer customer) {
        customers.remove(customer);
    }

    public void rentCar(Customer customer, Car car, Locale locale) {
        ResourceBundle messages = null;
        try {
            messages = ResourceBundle.getBundle("messages", locale);
        } catch (java.util.MissingResourceException e) {
            System.out.println("Error loading resource bundle: " + e.getMessage());
            return;
        }

        if (messages != null) {
            System.out.println("Resource Bundle Loaded: " + messages);
        }

        if (car.isRented()) {
            System.out.println(messages.getString("rent.success"));
        } else {
            car.setRented(true);
            customer.setCarRented(car);
            System.out.println(messages.getString("rent.failure"));
        }
    }

    public void returnCar(Customer customer, Car car, Locale locale) {
        ResourceBundle messages = ResourceBundle.getBundle("messages", locale);
        car.setRented(false);
        customer.setCarRented(null);
        System.out.println(messages.getString("return.success"));
    }

    public int calculatePrice(Car car, int days) {
        return car.getPricePerDay() * days;
    }

    public int processPayment(Customer customer, int amount) {
        customer.setBalance(customer.getBalance() - amount);
        return amount;
    }

    public void displayWelcomeMessage(Locale locale) {
        ResourceBundle messages = ResourceBundle.getBundle("messages", locale);
        System.out.println(messages.getString("welcome"));
    }
}
