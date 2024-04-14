package org.example;

import java.util.List;

public interface Strategy
{
      Server addTask(List<Server> servers, Task t);
}
