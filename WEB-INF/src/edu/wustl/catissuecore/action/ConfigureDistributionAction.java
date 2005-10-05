package edu.wustl.catissuecore.action;

import java.io.IOException;



import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.ConfigureResultViewForm;
import edu.wustl.catissuecore.util.global.Constants;




public class ConfigureDistributionAction extends Action
{

    /**
     * Overrides the execute method of Action class.
     * */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException
    {
    	request.setAttribute(Constants.TABLE_IDS, Constants.DISTRIBUTION_TABLE_IDS);
    	return (mapping.findForward("Success"));
    }
}
    