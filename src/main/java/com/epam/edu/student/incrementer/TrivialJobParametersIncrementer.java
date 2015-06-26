package com.epam.edu.student.incrementer;

import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersIncrementer;

public class TrivialJobParametersIncrementer implements
		JobParametersIncrementer {

	public JobParameters getNext(JobParameters parameters) {
		Map<String, JobParameter> map = new HashMap<String, JobParameter>(
				parameters.getParameters());
		map.put("run.count", new JobParameter(parameters
				.getLong("run.count", -1)+1));
		return new JobParameters(map);
	}

}