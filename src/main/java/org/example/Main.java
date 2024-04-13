package org.example;

public class Main {
    public static void main(String[] args) {
        Policy p = Policy.SHORTEST_TIME;
        SimulationManager sm = new SimulationManager(15, 5, 50, 2, 20, 2, 15, p);
        Thread t = new Thread(sm);
        t.start();
    }
}