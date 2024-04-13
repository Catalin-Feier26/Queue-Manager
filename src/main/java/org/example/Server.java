package org.example;

import java.util.SortedMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements Runnable{
    private int id;
    private ConcurrentLinkedQueue<Task> tasks;
    private AtomicInteger waitingPeriod;
    private volatile boolean shouldRun;
    public Server(int id){
        this.id=id;
        this.waitingPeriod=new AtomicInteger(0);
        this.tasks=new ConcurrentLinkedQueue<>();
        shouldRun=true;
    }
    public void addTask(Task t){
        t.waitingTime=waitingPeriod.get();
        tasks.add(t);
        waitingPeriod.addAndGet(t.getServiceTime());
    }
    public void run(){
        while(shouldRun)
        {
            if(!tasks.isEmpty())
            {
                Task t=tasks.poll();
                t.waitingTime=waitingPeriod.get();
                for(int i=0;i<t.getServiceTime();i++)
                {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("Client "+t.getId()+" has left the queue "+id+" at time "+(t.getArrivalTime()+t.getServiceTime())+" waiting time: "+t.waitingTime);
                waitingPeriod.addAndGet(-t.getServiceTime());
            }
        }
    }
    public void printTasks(){
        for(Task t:tasks){
            System.out.println("Client"+t.getId()+" arrived at "+t.getArrivalTime());
        }
    }
    public int getId(){
        return id;
    }
    public void stop(){
        shouldRun=false;
    }
    public int getWaitingPeriod(){
        return waitingPeriod.get();
    }
    public ConcurrentLinkedQueue<Task> getTasks(){
        return tasks;
    }

}
