
package com.krishagni.catissueplus.core.biospecimen.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;

import com.krishagni.catissueplus.core.administrative.domain.Site;
import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.SpecimenErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.VisitErrorCode;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.DependentEntityDetail;
import com.krishagni.catissueplus.core.common.service.LabelGenerator;
import com.krishagni.catissueplus.core.common.util.Status;
import com.krishagni.catissueplus.core.common.util.Utility;

@Configurable
@Audited
@AuditTable(value="CAT_SPECIMEN_COLL_GROUP_AUD")
public class Visit extends BaseExtensionEntity {
	private static final String ENTITY_NAME = "visit";
	
	public static final String VISIT_STATUS_PENDING = "Pending";
	
	public static final String VISIT_STATUS_COMPLETED = "Complete";

	public static final String VISIT_STATUS_MISSED = "Missed Collection";

	private String name;
	
	private Date visitDate;

	private String clinicalDiagnosis;

	private String clinicalStatus;

	private String activityStatus;

	private Site site;

	private String status;

	private String comments;

	private String surgicalPathologyNumber;
	
	private String sprName;
	
	private boolean sprLocked;

	private CollectionProtocolEvent cpEvent;

	private Set<Specimen> specimens = new HashSet<Specimen>();

	private CollectionProtocolRegistration registration;
	
	private String defNameTmpl;

	private String missedReason;

	private User missedBy;
	
	private String cohort;
	
	@Autowired
	@Qualifier("visitNameGenerator")
	private LabelGenerator labelGenerator;
	
