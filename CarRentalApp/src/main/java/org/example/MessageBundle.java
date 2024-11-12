package org.example;

import java.util.Locale;
import java.util.ResourceBundle;

public class MessageBundle {

    private ResourceBundle resourceBundle;

    public MessageBundle(Locale locale) {
        this.resourceBundle = ResourceBundle.getBundle("messages", locale);
    }

    public String getMessage(String key) {
        return resourceBundle.getString(key);
    }

    public void displayWelcomeMessage(Locale locale) {
        System.out.println(getMessage("welcomeMessage"));
    }

    public void displayCarName() {
        System.out.println(getMessage("car.name"));
    }

    public void displayCustomerName() {
        System.out.println(getMessage("customer.name"));
    }
}
