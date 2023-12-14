package com.example.tasklisttwo.web.mappers;

import com.example.tasklisttwo.model.task.Task;
import com.example.tasklisttwo.web.dto.task.TaskDTO;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface TaskMapper extends Mappable<Task, TaskDTO> {

}
