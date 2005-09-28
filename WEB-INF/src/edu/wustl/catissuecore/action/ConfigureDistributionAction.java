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
    	//ConfigureResultViewForm configForm = (ConfigureResultViewForm)form;
    	//configForm.setAction("configure");
    	String []tableIds = {"27","31","33","35","37"};
    	request.setAttribute(Constants.TABLE_IDS, tableIds);
    	return (mapping.findForward("Success"));
    }
}
    