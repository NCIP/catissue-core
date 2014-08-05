
package com.krishagni.catissueplus.core.administrative.services;

import com.krishagni.catissueplus.core.administrative.events.AllDepartmentsEvent;
import com.krishagni.catissueplus.core.administrative.events.CreateDepartmentEvent;
import com.krishagni.catissueplus.core.administrative.events.DepartmentCreatedEvent;
import com.krishagni.catissueplus.core.administrative.events.DepartmentDisabledEvent;
import com.krishagni.catissueplus.core.administrative.events.DepartmentGotEvent;
import com.krishagni.catissueplus.core.administrative.events.DepartmentUpdatedEvent;
import com.krishagni.catissueplus.core.administrative.events.DisableDepartmentEvent;
import com.krishagni.catissueplus.core.administrative.events.GetDepartmentEvent;
import com.krishagni.catissueplus.core.administrative.events.ReqAllDepartmentEvent;
import com.krishagni.catissueplus.core.administrative.events.UpdateDepartmentEvent;

public interface DepartmentService {

	public DepartmentCreatedEvent createDepartment(CreateDepartmentEvent event);

	public DepartmentUpdatedEvent updateDepartment(UpdateDepartmentEvent event);

	public DepartmentDisabledEvent deleteDepartment(DisableDepartmentEvent event);

	public DepartmentGotEvent getDepartment(GetDepartmentEvent event);

	public AllDepartmentsEvent getAllDepartments(ReqAllDepartmentEvent req);

}
