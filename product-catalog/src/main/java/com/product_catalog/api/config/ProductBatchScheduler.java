package com.product_catalog.api.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ProductBatchScheduler {

    private static final Logger logger = LoggerFactory.getLogger(ProductBatchScheduler.class);

    private final JobLauncher jobLauncher;
    private final Job processProduct;
    private final JobExecutionValidator jobExecutionValidator;

    @Autowired
    public ProductBatchScheduler(
            JobLauncher jobLauncher,
            Job processProduct,
            JobExecutionValidator jobExecutionValidator) {
        this.jobLauncher = jobLauncher;
        this.processProduct = processProduct;
        this.jobExecutionValidator = jobExecutionValidator;
    }

    @Scheduled(cron = "${batch.product.cron}")
    public void scheduleBatchJob() {
        if (jobExecutionValidator.isJobAlreadyExecuted(processProduct.getName())) {
            logger.error("Job 'processProduct' já foi executado com sucesso ou está em andamento. Ignorando execução");
            return;
        }
        try {
            JobParametersBuilder paramsBuilder = new JobParametersBuilder()
                    .addLong("startAt", System.currentTimeMillis());

            jobLauncher.run(processProduct, paramsBuilder.toJobParameters());
            logger.info("Job 'processProduct' executado automaticamente pelo agendador.");
        } catch (Exception e) {
            logger.error("Erro ao executar o Job 'processProduct' automaticamente:", e);
        }
    }
}