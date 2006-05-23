/**
 * <p>Title: ParticipantLookupAction Class>
 * <p>Description:	This Class is used to search the matching participant.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author vaishali_khandelwal
 * @Created on May 19, 2006
 */

package edu.wustl.catissuecore.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.bizlogic.ParticipantLookupLogic;
import edu.wustl.catissuecore.bizlogic.ParticipantLookupLogicImpl;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.AssignDataException;
import edu.wustl.common.factory.AbstractDomainObjectFactory;
import edu.wustl.common.factory.MasterFactory;
import edu.wustl.common.util.logger.Logger;

public class ParticipantLookupAction extends Action
{

    /**
     * Overrides the execute method of Action class.
     * */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException
    {
        String target = null;
        AbstractDomainObject abstractDomain = null;

        try
        {
            AbstractActionForm abstractForm = (AbstractActionForm) form;
            
			 AbstractDomainObjectFactory abstractDomainObjectFactory = 
            	(AbstractDomainObjectFactory) MasterFactory
            				.getFactory("edu.wustl.catissuecore.domain.DomainObjectFactory");
        
			 //Creating the column list which is used in Data grid to display column headings
			 List ColumnList=new ArrayList();
			 ColumnList.add("System Identifier");
			 ColumnList.add("Last Name");
			 ColumnList.add("First Name");
			 ColumnList.add("Middle Name");
			 ColumnList.add("Birth Date");
			 ColumnList.add("SSN");
			 ColumnList.add("Probability");
			 
			 request.setAttribute(Constants.SPREADSHEET_COLUMN_LIST,ColumnList);
                        
             abstractDomain = abstractDomainObjectFactory.getDomainObject(abstractForm.getFormId(), abstractForm);
             
             Participant participant=(Participant)abstractDomain;
             
             //creating the instance of ParticipantLookupLogic class
             ParticipantLookupLogic pll=new ParticipantLookupLogicImpl();
             
             try
             {
            	 List ParticipantList=pll.participantLookup(participant);
            
            	 if(ParticipantList.size()>0)
            	 {
            		 request.setAttribute(Constants.SPREADSHEET_DATA_LIST,ParticipantList);
            		
            	 }
            	 else
            	 {
            		 return mapping.findForward("participantAdd");
            	 }
             }
             catch(Exception e)
             {
            	 Logger.out.error("Error occured while participant lookup action:"+e);
             }
        }
        
        catch (AssignDataException excp)
        {
            target = new String(Constants.FAILURE);
            Logger.out.debug("excp "+excp.getMessage());
            Logger.out.error(excp.getMessage(), excp);
        }
        
        Logger.out.debug("target....................."+target); 
        return (mapping.findForward("success"));
    }
    
    protected SessionDataBean getSessionData(HttpServletRequest request) {
		Object obj = request.getSession().getAttribute(Constants.SESSION_DATA);
		if(obj!=null)
		{
			SessionDataBean sessionData = (SessionDataBean) obj;
			return  sessionData;
		}
		return null;
		
	}
    
    
    
}