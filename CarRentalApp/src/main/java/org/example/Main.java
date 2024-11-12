package org.example;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        CarRentalSystem carRentalSystem = new CarRentalSystem();

        // Choose language based on user input
        Locale locale = chooseLanguage();
        ResourceBundle messages = ResourceBundle.getBundle("messages", locale);

        carRentalSystem.displayWelcomeMessage(locale);

        Car car1 = new Car(1, messages.getString("car.toyotaName"), 100, false, locale);
        Car car2 = new Car(2, messages.getString("car.hondaName"), 120, false, locale);
        Car car3 = new Car(3, messages.getString("car.fordName"), 150, false, locale);

        Customer customer1 = new Customer(1, "Alice", 1000, locale);
        Customer customer2 = new Customer(2, "Bob", 2000, locale);


        // Add Cars and Customers to the system
        carRentalSystem.addCar(car1);
        carRentalSystem.addCar(car2);
        carRentalSystem.addCar(car3);

        carRentalSystem.addCustomer(customer1);
        carRentalSystem.addCustomer(customer2);

        System.out.println(customer1.toString());
        System.out.println(customer2.toString());

        // Show Cars and Customers
        System.out.println(messages.getString("car.name") + ": " + carRentalSystem.cars);
        System.out.println(messages.getString("customer.name") + ": " + carRentalSystem.customers);

        // Rent and Return Cars
        carRentalSystem.rentCar(customer1, car1, locale);
        System.out.println(messages.getString("car.name") + ": " + carRentalSystem.cars);
        System.out.println(messages.getString("customer.name") + ": " + carRentalSystem.customers);

        carRentalSystem.returnCar(customer1, car1, locale);
        System.out.println(messages.getString("car.name") + ": " + carRentalSystem.cars);
        System.out.println(messages.getString("customer.name") + ": " + carRentalSystem.customers);

        // Calculate Price
        System.out.println(messages.getString("car.rentalPrice") + carRentalSystem.calculatePrice(car1, 5));

        // Process Payment and Show Balance
        System.out.println(messages.getString("balance") + ": " + customer1.getBalance());
        System.out.println(messages.getString("paymentProcessed") + ": " + carRentalSystem.processPayment(customer1, 500));
        System.out.println(messages.getString("balance") + ": " + customer1.getBalance());

    }

    private static Locale chooseLanguage() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose language: 1 for English, 2 for Japanese");
        int choice = scanner.nextInt();
        scanner.close();
        if (choice == 2) {
            return new Locale("ja");
        } else {
            return new Locale("en");
        }

    }
}
