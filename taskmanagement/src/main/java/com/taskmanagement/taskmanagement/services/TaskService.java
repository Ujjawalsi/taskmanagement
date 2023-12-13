package com.taskmanagement.taskmanagement.services;

import com.taskmanagement.taskmanagement.constants.TaskResponse;
import com.taskmanagement.taskmanagement.entities.Task;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface TaskService {

    Task createTask(Task task);

    Task updateTask(Task task , Long taskId);

    Task getTaskByTaskId(Long taskId);

   TaskResponse getAllTasks(Integer pageNumber, Integer pageSize, String sortBy, String sortDir, UserDetails userDetails);

    List<Task> getTaskByStatus(String status);


    void deleteTask(Long taskId);


}
