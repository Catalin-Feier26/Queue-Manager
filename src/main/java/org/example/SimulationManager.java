package org.example;
import javax.swing.*;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Thread.currentThread;

public class SimulationManager implements Runnable
{
    private int  noOfClients;
    private int noOfQueues;
    private int simulationInterval;
    private int minArrivalTime;
    private int maxArrivalTime;
    private int minServiceTime;
    private int maxServiceTime;
    private ExecutorService executorService;
    private ConcurrentLinkedQueue<Task> tasks;
    private List<Server> servers;
    private Strategy strategy;
    private Policy policy;
    private AtomicInteger totalWaitingTime= new AtomicInteger(0);
    private int peakTime=0;
    private int maxClients=0;
    private int totalServiceTime=0;

    private volatile boolean running = true;

    private PrintWriter writer;
    private JTextArea textArea;

    public SimulationManager(int noOfClients, int noOfQueues, int simulationInterval, int minArrivalTime, int maxArrivalTime, int minServiceTime, int maxServiceTime, Policy policy, PrintWriter writer, JTextArea textArea)
    {
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
        this.policy=policy;
        this.writer = writer;
        this.textArea = textArea;

        if(policy == Policy.SHORTEST_TIME)
        {
            strategy = new TimeStrategy();
        }
        else if(policy == Policy.SHORTEST_QUEUE)
        {
            strategy = new QueueStrategy();
        }
    }
    public void run()
    {
        int currentTime=0;
        generateClients();
        sortClients();
        System.out.println("Simulation started");
        writer.println("Simulation started");
        SwingUtilities.invokeLater(() -> textArea.setText("Simulation started" + "\n"));

        for(int i=0;i<noOfQueues;i++)
        {
            Server s=new Server(i,writer,textArea);
            servers.add(s);
            executorService.execute(s);
        }
        while(currentTime<=simulationInterval && running)
        {

            printSimulationStatus(currentTime);
            writer.println("--------------------------------------------------");
            System.out.println("--------------------------------------------------");
            int totalClients=0;
            for(Server s:servers)
            {
                totalClients+=s.getTasks().size();
            }
            if(totalClients>maxClients)
            {
                maxClients=totalClients;
                peakTime=currentTime;
            }

            for(Task t:tasks)
            {
                if(t.getArrivalTime()==currentTime+1)
                {
                   Server selectedServer= strategy.addTask(servers,t);
                   totalWaitingTime.addAndGet(t.getWaitingTime());
                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            currentTime++;
        }
        for (Server s : servers)
        {
            s.stop();
        }
        executorService.shutdown();

        writer.println("Average waiting time: "+(double)totalWaitingTime.get()/noOfClients);
        System.out.println("Average waiting time: "+(double)totalWaitingTime.get()/noOfClients);
        SwingUtilities.invokeLater(() -> textArea.append("Average waiting time: "+(double)totalWaitingTime.get()/noOfClients + "\n"));

        writer.println("Peak time: " + peakTime + " with " + maxClients + " clients");
        System.out.println("Peak time: " + peakTime + " with " + maxClients + " clients");
        SwingUtilities.invokeLater(() -> textArea.append("Peak time: " + peakTime + " with " + maxClients + " clients" + "\n"));

        writer.println("Average service time: "+(double)totalServiceTime/noOfClients);
        System.out.println("Average service time: "+(double)totalServiceTime/noOfClients);
        SwingUtilities.invokeLater(() -> textArea.append("Average service time: "+(double)totalServiceTime/noOfClients + "\n"));
    }
    public void stopSimulation()
    {
        running=false;
        for (Server s : servers)
        {
            s.stop();
        }
        closeWriter();
        executorService.shutdown();
        //currentThread().interrupt();
    }
    public void closeWriter() {
        if (writer != null) {
            writer.close();
        }
    }
    private void generateClients()
    {
        for (int i = 1; i <= noOfClients; i++)
        {
            Task c = new Task(i, maxArrivalTime, maxServiceTime, minArrivalTime, minServiceTime);
            totalServiceTime+=c.getServiceTime();
            tasks.add(c);
        }
    }

    private void sortClients()
    {
        List<Task> sortedTasks=new ArrayList<>();
        while(!tasks.isEmpty())
        {
            Task t=tasks.poll();
            sortedTasks.add(t);
        }
        sortedTasks.sort(Comparator.comparing(Task::getArrivalTime));
        tasks.addAll(sortedTasks);
    }


    public void printSimulationStatus(int currentTime)
    {
        System.out.println("Time " + currentTime);
        writer.println("Time " + currentTime);
        SwingUtilities.invokeLater(() -> textArea.setText("Time " + currentTime + "\n"));
        displayWaitingTasks();

        for (Server s : servers)
        {
            s.printTasks();
        }
        System.out.println();
    }

    private void displayWaitingTasks()
    {
        int nrTot=0;
        System.out.print("Waiting clients: ");
        writer.print("Waiting clients: ");
        SwingUtilities.invokeLater(() -> textArea.append("Waiting clients: "));
        for(Task t: tasks)
        {
            if(!t.isInQueue())
            {
                nrTot++;
                writer.printf("(%d,%d,%d); ",t.getId(),t.getArrivalTime(),t.getServiceTime());
                System.out.printf("(%d,%d,%d); ",t.getId(),t.getArrivalTime(),t.getServiceTime());
            }
        }
        int finalNrTot = nrTot;
        SwingUtilities.invokeLater(() -> textArea.append(finalNrTot + "\n"));
        writer.println();
        System.out.println();
    }
}
