package com.product_catalog.api.config.batch;

import com.product_catalog.api.config.db.entity.ProductEntity;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class BatchConfiguration {

    private final JobRepository jobRepository;

    @Autowired
    public BatchConfiguration(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    @Bean
    public Job processProduct(JobRepository jobRepository, Step step) {
        return new JobBuilder("processProduct", jobRepository)
                .incrementer(new RunIdIncrementer())
                .preventRestart()
                .start(step)
                .build();
    }

    @Bean
    public Step step(JobRepository jobRepository,
                     PlatformTransactionManager transactionManager,
                     ItemReader<ProductEntity> productItemReader,
                     ItemProcessor<ProductEntity, ProductEntity> productItemProcessor,
                     ItemWriter<ProductEntity> productItemWriter
    ) {
        return new StepBuilder("step", jobRepository)
                .<ProductEntity, ProductEntity>chunk(64, transactionManager)
                .reader(productItemReader)
                .processor(productItemProcessor)
                .writer(productItemWriter)
                .taskExecutor(new SimpleAsyncTaskExecutor())
                .build();
    }

    @Bean
    public ItemReader<ProductEntity> productItemReader() {
        BeanWrapperFieldSetMapper<ProductEntity> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(ProductEntity.class);

        return new FlatFileItemReaderBuilder<ProductEntity>()
                .name("productItemReader")
                .resource(new ClassPathResource("product.csv"))
                .linesToSkip(1) // Ignora o cabeçalho
                .delimited()
                .names("name", "price", "stock_quantity")
                .fieldSetMapper(fieldSetMapper)
                .build();
    }

    @Bean
    public ItemWriter<ProductEntity> productItemWriter(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<ProductEntity>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .dataSource(dataSource)
                .sql(" INSERT INTO product " +
                        "(name, price, stock_quantity, create_date_time) " +
                        "VALUES (:name, :price, :stockQuantity, :createDateTime)")
                .build();
    }

    @Bean
    public ItemProcessor<ProductEntity, ProductEntity> productItemProcessor() {
        return new ProductProcessor();
    }
}
