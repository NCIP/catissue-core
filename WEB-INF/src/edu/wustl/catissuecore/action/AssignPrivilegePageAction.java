/**
 * <p>Title: AssignPrivilegePageAction Class>
 * <p>Description:	This class initializes the fields of AssignPrivileges.jsp Page</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00
 * Created on Sep 20, 2005
 */

package edu.wustl.catissuecore.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.AssignPrivilegesForm;
import edu.wustl.catissuecore.bizlogic.AssignPrivilegePageBizLogic;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.UserBizLogic;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.util.HibernateMetaData;
import gov.nih.nci.security.authorization.domainobjects.Role;

public class AssignPrivilegePageAction extends BaseAction
{
	/**
     * Overrides the execute method of Action class.
     * Initializes the various fields in AssignPrivileges.jsp Page.
     * */
    public ActionForward executeAction(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException
    {
        AssignPrivilegesForm privilegesForm = (AssignPrivilegesForm)form;
        SessionDataBean sessionData = getSessionData(request);
        //User ID of the user who has logged in
        Long loginUserId = sessionData.getUserId();
        
        //Constants that are required to populate lists in AssignPrivileges.jsp
        String [] assignOperation = {"Allow","Disallow"};
        
        //Setting the list of Object Types depending upon the privilege
        Vector objectTypes = new Vector();
        
        if(privilegesForm.getPrivilege() == null || (privilegesForm.getPrivilege()).equals("READ"))
        {
	        objectTypes.add(new NameValueBean("Collection Protocol","edu.wustl.catissuecore.domain.CollectionProtocol"));
	        
	        // Gautam : Commented as per Marks comments.
//	        objectTypes.add(new NameValueBean("Collection Protocol Registration",CollectionProtocolRegistration.class.getName()));
//	        objectTypes.add(new NameValueBean("Specimen Collection","edu.wustl.catissuecore.domain.SpecimenCollectionGroup"));
//	        objectTypes.add(new NameValueBean("Specimen","edu.wustl.catissuecore.domain.Specimen"));
//	        objectTypes.add(new NameValueBean("Distribution Protocol","edu.wustl.catissuecore.domain.DistributionProtocol"));
        }
        else
        {
	        objectTypes.add(new NameValueBean("Site","edu.wustl.catissuecore.domain.Site"));
	        objectTypes.add(new NameValueBean("Storage Container","edu.wustl.catissuecore.domain.StorageContainer"));
        }
        
        //String [] attributes = {Constants.ANY,"De-Id"};
        
        try
		{
        	//SETTING THE USER LIST
       		UserBizLogic userBizLogic = (UserBizLogic)BizLogicFactory.getInstance().getBizLogic(Constants.USER_FORM_ID);
        	Collection userCollection =  userBizLogic.getCSMUsers();
        	
        	if(userCollection != null && userCollection.size() !=0)
        	{
        		((Vector) userCollection).remove(0);//Removing SELECT Option
//        		Extracting RoleNames & their Ids
        		Vector roles = SecurityManager.getInstance(AssignPrivilegePageAction.class).getRoles();
        		//Create list of users for Use and Read privilege.
            	List usersForUsePrivilege = new ArrayList();
            	List usersForReadPrivilege = new ArrayList();

        		if(roles != null && roles.size() != 0)
        		{
        			for(int i=0;i<roles.size();i++)
        			{
        				Role role = (Role)roles.get(i);
        				String id = "Role_" + role.getId();
        				String roleName = role.getName();
        				//Aarti: Adding supervisor due to Bug#1854 - To assign or revoke use privilege 
        				//on storage containers and sites should NOT only apply to technician group users 
        				//but also to supervisors.
        				if(roleName.equals(Constants.TECHNICIAN) || roleName.equals(Constants.SUPERVISOR))
        				{
       						usersForUsePrivilege.add(new NameValueBean(roleName,id));
        				}
        				if(role.getName().equals(Constants.SCIENTIST))
        				{
        					usersForReadPrivilege.add(new NameValueBean(role.getName(),id));
        				}
        			}
        		}

            	Iterator userCollectionItr = userCollection.iterator();
            	while(userCollectionItr.hasNext())
            	{
            		NameValueBean userNameValue = (NameValueBean)userCollectionItr.next();
            		long userId = Long.parseLong(userNameValue.getValue());
            		Role role = SecurityManager.getInstance(AssignPrivilegePageAction.class).getUserRole(userId);
            		if(role!=null)
            		{
            			String roleName = role.getName();
            			//Make a list of technicians
            			//Aarti: Adding supervisors due to Bug#1854 - To assign or revoke use privilege 
        				//on storage containers and sites should NOT only apply to technician group users 
        				//but also to supervisors.
            			if(roleName.equals(Constants.TECHNICIAN) || roleName.equals(Constants.SUPERVISOR))
            				usersForUsePrivilege.add(userNameValue);
            			//Make a list of Scientists
            			if(roleName.equals(Constants.SCIENTIST))
            				usersForReadPrivilege.add(userNameValue);
            		}
            		//Remove the user who has logged in, from the users list.
            		if(userNameValue.getValue().equals(String.valueOf(loginUserId)))
            				userCollectionItr.remove();
            	}
        		request.setAttribute(Constants.GROUPS,userCollection);
        		//Set users for Read privilege - group of Scientists only
        		request.setAttribute(Constants.USERS_FOR_READ_PRIVILEGE,usersForReadPrivilege);
        		//Set users for Use privilege - group of Technicians only
            	request.setAttribute(Constants.USERS_FOR_USE_PRIVILEGE,usersForUsePrivilege);
        	}
 
        	request.setAttribute(Constants.ASSIGN,assignOperation);

        	//Get the privileges list according to the role of the user who has logged in.
        	Role loginUserRole = SecurityManager.getInstance(AssignPrivilegePageAction.class).getUserRole(Long.parseLong(sessionData.getCsmUserId()));
            //SETTING THE PRIVILEGES LIST
            Vector privileges = SecurityManager.getInstance(AssignPrivilegePageAction.class).getPrivilegesForAssignPrivilege(loginUserRole.getName());		
            request.setAttribute(Constants.PRIVILEGES,privileges);
            
            //SETTING THE OBJECT TYPES LIST            
            request.setAttribute(Constants.OBJECT_TYPES,objectTypes);
            
            //SETTING THE RECORD IDS AS PER THE OBJECT TYPES
            String [] privilegeName = {privilegesForm.getPrivilege()};
            String [] objects =null;
            
        
                Logger.out.debug("Object Type.............."+privilegesForm.getObjectType());
                Set recordIds = new HashSet();
                List recordNames = new ArrayList();              
                IBizLogic bizLogic = new DefaultBizLogic();                
            	if (privilegesForm.getObjectType()!=null)
            	{
					List subclassList = HibernateMetaData.getSubClassList(privilegesForm.getObjectType());
					objects = new String[subclassList.size()+1];
					Iterator subclassIt = subclassList.iterator();
					objects[0] = privilegesForm.getObjectType();
					for(int i =0;subclassIt.hasNext();i++)
					{
						objects[i+1] = (String) subclassIt.next();
					}					
					recordIds = SecurityManager.getInstance(AssignPrivilegePageAction.class).getObjectsForAssignPrivilege(String.valueOf(sessionData.getCsmUserId()),objects,privilegeName);
					
					AssignPrivilegePageBizLogic assignPrivilegePageBizLogic = (AssignPrivilegePageBizLogic)AbstractBizLogicFactory.getBizLogic(
							ApplicationProperties.getValue("app.bizLogicFactory"),
								"getBizLogic", Constants.ASSIGN_PRIVILEGE_FORM_ID);			  
					
					recordNames = assignPrivilegePageBizLogic.getRecordNames(recordIds,privilegesForm);
            	}
            	request.setAttribute(Constants.RECORD_IDS, recordNames);
	
            
            //request.setAttribute(Constants.ATTRIBUTES,attributes);
		}
        catch(Exception e)
		{
        	Logger.out.error(e.getMessage(),e);
		}
        
        return mapping.findForward((String)request.getParameter(Constants.PAGE_OF));
    }
}