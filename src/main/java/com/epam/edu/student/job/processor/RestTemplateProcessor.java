package com.epam.edu.student.job.processor;


import org.apache.log4j.Logger;
import org.springframework.batch.item.ItemProcessor;

import com.epam.edu.student.model.Alerts;

public class RestTemplateProcessor implements ItemProcessor<Alerts, Alerts> {
	private static final Logger LOG = Logger.getLogger(RestTemplateProcessor.class);
	@Override
	public Alerts process(Alerts item) throws Exception {
		LOG.debug("Alerts process ");
		Alerts alert = new Alerts();
		alert.setId(item.getId());
		alert.setAlertTypeId(item.getAlertTypeId());
		alert.setAlertMessage(item.getAlertMessage());
		alert.setAlertPosted(item.getAlertPosted());
		return alert;
	}

}
