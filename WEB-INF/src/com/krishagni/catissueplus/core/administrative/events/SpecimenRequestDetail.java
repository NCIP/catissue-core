package com.krishagni.catissueplus.core.administrative.events;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.krishagni.catissueplus.core.administrative.domain.SpecimenRequest;
import com.krishagni.catissueplus.core.administrative.domain.SpecimenRequestItem;
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenInfo;
import com.krishagni.catissueplus.core.common.events.UserSummary;
import com.krishagni.catissueplus.core.de.events.ExtensionDetail;

public class SpecimenRequestDetail extends SpecimenRequestSummary {
	private UserSummary processedBy;

	private Date dateOfProcessing;

	private String comments;

	private String requestorInstitute;

	private List<SpecimenRequestItemDetail> items;

	public UserSummary getProcessedBy() {
		return processedBy;
	}

	public void setProcessedBy(UserSummary processedBy) {
		this.processedBy = processedBy;
	}

	public Date getDateOfProcessing() {
		return dateOfProcessing;
	}

	public void setDateOfProcessing(Date dateOfProcessing) {
		this.dateOfProcessing = dateOfProcessing;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getRequestorInstitute() {
		return requestorInstitute;
	}

	public void setRequestorInstitute(String requestorInstitute) {
		this.requestorInstitute = requestorInstitute;
	}

	public List<SpecimenRequestItemDetail> getItems() {
		return items;
	}

	public void setItems(List<SpecimenRequestItemDetail> items) {
		this.items = items;
	}

	public static SpecimenRequestDetail from(SpecimenRequest request) {
		SpecimenRequestDetail result = new SpecimenRequestDetail();
		copyTo(request, result);

		if (request.getProcessedBy() != null) {
			result.setProcessedBy(UserSummary.from(request.getProcessedBy()));
			result.setDateOfProcessing(request.getDateOfProcessing());
		}

		result.setComments(request.getComments());

		if (request.getRequestor().getInstitute() != null) {
			result.setRequestorInstitute(request.getRequestor().getInstitute().getName());
		}

		result.setItems(SpecimenRequestItemDetail.from(request.getItems()));
		return result;
	}
}
