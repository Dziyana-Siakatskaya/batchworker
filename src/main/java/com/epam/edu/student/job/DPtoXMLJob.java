package com.epam.edu.student.job;

import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import com.epam.edu.student.model.Alert;
import com.epam.edu.student.processor.AlertItemProcessor;
import com.epam.edu.student.rowmapper.AllertRowMapper;

public class DPtoXMLJob {
	private static final Logger LOG = Logger.getLogger(DPtoXMLJob.class);

	@Autowired
	DataSource myDataSource;

	@Bean
	public ItemReader<Alert> reader() {
		JdbcCursorItemReader<Alert> reader = new JdbcCursorItemReader<Alert>();
		reader.setDataSource(myDataSource);
		reader.setSql("SELECT * FROM alerts");
		reader.setRowMapper(new AllertRowMapper());
		/*
		 * MySqlPagingQueryProvider provider = new MySqlPagingQueryProvider();
		 * provider
		 * .setSelectClause("SELECT alertMessage, alertPosted, id, alertPosted"
		 * ); provider.setFromClause("FROM alert"); Map<String, Order> sortKeys
		 * = new LinkedHashMap<String, Order>(); sortKeys.put("id",
		 * Order.ASCENDING); provider.setSortKeys(sortKeys);
		 * 
		 * JdbcPagingItemReader<Alert> reader = new
		 * JdbcPagingItemReader<Alert>(); reader.setDataSource(dataSource());
		 * reader.setRowMapper(new AllertRowMapper());
		 * reader.setQueryProvider(provider);
		 */

		return reader;
	}

	@Bean
	public ItemWriter<Alert> writer() {
		// StaxEventItemWriter<Alert> writer = new StaxEventItemWriter<Alert>();
		// writer.setResource(new ClassPathResource("alert.xml"));
		// writer.setRootTagName("alert");
		// writer.setMarshaller(myMarshaller());

		FlatFileItemWriter<Alert> writer = new FlatFileItemWriter<Alert>();
		writer.setResource(new ClassPathResource("alert.csv"));
		writer.setShouldDeleteIfExists(true);

		BeanWrapperFieldExtractor<Alert> fieldExtractor = new BeanWrapperFieldExtractor<Alert>();
		fieldExtractor.setNames(new String[] { "id", "alertTypeId",
				"alertMessage", "alertPosted" });
		DelimitedLineAggregator<Alert> delLineAgg = new DelimitedLineAggregator<Alert>();
		delLineAgg.setDelimiter("-");
		delLineAgg.setFieldExtractor(fieldExtractor);

		writer.setLineSeparator("|");
		writer.setLineAggregator(delLineAgg);

		return writer;
	}

	@Bean
	public Marshaller myMarshaller() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setClassesToBeBound(Alert.class);
		return marshaller;
	}

	@Bean
	public ItemProcessor<Alert, Alert> processor() {
		return new AlertItemProcessor();
	}

	@Bean(name = "testRead")
	public Job testRead(JobBuilderFactory jobs, Step step) {
		return jobs.get("testRead")
		// .incrementer(new TrivialJobParametersIncrementer())
				.flow(step).end().build();

		// What is the difference with //.start(step)
	}

	/*
	 * @Bean public Step step() { LOG.info("Here3"); return
	 * stepBuilders.get("step").<Alert, Alert> chunk(1)
	 * .reader(reader()).processor(processor()).writer(writer())
	 * .faultTolerant().build(); }
	 */

	@Bean
	public Step step(StepBuilderFactory stepBuilderFactory,
			ItemReader<Alert> reader, ItemWriter<Alert> writer,
			ItemProcessor<Alert, Alert> processor) {
		return stepBuilderFactory.get("step").<Alert, Alert> chunk(5)
				.reader(reader).processor(processor).writer(writer)
				.listener(new ItemReadListener<Alert>() {

					@Override
					public void beforeRead() {
					}

					@Override
					public void afterRead(Alert item) {
					}

					@Override
					public void onReadError(Exception ex) {
						LOG.error("onReadError " + ex);

					}
				}).listener(new ItemWriteListener<Alert>() {
					@Override
					public void beforeWrite(List<? extends Alert> items) {
						LOG.error("beforeWrite");
						for (Alert temp : items) {
							LOG.debug("\t " + temp.getAlertMessage());
						}
					}

					@Override
					public void afterWrite(List<? extends Alert> items) {
						LOG.debug("afterWrite");
						for (Alert temp : items) {
							LOG.error("\t\t " + temp.getAlertMessage());
						}
					}

					@Override
					public void onWriteError(Exception exception,
							List<? extends Alert> items) {
						LOG.error("onWriteError " + exception);
						for (Alert temp : items) {
							LOG.debug("\t " + temp.getAlertMessage());
						}
					}

				}).build();
	}
}
