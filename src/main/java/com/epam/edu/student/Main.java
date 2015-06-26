package com.epam.edu.student;

import org.apache.log4j.Logger;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.epam.edu.student.config.BatchConfigurer;

public class Main {
	private static final AnnotationConfigApplicationContext ANNOTATION_CONFIG_APPLICATION_CONTEXT = new AnnotationConfigApplicationContext(
			BatchConfigurer.class);
	private static final Logger LOG = Logger.getLogger(Main.class);

	public static void main(String[] args) {
		Main main = new Main();
		try {
			main.run2(args);
		} catch (NoSuchJobException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * private void run(String[] args) { ApplicationContext ctx = new
	 * AnnotationConfigApplicationContext(BatchConfigurer.class); List<Alert>
	 * result = ctx.getBean(JdbcTemplate.class).query( "select * FROM alerts",
	 * new AllertRowMapper()); for (Alert temp : result) { LOG.info("temp: " +
	 * temp.getAlertMessage()); } LOG.info("Number of Record:" + result.size());
	 * }
	 */

	private void run2(String[] args) throws NoSuchJobException {

		JobLauncher jobLauncher = (JobLauncher) ANNOTATION_CONFIG_APPLICATION_CONTEXT
				.getBean("jobLauncher");
		Job testRead = (Job) ANNOTATION_CONFIG_APPLICATION_CONTEXT
				.getBean("testRead");

		try {
			// JobParameters param = new
			// JobParametersBuilder().toJobParameters();
			JobExecution execution = jobLauncher.run(testRead,
					new JobParameters());
			execution.setVersion(2);
			LOG.info("Exit Status : " + execution.getStatus());
			LOG.info("Exit Status : " + execution.getAllFailureExceptions());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
