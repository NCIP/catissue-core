/**
 */

package edu.wustl.catissuecore.dto;

import java.util.HashMap;
import java.util.Map;

import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.multiRepository.bean.SiteUserRolePrivilegeBean;
import edu.wustl.common.actionForm.IValueObject;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.AssignDataException;

/**
 * @author supriya_dankh
 *
 */
public class UserDTO extends AbstractDomainObject
{
	/**
	 * user.
	 */
	private User user;
	//private List<SiteUserRolePrivilegeBean> siteUserRolePrivilegeBeanList;
	/**
	 * userRowIdBeanMap.
	 */
	private Map<String, SiteUserRolePrivilegeBean> userRowIdBeanMap =
		new HashMap<String, SiteUserRolePrivilegeBean>();
	/**
	 * @return User
	 */
	public User getUser()
	{
		return user;
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
		return userRowIdBeanMap;
	}
	/**
	 * @param userRowIdBeanMap : userRowIdBeanMap
	 */
	public void setUserRowIdBeanMap(Map<String, SiteUserRolePrivilegeBean> userRowIdBeanMap)
	{
		this.userRowIdBeanMap = userRowIdBeanMap;
	}

	@Override
	/**
	 * @return Long
	 */
	public Long getId()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	/**
	 * @param valueObject : valueObject
	 * @throws AssignDataException : AssignDataException
	 */
	public void setAllValues(IValueObject valueObject) throws AssignDataException
	{
		// TODO Auto-generated method stub

	}

	@Override
	/**
	 * @param identifier : identifier
	 */
	public void setId(Long identifier)
	{
		// TODO Auto-generated method stub

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
