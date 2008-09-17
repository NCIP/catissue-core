/**
 * 
 */

package edu.wustl.catissuecore.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import edu.wustl.catissuecore.bizlogic.UserBizLogic;
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
import edu.wustl.common.security.PrivilegeManager;
import edu.wustl.common.util.Permissions;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.global.Variables;
import edu.wustl.common.util.logger.Logger;
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
	
					List<NameValueBean> privList = bean.getPrivileges();
					if(!privList.isEmpty())
					{
						NameValueBean nmv1 = privList.get(0);
						if(Permissions.READ_DENIED.equals(nmv1.getName()))
						{
							bean.setSiteList(new ArrayList<Site>());
						}
					}
					
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
					String PEName = Constants.getCurrentAndFuturePGAndPEName(id);
					if(privilegeCache.hasPrivilege(PEName))
					{
						bean.setAllCPChecked(true);
					}
	
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
		CollectionProtocol cp = null;
		// Added by Ravindra - contains user ids of those users who are asso. to the CP
		// viz. PI of CP, co-ords of CP, users having explicit privileges on the CP (users in cp.assignedProtocolUserCollection)
		Set<Long> validUserIds = new HashSet<Long>();
		
 		try
		{
			hibernateDao.openSession(null);
			cp = (CollectionProtocol) hibernateDao.retrieve(CollectionProtocol.class.getName(), id);
			
			
			if(cp.getAssignedProtocolUserCollection()!=null)
			{
				for(User user : cp.getAssignedProtocolUserCollection())
				{
					validUserIds.add(user.getId());
				}
			}
			validUserIds.remove(cp.getPrincipalInvestigator().getId());
			
			// To show details of Sites having Default functionality on CP
			Collection<Site> siteCollection = cp.getSiteCollection();
			Set<Long> validSiteIds = new HashSet<Long>();
			
			if(siteCollection !=null)
			{
				for(Site site : siteCollection)
				{
					validSiteIds.add(site.getId());
				}
			}
			Set<Long> siteIdSetSpecific = new HashSet<Long>(); 
			
			List<NameValueBean> allPrivileges = new ArrayList<NameValueBean>();
			allPrivileges.addAll(Variables.privilegeGroupingMap.get("CP"));
			allPrivileges.addAll(Variables.privilegeGroupingMap.get("SCIENTIST"));
			
			for (NameValueBean nmv : allPrivileges)
			{
				String privilegeName = nmv.getName();
				Set<String> users = PrivilegeManager.getInstance().getAccesibleUsers(objectId,
						privilegeName);

				for (String userName : users)
				{
					User user = Utility.getUser(userName);
					// Added by Ravindra 
					// show Privileges of only those users who are associated to that CP
					if(!validUserIds.contains(user.getId()))
					{
						continue;
					}
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
							if(validSiteIds.contains(siteId))
							{
								siteList.add(site);
							}
							siteIdSetSpecific.add(siteId);
						}

						bean.setSiteList(siteList);

						bean.setPrivileges(privileges);
						
						// Added by Ravindra to handle bean for EDIT mode
						bean.setRowEdited(false);
						bean.setCustChecked(true);
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
			if(siteCollection != null && !siteCollection.isEmpty())
			{
				for(Site site : siteCollection)
				{
					if(!siteIdSetSpecific.contains(site.getId()))
					{
						SiteUserRolePrivilegeBean siteUserRolePrivilegeBean = new SiteUserRolePrivilegeBean();
						List<Site> siteList = new ArrayList<Site>();
						siteList.add(site);
						siteUserRolePrivilegeBean.setSiteList(siteList);
						result.put("SITE_"+site.getId(), siteUserRolePrivilegeBean);
					}
				}
			}
		}
		catch (Exception e)
		{
			Logger.out.debug(e.getMessage(), e);
			// return null;
		}
		finally
		{
			try
			{
				hibernateDao.closeSession();
			}
			catch (DAOException e)
			{
				Logger.out.debug(e.getMessage(), e);
			}
		}

		return result;
	}

    /**
     * get a map of the privileges all the users have on a given CP.
     * @param rowIdBeanMap 
     * 
     * @param id of the CP
     * @return a map of login name and list of name value beans representing privilege name and privilege id 
     * @throws CSException 
     */
    public static Map<String, SiteUserRolePrivilegeBean> getAllCurrentAndFuturePrivilegeUsersOnSite(Long siteId, Long cpId, Map<String, SiteUserRolePrivilegeBean> result)
    {
    	String objectId = Constants.getCurrentAndFuturePGAndPEName(siteId);

        AbstractDAO hibernateDao = DAOFactory.getInstance().getDAO(Constants.HIBERNATE_DAO);
        CollectionProtocol cp = null;
        // Added by Ravindra - contains user ids of those users who are asso. to the CP
        // viz. PI of CP, co-ords of CP, users having explicit privileges on the CP (users in cp.assignedProtocolUserCollection)
        Set<Long> invalidUserIds = new HashSet<Long>();
        
        try
        {
            hibernateDao.openSession(null);
            List siteList = new ArrayList();
            Site site = (Site) hibernateDao.retrieve(Site.class.getName(), siteId);
            siteList.add(site);
            if (cpId != null)
            {
                cp = (CollectionProtocol) hibernateDao.retrieve(CollectionProtocol.class.getName(), cpId);
                
                
                if(cp.getAssignedProtocolUserCollection()!=null)
                {
                    for(User user : cp.getAssignedProtocolUserCollection())
                    {
                        invalidUserIds.add(user.getId());
                    }
                }
                invalidUserIds.add(cp.getPrincipalInvestigator().getId());
            }
            hibernateDao.openSession(null);
            for (NameValueBean nmv : Variables.privilegeGroupingMap.get("CP"))
            {
                String privilegeName = nmv.getName();
                Set<String> users = PrivilegeManager.getInstance().getAccesibleUsers(objectId,
                        privilegeName);

                for (String userName : users)
                {
                    User user = Utility.getUser(userName);
                    // Added by Ravindra 
                    // show Privileges of only those users who are associated to that CP
                    if(invalidUserIds.contains(user.getId()))
                    {
                        continue;
                    }
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
                        boolean isPresent = false;
                        for (Site site1 : bean.getSiteList())
                        {
                        	if (site1.getId().equals(site.getId()))
                        	{
                        		isPresent = true;
                        	}
                        }
                        if (!isPresent)
                        {
                       bean.getSiteList().add(site);
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
