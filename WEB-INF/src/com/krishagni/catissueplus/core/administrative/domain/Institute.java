package com.krishagni.catissueplus.core.administrative.domain;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
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

	private Set<Department> departments = new HashSet<Department>();
	
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

	public Set<Department> getDepartments() {
		return departments;
	}

	public void setDepartments(Set<Department> departments) {
		this.departments = departments;
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
		updateDepartments(other.getDepartments());
		updateActivityStatus(other.getActivityStatus());
	}
	
	public List<DependentEntityDetail> getDependentEntities() {
		int userCount = 0;
		for (Department dept : getDepartments()) {
			userCount += dept.getUsers().size();
		}
		
		return DependentEntityDetail
				.listBuilder()
				.add(User.getEntityName(), userCount)
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

	private void updateDepartments(Collection<Department> newDepts) {
		Map<String, Department> existingDeptsMap = new HashMap<String, Department>();
		for (Department dept : getDepartments()) {
			existingDeptsMap.put(dept.getName(), dept);
		}

		for (Department dept : newDepts) {
			if (existingDeptsMap.remove(dept.getName()) != null) {
				continue;
			}

			Department newDept = new Department();
			newDept.setName(dept.getName());
			newDept.setInstitute(this);
			getDepartments().add(newDept);
		}

		for (Department removedDept : existingDeptsMap.values()) {
			if (CollectionUtils.isNotEmpty(removedDept.getUsers())) {
				throw OpenSpecimenException.userError(InstituteErrorCode.DEPT_REF_ENTITY_FOUND, removedDept.getName());
			}
		}

		getDepartments().removeAll(existingDeptsMap.values());
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
