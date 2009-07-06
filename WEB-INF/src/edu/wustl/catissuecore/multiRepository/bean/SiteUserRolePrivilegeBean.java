/**
 *
 */

package edu.wustl.catissuecore.multiRepository.bean;

import java.util.List;

import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.common.beans.NameValueBean;

/**
 * @author supriya_dankh
 * A java object used to populate ViewSitePrivilegesSummar.jsp.
 */
public class SiteUserRolePrivilegeBean
{

	/**
	 * Specify site List.
	 */
	private List<Site> siteList;
	/**
	 * Specify Instance of CollectionProtocol.
	 */
	private CollectionProtocol collectionProtocol;
	/**
	 * Specify Instance of User.
	 */
	private User user;
	/**
	 *  Specify Instance of NameValueBean for role.
	 */
	private NameValueBean role;
	/**
	 * Specify privileges list.
	 */
	private List<NameValueBean> privileges;
	/**
	 * Specify is All CP Checked.
	 */
	private boolean isAllCPChecked = false;
	/**
	 *  Specify isRowDeleted.
	 */
	private boolean isRowDeleted = false;
	/**
	 *  Specify isRowEdited.
	 */
	private boolean isRowEdited = true;
	//    private boolean isRowAddedInEditMode= false;
	/**
	 * Specify isCustChecked.
	 */
	private boolean isCustChecked = false;

	//	public boolean isRowAddedInEditMode() {
	//		return isRowAddedInEditMode;
	//	}
	//	public void setRowAddedInEditMode(boolean isRowAddedInEditMode) {
	//		this.isRowAddedInEditMode = isRowAddedInEditMode;
	//	}

	/**
	 * This method returns collectionProtocol.
	 * @return collectionProtocol.
	 */
	public CollectionProtocol getCollectionProtocol()
	{
		return collectionProtocol;
	}

	/**
	 * This method sets collectionProtocol.
	 * @param collectionProtocol CollectionProtocol object to be set.
	 */
	public void setCollectionProtocol(CollectionProtocol collectionProtocol)
	{
		this.collectionProtocol = collectionProtocol;
	}

	/**
	 * This method get list of privileges.
	 * @return privileges.
	 */
	public List<NameValueBean> getPrivileges()
	{
		return privileges;
	}

	/**
	 * This method set privileges.
	 * @param privileges privileges to be set.
	 */
	public void setPrivileges(List<NameValueBean> privileges)
	{
		this.privileges = privileges;
	}

	/**
	 * This method returns role.
	 * @return role.
	 */
	public NameValueBean getRole()
	{
		return role;
	}

	/**
	 * This method set role.
	 * @param role role to be set.
	 */
	public void setRole(NameValueBean role)
	{
		this.role = role;
	}

	/**
	 * This method get site list.
	 * @return site List
	 */
	public List<Site> getSiteList()
	{
		return siteList;
	}

	/**
	 * This method set site List.
	 * @param siteList site List to be set.
	 */
	public void setSiteList(List<Site> siteList)
	{
		this.siteList = siteList;
	}

	/**
	 * This method returns user.
	 * @return user
	 */
	public User getUser()
	{
		return user;
	}

	/**
	 * This method set user.
	 * @param user user to be set.
	 */
	public void setUser(User user)
	{
		this.user = user;
	}

	/**
	 * This method returns is all CP checked.
	 * @return isAllCPChecked boolean value.
	 */
	public boolean isAllCPChecked()
	{
		return isAllCPChecked;
	}

	/**
	 * This method set AllCPChecked.
	 * @param isAllCPChecked is All CP Checked.
	 */
	public void setAllCPChecked(boolean isAllCPChecked)
	{
		this.isAllCPChecked = isAllCPChecked;
	}

	/**
	 * This method returns isRowDeleted.
	 * @return isRowDeleted.
	 */
	public boolean isRowDeleted()
	{
		return isRowDeleted;
	}

	/**
	 * This method set isRowDeleted.
	 * @param isRowDeleted is Row Deleted.
	 */
	public void setRowDeleted(boolean isRowDeleted)
	{
		this.isRowDeleted = isRowDeleted;
	}

	/**
	 * This method returns is Row Edited.
	 * @return isRowEdited.
	 */
	public boolean isRowEdited()
	{
		return isRowEdited;
	}

	/**
	 * This method set isRowEdited.
	 * @param isRowEdited is Row Edited.
	 */
	public void setRowEdited(boolean isRowEdited)
	{
		this.isRowEdited = isRowEdited;
	}

	/**
	 * This method returns isCustChecked.
	 * @return isCustChecked.
	 */
	public boolean isCustChecked()
	{
		return isCustChecked;
	}

	/**
	 * This method set isCustChecked.
	 * @param isCustChecked isCustChecked.
	 */
	public void setCustChecked(boolean isCustChecked)
	{
		this.isCustChecked = isCustChecked;
	}
}
