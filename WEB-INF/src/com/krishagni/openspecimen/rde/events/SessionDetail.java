package com.krishagni.openspecimen.rde.events;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.krishagni.openspecimen.rde.domain.Session;

public class SessionDetail {
	private Long id;
	
	private Map<String, Object> data;
	
	private Date createdOn;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public static SessionDetail from(Session session) {
		SessionDetail detail = new SessionDetail();
		detail.setId(session.getId());
		detail.setData(session.getData());
		detail.setCreatedOn(session.getCreatedOn());
		return detail;
	}

	public static List<SessionDetail> from(List<Session> sessions) {
		return sessions.stream().map(SessionDetail::from).collect(Collectors.toList());
	}
}
