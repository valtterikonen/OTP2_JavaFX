package org.example;

import org.junit.Test;
import static org.junit.Assert.*;

public class DiceRollTest {

    @Test
    public void testRollDice() {
        int sum = DiceRoll.rollDice(3);
        assertTrue(sum >= 3 && sum <= 18);  // Minimum sum is 3 and max is 18 for 3 dice
    }
}

