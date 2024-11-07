package com.example.spring.service;

import com.example.spring.dto.UserDTO;
import com.example.spring.model.User;
import jakarta.transaction.Transactional;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

public interface UserService {
    Page<User> getUsers(String username, Pageable pageable);

    void addUser(UserDTO userDTO);

    User getUserDetails(long id);

    boolean editUser(User user);

    boolean deleteUser(long id);

    @Transactional
    void storeFileAndImportUsers(MultipartFile file);

    Stream<Path> getUploadFilePaths();

    Resource loadFileAsResource(String filename);

    List<User> getUsersByBirthday(Date birthday);
}
