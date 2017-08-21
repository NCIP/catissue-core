
package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.krishagni.catissueplus.core.biospecimen.domain.ParticipantMedicalIdentifier;

public class PmiDetail {

	String siteName;

	String mrn;

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public String getMrn() {
		return mrn;
	}

	public void setMrn(String mrn) {
		this.mrn = mrn;
	}
	
	public static PmiDetail from(ParticipantMedicalIdentifier pmi, boolean excludePhi) {
		PmiDetail result = new PmiDetail();
		result.setMrn(excludePhi ? "###" : pmi.getMedicalRecordNumber());
		result.setSiteName(pmi.getSite().getName());
		return result;
	}
	
	public static List<PmiDetail> from(Collection<ParticipantMedicalIdentifier> pmis, boolean excludePhi) {
		List<PmiDetail> result = new ArrayList<PmiDetail>();
		for (ParticipantMedicalIdentifier pmi : pmis) {
			result.add(PmiDetail.from(pmi, excludePhi));
		}
		
		return result;
	}
}
