package com.krishagni.catissueplus.core.administrative.events;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.krishagni.catissueplus.core.administrative.domain.SpecimenRequest;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolSummary;
import com.krishagni.catissueplus.core.common.events.UserSummary;

public class SpecimenRequestSummary {
	private Long id;

	private CollectionProtocolSummary cp;

	private UserSummary requestor;

	private Date dateOfRequest;

	private String activityStatus;

	private Integer requestedSpecimensCount;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public CollectionProtocolSummary getCp() {
		return cp;
	}

	public void setCp(CollectionProtocolSummary cp) {
		this.cp = cp;
	}

	public UserSummary getRequestor() {
		return requestor;
	}

	public void setRequestor(UserSummary requestor) {
		this.requestor = requestor;
	}

	public Date getDateOfRequest() {
		return dateOfRequest;
	}

	public void setDateOfRequest(Date dateOfRequest) {
		this.dateOfRequest = dateOfRequest;
	}

	public String getActivityStatus() {
		return activityStatus;
	}

	public void setActivityStatus(String activityStatus) {
		this.activityStatus = activityStatus;
	}

	public Integer getRequestedSpecimensCount() {
		return requestedSpecimensCount;
	}

	public void setRequestedSpecimensCount(Integer requestedSpecimensCount) {
		this.requestedSpecimensCount = requestedSpecimensCount;
	}

	public static void copyTo(SpecimenRequest request, SpecimenRequestSummary summary) {
		summary.setId(request.getId());
		summary.setCp(CollectionProtocolSummary.from(request.getCp()));
		summary.setRequestor(UserSummary.from(request.getRequestor()));
		summary.setDateOfRequest(request.getDateOfRequest());
		summary.setActivityStatus(request.getActivityStatus());
		summary.setRequestedSpecimensCount(request.getSpecimensCount());
	}

	public static SpecimenRequestSummary from(SpecimenRequest request) {
		SpecimenRequestSummary result = new SpecimenRequestSummary();
		copyTo(request, result);
		return result;
	}

	public static List<SpecimenRequestSummary> from(Collection<SpecimenRequest> requests) {
		List<SpecimenRequestSummary> result = new ArrayList<SpecimenRequestSummary>();
		for (SpecimenRequest request : requests) {
			result.add(SpecimenRequestSummary.from(request));
		}

		return result;
	}
}