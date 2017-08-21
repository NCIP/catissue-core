
package com.krishagni.catissueplus.core.administrative.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import com.krishagni.catissueplus.core.administrative.domain.factory.DistributionProtocolErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.BaseExtensionEntity;
import com.krishagni.catissueplus.core.common.CollectionUpdater;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.DependentEntityDetail;
import com.krishagni.catissueplus.core.common.util.Status;
import com.krishagni.catissueplus.core.common.util.Utility;
import com.krishagni.catissueplus.core.de.domain.SavedQuery;

@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
@AuditTable(value = "CAT_DISTRIBUTION_PROTOCOL_AUD")
public class DistributionProtocol extends BaseExtensionEntity {
	public static final String EXTN = "DistributionProtocolExtension";

	private static final String ENTITY_NAME = "distribution_protocol";

	private Institute institute;
	
	private Site defReceivingSite;

	private User principalInvestigator;

	private Set<User> coordinators = new HashSet<User>();

	private String title;

	private String shortTitle;

	private String irbId;

	private Date startDate;
	
	private Date endDate;

	private String activityStatus;
	
	private SavedQuery report;
	
	private Set<DistributionOrder> distributionOrders = new HashSet<DistributionOrder>();
	
	private Set<DpDistributionSite> distributingSites = new HashSet<DpDistributionSite>();
	
	private Set<DpRequirement> requirements = new HashSet<DpRequirement>();
	
	public static String getEntityName() {
		return ENTITY_NAME;
	}
	
	public Institute getInstitute() {
		return institute;
	}

	public void setInstitute(Institute institute) {
		this.institute = institute;
	}
	
	public Site getDefReceivingSite() {
		return defReceivingSite;
	}
	
	public void setDefReceivingSite(Site defReceivingSite) {
		this.defReceivingSite = defReceivingSite;
	}

	public User getPrincipalInvestigator() {
		return principalInvestigator;
	}

	public void setPrincipalInvestigator(User principalInvestigator) {
		this.principalInvestigator = principalInvestigator;
	}

	public Set<User> getCoordinators() {
		return coordinators;
	}

	public void setCoordinators(Set<User> coordinators) {
		this.coordinators = coordinators;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getShortTitle() {
		return shortTitle;
	}

	public void setShortTitle(String shortTitle) {
		this.shortTitle = shortTitle;
	}

	public String getIrbId() {
		return irbId;
	}

	public void setIrbId(String irbId) {
		this.irbId = irbId;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getActivityStatus() {
		return activityStatus;
	}

	public void setActivityStatus(String activityStatus) {
		this.activityStatus = activityStatus;
	}

	public SavedQuery getReport() {
		return report;
	}

	public void setReport(SavedQuery report) {
		this.report = report;
	}

	public Set<DistributionOrder> getDistributionOrders() {
		return distributionOrders;
	}

	public void setDistributionOrders(Set<DistributionOrder> distributionOrders) {
		this.distributionOrders = distributionOrders;
	}
	
	public Set<DpDistributionSite> getDistributingSites() {
		return distributingSites;
	}
	
	public void setDistributingSites(Set<DpDistributionSite> distributingSites) {
		this.distributingSites = distributingSites;
	}

	public Set<DpRequirement> getRequirements() {
		return requirements;
	}

	public void setRequirements(Set<DpRequirement> requirements) {
		this.requirements = requirements;
	}

	public void update(DistributionProtocol dp) {
		if (dp.getActivityStatus().equals(Status.ACTIVITY_STATUS_DISABLED.getStatus())) {
			setShortTitle(Utility.getDisabledValue(dp.getShortTitle(), 50));
			setTitle(Utility.getDisabledValue(dp.getTitle(), 255));
		} else {
			setShortTitle(dp.getShortTitle());
			setTitle(dp.getTitle());
		}
		setIrbId(dp.getIrbId());
		setInstitute(dp.getInstitute());
		setDefReceivingSite(dp.getDefReceivingSite());
		setPrincipalInvestigator(dp.getPrincipalInvestigator());
		setStartDate(dp.getStartDate());
		setEndDate(dp.getEndDate());
		setActivityStatus(dp.getActivityStatus());
		setReport(dp.getReport());
		CollectionUpdater.update(getCoordinators(), dp.getCoordinators());
		CollectionUpdater.update(getDistributingSites(), dp.getDistributingSites());
		setExtension(dp.getExtension());
	}
	
	public List<DependentEntityDetail> getDependentEntities() {
		return DependentEntityDetail
				.singletonList(DistributionOrder.getEntityName(), getDistributionOrders().size());
	}
	
	public void delete() {
		List<DependentEntityDetail> dependentEntities = getDependentEntities();
		if (!dependentEntities.isEmpty()) {
			throw OpenSpecimenException.userError(DistributionProtocolErrorCode.REF_ENTITY_FOUND);
		}
		
		setShortTitle(Utility.getDisabledValue(getShortTitle(), 50));
		setTitle(Utility.getDisabledValue(getTitle(), 255));
		setActivityStatus(Status.ACTIVITY_STATUS_DISABLED.getStatus());
	}
	
	public Set<Site> getAllDistributingSites() {
		Set<Site> sites = new HashSet<Site>();
		for (DpDistributionSite distSite : getDistributingSites()) {
			if (distSite.getSite() != null) {
				sites.add(distSite.getSite());
			} else {
				sites.addAll(distSite.getInstitute().getSites());
			}
		}
		
		return sites;
	}

	public Set<Institute> getDistributingInstitutes() {
		return getDistributingSites().stream().map(dpSite -> dpSite.getInstitute()).collect(Collectors.toSet());
	}
	
	public boolean hasRequirement(String specimenType, String anatomicSite, Set<String> pathologyStatuses, String clinicalDiagnosis) {
		for (DpRequirement req : getRequirements()) {
			if (req.equalsSpecimenGroup(specimenType, anatomicSite, pathologyStatuses, clinicalDiagnosis)) {
				return true;
			}
		}
		
		return false;
	}

	@Override
	public String getEntityType() {
		return EXTN;
	}
}
