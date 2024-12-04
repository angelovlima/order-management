package com.product_catalog.api.product.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBatchTest
@SpringBootTest
@AutoConfigureMockMvc
class BatchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job job;

    @Autowired
    private DataSource dataSource;

    @BeforeEach
    void clearBatchTables() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.execute("DELETE FROM BATCH_JOB_EXECUTION_CONTEXT");
        jdbcTemplate.execute("DELETE FROM BATCH_STEP_EXECUTION_CONTEXT");
        jdbcTemplate.execute("DELETE FROM BATCH_STEP_EXECUTION");
        jdbcTemplate.execute("DELETE FROM BATCH_JOB_EXECUTION_PARAMS");
        jdbcTemplate.execute("DELETE FROM BATCH_JOB_EXECUTION");
        jdbcTemplate.execute("DELETE FROM BATCH_JOB_INSTANCE");
    }

    @Test
    void testJobExecution() throws Exception {
        var jobExecution = jobLauncher.run(job, new JobParameters());

        assertThat(jobExecution.getStatus().isUnsuccessful()).isFalse();
        assertThat(jobExecution.getStatus().isRunning()).isFalse();
    }
}
