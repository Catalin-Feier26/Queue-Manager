package org.example;

import java.util.List;

public class TimeStrategy implements Strategy{
    public  void addTask(List<Server> servers, Task t){
        Server minTimeServer = servers.getFirst();

        for(Server server : servers){
            if(server.getWaitingPeriod() < minTimeServer.getWaitingPeriod()){
                minTimeServer = server;
            }
        }
        minTimeServer.addTask(t);
      //  System.out.println("Client "+t.getId()+" arrived at "+t.getArrivalTime()+" and will be directed to queue "+minTimeServer.getId()+ " new waiting time:" + t.waitingTime);
    }
}
