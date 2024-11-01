package com.example.spring.service;

import com.example.spring.dao.UserRepository;
import com.example.spring.dto.UserDTO;
import com.example.spring.helper.CSVHelper;
import com.example.spring.model.User;
import com.example.spring.storage.StorageService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final StorageService storageService;

    @Autowired
    public UserService(UserRepository userRepository, StorageService storageService) {
        this.userRepository = userRepository;
        this.storageService = storageService;
    }

    public Page<User> getUsers(String username, Pageable pageable) {
        if (username != null) {
            return userRepository.findAllByUsernameContainingIgnoreCase(username, pageable);
        } else {
            return userRepository.findAll(pageable);
        }
    }

    public boolean addUser(UserDTO userDTO) {
        try {
            userRepository.save(userDTO.toUser());
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

    @Transactional
    public void storeFileAndImportUsers(MultipartFile file) {
        try {
            List<User> users = CSVHelper.csvToUsers(file.getInputStream());
            userRepository.saveAll(users);
            storageService.store(file);
        } catch (IOException e) {
            throw new RuntimeException("Fail to store csv data: " + e.getMessage());
        }
    }

    public Stream<Path> getUploadFilePaths() {
        storageService.init();
        return storageService.loadAll();
    }

    public Resource loadFileAsResource(String filename) {
        return storageService.loadAsResource(filename);
    }
}
