package com.example.tasklisttwo.service;

import com.example.tasklisttwo.model.task.TaskImage;

import java.util.List;


public interface ImageService {

    String upload(TaskImage image);

    List<TaskImage> getImages(List<String> fileNames);

    void delete(String name);

    void deleteAll(List<String> names);

}
