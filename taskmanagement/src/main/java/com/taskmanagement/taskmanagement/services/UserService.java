package com.taskmanagement.taskmanagement.services;

import com.taskmanagement.taskmanagement.entities.User;

import java.util.List;

public interface UserService {

    User createUser(User user);

    User updateUser(User user , Long userId);

    User getByUserId(Long userId);

   List<User> getAllUser();

    void deleteUser(Long userId);

    User findUserByUserName(String username);
}
