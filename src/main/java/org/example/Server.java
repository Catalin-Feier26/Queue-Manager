package org.example;


import javax.swing.*;
import java.io.PrintWriter;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements Runnable
{
    private int id;
    private ConcurrentLinkedQueue<Task> tasks;
    private AtomicInteger waitingPeriod;
    private volatile boolean shouldRun;

    // To print the tasks in the queue in the file and in the text area
    private PrintWriter writer;
    private JTextArea textArea;

    public Server(int id, PrintWriter writer, JTextArea textArea)
    {
        this.id=id;
        this.waitingPeriod=new AtomicInteger(0);
        this.tasks=new ConcurrentLinkedQueue<>();
        shouldRun=true;
        this.writer = writer;
        this.textArea = textArea;
    }

    public void addTask(Task t)
    {
        tasks.add(t);
        t.setInQueue(true);

        if(tasks.isEmpty())
        {
            t.waitingTime=0;
        }else
        {
            t.waitingTime=waitingPeriod.get();
        }
        waitingPeriod.addAndGet(t.getServiceTime());
    }

    public void run()
    {
        while(shouldRun)
        {
            if(!tasks.isEmpty())
            {
                Task t=tasks.peek();
                t.waitingTime=waitingPeriod.get();
                for(int i=0;i<t.getServiceTime();i++)
                {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e)
                    {
                        shouldRun = false;
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
                tasks.poll();
                waitingPeriod.addAndGet(-t.getServiceTime());
            }
        }
    }
    public void printTasks()
    {
        System.out.print("Queue " + id + ": ");
        writer.print("Queue " + id + ": ");
        textArea.append("Queue " + id + ": ");
        if (!tasks.isEmpty())
        {
            for(Task t:tasks)
            {
                System.out.printf("(%d,%d,%d); ",t.getId(),t.getArrivalTime(),t.getServiceTime());
                writer.printf("(%d,%d,%d); ",t.getId(),t.getArrivalTime(),t.getServiceTime());
                textArea.append("(" + t.getId() + "," + t.getArrivalTime() + "," + t.getServiceTime() + "); ");
            }
        } else {
            System.out.print("closed");
            writer.print("closed");
            textArea.append("closed");
        }
        System.out.println();
        writer.println();
        textArea.append("\n");
    }
    public int getId(){
        return id;
    }
    public void stop(){
        shouldRun=false;
        Thread.currentThread().interrupt();
    }
    public int getWaitingPeriod(){
        return waitingPeriod.get();
    }
    public ConcurrentLinkedQueue<Task> getTasks(){
        return tasks;
    }

}
