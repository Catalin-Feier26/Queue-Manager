package org.example;

import java.util.List;

public class Scheduler {
    private Strategy strategy;
    private int maxNoServers;
    private int maxTasksPerServer;
    private List<Server> servers;
    private Scheduler(int maxNoServers, int maxTasksPerServer){
        for(int i = 0; i < maxNoServers; i++){
            assert servers != null;
            servers.add(new Server(i));
        }
    }
    public void changeStrategy(Policy policy){
        if(policy == Policy.SHORTEST_TIME){
            strategy = new TimeStrategy();
        }
        else if(policy == Policy.SHORTEST_QUEUE){
            strategy = new QueueStrategy();
        }
    }
    public void dispatchTask(Task t){
        strategy.addTask(servers, t);
    }
    private List<Server> getServers(){
        return servers;
    }
}
