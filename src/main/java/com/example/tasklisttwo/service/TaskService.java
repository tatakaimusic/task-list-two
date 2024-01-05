package com.example.tasklisttwo.service;

import com.example.tasklisttwo.model.task.Task;
import com.example.tasklisttwo.model.task.TaskImage;

import java.util.List;

public interface TaskService {

    Task getById(Long id);

    List<Task> getAllByUserId(Long userId);

    Task update(Task task);

    Task create(Task task, Long userId);

    void delete(Long id);

    void uploadImage(Long taskId, TaskImage image);

    List<TaskImage> getImages(Long taskId);

    void deleteImage(Long id, String name);

    void deleteAllImagesById(Long id);

}
