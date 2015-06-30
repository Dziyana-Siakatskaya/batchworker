package com.epam.edu.student.controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class TestController {

	@Autowired
	private Job restTemplateJob;

	@Autowired
	private JobLauncher jobLauncher;

	// private final static String RESPONSE_BODY = "(" + "%s" + ")";

	@RequestMapping(value = "/*", method = RequestMethod.GET)
	public String index() {
		JobParameters params = new JobParametersBuilder().addLong("time",System.currentTimeMillis()).addLong("alertTypeId",(long) 2).addLong("alertCount", (long) 3).toJobParameters();
		try {
			jobLauncher.run(restTemplateJob, params);
		} catch (JobExecutionAlreadyRunningException | JobRestartException
				| JobInstanceAlreadyCompleteException
				| JobParametersInvalidException e) {
			
			e.printStackTrace();
		}

		return "index";
	}

}
