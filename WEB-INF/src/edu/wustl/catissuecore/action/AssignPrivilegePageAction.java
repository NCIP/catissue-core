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
import java.util.Collection;
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
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.UserBizLogic;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.security.SecurityManager;
import edu.wustl.common.util.dbManager.HibernateMetaData;
import edu.wustl.common.util.logger.Logger;
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
        
        //Constants that are required to populate lists in AssignPrivileges.jsp
        String [] assignOperation = {"Allow","Disallow"};
        
        //Setting the list of Object Types depending upon the privilege
        Vector objectTypes = new Vector();
        
        if(privilegesForm.getPrivilege() == null || (privilegesForm.getPrivilege()).equals("READ"))
        {
	        objectTypes.add(new NameValueBean("Collection Protocol","edu.wustl.catissuecore.domain.CollectionProtocol"));
	        objectTypes.add(new NameValueBean("Collection Protocol Registration",CollectionProtocolRegistration.class.getName()));
	        objectTypes.add(new NameValueBean("Specimen Collection","edu.wustl.catissuecore.domain.SpecimenCollectionGroup"));
	        objectTypes.add(new NameValueBean("Specimen","edu.wustl.catissuecore.domain.Specimen"));
        }
        else
        {
	        objectTypes.add(new NameValueBean("Site","edu.wustl.catissuecore.domain.Site"));
	        objectTypes.add(new NameValueBean("Storage","edu.wustl.catissuecore.domain.StorageContainer"));
        }
        
        //String [] attributes = {Constants.ANY,"De-Id"};
        
        try
		{
        	//SETTING THE USER LIST
       		UserBizLogic userBizLogic = (UserBizLogic)BizLogicFactory.getBizLogic(Constants.USER_FORM_ID);
        	Collection userCollection =  userBizLogic.getUsers();
        	
        	if(userCollection != null && userCollection.size() !=0)
        	{
        		((Vector) userCollection).remove(0);//Removing SELECT Option
        		
        		//Extracting RoleNames & their Ids
        		Vector roles = SecurityManager.getInstance(AssignPrivilegePageAction.class).getRoles();
        		
        		if(roles != null && roles.size() != 0)
        		{
        			for(int i=0;i<roles.size();i++)
        			{
        				Role role = (Role)roles.get(i);
        				String id = "Role_" + role.getId();
        				((Vector) userCollection).add(i,new NameValueBean(role.getName(),id));
        			}
        		}
        		        		
        		request.setAttribute(Constants.GROUPS,userCollection);
        	}
        	
        	request.setAttribute(Constants.ASSIGN,assignOperation);        
            
            SessionDataBean bean = getSessionData(request);
            
            //SETTING THE PRIVILEGES LIST
            Vector privileges = SecurityManager.getInstance(AssignPrivilegePageAction.class).getPrivilegesForAssignPrivilege(bean.getUserName());		
            request.setAttribute(Constants.PRIVILEGES,privileges);
            
            //SETTING THE OBJECT TYPES LIST            
            request.setAttribute(Constants.OBJECT_TYPES,objectTypes);
            
            //SETTING THE RECORD IDS AS PER THE OBJECT TYPES
            String [] privilegeName = {privilegesForm.getPrivilege()};
            String [] objects =null;
            try {
            	if(privilegesForm.getObjectType()!=null)
            	{
				List subclassList = HibernateMetaData.getSubClassList(privilegesForm.getObjectType());
				objects = new String[subclassList.size()+1];
				Iterator subclassIt = subclassList.iterator();
				objects[0] = privilegesForm.getObjectType();
				for(int i =0;subclassIt.hasNext();i++)
				{
					objects[i+1] = (String) subclassIt.next();
				}
            	}
            	else
            	{
            		objects = new String[]{privilegesForm.getObjectType()};
            	}
			
			} catch (ClassNotFoundException e1) {
				Logger.out.debug("Exception:"+e1.getMessage(),e1);
				objects = new String[]{privilegesForm.getObjectType()};
			}
            
            Set recordIds = SecurityManager.getInstance(AssignPrivilegePageAction.class).getObjectsForAssignPrivilege(String.valueOf(bean.getUserId()),objects,privilegeName);
        	            
            request.setAttribute(Constants.RECORD_IDS,recordIds);
        	
            //request.setAttribute(Constants.ATTRIBUTES,attributes);
		}
        catch(Exception e)
		{
        	System.out.println(e);
        	Logger.out.error(e.getMessage(),e);
		}
        
        return mapping.findForward((String)request.getParameter(Constants.PAGEOF));
    }
}