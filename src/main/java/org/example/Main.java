package org.example;

public class Main {
    public static void main(String[] args) {

        SimulationManager sm = new SimulationManager(10, 3, 150, 3, 29, 3, 4);
        Thread t = new Thread(sm);
        t.start();
    }
}