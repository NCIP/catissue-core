package edu.wustl.catissuecore.bizlogic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.wustl.catissuecore.actionForm.AssignPrivilegesForm;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.multiRepository.bean.SiteUserRolePrivilegeBean;
import edu.wustl.catissuecore.util.global.CPPrivilege;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.dao.AbstractDAO;
import edu.wustl.common.dao.DAOFactory;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.security.PrivilegeUtility;
import edu.wustl.common.security.SecurityManager;
import edu.wustl.common.security.exceptions.SMException;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Variables;
import edu.wustl.common.util.logger.Logger;
import gov.nih.nci.security.authorization.domainobjects.Privilege;
import gov.nih.nci.security.authorization.domainobjects.Role;
import gov.nih.nci.security.exceptions.CSException;
import gov.nih.nci.security.exceptions.CSObjectNotFoundException;


/**
 * AssignPrivilegePageBizLogic is a class which contains the  
 * implementations for ShowAssignPrivilegePageAction.
 * @author jitendra_agrawal
 * @author vipin_bansal
 */

public class AssignPrivilegePageBizLogic extends DefaultBizLogic
{
	
	public List getRecordNames(Set recordIds, AssignPrivilegesForm privilegesForm) throws DAOException
	{				
		List recordNames = new ArrayList();
		//Bug: 2508: Jitendra to display name in alphabetically order.
		if(!recordIds.isEmpty())
		{
			Object[] whereColumn = new String[recordIds.size()];
			Iterator itr = recordIds.iterator();
			int i =0;
			while(itr.hasNext())
			{
				NameValueBean nameValueBean = (NameValueBean)itr.next();
				whereColumn[i] = nameValueBean.getValue();		
				i++;
			}					
			String sourceObjectName = privilegesForm.getObjectType();
			String[] selectColumnName = new String[2];
			String[] whereColumnName = {"id"};
			String[] whereColumnCondition = {"in"};
			Object[] whereColumnValue = {whereColumn};
			
			if(privilegesForm.getObjectType().equals("edu.wustl.catissuecore.domain.CollectionProtocol"))
			{
				selectColumnName[0] = "title";
				selectColumnName[1] = "id";
			}
			else
			{
				selectColumnName[0] = "name";
				selectColumnName[1] = "id";
			}	
			List list = retrieve(sourceObjectName, selectColumnName, whereColumnName,
					whereColumnCondition, whereColumnValue, null);
			
			if (!list.isEmpty())
			{
				for(i=0;i<list.size();i++)
				{
					Object[] obj = (Object[])list.get(i);
					recordNames.add(new NameValueBean(obj[0],obj[1]));							
				}
			}		
			Collections.sort(recordNames);
		}	
		return recordNames;
	}
	/**
	 * Gets the list of Site objects to be shown on UI for assign privileges.
	 * @param isToExcludeDisabled
	 * @return List<NameValueBean> of sites.
	 * @throws BizLogicException
	 */	
	public List<NameValueBean> getSiteList(boolean isToExcludeDisabled) throws BizLogicException
	{
		String sourceObjectName =Site.class.getName();
		String[] siteDisplayField = {"name"};
		String valueField = "id";
		
		String[] activityStatusArray = {Constants.ACTIVITY_STATUS_DISABLED,Constants.ACTIVITY_STATUS_CLOSED};
		String joinCondition = null;
		String separatorBetweenFields = ", ";

		String[] whereColumnName = new String[]{Constants.ACTIVITY_STATUS};
		String[] whereColumnCondition = new String[]{"not in"};
		Object[] whereColumnValue = {activityStatusArray};
		

		List<NameValueBean> siteNameValueBeanList;
		try
		{
			siteNameValueBeanList = getList(sourceObjectName, siteDisplayField, valueField, whereColumnName, whereColumnCondition, whereColumnValue, joinCondition,
							separatorBetweenFields,isToExcludeDisabled);
		}
		catch (DAOException e) 
		{
			throw new BizLogicException("Could not get List of siteNameValueBean", e);
		}
		NameValueBean siteNameValueBean = siteNameValueBeanList.get(0);
		if(siteNameValueBean.getValue().equals("-1"))
		{
			siteNameValueBeanList.remove(0);
		}
		
		return siteNameValueBeanList;
		
	}
	/**
	 * Gets list of users when page is loaded
	 * @param isToExcludeDisabled
	 * @return List of user NameVAlueBean objects.
	 * @throws BizLogicException
	 */
	public List<NameValueBean> getUserList(boolean isToExcludeDisabled)  throws BizLogicException
	{
		String sourceObjectName =User.class.getName();
		String[] userDisplayField = {"lastName","firstName"};
		String valueField = "id";
		
		String[] activityStatusArray = {Constants.ACTIVITY_STATUS_DISABLED,Constants.ACTIVITY_STATUS_CLOSED};
		String joinCondition = null;
		String separatorBetweenFields = ", ";

		String[] whereColumnName = new String[]{Constants.ACTIVITY_STATUS};
		String[] whereColumnCondition = new String[]{"not in"};
		Object[] whereColumnValue = {activityStatusArray};		

		List<NameValueBean> userNameValueBeanList;
		try
		{
			userNameValueBeanList = getList(sourceObjectName, userDisplayField, valueField, whereColumnName, whereColumnCondition, whereColumnValue, joinCondition,
							separatorBetweenFields,isToExcludeDisabled);
		}
		catch (DAOException e) 
		{
			throw new BizLogicException("Could not get List of userNameValueBean", e);
		}
		NameValueBean  userNameValueBean=userNameValueBeanList.get(0);
		if(userNameValueBean.getValue().equals("-1"))
		{
			userNameValueBeanList.remove(0);
		}
		
		return userNameValueBeanList;
		
	}	
	/**
	 * Gets list of roles when page is loaded
	 * @return List of role NameValueBean objects.
	 * @throws BizLogicException
	 */
	public List<NameValueBean> getRoleList() throws BizLogicException
    {
        //Sets the roleList attribute to be used in the Add/Edit User Page.
        Vector roleList;
		try 
		{
			roleList = SecurityManager.getInstance(this.getClass()).getRoles();
		} 
		catch (SMException e)
		{
			throw new BizLogicException("Could not get List of Roles", e);
		}
       
        ListIterator iterator = roleList.listIterator();
        
        List<NameValueBean> roleNameValueBeanList = new ArrayList<NameValueBean>();
        roleNameValueBeanList.add(0,new NameValueBean(Constants.SELECT_OPTION,"-1"));
        while (iterator.hasNext())
        {
            Role role = (Role) iterator.next();
            NameValueBean nameValueBean = new NameValueBean();
            if(!("SUPERADMINISTRATOR".equals(role.getName())))
            {
	            nameValueBean.setName(role.getName());
	            nameValueBean.setValue(String.valueOf(role.getId()));
	            roleNameValueBeanList.add(nameValueBean);
            }
        }
        
        return roleNameValueBeanList;
    }
	/**
	 * Gets list of actions when page is loaded
	 * @param isToExcludeDisabled
	 * @return List of user NameVAlueBean objects.
	 * @throws BizLogicException
	 */
	public List<NameValueBean> getActionList(boolean isToExcludeDisabled) throws BizLogicException
	{     
		List<NameValueBean> privilegeNameValueBeanList = new ArrayList<NameValueBean>();
		List<NameValueBean> cpPrivilegesList = Variables.privilegeGroupingMap.get("CP");
		for (NameValueBean nmv  : cpPrivilegesList)
		{
			NameValueBean privilegeNameValueBean = new NameValueBean(Utility.getDisplayLabelForUnderscore(nmv.getName()), nmv.getValue());
			privilegeNameValueBeanList.add(privilegeNameValueBean);
		}
		return privilegeNameValueBeanList;
	}
	
	
	
