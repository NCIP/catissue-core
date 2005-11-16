package edu.wustl.catissuecore.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.util.global.Constants;




public class ConfigureDistributionAction extends BaseAction
{

	/**
	 * This is the initialization action class for configuring Distribution report
	 * @author Poornima Govindrao
	 *  
	 */
    protected ActionForward executeAction(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception
    {
    	//Set the tables for the configuration of distribution report
    	HttpSession session =request.getSession();
    	session.setAttribute(Constants.TABLE_ALIAS_NAME, Constants.DISTRIBUTION_TABLE_AlIAS);
    	request.setAttribute(Constants.PAGEOF,Constants.PAGEOF_DISTRIBUTION);
    	return (mapping.findForward("Success"));
    }
}
    