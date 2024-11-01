package com.example.spring.batch;

import com.example.spring.dto.UserCSVRecordDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.batch.item.file.FlatFileParseException;

public class UserItemReadListener implements ItemReadListener<UserCSVRecordDTO> {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserItemReadListener.class);

    @Override
    public void onReadError(Exception ex) {
        if (ex instanceof FlatFileParseException parseException) {
            String builder = "An error has occurred when reading "
                    + parseException.getLineNumber()
                    + " the line. Here are its details about the bad input\n "
                    + parseException.getInput()
                    + "\n";
            LOGGER.error(builder, parseException);
        } else {
            LOGGER.error("An error occur ", ex);
        }
    }
}
