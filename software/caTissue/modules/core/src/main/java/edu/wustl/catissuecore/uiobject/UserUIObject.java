package edu.wustl.catissuecore.uiobject;

import edu.wustl.common.domain.UIObject;

public class UserUIObject implements UIObject {

	protected String oldPassword;

	protected String newPassword;

	protected String pageOf;

	/**
     * Role id of the user.
     */
    protected String roleId;

	/**
	 * WUSTLkey of a user.
	 */
	private String targetIdpLoginName = null;

	private String targetPassword;

	private String targetIdp;
	
	private String grouperUser;

	public String getGrouperUser() {
		return grouperUser;
	}

	public void setGrouperUser(String grouperUser) {
		this.grouperUser = grouperUser;
	}

	public String getTargetIdp() {
		return targetIdp;
	}

	public void setTargetIdp(final String targetIdp) {
		this.targetIdp = targetIdp;
	}

	public String getTargetPassword() {
		return targetPassword;
	}

	public void setTargetPassword(final String targetPassword) {
		this.targetPassword = targetPassword;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(final String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(final String newPassword) {
		this.newPassword = newPassword;
	}

	public String getPageOf() {
		return pageOf;
	}

	public void setPageOf(final String pageOf) {
		this.pageOf = pageOf;
	}

	public String getTargetIdpLoginName() {
		return targetIdpLoginName;
	}

	public void setTargetIdpLoginName(final String targetIdpLoginName) {
		this.targetIdpLoginName = targetIdpLoginName;
	}

	/**
	 * @return the roleId
	 */
	public String getRoleId() {
		return roleId;
	}

	/**
	 * @param roleId the roleId to set
	 */
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

}
