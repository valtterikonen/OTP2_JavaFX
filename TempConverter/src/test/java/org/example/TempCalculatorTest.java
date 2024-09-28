package org.example;


import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TempCalculatorTest {

    TempCalculator calculator= new TempCalculator();

    @Test
    public void multiplyTest(){
        assertEquals(10, calculator.multiply(5,2));
    }

    @Test
    public void subtractTest(){
        assertEquals(3, calculator.subtract(5,2));
    }

    @Test
    public void divideTest(){
        assertEquals(3, calculator.divide(6,2));
    }

    @Test
    public void kelvinToFahTest(){
        assertEquals(32, 0.0,  TempCalculator.kelvinToFah(273.15));
    }

}

