
package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolRegistration;
import com.krishagni.catissueplus.core.biospecimen.domain.Visit;
import com.krishagni.catissueplus.core.common.AttributeModifiedSupport;
import com.krishagni.catissueplus.core.common.ListenAttributeChanges;
import com.krishagni.catissueplus.core.common.events.UserSummary;
import com.krishagni.catissueplus.core.de.events.ExtensionDetail;

@ListenAttributeChanges
public class VisitDetail extends AttributeModifiedSupport {
	private Long cprId;

	private Long eventId;
	
	private String ppid;
	
	private String eventLabel;

	private Double eventPoint;

	private Long cpId;

	private String cpTitle;

	private String cpShortTitle;

	private Long id;

	private String name;
	
	private String clinicalDiagnosis;
	
	private String clinicalStatus;

	private String activityStatus;

	private String site;

	private String status;

	private String comments;

	private String surgicalPathologyNumber;
	
	private String sprName;

	private String missedReason;

	private UserSummary missedBy;
	
	private boolean sprLocked;

	private Date visitDate;
	
	private String code;
	
	private String cohort;
	
	private ExtensionDetail extensionDetail;

	public Long getCprId() {
		return cprId;
	}

	public void setCprId(Long cprId) {
		this.cprId = cprId;
	}

	public Long getEventId() {
		return eventId;
	}

	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}

	public String getPpid() {
		return ppid;
	}

	public void setPpid(String ppid) {
		this.ppid = ppid;
	}

	public String getEventLabel() {
		return eventLabel;
	}

	public void setEventLabel(String eventLabel) {
		this.eventLabel = eventLabel;
	}

	public Double getEventPoint() {
		return eventPoint;
	}

	public void setEventPoint(Double eventPoint) {
		this.eventPoint = eventPoint;
	}

	public Long getCpId() {
		return cpId;
	}

	public void setCpId(Long cpId) {
		this.cpId = cpId;
	}

	public String getCpTitle() {
		return cpTitle;
	}

	public void setCpTitle(String cpTitle) {
		this.cpTitle = cpTitle;
	}
	
	public String getCpShortTitle() {
		return cpShortTitle;		
	}

	public void setCpShortTitle(String cpShortTitle) {
		this.cpShortTitle = cpShortTitle;
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
		this.activityStatus = activityStatus;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getSurgicalPathologyNumber() {
		return surgicalPathologyNumber;
	}

	public void setSurgicalPathologyNumber(String surgicalPathologyNumber) {
		this.surgicalPathologyNumber = surgicalPathologyNumber;
	}
	
	public String getSprName() {
		return sprName;
	}

	public void setSprName(String sprName) {
		this.sprName = sprName;
	}
	
	public boolean isSprLocked() {
		return sprLocked;
	}

	public void setSprLocked(boolean sprLock) {
		this.sprLocked = sprLock;
	}

	public Date getVisitDate() {
		return visitDate;
	}

	public void setVisitDate(Date visitDate) {
		this.visitDate = visitDate;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMissedReason() {
		return missedReason;
	}

	public void setMissedReason(String missedReason) {
		this.missedReason = missedReason;
	}

	public UserSummary getMissedBy() {
		return missedBy;
	}

	public void setMissedBy(UserSummary missedBy) {
		this.missedBy = missedBy;
	}

	public String getCohort() {
		return cohort;
	}

	public void setCohort(String cohort) {
		this.cohort = cohort;
	}

	public ExtensionDetail getExtensionDetail() {
		return extensionDetail;
	}

	public void setExtensionDetail(ExtensionDetail extensionDetail) {
		this.extensionDetail = extensionDetail;
	}

	public static VisitDetail from(Visit visit) {
		return from(visit, true, true);
	}

	public static VisitDetail from(Visit visit, boolean partial, boolean excludePhi) {
		VisitDetail detail = new VisitDetail();
		detail.setActivityStatus(visit.getActivityStatus());
		detail.setClinicalDiagnosis(visit.getClinicalDiagnosis());
		detail.setClinicalStatus(visit.getClinicalStatus());
		detail.setStatus(visit.getStatus());
		detail.setComments(visit.getComments());
		detail.setId(visit.getId());
		detail.setName(visit.getName());
		detail.setSurgicalPathologyNumber(visit.getSurgicalPathologyNumber());
		detail.setSprName(visit.getSprName());
		detail.setSprLocked(visit.isSprLocked());
		detail.setVisitDate(visit.getVisitDate());
		detail.setMissedReason(visit.getMissedReason());
		detail.setCohort(visit.getCohort());

		if (visit.getMissedBy() != null) {
			detail.setMissedBy(UserSummary.from(visit.getMissedBy()));
		}

		if (visit.getSite() != null) {
			detail.setSite(visit.getSite().getName());
		}

		CollectionProtocolRegistration cpr = visit.getRegistration();
		detail.setCprId(cpr.getId());
		detail.setPpid(cpr.getPpid());
		detail.setCpId(cpr.getCollectionProtocol().getId());
		detail.setCpTitle(cpr.getCollectionProtocol().getTitle());
		detail.setCpShortTitle(cpr.getCollectionProtocol().getShortTitle());
		
		if (!visit.isUnplanned()) {
			detail.setEventId(visit.getCpEvent().getId());
			detail.setEventLabel(visit.getCpEvent().getEventLabel());
			detail.setEventPoint(visit.getCpEvent().getEventPoint());
		}
		
		if (!partial) {
			detail.setExtensionDetail(ExtensionDetail.from(visit.getExtension(), excludePhi));
		}
		return detail;
	}
	
	public static List<VisitDetail> from(Collection<Visit> visits) {
		List<VisitDetail> result = new ArrayList<VisitDetail>();
		
		if (CollectionUtils.isEmpty(visits)) {
			return result;
		}
		
		for (Visit visit : visits) {
			result.add(from(visit));
		}
		
		return result;
	}

}
