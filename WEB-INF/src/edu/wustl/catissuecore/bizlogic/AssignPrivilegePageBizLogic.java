
package edu.wustl.catissuecore.bizlogic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.wustl.catissuecore.actionForm.AssignPrivilegesForm;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.multiRepository.bean.SiteUserRolePrivilegeBean;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.util.global.Status;
import edu.wustl.common.util.global.Variables;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.security.exception.SMException;
import edu.wustl.security.global.ProvisionManager;
import edu.wustl.security.manager.ISecurityManager;
import edu.wustl.security.manager.SecurityManagerFactory;
import edu.wustl.security.privilege.PrivilegeUtility;
import gov.nih.nci.security.authorization.domainobjects.Privilege;
import gov.nih.nci.security.authorization.domainobjects.Role;
import gov.nih.nci.security.exceptions.CSException;
import gov.nih.nci.security.exceptions.CSObjectNotFoundException;

/**
 * AssignPrivilegePageBizLogic is a class which contains the implementations for
 * ShowAssignPrivilegePageAction.
 * 
 * @author jitendra_agrawal
 * @author vipin_bansal
 */

public class AssignPrivilegePageBizLogic extends CatissueDefaultBizLogic
{

	private static Logger logger = Logger.getCommonLogger(AssignPrivilegePageBizLogic.class);
	List<NameValueBean> scientistPrivilegesList = Variables.privilegeGroupingMap.get("SCIENTIST");

	public List getRecordNames(Set recordIds, AssignPrivilegesForm privilegesForm)
			throws BizLogicException
	{
		final List recordNames = new ArrayList();
		// Bug: 2508: Jitendra to display name in alphabetically order.
		if (!recordIds.isEmpty())
		{
			final Object[] whereColumn = new Long[recordIds.size()];
			final Iterator itr = recordIds.iterator();
			int i = 0;
			while (itr.hasNext())
			{
				final NameValueBean nameValueBean = (NameValueBean) itr.next();
				whereColumn[i] = Long.valueOf(nameValueBean.getValue());
				i++;
			}
			final String sourceObjectName = privilegesForm.getObjectType();
			final String[] selectColumnName = new String[2];
			final String[] whereColumnName = {"id"};
			final String[] whereColumnCondition = {"in"};
			final Object[] whereColumnValue = {whereColumn};

			if (privilegesForm.getObjectType().equals(
					"edu.wustl.catissuecore.domain.CollectionProtocol"))
			{
				selectColumnName[0] = "title";
				selectColumnName[1] = "id";
			}
			else
			{
				selectColumnName[0] = "name";
				selectColumnName[1] = "id";
			}
			final List list = this.retrieve(sourceObjectName, selectColumnName, whereColumnName,
					whereColumnCondition, whereColumnValue, null);

			if (!list.isEmpty())
			{
				for (i = 0; i < list.size(); i++)
				{
					final Object[] obj = (Object[]) list.get(i);
					recordNames.add(new NameValueBean(obj[0], obj[1]));
				}
			}
			Collections.sort(recordNames);
		}
		return recordNames;
	}

	/**
	 * Gets the list of Site objects to be shown on UI for assign privileges.
	 * 
	 * @param isToExcludeDisabled
	 * @return List<NameValueBean> of sites.
	 * @throws BizLogicException
	 */
	public List<NameValueBean> getSiteList(boolean isToExcludeDisabled,
			SessionDataBean sessionDataBean) throws BizLogicException
	{
		final String sourceObjectName = Site.class.getName();
		final String[] siteDisplayField = {"name"};
		final String valueField = "id";

		final String[] activityStatusArray = {Status.ACTIVITY_STATUS_DISABLED.toString(),
				Status.ACTIVITY_STATUS_CLOSED.toString()};

		List<NameValueBean> siteNameValueBeanList = null;
		final List<NameValueBean> tempSiteNameValueBeanList = new ArrayList<NameValueBean>();

		siteNameValueBeanList = new StorageContainerBizLogic().getRepositorySiteList(
				sourceObjectName, siteDisplayField, valueField, activityStatusArray,
				isToExcludeDisabled);

		// To remove 1st row which contains "Select" & "-1"
		siteNameValueBeanList.remove(0);
		tempSiteNameValueBeanList.addAll(siteNameValueBeanList);

		if (sessionDataBean != null && !sessionDataBean.isAdmin())
		{
			final Set<Long> idSet = new UserBizLogic().getRelatedSiteIds(sessionDataBean
					.getUserId());

			for (final NameValueBean nvb : siteNameValueBeanList)
			{
				final Long siteId = Long.valueOf(nvb.getValue());
				if (!idSet.contains(siteId))
				{
					tempSiteNameValueBeanList.remove(nvb);
				}
			}
			return tempSiteNameValueBeanList;
		}
		return siteNameValueBeanList;
	}

	public List<NameValueBean> getCPList(boolean isToExcludeDisabled) throws BizLogicException
	{
		final String sourceObjectName = CollectionProtocol.class.getName();
		final String[] siteDisplayField = {"shortTitle"};
		final String valueField = "id";

		final String[] activityStatusArray = {Status.ACTIVITY_STATUS_DISABLED.toString(),
				Status.ACTIVITY_STATUS_CLOSED.toString()};
		final String joinCondition = Constants.AND_JOIN_CONDITION;
		final String separatorBetweenFields = ", ";

		final String[] whereColumnName = new String[]{Status.ACTIVITY_STATUS.toString()};
		final String[] whereColumnCondition = new String[]{"not in"};
		final Object[] whereColumnValue = {activityStatusArray};

		List<NameValueBean> cpNameValueBeanList;

		cpNameValueBeanList = this.getList(sourceObjectName, siteDisplayField, valueField,
				whereColumnName, whereColumnCondition, whereColumnValue, joinCondition,
				separatorBetweenFields, isToExcludeDisabled);

		cpNameValueBeanList = this.removeSelect(cpNameValueBeanList);
		Collections.sort(cpNameValueBeanList);
		return cpNameValueBeanList;
	}

	/**
	 * Gets list of users when page is loaded
	 * 
	 * @param isToExcludeDisabled
	 * @return List of user NameVAlueBean objects.
	 * @throws BizLogicException
	 */
	public List<NameValueBean> getUserList(boolean isToExcludeDisabled) throws BizLogicException
	{
		final String sourceObjectName = User.class.getName();
		final String[] userDisplayField = {"lastName", "firstName"};
		final String valueField = "id";

		final String[] activityStatusArray = {Status.ACTIVITY_STATUS_DISABLED.toString(),
				Status.ACTIVITY_STATUS_CLOSED.toString()};
		final String joinCondition = Constants.AND_JOIN_CONDITION;
		final String separatorBetweenFields = ", ";

		final String[] whereColumnName = new String[]{Status.ACTIVITY_STATUS.toString()};
		final String[] whereColumnCondition = new String[]{"not in"};
		final Object[] whereColumnValue = {activityStatusArray};

		List<NameValueBean> userNameValueBeanList = new ArrayList<NameValueBean>();

		userNameValueBeanList = this.getList(sourceObjectName, userDisplayField, valueField,
				whereColumnName, whereColumnCondition, whereColumnValue, joinCondition,
				separatorBetweenFields, isToExcludeDisabled);

		userNameValueBeanList = this.removeSelect(userNameValueBeanList);

		return userNameValueBeanList;

	}

