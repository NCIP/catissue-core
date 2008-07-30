/**
 * 
 */
package edu.wustl.catissuecore.multiRepository.bean;

import java.util.List;
import java.util.Set;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.common.beans.NameValueBean;


/**
 * @author supriya_dankh
 * A java object used to populate ViewSitePrivilegesSummar.jsp. 
 */
public class SiteUserRolePrivilegeBean
{
	private List<Site> siteList ;
	private CollectionProtocol collectionProtocol;
	private User user ;
    private NameValueBean role;
    private List<NameValueBean> privileges;
    
	public CollectionProtocol getCollectionProtocol() {
		return collectionProtocol;
	}
	public void setCollectionProtocol(CollectionProtocol collectionProtocol) {
		this.collectionProtocol = collectionProtocol;
	}
	public List<NameValueBean> getPrivileges() {
		return privileges;
	}
	public void setPrivileges(List<NameValueBean> privileges) {
		this.privileges = privileges;
	}
	public NameValueBean getRole() {
		return role;
	}
	public void setRole(NameValueBean role) {
		this.role = role;
	}
	public List<Site> getSiteList() {
		return siteList;
	}
	public void setSiteList(List<Site> siteList) {
		this.siteList = siteList;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
    
    
}
