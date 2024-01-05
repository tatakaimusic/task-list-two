package com.example.tasklisttwo.repository;

import com.example.tasklisttwo.model.task.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query(value = """
            SELECT * FROM tasks t
            JOIN users_tasks ut ON  ut.task_id = t.id
            WHERE ut.user_id = :userId
                        """, nativeQuery = true)
    List<Task> findAllByUserId(@Param("userId") Long userId);

    @Modifying
    @Query(value = """
            INSERT INTO users_tasks (user_id, task_id)
            VALUES (:userId, :taskId)
            """, nativeQuery = true)
    void assignTask(@Param("userId") Long userId, @Param("taskId") Long taskId);

    @Modifying
    @Query(value = """
                        DELETE FROM tasks_images ti 
                        WHERE  ti.image = :name 
                        AND ti.task_id = :id
            """, nativeQuery = true)
    void deleteImage(@Param("name") String name, @Param("id") Long id);

    @Modifying
    @Query(value = """
                        DELETE FROM tasks_images ti 
                        WHERE ti.task_id = :id
            """, nativeQuery = true)
    void deleteAllImages(@Param("id") Long id);

}
