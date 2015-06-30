package com.epam.edu.student.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.json.JSONException;
import org.json.JSONObject;

@XmlRootElement(name = "alert")
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
	
	public JSONObject getJSON() {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.accumulate("id", id);
			jsonObject.accumulate("alertTypeId", alertTypeId);
			jsonObject.accumulate("alertMessage", alertMessage);
			jsonObject.accumulate("alertPosted", alertPosted);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonObject;
	}

}
