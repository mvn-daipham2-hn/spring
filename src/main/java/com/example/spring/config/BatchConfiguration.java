package com.example.spring.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.VirtualThreadTaskExecutor;

import java.util.Arrays;

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
    private final ApplicationContext applicationContext;
    private final JobLauncher jobLauncher;

    public BatchConfiguration(ApplicationContext applicationContext, JobLauncher jobLauncher) {
        this.applicationContext = applicationContext;
        this.jobLauncher = jobLauncher;
    }

    @Bean
    public VirtualThreadTaskExecutor taskExecutor() {
        return new VirtualThreadTaskExecutor("virtual-thread-executor");
    }

    ///  {@code java -jar <path-to-jar-file> --spring.batch.job.name=<job-name-to-execute>}
    ///
    ///  {@code java -jar <path-to-jar-file> --spring.batch.job.names=<job-name-to-execute>}
    ///
    ///  {@code java -jar <path-to-jar-file> --batch-job=<job-name-to-execute>}
    @Bean
    public CommandLineRunner runBatchJob() {
        return args -> {
            String batchJobArg = Arrays
                    .stream(args)
                    .filter(arg ->
                            arg.startsWith("--spring.batch.job.names")
                                    || arg.startsWith("--spring.batch.job.name")
                                    || arg.startsWith("--batch-job")
                    )
                    .findFirst()
                    .orElse(null);
            if (batchJobArg != null) {
                String[] batchJobArgs = batchJobArg.split("=");
                if (batchJobArgs.length == 2) {
                    Job job = getJob(batchJobArgs[1]);
                    if (job != null) {
                        runJob(job);
                    } else {
                        System.out.println("Batch job argument not found: " + batchJobArgs[1]);
                    }
                } else {
                    System.out.println("Batch job argument is not valid");
                }
            }
        };
    }

    private void runJob(Job job) {
        try {
            System.out.println("Running job " + job.getName());
            jobLauncher.run(job, new JobParameters());
        } catch (Exception e) {
            System.out.println("Error running job: " + e.getMessage());
        }
    }

    private Job getJob(String jobName) {
        if (applicationContext.containsBean(jobName)) {
            return applicationContext.getBean(jobName, Job.class);
        }
        return null;
    }
}
