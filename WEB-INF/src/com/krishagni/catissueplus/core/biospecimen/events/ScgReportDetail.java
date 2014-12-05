
package com.krishagni.catissueplus.core.biospecimen.events;

import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenCollectionGroup;

//
// TODO: Check why this is needed
//
public class ScgReportDetail extends VisitDetail {

	private String identifiedReport;

	private String deIdentifiedReport;

	public String getIdentifiedReport() {
		return identifiedReport;
	}

	public void setIdentifiedReport(String identifiedReport) {
		this.identifiedReport = identifiedReport;
	}

	public String getDeIdentifiedReport() {
		return deIdentifiedReport;
	}

	public void setDeIdentifiedReport(String deIdentifiedReport) {
		this.deIdentifiedReport = deIdentifiedReport;
	}

	public static ScgReportDetail fromDomain(SpecimenCollectionGroup scg) {
		ScgReportDetail detail = new ScgReportDetail();
		detail.setActivityStatus(scg.getActivityStatus());
		detail.setBarcode(scg.getBarcode());
		detail.setClinicalDiagnosis(scg.getClinicalDiagnosis());
		detail.setClinicalStatus(scg.getClinicalStatus());
		detail.setVisitSite(scg.getCollectionSite().getName());
		detail.setVisitStatus(scg.getCollectionStatus());
		detail.setComment(scg.getComment());
		detail.setCpeId(scg.getCollectionProtocolEvent().getId());
		detail.setCprId(scg.getCollectionProtocolRegistration().getId());
		detail.setId(scg.getId());
		detail.setName(scg.getName());
		detail.setIdentifiedReport(scg.getIdentifiedReport());
		detail.setDeIdentifiedReport(scg.getDeIdentifiedReport());
		detail.setSurgicalPathologyNumber(scg.getSurgicalPathologyNumber());
		return detail;
	}
}