	/**
	 * Gets list of roles when page is loaded
	 * 
	 * @return List of role NameValueBean objects.
	 * @throws BizLogicException
	 */
	public List<NameValueBean> getRoleList(String pageOf) throws BizLogicException
	{
		// Sets the roleList attribute to be used in the Add/Edit User Page.
		List roleList;
		try
		{
			roleList = SecurityManagerFactory.getSecurityManager().getRoles();
		}
		catch (final SMException e)
		{
			AssignPrivilegePageBizLogic.logger.error(e.getMessage(), e);
			throw new BizLogicException(e.getErrorKey(), e, e.getMsgValues());
		}

		final List<NameValueBean> roleNameValueBeanList = new ArrayList<NameValueBean>();
		roleNameValueBeanList.add(0, new NameValueBean(Constants.SELECT_OPTION, "-1"));
		roleNameValueBeanList.add(new NameValueBean(Constants.CUSTOM_ROLE, "0"));

		if (roleList != null && !roleList.isEmpty())
		{
			final ListIterator iterator = roleList.listIterator();

			while (iterator.hasNext())
			{
				final Role role = (Role) iterator.next();
				final NameValueBean nameValueBean = new NameValueBean();
				if ((Constants.PAGE_OF_ASSIGN_PRIVILEGE).equalsIgnoreCase(pageOf))
				{
					if (!((Constants.ROLE_SUPER_ADMINISTRATOR).equals(role.getName()))
							&& !((Constants.ROLE_ADMINISTRATOR).equals(role.getName())))
					{
						nameValueBean.setName(role.getName());
						nameValueBean.setValue(String.valueOf(role.getId()));
						roleNameValueBeanList.add(nameValueBean);
					}
				}
				else if (pageOf != null)
				{
					if ((Constants.ROLE_SUPER_ADMINISTRATOR).equals(role.getName())
							&& (Constants.SUPER_ADMIN_USER).equals("" + role.getId()))
					{
						nameValueBean.setName(Constants.SUPERADMINISTRATOR);
					}
					else
					{
						nameValueBean.setName(role.getName());
					}
					nameValueBean.setValue(String.valueOf(role.getId()));
					roleNameValueBeanList.add(nameValueBean);
				}
			}
		}
		return roleNameValueBeanList;
	}

	/**
	 * Gets list of actions when page is loaded
	 * 
	 * @param isToExcludeDisabled
	 * @return List of user NameVAlueBean objects.
	 * @throws BizLogicException
	 */
	public List<NameValueBean> getActionList(boolean isToExcludeDisabled) throws BizLogicException
	{
		List<NameValueBean> privilegeNameValueBeanList = null;

		final List<NameValueBean> cpPrivilegesList = Variables.privilegeGroupingMap.get("CP");

		privilegeNameValueBeanList = this.getReformedNameValueBeanList(cpPrivilegesList);

		return privilegeNameValueBeanList;
	}

	public List<NameValueBean> getActionListForUserPage(boolean isToExcludeDisabled)
			throws BizLogicException
	{
		final List<NameValueBean> privilegeNameValueBeanList = new ArrayList<NameValueBean>();
		final List<NameValueBean> cpPrivilegesList = Variables.privilegeGroupingMap.get("CP");

		final List<NameValueBean> sitePrivilegesList = Variables.privilegeGroupingMap.get("SITE");

		final Set<NameValueBean> set = new HashSet<NameValueBean>();
		set.addAll(cpPrivilegesList);
		set.addAll(sitePrivilegesList);
		privilegeNameValueBeanList.addAll(set);

		final List<NameValueBean> privList = this
				.getReformedNameValueBeanList(privilegeNameValueBeanList);

		return privList;
	}

	/**
	 * Gives list of JSONObjects, having user NameValueBean assigned to selected
	 * sites as a response
	 * 
	 * @param selectedSitesList
	 * @param assignPrivilegePageBizLogic
	 * @return List<JSONObject> ,list of JSONObjects, having user NameValueBean
	 *         assigned to selected sites
	 * @throws JSONException
	 * @throws IOException
	 */
	public List<JSONObject> getUsersForThisSites(List<Long> selectedSitesList)
			throws BizLogicException, JSONException
	{
		List<JSONObject> arrayList = new ArrayList<JSONObject>();
		List<NameValueBean> usersList = new ArrayList<NameValueBean>();

		usersList = this.getUsersForSelectedSites(selectedSitesList);

		if (usersList != null && !usersList.isEmpty())
		{
			arrayList = this.getJSONObjListOfNameValue(usersList);
		}
		return arrayList;
	}

	/**
	 * Gives the list of user NameValueBeanObjects that are registered under the
	 * selected sites.
	 * 
	 * @param selectedSitesList
	 * @return List<NameValueBean> list of user NameValueBeanObjects .
	 */
	public List<NameValueBean> getUsersForSelectedSites(List<Long> selectedSitesList)
			throws BizLogicException
	{
		List<NameValueBean> list = new ArrayList<NameValueBean>();

		if (selectedSitesList != null && !selectedSitesList.isEmpty())
		{
			List<NameValueBean> userList2 = new ArrayList<NameValueBean>();

			long siteId = selectedSitesList.get(0);
			list = this.getUsersList(siteId);

			for (int i = 1; i < selectedSitesList.size(); i++)
			{
				siteId = selectedSitesList.get(i);
				userList2 = this.getUsersList(siteId);

				list = this.getCommonElesList(list, userList2);
			}
		}
		Collections.sort(list);
		return list;
	}

	/**
	 * Given a list selected sites , this method will return the list of users
	 * registered under them.
	 * 
	 * @param siteIds
	 * @return List of user registered under the given site
	 */
	public List<NameValueBean> getUsersList(long siteId) throws BizLogicException
	{

		DAO dao = null;
		final List<NameValueBean> nameValuBeanList = new ArrayList<NameValueBean>();
		try
		{

			dao = this.openDAOSession(null);
			final Object obj = dao.retrieveById(Site.class.getName(), Long.valueOf(siteId));
			final Site site = (Site) obj;
			final Collection<User> userCollection = site.getAssignedSiteUserCollection();
			for (final User user : userCollection)
			{
				final NameValueBean valueBean = new NameValueBean();
				//valueBean.setName(user.getLastName()+","+user.getFirstName());
				valueBean.setName(user.getLoginName());
				valueBean.setValue(String.valueOf(user.getId()));
				nameValuBeanList.add(valueBean);
			}
		}
		catch (final DAOException e)
		{
			AssignPrivilegePageBizLogic.logger.error(e.getMessage(), e);
			throw new BizLogicException(e.getErrorKey(), e, e.getMsgValues());

		}
		finally
		{
			this.closeDAOSession(dao);

		}

		return nameValuBeanList;
	}

	public List<JSONObject> getCPsForThisSites(List<Long> selectedSitesList)
			throws BizLogicException, JSONException
	{
		List<JSONObject> arrayList = new ArrayList<JSONObject>();
		List<NameValueBean> cpsList = new ArrayList<NameValueBean>();

		if (selectedSitesList != null && !selectedSitesList.isEmpty())
		{
			cpsList = this.getCPsForSelectedSites(selectedSitesList);
		}
		else
		{
			cpsList = this.getCPList(false);
		}
		Collections.sort(cpsList);
		arrayList = this.getJSONObjListOfNameValue(cpsList);

		return arrayList;
	}

	public List<NameValueBean> getCPsForSelectedSites(List<Long> selectedSitesList)
			throws BizLogicException
	{
		List<NameValueBean> list = new ArrayList<NameValueBean>();

		if (selectedSitesList != null && !selectedSitesList.isEmpty())
		{
			List<NameValueBean> cpList2 = new ArrayList<NameValueBean>();

			long siteId = selectedSitesList.get(0);
			list = this.getCPsList(siteId);

			for (int i = 1; i < selectedSitesList.size(); i++)
			{
				siteId = selectedSitesList.get(i);
				cpList2 = this.getCPsList(siteId);

				list = this.getCommonElesList(list, cpList2);
			}
		}
		Collections.sort(list);
		return list;
	}

	public List<NameValueBean> getCPsList(long siteId) throws BizLogicException
	{
		DAO dao = null;
		final List<NameValueBean> nameValuBeanList = new ArrayList<NameValueBean>();
		try
		{
			dao = this.openDAOSession(null);
			final Object obj = dao.retrieveById(Site.class.getName(), siteId);
			final Site site = (Site) obj;
			final Collection<CollectionProtocol> cpCollection = site
					.getCollectionProtocolCollection();
			for (final CollectionProtocol colectionProtocol : cpCollection)
			{
				final NameValueBean valueBean = new NameValueBean();
				valueBean.setName(colectionProtocol.getShortTitle());
				valueBean.setValue(String.valueOf(colectionProtocol.getId()));
				nameValuBeanList.add(valueBean);
			}
		}
		catch (final DAOException e)
		{
			AssignPrivilegePageBizLogic.logger.error(e.getMessage(), e);
			e.printStackTrace() ;
			throw new BizLogicException(e.getErrorKey(), e, e.getMsgValues());
		}
		finally
		{
			this.closeDAOSession(dao);
		}
		return nameValuBeanList;
	}

