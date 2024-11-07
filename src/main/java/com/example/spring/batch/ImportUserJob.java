package com.example.spring.batch;

import com.example.spring.dto.UserCSVRecordDTO;
import com.example.spring.model.User;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.task.VirtualThreadTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class ImportUserJob {
    public FlatFileItemReader<UserCSVRecordDTO> reader() {
        BeanWrapperFieldSetMapper<UserCSVRecordDTO> fieldSetMapper = new BeanWrapperFieldSetMapper<>() {{
            setTargetType(UserCSVRecordDTO.class);
        }};
        String[] nameOfFields = new String[]{"id", "username", "email", "birthday"};
        String fileInput = "flat_large_user.csv";
        return new FlatFileItemReaderBuilder<UserCSVRecordDTO>()
                .name("userItemReader")
                .resource(new ClassPathResource(fileInput))
                // Configures the reader to parse the file as a delimited file (e.g., CSV).
                // By default, this method assumes that the delimiter is a comma (,),
                // but you can specify a different delimiter if needed (e.g., .delimiter("|"))
                .delimited()
                .names(nameOfFields)
                .fieldSetMapper(fieldSetMapper)
                .build();
    }

    public UserItemProcessor processor() {
        return new UserItemProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<User> importUserWriter(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<User>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO `user`(`id`, `username`, `email`, `birthday`) VALUES (:id,:username,:email,:birthday)")
                .dataSource(dataSource)
                .build();
    }

    @Bean
    public Step importUserStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            JdbcBatchItemWriter<User> importUserWriter,
            VirtualThreadTaskExecutor taskExecutor
    ) {
        String stepName = "importUserStep";
        return new StepBuilder(stepName, jobRepository)
                .<UserCSVRecordDTO, User>chunk(10, transactionManager)
                .reader(reader())
                .processor(processor())
                .writer(importUserWriter)
                .listener(new UserItemReadListener())
                .taskExecutor(taskExecutor)
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    public Job importUser(
            JobRepository jobRepository,
            JobCompletionNotificationListener listener,
            Step importUserStep
    ) {
        return new JobBuilder("importUser", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(importUserStep)
                .end()
                .build();
    }
}
