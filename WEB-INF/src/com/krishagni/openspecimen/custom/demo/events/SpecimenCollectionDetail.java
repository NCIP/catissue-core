package com.krishagni.openspecimen.custom.demo.events;

import java.util.Map;

import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolRegistrationDetail;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenDetail;
import com.krishagni.catissueplus.core.biospecimen.events.VisitDetail;

public class SpecimenCollectionDetail {
	private CollectionProtocolRegistrationDetail cpr;
	
	private Map<String, Object> patientSmokingHistory;
	
	private VisitDetail visit;
	
	private SpecimenDetail blood;
	
	private Map<String, Object> bloodExtn;
	
	private SpecimenDetail frozenTissue;
	
	public CollectionProtocolRegistrationDetail getCpr() {
		return cpr;
	}

	public void setCpr(CollectionProtocolRegistrationDetail cpr) {
		this.cpr = cpr;
	}

	public Map<String, Object> getPatientSmokingHistory() {
		return patientSmokingHistory;
	}

	public void setPatientSmokingHistory(Map<String, Object> patientSmokingHistory) {
		this.patientSmokingHistory = patientSmokingHistory;
	}

	public VisitDetail getVisit() {
		return visit;
	}

	public void setVisit(VisitDetail visit) {
		this.visit = visit;
	}

	public SpecimenDetail getBlood() {
		return blood;
	}

	public void setBlood(SpecimenDetail blood) {
		this.blood = blood;
	}

	public Map<String, Object> getBloodExtn() {
		return bloodExtn;
	}

	public void setBloodExtn(Map<String, Object> bloodExtn) {
		this.bloodExtn = bloodExtn;
	}

	public SpecimenDetail getFrozenTissue() {
		return frozenTissue;
	}

	public void setFrozenTissue(SpecimenDetail frozenTissue) {
		this.frozenTissue = frozenTissue;
	}
}
