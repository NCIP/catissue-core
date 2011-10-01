package edu.wustl.catissuecore.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.domain.AbstractDomainObject;

public class CPGridGrouperPrivilege extends AbstractDomainObject {

	private static final long serialVersionUID = 1234567891L;
	
	public static final String NAME_VALUE_SEP=":::";
	
	public static final String PRIVILEGE_SEP=",";
	
	public static final String GROUP_NAME_VALUE_SEP=":";
	
	public static final String TOSTRING_SEP=";;";
	
	public static final String ACTIVE="ACTIVE";
	
	public static final String DELETE="DELETE";
	
	public static final String GRID_GROUP_DESC = "GRID_GROUP";
	
	private Long id;
	
	private String groupName;
	
	private String stemName;
	
	private String privilegeString;
	
	private String status = ACTIVE;
	
	private String roleId;
	
	private CollectionProtocol collectionProtocol;
	
	public CPGridGrouperPrivilege() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public CPGridGrouperPrivilege(String groupName, String stemName, Set<NameValueBean> privileges, String roleId) {
		super();
		this.groupName = groupName;
		this.stemName = stemName;
		this.roleId=roleId;
		setPrivileges(privileges);
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id=id;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getStemName() {
		return stemName;
	}

	public void setStemName(String stemName) {
		this.stemName = stemName;
	}

	public String getPrivilegeString() {
		return privilegeString;
	}

	public void setPrivilegeString(String privilegeString) {
		this.privilegeString = privilegeString;
	}

	public Set<NameValueBean> getPrivileges() {
		Set<NameValueBean> nvSet = new HashSet<NameValueBean>();
		for(String s : privilegeString.split(PRIVILEGE_SEP)){
			String[] details = s.split(NAME_VALUE_SEP);
			nvSet.add(new NameValueBean(details[0], details[1]));
		}
		return nvSet;
	}

	public void setPrivileges(Set<NameValueBean> privileges) {
		List<String> nvStringList = new ArrayList();
		for(NameValueBean nv : privileges){
			nvStringList.add(nv.getName()+NAME_VALUE_SEP+nv.getValue());
		}
		privilegeString = StringUtils.join(nvStringList, ",");
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public CollectionProtocol getCollectionProtocol() {
		return collectionProtocol;
	}

	public void setCollectionProtocol(CollectionProtocol collectionProtocol) {
		this.collectionProtocol = collectionProtocol;
	}
	
	private String getPrivilegeDisplayString(){
		List<String> privilegeDisplayList = new ArrayList<String>();
		for(NameValueBean nv : getPrivileges()){
			privilegeDisplayList.add(nv.getName());
		}
		return StringUtils.join(privilegeDisplayList, PRIVILEGE_SEP);
	}
	
	public String getGroupDisplayString(){
		return groupName.split(GROUP_NAME_VALUE_SEP)[1];
	}
	
	@Override
	public String toString() {
		return stemName+TOSTRING_SEP+getGroupDisplayString()+TOSTRING_SEP+getPrivilegeDisplayString();
	}

	public static List<CPGridGrouperPrivilege> getActiveCPGridGrouperPrivileges(List<CPGridGrouperPrivilege> input){
		List<CPGridGrouperPrivilege> output = new ArrayList<CPGridGrouperPrivilege>();
        if (input == null) return output;
		for(CPGridGrouperPrivilege p : input){
			if(p.getStatus().equals(ACTIVE)) output.add(p);
		}
		return output;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
}
