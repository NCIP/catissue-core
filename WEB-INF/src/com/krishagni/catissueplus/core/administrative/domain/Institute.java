package com.krishagni.catissueplus.core.administrative.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import com.krishagni.catissueplus.core.administrative.domain.factory.InstituteErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.BaseEntity;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.DependentEntityDetail;
import com.krishagni.catissueplus.core.common.util.Status;
import com.krishagni.catissueplus.core.common.util.Utility;

@Audited
public class Institute extends BaseEntity {

	private static final String ENTITY_NAME ="institute";
	
	private String name;

	private String activityStatus;

	private Set<User> users = new HashSet<User>();
	
	private Set<Site> sites = new HashSet<Site>(); 

	public static String getEntityName() {
		return ENTITY_NAME;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getActivityStatus() {
		return activityStatus;
	}

	public void setActivityStatus(String activityStatus) {
		this.activityStatus = activityStatus;
	}

	@NotAudited
	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> userCollection) {
		this.users = userCollection;
	}
	
	@NotAudited
	public Set<Site> getSites() {
		return sites;
	}

	public void setSites(Set<Site> sites) {
		this.sites = sites;
	}

	public void update(Institute other) {		
		setName(other.getName());
		updateActivityStatus(other.getActivityStatus());
	}
	
	public List<DependentEntityDetail> getDependentEntities() {
		
		return DependentEntityDetail
				.listBuilder()
				.add(User.getEntityName(), getUsers().size())
				.add(Site.getEntityName(), getSites().size())
				.build();
	}
	
	public void delete(Boolean close) {
		String activityStatus = Status.ACTIVITY_STATUS_CLOSED.getStatus();
		if (!close) {
			ensureFreeOfDependencies();
			
			setName(Utility.getDisabledValue(getName(), 255));
			activityStatus = Status.ACTIVITY_STATUS_DISABLED.getStatus();
		}
		
		setActivityStatus(activityStatus);
	}

	private void updateActivityStatus(String newActivityStatus) {
		if (activityStatus.equals(newActivityStatus)) {
			return;
		}
		
		if (Status.ACTIVITY_STATUS_DISABLED.getStatus().equals(newActivityStatus)) {
			ensureFreeOfDependencies();
		}
		
		setActivityStatus(newActivityStatus);
	}
	
	private void ensureFreeOfDependencies() {
		List<DependentEntityDetail> dependentEntities = getDependentEntities();
		if (!dependentEntities.isEmpty()) {
			throw OpenSpecimenException.userError(InstituteErrorCode.REF_ENTITY_FOUND);
		}
	}
}
