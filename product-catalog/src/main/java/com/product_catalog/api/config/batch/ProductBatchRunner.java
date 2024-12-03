package com.product_catalog.api.config.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ProductBatchRunner {

    private static final Logger logger = LoggerFactory.getLogger(ProductBatchRunner.class);

    private final JobLauncher jobLauncher;
    private final Job processProduct;
    private final JobExecutionValidator jobExecutionValidator;

    @Autowired
    public ProductBatchRunner(
            JobLauncher jobLauncher,
            Job processProduct,
            JobExecutionValidator jobExecutionValidator) {
        this.jobLauncher = jobLauncher;
        this.processProduct = processProduct;
        this.jobExecutionValidator = jobExecutionValidator;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void runBatchOnStartup() {
        try {
            if (jobExecutionValidator.isJobAlreadyExecuted("processProduct")) {
                logger.info("O Job 'processProduct' já foi executado anteriormente. Ignorando a execução.");
                return;
            }

            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("startAt", System.currentTimeMillis())
                    .toJobParameters();

            jobLauncher.run(processProduct, jobParameters);
            logger.info("Job 'processProduct' executado com sucesso.");
        } catch (Exception e) {
            logger.error("Erro ao executar o Job 'processProduct':", e);
        }
    }
}