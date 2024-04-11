package org.example;

public class Main {
    public static void main(String[] args) {

        SimulationManager sm = new SimulationManager(10, 5, 150, 2, 29, 2, 10);
        Thread t = new Thread(sm);
        t.start();
    }
}