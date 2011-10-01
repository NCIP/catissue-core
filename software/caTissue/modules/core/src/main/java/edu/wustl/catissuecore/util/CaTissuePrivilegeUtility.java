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
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.global.Variables;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.dao.daofactory.DAOConfigFactory;
import edu.wustl.dao.daofactory.IDAOFactory;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.security.global.Permissions;
import edu.wustl.security.privilege.PrivilegeCache;
import edu.wustl.security.privilege.PrivilegeManager;

/**
 * A AppUtility class to do higher level, caTissue-specific processing of
 * privileges
 *
 * @author juberahamad_patel
 *
 */

public final class CaTissuePrivilegeUtility
{

	private static final Logger LOGGER = Logger.getCommonLogger(CaTissuePrivilegeUtility.class);
	/*
	 * create singleton Object
	 */
	private static CaTissuePrivilegeUtility catissuePrivUtiliy = new CaTissuePrivilegeUtility();

	/*
	 * Private constructor
	 */
	private CaTissuePrivilegeUtility()
	{

	}

	/*
	 * returns single object
	 */
	public static CaTissuePrivilegeUtility getInstance()
	{
		return catissuePrivUtiliy;
	}

	/**
	 * get privileges for all CPs for the user associated with the given
	 * privilege cache.
	 *
	 * @param privilegeCache instance of PrivilegeCache
	 * @return list of SiteUserRolePrivilegeBean where each
	 *         SiteUserRolePrivilegeBean represents privileges on a CP
	 * @throws ApplicationException Application Exception
	 */
	public static Map<String, SiteUserRolePrivilegeBean> getCPPrivileges(
			PrivilegeCache privilegeCache) throws ApplicationException
	{
		Map<String, SiteUserRolePrivilegeBean> map = new HashMap<String, SiteUserRolePrivilegeBean>();
		IDAOFactory daoFact = DAOConfigFactory.getInstance().getDAOFactory(
				CommonServiceLocator.getInstance().getAppName());
		DAO dao = null;

		try
		{
			dao = daoFact.getDAO();
			// TODO remove the DB call
			User user = AppUtility.getUser(privilegeCache.getLoginName());

			// User is NULL for InActive / Closed users
			if (user != null)
			{

				Map<String, List<NameValueBean>> privileges = privilegeCache
						.getPrivilegesforPrefix(CollectionProtocol.class.getName() + "_");

				dao.openSession(null);
				user = (User) dao.retrieveById(User.class.getName(), user.getId());

				for (Entry<String, List<NameValueBean>> entry : privileges.entrySet())
				{
					setSitePrivileges(map, dao, user, entry);
				}
			}
		}
		catch (DAOException e)
		{
			CaTissuePrivilegeUtility.LOGGER.error(e.getMessage(), e);
//			e.printStackTrace();
		}
		finally
		{
			try
			{
				dao.closeSession();
			}
			catch (DAOException e)
			{
				CaTissuePrivilegeUtility.LOGGER.error(e.getMessage(), e);
//				e.printStackTrace();
			}
		}

		return map;

	}

	/**
	 * method sets Site privileges
	 * @param map
	 * @param dao
	 * @param user
	 * @param entry
	 * @throws DAOException
	 */
	private static void setSitePrivileges(Map<String, SiteUserRolePrivilegeBean> map, DAO dao,
			User user, Entry<String, List<NameValueBean>> entry) throws DAOException
	{
		SiteUserRolePrivilegeBean bean = new SiteUserRolePrivilegeBean();

		java.util.Scanner scanner = new java.util.Scanner(entry.getKey().substring(
				entry.getKey().lastIndexOf("_") + 1));

		Long cpId = null;

		if (scanner.hasNextLong())
		{
			cpId = Long.valueOf(scanner.nextLong());

			CollectionProtocol cpObj = (CollectionProtocol) dao.retrieveById(
					CollectionProtocol.class.getName(), cpId);

			List<Site> siteList = new ArrayList<Site>();

			updateSiteList(user, cpObj, siteList);

			bean.setCollectionProtocol(cpObj);
			bean.setUser(user);
			// bean.setSiteList(new ArrayList<Site>(cp.getSiteCollection()));
			bean.setSiteList(siteList);
			NameValueBean nmv = new NameValueBean();
			nmv.setName("Custom role");
			nmv.setValue("-1");
			bean.setRole(nmv);
			bean.setPrivileges(entry.getValue());

			// Added by Ravindra to handle bean for EDIT mode
			bean.setRowEdited(false);

			List<NameValueBean> privList = bean.getPrivileges();
			updateBean(bean, privList);
			map.put("CP_" + cpObj.getId(), bean);
		}
	}

