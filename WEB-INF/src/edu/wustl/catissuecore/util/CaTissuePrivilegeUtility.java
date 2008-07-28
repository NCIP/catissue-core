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
import edu.wustl.catissuecore.multiRepository.bean.SiteUserRolePrivilegeBean;
import edu.wustl.catissuecore.util.global.Utility;
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
		
		Map<String, Set<String>> privileges = privilegeCache.getPrivilegesforPrefix(CollectionProtocol.class.getName()+"_");
		
		for(Entry<String, Set<String>> entry : privileges.entrySet())
		{
			SiteUserRolePrivilegeBean bean = new SiteUserRolePrivilegeBean();
			
			java.util.Scanner scanner = new java.util.Scanner(entry.getKey().substring(entry.getKey().lastIndexOf("_")+1));
			
			Long id = null; 
			
			if(scanner.hasNextLong())
			{
				id = new Long(scanner.nextLong());
				List<Long> idList = new ArrayList<Long>();
				idList.add(id);
				bean.setCpList(idList);
				//TODO remove the DB call
				bean.setUser(Utility.getUser(privilegeCache.getLoginName()));
				bean.setRole("CUSTOM_ROLE");
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
		
		Map<String, Set<String>> privileges = privilegeCache.getPrivilegesforPrefix(Site.class.getName()+"_");
		
		for(Entry<String, Set<String>> entry : privileges.entrySet())
		{
			SiteUserRolePrivilegeBean bean = new SiteUserRolePrivilegeBean();
			
			java.util.Scanner scanner = new java.util.Scanner(entry.getKey().substring(entry.getKey().lastIndexOf("_")+1));
			
			Long id = null; 
			
			if(scanner.hasNextLong())
			{			
				id = new Long(entry.getKey().substring(entry.getKey().lastIndexOf("_")+1));
				  
				List<Long> idList = new ArrayList<Long>();
				idList.add(id);
				bean.setSiteList(idList);
				//TODO remove the DB call
				bean.setUser(Utility.getUser(privilegeCache.getLoginName()));
				bean.setRole("CUSTOM_ROLE");
				bean.setPrivileges(entry.getValue());
				
				beans.add(bean);
			}
		}
		
		return beans;
	}
	

}
 