	/**
	 * Gives list of JSONObjects, having action NameValueBean assigned to
	 * selected role as a response
	 * 
	 * @param roleId
	 * @param assignPrivilegePageBizLogic
	 * @return List<JSONObject> ,list of JSONObjects, having action
	 *         NameValueBean assigned to selected role
	 * @throws JSONException
	 * @throws BizLogicException
	 */
	public List<JSONObject> getActionsForThisRole(String roleId, String pageOf,
			List<Long> selectedSiteIds, List<Long> selectedCPIds, boolean isAllCPChecked)
			throws JSONException, BizLogicException
	{
		List<NameValueBean> actionList = new ArrayList<NameValueBean>();
		List<NameValueBean> selectedActionsList = null;
		List<NameValueBean> cpList = null;
		if (pageOf != null && pageOf.equalsIgnoreCase(Constants.PAGE_OF_ASSIGN_PRIVILEGE))
		{
			if ("7".equals(roleId))
			{
				actionList = this.getReformedNameValueBeanList(this.scientistPrivilegesList);
				selectedActionsList = new ArrayList<NameValueBean>();

				// cpList = getCPList(false);
			}
			else
			{
				actionList = this.getActionList(false);
				if (!"0".equals(roleId))
				{
					selectedActionsList = this.getActionsList(roleId);
				}
				else
				{
					selectedActionsList = new ArrayList<NameValueBean>();
				}
			}
		}
		else if (pageOf != null)
		{
			if ("7".equals(roleId))
			{
				actionList = this.getReformedNameValueBeanList(this.scientistPrivilegesList);
				selectedActionsList = new ArrayList<NameValueBean>();

				cpList = this.getCPList(false);
			}
			else
			{
				if ((selectedCPIds != null) && !selectedCPIds.isEmpty() && selectedCPIds.size() > 0)
				{
					actionList = this.getReformedNameValueBeanList(Variables.privilegeGroupingMap
							.get("CP"));
				}

				else if ((selectedSiteIds != null) && !selectedSiteIds.isEmpty()
						&& selectedSiteIds.size() > 0)
				{
					if (isAllCPChecked)
					{
						actionList = this.getActionListForUserPage(false);
					}
					else
					{
						actionList = this
								.getReformedNameValueBeanList(Variables.privilegeGroupingMap
										.get("SITE"));
					}
				}
				else
				{
					actionList = this.getActionListForUserPage(false);
				}
				if (!"0".equals(roleId))
				{
					selectedActionsList = this.getActionsList(roleId);
				}
				else
				{
					selectedActionsList = new ArrayList<NameValueBean>();
				}
			}
		}
		// selectedActionsList = getActionsList(roleId);

		final List<JSONObject> arrayList = new ArrayList<JSONObject>();
		final JSONObject jsonObject = new JSONObject();

		final List<JSONObject> actionJsonArray = this.getJSONObjListOfNameValue(actionList);
		jsonObject.put("actionJsonArray", actionJsonArray);

		final List<JSONObject> selectedActionArray = this
				.getJSONObjListOfNameValue(selectedActionsList);
		jsonObject.put("selectedActionArray", selectedActionArray);

		final List<JSONObject> cpJsonArray = this.getJSONObjListOfNameValue(cpList);
		jsonObject.put("cpJsonArray", cpJsonArray);

		arrayList.add(jsonObject);

		return arrayList;
	}

	/**
	 * @param actionList
	 */
	// Remove underscore .Use Utility.getDisplayLabelForUnderscore method
	private List<NameValueBean> getReformedNameValueBeanList(List<NameValueBean> list)
	{
		List<NameValueBean> reformedList = null;

		if (list != null && !list.isEmpty() && list.size() > 0)
		{
			reformedList = new ArrayList<NameValueBean>();

			for (final NameValueBean nmv : list)
			{
				final NameValueBean tempNameValueBean = new NameValueBean(AppUtility
						.getDisplayLabelForUnderscore(nmv.getName()), nmv.getValue());
				reformedList.add(tempNameValueBean);
			}
		}
		return reformedList;
	}

	public List<JSONObject> getActionsForThisSites(String selectedRoleId, boolean isAllCPChecked)
			throws BizLogicException, JSONException
	{
		List<NameValueBean> actionList = null;
		if (isAllCPChecked)
		{
			actionList = this.getActionListForUserPage(false);
		}
		else
		{
			actionList = this.getReformedNameValueBeanList(Variables.privilegeGroupingMap
					.get("SITE"));
		}
		final List<NameValueBean> selectedActionsList = new ArrayList<NameValueBean>();

		if (selectedRoleId != null && selectedRoleId != ""
				&& !selectedRoleId.equalsIgnoreCase("-1"))
		{
			// List<NameValueBean> actionsListForSelRole =
			// getActionsForSelRole(selectedRoleId);
			final List<NameValueBean> actionsListForSelRole = this.getActionsList(selectedRoleId);

			if (actionsListForSelRole != null && !actionsListForSelRole.isEmpty())
			{
				for (int count = 0; count < actionsListForSelRole.size(); count++)
				{
					final NameValueBean nameValueBean = (NameValueBean) actionsListForSelRole
							.get(count);
					if (actionList.contains(nameValueBean))
					{
						selectedActionsList.add(nameValueBean);
					}
				}
			}
		}

		final List<JSONObject> arrayList = new ArrayList<JSONObject>();
		final JSONObject jsonObject = new JSONObject();

		final List<JSONObject> actionJsonArray = this.getJSONObjListOfNameValue(actionList);
		jsonObject.put("actionJsonArray", actionJsonArray);

		final List<JSONObject> selectedActionArray = this
				.getJSONObjListOfNameValue(selectedActionsList);
		jsonObject.put("selectedActionArray", selectedActionArray);

		arrayList.add(jsonObject);

		return arrayList;
	}

	public List<JSONObject> getActionsForThisCPs(String selectedRoleId) throws BizLogicException,
			JSONException
	{
		List<NameValueBean> selectedActionsList = null;
		List<NameValueBean> actionList = null;
		if ("7".equals(selectedRoleId))
		{
			actionList = this.getReformedNameValueBeanList(this.scientistPrivilegesList);
		}
		else
		{
			selectedActionsList = new ArrayList<NameValueBean>();
			actionList = this
					.getReformedNameValueBeanList(Variables.privilegeGroupingMap.get("CP"));

			if (selectedRoleId != null && selectedRoleId != ""
					&& !selectedRoleId.equalsIgnoreCase("-1")
					&& !selectedRoleId.equalsIgnoreCase("0"))
			{
				final List<NameValueBean> actionsListForSelRole = this
						.getActionsList(selectedRoleId);

				if (actionsListForSelRole != null && !actionsListForSelRole.isEmpty())
				{
					for (int count = 0; count < actionsListForSelRole.size(); count++)
					{
						final NameValueBean nameValueBean = (NameValueBean) actionsListForSelRole
								.get(count);
						if (actionList.contains(nameValueBean))
						{
							selectedActionsList.add(nameValueBean);
						}
					}
				}
			}
		}
		final List<JSONObject> arrayList = new ArrayList<JSONObject>();
		final JSONObject jsonObject = new JSONObject();

		final List<JSONObject> actionJsonArray = this.getJSONObjListOfNameValue(actionList);
		jsonObject.put("actionJsonArray", actionJsonArray);

		final List<JSONObject> selectedActionArray = this
				.getJSONObjListOfNameValue(selectedActionsList);
		jsonObject.put("selectedActionArray", selectedActionArray);

		arrayList.add(jsonObject);

		return arrayList;
	}

