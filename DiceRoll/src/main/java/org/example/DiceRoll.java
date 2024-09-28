package org.example;

import java.util.Random;
import java.util.Scanner;

public class DiceRoll {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("How many dice would you like to roll? ");
        int numberOfDice = scanner.nextInt();
        int sum = rollDice(numberOfDice);
        System.out.println("The total sum of the dice rolls is: " + sum);
        scanner.close();
    }

    public static int rollDice(int numberOfDice) {
        Random random = new Random();
        int sum = 0;
        for (int i = 1; i <= numberOfDice; i++) {
            int roll = random.nextInt(6) + 1;
            sum += roll;
        }
        return sum;
    }
}

