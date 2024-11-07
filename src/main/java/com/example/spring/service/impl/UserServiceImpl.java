package com.example.spring.service.impl;

import com.example.spring.dao.UserRepository;
import com.example.spring.dto.UserDTO;
import com.example.spring.helper.CSVHelper;
import com.example.spring.model.User;
import com.example.spring.service.StorageService;
import com.example.spring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final StorageService storageService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, StorageService storageService) {
        this.userRepository = userRepository;
        this.storageService = storageService;
    }

    @Override
    public Page<User> getUsers(String username, Pageable pageable) {
        if (username != null) {
            return userRepository.findAllByUsernameContainingIgnoreCase(username, pageable);
        } else {
            return userRepository.findAll(pageable);
        }
    }

    @Override
    public void addUser(UserDTO userDTO) {
        userRepository.save(userDTO.toUser());
    }

    @Override
    public User getUserDetails(long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
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

    @Override
    public boolean deleteUser(long id) {
        try {
            userRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void storeFileAndImportUsers(MultipartFile file) {
        try {
            List<User> users = CSVHelper.csvToUsers(file.getInputStream());
            userRepository.saveAll(users);
            storageService.store(file);
        } catch (IOException e) {
            throw new RuntimeException("Fail to store csv data: " + e.getMessage());
        }
    }

    @Override
    public Stream<Path> getUploadFilePaths() {
        storageService.init();
        return storageService.loadAll();
    }

    @Override
    public Resource loadFileAsResource(String filename) {
        return storageService.loadAsResource(filename);
    }

    @Override
    public List<User> getUsersByBirthday(Date birthday) {
        return userRepository.findUsersByBirthday(birthday);
    }
}