	/**
	 * Given a selected role this method will return the list of actions under
	 * this role.
	 * 
	 * @param role
	 * @return List of actions assigned to the selected role
	 */
	public List<NameValueBean> getActionsList(String roleId) throws BizLogicException
	{
		List<NameValueBean> nameValueBeanList = new ArrayList<NameValueBean>();
		if (roleId != null && roleId != "" && !roleId.equalsIgnoreCase("-1"))
		{
			Set<Privilege> actions;
			final PrivilegeUtility privilegeUtility = new PrivilegeUtility();
			if ("7".equals(roleId))
			{
				nameValueBeanList = this.getReformedNameValueBeanList(this.scientistPrivilegesList);
			}
			else
			{
				try
				{
					actions = privilegeUtility.getRolePrivileges(roleId);
					for (final Privilege action : actions)
					{
						final NameValueBean valueBean = new NameValueBean();
						valueBean
								.setName(AppUtility.getDisplayLabelForUnderscore(action.getName()));
						valueBean.setValue(String.valueOf(action.getId()));
						nameValueBeanList.add(valueBean);
					}
				}
				catch (final SMException e)
				{
					AssignPrivilegePageBizLogic.logger.error(e.getMessage(), e);
					e.printStackTrace();
					throw this.getBizLogicException(e, "error.privileges.from.given.role", roleId);
				}
			}
		}
		// List<NameValueBean> actionsListForSelRole=new
		// ArrayList<NameValueBean>();
		// List<NameValueBean> actionsListForSelSites=new
		// ArrayList<NameValueBean>();

		/*
		 * if(role!=null&& role!=""&& ! role.equalsIgnoreCase("-1")) {
		 * actionsListForSelRole = getActionsForSelRole(role); }
		 * if((selectedSiteIds!=null)&& ! selectedSiteIds.isEmpty()&&
		 * selectedSiteIds.size()>0) { actionsListForSelSites =
		 * getActionsForSelSites(); } if(role!=null &&
		 * ((selectedSiteIds!=null)&& ! selectedSiteIds.isEmpty()&&
		 * selectedSiteIds.size()>0)) { for(int
		 * count=0;count<actionsListForSelRole.size();count++) { NameValueBean
		 * nameValueBean=(NameValueBean)actionsListForSelRole.get(count);
		 * if(actionsListForSelSites.contains(nameValueBean)) {
		 * nameValueBeanList.add(nameValueBean); } } } else if(role!=null &&
		 * ((selectedSiteIds==null)|| selectedSiteIds.isEmpty()||
		 * selectedSiteIds.size()==0)) {
		 * nameValueBeanList=actionsListForSelRole; } else
		 * if((role==null||role=="") && ((selectedSiteIds!=null)&& !
		 * selectedSiteIds.isEmpty()&& selectedSiteIds.size()>0)) {
		 * nameValueBeanList=actionsListForSelSites; }
		 */
		return nameValueBeanList;

	}

	/**
	 * @param role
	 * @param nameValuBeanList
	 */
	/*
	 * public List<NameValueBean> getActionsForSelRole(String roleId) {
	 * List<NameValueBean> nameValuBeanList=new ArrayList<NameValueBean>();
	 * Set<Privilege> actions; PrivilegeUtility privilegeUtility = new
	 * PrivilegeUtility(); if("7".equals(roleId)) {
	 * nameValuBeanList=getReformedNameValueBeanList(scientistPrivilegesList); }
	 * else { try { actions = privilegeUtility.getRolePrivileges(roleId); for
	 * (Privilege action : actions) { NameValueBean valueBean=new
	 * NameValueBean();
	 * valueBean.setName(Utility.getDisplayLabelForUnderscore(action
	 * .getName())); valueBean.setValue(String.valueOf(action.getId()));
	 * nameValuBeanList.add(valueBean); } } catch (CSObjectNotFoundException e)
	 * { e.printStackTrace(); } catch (CSException e) { e.printStackTrace(); } }
	 * return nameValuBeanList; }
	 */
	/**
	 * Gets action data from request
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @return List<String> list of actionIds
	 */
	public List<String> getActionData(String actionIds)
	{
		String actionId;
		final List<String> actionIdsList = new ArrayList<String>();
		final StringTokenizer tokenizer = new StringTokenizer("" + actionIds, ",");
		while (tokenizer.hasMoreTokens())
		{
			actionId = tokenizer.nextToken();
			actionIdsList.add(actionId);
		}
		return actionIdsList;
	}

	/**
	 * Gives user related sites
	 * 
	 * @param userId
	 * @param siteIdsList
	 * @return List<Integer> list of siteIds related to selected user
	 */
	public List<Site> getUserSiteRelation(User user, List<Long> siteIdsList)
	{
		final List<Site> list = new ArrayList<Site>();
		final Collection<Site> siteCollection = user.getSiteCollection();
		if (siteCollection != null || !siteCollection.isEmpty())
		{
			for (final Site site : siteCollection)
			{
				if (siteIdsList.contains(site.getId()))
				{
					list.add(site);
				}
			}
		}

		return list;
	}

	/**
	 * Create the BeanObject
	 * 
	 * @param user
	 * @param userRelatedSites
	 * @param roleId
	 * @param actionIdsList
	 * @return SiteUserRolePrivilegeBean bean Object
	 */
	public SiteUserRolePrivilegeBean setUserPrivilegeSummary(User user,
			List<Site> userRelatedSites, NameValueBean roleNameValueBean,
			List<NameValueBean> actionBeanList, boolean isCustChecked)
	{
		final SiteUserRolePrivilegeBean surp = new SiteUserRolePrivilegeBean();

		// NameValueBean roleNameValueBean=new NameValueBean();
		// roleNameValueBean.setName(role.getName());
		// roleNameValueBean.setValue(role.getId());
		surp.setRole(roleNameValueBean);
		surp.setUser(user);
		surp.setSiteList(userRelatedSites);
		surp.setPrivileges(actionBeanList);
		surp.setCustChecked(isCustChecked);

		return surp;
	}

	/**
	 * gives JSONObject as a response for summary
	 * 
	 * @param userRelatedSites
	 * @param rowId
	 * @param user
	 * @param roleId
	 * @param actionIdsList
	 * @return JSONObject for response
	 * @throws JSONException
	 * @throws CSException
	 */
	public JSONObject getObjectForUPSummary(String rowId, NameValueBean roleNameValueBean,
			List<Site> userRelatedSites, User user, List<NameValueBean> actionBeanList,
			boolean isCustChecked) throws JSONException, BizLogicException
	{
		final JSONObject jsonobject = new JSONObject();

		// for role
		String roleName = "";
		if (roleNameValueBean != null)
		{
			roleName = roleNameValueBean.getName();
		}
		else if (!isCustChecked && roleNameValueBean == null)
		{
			roleName = Constants.DEFAULT_ROLE;
		}

		// for sites
		final StringBuffer sbForSites = new StringBuffer();
		if (userRelatedSites != null && userRelatedSites.size() > 0)
		{
			for (int i = 0; i < userRelatedSites.size(); i++)
			{
				if (i > 0)
				{
					sbForSites.append(",");
				}
				sbForSites.append(userRelatedSites.get(i).getName());
			}
		}
		final String sites = sbForSites.toString();

		// for user
		String userName = "";
		if (user != null)
		{
			userName = user.getLoginName();
		}
		else if (!isCustChecked && user == null)
		{
			userName = Constants.ALL_DEFAULT_USERS;
		}

		// for actions
		final StringBuffer sbForActions = new StringBuffer();
		String actionName = "";
		if (actionBeanList != null && !actionBeanList.isEmpty())
		{
			for (int i = 0; i < actionBeanList.size(); i++)
			{
				actionName = actionBeanList.get(i).getName();
				if (i > 0)
				{
					sbForActions.append(",");
				}
				// if((actionName).equals("PHI_ACCESS"))
				// {
				//sbForActions.append(ApplicationProperties.getValue(actionName)
				// );
				// }
				// else
				// {
				sbForActions.append(actionName);
				// }
			}
		}
		else if (!isCustChecked && (actionBeanList == null || actionBeanList.isEmpty()))
		{
			actionName = Constants.ALL_DEFAULT_PRIVILEGES;

			sbForActions.append(actionName);
		}
		final String actions = sbForActions.toString();

		jsonobject.append("roleName", roleName);
		jsonobject.append("userName", userName);
		jsonobject.append("sites", sites);
		jsonobject.append("actions", actions);
		jsonobject.append("rowId", rowId);

		return jsonobject;
	}

