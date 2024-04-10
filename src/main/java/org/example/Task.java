package org.example;

public class Task  implements Runnable{
    private int id;
    private int arrivalTime;
    private int serviceTime;
    public int waitingTime;
    public Task(int id, int arrivalTime, int serviceTime) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
        waitingTime=0;
    }
    public Task(int id, int maxArrivalTime, int maxServiceTime, int minArrivalTime, int minServiceTime) {
        this.id = id;
        this.arrivalTime = generateRandomTime(minArrivalTime, maxArrivalTime);
        this.serviceTime = generateRandomTime(minServiceTime, maxServiceTime);
        waitingTime=0;
    }
    public void run(){
        try {
            Thread.sleep(arrivalTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Client "+id+" arrived at "+arrivalTime+" and has the following service time"+serviceTime);
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
}