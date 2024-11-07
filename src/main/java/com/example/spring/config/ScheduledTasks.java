package com.example.spring.config;

import com.example.spring.model.User;
import com.example.spring.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
@EnableScheduling
public class ScheduledTasks {
    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);
    private final UserService userService;

    public ScheduledTasks(UserService userService) {
        this.userService = userService;
    }

    @Scheduled(cron = "0 0 6 * * *")
    public void logUsersWithBirthdayToday() {
        Date today = new Date();
        List<User> users = userService.getUsersByBirthday(today);
        log.info("----- Here is the user list has a birthday today -----");
        users.forEach(user -> log.info(user.toString()));
    }
}
