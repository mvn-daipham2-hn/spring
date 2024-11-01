package com.example.spring.config;

import com.example.spring.batch.JobCompletionNotificationListener;
import com.example.spring.batch.UserItemProcessor;
import com.example.spring.batch.UserItemReadListener;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.task.VirtualThreadTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/*
Spring Batch organizes the batch processing flow through several primary components:

    Job: A Job is the entire batch process, consisting of one or more Steps. A job instance is created for each unique job execution.

    Step: Each Job is broken into Steps. A step is a phase of the batch job, such as reading data, processing it, or writing results. Each step operates independently.

    ItemReader: Responsible for reading data from a source (like a database, file, or web service) into the batch process.

    ItemProcessor: This optional component is responsible for processing and transforming data. For instance, it may filter out invalid data or convert data formats.

    ItemWriter: Writes processed data to a destination, such as a database, file, or message queue.

    JobRepository: Handles the storage of job-related metadata, such as job and step executions. This information is often used to restart jobs or analyze job history.

    JobLauncher: Initiates job execution, usually managed by Spring's scheduling support or triggered through REST endpoints.
 */

@Configuration
public class BatchConfiguration {
    @Value("${file.input}")
    private String fileInput;
    @Value("${spring.batch.job.name}")
    private String jobName;


    @Bean
    public FlatFileItemReader<UserCSVRecordDTO> reader() {
        BeanWrapperFieldSetMapper<UserCSVRecordDTO> fieldSetMapper = new BeanWrapperFieldSetMapper<>() {{
            setTargetType(UserCSVRecordDTO.class);
        }};
        String[] nameOfFields = new String[]{"id", "username", "email", "birthday"};
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

    @Bean
    public JdbcBatchItemWriter<User> writer(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<User>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO `user`(`id`, `username`, `email`, `birthday`) VALUES (:id,:username,:email,:birthday)")
                .dataSource(dataSource)
                .build();
    }

    @Bean
    public Job importUserJob(
            JobRepository jobRepository,
            JobCompletionNotificationListener listener,
            Step importUserStep
    ) {
        return new JobBuilder(jobName, jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(importUserStep)
                .end()
                .build();
    }

    @Bean
    public UserItemProcessor processor() {
        return new UserItemProcessor();
    }

    @Bean
    public Step importUserStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            JdbcBatchItemWriter<User> writer,
            VirtualThreadTaskExecutor taskExecutor
    ) {
        return new StepBuilder("importUserStep", jobRepository)
                .<UserCSVRecordDTO, User>chunk(10, transactionManager)
                .reader(reader())
                .processor(processor())
                .writer(writer)
                .listener(new UserItemReadListener())
                .taskExecutor(taskExecutor)
                .build();
    }


    @Bean
    public VirtualThreadTaskExecutor taskExecutor() {
        return new VirtualThreadTaskExecutor("virtual-thread-executor");
    }
}
