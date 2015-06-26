package com.epam.edu.student.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "alert")
public class Alert {
	public Alert() {

	}

	public Alert(Alert tempAlert) {
		this.id = tempAlert.getId();
		this.alertTypeId = tempAlert.getAlertTypeId();
		this.alertMessage = tempAlert.getAlertMessage();
		this.alertPosted = tempAlert.getAlertPosted();
	}

	public Alert(String string, int alertTypeId) {
		this.alertTypeId = alertTypeId;
		this.alertMessage = string;		
	}

	Integer id;
	Integer alertTypeId;
	String alertMessage;
	Integer alertPosted;

	@XmlElement(name = "id")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@XmlElement(name = "alertTypeId")
	public Integer getAlertTypeId() {
		return alertTypeId;
	}

	public void setAlertTypeId(Integer alertTypeId) {
		this.alertTypeId = alertTypeId;
	}

	@XmlElement(name = "alertMessage")
	public String getAlertMessage() {
		return alertMessage;
	}

	public void setAlertMessage(String alertMessage) {
		this.alertMessage = alertMessage;
	}

	@XmlElement(name = "alertPosted")
	public Integer getAlertPosted() {
		return alertPosted;
	}

	public void setAlertPosted(Integer alertPosted) {
		this.alertPosted = alertPosted;
	}

}
