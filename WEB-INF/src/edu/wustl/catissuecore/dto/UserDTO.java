/**
 * 
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

	@Override
	public Long getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setAllValues(IValueObject valueObject)
			throws AssignDataException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setId(Long identifier) {
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
