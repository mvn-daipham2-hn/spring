package com.example.spring.helper;

import com.example.spring.dto.UserCSVRecordDTO;
import com.example.spring.dto.UserDTO;
import com.example.spring.model.User;
import jakarta.validation.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CSVHelper {
    public static String TYPE = "text/csv";

    public static boolean hasCSVFormat(MultipartFile file) {
        return TYPE.equals(file.getContentType());
    }

    private static void validateUserRecord(Validator validator, UserCSVRecordDTO userRecordDTO) {
        Set<ConstraintViolation<UserDTO>> violations = validator.validate(userRecordDTO);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    public static List<User> csvToUsers(InputStream inputStream) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            CSVFormat csvFormat = CSVFormat.DEFAULT
                    .builder()
                    .setHeader()
                    .setSkipHeaderRecord(true)
                    .setIgnoreHeaderCase(true)
                    .setTrim(true)
                    .build();
            CSVParser csvParser = new CSVParser(fileReader, csvFormat);
            List<User> users = new ArrayList<>();
            List<CSVRecord> records = csvParser.getRecords();
            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            Validator validator = factory.getValidator();
            for (CSVRecord record : records) {
                UserCSVRecordDTO userRecordDTO = new UserCSVRecordDTO(
                        record.get("id"),
                        record.get("username"),
                        record.get("email"),
                        record.get("birthday")
                );
                validateUserRecord(validator, userRecordDTO);
                users.add(userRecordDTO.toUser());
            }
            return users;
        } catch (IOException e) {
            throw new RuntimeException("Fail to parse CSV file:", e);
        }
    }
}
