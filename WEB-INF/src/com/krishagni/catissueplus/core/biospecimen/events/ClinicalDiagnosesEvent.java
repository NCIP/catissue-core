package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.List;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class ClinicalDiagnosesEvent extends ResponseEvent {
	private List<String> diagnoses; // Not a spell mistake. It is plural form of diagnosis

	public List<String> getDiagnoses() {
		return diagnoses;
	}

	public void setDiagnoses(List<String> diagnoses) {
		this.diagnoses = diagnoses;
	}
	
	public static ClinicalDiagnosesEvent ok(List<String> diagnoses) {
		ClinicalDiagnosesEvent resp = new ClinicalDiagnosesEvent();
		resp.setDiagnoses(diagnoses);
		resp.setStatus(EventStatus.OK);
		return resp;
	}
	
	public static ClinicalDiagnosesEvent notFound() {
		ClinicalDiagnosesEvent resp = new ClinicalDiagnosesEvent();
		resp.setStatus(EventStatus.NOT_FOUND);
		return resp;
	}
}
