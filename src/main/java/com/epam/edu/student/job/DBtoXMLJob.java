package com.epam.edu.student.job;

import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.batch.core.ItemProcessListener;
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
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import com.epam.edu.student.model.Alerts;
import com.epam.edu.student.processor.AlertsItemProcessor;
import com.epam.edu.student.rowmapper.AlertRowMapper;

public class DBtoXMLJob {
	private static final Logger LOG = Logger.getLogger(DBtoXMLJob.class);

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
		/*
		 * MySqlPagingQueryProvider provider = new MySqlPagingQueryProvider();
		 * provider
		 * .setSelectClause("SELECT alertMessage, alertPosted, id, alertPosted"
		 * ); provider.setFromClause("FROM alert"); Map<String, Order> sortKeys
		 * = new LinkedHashMap<String, Order>(); sortKeys.put("id",
		 * Order.ASCENDING); provider.setSortKeys(sortKeys);
		 * 
		 * JdbcPagingItemReader<Alerts> reader = new
		 * JdbcPagingItemReader<Alerts>(); reader.setDataSource(dataSource());
		 * reader.setRowMapper(new AllertRowMapper());
		 * reader.setQueryProvider(provider);
		 */

		return reader;
	}

	@Bean
	public ItemWriter<Alerts> writer() {
		StaxEventItemWriter<Alerts> writer = new StaxEventItemWriter<Alerts>();
		writer.setResource(new ClassPathResource("xml/alerts.xml"));
		writer.setForceSync(true);
		writer.setShouldDeleteIfEmpty(true);
		writer.setOverwriteOutput(true);
		writer.setVersion("1.0");
		writer.setEncoding("UTF-8");
		writer.setRootTagName("record");
		writer.setMarshaller(myMarshaller());

//		 FlatFileItemWriter<Alerts> writer = new FlatFileItemWriter<Alerts>();
//		 writer.setResource(new ClassPathResource("xml/alert.txt"));
//		 writer.setShouldDeleteIfExists(true);
//		
//		 BeanWrapperFieldExtractor<Alerts> fieldExtractor = new
//		 BeanWrapperFieldExtractor<Alerts>();
//		 fieldExtractor.setNames(new String[] { "id", "alertTypeId",
//		 "alertMessage", "alertPosted" });
//		 DelimitedLineAggregator<Alerts> delLineAgg = new
//		 DelimitedLineAggregator<Alerts>();
//		 delLineAgg.setDelimiter("-");
//		 delLineAgg.setFieldExtractor(fieldExtractor);

		// writer.setLineSeparator("|");
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
		return jobs.get("testRead")
		// .incrementer(new TrivialJobParametersIncrementer())
				.flow(step()).end().build();

		// What is the difference with //.start(step)
	}

	/*
	 * @Bean public Step step() { LOG.info("Here3"); return
	 * stepBuilders.get("step").<Alerts, Alerts> chunk(1)
	 * .reader(reader()).processor(processor()).writer(writer())
	 * .faultTolerant().build(); }
	 */

	@Bean
	public Step step() {
		return stepBuilderFactory.get("step").<Alerts, Alerts> chunk(1)
				.reader(reader()).processor(processor()).writer(writer())
				.listener(new ItemReadListener<Alerts>() {

					@Override
					public void beforeRead() {
					}

					@Override
					public void afterRead(Alerts item) {
					}

					@Override
					public void onReadError(Exception ex) {
						LOG.error("onReadError " + ex);

					}
				}).listener(new ItemWriteListener<Alerts>() {
					@Override
					public void beforeWrite(List<? extends Alerts> items) {
						LOG.error("beforeWrite");
						for (Alerts temp : items) {
							LOG.debug("\t " + temp.getAlertMessage());
						}
					}

					@Override
					public void afterWrite(List<? extends Alerts> items) {
						LOG.debug("afterWrite");
						for (Alerts temp : items) {
							LOG.error("\t\t " + temp.getAlertMessage());
						}
					}

					@Override
					public void onWriteError(Exception exception,
							List<? extends Alerts> items) {
						LOG.error("onWriteError " + exception);
						for (Alerts temp : items) {
							LOG.debug("\t " + temp.getAlertMessage());
						}
					}

				})
				.listener(new ItemProcessListener<Alerts, Alerts>() {

					@Override
					public void beforeProcess(Alerts item) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void afterProcess(Alerts item, Alerts result) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onProcessError(Alerts item, Exception e) {
						LOG.error("!!!!!!!!!!!!!! onProcessError " + item  + " " + e);
						
					}
				})
				.faultTolerant()
				.build();
	}
	
}
