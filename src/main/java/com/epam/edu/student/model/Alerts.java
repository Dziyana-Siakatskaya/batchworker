package com.epam.edu.student.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "record")
public class Alerts implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Alerts() {

	}

	private int id;
	private int alertTypeId;
	private String alertMessage;
	private int alertPosted;

	@XmlElement(name = "id")
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@XmlElement(name = "alertTypeId")
	public int getAlertTypeId() {
		return alertTypeId;
	}

	public void setAlertTypeId(int alertTypeId) {
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
	public int getAlertPosted() {
		return alertPosted;
	}

	public void setAlertPosted(int alertPosted) {
		this.alertPosted = alertPosted;
	}

	@Override
	public String toString() {
		return "Alert [id=" + id + ", alertTypeId=" + alertTypeId
				+ ", alertMessage=" + alertMessage + ", alertPosted="
				+ alertPosted + "]";
	}
	
	

}
