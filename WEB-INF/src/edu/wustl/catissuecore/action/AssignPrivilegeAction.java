/**
 * <p>Title: AssignPrivilegeAction Class>
 * <p>Description:	This class initializes the fields of AssignPrivilege.jsp Page</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00
 * Created on Sep 20, 2005
 */

package edu.wustl.catissuecore.action;

import java.io.IOException;
import java.util.Collection;
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
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.security.SecurityManager;
import edu.wustl.common.util.logger.Logger;

public class AssignPrivilegeAction extends BaseAction
{
    /**
     * Overrides the execute method of Action class.
     * Initializes the various fields in AssignPrivilege.jsp Page.
     * */
    public ActionForward executeAction(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException
    {
        AssignPrivilegesForm privilegesForm = (AssignPrivilegesForm)form;
        
        //Constants that are required to populate lists in AssignPrivileges.jsp
        String [] assignOperation = {"Allow","Disallow"};
        //String [] privileges = {Constants.ANY,"Add","Edit","Read","Use"};
        
        Vector objectTypes = new Vector();
        objectTypes.add(new NameValueBean(Constants.ANY,Constants.ANY));
        objectTypes.add(new NameValueBean("Participant","Participant"));
        objectTypes.add(new NameValueBean("Collection Protocol","CollectionProtocol"));
        objectTypes.add(new NameValueBean("Distribution Protocol","DistributionProtocol"));
        objectTypes.add(new NameValueBean("Specimen Collection","SpecimenCollectionGroup"));
        objectTypes.add(new NameValueBean("Specimen","Specimen"));
        objectTypes.add(new NameValueBean("Specimen Events","SpecimenEventParameters"));
        objectTypes.add(new NameValueBean("Storage","StorageContainer"));
        objectTypes.add(new NameValueBean("Site","Site"));
        objectTypes.add(new NameValueBean("Distribution","Distribution"));
        objectTypes.add(new NameValueBean("User","User"));
        
        //String [] recordIds = {Constants.ANY};
        
        String [] attributes = {Constants.ANY,"De-Id"};
        String [] groups = {"Administrator","Technician","Supervisor","Public User"};
        
        try
		{
        	//SETTING THE USER LIST
       		UserBizLogic userBizLogic = (UserBizLogic)BizLogicFactory.getBizLogic(Constants.USER_FORM_ID);
        	Collection userCollection =  userBizLogic.getUsers(Constants.ACTIVITY_STATUS_ACTIVE);
        	
        	if(userCollection != null && userCollection.size() !=0)
        	{
        		((Vector) userCollection).remove(0);//Removing SELECT Option
        		
        		((Vector) userCollection).add(0,new NameValueBean("Administrator","Administrator"));
        		((Vector) userCollection).add(1,new NameValueBean("Technician","Technician"));
        		((Vector) userCollection).add(2,new NameValueBean("Supervisor","Supervisor"));
        		((Vector) userCollection).add(3,new NameValueBean("Public User","Public User"));
        		
        		request.setAttribute(Constants.GROUPS,userCollection);
        	}
        	
        	request.setAttribute(Constants.ASSIGN,assignOperation);        
            
            SessionDataBean bean = getSessionData(request);
            
            //SETTING THE PRIVILEGES LIST
            Vector privileges = SecurityManager.getInstance(AssignPrivilegeAction.class).getPrivilegesForAssignPrivilege(bean.getUserName());		
            request.setAttribute(Constants.PRIVILEGES,privileges);
            
            //SETTING THE OBJECT TYPES LIST            
            request.setAttribute(Constants.OBJECT_TYPES,objectTypes);
            
            //SETTING THE RECORD IDS AS PER THE OBJECT TYPES
            String [] privilegeName = privilegesForm.getPrivileges();
            String [] objects = privilegesForm.getObjectTypes();
            
            Set recordIds = SecurityManager.getInstance(AssignPrivilegeAction.class).getObjectsForAssignPrivilege(String.valueOf(bean.getUserId()),objects,privilegeName);
        	
            System.out.println("*********************IS RECORD ID NULL" + (recordIds == null));
            
//            if(recordIds == null || recordIds.size() == 0)
//            {
//            	System.out.println("*********************RECORD ID IS NULL");
//            	recordIds.add(new NameValueBean(Constants.ANY,Constants.ANY));
//            }
            
            request.setAttribute(Constants.RECORD_IDS,recordIds);
        	
            request.setAttribute(Constants.ATTRIBUTES,attributes);
		}
        catch(Exception e)
		{
        	Logger.out.error(e.getMessage());
		}
        
        return mapping.findForward((String)request.getParameter(Constants.PAGEOF));
    }
    
//    private Collection loadRecordIds(String [] objectTypes) throws DAOException
//    {
//    	Vector recordIds = new Vector();
//    	recordIds.add(new NameValueBean(Constants.ANY,Constants.ANY));
//    	String [] systemIdentifier = {Constants.SYSTEM_IDENTIFIER};
//    	
//    	if(objectTypes != null)
//    	{
//    		for(int i=0;i<objectTypes.length;i++)
//    		{
//    			if(objectTypes.equals(Constants.ANY))
//    				continue;
//    			
//    			DefaultBizLogic bizLogic = new DefaultBizLogic();
//    			List list = bizLogic.getList(objectTypes[i],systemIdentifier,systemIdentifier[0],true);
//    			if(list != null && list.size() != 0)
//    			{
//    				recordIds.addAll(list);
//    			}
//    		}
//    	}
//    	
//    	return recordIds;
//    }
}