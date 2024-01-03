package com.example.tasklisttwo.web.controller;

import com.example.tasklisttwo.model.task.Task;
import com.example.tasklisttwo.model.user.User;
import com.example.tasklisttwo.service.TaskService;
import com.example.tasklisttwo.service.UserService;
import com.example.tasklisttwo.web.dto.task.TaskDTO;
import com.example.tasklisttwo.web.dto.user.UserDTO;
import com.example.tasklisttwo.web.mappers.TaskMapper;
import com.example.tasklisttwo.web.mappers.UserMapper;
import com.example.tasklisttwo.web.validation.OnCreate;
import com.example.tasklisttwo.web.validation.OnUpdate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@Validated
public class UserController {

    private final UserService userService;
    private final TaskService taskService;
    private final UserMapper userMapper;
    private final TaskMapper taskMapper;

    public UserController(UserService userService, TaskService taskService, UserMapper userMapper, TaskMapper taskMapper) {
        this.userService = userService;
        this.taskService = taskService;
        this.userMapper = userMapper;
        this.taskMapper = taskMapper;
    }

    @PutMapping
    @PreAuthorize("@customSecurityExpression.canAccessUser(#dto.id)")
    public UserDTO update(@Validated(OnUpdate.class) @RequestBody UserDTO dto) {
        User user = userMapper.toEntity(dto);
        User updatedUser = userService.update(user);
        return userMapper.toDTO(updatedUser);
    }

    @GetMapping("/{id}")
    @PreAuthorize("@customSecurityExpression.canAccessUser(#id)")
    public UserDTO getById(@PathVariable Long id) {
        User user = userService.getById(id);
        return userMapper.toDTO(user);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@customSecurityExpression.canAccessUser(#id)")
    public void deleteById(@PathVariable Long id) {
        userService.delete(id);
    }

    @GetMapping("/{id}/tasks")
    @PreAuthorize("@customSecurityExpression.canAccessUser(#id)")
    public List<TaskDTO> getTasksByUserId(@PathVariable Long id) {
        List<Task> tasks = taskService.getAllByUserId(id);
        return taskMapper.toDTO(tasks);
    }

    @PostMapping("/{id}/tasks")
    @PreAuthorize("@customSecurityExpression.canAccessUser(#id)")
    public TaskDTO createTask(
            @PathVariable Long id,
            @Validated(OnCreate.class) @RequestBody TaskDTO dto
    ) {
        Task task = taskMapper.toEntity(dto);
        Task createdTask = taskService.create(task, id);
        return taskMapper.toDTO(createdTask);
    }

}
