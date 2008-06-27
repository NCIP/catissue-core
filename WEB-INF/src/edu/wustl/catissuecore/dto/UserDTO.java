/**
 * 
 */
package edu.wustl.catissuecore.dto;

import java.util.List;

import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.multiRepository.bean.SiteUserRolePrivilegeBean;


/**
 * @author supriya_dankh
 *
 */
public class UserDTO
{
  
	private User user;
	private List<SiteUserRolePrivilegeBean> siteUserRolePrivilegeBeanList;
	
	public User getUser()
	{
		return user;
	}
	
	public void setUser(User user)
	{
		this.user = user;
	}
	
	public List<SiteUserRolePrivilegeBean> getSiteUserRolePrivilegeBeanList()
	{
		return siteUserRolePrivilegeBeanList;
	}
	
	public void setSiteUserRolePrivilegeBeanList(
			List<SiteUserRolePrivilegeBean> siteUserRolePrivilegeBeanList)
	{
		this.siteUserRolePrivilegeBeanList = siteUserRolePrivilegeBeanList;
	}
	
	
}
