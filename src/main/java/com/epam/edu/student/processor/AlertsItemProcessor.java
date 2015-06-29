package com.epam.edu.student.processor;

import org.springframework.batch.item.ItemProcessor;

import com.epam.edu.student.model.Alerts;

public class AlertsItemProcessor implements ItemProcessor<Alerts, Alerts> {
	//private static final Logger LOG = Logger.getLogger(AlertsItemProcessor.class);

	@Override
	public Alerts process(final Alerts item) throws Exception {
			final Alerts transForm = new Alerts();
			transForm.setId(item.getId());
			transForm.setAlertTypeId(item.getAlertTypeId());
			transForm.setAlertMessage(item.getAlertMessage().toUpperCase());
			transForm.setAlertPosted(item.getAlertPosted());
			return transForm;
	}

}
