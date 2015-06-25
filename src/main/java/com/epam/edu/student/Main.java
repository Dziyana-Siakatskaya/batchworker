package com.epam.edu.student;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;

import com.epam.edu.student.model.Alert;
import com.epam.edu.student.rowmapper.AllertRowMapper;

@ComponentScan("com.epam.edu.student")
@EnableAutoConfiguration
public class Main {
	private static final Logger LOG = Logger.getLogger(Main.class);
	public static void main(String[] args) {
		Main main = new Main();
		main.run(args);
	}

	private void run(String[] args) {

		ApplicationContext ctx = SpringApplication.run(Main.class, args);

		List<Alert> result = ctx.getBean(JdbcTemplate.class).query(
				"select * FROM alerts", new AllertRowMapper());

		for (Alert temp : result) {
			LOG.info("temp: " + temp.getAlertMessage());
		}
		LOG.info("Number of Record:" + result.size());
	}
}