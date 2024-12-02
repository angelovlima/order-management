package com.product_catalog.api.config;

import com.product_catalog.api.model.entity.Product;
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
                     ItemReader<Product> productItemReader,
                     ItemProcessor<Product, Product> productItemProcessor,
                     ItemWriter<Product> productItemWriter
    ) {
        return new StepBuilder("step", jobRepository)
                .<Product, Product>chunk(64, transactionManager)
                .reader(productItemReader)
                .processor(productItemProcessor)
                .writer(productItemWriter)
                .taskExecutor(new SimpleAsyncTaskExecutor())
                .build();
    }

    @Bean
    public ItemReader<Product> productItemReader() {
        BeanWrapperFieldSetMapper<Product> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(Product.class);

        return new FlatFileItemReaderBuilder<Product>()
                .name("productItemReader")
                .resource(new ClassPathResource("product.csv"))
                .linesToSkip(1) // Ignora o cabeçalho
                .delimited()
                .names("name", "price", "stock_quantity")
                .fieldSetMapper(fieldSetMapper)
                .build();
    }

    @Bean
    public ItemWriter<Product> productItemWriter(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Product>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .dataSource(dataSource)
                .sql(" INSERT INTO product " +
                        "(name, price, stock_quantity, create_date_time) " +
                        "VALUES (:name, :price, :stockQuantity, :createDateTime)")
                .build();
    }

    @Bean
    public ItemProcessor<Product, Product> productItemProcessor() {
        return new ProductProcessor();
    }
}
