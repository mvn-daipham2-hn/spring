package com.example.spring.batch;

import com.example.spring.dto.UserCSVRecordDTO;
import com.example.spring.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class UserItemProcessor implements ItemProcessor<UserCSVRecordDTO, User> {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserItemProcessor.class);

    @Override
    public User process(UserCSVRecordDTO userCSVRecord) {
        User user = userCSVRecord.toUser();
        LOGGER.info("Converting ( {} ) into ( {} )", userCSVRecord, user);
        return user;
    }
}
