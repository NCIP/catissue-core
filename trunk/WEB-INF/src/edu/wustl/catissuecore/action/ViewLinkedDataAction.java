/**
 * <p>Title: IntegratorAction Class>
 * <p>Description:	This Class is used to handle integration of caTissue Core with caTies</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Krunal Thakkar
 * @version 1.00
 * Created on May 19, 2006
 */

package edu.wustl.catissuecore.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.IntegrationBizLogic;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.util.logger.Logger;

/**
 * This Class is used to handle integration of caTissue Core with caTies.
 * @author Krunal Thakkar
 */
public class ViewLinkedDataAction extends Action 
{
    /**
     * Overrides the execute method of Action class.
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception
    {
	    
	    Logger.out.debug("Identifier parameter received in IntegratorAction==>"+request.getParameter(Constants.SYSTEM_IDENTIFIER));
	    
	    Logger.out.debug("ApplicationName received in IntegrationAction==>"+request.getParameter(Constants.APPLICATION_ID));
	    
	    AbstractActionForm abstractActionForm = (AbstractActionForm)form;
	    
	    List integrationDataList=new ArrayList();
	    
	    Logger.out.debug("FormBean Identifier===>"+abstractActionForm.getId());
	    
	    Logger.out.debug("FormBean ID===>"+abstractActionForm.getFormId());
	    
	    //Getting instance of IntegrationBizLogic using BizLogicFactory
	    IntegrationBizLogic integrationBizLogic = (IntegrationBizLogic)BizLogicFactory.getInstance().getBizLogic(abstractActionForm.getFormId()); 
	    
	    //Retrieving linked data from integrated application i.e. CAE/caTies
	    integrationDataList = (ArrayList)integrationBizLogic.getLinkedAppData(new Long(request.getParameter(Constants.SYSTEM_IDENTIFIER)), request.getParameter(Constants.APPLICATION_ID));
	    
	    //populating linked data for Participant
	    if( abstractActionForm.getFormId() == Constants.PARTICIPANT_FORM_ID )
	    {
	        Logger.out.debug("Generating IntegrationData for Participant");
	        
	        List reportIdList = new ArrayList();
	        List reportList = new ArrayList();
	        
	        //check for size of the list of integration data
	        if(integrationDataList.size()>0)
	        {
	            //generating list of PathologyReport and PathologyReportId
	            for(int i=0; i<integrationDataList.size(); i++)
	            {
	                NameValueBean report=(NameValueBean)integrationDataList.get(i);
	                
	                reportIdList.add(new NameValueBean(report.getName(), new Integer(i)));
	                reportList.add((report.getValue()).replaceAll("\n", "\\\\n"));
	                
	                if(i==0)
	                {
	                    //request.setAttribute("firstReportId", report.getName() );//  new NameValueBean(report.getName(), report.getName()));
	                    request.setAttribute("firstReport", report.getValue());
	                }
	            }
	        }
	        
	        //Storing required attributes into Request
	        request.setAttribute("reportIdList", reportIdList);
	        request.setAttribute("reportList",reportList);
	    }
	    //populating linked data for SpecimenCollectionGroup and Specimen
	    else
	    {
	        Logger.out.debug("Generating IntegrationData for SpecimenCollectionGroup");
		    String integrationData=new String();
		    
		    //check for size of the list of integration data
		    if(integrationDataList.size()>0)
		    {
		        //generating pathologyreport from list of integration data
			    for(int i=0; i<integrationDataList.size(); i++)
			    {
			        integrationData += (String)integrationDataList.get(i);
			    }
		    }
		    
		    Logger.out.debug("IntegrationData in IntegratorAction===>"+integrationData);
		    
		    //Storing required attributes into Request
		    request.setAttribute(Constants.LINKED_DATA,integrationData);
	    }
	    
	    //Storing id into Request
	    request.setAttribute(Constants.SYSTEM_IDENTIFIER,request.getParameter(Constants.SYSTEM_IDENTIFIER));
	    
	    //Storing link of Edit Tab into Request
	    request.setAttribute(Constants.EDIT_TAB_LINK, request.getParameter("editTabLink"));
	    
	    //set the menu selection 
	    //request.setAttribute(Constants.MENU_SELECTED, "14"); 
	    
	    return mapping.findForward(Constants.SUCCESS);
    }
}
