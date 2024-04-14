package org.example;

import java.util.List;

public interface Strategy {
    public  Server addTask(List<Server> servers, Task t);
}
