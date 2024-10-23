package com.example.spring.service;

import com.example.spring.dao.UserRepository;
import com.example.spring.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getUsers(String username) {
        List<User> users = new ArrayList<>();
        if (username != null) {
            users.addAll(userRepository.findAllByUsername(username));
        } else {
            users.addAll(userRepository.findAll());
        }

        return users;
    }

    public boolean addUser(User user) {
        try {
            userRepository.save(user);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public User getUserDetails(long id) {
        return userRepository.findById(id).orElse(null);
    }

    public boolean editUser(User user) {
        try {
            User exitedUser = userRepository.findById(user.getId()).orElse(null);
            if (exitedUser == null) {
                return false;
            } else {
                userRepository.save(user);
                return true;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public boolean deleteUser(long id) {
        try {
            userRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
