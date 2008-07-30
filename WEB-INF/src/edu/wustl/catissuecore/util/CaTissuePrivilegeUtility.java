/**
 * 
 */
package edu.wustl.catissuecore.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.sun.java_cup.internal.runtime.Scanner;

import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.multiRepository.bean.SiteUserRolePrivilegeBean;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.dao.AbstractDAO;
import edu.wustl.common.dao.DAOFactory;
import edu.wustl.common.security.PrivilegeCache;
import edu.wustl.common.util.dbManager.DAOException;


/**
 * A Utility class to do higher level, caTissue-specific processing of privileges
 * 
 * @author juberahamad_patel
 *
 */
public class CaTissuePrivilegeUtility
{
	
	/**
	 * get privileges for all CPs for the user associated with the given privilege cache
	 * 
	 * @param privilegeCache
	 * @return list of SiteUserRolePrivilegeBean where each SiteUserRolePrivilegeBean represents privileges on a CP
	 * @throws DAOException
	 */
	public static List<SiteUserRolePrivilegeBean> getCPPrivileges(PrivilegeCache privilegeCache) throws DAOException
	{
		List<SiteUserRolePrivilegeBean> beans = new ArrayList<SiteUserRolePrivilegeBean>();
		
		//TODO remove the DB call
		User user = Utility.getUser(privilegeCache.getLoginName());
		user.getSiteCollection();
				
		Map<String, List<NameValueBean>> privileges = privilegeCache.getPrivilegesforPrefix(CollectionProtocol.class.getName()+"_");
		
		for(Entry<String, List<NameValueBean>> entry : privileges.entrySet())
		{
			SiteUserRolePrivilegeBean bean = new SiteUserRolePrivilegeBean();
			
			java.util.Scanner scanner = new java.util.Scanner(entry.getKey().substring(entry.getKey().lastIndexOf("_")+1));
			
			Long id = null; 
			
			if(scanner.hasNextLong())
			{
				id = new Long(scanner.nextLong());
				CollectionProtocol cp = null;
				
				AbstractDAO dao = DAOFactory.getInstance().getDAO(Constants.HIBERNATE_DAO);
				try
				{
					dao.openSession(null);
					cp = (CollectionProtocol)dao.retrieve(CollectionProtocol.class.getName(), id);
					cp.getSiteCollection();
				}
				finally
				{
					dao.closeSession();
				}
				
				bean.setCollectionProtocol(cp);
				bean.setUser(user);
				NameValueBean nmv = new NameValueBean();
				nmv.setName("CUSTOM_ROLE");
				nmv.setValue("-1");
				bean.setRole(nmv);
				bean.setPrivileges(entry.getValue());
				
				beans.add(bean);
			}
		}
		
		return beans;
		
	}
	
	
	/**
	 * get privileges for all Sites for the User associated with the given privilege cache
	 * 
	 * @param privilegeCache
	 * @return list of SiteUserRolePrivilegeBean where each SiteUserRolePrivilegeBean represents privileges on a Site
	 * 	@throws DAOException
	 */
	public static List<SiteUserRolePrivilegeBean> getSitePrivileges(PrivilegeCache privilegeCache) throws DAOException
	{
		List<SiteUserRolePrivilegeBean> beans = new ArrayList<SiteUserRolePrivilegeBean>();
		
		//TODO remove the DB call
		User user = Utility.getUser(privilegeCache.getLoginName());
		user.getSiteCollection();
				
		Map<String, List<NameValueBean>> privileges = privilegeCache.getPrivilegesforPrefix(Site.class.getName()+"_");
		
		for(Entry<String, List<NameValueBean>> entry : privileges.entrySet())
		{
			SiteUserRolePrivilegeBean bean = new SiteUserRolePrivilegeBean();
			
			java.util.Scanner scanner = new java.util.Scanner(entry.getKey().substring(entry.getKey().lastIndexOf("_")+1));
			
			Long id = null; 
			
			if(scanner.hasNextLong())
			{			
				id = new Long(entry.getKey().substring(entry.getKey().lastIndexOf("_")+1));
				Site site = null;
				  
				AbstractDAO dao = DAOFactory.getInstance().getDAO(Constants.HIBERNATE_DAO);
				try
				{
					dao.openSession(null);
					site = (Site)dao.retrieve(Site.class.getName(), id);
					site.getCollectionProtocolCollection();
					site.getAssignedSiteUserCollection();
				}
				finally
				{
					dao.closeSession();
				}
				
				List<Site> siteList = new ArrayList<Site>();
				siteList.add(site);
				bean.setSiteList(siteList);
				bean.setUser(user);
				
				NameValueBean nmv = new NameValueBean();
				nmv.setName("CUSTOM_ROLE");
				nmv.setValue("-1");
				bean.setRole(nmv);
				bean.setPrivileges(entry.getValue());
				
				beans.add(bean);
			}
		}
		
		return beans;
	}
	

}
 