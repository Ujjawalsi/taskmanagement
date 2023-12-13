package com.taskmanagement.taskmanagement.repositories;

import com.taskmanagement.taskmanagement.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepo extends JpaRepository<User, Long> {
        @Query(value = "SELECT * FROM user WHERE user_name = :userName", nativeQuery = true)
        User findByUserName(@Param("userName") String username);


}
