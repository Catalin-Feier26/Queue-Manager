package org.example;

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
        tasks.add(t);
        waitingPeriod.addAndGet(t.getServiceTime());
    }
    public void run(){
        while(shouldRun){
            if(!tasks.isEmpty()){
                Task t=tasks.poll();
                try {
                    Thread.sleep(t.getServiceTime());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("Server "+id+" stopped");
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
