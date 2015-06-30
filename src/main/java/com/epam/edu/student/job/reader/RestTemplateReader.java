package com.epam.edu.student.job.reader;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.epam.edu.student.model.Alerts;

public class RestTemplateReader implements ItemReader<Alerts> {
	private static final Logger LOG = Logger
			.getLogger(RestTemplateReader.class);

	private long alertTypeId, alertCount;

	public RestTemplateReader(final long alertTypeId, final long alertCount) {
		this.alertTypeId = alertTypeId;
		this.alertCount = alertCount;
	}

	private boolean isServerRequested = false;
	private Alerts[] alerts;
	private int iterator;

	@Override
	public Alerts read() throws Exception, UnexpectedInputException,
			ParseException, NonTransientResourceException {

		if (!isServerRequested) {
			requestToServer();
		}

		if (iterator < alerts.length) {
			Alerts alert = new Alerts();

			alert.setId(alerts[iterator].getId());
			alert.setAlertTypeId(alerts[iterator].getAlertTypeId());
			alert.setAlertMessage(alerts[iterator].getAlertMessage() + "processed");
			alert.setAlertPosted(alerts[iterator].getAlertPosted());
			iterator++;
			return alert;
		} else
			return null;
	}

	private void requestToServer() {
		isServerRequested = true;
		RestTemplate restTemplate = new RestTemplate();

		/*
		 * List<HttpMessageConverter<?>> messageConverters = new
		 * ArrayList<HttpMessageConverter<?>>(); messageConverters.add(new
		 * MappingJackson2HttpMessageConverter());
		 * restTemplate.setMessageConverters(messageConverters);
		 */

		String url = "http://localhost:9999/restful-server/getAlert";

		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
				.queryParam("alertTypeId", alertTypeId)
				.queryParam("alertCount", alertCount);

		HttpEntity<?> entity = new HttpEntity<>(headers);

		HttpEntity<Alerts[]> serverResponse = restTemplate.exchange(builder
				.build().encode().toUri(), HttpMethod.POST, entity,
				Alerts[].class);
		alerts = serverResponse.getBody();
		for (Alerts temp : serverResponse.getBody())
			System.out.println(temp.toString());
	}

}
