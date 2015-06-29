package com.epam.edu.student.config;

import javax.sql.DataSource;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.MapJobRepositoryFactoryBean;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.epam.edu.student.job.DBtoXMLJob;

@Configuration
@EnableBatchProcessing
@EnableTransactionManagement
@Import(DBtoXMLJob.class)
@ComponentScan("com.epam.edu.student")
public class BatchConfigurer {
	// private static final Logger LOG =
	// Logger.getLogger(BatchConfigurer.class);

	@Autowired
	DataSource myDataSource;

	public PlatformTransactionManager getTransactionManager() throws Exception {
		return new DataSourceTransactionManager(myDataSource);
	}

	// @Autowired
	// private JobBuilderFactory jobBuilders;
	//
	// @Autowired
	// private StepBuilderFactory stepBuilders;

	@Bean
	public ResourcelessTransactionManager transactionManager() {
		return new ResourcelessTransactionManager();
	}

	@Bean
	public JobRepository jobRepository() throws Exception {
		return new MapJobRepositoryFactoryBean(transactionManager())
				.getObject();
	}

	@Bean
	public JobLauncher jobLauncher() throws Exception {
		SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
		jobLauncher.setJobRepository(jobRepository());
		return jobLauncher;
	}

	// Not necessary
	 @Bean
	 public JdbcTemplate jdbcTemplate(DataSource dataSource) {
	 return new JdbcTemplate(dataSource);
	 }

}
