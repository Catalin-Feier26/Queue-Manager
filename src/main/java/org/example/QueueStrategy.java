package org.example;

import java.util.List;

public class QueueStrategy implements Strategy{
    public  Server addTask(List<Server> servers, Task t){
        Server minQueueServer = servers.get(0);

        for(Server server : servers){
            if(server.getTasks().size() < minQueueServer.getTasks().size()){
                minQueueServer = server;
            }
        }
    //    System.out.println("Client "+t.getId()+" arrived at "+t.getArrivalTime()+" and will be directed to queue "+minQueueServer.getId()+" new waiting time:" + minQueueServer.getWaitingPeriod());
        minQueueServer.addTask(t);
     //   System.out.println("Client "+t.getId()+" arrived at "+t.getArrivalTime()+" and will be directed to queue "+minQueueServer.getId()+" new waiting time:" + minQueueServer.getWaitingPeriod());
        return minQueueServer;
    }
}
