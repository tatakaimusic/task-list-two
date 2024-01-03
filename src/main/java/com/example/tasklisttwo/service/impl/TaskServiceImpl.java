package com.example.tasklisttwo.service.impl;

import com.example.tasklisttwo.model.exception.ResourceNotFoundException;
import com.example.tasklisttwo.model.task.Status;
import com.example.tasklisttwo.model.task.Task;
import com.example.tasklisttwo.repository.TaskRepository;
import com.example.tasklisttwo.service.TaskService;
import com.example.tasklisttwo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserService userService;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository, UserService userService) {
        this.taskRepository = taskRepository;
        this.userService = userService;
    }

    @Override
    @Cacheable(value = "TaskService::getById", key = "#id")
    public Task getById(Long id) {
        return taskRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(
                        "Task with this id doesn't exist!"
                )
        );
    }

    @Override
    public List<Task> getAllByUserId(Long userId) {
        return taskRepository.findAllByUserId(userId);
    }

    @Transactional
    @Override
    @CachePut(value = "TaskService::getById", key = "#task.id")
    public Task update(Task task) {
        if (task.getStatus() == null) {
            task.setStatus(Status.TODO);
        }
        return taskRepository.save(task);
    }

    @Transactional
    @Override
    public Task create(Task task, Long userId) {
        task.setStatus(Status.TODO);
        task = taskRepository.save(task);
        taskRepository.assignTask(userId, task.getId());
        return task;
    }

    @Transactional
    @Override
    @CacheEvict(value = "TaskService::getById", key = "#id")
    public void delete(Long id) {
        taskRepository.deleteById(id);
    }

}
