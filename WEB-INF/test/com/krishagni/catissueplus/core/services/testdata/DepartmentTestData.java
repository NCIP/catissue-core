
package com.krishagni.catissueplus.core.services.testdata;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.krishagni.catissueplus.core.administrative.domain.Department;
import com.krishagni.catissueplus.core.administrative.domain.Institute;
import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.administrative.events.CreateDepartmentEvent;
import com.krishagni.catissueplus.core.administrative.events.DepartmentDetails;
import com.krishagni.catissueplus.core.administrative.events.DisableDepartmentEvent;
import com.krishagni.catissueplus.core.administrative.events.GetDepartmentEvent;
import com.krishagni.catissueplus.core.administrative.events.ReqAllDepartmentEvent;
import com.krishagni.catissueplus.core.administrative.events.UpdateDepartmentEvent;

public class DepartmentTestData {

	public static final String DEPARTMENT_NAME = "department name";

	public static final String INSTITUTE = "institute";

	public static final Object ACTIVITY_STATUS_DISABLED = "Disabled";

	public static final Object PERMISSIBLE_VALUE = "permissible value";

	public static CreateDepartmentEvent getCreateDepartmentEvent() {
		CreateDepartmentEvent event = new CreateDepartmentEvent();
		DepartmentDetails details = new DepartmentDetails();
		details.setName("My department");
		details.setInstituteName("My Inst");
		event.setDepartmentDetails(details);
		return event;
	}

	public static UpdateDepartmentEvent getUpdateDepartmentEvent() {
		UpdateDepartmentEvent event = new UpdateDepartmentEvent();
		DepartmentDetails details = new DepartmentDetails();
		details.setId(1l);
		details.setName("My department");
		details.setInstituteName("My Inst");
		event.setDepartmentDetails(details);
		return event;
	}

	public static Department getDepartment(long id) {
		Department department = new Department();
		department.setId(id);
		department.setName("My department");
		department.setInstitute(getInstitue());
		Long ide = department.getId(); //for coverage
		return department;
	}

	private static Institute getInstitue() {
		Institute institute = new Institute();
		institute.setId(1l);
		institute.setName("My Inst");
		Long id = institute.getId(); //for coverage
		return institute;
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

	public static UpdateDepartmentEvent getUpdateDepartmentEventForChangedName() {
		UpdateDepartmentEvent event = getUpdateDepartmentEvent();
		event.getDepartmentDetails().setName("My Bio Department");
		return event;
	}

	public static DisableDepartmentEvent getDisableDepartmentEventForName() {
		DisableDepartmentEvent event = new DisableDepartmentEvent();
		event.setName("Abc");
		return event;
	}

	public static DisableDepartmentEvent getDisableDepartmentEvent() {
		DisableDepartmentEvent event = new DisableDepartmentEvent();
		event.setId(1l);
		return event;
	}

	public static Department getDepartmentForDisable(long id) {
		Department department = getDepartment(id);
		department.setUserCollection(getUserCollection());
		return department;
	}

	private static Set<User> getUserCollection() {
		Set<User> users = new HashSet<User>();
		users.add(new User());
		return users;
	}

	public static GetDepartmentEvent getDepartmentEvent() {
		GetDepartmentEvent event = new GetDepartmentEvent();
		event.setId(1l);
		return event;
	}

	public static GetDepartmentEvent getDepartmentEventForName() {
		GetDepartmentEvent event = new GetDepartmentEvent();
		event.setName("Abc");
		return event;
	}

	public static List<Department> getDepartments() {
		List<Department> departments = new ArrayList<Department>();
		departments.add(getDepartment(1L));
		departments.add(getDepartment(2L));
		return departments;
	}

	public static ReqAllDepartmentEvent getAllDepartmentEvent() {
		ReqAllDepartmentEvent event = new ReqAllDepartmentEvent();
		event.setMaxResults(1000);
		return event;
	}

}
