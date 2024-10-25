import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Scanner;

public class LocalizedGreeting {

    public static void main(String[] args) {
        // Prompt user to select a language
        System.out.println("Select a language: ");
        System.out.println("1. English");
        System.out.println("2. French");
        System.out.println("3. Spanish");
        System.out.println("4. Persian");

        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();

        // Set the locale based on user's choice
        Locale locale;
        switch (choice) {
            case 1:
                locale = new Locale("en", "US");
                break;
            case 2:
                locale = new Locale("fr", "FR");
                break;
            case 3:
                locale = new Locale("es", "ES");
                break;
            case 4:
                locale = new Locale("fa", "IR");
                break;
            default:
                System.out.println("Invalid choice. Defaulting to English.");
                locale = new Locale("en", "US");
                break;
        }

        // Load the resource bundle based on selected locale
        ResourceBundle messages = ResourceBundle.getBundle("MessagesBundle", locale);
        System.out.println(messages.getString("greeting"));

        scanner.close();
    }
}
