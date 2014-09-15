package com.krishagni.catissueplus.core.services.testdata;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import com.krishagni.rbac.domain.Group;
import com.krishagni.rbac.domain.GroupRole;
import com.krishagni.rbac.domain.Operation;
import com.krishagni.rbac.domain.Resource;
import com.krishagni.rbac.domain.ResourceInstanceOp;
import com.krishagni.rbac.domain.Role;
import com.krishagni.rbac.domain.RoleAccessControl;
import com.krishagni.rbac.domain.Subject;
import com.krishagni.rbac.domain.SubjectRole;
import com.krishagni.rbac.events.RoleDetails;
import com.krishagni.rbac.events.SubjectDetails;


public class RbacTestData {

	public static RoleDetails getRoleDetails(Long id, String name) {
		return RoleDetails.fromRole(getRole(id,name));
	}
	
	public static Role getRole(Long id, String name) {
		Role role = new Role();
		role.setId(id);
		role.setName(name);
		role.setAcl(new HashSet<RoleAccessControl>(getAcl()));
		return role;
	}
	
	public static List<RoleAccessControl> getAcl() {
		return Collections.singletonList(getRac(new Long(1)));
	}
	
	public static RoleAccessControl getRac(Long id) {
		RoleAccessControl rac = new RoleAccessControl();
		rac.setId(id);
		rac.setResource(getResource());
		rac.setOperations(new HashSet<ResourceInstanceOp>(getOps()));
		return rac;
	}
	
	public static List<ResourceInstanceOp> getOps() {
		List<ResourceInstanceOp> rip = new ArrayList<ResourceInstanceOp>();
		ResourceInstanceOp ri = new ResourceInstanceOp();
		ri.setId(new Long(1));
		ri.setOperation(getOperation());
		ri.setResourceInstanceId(new Long(-1));
		rip.add(ri);
		return rip;
	}
	
	public static Operation getOperation() {
		Operation o = new Operation();
		o.setName("Derive");
		o.setId(new Long(1));
		return o;
	}
	
	public static Subject getSubject(Long id) {
		Subject sb = new Subject();
		sb.setId(id);
		sb.getSubjectRoles().add(getSubjectRole((long)1, "admin"));
		sb.getSubjectRoles().add(getSubjectRole((long)2, "admin"));
		return sb;
	}
	
	public static SubjectDetails getSubjectDetails(Long id) {
		return SubjectDetails.fromSubject(getSubject(id));
	}
	
	public static SubjectRole getSubjectRole(Long dsoId, String roleName) {
		SubjectRole sr = new SubjectRole();
		sr.setDsoId(dsoId);
		sr.setRole(getRole(new Long(1), roleName));
		return sr;
	}
	
	public static GroupRole getGroupRole(Long dsoId, String roleName) {
		GroupRole gr = new GroupRole();
		gr.setDsoId(dsoId);
		gr.setRole(getRole(new Long(1), roleName));
		return gr;
	}
	
	public static Group getGroup(Long id) {
		Group gp = new Group();
		gp.setId(id);
		gp.getGroupRoles().add(getGroupRole((long)1, "admin"));
		gp.getGroupRoles().add(getGroupRole((long)2, "admin"));
		return gp;
	}

	public static Resource getResource() {
		Resource r = new Resource();
		r.setId(new Long(1));
		r.setName("Specimen");
		return r;
	}
}
