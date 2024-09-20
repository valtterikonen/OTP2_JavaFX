import org.example.TimeCalculator;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class TimeCalculatorTest {

    @Test
    public void testCalculateTimeNormalCase() {
        TimeCalculator calculator = new TimeCalculator();
        assertEquals(2.0, calculator.calculateTime(100.0, 50.0), 0.0001);
    }

    @Test
    public void testCalculateTimeZeroSpeed() {
        TimeCalculator calculator = new TimeCalculator();
        assertThrows(IllegalArgumentException.class, () -> {
            calculator.calculateTime(100, 0);
        });
    }

    @Test
    public void testCalculateTimeNegativeSpeed() {
        TimeCalculator calculator = new TimeCalculator();
        assertThrows(IllegalArgumentException.class, () -> {
            calculator.calculateTime(100, -10);
        });
    }
}