	/**
	 * Delete row from the map
	 * 
	 * @param deletedRowsArray
	 * @param rowIdObjectBeanMap
	 */
	public Map<String, SiteUserRolePrivilegeBean> deletePrivilege(
			Map<String, SiteUserRolePrivilegeBean> rowIdBeanMap, String deletedRowsArray,
			String operation)
	{
		String deletedRowId = "";
		SiteUserRolePrivilegeBean surp = null;
		final List<String> deletedRowsList = new ArrayList<String>();
		final StringTokenizer tokenizer = new StringTokenizer(deletedRowsArray, ",");
		while (tokenizer.hasMoreTokens())
		{
			deletedRowId = tokenizer.nextToken().intern();
			deletedRowsList.add(deletedRowId);
			surp = rowIdBeanMap.get(deletedRowId);
			if ((Constants.ADD).equalsIgnoreCase(operation)
					|| ((Constants.EDIT).equals(operation) && surp.isRowEdited()))
			{
				rowIdBeanMap.remove(deletedRowId);
			}
			else if ((Constants.EDIT).equals(operation))
			{
				surp.setRowDeleted(true);
				rowIdBeanMap.put(deletedRowId, surp);
			}
		}

		return rowIdBeanMap;
	}

	/**
	 * Gives Map having rowId and UsersummaryBean Object and returns a list
	 * containing rowId and userSummaryBean Object,used to send response.
	 * 
	 * @param rowIdBeanMap
	 * @param userIds
	 * @param siteIds
	 * @param roleId
	 * @param actionIds
	 * @return List<JSONObject> of userSummaryBean Objects
	 */
	public List<JSONObject> addPrivilege(Map<String, SiteUserRolePrivilegeBean> rowIdBeanMap,
			String userIds, String siteIds, String roleId, String actionIds, boolean isCustChecked,
			String operation) throws BizLogicException, JSONException, CSException
	{
		final List<JSONObject> listForUPSummary = new ArrayList<JSONObject>();

		final List<Long> siteIdsList = this.getInputData(siteIds);
		final List<Long> userIdsList = this.getInputData(userIds);
		final List<String> actionIdsList = this.getActionData(actionIds);

		DAO dao = null;

		try
		{
			dao = this.openDAOSession(null);
			JSONObject jsonObject = new JSONObject();
			if (!isCustChecked && (siteIdsList != null || !siteIdsList.isEmpty()))
			{
				// CaTissuePrivilegeUtility caTissuePrivilegeUtility = new
				// CaTissuePrivilegeUtility();
				for (int count = 0; count < siteIdsList.size(); count++)
				{
					final long siteId = siteIdsList.get(count);
					if (rowIdBeanMap == null)
					{
						rowIdBeanMap = new HashMap<String, SiteUserRolePrivilegeBean>();
					}

					if (userIdsList == null || userIdsList.isEmpty())
					{
						final String rowId = "Site_" + siteId;

						final Object object = dao.retrieveById(Site.class.getName(), siteId);
						final Site site = (Site) object;
						final List<Site> sites = new ArrayList<Site>();
						sites.add(site);
						final SiteUserRolePrivilegeBean surpBean = this.setUserPrivilegeSummary(
								null, sites, null, null, isCustChecked);

						this.updateRowInEditMode(rowIdBeanMap, operation, rowId);

						rowIdBeanMap.put(rowId, surpBean);

						jsonObject = this.getObjectForUPSummary(rowId, null, sites, null, null,
								isCustChecked);

						listForUPSummary.add(jsonObject);

					}

					// Map<String, SiteUserRolePrivilegeBean>
					// map=caTissuePrivilegeUtility
					// .getAllCurrentAndFuturePrivilegeUsersOnSite(siteId, null,
					// rowIdBeanMap);
					// rowIdBeanMap.putAll(map);

					// Set keySet = map.keySet();
					// Iterator iterator = keySet.iterator();
					//				
					// while(iterator.hasNext())
					// {
					// String rowId =(String)iterator.next();
					// SiteUserRolePrivilegeBean surp = map.get(rowId);
					// NameValueBean roleNameValueBean = surp.getRole();
					// List<Site> sites = surp.getSiteList();
					// List<NameValueBean> actionBeanList =
					// surp.getPrivileges();
					// User user = surp.getUser();
					// jsonObject=getObjectForUPSummary(rowId,roleNameValueBean,
					// sites,user,actionBeanList);
					// listForUPSummary.add(jsonObject);
					// }
				}
			}
			else
			{
				for (int k = 0; k < userIdsList.size(); k++)
				{
					String roleName = "";
					// Role role=null;
					NameValueBean roleNameValueBean = null;

					if (!("0").equalsIgnoreCase(roleId))
					{
						final ISecurityManager securityManager = SecurityManagerFactory
								.getSecurityManager();

						try
						{
							final Role role = ProvisionManager.getInstance()
									.getUserProvisioningManager().getRoleById(roleId);
							roleName = role.getName();
							roleNameValueBean = new NameValueBean(roleName, roleId);
						}
						catch (final CSException e)
						{
							AssignPrivilegePageBizLogic.logger.error(e.getMessage(), e);
							final ErrorKey errorKey = ErrorKey.getErrorKey("role.nt.retrieved");
							throw new BizLogicException(errorKey, e, "");
						}
					}
					else
					{
						roleName = "Custom";
						// role=new Role();
						// role.setName(roleName);
						// role.setId(new Long(roleId));
						roleNameValueBean = new NameValueBean(roleName, roleId);
					}

					final long userId = userIdsList.get(k);
					final Object object = dao.retrieveById(User.class.getName(), userId);
					final User user = (User) object;

					final List<Site> userRelatedSites = this.getUserSiteRelation(user, siteIdsList);

					final List<NameValueBean> actionBeanList = this
							.getPrivilegesNameValueBeanList(actionIdsList);

					final SiteUserRolePrivilegeBean surpBean = this.setUserPrivilegeSummary(user,
							userRelatedSites, roleNameValueBean, actionBeanList, isCustChecked);

					final String rowId = "" + userId;

					this.updateRowInEditMode(rowIdBeanMap, operation, rowId);

					rowIdBeanMap.put(rowId, surpBean);

					jsonObject = this.getObjectForUPSummary(rowId, roleNameValueBean,
							userRelatedSites, user, actionBeanList, isCustChecked);

					listForUPSummary.add(jsonObject);
				}

			}
		}
		catch (final DAOException daoExp)
		{
			AssignPrivilegePageBizLogic.logger.error(daoExp.getMessage(), daoExp);
			daoExp.printStackTrace();
			daoExp.printStackTrace();
			// ErrorKey errorKey = ErrorKey.getErrorKey("dao.error");
			throw new BizLogicException(daoExp.getErrorKey(), daoExp, daoExp.getMsgValues());
		}
		catch (final SMException smExp)
		{
			AssignPrivilegePageBizLogic.logger.error(smExp.getMessage(), smExp);
			smExp.printStackTrace();
			throw this.getBizLogicException(smExp, smExp.getErrorKeyName(), smExp.getMsgValues());
		}
		finally
		{
			this.closeDAOSession(dao);
		}
		return listForUPSummary;
	}

	/**
	 * @param rowIdBeanMap
	 * @param operation
	 * @param rowId
	 */
	private void updateRowInEditMode(Map<String, SiteUserRolePrivilegeBean> rowIdBeanMap,
			String operation, String rowId)
	{
		if ((Constants.EDIT).equalsIgnoreCase(operation))
		{
			final SiteUserRolePrivilegeBean tempBean = rowIdBeanMap.get(rowId);
			if (rowIdBeanMap.containsKey(rowId) && !tempBean.isRowEdited())
			{
				final String updatedRowId = "Deleted_" + rowId;
				tempBean.setRowDeleted(true);
				rowIdBeanMap.remove(rowId);
				rowIdBeanMap.put(updatedRowId, tempBean);
			}
		}
	}

