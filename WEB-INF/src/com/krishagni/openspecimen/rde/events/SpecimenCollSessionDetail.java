package com.krishagni.openspecimen.rde.events;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.krishagni.openspecimen.rde.domain.SpecimenCollectionSession;

public class SpecimenCollSessionDetail {
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
	
	public static List<SpecimenCollSessionDetail> from(List<SpecimenCollectionSession> sessions) {
		List<SpecimenCollSessionDetail> result = new ArrayList<SpecimenCollSessionDetail>();
		for (SpecimenCollectionSession session : sessions) {
			result.add(from(session));
		}
		
		return result;
	}
	
	public static SpecimenCollSessionDetail from(SpecimenCollectionSession session) {
		SpecimenCollSessionDetail detail = new SpecimenCollSessionDetail();
		detail.setId(session.getId());
		detail.setData(session.getData());
		detail.setCreatedOn(session.getCreatedOn());
		return detail;
	}
}
