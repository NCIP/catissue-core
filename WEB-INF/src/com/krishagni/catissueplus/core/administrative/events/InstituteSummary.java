
package com.krishagni.catissueplus.core.administrative.events;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.krishagni.catissueplus.core.administrative.domain.Institute;

public class InstituteSummary {

	private Long id;

	private String name;

	private String activityStatus;

	private int departmentsCount;

	private int usersCount;

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

	public String getActivityStatus() {
		return activityStatus;
	}

	public void setActivityStatus(String activityStatus) {
		this.activityStatus = activityStatus;
	}

	public int getDepartmentsCount() {
		return departmentsCount;
	}

	public void setDepartmentsCount(int departmentsCount) {
		this.departmentsCount = departmentsCount;
	}

	public int getUsersCount() {
		return usersCount;
	}

	public void setUsersCount(int usersCount) {
		this.usersCount = usersCount;
	}

	protected static void transform(Institute institute, InstituteSummary summary) {
		summary.setId(institute.getId());
		summary.setName(institute.getName());
		summary.setActivityStatus(institute.getActivityStatus());
	}

	public static InstituteSummary from(Institute institute) {
		InstituteSummary summary = new InstituteSummary();
		transform(institute, summary);
		return summary;
	}

	public static List<InstituteSummary> from(List<Institute> institutes) {
		List<InstituteSummary> result = new ArrayList<InstituteSummary>();

		if (CollectionUtils.isEmpty(institutes)) {
			return result;
		}

		for (Institute institute : institutes) {
			result.add(from(institute));
		}

		return result;
	}

}