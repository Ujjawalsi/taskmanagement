package com.taskmanagement.taskmanagement.controllers;

import com.taskmanagement.taskmanagement.constants.ApiResponse;
import com.taskmanagement.taskmanagement.constants.AppConstants;
import com.taskmanagement.taskmanagement.constants.TaskResponse;
import com.taskmanagement.taskmanagement.entities.Task;
import com.taskmanagement.taskmanagement.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;


    @PostMapping("/")
    public ResponseEntity<Task> createTask(@RequestBody Task task ){
            Task task1 = this.taskService.createTask(task);
            return new ResponseEntity<>(task1, HttpStatus.CREATED);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Task>updateTask(@RequestBody Task updatedTask,@PathVariable("id")Long taskId){
        Task task1  = this.taskService.updateTask(updatedTask, taskId);
        return new ResponseEntity<>(task1, HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Task>getSingleTask(@PathVariable("id")Long taskId){
        Task task= this.taskService.getTaskByTaskId(taskId);
        return new ResponseEntity<>(task, HttpStatus.OK);

    }

    @GetMapping("/")
    public ResponseEntity<TaskResponse>getAllTasks(@RequestParam(value = "pageNumber",defaultValue = AppConstants.PAGE_NUMBER, required = false)Integer pageNumber,
                                                 @RequestParam(value = "pageSize",defaultValue = AppConstants.PAGE_SIZE,required = false)Integer pageSize,
                                                 @RequestParam(value = "sortBY", defaultValue = AppConstants.SORT_BY, required = false)String sortBy,
                                                 @RequestParam(value = "sortDir",defaultValue = AppConstants.SORT_DIR,required = false)String sortDir, @AuthenticationPrincipal UserDetails userDetails){
      TaskResponse taskList= this.taskService.getAllTasks(pageNumber,pageSize,sortBy,sortDir, userDetails);
        return new ResponseEntity<>(taskList,HttpStatus.OK);

    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Task>> getAllTasksByStatus(@PathVariable("status") String status){
        List<Task> taskListByStatus = this.taskService.getTaskByStatus(status);
        return new ResponseEntity<>(taskListByStatus, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteTask(@PathVariable("id") Long taskId){
        this.taskService.deleteTask(taskId);
        return new ResponseEntity<>(new ApiResponse("Task Deleted Successfully", true), HttpStatus.OK);
    }
}