	/**
	 * Method to show userPrivilege summary details on tab change
	 * 
	 * @param session
	 * @param dao
	 * @return List of String array
	 * @throws CSObjectNotFoundException
	 * @throws CSException
	 * @throws BizLogicException
	 */
	public List<String[]> privilegeDataOnTabSwitch(Map<String, SiteUserRolePrivilegeBean> map,
			String pageOf) throws BizLogicException
	{
		List<String[]> list = null;

		if (map != null && !map.isEmpty() && map.size() > 0)
		{
			list = new ArrayList<String[]>();

			final int mapSize = map.size();
			final Object[] surpArray = map.values().toArray();
			final Object[] keyArray = map.keySet().toArray();
			SiteUserRolePrivilegeBean bean = null;

			for (int j = 0; j < mapSize; j++)
			{
				final String[] array = new String[5];
				String rowId = "";

				bean = (SiteUserRolePrivilegeBean) surpArray[j];

				if (!bean.isRowDeleted())
				{
					// for row id
					for (final Object element : keyArray)
					{
						final SiteUserRolePrivilegeBean beanForKey = map.get(element);
						if (bean == beanForKey)
						{
							rowId = (String) element;
						}
					}

					// for role
					String roleName = "";
					if (bean.getRole() != null)
					{
						roleName = bean.getRole().getName();
					}
					else if (!bean.isCustChecked())
					{
						roleName = Constants.DEFAULT_ROLE;
					}

					// for site
					final String sites = this.displaySiteNames(bean);

					// for privileges
					final String actionNames = this.displayPrivilegesNames(bean);

					// if((!
					// (Constants.PAGE_OF_USER).equalsIgnoreCase(pageOf))&&
					// !((Constants.PAGE_OF_USER_PROFILE).equals(pageOf)))
					if ((pageOf != null && (Constants.PAGE_OF_ASSIGN_PRIVILEGE)
							.equalsIgnoreCase(pageOf)))
					{
						// for user
						String userName = "";
						if (bean.getUser() != null)
						{
							userName = bean.getUser().getFirstName();
						}
						else if (!bean.isCustChecked())
						{
							userName = Constants.ALL_DEFAULT_USERS;
						}
						array[0] = userName;
					}
					else if (pageOf != null)
					{
						// for CP
						if (bean.getCollectionProtocol() != null)
						{
							final String cpName = bean.getCollectionProtocol().getShortTitle();
							if (cpName != null && !cpName.equalsIgnoreCase(""))
							{
								array[0] = cpName;
							}
							else
							{
								array[0] = Constants.NA;
							}
						}
						else if (bean.isAllCPChecked())
						{
							array[0] = Constants.ALL_CURRENT_AND_FUTURE;
						}
						else if (!bean.isAllCPChecked())
						{
							array[0] = Constants.NA;
						}
					}

					array[1] = sites;
					array[2] = roleName;
					array[3] = actionNames;
					array[4] = rowId;
					list.add(j, array);
				}
			}
		}
		return list;
	}

	/**
	 * @param bean
	 * @return
	 * @throws BizLogicException
	 */
	public String displayPrivilegesNames(SiteUserRolePrivilegeBean bean) throws BizLogicException
	{
		final StringBuffer sbForActions = new StringBuffer();
		final List<NameValueBean> privilegesList = bean.getPrivileges();
		if (privilegesList != null && !privilegesList.isEmpty())
		{
			NameValueBean nameValueBean = null;
			String actionName = "";
			for (int i = 0; i < privilegesList.size(); i++)
			{
				nameValueBean = privilegesList.get(i);
				actionName = nameValueBean.getName();
				if (i > 0)
				{
					sbForActions.append(",");
				}
				sbForActions.append(AppUtility.getDisplayLabelForUnderscore(actionName));

				// if((actionName).equals("PHI_ACCESS"))
				// {
				//sbForActions.append(ApplicationProperties.getValue(actionName)
				// );
				// }
				// else
				// {
				// sbForActions.append(Utility.getDisplayLabelForUnderscore(
				// actionName));
				// }
				//		 
			}
		}
		else if (!bean.isCustChecked())
		{
			sbForActions.append(Constants.ALL_DEFAULT_PRIVILEGES);
		}
		final String actionNames = sbForActions.toString();
		return actionNames;
	}

	/**
	 * @param dao
	 * @param bean
	 * @return
	 * @throws BizLogicException
	 */
	public String displaySiteNames(SiteUserRolePrivilegeBean bean) throws BizLogicException
	{
		final StringBuffer sbForSitesNames = new StringBuffer();
		if (bean.getSiteList() != null && !bean.getSiteList().isEmpty())
		{
			for (int i = 0; i < bean.getSiteList().size(); i++)
			{
				if (i > 0)
				{
					sbForSitesNames.append(",");
				}

				final Site site = bean.getSiteList().get(i);
				sbForSitesNames.append(site.getName());
			}
		}

		final String sites = sbForSitesNames.toString();
		return sites;
	}

	public List<JSONObject> editPrivilege(Map<String, SiteUserRolePrivilegeBean> rowIdBeanMap,
			String selectedRow) throws JSONException, BizLogicException
	{
		final List<JSONObject> privilegeList = new ArrayList<JSONObject>();
		if (rowIdBeanMap != null && !rowIdBeanMap.isEmpty())
		{
			final SiteUserRolePrivilegeBean surp = rowIdBeanMap.get(selectedRow);

			final JSONObject jsonObject = new JSONObject();

			// for customize checkbox
			jsonObject.put("isCustChecked", surp.isCustChecked());

			// for site
			final JSONArray siteJsonArray = new JSONArray();

			if (surp.getSiteList() != null && !surp.getSiteList().isEmpty())
			{
				for (final Site site : surp.getSiteList())
				{
					final long siteId = site.getId();
					siteJsonArray.put(siteId);
				}
			}
			jsonObject.put("siteJsonArray", siteJsonArray);

			// for User
			if (surp.getUser() != null)
			{
				final long selectedUserId = surp.getUser().getId();
				jsonObject.append("selectedUserId", selectedUserId);
			}

			final List<Site> siteList = surp.getSiteList();
			final List<Long> selectedSitesList = new ArrayList<Long>();
			for (int z = 0; z < siteList.size(); z++)
			{
				selectedSitesList.add(siteList.get(z).getId());
			}

			final List<JSONObject> userNameValueBeanList = this
					.getUsersForThisSites(selectedSitesList);
			jsonObject.put("userJsonArray", userNameValueBeanList);

			// for Role
			String roleId = "";
			if (surp.getRole() != null)
			{
				roleId = surp.getRole().getValue();

				jsonObject.append("roleId", roleId);
			}

			// for Privileges
			List<JSONObject> selActionJsonArray = null;
			List<JSONObject> actionJsonArray = null;
			List<NameValueBean> actionList = null;
			final List<NameValueBean> privileges = surp.getPrivileges();

			if (("7").equals(roleId))
			{
				actionList = this.getReformedNameValueBeanList(this.scientistPrivilegesList);
			}
			else
			{
				actionList = this.getActionList(false);
			}

			if (privileges != null)
			{
				selActionJsonArray = this.getJSONObjListOfNameValue(privileges);
				jsonObject.put("selActionJsonArray", selActionJsonArray);
			}

			actionJsonArray = this.getJSONObjListOfNameValue(actionList);
			jsonObject.put("actionJsonArray", actionJsonArray);

			privilegeList.add(jsonObject);
		}
		return privilegeList;
	}

