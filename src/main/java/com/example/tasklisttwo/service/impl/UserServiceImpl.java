package com.example.tasklisttwo.service.impl;

import com.example.tasklisttwo.model.exception.ResourceNotFoundException;
import com.example.tasklisttwo.model.user.Role;
import com.example.tasklisttwo.model.user.User;
import com.example.tasklisttwo.repository.UserRepository;
import com.example.tasklisttwo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
//    @Cacheable(value = "UserService::getById", key = "#id")
    public User getById(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User with this id doesn't exist!")
        );
    }

    @Override
//    @Cacheable(value = "UserService::getByUsername", key = "#username")
    public User getByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(
                () -> new ResourceNotFoundException("User with this username doesn't exist!")
        );
    }

    @Override
    @Transactional
//    @Caching(put = {
//            @CachePut(value = "UserService::getById", key = "#user.id"),
//            @CachePut(value = "UserService::getByUsername", key = "#user.username")
//    })
    public User update(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    @Transactional
//    @Caching(cacheable = {
//            @Cacheable(value = "UserService::getByUsername", key = "#user.username")
//    })
    public User create(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new IllegalStateException("User already exists!");
        }
        if (!user.getPassword().equals(user.getPasswordConfirmation())) {
            throw new IllegalStateException("Password and password confirmation don't match!");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Set<Role> roles = Set.of(Role.ROLE_USER);
        user.setRoles(roles);
        return userRepository.save(user);
    }

    @Override
//    @Cacheable(value = "UserService::isTaskOwner", key = "#userId + '.' + #taskId")
    public boolean isTaskOwner(Long userId, Long taskId) {
        return userRepository.isTaskOwner(userId, taskId);
    }

    @Override
    @Transactional
//    @Caching(evict = {
//            @CacheEvict(value = "UserService::getById", key = "#id")
//    })
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

}
