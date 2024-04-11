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
    }
}
