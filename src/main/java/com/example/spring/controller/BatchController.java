package com.example.spring.controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/batchs")
public class BatchController {
    private final ApplicationContext applicationContext;
    private final JobLauncher jobLauncher;

    public BatchController(ApplicationContext applicationContext, JobLauncher jobLauncher) {
        this.applicationContext = applicationContext;
        this.jobLauncher = jobLauncher;
    }

    private Job getJob(String jobName) {
        if (applicationContext.containsBean(jobName)) {
            return applicationContext.getBean(jobName, Job.class);
        }
        return null;
    }


    @PostMapping("/startJob/{jobName}")
    public ResponseEntity<String> startJob(@PathVariable String jobName) {
        try {
            Job job = getJob(jobName);
            if (job == null) {
                return new ResponseEntity<>("Job `" + jobName + "` not found", HttpStatus.NOT_FOUND);
            }
            jobLauncher.run(job, new JobParameters());
            return ResponseEntity.ok("Batch job has been started");
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Job execution failed, " + e.getMessage()
                    );
        }
    }
}
