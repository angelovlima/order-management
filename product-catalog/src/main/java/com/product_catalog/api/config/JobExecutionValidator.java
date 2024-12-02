package com.product_catalog.api.config;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JobExecutionValidator {

    private final JobExplorer jobExplorer;

    @Autowired
    public JobExecutionValidator(JobExplorer jobExplorer) {
        this.jobExplorer = jobExplorer;
    }

    public boolean isJobAlreadyExecuted(String jobName) {
        return jobExplorer.findJobInstancesByJobName(jobName, 0, 1).stream()
                .flatMap(jobInstance -> jobExplorer.getJobExecutions(jobInstance).stream())
                .anyMatch(jobExecution -> jobExecution.getStatus() == BatchStatus.COMPLETED);
    }

}

