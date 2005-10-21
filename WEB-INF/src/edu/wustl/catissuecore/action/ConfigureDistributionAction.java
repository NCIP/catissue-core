package edu.wustl.catissuecore.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.util.global.Constants;




public class ConfigureDistributionAction extends Action
{

	/**
	 * This is the initialization action class for configuring Distribution report
	 * @author Poornima Govindrao
	 *  
	 */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException
    {
    	//Set the tables for the configuration of distribution report
    	request.setAttribute(Constants.TABLE_ALIAS_NAME, Constants.DISTRIBUTION_TABLE_AlIAS);
    	return (mapping.findForward("Success"));
    }
}
    