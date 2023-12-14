package com.example.tasklisttwo.web.mappers;

import com.example.tasklisttwo.model.user.User;
import com.example.tasklisttwo.web.dto.user.UserDTO;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface UserMapper extends Mappable<User, UserDTO> {

}
