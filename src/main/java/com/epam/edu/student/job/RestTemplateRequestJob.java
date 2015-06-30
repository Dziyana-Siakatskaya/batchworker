package com.epam.edu.student.job;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import RestTemplateReader.RestTemplateWriter;

import com.epam.edu.student.job.processor.RestTemplateProcessor;
import com.epam.edu.student.job.reader.RestTemplateReader;
import com.epam.edu.student.model.Alerts;

@Configuration
public class RestTemplateRequestJob {
	private static final Logger LOG = Logger.getLogger(DBtoXMLJob.class);

	@Autowired
	DataSource myDataSource;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Bean(name = "restTemplateRead")
	@StepScope
	public ItemReader<Alerts> restTemplateRead(
			@Value("#{jobParameters[alertTypeId]}") long alertTypeId,
			@Value("#{jobParameters[alertCount]}") long alertCount)
			throws Exception {
		return new RestTemplateReader(alertTypeId, alertCount);
	}

	@Bean(name = "restTemplateWriter")
	public ItemWriter<Alerts> writer() {
		return new RestTemplateWriter();
	}

	/*@Bean
	public Marshaller myMarshaller() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setClassesToBeBound(Alerts.class);
		return marshaller;
	}*/

	@Bean(name = "restTemplateProcessor")
	public ItemProcessor<Alerts, Alerts> processor() {
		return new RestTemplateProcessor();
	}
	
	@Bean(name = "restTemplateJob")
	public Job getAlertOnType(JobBuilderFactory jobs, Step restTemplateStep)
			throws Exception {
		return jobs.get("restTemplateJob").flow(restTemplateStep).end()
				.build();
	}

	@Bean
	public Step restTemplateStep(ItemReader<Alerts> restTemplateRead, ItemWriter<? super Alerts> restTemplateWriter, ItemProcessor<? super Alerts, ? extends Alerts> restTemplateProcessor)
			throws Exception {
		return stepBuilderFactory.get("restTemplateStep")
				.<Alerts, Alerts> chunk(1).reader(restTemplateRead)
				.processor(restTemplateProcessor).writer(restTemplateWriter).faultTolerant()
				.build();
	}

}
