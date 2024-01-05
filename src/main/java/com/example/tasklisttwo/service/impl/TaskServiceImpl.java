package com.example.tasklisttwo.service.impl;

import com.example.tasklisttwo.model.exception.ImageDeleteException;
import com.example.tasklisttwo.model.exception.ResourceNotFoundException;
import com.example.tasklisttwo.model.task.Status;
import com.example.tasklisttwo.model.task.Task;
import com.example.tasklisttwo.model.task.TaskImage;
import com.example.tasklisttwo.repository.TaskRepository;
import com.example.tasklisttwo.service.ImageService;
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
    private final ImageService imageService;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository, UserService userService, ImageService imageService) {
        this.taskRepository = taskRepository;
        this.userService = userService;
        this.imageService = imageService;
    }

    @Override
//    @Cacheable(value = "TaskService::getById", key = "#id")
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
//    @CachePut(value = "TaskService::getById", key = "#task.id")
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
//    @CacheEvict(value = "TaskService::getById", key = "#id")
    public void delete(Long id) {
        taskRepository.deleteById(id);
    }

    @Override
    @Transactional
//    @CacheEvict(value = "TaskService::getById", key = "#taskId")
    public void uploadImage(Long taskId, TaskImage image) {
        Task task = getById(taskId);
        String filename = imageService.upload(image);
        task.getImages().add(filename);
        taskRepository.save(task);
    }

    @Override
    public List<TaskImage> getImages(Long taskId) {
        Task task = getById(taskId);
        List<String> fileNames = task.getImages();
        List<TaskImage> images = imageService.getImages(fileNames);
        return images;
    }

    @Transactional
    @Override
    public void deleteImage(Long id, String name) {
        Task task = getById(id);
        if (!task.getImages().remove(name)) {
            throw new ImageDeleteException("This image doesn't exist!");
        }
        taskRepository.deleteImage(name, id);
        imageService.delete(name);
    }

    @Transactional
    @Override
    public void deleteAllImagesById(Long id) {
        Task task = getById(id);
        if (task.getImages().size() == 0) {
            throw new ImageDeleteException("This task doesn't have any images!");
        }
        imageService.deleteAll(task.getImages());
        taskRepository.deleteAllImages(id);
    }

}
