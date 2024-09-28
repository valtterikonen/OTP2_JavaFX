package org.example;

public class TempCalculator {

    public int subtract(int a, int b) {
        return a - b;
    }

    public int multiply(int a, int b) {
        return a * b;
    }

    public int divide(int a, int b) {
        if (b == 0) {
            throw new IllegalArgumentException("Cannot divide by zero!");
        }
        return a / b;
    }

    public static  double	kelvinToFah(double kel) {
        return Math.round((kel-273.15)* (9/5) + 32);
    }

    public static void main(String[] args){
        System.out.println("Done docker");
    }
}
