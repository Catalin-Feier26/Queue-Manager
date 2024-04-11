package org.example;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimulationManager implements Runnable {
    private final int  noOfClients;
    private final int noOfQueues;
    private final int simulationInterval;
    private final int minArrivalTime;
    private  final int maxArrivalTime;
    private final int minServiceTime;
    private final int maxServiceTime;
    private final ExecutorService executorService;
    private ConcurrentLinkedQueue<Task> tasks;
    private List<Server> servers;

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
        this.servers=new ArrayList<>();
    }
    public void run(){
        int currentTime=0;
        for(int i=0;i<noOfQueues;i++){
            Server s=new Server(i);
            servers.add(s);
            executorService.execute(s);
        }
        generateClients();
        sortClients();
        while(currentTime<simulationInterval){
            currentTime++;
            System.out.println("Time "+currentTime);
        }
        for (Server s : servers) {
            s.stop();
        }
        executorService.shutdown();
    }
    private void generateClients() {
        for (int i = 1; i <= noOfClients; i++) {
            Task c = new Task(i, maxArrivalTime, maxServiceTime, minArrivalTime, minServiceTime);
            tasks.add(c);
        }
    }
    private void sortClients(){
        List<Task> sortedTasks=new ArrayList<>();
        while(!tasks.isEmpty()){
            Task t=tasks.poll();
            sortedTasks.add(t);
        }
        sortedTasks.sort(Comparator.comparing(Task::getArrivalTime));
        tasks.addAll(sortedTasks);
    }
    private void displayTasks(){
        for(Task t:tasks){
            System.out.println("Client " + t.getId() + " arrived at " + t.getArrivalTime() + " and has the following service time " + t.getServiceTime());
        }
    }
}
