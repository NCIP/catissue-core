/**
 * 
 */
package edu.wustl.catissuecore.multiRepository.bean;

import java.util.List;
import java.util.Set;

import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.common.util.Permissions;


/**
 * @author supriya_dankh
 * A java object used to populate ViewSitePrivilegesSummar.jsp. 
 */
public class SiteUserRolePrivilegeBean
{
	private List<Long> siteList ;
	private List<Long> cpList;
	private User user ;
    private String role;
    private Set<String> privileges;
	
	public List<Long> getSiteList()
	{
		return siteList;
	}
	
	public void setSiteList(List<Long> siteList)
	{
		this.siteList = siteList;
	}
	
	public String getRole()
	{
		return role;
	}
	
	public void setRole(String role)
	{
		this.role = role;
	}
	
	public Set<String> getPrivileges()
	{
		return privileges;
	}
	
	public void setPrivileges(Set<String> privileges)
	{
		this.privileges = privileges;
	}
	
		public List<Long> getCpList()
	{
		return cpList;
	}

	public void setCpList(List<Long> cpList)
	{
		this.cpList = cpList;
	}

	
	public User getUser()
	{
		return user;
	}

	
	public void setUser(User user)
	{
		this.user = user;
	}
    
    
}