	/**
	 * method updates siteList for setting site Privileges
	 *
	 * @param user
	 * @param cpObj
	 * @param siteList
	 */
	private static void updateSiteList(User user, CollectionProtocol cpObj, List<Site> siteList)
	{
		Set<Long> siteIds = new HashSet<Long>();
		for (Site site : user.getSiteCollection())
		{
			siteIds.add(site.getId());
		}

		for (Site site : cpObj.getSiteCollection())
		{
			if (siteIds.contains(site.getId()))
			{
				siteList.add(site);
			}
		}
	}

	/**
	 * update bean by setting siteList.
	 * @param bean
	 * @param privList
	 */
	private static void updateBean(SiteUserRolePrivilegeBean bean, List<NameValueBean> privList)
	{
		if (!privList.isEmpty())
		{
			NameValueBean nmv1 = privList.get(0);
			if (Permissions.READ_DENIED.equals(nmv1.getName()))
			{
				bean.setSiteList(new ArrayList<Site>());
			}
		}
	}

	/**
	 * get privileges for all Sites for the User associated with the given
	 * privilege cache
	 *
	 * @param privilegeCache
	 * @return list of SiteUserRolePrivilegeBean where each
	 *         SiteUserRolePrivilegeBean represents privileges on a Site
	 * @throws ApplicationException
	 */
	public static Map<String, SiteUserRolePrivilegeBean> getSitePrivileges(
			PrivilegeCache privilegeCache) throws ApplicationException
	{
		Map<String, SiteUserRolePrivilegeBean> map = new HashMap<String, SiteUserRolePrivilegeBean>();

		// TODO remove the DB call
		User user = AppUtility.getUser(privilegeCache.getLoginName());

		// User is NULL for InActive / Closed users
		if (user != null)
		{
			Map<String, List<NameValueBean>> privileges = privilegeCache
					.getPrivilegesforPrefix(Site.class.getName() + "_");

			IDAOFactory daoFact = DAOConfigFactory.getInstance().getDAOFactory(
					CommonServiceLocator.getInstance().getAppName());
			DAO dao = null;
			try
			{
				dao = daoFact.getDAO();
				dao.openSession(null);

				user = (User) dao.retrieveById(User.class.getName(), user.getId());

				for (Entry<String, List<NameValueBean>> entry : privileges.entrySet())
				{
					SiteUserRolePrivilegeBean bean = new SiteUserRolePrivilegeBean();

					java.util.Scanner scanner = new java.util.Scanner(entry.getKey().substring(
							entry.getKey().lastIndexOf("_") + 1));
					Long siteId = null;
					if (scanner.hasNextLong())
					{
						siteId = Long.valueOf(entry.getKey().substring(
								entry.getKey().lastIndexOf("_") + 1));
						Site site = null;
						site = (Site) dao.retrieveById(Site.class.getName(), siteId);
						List<Site> siteList = new ArrayList<Site>();
						siteList.add(site);
						bean.setSiteList(siteList);
						bean.setUser(user);
						updateCPStatus(privilegeCache, entry, bean, siteId);
						map.put(Constants.DOUBLE_QUOTES + site.getId(), bean);
					}
				}
			}
			finally
			{
				dao.closeSession();
			}
		}
		return map;
	}

	/**
	 * method updates CP status.
	 * @param privilegeCache
	 * @param entry
	 * @param bean
	 * @param siteId
	 */
	private static void updateCPStatus(PrivilegeCache privilegeCache,
			Entry<String, List<NameValueBean>> entry, SiteUserRolePrivilegeBean bean, Long siteId)
	{
		NameValueBean nmv = new NameValueBean();
		nmv.setName("Custom role");
		nmv.setValue("-1");
		bean.setRole(nmv);
		bean.setPrivileges(entry.getValue());

		// Added by Ravindra to handle bean for EDIT mode
		bean.setRowEdited(false);
		String PEName = Constants.getCurrentAndFuturePGAndPEName(siteId);
		if (privilegeCache.hasPrivilege(PEName))
		{
			bean.setAllCPChecked(true);
		}
	}

