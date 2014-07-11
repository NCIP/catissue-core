
package com.krishagni.catissueplus.core.biospecimen.events;

import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenCollectionGroup;

public class ScgReportDetail extends ScgDetail {

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
		detail.setCollectionComments(scg.getCollectionComments());
		detail.setCollectionContainer(scg.getCollectionContainer());
		detail.setCollectionProcedure(scg.getCollectionProcedure());
		detail.setCollectionSiteName(scg.getCollectionSite().getName());
		detail.setCollectionStatus(scg.getCollectionStatus());
		detail.setCollectionTimestamp(scg.getCollectionTimestamp());
		detail.setCollectorName(scg.getCollector().getLastName() + ", " + scg.getCollector().getFirstName());
		detail.setComment(scg.getComment());
		detail.setCpeId(scg.getCollectionProtocolEvent().getId());
		detail.setCprId(scg.getCollectionProtocolRegistration().getId());
		detail.setId(scg.getId());
		detail.setName(scg.getName());
		detail.setIdentifiedReport(scg.getIdentifiedReport());
		detail.setDeIdentifiedReport(scg.getDeIdentifiedReport());
		detail.setReceivedComments(scg.getReceivedComments());
		detail.setReceivedQuality(scg.getReceivedQuality());
		detail.setReceivedTimestamp(scg.getReceivedTimestamp());
		detail.setReceiverName(scg.getReceiver().getLastName() + ", " + scg.getReceiver().getFirstName());
		detail.setSurgicalPathologyNumber(scg.getSurgicalPathologyNumber());
		return detail;
	}

}
