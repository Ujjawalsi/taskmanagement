package com.taskmanagement.taskmanagement.controllers;

import com.taskmanagement.taskmanagement.constants.ApiResponse;
import com.taskmanagement.taskmanagement.entities.User;
import com.taskmanagement.taskmanagement.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/")
    public ResponseEntity<User> createUser(@RequestBody User user ){

            User userCreated = this.userService.createUser(user);
            return new ResponseEntity<>(userCreated, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User>updateUser(@RequestBody User updatedUser,@PathVariable("id")Long userId){
        User updateUser = this.userService.updateUser(updatedUser, userId);
        return new ResponseEntity<>(updateUser, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse>deleteUser(@PathVariable("id")Long userId){
        this.userService.deleteUser(userId);
        return new ResponseEntity<>(new ApiResponse("User Deleted Successfully", true), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User>getUser(@PathVariable("id")Long userId){
        User user = this.userService.getByUserId(userId);
        return new ResponseEntity<>(user, HttpStatus.OK);

    }

    @GetMapping("/")
    public ResponseEntity<List<User>>getAllUsers(){
        List<User>userList= this.userService.getAllUser();
        return new ResponseEntity<>(userList,HttpStatus.OK);

    }
}
