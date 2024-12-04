package com.product_catalog.api.product.controller;

import com.product_catalog.api.config.batch.BatchResponse;
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
    public ResponseEntity<BatchResponse> runBatchManually() {
        BatchResponse response = new BatchResponse();
        response.setJobName(processProduct.getName());

        if (jobExecutionValidator.isJobAlreadyExecuted(processProduct.getName())) {
            response.setMessage("Job já foi executado com sucesso ou está em andamento.");
            response.setStatus("BAD_REQUEST");
            return ResponseEntity.badRequest().body(response);
        }

        try {
            JobParametersBuilder paramsBuilder = new JobParametersBuilder()
                    .addLong("startAt", System.currentTimeMillis());

            jobLauncher.run(processProduct, paramsBuilder.toJobParameters());
            logger.info("Job 'processProduct' executado manualmente.");
            response.setMessage("Job executado manualmente com sucesso.");
            response.setStatus("OK");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Erro ao executar o Job 'processProduct' manualmente:", e);
            response.setMessage("Erro ao executar o Job manualmente.");
            response.setStatus("ERROR");
            return ResponseEntity.status(500).body(response);
        }
    }

}
