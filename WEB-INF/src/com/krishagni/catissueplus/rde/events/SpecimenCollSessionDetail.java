package com.krishagni.catissueplus.rde.events;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.krishagni.catissueplus.rde.domain.SpecimenCollectionSession;

public class SpecimenCollSessionDetail {
	private Long id;
	
	private String data;
	
	private Date createdOn;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
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
