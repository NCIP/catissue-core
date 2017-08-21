package com.krishagni.core.tests.testdata;

import java.util.ArrayList;
import java.util.List;

import com.krishagni.catissueplus.core.administrative.events.DepartmentDetail;
import com.krishagni.catissueplus.core.administrative.events.InstituteDetail;

public class InstituteTestData {
	public static InstituteDetail getInstituteDetail() {
		InstituteDetail detail = new InstituteDetail();
		detail.setDepartments(getListOfDepartment());
		detail.setName("default-institute");
		return detail;
	}

	public static InstituteDetail getUpdateInstituteDetail() {
		InstituteDetail detail = new InstituteDetail();
		detail.setId(1L);
		detail.setDepartments(getListOfDepartmentforUpdate());
		detail.setName("updated-institute");
		return detail;
	}
	
	private static List<DepartmentDetail> getListOfDepartment() {
		List<DepartmentDetail> deptList = new ArrayList<DepartmentDetail>();
		DepartmentDetail detail = new DepartmentDetail();
		detail.setId(1L);
		detail.setName("default-department");
		deptList.add(detail);
		return deptList;
	}

	private static List<DepartmentDetail> getListOfDepartmentforUpdate() {
		List<DepartmentDetail> deptList = new ArrayList<DepartmentDetail>();
		DepartmentDetail detail1 = new DepartmentDetail();
		detail1.setId(1L);
		detail1.setName("updated-department");
		deptList.add(detail1);
	
		return deptList;
	}

	public static List<DepartmentDetail> getInvalidDepartmentDetailList() {
		List<DepartmentDetail> deptList = new ArrayList<DepartmentDetail>();
		DepartmentDetail detail = new DepartmentDetail();
		detail.setId(-1L);
		detail.setName("invalid-department");
		deptList.add(detail);
		return deptList;
	}
}
