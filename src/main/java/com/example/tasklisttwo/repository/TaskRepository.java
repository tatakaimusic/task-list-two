package com.example.tasklisttwo.repository;

import com.example.tasklisttwo.model.task.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query(value = """
            SELECT * FROM tasks t
            JOIN users_tasks ut ON  ut.task_id = t.id
            WHERE ut.user_id = :userId
                        """, nativeQuery = true)
    List<Task> findAllByUserId(@Param("userId") Long userId);

}