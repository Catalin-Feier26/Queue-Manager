package org.example;

public class Main {
    public static void main(String[] args) {
        Policy p = Policy.SHORTEST_TIME;
        SimulationManager sm = new SimulationManager(50, 5, 60, 2, 40, 1, 7, p);
        Thread t = new Thread(sm);
        t.start();
    }
}