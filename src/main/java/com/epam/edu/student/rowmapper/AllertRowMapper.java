package com.epam.edu.student.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.epam.edu.student.model.Alert;

@Component(value = "rowMapper")
public class AllertRowMapper implements RowMapper<Alert> {
	 
		@Override
		public Alert mapRow(ResultSet rs, int rowNum) throws SQLException {
			Alert alert = new Alert();	 
			alert.setId(rs.getInt("id"));
			alert.setAlertTypeId(rs.getInt("alertTypeId"));
			alert.setAlertMessage(rs.getString("alertMessage"));
			alert.setAlertPosted(rs.getInt("alertPosted"));
	 
			return alert;
		}
	 
	}