package com.epam.edu.student.processor;

import org.springframework.batch.item.ItemProcessor;

import com.epam.edu.student.model.Alert;

public class AlertItemProcessor implements ItemProcessor<Alert, Alert> {
	//private static final Logger LOG = Logger.getLogger(AlertItemProcessor.class);

	@Override
	public Alert process(Alert item) throws Exception {
		System.out.println("alert id: ");
		return new Alert(item);
	}

}
