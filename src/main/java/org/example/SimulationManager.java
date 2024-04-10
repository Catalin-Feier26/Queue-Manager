package org.example;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;

public class SimulationManager implements Runnable {
    private int noOfClients;
    private int noOfQueues;
    private int simulationInterval;
    private int minArrivalTime;
    private int maxArrivalTime;
    private int minServiceTime;
    private int maxServiceTime;
    private ExecutorService executorService;
    private ConcurrentLinkedQueue<Task> tasks;

    public SimulationManager(int noOfClients, int noOfQueues, int simulationInterval, int minArrivalTime, int maxArrivalTime, int minServiceTime, int maxServiceTime) {
        this.noOfClients = noOfClients;
        this.noOfQueues = noOfQueues;
        this.simulationInterval = simulationInterval;
        this.minArrivalTime = minArrivalTime;
        this.maxArrivalTime = maxArrivalTime;
        this.minServiceTime = minServiceTime;
        this.maxServiceTime = maxServiceTime;
        this.executorService= Executors.newFixedThreadPool(noOfQueues);
        this.tasks=new ConcurrentLinkedQueue<>();
    }
    public void run(){
        int currentTime=0;
        System.out.println("Simulation started "+currentTime);
        generateClients();
        while(currentTime<simulationInterval){
            currentTime++;
            System.out.println("Time "+currentTime);
        }
         executorService.shutdown();
    }
    private void generateClients() {
        for (int i = 1; i <= noOfClients; i++) {
            Task c = new Task(i, maxArrivalTime, maxServiceTime, minArrivalTime, minServiceTime);
            tasks.add(c);
            System.out.println("Client " + c.getId() + " arrived at " + c.getArrivalTime() + " and has the following service time " + c.getServiceTime());
        }
    }

}
