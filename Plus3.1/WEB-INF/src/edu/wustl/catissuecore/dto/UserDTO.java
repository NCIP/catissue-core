/**
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

	/**
	 * user.
	 */
	private User user;
	//private List<SiteUserRolePrivilegeBean> siteUserRolePrivilegeBeanList;
	/**
	 * userRowIdBeanMap.
	 */
	private Map<String, SiteUserRolePrivilegeBean> userRowIdBeanMap = new HashMap<String, SiteUserRolePrivilegeBean>();

	/**
	 * @return User
	 */
	public User getUser()
	{
		return this.user;
	}

	/**
	 * @param user : user
	 */
	public void setUser(User user)
	{
		this.user = user;
	}

	/**
	 * @return Map
	 */
	public Map<String, SiteUserRolePrivilegeBean> getUserRowIdBeanMap()
	{
		return this.userRowIdBeanMap;
	}

	/**
	 * @param userRowIdBeanMap : userRowIdBeanMap
	 */
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
