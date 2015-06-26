package com.epam.edu.student.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.epam.edu.student.model.Alerts;

@Component(value = "rowMapper")
public class AlertRowMapper implements RowMapper<Alerts> {
	//private static final Logger LOG = Logger.getLogger(AllertRowMapper.class);

	@Override
	public Alerts mapRow(ResultSet rs, int rowNum) throws SQLException {
		Alerts alert = new Alerts();
		alert.setId(rs.getInt("id"));
		alert.setAlertTypeId(rs.getInt("alertTypeId"));
		alert.setAlertMessage(rs.getString("alertMessage"));
		alert.setAlertPosted(rs.getInt("alertPosted"));
		return alert;
	}

}