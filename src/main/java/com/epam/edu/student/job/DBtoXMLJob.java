package com.epam.edu.student.job;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import com.epam.edu.student.job.processor.AlertsItemProcessor;
import com.epam.edu.student.job.rowmapper.AlertRowMapper;
import com.epam.edu.student.model.Alerts;

@Configuration
public class DBtoXMLJob {
	// private static final Logger LOG = Logger.getLogger(DBtoXMLJob.class);

	@Autowired
	DataSource myDataSource;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Bean
	public ItemReader<Alerts> reader() {
		JdbcCursorItemReader<Alerts> reader = new JdbcCursorItemReader<Alerts>();
		reader.setDataSource(myDataSource);
		reader.setSql("SELECT * FROM alerts");
		reader.setRowMapper(new AlertRowMapper());
		return reader;
	}

	@Bean(name = "getAlertOnTypeRead")
	@StepScope
	public ItemReader<Alerts> getAlertOnTypeRead(
			@Value("#{jobParameters[alertTypeId]}") long alertTypeId,
			@Value("#{jobParameters[alertCount]}") long alertCount)
			throws Exception {

		MySqlPagingQueryProvider provider = new MySqlPagingQueryProvider();
		provider.setSelectClause("SELECT * ");
		provider.setFromClause("FROM alerts ");
		provider.setWhereClause("alertTypeId = :alertTypeId AND alertPosted = 0");
		Map<String, Order> sortKeys = new LinkedHashMap<String, Order>();
		sortKeys.put("id", Order.ASCENDING);
		provider.setSortKeys(sortKeys);

		JdbcPagingItemReader<Alerts> reader = new JdbcPagingItemReader<Alerts>();
		reader.setDataSource(myDataSource);
		reader.setRowMapper(new AlertRowMapper());
		reader.setQueryProvider(provider);
		reader.setMaxItemCount((int) alertCount);

		Map<String, Object> parameterValues = new HashMap<String, Object>();
		parameterValues.put("alertTypeId", alertTypeId);
		reader.setParameterValues(parameterValues);
		reader.afterPropertiesSet();

		return reader;
	}

	@Bean
	public ItemWriter<Alerts> jsonWriter() {
		StaxEventItemWriter<Alerts> writer = new StaxEventItemWriter<Alerts>();
		writer.setResource(new FileSystemResource("xml/alerts.xml"));
		writer.setRootTagName("alerts");
		writer.setMarshaller(myMarshaller());

		// FlatFileItemWriter<Alerts> writer = new FlatFileItemWriter<Alerts>();
		// writer.setResource(new FileSystemResource("xml/alert.csv"));
		// writer.setShouldDeleteIfExists(true);
		//
		// BeanWrapperFieldExtractor<Alerts> fieldExtractor = new
		// BeanWrapperFieldExtractor<Alerts>();
		// fieldExtractor.setNames(new String[] { "id", "alertTypeId",
		// "alertMessage", "alertPosted" });
		// DelimitedLineAggregator<Alerts> delLineAgg = new
		// DelimitedLineAggregator<Alerts>();
		// delLineAgg.setDelimiter("-");
		// delLineAgg.setFieldExtractor(fieldExtractor);
		//
		// writer.setLineSeparator(",");
		// writer.setLineAggregator(delLineAgg);

		return writer;
	}
	
	@Bean
	public ItemWriter<Alerts> writer() {
		StaxEventItemWriter<Alerts> writer = new StaxEventItemWriter<Alerts>();
		writer.setResource(new FileSystemResource("xml/alerts.xml"));
		writer.setRootTagName("alerts");
		writer.setMarshaller(myMarshaller());

		// FlatFileItemWriter<Alerts> writer = new FlatFileItemWriter<Alerts>();
		// writer.setResource(new FileSystemResource("xml/alert.csv"));
		// writer.setShouldDeleteIfExists(true);
		//
		// BeanWrapperFieldExtractor<Alerts> fieldExtractor = new
		// BeanWrapperFieldExtractor<Alerts>();
		// fieldExtractor.setNames(new String[] { "id", "alertTypeId",
		// "alertMessage", "alertPosted" });
		// DelimitedLineAggregator<Alerts> delLineAgg = new
		// DelimitedLineAggregator<Alerts>();
		// delLineAgg.setDelimiter("-");
		// delLineAgg.setFieldExtractor(fieldExtractor);
		//
		// writer.setLineSeparator(",");
		// writer.setLineAggregator(delLineAgg);

		return writer;
	}

	@Bean
	public Marshaller myMarshaller() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setClassesToBeBound(Alerts.class);
		return marshaller;
	}

	@Bean
	public ItemProcessor<Alerts, Alerts> processor() {
		return new AlertsItemProcessor();
	}

	@Bean(name = "testRead")
	public Job testRead(JobBuilderFactory jobs, Step step) {
		return jobs.get("testRead").flow(step()).end().build();
	}

	@Bean
	public Step step() {
		return stepBuilderFactory.get("step").<Alerts, Alerts> chunk(5)
				.reader(reader()).processor(processor()).writer(writer())
				.faultTolerant().build();
	}

	@Bean(name = "getAlertOnType")
	public Job getAlertOnType(JobBuilderFactory jobs, Step getAlertOnTypeStep)
			throws Exception {
		return jobs.get("getAlertOnType").flow(getAlertOnTypeStep).end()
				.build();
	}

	@Bean
	public Step getAlertOnTypeStep(ItemReader<Alerts> getAlertOnTypeRead, ItemWriter<? super Alerts> jsonWriter)
			throws Exception {
		return stepBuilderFactory.get("getAlertOnTypeStep")
				.<Alerts, Alerts> chunk(5).reader(getAlertOnTypeRead)
				.processor(processor()).writer(jsonWriter).faultTolerant()
				.build();
	}

}
