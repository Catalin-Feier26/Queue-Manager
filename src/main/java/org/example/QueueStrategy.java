package org.example;

import java.util.List;

public class QueueStrategy implements Strategy{
    public  void addTask(List<Server> servers, Task t){
        Server minQueueServer = servers.get(0);

        for(Server server : servers){
            if(server.getTasks().size() < minQueueServer.getTasks().size()){
                minQueueServer = server;
            }
        }

        minQueueServer.addTask(t);
    }
}