	/**
	 * Given a list selected sites , this method will return the list of users registered under them.
	 * @param siteIds
	 * @return List of user registered under the given site
	 */
	public List<NameValueBean> getUsersList(long siteId) throws BizLogicException{	
		AbstractDAO dao = DAOFactory.getInstance().getDAO(Constants.HIBERNATE_DAO);
		List<NameValueBean> nameValuBeanList=new ArrayList<NameValueBean>();
		try
		{
			dao.openSession(null);
			Object obj = dao.retrieve(Site.class.getName(),siteId);
			Site site = (Site)obj;
			Collection<User> userCollection = site.getAssignedSiteUserCollection();
			for (User user : userCollection)
			{ 
				NameValueBean valueBean=new NameValueBean();
				valueBean.setName(user.getLastName()+","+user.getFirstName());
				valueBean.setValue(String.valueOf(user.getId()));
				nameValuBeanList.add(valueBean);
			}
		}
		catch (DAOException e)
		{
			throw new BizLogicException("Could not get Object of Site", e);
		}
		finally
		{
			try
			{
				dao.closeSession();
			}
			catch (DAOException e)
			{
				throw new BizLogicException("Couldn't close hibernate session",e);
			}
		}
		
		return nameValuBeanList;
	}
	/**
	 * Given a selected role this method will return the list of actions under this role.
	 * @param role
	 * @return List of actions assigned to the selected role
	 */
	public List<NameValueBean> getActionsList(String role) 
	{	
		List<NameValueBean> nameValuBeanList=new ArrayList<NameValueBean>();
			
	    PrivilegeUtility privilegeUtility = new PrivilegeUtility();
	    Set<Privilege> actions;
		try
		{ 
			actions = privilegeUtility.getRolePrivileges(role);
			  for (Privilege action : actions)
				{
			    	NameValueBean valueBean=new NameValueBean();
			    	valueBean.setName(action.getName());
			    	valueBean.setValue(String.valueOf(action.getId()));
			    	nameValuBeanList.add(valueBean);
				}
		}
		catch (CSObjectNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (CSException e)
		{
			e.printStackTrace();
		}
	    
		return nameValuBeanList;
	}
	/**
	 * Gives the list of user NameValueBeanObjects that are registered under the selected sites.
	 * @param selectedSitesList
	 * @return List<NameValueBean> list of user NameValueBeanObjects .
	 */
	public List<NameValueBean> getUsersForSelectedSites(List<Long> selectedSitesList) throws BizLogicException
	{
		Set<NameValueBean> userSet = new HashSet<NameValueBean>();
		for(int i=0;i<selectedSitesList.size();i++)
		{
			long siteId=selectedSitesList.get(i);
			 userSet.addAll(getUsersList(siteId));
		}
		List<NameValueBean> list = new ArrayList<NameValueBean>();
		list.addAll(userSet);
 		return list;
	}
	
	/**
	 *Create the List of siteIds from request data
	 * @param assignPrivilegePageBizLogic
	 * @param request
	 * @return List<Integer> list of siteIds 
	 */
	public List<Long> getSiteData(String siteIds)
	{
		long siteId;
		List<Long> siteIdsList = new ArrayList<Long>();
		StringTokenizer tokenizer = new StringTokenizer("" + siteIds, ",");
		while (tokenizer.hasMoreTokens()) {
			siteId = Long.parseLong(tokenizer.nextToken());
			siteIdsList.add(siteId);
		}
		return siteIdsList;
	}
	/**
	 * Gives list of JSONObjects, having user NameValueBean assigned to selected sites as a response
	 * @param selectedSitesList
	 * @param assignPrivilegePageBizLogic
	 * @return List<JSONObject> ,list of JSONObjects, having user NameValueBean assigned to selected sites
	 * @throws JSONException 
	 * @throws IOException
	 */
	public List<JSONObject> getUsersForThisSites(List<Long> selectedSitesList) throws BizLogicException,JSONException
	{
		List<JSONObject> arrayList = new ArrayList<JSONObject>();
		List<NameValueBean> usersList = new ArrayList<NameValueBean>();
		usersList = getUsersForSelectedSites(selectedSitesList);
		JSONObject jsonobject = null;
		for (int j = 0; j < usersList.size(); j++) 
		{
			jsonobject = new JSONObject();
			jsonobject.append("locationName", ((NameValueBean) usersList
					.get(j)).getName());
			jsonobject.append("locationId", ((NameValueBean) usersList
					.get(j)).getValue());
			arrayList.add(jsonobject);
		}
		return arrayList;
	}
	/**
	 * Gives list of JSONObjects, having action NameValueBean assigned to selected role as a response 
	 * @param roleId
	 * @param assignPrivilegePageBizLogic
	 * @return List<JSONObject> ,list of JSONObjects, having action NameValueBean assigned to selected role
	 * @throws JSONException 
	 */
	public List<JSONObject> getActionsForThisRole(String roleId)throws JSONException 
	{
		List<NameValueBean> actionsList = getActionsList(roleId);
		// request.setAttribute(Constants.ACTIONLIST, actionsList);
		List<JSONObject> arrayList = new ArrayList<JSONObject>();
		
			JSONObject jsonobject = null;
			for (int i = 0; i < actionsList.size(); i++)
			{
				jsonobject = new JSONObject();
				jsonobject.append("locationName", ((NameValueBean) actionsList
						.get(i)).getName());
				jsonobject.append("locationId", ((NameValueBean) actionsList
						.get(i)).getValue());
				arrayList.add(jsonobject);
			}
		return arrayList;
	}
	/**
	 * Create the List of userIds from request data
	 * @param assignPrivilegePageBizLogic
	 * @param request
	 * @return List<Long> list of userIds
	 */
	public List<Long> getUserData(String userIds) 
	{
		long userId;
		List<Long> userIdsList = new ArrayList<Long>();
		
		StringTokenizer tokenizer = new StringTokenizer("" + userIds, ",");
		while (tokenizer.hasMoreTokens())
		{
			userId = Long.parseLong(tokenizer.nextToken());
			userIdsList.add(userId);
		}
		return userIdsList;
	}

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
		List<String> actionIdsList = new ArrayList<String>();
		StringTokenizer tokenizer = new StringTokenizer("" + actionIds, ",");
		while (tokenizer.hasMoreTokens()) 
		{
			actionId = tokenizer.nextToken();
			actionIdsList.add(actionId);
		}
		return actionIdsList;
	}
	/**
	 * Gives user related sites
	 * @param userId
	 * @param siteIdsList
	 * @return List<Integer> list of siteIds related to selected user
	 */
	public List<Site> getUserSiteRelation(User user,List<Long> siteIdsList) 
	{
		List<Site> list = new ArrayList<Site>();
		Collection<Site> siteCollection = user.getSiteCollection();
		if(siteCollection!=null || !siteCollection.isEmpty())
		{
			for (Site site : siteCollection)
			{
				if(siteIdsList.contains(new Integer(site.getId().toString())))
				{
					list.add(site);
				}
			}
		}
		
		return list;
	}
	/**
	 * Create the BeanObject
	 * @param user
	 * @param userRelatedSites
	 * @param roleId
	 * @param actionIdsList
	 * @return SiteUserRolePrivilegeBean  bean Object
	 */
	public SiteUserRolePrivilegeBean setUserPrivilegeSummary(User user,List<Site> userRelatedSites, String roleId, List<String> actionIdsList)
	{
		SiteUserRolePrivilegeBean surp = new SiteUserRolePrivilegeBean();
		
		surp.setUser(user);
		surp.setRole(roleId);
		
		List<Long> listOfSiteIds = new ArrayList<Long>();
		for(Site site :userRelatedSites)
		{
			listOfSiteIds.add(site.getId());
		}
		surp.setSiteList(listOfSiteIds);
		
		Set<String> privilegs = new HashSet<String>();
		privilegs.addAll(actionIdsList);
		surp.setPrivileges(privilegs);
		return surp;
	}
	/**
	 * gives JSONObject as a response for summary 
	 * @param userRelatedSites
	 * @param rowId
	 * @param user
	 * @param roleId
	 * @param actionIdsList 
	 * @return JSONObject for response
	 * @throws JSONException
	 * @throws CSException 
	 */
	public JSONObject getObjectForUPSummary(List<Site> userRelatedSites, String rowId, User user, String roleName, List<String> actionIdsList) throws JSONException,BizLogicException 
	{
		StringBuffer sbForSitesIds = new StringBuffer();
		for (int i = 0; i < userRelatedSites.size(); i++) 
		{
			if (i > 0)
			{
				sbForSitesIds.append(",");
			}
		    sbForSitesIds.append(userRelatedSites.get(i).getName());
			
		}
		String sites = sbForSitesIds.toString();

		JSONObject jsonobject = new JSONObject();
		jsonobject.append("rowId", rowId);

		String userName = user.getFirstName();
		long userId=user.getId();
		jsonobject.append("userName", userName);
		jsonobject.append("userId", userId);
		jsonobject.append("sites", sites);
		StringBuffer sbForActions = new StringBuffer();
		for (int i = 0; i < actionIdsList.size(); i++) 
		{
			if (i > 0)
			{
				sbForActions.append(",");
			}
			
			PrivilegeUtility privilegeUtility = new PrivilegeUtility();
			Privilege action;
			try 
			{
				action = privilegeUtility.getPrivilegeById(actionIdsList.get(i));
			}
			catch (CSException e)
			{
				throw new BizLogicException("Could not get the action",e);
			}
			if((action.getName()).equals("PHI_ACCESS"))
			{
				sbForActions.append(ApplicationProperties.getValue(action.getName()));
			}
			else
			sbForActions.append(Utility.getDisplayLabelForUnderscore(action.getName()));
		//	sbForActions.append(ApplicationProperties.getValue(action.getName()));
		}
		String actionName =sbForActions.toString();
		jsonobject.append("roleName", roleName);
		jsonobject.append("actionName", actionName);
		return jsonobject;
	}
	/**
	 *  Delete row from the map
	 * @param deletedRowsArray
	 * @param rowIdObjectBeanMap
	 */
	public Map<String, SiteUserRolePrivilegeBean> deletePrivilege(Map<String, SiteUserRolePrivilegeBean> rowIdBeanMap,String deletedRowsArray) 
	{
		String deletedRowId = "";
		List<String> deletedRowsList = new ArrayList<String>();
		StringTokenizer tokenizer = new StringTokenizer(deletedRowsArray, ",");
		while (tokenizer.hasMoreTokens())
		{
			deletedRowId = tokenizer.nextToken().intern();
			deletedRowsList.add(deletedRowId);
			rowIdBeanMap.remove(deletedRowId);
		}
		
		return rowIdBeanMap;
	}    
	/**
	 * Gives Map having rowId and UsersummaryBean Object and returns a list containing rowId and userSummaryBean Object,used to send response.  
	 * @param rowIdBeanMap
	 * @param userIds
	 * @param siteIds
	 * @param roleId
	 * @param actionIds
	 * @return List<JSONObject> of userSummaryBean Objects
	 */	
	public List<JSONObject> addPrivilege(Map<String, SiteUserRolePrivilegeBean> rowIdBeanMap, String userIds, String siteIds, String roleId, String actionIds)throws BizLogicException,JSONException 
	{  
		List<JSONObject> listForUPSummary = new ArrayList<JSONObject>();
		List<Long> siteIdsList = new ArrayList<Long>();
		List<Long> userIdsList = new ArrayList<Long>();
		List<String> actionIdsList = new ArrayList<String>();

 		siteIdsList = getSiteData(siteIds);
		userIdsList =getUserData(userIds);
		actionIdsList = getActionData(actionIds);
		AbstractDAO dao = DAOFactory.getInstance().getDAO(Constants.HIBERNATE_DAO);

		try 
		{
			for (int k = 0; k < userIdsList.size(); k++) 
			{ 
				String roleName= "";
				SecurityManager securityManager=SecurityManager.getInstance(this.getClass());
				try
				{
				Role role=securityManager.getUserProvisioningManager().getRoleById(roleId);
				roleName=role.getName();
				}
				catch (CSException e)
				{
					throw new BizLogicException("Could not get the role",e);
				}
				
				 long userId = userIdsList.get(k);
				 dao.openSession(null); 
				 Object object = dao.retrieve(User.class.getName(), userId);
				 User user  = (User)object;
				 List<Site> userRelatedSites = getUserSiteRelation(user, siteIdsList);
				
				 SiteUserRolePrivilegeBean surpBean =setUserPrivilegeSummary(user, userRelatedSites, roleId, actionIdsList);
				
				 String rowId = "" + userId;
				 rowIdBeanMap.put(rowId,surpBean);
				 
				 JSONObject jsonObject=getObjectForUPSummary(userRelatedSites,rowId,user,roleName,actionIdsList);
				 
				 listForUPSummary.add(jsonObject);
			}
		}  
		catch (DAOException e) 
		{
			throw new BizLogicException("DAOException in  getting objectList for AssignPrivilegePageBizLogic  in ShowAssignPrivilegePageAction..."+e);
		} 
		finally
		{
			try
			{
				dao.closeSession();
			}
			catch(DAOException daoEx)
			{
				Logger.out.error(daoEx.getMessage(), daoEx);
	    		return null;
			}
		}
		return listForUPSummary;
	}
	
