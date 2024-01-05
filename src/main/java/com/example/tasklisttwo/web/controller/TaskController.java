package com.example.tasklisttwo.web.controller;

import com.example.tasklisttwo.model.task.Task;
import com.example.tasklisttwo.model.task.TaskImage;
import com.example.tasklisttwo.service.ImageService;
import com.example.tasklisttwo.service.TaskService;
import com.example.tasklisttwo.web.dto.task.ImageName;
import com.example.tasklisttwo.web.dto.task.TaskDTO;
import com.example.tasklisttwo.web.dto.task.TaskImageDTO;
import com.example.tasklisttwo.web.mappers.TaskImageMapper;
import com.example.tasklisttwo.web.mappers.TaskMapper;
import com.example.tasklisttwo.web.validation.OnUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
@Validated
public class TaskController {

    private final TaskService taskService;
    private final ImageService imageService;
    private final TaskMapper taskMapper;
    private final TaskImageMapper taskImageMapper;

    @Autowired
    public TaskController(TaskService taskService, ImageService imageService, TaskMapper taskMapper, TaskImageMapper taskImageMapper) {
        this.taskService = taskService;
        this.imageService = imageService;
        this.taskMapper = taskMapper;
        this.taskImageMapper = taskImageMapper;
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

    @PostMapping("/{id}/image")
    @PreAuthorize("@customSecurityExpression.canAccessTask(#id)")
    public void uploadImage(@PathVariable Long id,
                            @Validated @ModelAttribute TaskImageDTO imageDTO
    ) {
        TaskImage image = taskImageMapper.toEntity(imageDTO);
        taskService.uploadImage(id, image);
    }

    @GetMapping("/{id}/images")
    @PreAuthorize("@customSecurityExpression.canAccessTask(#id)")
    public List<TaskImageDTO> downloadImages(@PathVariable Long id) {
        List<TaskImage> images = taskService.getImages(id);
        List<TaskImageDTO> dtos = taskImageMapper.toDTO(images);
        return dtos;
    }

    @DeleteMapping("/{id}/image")
    @PreAuthorize("@customSecurityExpression.canAccessTask(#id)")
    public void deleteImage(@PathVariable Long id, @RequestBody ImageName name) {
        taskService.deleteImage(id, name.getName());
    }

    @DeleteMapping("/{id}/images")
    @PreAuthorize("@customSecurityExpression.canAccessTask(#id)")
    public void deleteAllImages(@PathVariable Long id) {
        taskService.deleteAllImagesById(id);
    }

}
