package com.epam.edu.student.config;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import com.epam.edu.student.model.Alert;
import com.epam.edu.student.processor.AlertItemProcessor;
import com.epam.edu.student.rowmapper.AllertRowMapper;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

	@Bean
	public JdbcTemplate jdbcTemplate(DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}

	@Bean
	public DataSource getDataSource() {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		dataSource.setUrl("jdbc:mysql://localhost:3306/test");
		dataSource.setUsername("root");
		dataSource.setPassword("");
		return dataSource;
	}

	@Bean
	public ItemReader<Alert> reader() {
		JdbcCursorItemReader<Alert> reader = new JdbcCursorItemReader<Alert>();
		reader.setDataSource(getDataSource());
		// reader.setItemSqlParameterSourceProvider(new
		// BeanPropertyItemSqlParameterSourceProvider<Marksheet>());
		reader.setSql("SELECT * FROM alerts");

		reader.setRowMapper(new AllertRowMapper());

		return reader;
	}

	@Bean
	public ItemWriter<Alert> writer() {
		StaxEventItemWriter<Alert> writer = new StaxEventItemWriter<Alert>();
		writer.setResource(new ClassPathResource("alert.xml"));
		writer.setRootTagName("alert");
		writer.setMarshaller(myMarshaller());
		return writer;
	}

	@SuppressWarnings("rawtypes")
	@Bean
	public Jaxb2Marshaller myMarshaller() {
		// XStreamMarshaller myMarshaller = new XStreamMarshaller();
		Jaxb2Marshaller myMarshaller = new Jaxb2Marshaller();
		Class[] supClasses = new Class[1];
		supClasses[0] = Alert.class;
		myMarshaller.setClassesToBeBound(supClasses);
		return myMarshaller;
	}

	@Bean
	public ItemProcessor<Alert, Alert> processor() {
		return new AlertItemProcessor();
	}

	@Bean
	public Job testJob(JobBuilderFactory jobs, Step step) {
		return jobs.get("testJob").start(step()).build();
	}
	
	@Autowired
	private StepBuilderFactory stepBuilders;
	
	@Bean
	public Step step(){
		return stepBuilders.get("step")
				.<Alert,Alert>chunk(1)
				.reader(reader())
				.processor(processor())
				.writer(writer())
				.faultTolerant()
				.build();
	}

	@Bean
	public Step step(StepBuilderFactory stepBuilderFactory,
			ItemReader<Alert> reader, ItemWriter<Alert> writer,
			ItemProcessor<Alert, Alert> processor) {
		return stepBuilderFactory.get("step").<Alert, Alert> chunk(5)
				.reader(reader).processor(processor).writer(writer).build();
	}


	
}