	/*
	 * public ActionErrors validationForAddPrivOnUserPage(String
	 * pageOf,List<Long> siteIdsList,List<Long> cpIdsList,String
	 * roleId,List<String> actionIdsList,boolean isAllCPChecked) { boolean
	 * errorFlagForSite=false; boolean errorFlagForAction=false; boolean
	 * errorFlagForRole=false; boolean errorFlagForCP=false; boolean
	 * tempFlagForSite = false; ActionErrors errors = new ActionErrors();
	 * if(actionIdsList!=null && !actionIdsList.isEmpty()) { for(String
	 * actionId:actionIdsList) { if(actionId.equals("12")) {
	 * tempFlagForSite=true; if(cpIdsList!=null && cpIdsList.size()==0) {
	 * errorFlagForCP=true; } } } } if((siteIdsList==null
	 * ||siteIdsList.isEmpty()||siteIdsList.size()==0) && !tempFlagForSite) {
	 * errorFlagForSite=true; } if(roleId==null||roleId.equals("")) {
	 * errorFlagForRole=true; } if(actionIdsList==null
	 * ||actionIdsList.isEmpty()||actionIdsList.size()==0) {
	 * errorFlagForAction=true; }
	 * if(errorFlagForSite==true||errorFlagForAction==
	 * true||errorFlagForRole==true||errorFlagForCP==true) { ActionError error =
	 * new ActionError("ShoppingCart.emptyCartTitle");
	 * errors.add(ActionErrors.GLOBAL_ERROR, error); } return errors; } public
	 * ActionErrors validationForAddPrivOnCPPage(String pageOf,List<Long>
	 * siteIdsList,List<Long> userIdsList,String roleId,List<String>
	 * actionIdsList) { boolean errorFlagForSite=false; boolean
	 * errorFlagForUser=false; boolean errorFlagForAction=false; boolean
	 * errorFlagForRole=false; ActionErrors errors = null; if(siteIdsList==null
	 * ||siteIdsList.isEmpty()||siteIdsList.size()==0) { errorFlagForSite=true;
	 * } if(userIdsList==null ||userIdsList.isEmpty()||userIdsList.size()==0) {
	 * errorFlagForUser = true; } if(roleId==null||roleId.equals("")) {
	 * errorFlagForRole=true; } if(actionIdsList==null
	 * ||actionIdsList.isEmpty()||actionIdsList.size()==0) {
	 * errorFlagForAction=true; }
	 * if(errorFlagForSite==true||errorFlagForUser==true
	 * ||errorFlagForAction==true||errorFlagForRole==true) { errors = new
	 * ActionErrors(); ActionError error = new
	 * ActionError("ShoppingCart.emptyCartTitle");
	 * errors.add(ActionErrors.GLOBAL_ERROR, error); } return errors; }
	 */
	// for user page--
	public List<JSONObject> addPrivilegeForUserPage(
			Map<String, SiteUserRolePrivilegeBean> rowIdBeanMap, String cpIds, String siteIds,
			String roleId, String actionIds, boolean isAllCPChecked, String operation)
			throws BizLogicException, JSONException, CSException
	{
		final List<JSONObject> listForUPSummary = new ArrayList<JSONObject>();
		List<Long> siteIdsList = new ArrayList<Long>();
		List<Long> cpIdsList = new ArrayList<Long>();
		List<String> actionIdsList = new ArrayList<String>();

		siteIdsList = this.getInputData(siteIds);
		cpIdsList = this.getInputData(cpIds);
		actionIdsList = this.getActionData(actionIds);
		DAO dao = null;

		try
		{
			dao = this.openDAOSession(null);
			if (cpIdsList != null && !cpIdsList.isEmpty())
			{
				for (int k = 0; k < cpIdsList.size(); k++)
				{
					String roleName = "";
					Role role = null;

					if (!("0").equalsIgnoreCase(roleId))
					{
						final ISecurityManager securityManager = SecurityManagerFactory
								.getSecurityManager();
						role = ProvisionManager.getInstance().getUserProvisioningManager()
								.getRoleById(roleId);
					}
					else
					{
						roleName = "Custom";
						role = new Role();
						role.setName(roleName);
						role.setId(new Long(roleId));
					}

					final long cpId = cpIdsList.get(k);
					final Object object = dao
							.retrieveById(CollectionProtocol.class.getName(), cpId);
					final CollectionProtocol collectionProtocol = (CollectionProtocol) object;

					List<Site> cpRelatedSites = null;

					if (siteIdsList != null && siteIdsList.size() > 0)
					{
						cpRelatedSites = this.getCPSiteRelationForUserPage(collectionProtocol,
								siteIdsList);
					}

					final List<NameValueBean> actionBeanList = this
							.getPrivilegesNameValueBeanList(actionIdsList);

					final SiteUserRolePrivilegeBean surpBean = this
							.setUserPrivilegeSummaryForUserPage(collectionProtocol, cpRelatedSites,
									role, actionBeanList, isAllCPChecked, operation);

					final String rowId = "CP_" + cpId;
					this.updateRowInEditMode(rowIdBeanMap, operation, rowId);
					rowIdBeanMap.put(rowId, surpBean);

					final JSONObject jsonObject = this.getObjectForUPSummaryForUserPage(rowId,
							role, cpRelatedSites, collectionProtocol, actionBeanList,
							isAllCPChecked);

					listForUPSummary.add(jsonObject);
				}
			}
			else
			{
				for (int k = 0; k < siteIdsList.size(); k++)
				{
					String roleName = "";
					Role role = null;

					if (!("0").equalsIgnoreCase(roleId))
					{
						final ISecurityManager securityManager = SecurityManagerFactory
								.getSecurityManager();
						role = ProvisionManager.getInstance().getUserProvisioningManager()
								.getRoleById(roleId);
						roleName = role.getName();

					}
					else
					{
						roleName = "Custom";
						role = new Role();
						role.setName(roleName);
						role.setId(new Long(roleId));
					}

					final long siteId = siteIdsList.get(k);
					final Object object = dao.retrieveById(Site.class.getName(), siteId);
					final Site site = (Site) object;
					final List<Site> siteLists = new ArrayList<Site>();
					siteLists.add(site);

					final List<NameValueBean> actionBeanList = this
							.getPrivilegesNameValueBeanList(actionIdsList);

					final SiteUserRolePrivilegeBean surpBean = this
							.setUserPrivilegeSummaryForUserPage(null, siteLists, role,
									actionBeanList, isAllCPChecked, operation);

					final String rowId = "" + siteId;

					this.updateRowInEditMode(rowIdBeanMap, operation, rowId);

					rowIdBeanMap.put(rowId, surpBean);

					final JSONObject jsonObject = this.getObjectForUPSummaryForUserPage(rowId,
							role, siteLists, null, actionBeanList, isAllCPChecked);

					listForUPSummary.add(jsonObject);
				}
			}
		}
		catch (final ApplicationException e)
		{
			AssignPrivilegePageBizLogic.logger.error(e.getMessage(), e);
			e.printStackTrace();
			throw new BizLogicException(e.getErrorKey(), e, e.getMsgValues());
		}
		finally
		{
			this.closeDAOSession(dao);

		}
		return listForUPSummary;
	}

	public List<Site> getCPSiteRelationForUserPage(CollectionProtocol collectionProtocol,
			List<Long> siteIdsList)
	{
		final List<Site> list = new ArrayList<Site>();
		final Collection<Site> siteCollection = collectionProtocol.getSiteCollection();
		if (siteCollection != null || !siteCollection.isEmpty())
		{
			for (final Site site : siteCollection)
			{
				if (siteIdsList.contains(site.getId()))
				{
					list.add(site);
				}
			}
		}

		return list;
	}

	// Returns SiteUserRolePrivilegeBean Bean for user page
	public SiteUserRolePrivilegeBean setUserPrivilegeSummaryForUserPage(
			CollectionProtocol collectionProtocol, List<Site> cpRelatedSites, Role role,
			List<NameValueBean> actionBeanList, boolean isAllCPChecked, String operation)
	{
		final SiteUserRolePrivilegeBean surp = new SiteUserRolePrivilegeBean();

		surp.setCollectionProtocol(collectionProtocol);

		final NameValueBean roleNameValueBean = new NameValueBean();
		roleNameValueBean.setName(role.getName());
		roleNameValueBean.setValue(role.getId());
		surp.setRole(roleNameValueBean);

		surp.setSiteList(cpRelatedSites);

		surp.setPrivileges(actionBeanList);

		surp.setAllCPChecked(isAllCPChecked);

		return surp;
	}

