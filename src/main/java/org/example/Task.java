package org.example;

public class Task
{
    private int id;
    private int arrivalTime;
    private int serviceTime;
    public int leftServiceTime;
    public int waitingTime;
    private boolean inQueue;
    public boolean isAtFront;
    public Task(int id, int arrivalTime, int serviceTime)
    {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
        leftServiceTime=serviceTime;
        waitingTime=0;
        isAtFront=false;
    }

    public Task(int id, int maxArrivalTime, int maxServiceTime, int minArrivalTime, int minServiceTime)
    {
        this.id = id;
        this.arrivalTime = generateRandomTime(minArrivalTime, maxArrivalTime);
        this.serviceTime = generateRandomTime(minServiceTime, maxServiceTime);
        waitingTime=0;
        inQueue=false;
    }

    public boolean isInQueue() {
        return inQueue;
    }
    public void setInQueue(boolean inQueue) {
        this.inQueue = inQueue;
    }
    public int getId() {
        return id;
    }
    public int getArrivalTime() {
        return arrivalTime;
    }
    public int getServiceTime() {
        return serviceTime;
    }
    private int generateRandomTime(int min, int max){
        return (int)(Math.random() * (max - min + 1) + min);
    }

    public int getWaitingTime() {
        return waitingTime;
    }
}
