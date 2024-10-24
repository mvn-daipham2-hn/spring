package com.example.spring.service;

import com.example.spring.dao.UserRepository;
import com.example.spring.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    UserRepository userRepository;

//    @PostConstruct
//    public void initData() {
//        System.out.println("__________Reset and init data________________");
//        userRepository.deleteAll();
//        for (int i = 0; i < 100; i++) {
//            Date birthday = new Date();
//            try {
//                birthday = new SimpleDateFormat("dd-MM-yyyy")
//                        .parse("01-01-19" + (i < 10 ? "0" + i : i));
//            } catch (Exception ignored) {
//            }
//            User newUser = new User("user" + i + "@gmail.com", "USER" + i, birthday);
//            userRepository.save(newUser);
//        }
//    }

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Page<User> getUsers(String username, Pageable pageable) {
        if (username != null) {
            return userRepository.findAllByUsername(username, pageable);
        } else {
            return userRepository.findAll(pageable);
        }
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
