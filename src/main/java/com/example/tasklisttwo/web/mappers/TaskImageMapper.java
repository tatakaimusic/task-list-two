package com.example.tasklisttwo.web.mappers;

import com.example.tasklisttwo.model.task.TaskImage;
import com.example.tasklisttwo.web.dto.task.TaskImageDTO;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface TaskImageMapper extends Mappable<TaskImage, TaskImageDTO> {
}
