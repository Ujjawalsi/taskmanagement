package com.taskmanagement.taskmanagement.implementations;

import com.taskmanagement.taskmanagement.entities.User;
import com.taskmanagement.taskmanagement.exceptions.ResourceNotFoundException;
import com.taskmanagement.taskmanagement.repositories.UserRepo;
import com.taskmanagement.taskmanagement.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;
    @Override
    public User createUser(User user) {
        User user1 = new User();
        user1.setPassword(user.getPassword());
        user1.setUsername(user.getUsername());
        user1.setRoles(user.getRoles());
        return this.userRepo.save(user1);
    }

    @Override
    public User updateUser(User user, Long userId) {
        User user1 = this.userRepo.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User", "userId", userId));
        user1.setUsername(user.getUsername());
        user1.setPassword(user.getPassword());
        user1.setRoles(user.getRoles());
        return this.userRepo.save(user1);
    }

    @Override
    public User getByUserId(Long userId) {
        return this.userRepo.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User", "userId", userId));
    }

    @Override
    public List<User> getAllUser() {
        return this.userRepo.findAll();
    }

    @Override
    public void deleteUser(Long userId) {
        User existingUser = this.userRepo.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User", "userId", userId));
        this.userRepo.delete(existingUser);

    }

    @Override
    public User findUserByUserName(String username) {
        return this.userRepo.findByUserName(username);
    }
}
