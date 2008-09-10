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
import java.util.Vector;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.eclipse.jdt.core.compiler.IScanner;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.wustl.catissuecore.actionForm.AssignPrivilegesForm;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.multiRepository.bean.SiteUserRolePrivilegeBean;
import edu.wustl.catissuecore.util.CaTissuePrivilegeUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.listener.CatissueCoreServletContextListener;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
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
	private static org.apache.log4j.Logger logger =Logger.getLogger(CatissueCoreServletContextListener.class); 
	List<NameValueBean> scientistPrivilegesList = Variables.privilegeGroupingMap.get("SCIENTIST");
	
	public List getRecordNames(Set recordIds, AssignPrivilegesForm privilegesForm) throws DAOException
	{				
		List recordNames = new ArrayList();
		//Bug: 2508: Jitendra to display name in alphabetically order.
		if(!recordIds.isEmpty())
		{
			Object[] whereColumn = new Long[recordIds.size()];
			Iterator itr = recordIds.iterator();
			int i =0;
			while(itr.hasNext())
			{
				NameValueBean nameValueBean = (NameValueBean)itr.next();
				whereColumn[i] =Long.valueOf(nameValueBean.getValue());		
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
	public List<NameValueBean> getSiteList(boolean isToExcludeDisabled, SessionDataBean sessionDataBean) throws BizLogicException
	{
		String sourceObjectName =Site.class.getName();
		String[] siteDisplayField = {"name"};
		String valueField = "id";
		
		String[] activityStatusArray = {Constants.ACTIVITY_STATUS_DISABLED,Constants.ACTIVITY_STATUS_CLOSED};

		List<NameValueBean> siteNameValueBeanList = null;
		List<NameValueBean> tempSiteNameValueBeanList = new ArrayList<NameValueBean>();
		try 
		{
			siteNameValueBeanList = new StorageContainerBizLogic().getRepositorySiteList(sourceObjectName, siteDisplayField, valueField, activityStatusArray, isToExcludeDisabled);
		} 
		catch (DAOException e) 
		{
			logger.debug(e.getMessage(), e);
		}
		
		// To remove 1st row which contains "Select" & "-1"
		siteNameValueBeanList.remove(0);
		tempSiteNameValueBeanList.addAll(siteNameValueBeanList);
		
		if(sessionDataBean != null && !sessionDataBean.isAdmin())
		{
			Set<Long> idSet = new UserBizLogic().getRelatedSiteIds(sessionDataBean.getUserId());
			
			for(NameValueBean nvb : siteNameValueBeanList)
			{
				Long siteId = Long.valueOf(nvb.getValue());
				if(!idSet.contains(siteId))
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
		String sourceObjectName =CollectionProtocol.class.getName();
		String[] siteDisplayField = {"shortTitle"};
		String valueField = "id";
		
		String[] activityStatusArray = {Constants.ACTIVITY_STATUS_DISABLED,Constants.ACTIVITY_STATUS_CLOSED};
		String joinCondition = null;
		String separatorBetweenFields = ", ";

		String[] whereColumnName = new String[]{Constants.ACTIVITY_STATUS};
		String[] whereColumnCondition = new String[]{"not in"};
		Object[] whereColumnValue = {activityStatusArray};

		List<NameValueBean> cpNameValueBeanList;
		try
		{
			cpNameValueBeanList = getList(sourceObjectName, siteDisplayField, valueField, whereColumnName, whereColumnCondition, whereColumnValue, joinCondition,
							separatorBetweenFields,isToExcludeDisabled);
		}
		catch (DAOException e) 
		{
			throw new BizLogicException("Could not get List of siteNameValueBean", e);
		}

		cpNameValueBeanList = removeSelect(cpNameValueBeanList);
		
		return cpNameValueBeanList;
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

		List<NameValueBean> userNameValueBeanList=new ArrayList<NameValueBean>();
		try
		{
			userNameValueBeanList = getList(sourceObjectName, userDisplayField, valueField, whereColumnName, whereColumnCondition, whereColumnValue, joinCondition,
							separatorBetweenFields,isToExcludeDisabled);
		}
		catch (DAOException e) 
		{
			throw new BizLogicException("Could not get List of userNameValueBean", e);
		}
		
		userNameValueBeanList=removeSelect(userNameValueBeanList);
		
		return userNameValueBeanList;
		
	}	
	/**
	 * Gets list of roles when page is loaded
	 * @return List of role NameValueBean objects.
	 * @throws BizLogicException
	 */
	public List<NameValueBean> getRoleList(String pageOf) throws BizLogicException
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
        
        List<NameValueBean> roleNameValueBeanList = new ArrayList<NameValueBean>();
        roleNameValueBeanList.add(0,new NameValueBean(Constants.SELECT_OPTION,"-1"));
        roleNameValueBeanList.add(new NameValueBean(Constants.CUSTOM_ROLE,"0"));
        
        if(roleList!=null&&!roleList.isEmpty())
        {
	        ListIterator iterator = roleList.listIterator();
	        
	        while (iterator.hasNext())
	        {
	            Role role = (Role) iterator.next();
	            NameValueBean nameValueBean = new NameValueBean();
	            if((Constants.PAGEOF_ASSIGN_PRIVILEGE).equalsIgnoreCase(pageOf))
	            {
	            	if(!((Constants.ROLE_SUPER_ADMINISTRATOR).equals(role.getName()))&&!((Constants.ROLE_ADMINISTRATOR).equals(role.getName())))
	            	{
	            		 nameValueBean.setName(role.getName());
	            		 nameValueBean.setValue(String.valueOf(role.getId()));
	        	         roleNameValueBeanList.add(nameValueBean);
	            	}
	            }
	            else if(pageOf!=null)
	            {
	                if((Constants.ROLE_SUPER_ADMINISTRATOR).equals(role.getName())&& (Constants.SUPER_ADMIN_USER).equals(""+role.getId()))
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
	 * @param isToExcludeDisabled
	 * @return List of user NameVAlueBean objects.
	 * @throws BizLogicException
	 */
	public List<NameValueBean> getActionList(boolean isToExcludeDisabled) throws BizLogicException
	{     
		List<NameValueBean> privilegeNameValueBeanList =null;
		
		List<NameValueBean> cpPrivilegesList = Variables.privilegeGroupingMap.get("CP");
		
		privilegeNameValueBeanList= getReformedNameValueBeanList(cpPrivilegesList);
		
		return privilegeNameValueBeanList;
	}
	
	public List<NameValueBean> getActionListForUserPage(boolean isToExcludeDisabled) throws BizLogicException
	{     
		List<NameValueBean> privilegeNameValueBeanList = new ArrayList<NameValueBean>();
		List<NameValueBean> cpPrivilegesList = Variables.privilegeGroupingMap.get("CP");
		
		List<NameValueBean> sitePrivilegesList = Variables.privilegeGroupingMap.get("SITE"); 
		
		Set<NameValueBean> set =new HashSet<NameValueBean>();
		set.addAll(cpPrivilegesList);
		set.addAll(sitePrivilegesList);
		privilegeNameValueBeanList.addAll(set);
		
		List<NameValueBean> privList = getReformedNameValueBeanList(privilegeNameValueBeanList);
		
		return privList;
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
		
		if(usersList!=null && !usersList.isEmpty())
		{
			arrayList = getJSONObjListOfNameValue(usersList);
		}
		return arrayList;
	}
	
	/**
	 * Gives the list of user NameValueBeanObjects that are registered under the selected sites.
	 * @param selectedSitesList
	 * @return List<NameValueBean> list of user NameValueBeanObjects .
	 */
	public List<NameValueBean> getUsersForSelectedSites(List<Long> selectedSitesList) throws BizLogicException
	{
		List<NameValueBean> list = new ArrayList<NameValueBean>();
		
		if(selectedSitesList!=null && !selectedSitesList.isEmpty())
		{
			List<NameValueBean> userList2 = new ArrayList<NameValueBean>();
			
			long siteId=selectedSitesList.get(0);
			list=getUsersList(siteId);
			
			for(int i=1; i<selectedSitesList.size();i++)
			{
				siteId=selectedSitesList.get(i);
				userList2 = getUsersList(siteId);
				
				list = getCommonElesList(list, userList2);
			}
		}
 		return list;
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
			//	valueBean.setName(user.getLastName()+","+user.getFirstName());
				valueBean.setName(user.getLoginName());
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
	

	public List<JSONObject> getCPsForThisSites(List<Long> selectedSitesList) throws BizLogicException,JSONException
	{
		List<JSONObject> arrayList=new ArrayList<JSONObject>();
		List<NameValueBean> cpsList = new ArrayList<NameValueBean>();
		
		if(selectedSitesList!=null && ! selectedSitesList.isEmpty())
		{
			cpsList = getCPsForSelectedSites(selectedSitesList);
		}
		else
		{
			cpsList = getCPList(false);
		}
		
		arrayList = getJSONObjListOfNameValue(cpsList);
		
		return arrayList;
	}
	
	public List<NameValueBean> getCPsForSelectedSites(List<Long> selectedSitesList)throws BizLogicException
	{
		List<NameValueBean> list = new ArrayList<NameValueBean>();
		
		if(selectedSitesList!=null && !selectedSitesList.isEmpty())
		{
			List<NameValueBean> cpList2 = new ArrayList<NameValueBean>();
			
			long siteId=selectedSitesList.get(0);
			list = getCPsList(siteId);
			
			for(int i=1; i<selectedSitesList.size();i++)
			{
				siteId=selectedSitesList.get(i);
				cpList2 = getCPsList(siteId);
				
				list = getCommonElesList(list, cpList2);
			}
		}
 		return list;
	}
	
	public List<NameValueBean> getCPsList(long siteId) throws BizLogicException{	
		AbstractDAO dao = DAOFactory.getInstance().getDAO(Constants.HIBERNATE_DAO);
		List<NameValueBean> nameValuBeanList=new ArrayList<NameValueBean>();
		try
		{
			dao.openSession(null);
			Object obj = dao.retrieve(Site.class.getName(),siteId); 
			Site site = (Site)obj;
			Collection<CollectionProtocol> cpCollection=site.getCollectionProtocolCollection();
			for (CollectionProtocol colectionProtocol : cpCollection)
			{ 
				NameValueBean valueBean=new NameValueBean();
				valueBean.setName(colectionProtocol.getShortTitle());
				valueBean.setValue(String.valueOf(colectionProtocol.getId()));
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
	 * Gives list of JSONObjects, having action NameValueBean assigned to selected role as a response 
	 * @param roleId
	 * @param assignPrivilegePageBizLogic
	 * @return List<JSONObject> ,list of JSONObjects, having action NameValueBean assigned to selected role
	 * @throws JSONException 
	 * @throws BizLogicException 
	 */
	public List<JSONObject> getActionsForThisRole(String roleId, String pageOf,List<Long> selectedSiteIds,List<Long> selectedCPIds,boolean isAllCPChecked)throws JSONException, BizLogicException 
	{
		List<NameValueBean> actionList = new ArrayList<NameValueBean> ();
		List<NameValueBean> selectedActionsList=null;
		List<NameValueBean> cpList = null;
		if (pageOf != null && pageOf.equalsIgnoreCase(Constants.PAGEOF_ASSIGN_PRIVILEGE))
		{
			if("7".equals(roleId))
			{
				actionList = getReformedNameValueBeanList(scientistPrivilegesList);
				selectedActionsList=new ArrayList<NameValueBean>();
				
				cpList = getCPList(false);
			}
			else
			{
				actionList=getActionList(false);
				if(!"0".equals(roleId))
				{
					selectedActionsList = getActionsList(roleId);
				}
				else
				{
					selectedActionsList=new ArrayList<NameValueBean>();
				}
			}
		}
		else if(pageOf != null)
		{
			if("7".equals(roleId))
			{
				actionList = getReformedNameValueBeanList(scientistPrivilegesList);
				selectedActionsList=new ArrayList<NameValueBean>();
				
				cpList = getCPList(false);
			}
			else
			{
				if((selectedCPIds!=null)&& ! selectedCPIds.isEmpty()&& selectedCPIds.size()>0)
				{
					actionList = getReformedNameValueBeanList(Variables.privilegeGroupingMap.get("CP"));
				}
				
				else if((selectedSiteIds!=null)&& ! selectedSiteIds.isEmpty()&& selectedSiteIds.size()>0)
				{
					if(isAllCPChecked)
					{
						actionList = getActionListForUserPage(false);
					}
					else
					{
						actionList = getReformedNameValueBeanList(Variables.privilegeGroupingMap.get("SITE"));
					}
				}
				else 
				{
					actionList = getActionListForUserPage(false);
				}
				if(!"0".equals(roleId))
				{
					selectedActionsList = getActionsList(roleId);
				}
				else
				{
					selectedActionsList=new ArrayList<NameValueBean>();
				}
			}
		}
	//	 selectedActionsList = getActionsList(roleId);

		List<JSONObject> arrayList = new ArrayList<JSONObject>();
		JSONObject jsonObject=new JSONObject();
	
		List<JSONObject> actionJsonArray=getJSONObjListOfNameValue(actionList);
		jsonObject.put("actionJsonArray", actionJsonArray);
	
		List<JSONObject> selectedActionArray=getJSONObjListOfNameValue(selectedActionsList);
		jsonObject.put("selectedActionArray", selectedActionArray);
		
		List<JSONObject> cpJsonArray=getJSONObjListOfNameValue(cpList);
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
		List<NameValueBean> reformedList=null;
		
		if(list!=null && !list.isEmpty()&& list.size()>0)
		{
			reformedList=new ArrayList<NameValueBean>();
			   
			for (NameValueBean nmv  : list)
			{
				NameValueBean tempNameValueBean = new NameValueBean(Utility.getDisplayLabelForUnderscore(nmv.getName()), nmv.getValue());
				reformedList.add(tempNameValueBean);
			}
		}
		return reformedList;
	}
	
	public List<JSONObject> getActionsForThisSites(String selectedRoleId,boolean isAllCPChecked) throws BizLogicException,JSONException
	{
		List<NameValueBean> actionList=null;
		if(isAllCPChecked)
		{
			actionList = getActionListForUserPage(false);
		}
		else
		{
			actionList = getReformedNameValueBeanList(Variables.privilegeGroupingMap.get("SITE"));
		}
		List<NameValueBean> selectedActionsList=new ArrayList<NameValueBean>();
		
		if(selectedRoleId!=null&& selectedRoleId!=""&& ! selectedRoleId.equalsIgnoreCase("-1"))
		{
		//	List<NameValueBean> actionsListForSelRole = getActionsForSelRole(selectedRoleId);
			List<NameValueBean> actionsListForSelRole = getActionsList(selectedRoleId);
			
			if(actionsListForSelRole!=null && ! actionsListForSelRole.isEmpty())
			{
				for(int count=0;count<actionsListForSelRole.size();count++)
				{
					NameValueBean nameValueBean=(NameValueBean)actionsListForSelRole.get(count);
					if(actionList.contains(nameValueBean))
					{
						selectedActionsList.add(nameValueBean);
					}
				}
			}
		}

		List<JSONObject> arrayList = new ArrayList<JSONObject>();
		JSONObject jsonObject=new JSONObject();

		List<JSONObject> actionJsonArray=getJSONObjListOfNameValue(actionList);
		jsonObject.put("actionJsonArray", actionJsonArray);

		List<JSONObject> selectedActionArray=getJSONObjListOfNameValue(selectedActionsList);
		jsonObject.put("selectedActionArray", selectedActionArray);
		
		arrayList.add(jsonObject);
		
		return arrayList;
	}

	public List<JSONObject> getActionsForThisCPs(String selectedRoleId) throws BizLogicException,JSONException
	{
		List<NameValueBean> selectedActionsList=null;
		List<NameValueBean> actionList = null;
		if("7".equals(selectedRoleId))
		{
			actionList = getReformedNameValueBeanList(scientistPrivilegesList);
		}
		else
		{
			selectedActionsList = new ArrayList<NameValueBean>();
			actionList = getReformedNameValueBeanList(Variables.privilegeGroupingMap.get("CP"));
		
			if(selectedRoleId!=null&& selectedRoleId!=""&& ! selectedRoleId.equalsIgnoreCase("-1"))
			{
				List<NameValueBean> actionsListForSelRole = getActionsList(selectedRoleId);
				
				if(actionsListForSelRole!=null && ! actionsListForSelRole.isEmpty())
				{
					for(int count=0;count<actionsListForSelRole.size();count++)
					{
						NameValueBean nameValueBean=(NameValueBean)actionsListForSelRole.get(count);
						if(actionList.contains(nameValueBean))
						{
							selectedActionsList.add(nameValueBean);
						}
					}
				}
			}
		}
		List<JSONObject> arrayList = new ArrayList<JSONObject>();
		JSONObject jsonObject=new JSONObject();

		List<JSONObject> actionJsonArray=getJSONObjListOfNameValue(actionList);
		jsonObject.put("actionJsonArray", actionJsonArray);

		List<JSONObject> selectedActionArray=getJSONObjListOfNameValue(selectedActionsList);
		jsonObject.put("selectedActionArray", selectedActionArray);
		
		arrayList.add(jsonObject);
		
		return arrayList;
	}
	/**
	 * Given a selected role this method will return the list of actions under this role.
	 * @param role
	 * @return List of actions assigned to the selected role
	 */
	public List<NameValueBean> getActionsList(String roleId) 
	{	
		List<NameValueBean> nameValueBeanList=new ArrayList<NameValueBean>();
		if(roleId!=null&& roleId!=""&& ! roleId.equalsIgnoreCase("-1"))
		{
			Set<Privilege> actions;	
			PrivilegeUtility privilegeUtility = new PrivilegeUtility();
			if("7".equals(roleId))
			{
				nameValueBeanList=getReformedNameValueBeanList(scientistPrivilegesList);
			}
			else
			{
			   try
			   { 
				actions = privilegeUtility.getRolePrivileges(roleId);
				  for (Privilege action : actions)
					{
				    	NameValueBean valueBean=new NameValueBean();
				    	valueBean.setName(Utility.getDisplayLabelForUnderscore(action.getName()));
				    	valueBean.setValue(String.valueOf(action.getId()));
				    	nameValueBeanList.add(valueBean);
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
			}
		}
	//	List<NameValueBean> actionsListForSelRole=new ArrayList<NameValueBean>();
	//	List<NameValueBean> actionsListForSelSites=new ArrayList<NameValueBean>();
		
	/*	if(role!=null&& role!=""&& ! role.equalsIgnoreCase("-1"))
		{
			actionsListForSelRole = getActionsForSelRole(role);
		}
		
		if((selectedSiteIds!=null)&& ! selectedSiteIds.isEmpty()&& selectedSiteIds.size()>0)
		{
			actionsListForSelSites = getActionsForSelSites();
		}
		
		if(role!=null && ((selectedSiteIds!=null)&& ! selectedSiteIds.isEmpty()&& selectedSiteIds.size()>0))
		{
			for(int count=0;count<actionsListForSelRole.size();count++)
			{
				NameValueBean nameValueBean=(NameValueBean)actionsListForSelRole.get(count);
				if(actionsListForSelSites.contains(nameValueBean))
				{
					nameValueBeanList.add(nameValueBean);
				}
			}
		}
		else if(role!=null && ((selectedSiteIds==null)||  selectedSiteIds.isEmpty()|| selectedSiteIds.size()==0))
		{
			nameValueBeanList=actionsListForSelRole;
		}
		else if((role==null||role=="") && ((selectedSiteIds!=null)&& ! selectedSiteIds.isEmpty()&& selectedSiteIds.size()>0))
		{
			nameValueBeanList=actionsListForSelSites;
		}
	*/		
		return nameValueBeanList;
		
	}
	
	/**
	 * @param role
	 * @param nameValuBeanList
	 */
	/*
	public  List<NameValueBean> getActionsForSelRole(String roleId)
	{
		List<NameValueBean> nameValuBeanList=new ArrayList<NameValueBean>();
		Set<Privilege> actions;	
		PrivilegeUtility privilegeUtility = new PrivilegeUtility();
		if("7".equals(roleId))
		{
		   nameValuBeanList=getReformedNameValueBeanList(scientistPrivilegesList);
		}
		else
		{
		   try
		   { 
			actions = privilegeUtility.getRolePrivileges(roleId);
			  for (Privilege action : actions)
				{
			    	NameValueBean valueBean=new NameValueBean();
			    	valueBean.setName(Utility.getDisplayLabelForUnderscore(action.getName()));
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
		}
		return nameValuBeanList;
	}
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
				if(siteIdsList.contains(site.getId()))
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
	public SiteUserRolePrivilegeBean setUserPrivilegeSummary(User user,List<Site> userRelatedSites, NameValueBean roleNameValueBean, List<NameValueBean> actionBeanList,boolean isCustChecked)
	{
		SiteUserRolePrivilegeBean surp = new SiteUserRolePrivilegeBean();
		
//		NameValueBean roleNameValueBean=new NameValueBean();
//		roleNameValueBean.setName(role.getName());
//		roleNameValueBean.setValue(role.getId());
		surp.setRole(roleNameValueBean);
		surp.setUser(user);
		surp.setSiteList(userRelatedSites);
		surp.setPrivileges(actionBeanList);
		surp.setCustChecked(isCustChecked);
		
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
	public JSONObject getObjectForUPSummary(String rowId,NameValueBean roleNameValueBean,List<Site> userRelatedSites, User user, List<NameValueBean> actionBeanList,boolean isCustChecked) throws JSONException,BizLogicException 
	{
		JSONObject jsonobject = new JSONObject();
		
		// for role
		String roleName = "";
		if(roleNameValueBean!=null)
		{
			roleName = roleNameValueBean.getName();
		}
		else if(!isCustChecked && roleNameValueBean==null)
		{
			roleName =Constants.DEFAULT_ROLE;
		}
		
		//for sites
		StringBuffer sbForSites = new StringBuffer();
		if(userRelatedSites!=null && userRelatedSites.size()>0)
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
		String sites = sbForSites.toString();

		// for user
		String userName = "";
		if (user != null)
		{
			userName = user.getLoginName();
		}
		else if(!isCustChecked && user==null)
		{
			userName =Constants.ALL_DEFAULT_USERS;
		}
		
		// for actions
		StringBuffer sbForActions = new StringBuffer();
		String actionName="";
		if(actionBeanList!=null && !actionBeanList.isEmpty())
		{
			for (int i = 0; i < actionBeanList.size(); i++) 
			{
				actionName = actionBeanList.get(i).getName();
				if (i > 0)
				{
					sbForActions.append(",");
				}
	//			if((actionName).equals("PHI_ACCESS"))
	//			{
	//				sbForActions.append(ApplicationProperties.getValue(actionName));
	//			}
	//			else
	//			{
					sbForActions.append(actionName);
			//	}
			}
		}
		else if(!isCustChecked && (actionBeanList==null||actionBeanList.isEmpty()))
		{
			actionName =Constants.ALL_DEFAULT_PRIVILEGES;
			
			sbForActions.append(actionName);
		}
		String actions =sbForActions.toString();
		
		jsonobject.append("roleName", roleName);
		jsonobject.append("userName", userName);
		jsonobject.append("sites", sites);
		jsonobject.append("actions", actions);
		jsonobject.append("rowId", rowId);
		
		return jsonobject;
	}
	/**
	 *  Delete row from the map
	 * @param deletedRowsArray
	 * @param rowIdObjectBeanMap
	 */
	public Map<String, SiteUserRolePrivilegeBean> deletePrivilege(Map<String, SiteUserRolePrivilegeBean> rowIdBeanMap,String deletedRowsArray,String operation) 
	{
			String deletedRowId = "";
			SiteUserRolePrivilegeBean surp=null;
			List<String> deletedRowsList = new ArrayList<String>();
			StringTokenizer tokenizer = new StringTokenizer(deletedRowsArray, ",");
			while (tokenizer.hasMoreTokens())
			{
				deletedRowId = tokenizer.nextToken().intern();
				deletedRowsList.add(deletedRowId);
				surp = rowIdBeanMap.get(deletedRowId);
				if((Constants.ADD).equalsIgnoreCase(operation)||((Constants.EDIT).equals(operation)&& surp.isRowEdited()))
				{
					rowIdBeanMap.remove(deletedRowId);
				}
				else if((Constants.EDIT).equals(operation))
				{
					surp.setRowDeleted(true);
					rowIdBeanMap.put(deletedRowId, surp);
				}
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
	public List<JSONObject> addPrivilege(Map<String, SiteUserRolePrivilegeBean> rowIdBeanMap, String userIds, String siteIds, String roleId, String actionIds,boolean isCustChecked,String operation)throws BizLogicException,JSONException, CSException 
	{  
		List<JSONObject> listForUPSummary = new ArrayList<JSONObject>();

		List<Long> siteIdsList = getInputData(siteIds);
		List<Long> userIdsList =getInputData(userIds);
		List<String> actionIdsList = getActionData(actionIds);
		AbstractDAO dao = DAOFactory.getInstance().getDAO(Constants.HIBERNATE_DAO);
		
		JSONObject jsonObject = new JSONObject();

		if(!isCustChecked && (siteIdsList!=null|| !siteIdsList.isEmpty()))
		{
			//CaTissuePrivilegeUtility caTissuePrivilegeUtility = new CaTissuePrivilegeUtility();
			for(int count=0;count<siteIdsList.size();count++)
			{
				long siteId = siteIdsList.get(count);
				if (rowIdBeanMap == null)
				{
					rowIdBeanMap = new HashMap<String, SiteUserRolePrivilegeBean>();
				}
				
				if(userIdsList==null || userIdsList.isEmpty())
				{
					String rowId = "Site_" +siteId;
					
					try
					{
						dao.openSession(null);
					
						Object object = dao.retrieve(Site.class.getName(), siteId);
						Site site  = (Site)object;
						List<Site> sites = new ArrayList<Site>();
						sites.add(site);
						SiteUserRolePrivilegeBean surpBean =setUserPrivilegeSummary(null, sites, null, null,isCustChecked);
						
						updateRowInEditMode(rowIdBeanMap, operation, rowId);
						
						rowIdBeanMap.put(rowId,surpBean);
						
						jsonObject = getObjectForUPSummary(rowId,null,sites,null,null,isCustChecked);
						 
						listForUPSummary.add(jsonObject);
					} 
					catch (DAOException e) 
					{
						e.printStackTrace();
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
							logger.error(daoEx.getMessage(), daoEx);
				    		return null;
						}
					}
				}
				
//				Map<String, SiteUserRolePrivilegeBean> map=caTissuePrivilegeUtility.getAllCurrentAndFuturePrivilegeUsersOnSite(siteId, null, rowIdBeanMap);
//				rowIdBeanMap.putAll(map);
				
//				Set keySet = map.keySet();
//				Iterator iterator = keySet.iterator();
//				
//				while(iterator.hasNext())
//				{
//					String rowId =(String)iterator.next();
//					SiteUserRolePrivilegeBean surp = map.get(rowId);
//					NameValueBean roleNameValueBean = surp.getRole();
//					List<Site> sites = surp.getSiteList();
//					List<NameValueBean> actionBeanList = surp.getPrivileges();
//					User user = surp.getUser();
//					jsonObject=getObjectForUPSummary(rowId,roleNameValueBean,sites,user,actionBeanList);
//					listForUPSummary.add(jsonObject);
//				}
			}
		}
		else
		{
		try 
		{
			for (int k = 0; k < userIdsList.size(); k++) 
			{ 
				String roleName= "";
			//	Role role=null;
				NameValueBean roleNameValueBean = null;
				
				if(!("0").equalsIgnoreCase(roleId))
				{
					SecurityManager securityManager=SecurityManager.getInstance(this.getClass());
					
					try
					{
						Role role=securityManager.getUserProvisioningManager().getRoleById(roleId);
						roleName = role.getName();
						roleNameValueBean = new NameValueBean(roleName, roleId);
					}
					catch (CSException e)
					{
						throw new BizLogicException("Could not get the role",e);
					}
				}
				else
				{
					roleName="Custom";
//					role=new Role();
//					role.setName(roleName);
//					role.setId(new Long(roleId));
					roleNameValueBean = new NameValueBean(roleName , roleId);
				}
				
				 long userId = userIdsList.get(k);
				 dao.openSession(null); 
				 Object object = dao.retrieve(User.class.getName(), userId);
				 User user  = (User)object;
				 
				 List<Site> userRelatedSites = getUserSiteRelation(user, siteIdsList);
				 
				 List<NameValueBean> actionBeanList = getPrivilegesNameValueBeanList(actionIdsList);
				
				 SiteUserRolePrivilegeBean surpBean =setUserPrivilegeSummary(user, userRelatedSites, roleNameValueBean, actionBeanList,isCustChecked);
				
				 String rowId = "" + userId;
				 
				 updateRowInEditMode(rowIdBeanMap, operation, rowId);
				 
				 rowIdBeanMap.put(rowId,surpBean);
				 
				 jsonObject=getObjectForUPSummary(rowId,roleNameValueBean,userRelatedSites,user,actionBeanList,isCustChecked);
				 
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
				logger.error(daoEx.getMessage(), daoEx);
	    		return null;
			}
		}
		}
		return listForUPSummary;
	}
	/**
	 * @param rowIdBeanMap
	 * @param operation
	 * @param rowId
	 */
	private void updateRowInEditMode(Map<String, SiteUserRolePrivilegeBean> rowIdBeanMap, String operation, String rowId) {
		if((Constants.EDIT).equalsIgnoreCase(operation))
		 {
			 SiteUserRolePrivilegeBean tempBean=rowIdBeanMap.get(rowId);
			 if(rowIdBeanMap.containsKey(rowId)&& !tempBean.isRowEdited())
			 {
				 String updatedRowId="Deleted_"+rowId;
				 tempBean.setRowDeleted(true);
				 rowIdBeanMap.remove(rowId);
				 rowIdBeanMap.put(updatedRowId, tempBean);
			 }
		 }
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
public List<String[]> privilegeDataOnTabSwitch(Map<String, SiteUserRolePrivilegeBean>map,String pageOf) throws BizLogicException 
{
		List<String[]> list=null;
		
		if(map!=null && !map.isEmpty() && map.size()>0)
		{
			list=new ArrayList<String[]>();
			
			int mapSize=map.size();
			Object[] surpArray= map.values().toArray();
			Object[] keyArray=map.keySet().toArray();
			SiteUserRolePrivilegeBean bean=null;
			
			for(int j=0;j<mapSize;j++)
			{
				String[] array=new String[5];
				String rowId="";
				
				bean =(SiteUserRolePrivilegeBean)surpArray[j];
				
				if(!bean.isRowDeleted())
				{
					//for row id
					for(int count=0;count<keyArray.length;count++)
					{
						SiteUserRolePrivilegeBean beanForKey=map.get(keyArray[count]);
						if (bean==beanForKey)
						{
							rowId=(String)keyArray[count];
						}
					}
					
					//for role
					String roleName = "";
					if(bean.getRole()!=null)
					{
						roleName= bean.getRole().getName();
					}
					else if(!bean.isCustChecked())
					{
						roleName = Constants.DEFAULT_ROLE;
					}
					
					// for site
					String sites = displaySiteNames(bean);
					
					// for privileges
					String actionNames = displayPrivilegesNames(bean);
					
				//	if((! (Constants.PAGE_OF_USER).equalsIgnoreCase(pageOf))&&!((Constants.PAGEOF_USER_PROFILE).equals(pageOf)))
					if((pageOf!=null && (Constants.PAGEOF_ASSIGN_PRIVILEGE).equalsIgnoreCase(pageOf)))
					{
						// for user
						String userName ="";
						if(bean.getUser()!=null)
						{
							userName = bean.getUser().getFirstName();
						}
						else if(!bean.isCustChecked())
						{
							userName = Constants.ALL_DEFAULT_USERS;
						}
						array[0]=userName;
					}
					else if(pageOf!=null)
					{
						// for CP
						if(bean.getCollectionProtocol() != null)
						{
							String cpName = bean.getCollectionProtocol().getShortTitle();
							if(cpName!=null && !cpName.equalsIgnoreCase(""))
							{
								array[0]=cpName;
							}
							else
							{
								array[0]=Constants.NA;
							}
						}
						else if(bean.isAllCPChecked())
						{
							array[0]=Constants.ALL_CURRENT_AND_FUTURE;
						}
						else if(!bean.isAllCPChecked())
						{
							array[0]=Constants.NA;
						}
					}
					
					array[1]=sites;
					array[2]=roleName;
					array[3]=actionNames;
					array[4]=rowId;
					list.add(j,array);
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
		StringBuffer sbForActions = new StringBuffer();
		List<NameValueBean> privilegesList = bean.getPrivileges();
		if(privilegesList!=null && !privilegesList.isEmpty())
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
				sbForActions.append(Utility.getDisplayLabelForUnderscore(actionName));
				
	//					if((actionName).equals("PHI_ACCESS"))
	//					{
	//						sbForActions.append(ApplicationProperties.getValue(actionName));
	//					}
	//					else
	//					{
	//						sbForActions.append(Utility.getDisplayLabelForUnderscore(actionName));
	//					}
	//		 
			}
		}
		else if(!bean.isCustChecked())
		{
			sbForActions.append(Constants.ALL_DEFAULT_PRIVILEGES);
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
		StringBuffer sbForSitesNames = new StringBuffer();
		if(bean.getSiteList()!=null && ! bean.getSiteList().isEmpty())
		{
			for (int i = 0; i < bean.getSiteList().size(); i++) 
			{
				if (i > 0)
				{
					sbForSitesNames.append(",");
				}
		
				Site site=bean.getSiteList().get(i);
				sbForSitesNames.append(site.getName());
			}
		}
		
		String sites = sbForSitesNames.toString();
		return sites;
	}
	
	public List<JSONObject> editPrivilege (Map<String, SiteUserRolePrivilegeBean> rowIdBeanMap,String selectedRow)throws JSONException,BizLogicException 
	{
		List<JSONObject> privilegeList = new ArrayList<JSONObject>();
		if(rowIdBeanMap!=null && ! rowIdBeanMap.isEmpty())
		{
			SiteUserRolePrivilegeBean surp=rowIdBeanMap.get(selectedRow);
			
			
			JSONObject jsonObject=new JSONObject();
			
			// for customize checkbox
			jsonObject.put("isCustChecked",surp.isCustChecked());
			
			// for site
			JSONArray siteJsonArray=new JSONArray();
			
			if(surp.getSiteList()!=null&&!surp.getSiteList().isEmpty())
			{
				for(Site site:surp.getSiteList())
				{
					long siteId=site.getId();
					siteJsonArray.put(siteId);
				}
			}
			jsonObject.put("siteJsonArray",siteJsonArray );
	
			// for User
			if(surp.getUser()!=null)
			{
				long selectedUserId=surp.getUser().getId();
				jsonObject.append("selectedUserId", selectedUserId);
			}
			
			List<Site> siteList=surp.getSiteList();
			List<Long> selectedSitesList=new ArrayList<Long>();
			for(int z=0;z<siteList.size();z++)
			{
				selectedSitesList.add(siteList.get(z).getId());
			}
	
			List<JSONObject> userNameValueBeanList = getUsersForThisSites(selectedSitesList);
			jsonObject.put("userJsonArray",userNameValueBeanList);
			
			// for Role
			String roleId = "";
			if(surp.getRole()!=null)
			{
				roleId=surp.getRole().getValue();
			
				jsonObject.append("roleId", roleId);
			}
			
			// for Privileges
			List<JSONObject> selActionJsonArray=null;
			List<JSONObject> actionJsonArray=null;
			List<NameValueBean> actionList=null;
			List<NameValueBean> privileges=surp.getPrivileges();
			
			if(("7").equals(roleId))
			{
				actionList = getReformedNameValueBeanList(scientistPrivilegesList);
			}
			else
			{
				actionList = getActionList(false);
			}
			
			if(privileges!=null)
			{
				selActionJsonArray = getJSONObjListOfNameValue(privileges);
				jsonObject.put("selActionJsonArray",selActionJsonArray );
			}
			
			actionJsonArray = getJSONObjListOfNameValue(actionList);
			jsonObject.put("actionJsonArray",actionJsonArray);
			
			privilegeList.add(jsonObject);
		}
		return privilegeList;
	}
/*
	public ActionErrors  validationForAddPrivOnUserPage(String pageOf,List<Long> siteIdsList,List<Long> cpIdsList,String roleId,List<String> actionIdsList,boolean isAllCPChecked)
	{
        boolean errorFlagForSite=false;
	    boolean errorFlagForAction=false;
	    boolean errorFlagForRole=false;
	    boolean errorFlagForCP=false;
	    boolean tempFlagForSite = false;
	    
	    ActionErrors errors = new ActionErrors();
	    
	    if(actionIdsList!=null && !actionIdsList.isEmpty())
        {
      	  for(String actionId:actionIdsList)
      	  {
      		  if(actionId.equals("12"))
      		  {
      			  tempFlagForSite=true;	
              		if(cpIdsList!=null && cpIdsList.size()==0)
              		{
              			errorFlagForCP=true;
              		}
      		  }
      	  }
        }
		  if((siteIdsList==null ||siteIdsList.isEmpty()||siteIdsList.size()==0) && !tempFlagForSite)
          {
                errorFlagForSite=true;
          }
          if(roleId==null||roleId.equals(""))
          {
                errorFlagForRole=true;
          }
          if(actionIdsList==null ||actionIdsList.isEmpty()||actionIdsList.size()==0)
          {
        	  errorFlagForAction=true; 
          }
          
          if(errorFlagForSite==true||errorFlagForAction==true||errorFlagForRole==true||errorFlagForCP==true)
          {
			  ActionError error = new ActionError("ShoppingCart.emptyCartTitle");
			  errors.add(ActionErrors.GLOBAL_ERROR, error);
          }
          return errors;
	}
     public ActionErrors  validationForAddPrivOnCPPage(String pageOf,List<Long> siteIdsList,List<Long> userIdsList,String roleId,List<String> actionIdsList)
    {
          boolean errorFlagForSite=false;
      	  boolean errorFlagForUser=false;
      	  boolean errorFlagForAction=false;
      	  boolean errorFlagForRole=false;
      	  
      	ActionErrors errors = null;
      	    
      	if(siteIdsList==null ||siteIdsList.isEmpty()||siteIdsList.size()==0)
        {
              errorFlagForSite=true;
        }
        if(userIdsList==null ||userIdsList.isEmpty()||userIdsList.size()==0)
        {
        	errorFlagForUser = true;
        }
        if(roleId==null||roleId.equals(""))
        {
              errorFlagForRole=true;
        }
        if(actionIdsList==null ||actionIdsList.isEmpty()||actionIdsList.size()==0)
        {
      	    errorFlagForAction=true; 
        }
          if(errorFlagForSite==true||errorFlagForUser==true||errorFlagForAction==true||errorFlagForRole==true)
	      {
        	  errors = new ActionErrors();
			  ActionError error = new ActionError("ShoppingCart.emptyCartTitle");
			  errors.add(ActionErrors.GLOBAL_ERROR, error);
	      }
          return errors;
 }
 
 */
// for user page--
public List<JSONObject> addPrivilegeForUserPage(Map<String, SiteUserRolePrivilegeBean> rowIdBeanMap, String cpIds, String siteIds, String roleId, String actionIds,boolean isAllCPChecked,String operation)throws BizLogicException,JSONException, CSException 
{  
	List<JSONObject> listForUPSummary = new ArrayList<JSONObject>();
	List<Long> siteIdsList = new ArrayList<Long>();
	List<Long> cpIdsList = new ArrayList<Long>();
	List<String> actionIdsList = new ArrayList<String>();

	siteIdsList = getInputData(siteIds);
	cpIdsList = getInputData(cpIds);
	actionIdsList = getActionData(actionIds);
	AbstractDAO dao = DAOFactory.getInstance().getDAO(Constants.HIBERNATE_DAO);

	try 
	{
		if(cpIdsList!=null&&!cpIdsList.isEmpty())
		{
			for (int k = 0; k < cpIdsList.size(); k++) 
			{ 
				String roleName= "";
				Role role=null;
				
				if(!("0").equalsIgnoreCase(roleId))
				{
					SecurityManager securityManager=SecurityManager.getInstance(this.getClass());
					
					try
					{
						role=securityManager.getUserProvisioningManager().getRoleById(roleId);
					}
					catch (CSException e)
					{
						throw new BizLogicException("Could not get the role",e);
					}
				}
				else
				{
					roleName="Custom";
					role=new Role();
					role.setName(roleName);
					role.setId(new Long(roleId));
				}
				
				 long cpId = cpIdsList.get(k);
				 dao.openSession(null); 
				 Object object = dao.retrieve(CollectionProtocol.class.getName(), cpId);
				 CollectionProtocol collectionProtocol  = (CollectionProtocol)object;
				 
				 List<Site> cpRelatedSites=null;
				 
				 if(siteIdsList!=null && siteIdsList.size()>0)
				 {
					 cpRelatedSites = getCPSiteRelationForUserPage(collectionProtocol, siteIdsList);
				 }
				 
				 List<NameValueBean> actionBeanList = getPrivilegesNameValueBeanList(actionIdsList);

				 SiteUserRolePrivilegeBean surpBean =setUserPrivilegeSummaryForUserPage(collectionProtocol, cpRelatedSites, role, actionBeanList,isAllCPChecked,operation);
				
				 String rowId = "CP_"+ cpId;
				 updateRowInEditMode(rowIdBeanMap, operation, rowId);
				 rowIdBeanMap.put(rowId,surpBean);
				 
				 JSONObject jsonObject=getObjectForUPSummaryForUserPage(rowId,role,cpRelatedSites,collectionProtocol,actionBeanList,isAllCPChecked);
				 
				 listForUPSummary.add(jsonObject);
			}
		}
		else
		{
			for (int k = 0; k < siteIdsList.size(); k++) 
			{ 
				String roleName= "";
				Role role=null;
				
				if(!("0").equalsIgnoreCase(roleId))
				{
					SecurityManager securityManager=SecurityManager.getInstance(this.getClass());
					
					try
					{
					role=securityManager.getUserProvisioningManager().getRoleById(roleId);
					roleName=role.getName();
					}
					catch (CSException e)
					{
						throw new BizLogicException("Could not get the role",e);
					}
				}
				else
				{
					roleName="Custom";
					role=new Role();
					role.setName(roleName);
					role.setId(new Long(roleId));
				}
				
				 long siteId = siteIdsList.get(k);
				 dao.openSession(null); 
				 Object object = dao.retrieve(Site.class.getName(), siteId);
				 Site site  = (Site)object;
				 List <Site> siteLists = new ArrayList<Site>();
				 siteLists.add(site);
				 
				 List<NameValueBean> actionBeanList = getPrivilegesNameValueBeanList(actionIdsList);
				
				 SiteUserRolePrivilegeBean surpBean =setUserPrivilegeSummaryForUserPage(null, siteLists, role, actionBeanList,isAllCPChecked,operation);
				
				 String rowId = "" + siteId;
				 
				 updateRowInEditMode(rowIdBeanMap, operation, rowId);

				 rowIdBeanMap.put(rowId,surpBean);
				 
				 JSONObject jsonObject=getObjectForUPSummaryForUserPage(rowId,role,siteLists,null,actionBeanList,isAllCPChecked);
				 
				 listForUPSummary.add(jsonObject);
			}
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
			logger.error(daoEx.getMessage(), daoEx);
    		return null;
		}
	}
	return listForUPSummary;
}


public List<Site> getCPSiteRelationForUserPage(CollectionProtocol collectionProtocol,List<Long> siteIdsList) 
{
	List<Site> list = new ArrayList<Site>();
	Collection<Site> siteCollection = collectionProtocol.getSiteCollection();
	if(siteCollection !=null || !siteCollection.isEmpty())
	{
		for (Site site : siteCollection)
		{
			if(siteIdsList.contains(site.getId()))
			{
				list.add(site);
			}
		}
	}
	
	return list;
}

// Returns SiteUserRolePrivilegeBean Bean for user page
public SiteUserRolePrivilegeBean setUserPrivilegeSummaryForUserPage(CollectionProtocol collectionProtocol,List<Site> cpRelatedSites, Role role, List<NameValueBean> actionBeanList,boolean isAllCPChecked,String operation)
{
	SiteUserRolePrivilegeBean surp = new SiteUserRolePrivilegeBean();
	
	surp.setCollectionProtocol(collectionProtocol);

	NameValueBean roleNameValueBean=new NameValueBean();
	roleNameValueBean.setName(role.getName());
	roleNameValueBean.setValue(role.getId());
	surp.setRole(roleNameValueBean);
	
	surp.setSiteList(cpRelatedSites);
	
	surp.setPrivileges(actionBeanList);
	
	surp.setAllCPChecked(isAllCPChecked);
	
	return surp;
}

// Returns summmary Object
public JSONObject getObjectForUPSummaryForUserPage(String rowId,Role role,List<Site> cpRelatedSites,  CollectionProtocol collectionProtocol, List<NameValueBean> actionBeanList,boolean isAllCPChecked) throws JSONException,BizLogicException 
{
	JSONObject jsonobject = new JSONObject();
	
	// for role
	String roleName = "";
	if(role!=null)
	{
		roleName = role.getName();
	}
	//for sites
	StringBuffer sbForSites = new StringBuffer();
	if(cpRelatedSites!=null&&cpRelatedSites.size()>0)
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
	String sites = sbForSites.toString();

	// for cp
	String cpName = "";
	if (collectionProtocol != null)
	{
		cpName = collectionProtocol.getShortTitle();
	}
	else if(isAllCPChecked && cpRelatedSites!=null)
	{
		cpName="All Current and Future";
	}
	else if(cpRelatedSites!=null && ! isAllCPChecked)
	{
		cpName="N/A";
	}
	
	// for actions
	StringBuffer sbForActions = new StringBuffer();
	for (int i = 0; i < actionBeanList.size(); i++) 
	{
		if (i > 0)
		{
			sbForActions.append(",");
		}
		sbForActions.append(actionBeanList.get(i).getName());
	}
	String actions =sbForActions.toString();
	
	jsonobject.append("roleName", roleName);
	jsonobject.append("cpName", cpName);
	jsonobject.append("sites", sites);
	jsonobject.append("actions", actions);
	jsonobject.append("rowId", rowId);
	return jsonobject;
}


public List<JSONObject> editPrivilegeForUserPage (Map<String, SiteUserRolePrivilegeBean> rowIdBeanMap,String selectedRow)throws JSONException,BizLogicException 
{
	SiteUserRolePrivilegeBean surp=rowIdBeanMap.get(selectedRow);
	
	List<JSONObject> privilegeList = new ArrayList<JSONObject>();
	JSONObject jsonObject=new JSONObject();
	long selectedCPId=0;
	
	// for site
	JSONArray siteJsonArray=new JSONArray();
	
	if(surp.getSiteList()!=null&&surp.getSiteList().size()>0)
	{
		for(Site site:surp.getSiteList())
		{
			long siteId=site.getId();
			siteJsonArray.put(siteId);
		}
	}
	jsonObject.put("siteJsonArray",siteJsonArray );

	// for CP
	if(surp.getCollectionProtocol()!=null)
	{
		selectedCPId=surp.getCollectionProtocol().getId();
	}
	jsonObject.append("selectedCPId", selectedCPId);
	
	List<Site> siteList=surp.getSiteList();
	List<JSONObject> cpNameValueBeanList=new ArrayList<JSONObject>();
	if(siteList!=null&& !siteList.isEmpty())
	{
		List<Long> selectedSitesList=new ArrayList<Long>();
		for(int z=0;z<siteList.size();z++)
		{
			selectedSitesList.add(siteList.get(z).getId());
		}
	
		 cpNameValueBeanList=getCPsForThisSites(selectedSitesList);
	 }
	else
	{
		List<NameValueBean> list=getCPList(false);
		
		cpNameValueBeanList=getJSONObjListOfNameValue(list);
	}
	jsonObject.put("cpJsonArray",cpNameValueBeanList);
	
	// for Role
	String roleId=surp.getRole().getValue();
	
	jsonObject.append("roleId", roleId);
	
	// for Privileges
	List<JSONObject> selActionJsonArray=null;
	List<JSONObject> actionJsonArray=null;
	List<NameValueBean> actionList=null;
	List<NameValueBean> privileges = surp.getPrivileges();
	
	if(("7").equals(roleId))
	{
		actionList =  getReformedNameValueBeanList(scientistPrivilegesList);
	}
	else
	{
		actionList = getActionListForUserPage(false);
	}
	
	selActionJsonArray = getJSONObjListOfNameValue(privileges);
	actionJsonArray = getJSONObjListOfNameValue(actionList);
	
	jsonObject.put("selActionJsonArray",selActionJsonArray );
	jsonObject.put("actionJsonArray",actionJsonArray);
	
	privilegeList.add(jsonObject);
	return privilegeList;
}

/**
 * @param actionIdsList
 * @return
 * @throws CSException
 */
// Takes List of ids of actions and gives list of NameValueBean pairs of actions.
public List<NameValueBean> getPrivilegesNameValueBeanList(List<String> actionIdsList) throws CSException {
	List<NameValueBean> actionBeanList=new ArrayList<NameValueBean>();
	 NameValueBean nameValueBean=null;
	 for(int len=0;len<actionIdsList.size();len++)
	 {
		 nameValueBean=new NameValueBean();
		 String actionId=actionIdsList.get(len);
		 String actionName="";
		 PrivilegeUtility privilegeUtility = new PrivilegeUtility();
		 Privilege action=null;
			
			action = privilegeUtility.getPrivilegeById(actionId);
			actionName=action.getName();
			nameValueBean.setName(Utility.getDisplayLabelForUnderscore(actionName));
			nameValueBean.setValue(actionId);
			actionBeanList.add(nameValueBean);
	 }
	return actionBeanList;
}

// To remove "Select" opteion from list.
public List<NameValueBean> removeSelect(List<NameValueBean> list) 
{
	if(list != null && !list.isEmpty()&& list.size()>0)
	{
		for(int len=0;len<list.size();len++)
		{
			String val=""+((NameValueBean)list.get(len)).getValue();
			String name=((NameValueBean)list.get(len)).getName();
			if(((""+Constants.SELECT_OPTION_VALUE).equalsIgnoreCase(val))&& ((Constants.SELECT_OPTION).equalsIgnoreCase(name)))
			{
				list.remove(len);
			}
		}
	}
	return list;
}
// Takes list of NameValueBean as input and Gives JSONArray of JSON Objects having Name and Value as  fields.
	public List<JSONObject>  getJSONObjListOfNameValue(List<NameValueBean> list) throws JSONException
	{
		List<JSONObject> jsonObjectList= new ArrayList<JSONObject>();
		if(list!=null && !list.isEmpty() && list.size()>0)
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

// Takes String ,having ids joined with separartor ",",as a input and Returns List of ids of long type.
public List<Long> getInputData(String ids)
{
	long tempId; 
	List<Long> list = new ArrayList<Long>();
	if(ids!=null && ! ids.equals(""))
	{
		list = new ArrayList<Long>();
		
		StringTokenizer tokenizer = new StringTokenizer("" + ids, ",");
		while (tokenizer.hasMoreTokens()) 
		{
			tempId =  Long.parseLong(tokenizer.nextToken());
			list.add(tempId);
		}
	}
	return list;
}

public List<NameValueBean> getCommonElesList(List<NameValueBean> list1,List<NameValueBean> list2) {
	List<NameValueBean> resultList = new ArrayList<NameValueBean>();
	if((list1!=null && ! list1.isEmpty())&&(list2!=null && ! list2.isEmpty()))
	{
		for(NameValueBean nameValueBean:list2)
		{
			if(list1.contains(nameValueBean))
			{
				resultList.add(nameValueBean);
			}
		}
	}
	return resultList;
}
/*
//Takes String having cpIds concatenated by ',' and returns List of CP ids.
public List<Long> getCPData(String cpIds) 
{
	long cpId;
	List<Long> cpIdsList = new ArrayList<Long>();
	
	StringTokenizer tokenizer = new StringTokenizer("" + cpIds, ",");
	while (tokenizer.hasMoreTokens())
	{
		cpId = Long.parseLong(tokenizer.nextToken());
		cpIdsList.add(cpId);
	}
	return cpIdsList;
}
*/
/**
 * Gets list of actions when page is loaded
 * @param isToExcludeDisabled
 * @return List of user NameVAlueBean objects.
 * @throws BizLogicException
 */
/*
public List<NameValueBean> getAllPrivilegeList(boolean isToExcludeDisabled) throws BizLogicException
{     
	List<NameValueBean> privilegeNameValueBeanList = Utility.getAllPrivileges();
	for (int i=0;i<privilegeNameValueBeanList.size();i++)
	{
		NameValueBean bean=(NameValueBean)privilegeNameValueBeanList.get(i);
		
		NameValueBean privilegeNameValueBean = new NameValueBean(Utility.getDisplayLabelForUnderscore(bean.getName()),bean.getValue());
		privilegeNameValueBeanList.add(privilegeNameValueBean);
	}
	return privilegeNameValueBeanList;
}
*/
/*	
public List<NameValueBean> getActionsForSelSites() 
{
	List<NameValueBean> tempList=Variables.privilegeGroupingMap.get("SITE");
	List<NameValueBean> actionsListForSelSites=new ArrayList<NameValueBean>();
	NameValueBean nameValueBean=null;
	
	for(int i=0;i<tempList.size();i++)
	{
		nameValueBean=new NameValueBean();
		String privilegeName=((NameValueBean)tempList.get(i)).getName();
		String privilegeValue=((NameValueBean)tempList.get(i)).getValue();
		nameValueBean.setName(Utility.getDisplayLabelForUnderscore(privilegeName));
		nameValueBean.setValue(privilegeValue);
		actionsListForSelSites.add(nameValueBean);
	}
	return actionsListForSelSites;
}
public List<NameValueBean> getActionsForSelCPs() 
{
	List<NameValueBean> tempList=Variables.privilegeGroupingMap.get("CP");
	List<NameValueBean> actionsListForSelSites=new ArrayList<NameValueBean>();
	NameValueBean nameValueBean=null;
	
	for(int i=0;i<tempList.size();i++)
	{
		nameValueBean=new NameValueBean();
		String privilegeName=((NameValueBean)tempList.get(i)).getName();
		String privilegeValue=((NameValueBean)tempList.get(i)).getValue();
		nameValueBean.setName(Utility.getDisplayLabelForUnderscore(privilegeName));
		nameValueBean.setValue(privilegeValue);
		actionsListForSelSites.add(nameValueBean);
	}
	return actionsListForSelSites;
}


*/

/*public List<NameValueBean> getPrivilegesForScientist() 
{
	List<NameValueBean> list=new ArrayList<NameValueBean>();
	
	NameValueBean valueBean=new NameValueBean();
    valueBean.setName("View Data");
    valueBean.setValue("30");
    list.add(valueBean);
    return list;
}*/


/**
 *Create the List of siteIds from request data
 * @param assignPrivilegePageBizLogic
 * @param request
 * @return List<Integer> list of siteIds 
 */
/*
public List<Long> getSiteData(String siteIds)
{
	
	List<Long> siteIdsList = new ArrayList<Long>();
	if(!("").equalsIgnoreCase(siteIds)&& siteIds!=null)
	{
		long siteId;
		StringTokenizer tokenizer = new StringTokenizer("" + siteIds, ",");
		while (tokenizer.hasMoreTokens()) {
			siteId = Long.parseLong(tokenizer.nextToken());
			siteIdsList.add(siteId);
		}
	}
	return siteIdsList;
}

*/

/**
 * Create the List of userIds from request data
 * @param assignPrivilegePageBizLogic
 * @param request
 * @return List<Long> list of userIds
 */
/*
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
*/
}