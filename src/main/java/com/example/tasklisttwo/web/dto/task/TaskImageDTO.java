package com.example.tasklisttwo.web.dto.task;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public class TaskImageDTO {

    @NotNull(message = "Image must be not null!")
    private MultipartFile file;

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

}
