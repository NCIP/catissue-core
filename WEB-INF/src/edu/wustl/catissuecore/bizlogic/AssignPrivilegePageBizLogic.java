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
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.multiRepository.bean.SiteUserRolePrivilegeBean;
import edu.wustl.catissuecore.util.global.Constants;
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
			Logger.out.debug(e.getMessage(), e);
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

		cpNameValueBeanList=removeSelect(cpNameValueBeanList);
		
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
       
        ListIterator iterator = roleList.listIterator();
        
        List<NameValueBean> roleNameValueBeanList = new ArrayList<NameValueBean>();
        roleNameValueBeanList.add(0,new NameValueBean(Constants.SELECT_OPTION,"-1"));
        roleNameValueBeanList.add(new NameValueBean("Custom","0"));
       
        while (iterator.hasNext())
        {
            Role role = (Role) iterator.next();
            NameValueBean nameValueBean = new NameValueBean();
            if(! ("pageOfUserAdmin").equalsIgnoreCase(pageOf))
            {
            	if(!(("SUPERADMINISTRATOR").equals(role.getName()))&&!(("Administrator").equals(role.getName())))
            	{
            		 nameValueBean.setName(role.getName());
            		 nameValueBean.setValue(String.valueOf(role.getId()));
        	         roleNameValueBeanList.add(nameValueBean);
            	}
            	 
            }
            else
            {
            	if(!("SUPERADMINISTRATOR".equals(role.getName())))
                {
    	            nameValueBean.setName(role.getName());
    	        }
                else
                {
                	nameValueBean.setName("Super Administrator");
                }
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
	
	
	public List<NameValueBean> getActionListForUserPage(boolean isToExcludeDisabled) throws BizLogicException
	{     
		List<NameValueBean> privilegeNameValueBeanList = new ArrayList<NameValueBean>();
		List<NameValueBean> cpPrivilegesList = Variables.privilegeGroupingMap.get("CP");
		
		List<NameValueBean> sitePrivilegesList = Variables.privilegeGroupingMap.get("SITE"); 
		
		privilegeNameValueBeanList.addAll(sitePrivilegesList); 
		privilegeNameValueBeanList.addAll(cpPrivilegesList);
		List<NameValueBean> privList = new ArrayList<NameValueBean>();
		for (NameValueBean nmv  : privilegeNameValueBeanList)
		{
			NameValueBean privilegeNameValueBean = new NameValueBean(Utility.getDisplayLabelForUnderscore(nmv.getName()), nmv.getValue());
			privList.add(privilegeNameValueBean);
		}
		return privList;
	}
	
	/**
	 * Gets list of actions when page is loaded
	 * @param isToExcludeDisabled
	 * @return List of user NameVAlueBean objects.
	 * @throws BizLogicException
	 */
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
			Set<NameValueBean> userSet = new HashSet<NameValueBean>();
			for(int i=0;i<selectedSitesList.size();i++)
			{
				long siteId=selectedSitesList.get(i);
				
				userSet.addAll(getUsersList(siteId));
			}
			list.addAll(userSet);
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
	

	public List<JSONObject> getCPsForThisSites(List<Long> selectedSitesList) throws BizLogicException,JSONException
	{
		List<JSONObject> arrayList=new ArrayList<JSONObject>();
		List<NameValueBean> cpsList = new ArrayList<NameValueBean>();
		
		cpsList = getCPsForSelectedSites(selectedSitesList);
		
		if(cpsList!=null && !cpsList.isEmpty()&& cpsList.size()>0)
		{
			arrayList = getJSONObjListOfNameValue(cpsList);
		}
		return arrayList;
	}
	
	public List<NameValueBean> getCPsForSelectedSites(List<Long> selectedSitesList)throws BizLogicException
	{
		List<NameValueBean> list = new ArrayList<NameValueBean>();
		if(selectedSitesList!=null && ! selectedSitesList.isEmpty())
		{
			Set<NameValueBean> cpSet = new HashSet<NameValueBean>();
			long siteId=0;
			for(int i=0;i<selectedSitesList.size();i++)
			{
				 siteId=(long)selectedSitesList.get(i);
				 cpSet.addAll(getCPsList(siteId));
			}
			list.addAll(cpSet);
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
		if (pageOf != null && !pageOf.equalsIgnoreCase("pageOfUserAdmin"))
		{
			actionList=getActionList(false);
		}
		else 
		{
			if((selectedCPIds!=null)&& ! selectedCPIds.isEmpty()&& selectedCPIds.size()>0)
			{
				actionList = getActionsForSelCPs();
			}
			
			else if((selectedSiteIds!=null)&& ! selectedSiteIds.isEmpty()&& selectedSiteIds.size()>0)
			{
				if(isAllCPChecked)
				{
					actionList = getActionListForUserPage(false);
				}
				else
				{
					actionList = getActionsForSelSites();
				}
			}
			else 
			{
				actionList = getActionListForUserPage(false);
			}
		}
		
		List<NameValueBean> selectedActionsList = getActionsList(roleId);

		List<JSONObject> arrayList = new ArrayList<JSONObject>();
		JSONObject jsonObject=new JSONObject();
	
		List<JSONObject> actionJsonArray=getJSONObjListOfNameValue(actionList);
		jsonObject.put("actionJsonArray", actionJsonArray);
	
		List<JSONObject> selectedActionArray=getJSONObjListOfNameValue(selectedActionsList);
		jsonObject.put("selectedActionArray", selectedActionArray);
		
		arrayList.add(jsonObject);
		
		return arrayList;
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
			actionList =  getActionsForSelSites();
		}
	//	actionList =  getActionsForSelSites();
		List<NameValueBean> selectedActionsList=new ArrayList<NameValueBean>();
		
		if(selectedRoleId!=null&& selectedRoleId!=""&& ! selectedRoleId.equalsIgnoreCase("-1"))
		{
			List<NameValueBean> actionsListForSelRole = getActionsForSelRole(selectedRoleId);
			for(int count=0;count<actionsListForSelRole.size();count++)
			{
				NameValueBean nameValueBean=(NameValueBean)actionsListForSelRole.get(count);
				if(actionList.contains(nameValueBean))
				{
					selectedActionsList.add(nameValueBean);
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
		List<NameValueBean> selectedActionsList=new ArrayList<NameValueBean>();
		List<NameValueBean> actionList = getActionsForSelCPs();
		
		if(selectedRoleId!=null&& selectedRoleId!=""&& ! selectedRoleId.equalsIgnoreCase("-1"))
		{
			List<NameValueBean> actionsListForSelRole = getActionsForSelRole(selectedRoleId);
			for(int count=0;count<actionsListForSelRole.size();count++)
			{
				NameValueBean nameValueBean=(NameValueBean)actionsListForSelRole.get(count);
				if(actionList.contains(nameValueBean))
				{
					selectedActionsList.add(nameValueBean);
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
	public List<NameValueBean> getActionsList(String role) 
	{	
		List<NameValueBean> nameValueBeanList=new ArrayList<NameValueBean>();
		if(role!=null&& role!=""&& ! role.equalsIgnoreCase("-1"))
		{
			nameValueBeanList = getActionsForSelRole(role);
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
	public  List<NameValueBean> getActionsForSelRole(String role)
	{
		List<NameValueBean> nameValuBeanList=new ArrayList<NameValueBean>();
		Set<Privilege> actions;	
		PrivilegeUtility privilegeUtility = new PrivilegeUtility();
		if("7".equals(role))
		{
		   NameValueBean scNameValueBean=null;
		   for(int count=0;count<scientistPrivilegesList.size();count++)
		   {
			  scNameValueBean =new NameValueBean();
			  String scPrivilegeName=((NameValueBean)scientistPrivilegesList.get(count)).getName();
			  scNameValueBean.setName(Utility.getDisplayLabelForUnderscore(scPrivilegeName));
			  scNameValueBean.setValue(((NameValueBean)scientistPrivilegesList.get(count)).getValue());
			  nameValuBeanList.add(scNameValueBean);
		   }
		}
		else
		{
		   try
		   { 
			actions = privilegeUtility.getRolePrivileges(role);
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
	public SiteUserRolePrivilegeBean setUserPrivilegeSummary(User user,List<Site> userRelatedSites, Role role, List<NameValueBean> actionBeanList)
	{
		SiteUserRolePrivilegeBean surp = new SiteUserRolePrivilegeBean();
		
		NameValueBean roleNameValueBean=new NameValueBean();
		roleNameValueBean.setName(role.getName());
		roleNameValueBean.setValue(role.getId());
		surp.setRole(roleNameValueBean);
		
		surp.setUser(user);
		surp.setSiteList(userRelatedSites);
		surp.setPrivileges(actionBeanList);

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
	public JSONObject getObjectForUPSummary(List<Site> userRelatedSites, String rowId, User user, String roleName, List<String> actionIdsList,String roleId) throws JSONException,BizLogicException 
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
			String actionName="";
			Privilege action;
			PrivilegeUtility privilegeUtility = new PrivilegeUtility();
			if("7".equals(roleId))
			{
				List<NameValueBean>  scPrivilegesList = scientistPrivilegesList;
				for(int m=0;m<scPrivilegesList.size();m++)
				{
					NameValueBean nameValueBean=scPrivilegesList.get(m);
					actionName=nameValueBean.getName();
					sbForActions.append(actionName);
				}
			}
			else
			{
				try 
				{
					action = privilegeUtility.getPrivilegeById(actionIdsList.get(i));
					actionName=action.getName();
				}
				catch (CSException e)
				{
					throw new BizLogicException("Could not get the action",e);
				}
				
		//	}
			
			if((actionName).equals("PHI_ACCESS"))
			{
				sbForActions.append(ApplicationProperties.getValue(actionName));
			}
			else
			{
				sbForActions.append(Utility.getDisplayLabelForUnderscore(actionName));
			}
			}
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
				if((Constants.ADD).equalsIgnoreCase(operation))
				{
					rowIdBeanMap.remove(deletedRowId);
				}
				else
				{
					surp = rowIdBeanMap.get(deletedRowId);
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
	public List<JSONObject> addPrivilege(Map<String, SiteUserRolePrivilegeBean> rowIdBeanMap, String userIds, String siteIds, String roleId, String actionIds,String operation)throws BizLogicException,JSONException, CSException 
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
				
				 long userId = userIdsList.get(k);
				 dao.openSession(null); 
				 Object object = dao.retrieve(User.class.getName(), userId);
				 User user  = (User)object;
				 
				 List<Site> userRelatedSites = getUserSiteRelation(user, siteIdsList);
				 
				 List<NameValueBean> actionBeanList = getPrivilegesNameValueBeanList(actionIdsList);
				
				 SiteUserRolePrivilegeBean surpBean =setUserPrivilegeSummary(user, userRelatedSites, role, actionBeanList);
				
				 String rowId = "" + userId;
				 if((Constants.EDIT).equalsIgnoreCase(operation))
				 {
					 if(rowIdBeanMap.containsKey(rowId))
					 {
						 String updatedRowId="Updated_"+rowId;
						 SiteUserRolePrivilegeBean tempBean=rowIdBeanMap.get(rowId);
						 tempBean.setRowDeleted(true);
						 rowIdBeanMap.remove(rowId);
						 rowIdBeanMap.put(updatedRowId, tempBean);
						 
					 }
				 }
				 rowIdBeanMap.put(rowId,surpBean);
				 
				 JSONObject jsonObject=getObjectForUPSummary(userRelatedSites,rowId,user,roleName,actionIdsList,roleId);
				 
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
public List<String[]> privilegeDataOnTabSwitch(Map<String, SiteUserRolePrivilegeBean>map,String pageOf) throws BizLogicException 
{
		List<String[]> list=new ArrayList<String[]>();
		
		int mapSize=map.size();
		Object[] surpArray= map.values().toArray();
		SiteUserRolePrivilegeBean bean=null;
		Object[] keyArray=map.keySet().toArray();
		
		for(int j=0;j<mapSize;j++)
		{
			String[] array=new String[5];
			String rowId="";
			
			bean =(SiteUserRolePrivilegeBean)surpArray[j];
			
			if(!bean.isRowDeleted())
			{
//				 for row id
				for(int count=0;count<keyArray.length;count++)
				{
					SiteUserRolePrivilegeBean beanForKey=map.get(keyArray[count]);
					if (bean==beanForKey)
					{
						rowId=(String)keyArray[count];
					}
				}
				
				//for role
				String roleName= bean.getRole().getName();
				
				// for site
				String sites = displaySiteNames(bean);
				
				// for privileges
				String actionNames = displayPrivilegesNames(bean);
				if(! pageOf.equalsIgnoreCase("pageOfUserAdmin"))
				{
					// for user
					String userName = bean.getUser().getFirstName();
					array[0]=userName;
				}
				else
				{
					// for user
					if(bean.getCollectionProtocol() != null)
					{
						String cpName = bean.getCollectionProtocol().getShortTitle();
						if(cpName!=null && !cpName.equalsIgnoreCase(""))
						{
							array[0]=cpName;
						}
						else
						{
							array[0]="N/A";
						}
					}
					else if(bean.isAllCPChecked())
					{
						array[0]="All Current and Future";
					}
					else if(!bean.isAllCPChecked())
					{
						array[0]="N/A";
					}
				}
				
				array[1]=sites;
				array[2]=roleName;
				array[3]=actionNames;
				array[4]=rowId;
				list.add(j,array);
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
		for (int i = 0; i < bean.getPrivileges().size(); i++) 
		{
			if (i > 0)
			{
				sbForActions.append(",");
			}
			String actionName="";
			if("7".equals(bean.getRole()))
			{
				List<NameValueBean>  scPrivilegesList = scientistPrivilegesList;
				for(int m=0;m<scPrivilegesList.size();m++)
				{
					NameValueBean nameValueBean=scPrivilegesList.get(m);
					actionName=nameValueBean.getName();
					sbForActions.append(actionName);
				}
			}
			else
			{
			String actionId=((NameValueBean)bean.getPrivileges().toArray()[i]).getValue();
			PrivilegeUtility privilegeUtility = new PrivilegeUtility();
			Privilege action;
			try 
			{
				action = privilegeUtility.getPrivilegeById(actionId);
				actionName=((NameValueBean)bean.getPrivileges().toArray()[i]).getName();
			}
			catch (CSException e) 
			{
				throw new BizLogicException(e);
			}
		//	}
				if((actionName).equals("PHI_ACCESS"))
				{
					sbForActions.append(ApplicationProperties.getValue(actionName));
				}
				else
				{
					sbForActions.append(Utility.getDisplayLabelForUnderscore(actionName));
				}
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
		StringBuffer sbForSitesNames = new StringBuffer();
		for (int i = 0; i < bean.getSiteList().size(); i++) 
		{
			if (i > 0)
			{
				sbForSitesNames.append(",");
			}
	
			Site site=bean.getSiteList().get(i);
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
		
		for(Site site:surp.getSiteList())
		{
			long siteId=site.getId();
			siteJsonArray.put(siteId);
		}
		jsonObject.put("siteJsonArray",siteJsonArray );

		// for User
		jsonObject.append("selectedUserId", selectedUserId);
		
		List<Site> siteList=surp.getSiteList();
		List<Long> selectedSitesList=new ArrayList<Long>();
		for(int z=0;z<siteList.size();z++)
		{
			selectedSitesList.add(siteList.get(z).getId());
		}

		List<JSONObject> userNameValueBeanList=getUsersForThisSites(selectedSitesList);
		jsonObject.put("userJsonArray",userNameValueBeanList);
		
		
		// for Role
		String roleId=surp.getRole().getValue();
		
		jsonObject.append("roleId", roleId);
		
		// for Privileges
		JSONArray selActionJsonArray=new JSONArray();
		List<NameValueBean> privileges=surp.getPrivileges();
		if(("7").equals(roleId))
		{
			List<NameValueBean> list= scientistPrivilegesList;
			JSONObject actionJsonObject=null;
			for(int z=0;z<list.size();z++)
			{
				actionJsonObject=new JSONObject();
				actionJsonObject.append("actionName", ((NameValueBean)list.get(z)).getName());
				actionJsonObject.append("actionId", ((NameValueBean)list.get(z)).getValue());
				selActionJsonArray.put(actionJsonObject);
			}
			
		}
		else
		{
			for(NameValueBean actionNameValueBean:privileges)
			{
				String actionId=actionNameValueBean.getValue();
				selActionJsonArray.put(actionId);
			}
		}
		jsonObject.put("selActionJsonArray",selActionJsonArray );
		
		List<JSONObject> actionJsonArray=new ArrayList<JSONObject>();
		List<NameValueBean> actionList=getActionList(false);
		if(actionList!=null && ! actionList.isEmpty())
		{
			actionJsonArray = getJSONObjListOfNameValue(actionList);
		}
		jsonObject.put("actionJsonArray",actionJsonArray);
		
		privilegeList.add(jsonObject);
		return privilegeList;
	}

// for user page--
public List<JSONObject> addPrivilegeForUserPage(Map<String, SiteUserRolePrivilegeBean> rowIdBeanMap, String cpIds, String siteIds, String roleId, String actionIds,boolean isAllCPChecked,String operation)throws BizLogicException,JSONException, CSException 
{  
	List<JSONObject> listForUPSummary = new ArrayList<JSONObject>();
	List<Long> siteIdsList = new ArrayList<Long>();
	List<Long> cpIdsList = new ArrayList<Long>();
	List<String> actionIdsList = new ArrayList<String>();

	siteIdsList = getSiteData(siteIds);
	cpIdsList = getCPData(cpIds);
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
				
				 long cpId = cpIdsList.get(k);
				 dao.openSession(null); 
				 Object object = dao.retrieve(CollectionProtocol.class.getName(), cpId);
				 CollectionProtocol collectionProtocol  = (CollectionProtocol)object;
				 
				 List<Site> cpRelatedSites = getCPSiteRelationForUserPage(collectionProtocol, siteIdsList);
				 
				 List<NameValueBean> actionBeanList = getPrivilegesNameValueBeanList(actionIdsList);
				
			     SiteUserRolePrivilegeBean surpBean =setUserPrivilegeSummaryForUserPage(collectionProtocol, cpRelatedSites, role, actionBeanList,isAllCPChecked);
				
				 String rowId = "CP_"+ cpId;
				 if((Constants.EDIT).equalsIgnoreCase(operation))
				 {
					 if(rowIdBeanMap.containsKey(rowId))
					 {
						 String updatedRowId="Updated_"+rowId;
						 SiteUserRolePrivilegeBean tempBean=rowIdBeanMap.get(rowId);
						 tempBean.setRowDeleted(true);
						 rowIdBeanMap.remove(rowId);
						 rowIdBeanMap.put(updatedRowId, tempBean);
						 
					 }
				 }
				 rowIdBeanMap.put(rowId,surpBean);
				 
				 JSONObject jsonObject=getObjectForUPSummaryForUserPage(cpRelatedSites,rowId,collectionProtocol,roleName,actionIdsList,roleId,isAllCPChecked);
				 
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
			//	 List<Site> cpRelatedSites = getCPSiteRelationForUserPage(si, siteIdsList);
				 
				 List<NameValueBean> actionBeanList = getPrivilegesNameValueBeanList(actionIdsList);
				
				 SiteUserRolePrivilegeBean surpBean =setUserPrivilegeSummaryForUserPage(null, siteLists, role, actionBeanList,isAllCPChecked);
				
				 String rowId = "" + siteId;
				 if((Constants.EDIT).equalsIgnoreCase(operation))
				 {
					 if(rowIdBeanMap.containsKey(rowId))
					 {
						 String updatedRowId="Updated_"+rowId;
						 SiteUserRolePrivilegeBean tempBean=rowIdBeanMap.get(rowId);
						 tempBean.setRowDeleted(true);
						 rowIdBeanMap.remove(rowId);
						 rowIdBeanMap.put(updatedRowId, tempBean);
						 
					 }
				 }
				 rowIdBeanMap.put(rowId,surpBean);
				 
				 JSONObject jsonObject=getObjectForUPSummaryForUserPage(siteLists,rowId,null,roleName,actionIdsList,roleId,isAllCPChecked);
				 
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
			Logger.out.error(daoEx.getMessage(), daoEx);
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

public SiteUserRolePrivilegeBean setUserPrivilegeSummaryForUserPage(CollectionProtocol collectionProtocol,List<Site> cpRelatedSites, Role role, List<NameValueBean> actionBeanList,boolean isAllCPChecked)
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


public JSONObject getObjectForUPSummaryForUserPage(List<Site> cpRelatedSites, String rowId, CollectionProtocol collectionProtocol, String roleName, List<String> actionIdsList,String roleId,boolean isAllCPChecked) throws JSONException,BizLogicException 
{
	StringBuffer sbForSitesIds = new StringBuffer();
	for (int i = 0; i < cpRelatedSites.size(); i++) 
	{
		if (i > 0)
		{
			sbForSitesIds.append(",");
		}
	    sbForSitesIds.append(cpRelatedSites.get(i).getName());
		
	}
	String sites = sbForSitesIds.toString();

	JSONObject jsonobject = new JSONObject();
	jsonobject.append("rowId", rowId);
	String cpName = "";
	long cpId = 0;
	if (collectionProtocol != null)
	{
	cpName = collectionProtocol.getShortTitle();
	cpId=collectionProtocol.getId();
	}
	else if(isAllCPChecked && cpRelatedSites!=null)
	{
		cpName="All Current and Future";
	}
	else if(cpRelatedSites!=null && ! isAllCPChecked)
	{
		cpName="N/A";
	}
	
	jsonobject.append("cpName", cpName);
	jsonobject.append("cpId", cpId);
	jsonobject.append("sites", sites);
	StringBuffer sbForActions = new StringBuffer();
	for (int i = 0; i < actionIdsList.size(); i++) 
	{
		if (i > 0)
		{
			sbForActions.append(",");
		}
		String actionName="";
		Privilege action;
		PrivilegeUtility privilegeUtility = new PrivilegeUtility();
		if("7".equals(roleId))
		{
			List<NameValueBean>  scPrivilegesList = scientistPrivilegesList;
			for(int m=0;m<scPrivilegesList.size();m++)
			{
				NameValueBean nameValueBean=scPrivilegesList.get(m);
				actionName=nameValueBean.getName();
				sbForActions.append(actionName);
			}
		}
		else
		{
			try 
			{
				action = privilegeUtility.getPrivilegeById(actionIdsList.get(i));
				actionName=action.getName();
			}
			catch (CSException e)
			{
				throw new BizLogicException("Could not get the action",e);
			}
			
	//	}
		
		if((actionName).equals("PHI_ACCESS"))
		{
			sbForActions.append(ApplicationProperties.getValue(actionName));
		}
		else
		{
			sbForActions.append(Utility.getDisplayLabelForUnderscore(actionName));
		}
		}
	//	sbForActions.append(ApplicationProperties.getValue(action.getName()));
	}
	String actionName =sbForActions.toString();
	jsonobject.append("roleName", roleName);
	jsonobject.append("actionName", actionName);
	return jsonobject;
}

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


public List<JSONObject> editPrivilegeForUserPage (Map<String, SiteUserRolePrivilegeBean> rowIdBeanMap,String selectedRow)throws JSONException,BizLogicException 
{
	SiteUserRolePrivilegeBean surp=rowIdBeanMap.get(selectedRow);
	
	List<JSONObject> privilegeList = new ArrayList<JSONObject>();
	JSONObject jsonObject=new JSONObject();
	long selectedCPId=0;
	if(surp.getCollectionProtocol()!=null)
	{
		selectedCPId=surp.getCollectionProtocol().getId();
	}
	
	// for site
	JSONArray siteJsonArray=new JSONArray();
	
	for(Site site:surp.getSiteList())
	{
		long siteId=site.getId();
		siteJsonArray.put(siteId);
	}
	jsonObject.put("siteJsonArray",siteJsonArray );

	// for CP
	jsonObject.append("selectedCPId", selectedCPId);
	
	List<Site> siteList=surp.getSiteList();
	List<Long> selectedSitesList=new ArrayList<Long>();
	for(int z=0;z<siteList.size();z++)
	{
		selectedSitesList.add(siteList.get(z).getId());
	}

	List<JSONObject> cpNameValueBeanList=getCPsForThisSites(selectedSitesList);
	jsonObject.put("cpJsonArray",cpNameValueBeanList);
	
	
	// for Role
	String roleId=surp.getRole().getValue();
	
	jsonObject.append("roleId", roleId);
	
	// for Privileges
//	JSONArray selActionJsonArray=new JSONArray();
	List<JSONObject> selActionJsonArray=new ArrayList<JSONObject>();
	List<NameValueBean> privileges=surp.getPrivileges();
	if(("7").equals(roleId))
	{
		List<NameValueBean> list= scientistPrivilegesList;
		JSONObject actionJsonObject=null;
		for(int z=0;z<list.size();z++)
		{
			actionJsonObject=new JSONObject();
			actionJsonObject.append("actionName", ((NameValueBean)list.get(z)).getName());
			actionJsonObject.append("actionId", ((NameValueBean)list.get(z)).getValue());
			selActionJsonArray.add(actionJsonObject);
		}
		
	}
	else
	{
//		for(NameValueBean actionNameValueBean:privileges)
//		{
//			String actionId=actionNameValueBean.getValue();
//			selActionJsonArray.put(actionId);
//		}
		selActionJsonArray=getJSONObjListOfNameValue(privileges);
	}
	jsonObject.put("selActionJsonArray",selActionJsonArray );
	
	List<JSONObject> actionJsonArray=new ArrayList<JSONObject>();
	List<NameValueBean> actionList=getActionListForUserPage(false);

	if(actionList!=null && ! actionList.isEmpty())
	{
		actionJsonArray=getJSONObjListOfNameValue(actionList);
	}
	jsonObject.put("actionJsonArray",actionJsonArray);
	
	privilegeList.add(jsonObject);
	return privilegeList;
}

/**
 * @param actionIdsList
 * @return
 * @throws CSException
 */
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
			nameValueBean.setName(actionName);
			nameValueBean.setValue(actionId);
			actionBeanList.add(nameValueBean);
	 }
	return actionBeanList;
}





public List<NameValueBean> removeSelect(List<NameValueBean> list) 
{
	if(list != null && !list.isEmpty())
	{
		for(int len=0;len<list.size();len++)
		{
			String val=""+((NameValueBean)list.get(len)).getValue();
			String name=((NameValueBean)list.get(len)).getName();
			if((("-1").equalsIgnoreCase(val))&& ((Constants.SELECT_OPTION).equalsIgnoreCase(name)))
			{
				list.remove(len);
			}
		}
	}
	return list;
}
// get JSONArray of JSON Objects having Name and Value as  fields.
	public List<JSONObject>  getJSONObjListOfNameValue(List<NameValueBean> list) throws JSONException
	{
		List<JSONObject> jsonObjectList= new ArrayList<JSONObject>(); 
		JSONObject jsonObject = null;
		for (int i = 0; i < list.size(); i++)
		{
			jsonObject = new JSONObject();
			jsonObject.append("locationName", ((NameValueBean) list.get(i)).getName());
			jsonObject.append("locationId", ((NameValueBean) list.get(i)).getValue());
			
			jsonObjectList.add(jsonObject);
		}

		return jsonObjectList;
	}
}