	/**
	 * get the privileges on all Sites and CPs for the user associated with the
	 * given privilege cache
	 *
	 * @param privilegeCache
	 * @return
	 * @throws ApplicationException
	 * @throws DAOException
	 */
	public static Map<String, SiteUserRolePrivilegeBean> getAllPrivileges(
			PrivilegeCache privilegeCache) throws ApplicationException
	{
		Map<String, SiteUserRolePrivilegeBean> map = null;
		try
		{
			map = getCPPrivileges(privilegeCache);
			map.putAll(getSitePrivileges(privilegeCache));

		}
		catch (DAOException e)
		{
			CaTissuePrivilegeUtility.LOGGER.error(e.getMessage(), e);
//			e.printStackTrace();
		}

		return map;

	}

	/**
	 * get a map of the privileges all the users have on a given CP.
	 *
	 * @param cpId
	 *            of the CP
	 * @return a map of login name and list of name value beans representing
	 *         privilege name and privilege id
	 */
	public static Map<String, SiteUserRolePrivilegeBean> getPrivilegesOnCP(Long cpId)
	{
		Map<String, SiteUserRolePrivilegeBean> privilegeOnCPMap = new HashMap<String, SiteUserRolePrivilegeBean>();

		String objectId = CollectionProtocol.class.getName() + "_" + cpId;

		IDAOFactory daoFact = DAOConfigFactory.getInstance().getDAOFactory(
				CommonServiceLocator.getInstance().getAppName());
		DAO hibernateDao = null;
		CollectionProtocol cpObj = null;
		// Added by Ravindra - contains user ids of those users who are asso. to
		// the CP
		// viz. PI of CP, co-ords of CP, users having explicit privileges on the
		// CP (users in cp.assignedProtocolUserCollection)
		Set<Long> validUserIds = new HashSet<Long>();

		try
		{
			hibernateDao = daoFact.getDAO();
			hibernateDao.openSession(null);
			cpObj = (CollectionProtocol) hibernateDao.retrieveById(CollectionProtocol.class.getName(),
					cpId);

			getValidUserIds(cpObj, validUserIds);
			validUserIds.remove(cpObj.getPrincipalInvestigator().getId());

			// To show details of Sites having Default functionality on CP
			Collection<Site> siteCollection = cpObj.getSiteCollection();
			Set<Long> validSiteIds = new HashSet<Long>();

			getValidSiteIds(siteCollection, validSiteIds);
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
					User user = AppUtility.getUser(userName);

					// User is NULL for InActive / Closed users
					if (user == null || !validUserIds.contains(user.getId()))
					{
						continue;
					}
					// Added by Ravindra
					// show Privileges of only those users who are associated to
					// that CP
					// if()
					// {
					// continue;
					// }
					SiteUserRolePrivilegeBean bean = privilegeOnCPMap.get(user.getId().toString());
					// if no privilege was so far detected for this user
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
						//For fixing bug #15397
						//						hibernateDao.openSession(null);
						for (Long siteId : siteSet)
						{
							Site site = (Site) hibernateDao.retrieveById(Site.class.getName(),
									siteId);
							if (validSiteIds.contains(siteId))
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
						privilegeOnCPMap.put(user.getId().toString(), bean);
					}
					// else simply add the current privilege to the existing set
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
			updatePrivilegeMap(privilegeOnCPMap, siteCollection, siteIdSetSpecific);
		}
		catch (Exception e)
		{
			CaTissuePrivilegeUtility.LOGGER.error(e.getMessage(), e);
//			e.printStackTrace();
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
				CaTissuePrivilegeUtility.LOGGER.error(e.getMessage(), e);
//				e.printStackTrace();
			}
		}

