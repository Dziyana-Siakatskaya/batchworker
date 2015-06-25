package com.epam.edu.student.model;

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
		System.out.println(this.alertTypeId + " " + this.alertMessage);
	}

	Integer id;
	Integer alertTypeId;
	String alertMessage;
	Integer alertPosted;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getAlertTypeId() {
		return alertTypeId;
	}

	public void setAlertTypeId(Integer alertTypeId) {
		this.alertTypeId = alertTypeId;
	}

	public String getAlertMessage() {
		return alertMessage;
	}

	public void setAlertMessage(String alertMessage) {
		this.alertMessage = alertMessage;
	}

	public Integer getAlertPosted() {
		return alertPosted;
	}

	public void setAlertPosted(Integer alertPosted) {
		this.alertPosted = alertPosted;
	}

}
