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
import edu.wustl.common.bizlogic.AbstractBizLogic;
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
	    
	    Logger.out.debug("ApplicationName received in IntegrationAction==>"+request.getParameter("applicationName"));
	    
	    AbstractActionForm abstractActionForm = (AbstractActionForm)form;
	    
	    ArrayList integrationDataList=new ArrayList();
	    
	    Logger.out.debug("FormBean Identifier===>"+abstractActionForm.getSystemIdentifier());
	    
	    Logger.out.debug("FormBean ID===>"+abstractActionForm.getFormId());
	    
	    AbstractBizLogic bizLogic = BizLogicFactory.getBizLogic(abstractActionForm.getFormId());
	    
	    IntegrationBizLogic integrationBizLogic= (IntegrationBizLogic)bizLogic;
	    
	    integrationDataList= (ArrayList)integrationBizLogic.getLinkedAppData(new Long(request.getParameter(Constants.SYSTEM_IDENTIFIER)), request.getParameter("applicationName"));
	    
	    String integrationData=new String();
	    
	    if(integrationDataList.size()>0)
	    {
		    for(int i=0; i<integrationDataList.size(); i++)
		    {
		        integrationData += (String)integrationDataList.get(i);
		    }
	    }
	    
	    Logger.out.debug("IntegrationData in IntegratorAction===>"+integrationData);
	    
	    request.setAttribute(Constants.SYSTEM_IDENTIFIER,request.getParameter(Constants.SYSTEM_IDENTIFIER));
	   
	    request.setAttribute("integrationData",integrationData);
	    
	    request.setAttribute("actionForm", form);
	    
	    return mapping.findForward("success");
    }
}
