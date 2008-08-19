/**
 * 
 */

package edu.wustl.catissuecore.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import edu.emory.mathcs.backport.java.util.Collections;
import edu.wustl.catissuecore.bizlogic.UserBizLogic;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.multiRepository.bean.SiteUserRolePrivilegeBean;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.dao.AbstractDAO;
import edu.wustl.common.dao.DAO;
import edu.wustl.common.dao.DAOFactory;
import edu.wustl.common.security.PrivilegeCache;
import edu.wustl.common.security.PrivilegeManager;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.global.Variables;
import gov.nih.nci.security.authorization.domainobjects.Role;
import gov.nih.nci.security.exceptions.CSException;

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
	public static Map<String, SiteUserRolePrivilegeBean> getCPPrivileges(
			PrivilegeCache privilegeCache) throws DAOException
	{
		Map<String, SiteUserRolePrivilegeBean> map = new HashMap<String, SiteUserRolePrivilegeBean>();

		//TODO remove the DB call
		User user = Utility.getUser(privilegeCache.getLoginName());

		Map<String, List<NameValueBean>> privileges = privilegeCache
				.getPrivilegesforPrefix(CollectionProtocol.class.getName() + "_");

		AbstractDAO dao = DAOFactory.getInstance().getDAO(Constants.HIBERNATE_DAO);
		try
		{
			dao.openSession(null);
			user = (User) dao.retrieve(User.class.getName(), user.getId());

			for (Entry<String, List<NameValueBean>> entry : privileges.entrySet())
			{
				SiteUserRolePrivilegeBean bean = new SiteUserRolePrivilegeBean();

				java.util.Scanner scanner = new java.util.Scanner(entry.getKey().substring(
						entry.getKey().lastIndexOf("_") + 1));

				Long id = null;

				if (scanner.hasNextLong())
				{
					id = new Long(scanner.nextLong());

					CollectionProtocol cp = (CollectionProtocol) dao.retrieve(
							CollectionProtocol.class.getName(), id);
					for (Site site : cp.getSiteCollection())
					{
						Long temp = site.getId();

					}

					bean.setCollectionProtocol(cp);
					bean.setUser(user);
					bean.setSiteList(new ArrayList<Site>(cp.getSiteCollection()));
					NameValueBean nmv = new NameValueBean();
					nmv.setName("Custom role");
					nmv.setValue("-1");
					bean.setRole(nmv);
					bean.setPrivileges(entry.getValue());

					// Added by Ravindra to handle bean for EDIT mode
					bean.setRowEdited(false);
	
					map.put("CP_"+cp.getId(), bean);
				}
			}
		}

		finally
		{
			dao.closeSession();
		}

		return map;

	}

	/**
	 * get privileges for all Sites for the User associated with the given privilege cache
	 * 
	 * @param privilegeCache
	 * @return list of SiteUserRolePrivilegeBean where each SiteUserRolePrivilegeBean represents privileges on a Site
	 * 	@throws DAOException
	 */
	public static Map<String, SiteUserRolePrivilegeBean> getSitePrivileges(
			PrivilegeCache privilegeCache) throws DAOException
	{
		Map<String, SiteUserRolePrivilegeBean> map = new HashMap<String, SiteUserRolePrivilegeBean>();

		//TODO remove the DB call
		User user = Utility.getUser(privilegeCache.getLoginName());

		Map<String, List<NameValueBean>> privileges = privilegeCache
				.getPrivilegesforPrefix(Site.class.getName() + "_");

		AbstractDAO dao = DAOFactory.getInstance().getDAO(Constants.HIBERNATE_DAO);
		try
		{
			dao.openSession(null);

			user = (User) dao.retrieve(User.class.getName(), user.getId());

			for (Entry<String, List<NameValueBean>> entry : privileges.entrySet())
			{
				SiteUserRolePrivilegeBean bean = new SiteUserRolePrivilegeBean();

				java.util.Scanner scanner = new java.util.Scanner(entry.getKey().substring(
						entry.getKey().lastIndexOf("_") + 1));

				Long id = null;

				if (scanner.hasNextLong())
				{
					id = new Long(entry.getKey().substring(entry.getKey().lastIndexOf("_") + 1));
					Site site = null;

					site = (Site) dao.retrieve(Site.class.getName(), id);
					List<Site> siteList = new ArrayList<Site>();
					siteList.add(site);
					bean.setSiteList(siteList);
					bean.setUser(user);

					NameValueBean nmv = new NameValueBean();
					nmv.setName("Custom role");
					nmv.setValue("-1");
					bean.setRole(nmv);
					bean.setPrivileges(entry.getValue());

					// Added by Ravindra to handle bean for EDIT mode
					bean.setRowEdited(false);
	
					map.put(""+site.getId(), bean);
				}
			}
		}
		finally
		{
			dao.closeSession();
		}

		return map;
	}

	/**
	 * get the privileges on all Sites and CPs for the user associated with the given privilege cache
	 * 
	 * @param privilegeCache
	 * @return
	 * @throws DAOException
	 */
	public static Map<String, SiteUserRolePrivilegeBean> getAllPrivileges(
			PrivilegeCache privilegeCache)
	{
		Map<String, SiteUserRolePrivilegeBean> map = null;
		try
		{
			map = getCPPrivileges(privilegeCache);
			map.putAll(getSitePrivileges(privilegeCache));

		}
		catch (DAOException e)
		{
			e.printStackTrace();
		}

		return map;

	}

	/**
	 * get a map of the privileges all the users have on a given CP.
	 * 
	 * @param id of the CP
	 * @return a map of login name and list of name value beans representing privilege name and privilege id 
	 * @throws CSException 
	 */
	public static Map<String, SiteUserRolePrivilegeBean> getPrivilegesOnCP(Long id)
	{
		Map<String, SiteUserRolePrivilegeBean> result = new HashMap<String, SiteUserRolePrivilegeBean>();

		String objectId = CollectionProtocol.class.getName() + "_" + id;

		AbstractDAO hibernateDao = DAOFactory.getInstance().getDAO(Constants.HIBERNATE_DAO);

		try
		{
			hibernateDao.openSession(null);

			for (NameValueBean nmv : Variables.privilegeGroupingMap.get("CP"))
			{
				String privilegeName = nmv.getName();
				Set<String> users = PrivilegeManager.getInstance().getAccesibleUsers(objectId,
						privilegeName);

				for (String userName : users)
				{
					User user = Utility.getUser(userName);
					SiteUserRolePrivilegeBean bean = result.get(user.getId().toString());
					//if no privilege was so far detected for this user
					if (bean == null)
					{
						bean = new SiteUserRolePrivilegeBean();
						List<NameValueBean> privileges = new ArrayList<NameValueBean>();
						privileges.add(nmv);

						NameValueBean role = new NameValueBean();
						role.setName("Custom Role");
						role.setValue("-1");
						bean.setRole(role);
						Set<Long> siteSet = new UserBizLogic().getRelatedSiteIds(user.getId());
						List<Site> siteList = new ArrayList<Site>();
						if (siteSet == null || siteSet.isEmpty())
						{
							continue;
						}

						hibernateDao.openSession(null);
						for (Long siteId : siteSet)
						{
							Site site = (Site) hibernateDao.retrieve(Site.class.getName(), siteId);
							siteList.add(site);
						}

						bean.setSiteList(siteList);

						bean.setPrivileges(privileges);
						
						// Added by Ravindra to handle bean for EDIT mode
						bean.setRowEdited(false);
						
						bean.setUser(user);
						result.put(user.getId().toString(), bean);
					}
					//else simply add the current privilege to the existing set
					else
					{
						List<NameValueBean> privileges = bean.getPrivileges();
						if (!privileges.contains(nmv))
						{
							privileges.add(nmv);
						}
					}
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
		finally
		{
			try
			{
				hibernateDao.closeSession();
			}
			catch (DAOException e)
			{
				e.printStackTrace();
			}
		}

		return result;
	}

}