		return privilegeOnCPMap;
	}

	/**
	 * update privilegeOnCPMap by site and siteUserRolePrivileges
	 * @param privilegeOnCPMap
	 * @param siteCollection
	 * @param siteIdSetSpecific
	 */
	private static void updatePrivilegeMap(Map<String, SiteUserRolePrivilegeBean> privilegeOnCPMap,
			Collection<Site> siteCollection, Set<Long> siteIdSetSpecific)
	{
		if (siteCollection != null && !siteCollection.isEmpty())
		{
			for (Site site : siteCollection)
			{
				if (!siteIdSetSpecific.contains(site.getId()))
				{
					SiteUserRolePrivilegeBean siteUserRolePrivilegeBean = new SiteUserRolePrivilegeBean();
					List<Site> siteList = new ArrayList<Site>();
					siteList.add(site);
					siteUserRolePrivilegeBean.setSiteList(siteList);
					privilegeOnCPMap.put("SITE_" + site.getId(), siteUserRolePrivilegeBean);
				}
			}
		}
	}

	/**
	 * get valid siteIds for getting privileges on CP
	 * @param siteCollection
	 * @param validSiteIds
	 */
	private static void getValidSiteIds(Collection<Site> siteCollection, Set<Long> validSiteIds)
	{
		if (siteCollection != null)
		{
			for (Site site : siteCollection)
			{
				validSiteIds.add(site.getId());
			}
		}
	}

	/**
	 * get valid UserIds for getting privileges on CP
	 * @param cpObj instance of CollectionProtocol
	 * @param validUserIds
	 */
	private static void getValidUserIds(CollectionProtocol cpObj, Set<Long> validUserIds)
	{
		if (cpObj.getAssignedProtocolUserCollection() != null)
		{
			for (User user : cpObj.getAssignedProtocolUserCollection())
			{
				validUserIds.add(user.getId());
			}
		}
	}

	/**
	 * get a map of the privileges all the users have on a given CP.
	 *
	 * @param rowIdBeanMap
	 *
	 * @param id
	 *            of the CP
	 * @return a map of login name and list of name value beans representing
	 *         privilege name and privilege id
	 */
	public static Map<String, SiteUserRolePrivilegeBean> getAllCurrentAndFuturePrivilegeUsersOnSite(
			Long siteId, Long cpId, Map<String, SiteUserRolePrivilegeBean> result)
	{
		String objectId = Constants.getCurrentAndFuturePGAndPEName(siteId);

		IDAOFactory daoFact = DAOConfigFactory.getInstance().getDAOFactory(
				CommonServiceLocator.getInstance().getAppName());
		DAO hibernateDao = null;
		// Added by Ravindra - contains user ids of those users who are asso. to
		// the CP
		// viz. PI of CP, co-ords of CP, users having explicit privileges on the
		// CP (users in cp.assignedProtocolUserCollection)
		Set<Long> invalidUserIds = new HashSet<Long>();

		try
		{
			hibernateDao = daoFact.getDAO();
			hibernateDao.openSession(null);
			List siteList = new ArrayList();
			Site site = (Site) hibernateDao.retrieveById(Site.class.getName(), siteId);
			siteList.add(site);

			getInvalidUserIds(cpId, hibernateDao, invalidUserIds);

			hibernateDao.openSession(null);
			for (NameValueBean nmv : Variables.privilegeGroupingMap.get("CP"))
			{
				String privilegeName = nmv.getName();
				Set<String> users = PrivilegeManager.getInstance().getAccesibleUsers(objectId,
						privilegeName);

				for (String userName : users)
				{
					User user = AppUtility.getUser(userName);

					// User is NULL for InActive / Closed users
					if (user == null)
					{
						continue;
					}
					// Added by Ravindra
					// show Privileges of only those users who are associated to
					// that CP
					if (invalidUserIds.contains(user.getId()))
					{
						continue;
					}
					SiteUserRolePrivilegeBean bean = result.get(user.getId().toString());
					// if no privilege was so far detected for this user
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
					// else simply add the current privilege to the existing set
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
			CaTissuePrivilegeUtility.LOGGER.error(e.getMessage(), e);
//			e.printStackTrace();
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
				CaTissuePrivilegeUtility.LOGGER.error(e.getMessage(), e);
//				e.printStackTrace();

			}
		}

		return result;
	}

	/**
	 * method adds invaliduserIds.
	 * @param cpId collection protocol id
	 * @param hibernateDao DAO instance
	 * @param invalidUserIds set of invalid user's id's
	 * @throws DAOException instance of DAOException
	 */
	private static void getInvalidUserIds(Long cpId, DAO hibernateDao, Set<Long> invalidUserIds)
			throws DAOException
	{
		if (cpId != null)
		{
			CollectionProtocol cpObj;
			cpObj = (CollectionProtocol) hibernateDao.retrieveById(CollectionProtocol.class.getName(),
					cpId);

			if (cpObj.getAssignedProtocolUserCollection() != null)
			{
				for (User user : cpObj.getAssignedProtocolUserCollection())
				{
					invalidUserIds.add(user.getId());
				}
			}
			invalidUserIds.add(cpObj.getPrincipalInvestigator().getId());
		}
	}
}