	public static String getEntityName() {
		return ENTITY_NAME;
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
		if (StringUtils.isBlank(activityStatus)) {
			activityStatus = Status.ACTIVITY_STATUS_ACTIVE.getStatus();
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

	public void setSprLocked(boolean sprLocked) {
		this.sprLocked = sprLocked;
	}

	public CollectionProtocolEvent getCpEvent() {
		return cpEvent;
	}

	public void setCpEvent(CollectionProtocolEvent cpEvent) {
		this.cpEvent = cpEvent;
	}

	@NotAudited
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
			if (specimen.getParentSpecimen() == null && specimen.getPooledSpecimen() == null) {
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

	public String getMissedReason() {
		return missedReason;
	}

	public void setMissedReason(String missedVisitReason) {
		this.missedReason = missedVisitReason;
	}

	public User getMissedBy() {
		return missedBy;
	}

	public void setMissedBy(User missedBy) {
		this.missedBy = missedBy;
	}

	public String getCohort() {
		return cohort;
	}

	public void setCohort(String cohort) {
		this.cohort = cohort;
	}

	public void setActive() {
		this.setActivityStatus(Status.ACTIVITY_STATUS_ACTIVE.getStatus());
	}

	public boolean isActive() {
		return Status.ACTIVITY_STATUS_ACTIVE.getStatus().equals(this.activityStatus);
	}

	public boolean isCompleted() {
		return isCompleted(getStatus());
	}
	
	public boolean isPending() {
		return isPending(getStatus());
	}
	
	public boolean isMissed() {
		return isMissed(getStatus());
	}
	
	public List<DependentEntityDetail> getDependentEntities() {
		return DependentEntityDetail.singletonList(Specimen.getEntityName(), getActiveSpecimens()); 
	}
	
	public void updateActivityStatus(String activityStatus) {
		if (this.activityStatus != null && this.activityStatus.equals(activityStatus)) {
			return;
		}
		
		if (Status.ACTIVITY_STATUS_DISABLED.getStatus().equals(activityStatus)) {
			delete();
		}
	}
	
	public void delete() {
		ensureNoActiveChildObjects();
		
		for (Specimen specimen : getSpecimens()) {
			specimen.disable(false);
		}
		
		setName(Utility.getDisabledValue(getName(), 255));
		setActivityStatus(Status.ACTIVITY_STATUS_DISABLED.getStatus());
	}
	
	public void update(Visit visit) {
		updateActivityStatus(visit.getActivityStatus());
		if (!isActive()) {
			return;
		}
		
		setName(visit.getName());
		setClinicalDiagnosis(visit.getClinicalDiagnosis());
		setClinicalStatus(visit.getClinicalStatus());
		setCpEvent(visit.getCpEvent());
		setRegistration(visit.getRegistration());
		setSite(visit.getSite());
		updateStatus(visit.getStatus());		
		setComments(visit.getComments());
		setMissedReason(isMissed() ? visit.getMissedReason() : null);
		setMissedBy(isMissed() ? visit.getMissedBy() : null);
		setSurgicalPathologyNumber(visit.getSurgicalPathologyNumber());
		setVisitDate(visit.getVisitDate());
		setCohort(visit.getCohort());
		setExtension(visit.getExtension());
	}

	public void updateSprName(String sprName) {
		setSprName(sprName);
	}	
	
	public void addSpecimen(Specimen specimen) {
		specimen.setVisit(this);
		getSpecimens().add(specimen);
	}
	
	public CollectionProtocol getCollectionProtocol() {
		return registration.getCollectionProtocol();
	}
	
	public void setNameIfEmpty() {
		if (StringUtils.isNotBlank(name)) {
			return;
		}

		String visitNameFmt = getCollectionProtocol().getVisitNameFormat();
		if (StringUtils.isBlank(visitNameFmt)) {
			visitNameFmt = defNameTmpl;
		}
		
		setName(labelGenerator.generateLabel(visitNameFmt, this));
	}
	
	public void updateStatus(String status) {
		if (StringUtils.isBlank(status)) {
			throw OpenSpecimenException.userError(VisitErrorCode.INVALID_STATUS);
		}
		
		if (status.equals(getStatus())) {
			return;
		}
		
		setStatus(status);		
		if (isMissed(status) || isPending(status)) {
			updateSpecimenStatus(status);
		}		
	}
	
	public void updateSpecimenStatus(String status) {
		for (Specimen specimen : getTopLevelSpecimens()) {
			specimen.updateCollectionStatus(status);
		}
		
		if (Specimen.isMissed(status)) {
			createMissedSpecimens();
		}
	}
	
	public void createMissedSpecimens() {
		Set<SpecimenRequirement> anticipated = getCpEvent().getTopLevelAnticipatedSpecimens();
		for (Specimen specimen : getTopLevelSpecimens()) {
			if (specimen.getSpecimenRequirement() != null) {
				anticipated.remove(specimen.getSpecimenRequirement());
			}			
		}
		
		for (SpecimenRequirement sr : anticipated) {
			Specimen specimen = sr.getSpecimen();
			specimen.setVisit(this);
			specimen.updateCollectionStatus(Specimen.MISSED_COLLECTION);
			addSpecimen(specimen);
		}		
	}
	
	public static boolean isCompleted(String status) {
		return Visit.VISIT_STATUS_COMPLETED.equals(status);
	}
	
	public static boolean isPending(String status) {
		return Visit.VISIT_STATUS_PENDING.equals(status);
	}
	
	public static boolean isMissed(String status) {
		return Visit.VISIT_STATUS_MISSED.equals(status);
	}	
		
	@Override
	public String getEntityType() {
		return "VisitExtension";
	}
	
	private void ensureNoActiveChildObjects() {
		for (Specimen specimen : getSpecimens()) {
			if (specimen.isActiveOrClosed() && specimen.isCollected()) {
				throw OpenSpecimenException.userError(SpecimenErrorCode.REF_ENTITY_FOUND);
			}
		}
	}
	
	private int getActiveSpecimens() {
		int count = 0;
		for (Specimen specimen : getSpecimens()) {
			if (specimen.isActiveOrClosed() && specimen.isCollected()) {
				++count;
			}
		}
		
		return count;
	}
}
