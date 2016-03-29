package com.krishagni.openspecimen.rde.events;

import java.util.Date;
import java.util.List;

import com.krishagni.catissueplus.core.biospecimen.events.SpecimenDetail;
import com.krishagni.catissueplus.core.common.events.UserSummary;

public class SpecimenAndFrozenEventDetail {
	private List<SpecimenDetail> specimens;
	
	private List<EventDetail> events;

	public List<SpecimenDetail> getSpecimens() {
		return specimens;
	}

	public void setSpecimens(List<SpecimenDetail> specimens) {
		this.specimens = specimens;
	}

	public List<EventDetail> getEvents() {
		return events;
	}

	public void setEvents(List<EventDetail> events) {
		this.events = events;
	}
	
	public static class EventDetail {
		private Long id;
		
		private Long specimenId;
		
		private Long visitId;
		
		private Long reqId;
		
		private UserSummary user;
		
		private Date time;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public Long getSpecimenId() {
			return specimenId;
		}

		public void setSpecimenId(Long specimenId) {
			this.specimenId = specimenId;
		}

		public Long getVisitId() {
			return visitId;
		}

		public void setVisitId(Long visitId) {
			this.visitId = visitId;
		}

		public Long getReqId() {
			return reqId;
		}

		public void setReqId(Long reqId) {
			this.reqId = reqId;
		}

		public UserSummary getUser() {
			return user;
		}

		public void setUser(UserSummary user) {
			this.user = user;
		}

		public Date getTime() {
			return time;
		}

		public void setTime(Date time) {
			this.time = time;
		}	
	}	
}
