/*
 * Created on Jul 14, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package edu.wustl.catissuecore.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.util.logger.Logger;


/**
 * @author gautam_shetty
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SpecimenArrayTypeAction extends Action
{
    /* (non-Javadoc)
     * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        String operation = request.getParameter(Constants.OPERATION);
        request.setAttribute(Constants.OPERATION, operation);
        
        //Setting the specimen class list
        List specimenClassList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_SPECIMEN_CLASS,null);
    	request.setAttribute(Constants.SPECIMEN_CLASS_LIST, specimenClassList);
    	
    	//Setting the specimen type list
    	List specimenTypeList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_SPECIMEN_TYPE,null);
    	request.setAttribute(Constants.SPECIMEN_TYPE_LIST, specimenTypeList);
    	
		String strMenu = request.getParameter(Constants.MENU_SELECTED);
		if(strMenu != null) {
			request.setAttribute(Constants.MENU_SELECTED ,strMenu);
			Logger.out.debug(Constants.MENU_SELECTED + " " +strMenu +" set successfully");
		}
    	return mapping.findForward(Constants.SUCCESS);
    }
}
