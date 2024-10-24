import java.util.Locale;
import java.util.Locale;
import java.util.ResourceBundle;
import org.junit.Assert;
import org.junit.Test;

public class LocalizedGreetingTest {
    public LocalizedGreetingTest() {
    }

    @Test
    public void testEnglishLocale() {
        Locale locale = new Locale("en", "US");
        ResourceBundle messages = ResourceBundle.getBundle("MessagesBundle", locale);
        Assert.assertEquals("Hello! Welcome to our application.", messages.getString("greeting"));
    }

    @Test
    public void testFrenchLocale() {
        Locale locale = new Locale("fr", "FR");
        ResourceBundle messages = ResourceBundle.getBundle("MessagesBundle", locale);
        Assert.assertEquals("Bonjour! Bienvenue dans notre application.", messages.getString("greeting"));
    }

    @Test
    public void testSpanishLocale() {
        Locale locale = new Locale("es", "ES");
        ResourceBundle messages = ResourceBundle.getBundle("MessagesBundle", locale);
        Assert.assertEquals("¡Hola! Bienvenido a nuestra aplicación.", messages.getString("greeting"));
    }

    @Test
    public void testFarsiLocale() {
        Locale locale = new Locale("fa", "IR");
        ResourceBundle messages = ResourceBundle.getBundle("MessagesBundle", locale);
        Assert.assertEquals("سلام! به برنامه ما خوش آمدید", messages.getString("greeting"));
    }
}
