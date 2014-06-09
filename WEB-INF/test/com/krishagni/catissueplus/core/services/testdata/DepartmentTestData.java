
package com.krishagni.catissueplus.core.services.testdata;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.krishagni.catissueplus.core.administrative.domain.Department;
import com.krishagni.catissueplus.core.administrative.domain.Institute;
import com.krishagni.catissueplus.core.administrative.events.CreateDepartmentEvent;
import com.krishagni.catissueplus.core.administrative.events.DepartmentDetails;
import com.krishagni.catissueplus.core.administrative.events.UpdateDepartmentEvent;

public class DepartmentTestData {

	public static final Object DEPARTMENT_NAME = "department name";

	public static final Object INSTITUTE = "institute";

	public static CreateDepartmentEvent getCreateDepartmentEvent() {
		CreateDepartmentEvent event = new CreateDepartmentEvent();
		DepartmentDetails details = new DepartmentDetails();
		details.setName("My department");
		details.setInstituteNames(getInstituteNames());
		event.setDepartmentDetails(details);
		return event;
	}

	private static List<String> getInstituteNames() {
		List<String> instituteNames = new ArrayList<String>();
		instituteNames.add("My Inst");
		return instituteNames;
	}

	public static UpdateDepartmentEvent getUpdateDepartmentEvent() {
		UpdateDepartmentEvent event = new UpdateDepartmentEvent();
		DepartmentDetails details = new DepartmentDetails();
		details.setName("My department");
		details.setInstituteNames(getInstituteNames());
		event.setDepartmentDetails(details);
		return event;
	}

	public static Department getDepartment(long id) {
		Department department = new Department();
		department.setId(id);
		department.setName("My department");
		department.setInstitutes(getInstitues());
		return department;
	}

	private static Set<Institute> getInstitues() {
		Set<Institute> institutes = new HashSet<Institute>();
		Institute institute = new Institute();
		institute.setId(1l);
		institute.setName("My Inst");
		institutes.add(institute);
		return institutes;
	}

	public static CreateDepartmentEvent getCreateDepartmentEventWithEmptyDepartmentName() {
		CreateDepartmentEvent event = getCreateDepartmentEvent();
		DepartmentDetails departmentDetails = event.getDepartmentDetails();
		departmentDetails.setName(null);
		return event;
	}

	public static UpdateDepartmentEvent getUpdateDepartmentEventWithEmptyDepartmentName() {
		UpdateDepartmentEvent event = getUpdateDepartmentEvent();
		DepartmentDetails departmentDetails = event.getDepartmentDetails();
		departmentDetails.setName(null);
		return event;
	}

}