	// Returns summmary Object
	public JSONObject getObjectForUPSummaryForUserPage(String rowId, Role role,
			List<Site> cpRelatedSites, CollectionProtocol collectionProtocol,
			List<NameValueBean> actionBeanList, boolean isAllCPChecked) throws JSONException,
			BizLogicException
	{
		final JSONObject jsonobject = new JSONObject();

		// for role
		String roleName = "";
		if (role != null)
		{
			roleName = role.getName();
		}
		// for sites
		final StringBuffer sbForSites = new StringBuffer();
		if (cpRelatedSites != null && cpRelatedSites.size() > 0)
		{
			for (int i = 0; i < cpRelatedSites.size(); i++)
			{
				if (i > 0)
				{
					sbForSites.append(",");
				}
				sbForSites.append(cpRelatedSites.get(i).getName());
			}
		}
		final String sites = sbForSites.toString();

		// for cp
		String cpName = "";
		if (collectionProtocol != null)
		{
			cpName = collectionProtocol.getShortTitle();
		}
		else if (isAllCPChecked && cpRelatedSites != null)
		{
			cpName = "All Current and Future";
		}
		else if (cpRelatedSites != null && !isAllCPChecked)
		{
			cpName = "N/A";
		}

		// for actions
		final StringBuffer sbForActions = new StringBuffer();
		for (int i = 0; i < actionBeanList.size(); i++)
		{
			if (i > 0)
			{
				sbForActions.append(",");
			}
			sbForActions.append(actionBeanList.get(i).getName());
		}
		final String actions = sbForActions.toString();

		jsonobject.append("roleName", roleName);
		jsonobject.append("cpName", cpName);
		jsonobject.append("sites", sites);
		jsonobject.append("actions", actions);
		jsonobject.append("rowId", rowId);
		return jsonobject;
	}

	public List<JSONObject> editPrivilegeForUserPage(
			Map<String, SiteUserRolePrivilegeBean> rowIdBeanMap, String selectedRow)
			throws JSONException, BizLogicException
	{
		final SiteUserRolePrivilegeBean surp = rowIdBeanMap.get(selectedRow);

		final List<JSONObject> privilegeList = new ArrayList<JSONObject>();
		final JSONObject jsonObject = new JSONObject();
		long selectedCPId = 0;

		// for checkBox
		final boolean isAllCPChecked = surp.isAllCPChecked();
		jsonObject.put("isAllCPChecked", isAllCPChecked);

		// for site
		final JSONArray siteJsonArray = new JSONArray();

		if (surp.getSiteList() != null && surp.getSiteList().size() > 0)
		{
			for (final Site site : surp.getSiteList())
			{
				final long siteId = site.getId();
				siteJsonArray.put(siteId);
			}
		}
		jsonObject.put("siteJsonArray", siteJsonArray);

		// for CP
		if (surp.getCollectionProtocol() != null)
		{
			selectedCPId = surp.getCollectionProtocol().getId();
		}
		jsonObject.append("selectedCPId", selectedCPId);

		final List<Site> siteList = surp.getSiteList();
		List<JSONObject> cpNameValueBeanList = new ArrayList<JSONObject>();
		if (siteList != null && !siteList.isEmpty())
		{
			final List<Long> selectedSitesList = new ArrayList<Long>();
			for (int z = 0; z < siteList.size(); z++)
			{
				selectedSitesList.add(siteList.get(z).getId());
			}

			cpNameValueBeanList = this.getCPsForThisSites(selectedSitesList);
		}
		else
		{
			final List<NameValueBean> list = this.getCPList(false);

			cpNameValueBeanList = this.getJSONObjListOfNameValue(list);
		}
		jsonObject.put("cpJsonArray", cpNameValueBeanList);

		// for Role
		final String roleId = surp.getRole().getValue();

		jsonObject.append("roleId", roleId);

		// for Privileges
		List<JSONObject> selActionJsonArray = null;
		List<JSONObject> actionJsonArray = null;
		List<NameValueBean> actionList = null;
		final List<NameValueBean> privileges = surp.getPrivileges();

		if (("7").equals(roleId))
		{
			actionList = this.getReformedNameValueBeanList(this.scientistPrivilegesList);
		}
		else
		{
			if (selectedCPId > 0)
			{
				actionList = this.getReformedNameValueBeanList(Variables.privilegeGroupingMap
						.get("CP"));
			}
			else if (surp.getSiteList() != null && !(surp.getSiteList()).isEmpty())
			{
				if (isAllCPChecked)
				{
					actionList = this.getActionListForUserPage(false);
				}
				else
				{
					actionList = this.getReformedNameValueBeanList(Variables.privilegeGroupingMap
							.get("SITE"));
				}
			}
		}

		selActionJsonArray = this.getJSONObjListOfNameValue(privileges);
		actionJsonArray = this.getJSONObjListOfNameValue(actionList);

		jsonObject.put("selActionJsonArray", selActionJsonArray);
		jsonObject.put("actionJsonArray", actionJsonArray);

		privilegeList.add(jsonObject);
		return privilegeList;
	}

	/**
	 * @param actionIdsList
	 * @return
	 * @throws CSException
	 */
	// Takes List of ids of actions and gives list of NameValueBean pairs of
	// actions.
	public List<NameValueBean> getPrivilegesNameValueBeanList(List<String> actionIdsList)
			throws BizLogicException
	{
		final List<NameValueBean> actionBeanList = new ArrayList<NameValueBean>();
		NameValueBean nameValueBean = null;
		String actionId = "";
		try
		{
			for (int len = 0; len < actionIdsList.size(); len++)
			{
				nameValueBean = new NameValueBean();
				actionId = actionIdsList.get(len);
				String actionName = "";
				final PrivilegeUtility privilegeUtility = new PrivilegeUtility();
				Privilege action = null;

				action = privilegeUtility.getPrivilegeById(actionId);

				actionName = action.getName();
				nameValueBean.setName(AppUtility.getDisplayLabelForUnderscore(actionName));
				nameValueBean.setValue(actionId);
				actionBeanList.add(nameValueBean);
			}
		}
		catch (final SMException e)
		{
			AssignPrivilegePageBizLogic.logger.error(e.getMessage(), e);
			e.printStackTrace();
			throw this.getBizLogicException(e, "error.privileges.from.given.role", actionId);
		}
		return actionBeanList;
	}

	// To remove "Select" option from list.
	public List<NameValueBean> removeSelect(List<NameValueBean> list)
	{
		if (list != null && !list.isEmpty() && list.size() > 0)
		{
			for (int len = 0; len < list.size(); len++)
			{
				final String val = "" + ((NameValueBean) list.get(len)).getValue();
				final String name = ((NameValueBean) list.get(len)).getName();
				if ((("" + Constants.SELECT_OPTION_VALUE).equalsIgnoreCase(val))
						&& ((Constants.SELECT_OPTION).equalsIgnoreCase(name)))
				{
					list.remove(len);
				}
			}
		}
		return list;
	}

	// Takes list of NameValueBean as input and Gives JSONArray of JSON Objects
	// having Name and Value as fields.
	public List<JSONObject> getJSONObjListOfNameValue(List<NameValueBean> list)
			throws JSONException
	{
		List<JSONObject> jsonObjectList = new ArrayList<JSONObject>();
		if (list != null && !list.isEmpty() && list.size() > 0)
		{
			jsonObjectList = new ArrayList<JSONObject>();
			JSONObject jsonObject = null;
			for (int i = 0; i < list.size(); i++)
			{
				jsonObject = new JSONObject();
				jsonObject.append("locationName", ((NameValueBean) list.get(i)).getName());
				jsonObject.append("locationId", ((NameValueBean) list.get(i)).getValue());

				jsonObjectList.add(jsonObject);
			}
		}
		return jsonObjectList;
	}

	// Takes String ,having ids joined with separartor ",",as a input and
	// Returns List of ids of long type.
	public List<Long> getInputData(String ids)
	{
		long tempId;
		List<Long> list = new ArrayList<Long>();
		if (ids != null && !ids.equals(""))
		{
			list = new ArrayList<Long>();

			final StringTokenizer tokenizer = new StringTokenizer("" + ids, ",");
			while (tokenizer.hasMoreTokens())
			{
				tempId = Long.parseLong(tokenizer.nextToken());
				list.add(tempId);
			}
		}
		return list;
	}

	public List<NameValueBean> getCommonElesList(List<NameValueBean> list1,
			List<NameValueBean> list2)
	{
		final List<NameValueBean> resultList = new ArrayList<NameValueBean>();
		if ((list1 != null && !list1.isEmpty()) && (list2 != null && !list2.isEmpty()))
		{
			for (final NameValueBean nameValueBean : list2)
			{
				if (list1.contains(nameValueBean))
				{
					resultList.add(nameValueBean);
				}
			}
		}
		return resultList;
	}

}