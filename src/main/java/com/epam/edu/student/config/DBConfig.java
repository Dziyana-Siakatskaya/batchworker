package com.epam.edu.student.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
public class DBConfig {
	//private static final Logger LOG = Logger.getLogger(DBConfig.class);

	@Autowired
	private Environment env;
	

	@Bean (name = "myDataSource")
	public DataSource dataSource() {
		//what is the differnce?
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		//BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");//env.getProperty("DriverClassName"));
		dataSource.setUrl("jdbc:mysql://localhost:3306/batchsql");//env.getProperty("Url"));
		dataSource.setUsername("root");//env.getProperty("Username"));
		dataSource.setPassword("");//env.getProperty("Password"));
		return dataSource;
	}

	
}