	/**
	 * Method to show userPrivilege summary details on tab change
	 * @param session
	 * @param dao
	 * @return List of String array  
	 * @throws CSObjectNotFoundException
	 * @throws CSException
	 * @throws DAOException
	 */
	public List<String[]> privilegeDataOnTabSwitch(Map<String, SiteUserRolePrivilegeBean> rowIdBeanMap) throws BizLogicException {
		List<String[]> list=new ArrayList<String[]>();
		
		int mapSize=rowIdBeanMap.size();
		Object[] surpArray= rowIdBeanMap.values().toArray();
		SiteUserRolePrivilegeBean bean=null;
		Object[] keyArray=rowIdBeanMap.keySet().toArray();
		
		for(int j=0;j<mapSize;j++)
		{
			String[] array=new String[5];
			String rowId="";
			
			bean =(SiteUserRolePrivilegeBean)surpArray[j];
			
			// for role id
			for(int count=0;count<keyArray.length;count++)
			{
				SiteUserRolePrivilegeBean beanForKey=rowIdBeanMap.get(keyArray[count]);
				if (bean==beanForKey)
				{
					rowId=(String)keyArray[count];
				}
			}
			
			// for user
			String userName = bean.getUser().getFirstName();
			
			//for role
			String roleName= "";
			String roleId=bean.getRole();
			
			SecurityManager securityManager=SecurityManager.getInstance(this.getClass());
			try
			{
			Role role=securityManager.getUserProvisioningManager().getRoleById(roleId);
			roleName=role.getName();
			}
			catch (CSException e)
			{
				throw new BizLogicException("Could not get the role",e);
			}
			
			// for site
			String sites = displaySiteNames(bean);
			
			// for privileges
			String actionNames = displayPrivilegesNames(bean);
			
			array[0]=userName;
			array[1]=sites;
			array[2]=roleName;
			array[3]=actionNames;
			array[4]=rowId;
			list.add(j,array);
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
		StringBuffer sbForActions = new StringBuffer();
		for (int i = 0; i < bean.getPrivileges().size(); i++) 
		{
			if (i > 0)
			{
				sbForActions.append(",");
			}
			String actionId=(String)bean.getPrivileges().toArray()[i];
			PrivilegeUtility privilegeUtility = new PrivilegeUtility();
			Privilege action;
			try 
			{
				action = privilegeUtility.getPrivilegeById(actionId);
			}
			catch (CSException e) 
			{
				throw new BizLogicException(e);
			}
				if((action.getName()).equals("PHI_ACCESS"))
				{
					sbForActions.append(ApplicationProperties.getValue(action.getName()));
				}
				else
				{
					sbForActions.append(Utility.getDisplayLabelForUnderscore(action.getName()));
				}
		//	sbForActions.append(ApplicationProperties.getValue(action.getName()));
		}
		String actionNames =sbForActions.toString();
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
		AbstractDAO dao = DAOFactory.getInstance().getDAO(Constants.HIBERNATE_DAO);
		StringBuffer sbForSitesNames = new StringBuffer();
		for (int i = 0; i < bean.getSiteList().size(); i++) 
		{
			if (i > 0)
			{
				sbForSitesNames.append(",");
			}
			Site site= null;
			try
			{
			dao.openSession(null); 
			
			Object object = dao.retrieve(Site.class.getName(), (long)bean.getSiteList().get(i));
			site=(Site)object;
			} 
			catch (DAOException e)
			{
				throw new BizLogicException("Could not retrieve site", e);
			}
			finally 
			{
				try
				{
					dao.closeSession();
				} 
				catch (DAOException e)
				{
					throw new BizLogicException("Couldn't close hibernate session",e);
				}
			}
			sbForSitesNames.append(site.getName());
		}
		String sites = sbForSitesNames.toString();
		return sites;
	}
	
	public List<JSONObject> editPrivilege (Map<String, SiteUserRolePrivilegeBean> rowIdBeanMap,String selectedRow)throws JSONException,BizLogicException 
	{
		SiteUserRolePrivilegeBean surp=rowIdBeanMap.get(selectedRow);
		
		List<JSONObject> privilegeList = new ArrayList<JSONObject>();
		JSONObject jsonObject=new JSONObject();
		
		long selectedUserId=surp.getUser().getId();
		
		// for site
		JSONArray siteJsonArray=new JSONArray();
		
		for(long siteId:surp.getSiteList())
		{
			siteJsonArray.put(siteId);
		}
		jsonObject.put("siteJsonArray",siteJsonArray );

		// for User
		jsonObject.append("selectedUserId", selectedUserId);

		List<JSONObject> userNameValueBeanList=getUsersForThisSites(surp.getSiteList());
		jsonObject.put("userJsonArray",userNameValueBeanList);
		
		
		// for Role
		String roleId=surp.getRole();
		
		jsonObject.append("roleId", roleId);
		
		// for Privileges
		JSONArray actionJsonArray=new JSONArray();
		Set<String> privileges=surp.getPrivileges();
		
		for(String actionId:privileges)
		{
			actionJsonArray.put(actionId);
		}
		jsonObject.put("actionJsonArray",actionJsonArray );
		
		privilegeList.add(jsonObject);
		return privilegeList;
	}
}
