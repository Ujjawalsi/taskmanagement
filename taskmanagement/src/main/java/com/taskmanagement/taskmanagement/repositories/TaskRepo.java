package com.taskmanagement.taskmanagement.repositories;

import com.taskmanagement.taskmanagement.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepo extends JpaRepository<Task, Long> {

  List<Task> findByStatus(String status);
}
