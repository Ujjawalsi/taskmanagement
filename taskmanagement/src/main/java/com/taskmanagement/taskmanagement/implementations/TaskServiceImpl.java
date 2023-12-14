package com.taskmanagement.taskmanagement.implementations;

import com.taskmanagement.taskmanagement.constants.TaskResponse;
import com.taskmanagement.taskmanagement.entities.Role;
import com.taskmanagement.taskmanagement.entities.Task;
import com.taskmanagement.taskmanagement.exceptions.ResourceNotFoundException;
import com.taskmanagement.taskmanagement.exceptions.ResourceNotFoundExceptionForStatus;
import com.taskmanagement.taskmanagement.repositories.TaskRepo;
import com.taskmanagement.taskmanagement.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepo taskRepo;
    @Override
    public Task createTask(Task task) {
        Task task1 = new Task();
        task1.setDescription(task.getDescription());
        task1.setTitle(task.getTitle());
        task1.setDueDate(task.getDueDate());
        if (task.getStatus().equalsIgnoreCase("Pending")|| task.getStatus().equalsIgnoreCase("In progress") || task.getStatus().equalsIgnoreCase("Completed")
        ||  task.getStatus().equalsIgnoreCase("New")){
            task1.setStatus(task.getStatus());
        }else{
            throw new RuntimeException("Invalid status. Allowed values: New ,pending, in progress, completed");
        }
        return this.taskRepo.save(task1);
    }

    @Override
    public Task updateTask(Task task, Long taskId) {
        Task task1 = this.taskRepo.findById(taskId).orElseThrow(()-> new ResourceNotFoundException("Task", "taskId", taskId));
        if (task.getStatus().equalsIgnoreCase("Pending")|| task.getStatus().equalsIgnoreCase("In progress") || task.getStatus().equalsIgnoreCase("Completed")
                ||  task.getStatus().equalsIgnoreCase("New")) {
            task1.setStatus(task.getStatus());
        }else{
            throw new RuntimeException("Invalid status. Allowed values: New ,pending, in progress, completed");
        }
        task1.setTitle(task.getTitle());
        task1.setDescription(task.getDescription());
        task1.setDueDate(task.getDueDate());
        return this.taskRepo.save(task1);
    }

    @Override
    public Task getTaskByTaskId(Long taskId) {
        return this.taskRepo.findById(taskId).orElseThrow(() -> new ResourceNotFoundException("Task", "taskId", taskId));
    }

    @Override
    public TaskResponse getAllTasks(Integer pageNumber, Integer pageSize, String sortBy, String sortDir , UserDetails userDetails) {

        if (userDetails == null) {
            throw new RuntimeException("Unable to find UserDetails");
        }
        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(role -> role.getAuthority().equals(Role.ADMIN.name()));
        boolean isSubUser = userDetails.getAuthorities().stream().anyMatch(role -> role.getAuthority().equalsIgnoreCase(Role.SUB_ADMIN.name()));
        boolean isUser = userDetails.getAuthorities().stream().anyMatch(role -> role.getAuthority().equalsIgnoreCase(Role.USER.name()));

        Sort sort = (sortDir.equalsIgnoreCase("asc")) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageRequest = PageRequest.of(pageNumber, pageSize, sort);
        Page<Task> pageTasks = this.taskRepo.findAll(pageRequest);
        List<Task> taskList = pageTasks.getContent();
        TaskResponse taskResponse = new TaskResponse();
        if (isAdmin) {
            taskResponse.setContent(taskList);
        } else if (isSubUser) {
         List<Task>subUsersTaskList = taskList.stream().filter(task->task.getStatus().equalsIgnoreCase("pending")|| task.getStatus().equalsIgnoreCase("in progress")).toList();
         taskResponse.setContent(subUsersTaskList);
        } else if (isUser) {
            List<Task>usersTaskList = taskList.stream().filter(task->task.getStatus().equalsIgnoreCase("New")).toList();
            taskResponse.setContent(usersTaskList);
        }
        taskResponse.setPageNumber(pageTasks.getNumber());
        taskResponse.setPageSize(pageTasks.getSize());
        taskResponse.setTotalElements(pageTasks.getTotalElements());
        taskResponse.setTotalPages(pageTasks.getTotalPages());
        taskResponse.setLastPage(pageTasks.isLast());
        return taskResponse;
    }

    @Override
    public List<Task> getTaskByStatus(String status) {
        List<Task> taskList = this.taskRepo.findByStatus(status);
        if (taskList.isEmpty()){
            throw new ResourceNotFoundExceptionForStatus("Task", "status", status);
        }
        return taskList;
    }

    @Override
    public void deleteTask(Long taskId) {
        Task existingTask = this.taskRepo.findById(taskId).orElseThrow(() -> new ResourceNotFoundException("Task", "taskId", taskId));
        this.taskRepo.delete(existingTask);

    }
}
