import java.util.Locale;
import java.util.ResourceBundle;

public class LocalizedGreeting {
    public LocalizedGreeting() {
    }

    public static void main(String[] args) {
        new Locale("en", "US");
        Locale faLocale = new Locale("fa", "IR");
        ResourceBundle messages = ResourceBundle.getBundle("MessagesBundle", faLocale);
        System.out.println(messages.getString("greeting"));
    }
}
