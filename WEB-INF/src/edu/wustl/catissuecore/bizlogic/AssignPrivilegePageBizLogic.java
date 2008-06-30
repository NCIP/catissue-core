
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

import javax.servlet.http.HttpSession;

import org.json.JSONException;
import org.json.JSONObject;

import edu.wustl.catissuecore.action.UserAction;
import edu.wustl.catissuecore.actionForm.AssignPrivilegesForm;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.multiRepository.bean.SiteUserRolePrivilegeBean;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.dao.AbstractDAO;
import edu.wustl.common.dao.DAOFactory;
import edu.wustl.common.security.PrivilegeUtility;
import edu.wustl.common.security.SecurityManager;
import edu.wustl.common.security.exceptions.SMException;
import edu.wustl.common.util.dbManager.DAOException;
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
	 * @throws DAOException
	 */	
	public List<NameValueBean> getSiteList(boolean isToExcludeDisabled) throws DAOException
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
		

		List<NameValueBean> siteNameValueBeanList =getList(sourceObjectName, siteDisplayField, valueField, whereColumnName, whereColumnCondition, whereColumnValue, joinCondition,
						separatorBetweenFields,isToExcludeDisabled);
		NameValueBean siteNameValueBean = siteNameValueBeanList.get(0);
		if(siteNameValueBean.getValue().equals("-1"))
		{
			siteNameValueBeanList.remove(0);
		}
		NameValueBean selectAllObject=new NameValueBean();
		selectAllObject.setName("--All--");
		selectAllObject.setValue("-1");
		siteNameValueBeanList.add(0, selectAllObject);
		return siteNameValueBeanList;
		
	}
	/**
	 * Gets list of users when page is loaded
	 * @param isToExcludeDisabled
	 * @return List of user NameVAlueBean objects.
	 * @throws DAOException
	 */
	public List<NameValueBean> getUserList(boolean isToExcludeDisabled) throws DAOException
	{
		String sourceObjectName =User.class.getName();
		String[] userDisplayField = {"firstName"};
		String valueField = "id";
		
		String[] activityStatusArray = {Constants.ACTIVITY_STATUS_DISABLED,Constants.ACTIVITY_STATUS_CLOSED};
		String joinCondition = null;
		String separatorBetweenFields = ", ";

		String[] whereColumnName = new String[]{Constants.ACTIVITY_STATUS};
		String[] whereColumnCondition = new String[]{"not in"};
		Object[] whereColumnValue = {activityStatusArray};		

		List<NameValueBean> userNameValueBeanList= getList(sourceObjectName, userDisplayField, valueField, whereColumnName, whereColumnCondition, whereColumnValue, joinCondition,
						separatorBetweenFields,isToExcludeDisabled);
		NameValueBean  userNameValueBean=userNameValueBeanList.get(0);
		if(userNameValueBean.getValue().equals("-1"))
		{
			userNameValueBeanList.remove(0);
		}
		
		NameValueBean selectAllObject=new NameValueBean();
		selectAllObject.setName("--All--");
		selectAllObject.setValue("-1");
		userNameValueBeanList.add(0, selectAllObject);
		return userNameValueBeanList;
		
	}	
	/**
	 * Gets list of roles when page is loaded
	 * @return List of role NameValueBean objects.
	 * @throws SMException
	 */
	public List<NameValueBean> getRoleList() throws SMException
    {
        //Sets the roleList attribute to be used in the Add/Edit User Page.
        Vector roleList = SecurityManager.getInstance(UserAction.class).getRoles();
        
        ListIterator iterator = roleList.listIterator();
        
        List<NameValueBean> roleNameValueBeanList = new ArrayList<NameValueBean>();
         
        while (iterator.hasNext())
        {
            Role role = (Role) iterator.next();
            NameValueBean nameValueBean = new NameValueBean();
            nameValueBean.setName(role.getName());
            nameValueBean.setValue(String.valueOf(role.getId()));
            roleNameValueBeanList.add(nameValueBean);
        }
        return roleNameValueBeanList;
    }
	/**
	 * Gets list of actions when page is loaded
	 * @param isToExcludeDisabled
	 * @return List of user NameVAlueBean objects.
	 * @throws DAOException
	 * @throws CSException 
	 */
	public List<NameValueBean> getActionList(boolean isToExcludeDisabled) throws DAOException, CSException
	{
		PrivilegeUtility privilegeUtility = new PrivilegeUtility();
		List<Privilege> prvilegeList =  privilegeUtility.getPrivilegeList();
		List<NameValueBean> privilegeNameValueBeanList  = new ArrayList<NameValueBean>();
		for (Privilege privilege2 : prvilegeList)
		{
			NameValueBean privilegeNameValueBean = new NameValueBean();
			privilegeNameValueBean.setName(privilege2.getName());
			privilegeNameValueBean.setValue(String.valueOf(privilege2.getId()));
			privilegeNameValueBeanList.add(privilegeNameValueBean);
		}
		
		NameValueBean privilegeNameValueBean = new NameValueBean();
		privilegeNameValueBean.setName("--All--");
		privilegeNameValueBean.setValue("-1");
		privilegeNameValueBeanList.add(privilegeNameValueBean);
		return privilegeNameValueBeanList;
	}
	/**
	 * Given a list selected sites , this method will return the list of users registered under them.
	 * @param siteIds
	 * @return List of user registered under the given site
	 */
	public List<NameValueBean> getUsersList(long siteId) {	
		AbstractDAO dao = DAOFactory.getInstance().getDAO(Constants.HIBERNATE_DAO);
		List<NameValueBean> nameValuBeanList=new ArrayList<NameValueBean>();
		try
		{
			dao.openSession(null);
			Object obj = dao.retrieve(Site.class.getName(),siteId);
			Site site = (Site)obj;
			Collection<User> userCollection = site.getUserCollection();
			for (User user : userCollection)
			{ 
				NameValueBean valueBean=new NameValueBean();
				valueBean.setName(user.getLoginName());
				valueBean.setValue(String.valueOf(user.getId()));
				nameValuBeanList.add(valueBean);
			}
			
		}
		catch (DAOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				dao.closeSession();
			}
			catch (DAOException e)
			{
				e.printStackTrace();
			}
		}
		
		return nameValuBeanList;
	}
	/**
	 * Given a selected role this method will return the list of actions under this role.
	 * @param role
	 * @return List of actions assigned to the selected role
	 */
	public List<NameValueBean> getActionsList(String role) {	
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
	public List<NameValueBean> getUsersForSelectedSites(List<Integer> selectedSitesList) {
		Set<NameValueBean> userSet = new HashSet<NameValueBean>();
		for(int i=0;i<selectedSitesList.size();i++){
			int siteId=selectedSitesList.get(i);
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
	public List<Integer> getSiteData(String siteIds) {
		int siteId;
		List<Integer> siteIdsList = new ArrayList<Integer>();
		StringTokenizer tokenizer = new StringTokenizer("" + siteIds, ",");
		while (tokenizer.hasMoreTokens()) {
			siteId = Integer.parseInt(tokenizer.nextToken());
			if (siteId == -1) {
				try {
					List<NameValueBean> list = getSiteList(false);
					for(NameValueBean nv : list)
					{
						if (!nv.getValue().equals("-1"))
						{
							siteIdsList.add(Integer.parseInt(nv.getValue()));
						}
					}
				} catch (DAOException e) {
					Logger.out.error("DAOException from getSiteList in AssignPrivilegePageBizLogic..."+e);
				}
				break;
			} else {
				siteIdsList.add(siteId);
			}
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
	public List<JSONObject> getUsersForThisSites(List<Integer> selectedSitesList) throws JSONException
			 {
		List<JSONObject> arrayList = new ArrayList<JSONObject>();
		List<NameValueBean> usersList = new ArrayList<NameValueBean>();
		usersList = getUsersForSelectedSites(selectedSitesList);
			JSONObject jsonobject = null;
			for (int j = 0; j < usersList.size(); j++) {
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
	 * @throws IOException
	 * @throws JSONException 
	 */
	public List<JSONObject> getActionsForThisRole(String roleId)throws IOException, JSONException {
		List<NameValueBean> actionsList = getActionsList(roleId);
		// request.setAttribute(Constants.ACTIONLIST, actionsList);
		List<JSONObject> arrayList = new ArrayList<JSONObject>();
		
			JSONObject jsonobject = null;
			for (int i = 0; i < actionsList.size(); i++) {
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
	public List<Long> getUserData(String userIds) {
		long userId;
		List<Long> userIdsList = new ArrayList<Long>();
		
		StringTokenizer tokenizer = new StringTokenizer("" + userIds, ",");
		while (tokenizer.hasMoreTokens()) {
			userId = Long.parseLong(tokenizer.nextToken());
			// if(userId==-1){
			// try {
			// userIdsList=assignPrivilegePageBizLogic.getUserList(false);
			// } catch (DAOException e) {
			// e.printStackTrace();
			// }
			// break;
			// }
			// else{
			userIdsList.add(userId);
			// }
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
	public List<String> getActionData(String actionIds) {
		String actionId; 
		List<String> actionIdsList = new ArrayList<String>();
		StringTokenizer tokenizer = new StringTokenizer("" + actionIds, ",");
		while (tokenizer.hasMoreTokens()) {
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
	public List<Site> getUserSiteRelation(User user,
			List<Integer> siteIdsList) {
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
	public SiteUserRolePrivilegeBean setUserPrivilegeSummary(User user,
			List<Site> userRelatedSites, String roleId, List<String> actionIdsList) {
		SiteUserRolePrivilegeBean surp = new SiteUserRolePrivilegeBean();
		surp.setUser(user);
		surp.setRole(roleId);
		List<Integer> listOfSiteIds = new ArrayList<Integer>();
		for(Site site :userRelatedSites)
		{
			listOfSiteIds.add(new Integer(site.getId().intValue()));
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
	public JSONObject getObjectForUPSummary(List<Site> userRelatedSites, String rowId, User user, String roleId, List<String> actionIdsList) throws JSONException, CSException {
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
			Privilege action = privilegeUtility.getPrivilegeById(actionIdsList.get(i));
			sbForActions.append(action.getName());
		}
		String actionName =sbForActions.toString();
		jsonobject.append("roleId", roleId);
		jsonobject.append("actionName", actionName);
		return jsonobject;
	}
	/**
	 *  Delete row from the map
	 * @param deletedRowsArray
	 * @param rowIdObjectBeanMap
	 */
	public void deletePrivilege(HttpSession session,String deletedRowsArray) {
		Map<String, SiteUserRolePrivilegeBean> rowIdBeanMap= new HashMap<String, SiteUserRolePrivilegeBean>();
		if (session.getAttribute(Constants.ROW_ID_OBJECT_BEAN_MAP) != null) {
			rowIdBeanMap = (Map<String, SiteUserRolePrivilegeBean>) session.getAttribute("rowIdObjectBeanMap");
		}
		String deletedRowId = "";
		List<String> deletedRowsList = new ArrayList<String>();
		StringTokenizer tokenizer = new StringTokenizer(deletedRowsArray, ",");
		while (tokenizer.hasMoreTokens()) {
			deletedRowId = tokenizer.nextToken().intern();
			deletedRowsList.add(deletedRowId);
			rowIdBeanMap.remove(deletedRowId);
		}
	}
	/**
	 * Gives Map having rowId and UsersummaryBean Object and returns a list containing rowId and userSummaryBean Object,used to send response.  
	 * @param session
	 * @param userIds
	 * @param siteIds
	 * @param roleId
	 * @param actionIds
	 * @return List<JSONObject> of userSummaryBean Objects
	 */	
	public List<JSONObject> addPrivilege(HttpSession session, String userIds, String siteIds, String roleId, String actionIds) 
	{
		Map<String, SiteUserRolePrivilegeBean> rowIdBeanMap= new HashMap<String, SiteUserRolePrivilegeBean>();
		if (session.getAttribute(Constants.ROW_ID_OBJECT_BEAN_MAP) != null) {
			rowIdBeanMap = (Map<String, SiteUserRolePrivilegeBean>) session.getAttribute("rowIdObjectBeanMap");
		}
		List<JSONObject> listForUPSummary = new ArrayList<JSONObject>();
		List<Integer> siteIdsList = new ArrayList<Integer>();
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
				 long userId = userIdsList.get(k);
				 dao.openSession(null); 
				 Object object = dao.retrieve(User.class.getName(), userId);
				 User user  = (User)object;
				 List<Site> userRelatedSites = getUserSiteRelation(user, siteIdsList);
				 
				 SiteUserRolePrivilegeBean surpBean =setUserPrivilegeSummary(user, userRelatedSites, roleId, actionIdsList);
				
				 String rowId = "" + userId;
				 rowIdBeanMap.put(rowId,surpBean);
				 
				 JSONObject jsonObject=getObjectForUPSummary(userRelatedSites,rowId,user,roleId,actionIdsList);
				 
				 listForUPSummary.add(jsonObject);
			}
			 session.setAttribute(Constants.ROW_ID_OBJECT_BEAN_MAP,	rowIdBeanMap);
		} catch (JSONException e) {
			Logger.out.error("JSONException in  sending JSON response in ShowAssignPrivilegePageAction..."+e);
		} catch (DAOException e) {
			Logger.out.error("DAOException in  getting objectList for AssignPrivilegePageBizLogic  in ShowAssignPrivilegePageAction..."+e);
		}catch (CSException e) {
			Logger.out.error("CSException in  getting objectList for AssignPrivilegePageBizLogic  in ShowAssignPrivilegePageAction..."+e);
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
}
