package com.product_catalog.api.product.controller;

import com.product_catalog.api.config.batch.JobExecutionValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/batch")
public class BatchController {

    private static final Logger logger = LoggerFactory.getLogger(BatchController.class);

    private final JobLauncher jobLauncher;
    private final Job processProduct;
    private final JobExecutionValidator jobExecutionValidator;

    public BatchController(JobLauncher jobLauncher, Job processProduct, JobExecutionValidator jobExecutionValidator) {
        this.jobLauncher = jobLauncher;
        this.processProduct = processProduct;
        this.jobExecutionValidator = jobExecutionValidator;
    }

    @GetMapping("/run")
    public ResponseEntity<String> runBatchManually() {
        if (jobExecutionValidator.isJobAlreadyExecuted(processProduct.getName())) {
            return ResponseEntity.badRequest()
                    .body("Job 'processProduct' já foi executado com sucesso ou está em andamento.");
        }
        try {
            JobParametersBuilder paramsBuilder = new JobParametersBuilder()
                    .addLong("startAt", System.currentTimeMillis());

            jobLauncher.run(processProduct, paramsBuilder.toJobParameters());
            logger.info("Job 'processProduct' executado manualmente.");
            return ResponseEntity.ok("Job 'processProduct' executado manualmente com sucesso.");
        } catch (Exception e) {
            logger.error("Erro ao executar o Job 'processProduct' manualmente:", e);
            return ResponseEntity.status(500).body("Erro ao executar o Job manualmente.");
        }
    }
}
