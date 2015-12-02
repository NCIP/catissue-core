package com.krishagni.catissueplus.core.administrative.events;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.krishagni.catissueplus.core.administrative.domain.SpecimenRequest;
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenInfo;
import com.krishagni.catissueplus.core.common.events.UserSummary;
import com.krishagni.catissueplus.core.de.events.ExtensionDetail;

public class SpecimenRequestDetail extends SpecimenRequestSummary {
	private UserSummary processedBy;

	private Date dateOfProcessing;

	private String comments;

	private List<SpecimenInfo> specimens;

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

	public List<SpecimenInfo> getSpecimens() {
		return specimens;
	}

	public void setSpecimens(List<SpecimenInfo> specimens) {
		this.specimens = specimens;
	}

	public static SpecimenRequestDetail from(SpecimenRequest request) {
		SpecimenRequestDetail result = new SpecimenRequestDetail();
		copyTo(request, result);

		if (request.getProcessedBy() != null) {
			result.setProcessedBy(UserSummary.from(request.getProcessedBy()));
			result.setDateOfProcessing(request.getDateOfProcessing());
		}

		result.setComments(request.getComments());
		result.setSpecimens(SpecimenInfo.from(new ArrayList<Specimen>(request.getSpecimens())));
		return result;
	}
}
