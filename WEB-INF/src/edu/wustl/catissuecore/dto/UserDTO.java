/**
 * 
 */
package edu.wustl.catissuecore.dto;

import java.util.HashMap;
import java.util.Map;

import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.multiRepository.bean.SiteUserRolePrivilegeBean;


/**
 * @author supriya_dankh
 *
 */
public class UserDTO
{
  
	private User user;
	//private List<SiteUserRolePrivilegeBean> siteUserRolePrivilegeBeanList;
	private Map<String, SiteUserRolePrivilegeBean> userRowIdBeanMap= new HashMap<String, SiteUserRolePrivilegeBean>();
	
	public User getUser()
	{
		return user;
	}
	
	public void setUser(User user)
	{
		this.user = user;
	}

	public Map<String, SiteUserRolePrivilegeBean> getUserRowIdBeanMap() 
	{
		return userRowIdBeanMap;
	}

	public void setUserRowIdBeanMap(Map<String, SiteUserRolePrivilegeBean> userRowIdBeanMap) 
	{
		this.userRowIdBeanMap = userRowIdBeanMap;
	}
	
	/*public List<SiteUserRolePrivilegeBean> getSiteUserRolePrivilegeBeanList()
	{
		return siteUserRolePrivilegeBeanList;
	}
	
	public void setSiteUserRolePrivilegeBeanList(
			List<SiteUserRolePrivilegeBean> siteUserRolePrivilegeBeanList)
	{
		this.siteUserRolePrivilegeBeanList = siteUserRolePrivilegeBeanList;
	}*/
	
	
}
