package com.example.tasklisttwo.web.controller;

import com.example.tasklisttwo.model.task.Task;
import com.example.tasklisttwo.service.TaskService;
import com.example.tasklisttwo.web.dto.task.TaskDTO;
import com.example.tasklisttwo.web.mappers.TaskMapper;
import com.example.tasklisttwo.web.validation.OnUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tasks")
@Validated
public class TaskController {

    private final TaskService taskService;
    private final TaskMapper taskMapper;

    @Autowired
    public TaskController(TaskService taskService, TaskMapper taskMapper) {
        this.taskService = taskService;
        this.taskMapper = taskMapper;
    }

    @GetMapping("/{id}")
    @PreAuthorize("@customSecurityExpression.canAccessTask(#id)")
    public TaskDTO getById(@PathVariable Long id) {
        Task task = taskService.getById(id);
        return taskMapper.toDTO(task);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@customSecurityExpression.canAccessTask(#id)")
    public void deleteById(@PathVariable Long id) {
        taskService.delete(id);
    }

    @PutMapping
    @PreAuthorize("@customSecurityExpression.canAccessTask(#dto.id)")
    public TaskDTO update(@Validated(OnUpdate.class) @RequestBody TaskDTO dto) {
        Task task = taskMapper.toEntity(dto);
        Task updatedTask = taskService.update(task);
        return taskMapper.toDTO(updatedTask);
    }

}
