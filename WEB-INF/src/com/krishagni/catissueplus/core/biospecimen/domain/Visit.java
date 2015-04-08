
package com.krishagni.catissueplus.core.biospecimen.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.hibernate.envers.Audited;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;

import com.krishagni.catissueplus.core.administrative.domain.Site;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.VisitErrorCode;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.service.LabelGenerator;
import com.krishagni.catissueplus.core.common.util.Status;
import com.krishagni.catissueplus.core.common.util.Utility;

@Configurable
@Audited
public class Visit {
	private static final String ENTITY_NAME = "visit";
	
	public static final String VISIT_STATUS_PENDING = "Pending";
	
	public static final String VISIT_STATUS_COMPLETED = "Complete";

	private Long id;

	private String name;
	
	private Date visitDate;

	private String clinicalDiagnosis;

	private String clinicalStatus;

	private String activityStatus;

	private String identifiedReport;

	private String deIdentifiedReport;

	private Site site;

	private String status;

	private String comments;

	private String surgicalPathologyNumber;

	private CollectionProtocolEvent cpEvent;

	private Set<Specimen> specimens = new HashSet<Specimen>();

	private CollectionProtocolRegistration registration;
	
	private String defNameTmpl;
	
	@Autowired
	@Qualifier("visitNameGenerator")
	private LabelGenerator labelGenerator;
	
	public static String getEntityName() {
		return ENTITY_NAME;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getVisitDate() {
		return visitDate;
	}

	public void setVisitDate(Date visitDate) {
		this.visitDate = visitDate;
	}

	public String getClinicalDiagnosis() {
		return clinicalDiagnosis;
	}

	public void setClinicalDiagnosis(String clinicalDiagnosis) {
		this.clinicalDiagnosis = clinicalDiagnosis;
	}

	public String getClinicalStatus() {
		return clinicalStatus;
	}

	public void setClinicalStatus(String clinicalStatus) {
		this.clinicalStatus = clinicalStatus;
	}

	public String getActivityStatus() {
		return activityStatus;
	}

	public void setActivityStatus(String activityStatus) {
		if (this.activityStatus != null && this.activityStatus.equals(activityStatus)) {
			return;
		}
		
		if (StringUtils.isBlank(activityStatus)) {
			activityStatus = Status.ACTIVITY_STATUS_ACTIVE.getStatus();
		}
		
		if (this.activityStatus != null && Status.ACTIVITY_STATUS_DISABLED.getStatus().equals(activityStatus)) {
			delete();
		}		

		this.activityStatus = activityStatus;
	}

	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		if (this.status != null && this.status.equals(status)) {
			return;
		}
		
		if (StringUtils.isBlank(status)) {
			throw OpenSpecimenException.userError(VisitErrorCode.INVALID_STATUS);
		}
		
		if (this.status != null && VISIT_STATUS_PENDING.equals(status)) {
			for (Specimen specimen : specimens) {
				specimen.setPending();
			}
		}
		
		this.status = status;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

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

	public String getSurgicalPathologyNumber() {
		return surgicalPathologyNumber;
	}

	public void setSurgicalPathologyNumber(String surgicalPathologyNumber) {
		this.surgicalPathologyNumber = surgicalPathologyNumber;
	}

	public CollectionProtocolEvent getCpEvent() {
		return cpEvent;
	}

	public void setCpEvent(CollectionProtocolEvent cpEvent) {
		this.cpEvent = cpEvent;
	}

	public Set<Specimen> getSpecimens() {
		return specimens;
	}

	public void setSpecimens(Set<Specimen> specimens) {
		this.specimens = specimens;
	}
	
	public Set<Specimen> getTopLevelSpecimens() {
		Set<Specimen> result = new HashSet<Specimen>();
		if (specimens == null) {
			return specimens;
		}
		
		for (Specimen specimen : specimens) {
			if (specimen.getParentSpecimen() == null) {
				result.add(specimen);
			}
		}
		
		return result;
	}

	public CollectionProtocolRegistration getRegistration() {
		return registration;
	}

	public void setRegistration(CollectionProtocolRegistration registration) {
		this.registration = registration;
	}

	public String getDefNameTmpl() {
		return defNameTmpl;
	}

	public void setDefNameTmpl(String defNameTmpl) {
		this.defNameTmpl = defNameTmpl;
	}

	public void setActive() {
		this.setActivityStatus(Status.ACTIVITY_STATUS_ACTIVE.getStatus());
	}

	public boolean isActive() {
		return Status.ACTIVITY_STATUS_ACTIVE.getStatus().equals(this.activityStatus);
	}

	public boolean isCompleted() {
		return Visit.VISIT_STATUS_COMPLETED.equals(this.status);
	}

	public void delete() {
		checkActiveDependents();
		this.name = Utility.getDisabledValue(name);
		this.activityStatus = Status.ACTIVITY_STATUS_DISABLED.getStatus();
	}

	public void update(Visit visit) {
		setActivityStatus(visit.getActivityStatus());
		setClinicalDiagnosis(visit.getClinicalDiagnosis());
		setClinicalStatus(visit.getClinicalStatus());
		setCpEvent(visit.getCpEvent());
		setRegistration(visit.getRegistration());
		setSite(visit.getSite());
		setStatus(visit.getStatus());
		setIdentifiedReport(visit.getIdentifiedReport());
		setDeIdentifiedReport(visit.getDeIdentifiedReport());
		setComments(visit.getComments());
		setName(visit.getName());		
		setSurgicalPathologyNumber(visit.getSurgicalPathologyNumber());
		setVisitDate(visit.getVisitDate());
	}

	public void updateReports(Visit scg) {
		this.setIdentifiedReport(scg.getIdentifiedReport());
		this.setDeIdentifiedReport(scg.getDeIdentifiedReport());
	}	
	
	public void addSpecimen(Specimen specimen) {
		specimens.add(specimen);
	}
	
	public CollectionProtocol getCollectionProtocol() {
		return registration.getCollectionProtocol();
	}
	
	public void setNameIfEmpty() {
		if (StringUtils.isNotBlank(name)) {
			return;
		}
		
		setName(labelGenerator.generateLabel(defNameTmpl, this));
	}
	
	private void checkActiveDependents() {
		for (Specimen specimen : this.specimens) {
			if (specimen.isActive()) {
				throw OpenSpecimenException.userError(VisitErrorCode.REF_ENTITY_FOUND);
			}
		}
	}	
}
