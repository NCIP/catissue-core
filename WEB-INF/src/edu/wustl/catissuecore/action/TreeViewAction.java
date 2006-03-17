/**
 * <p>Title: TreeViewAction Class>
 * <p>Description: TreeViewAction is used to display the query results tree view.</p>
 * Copyright: Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 */
package edu.wustl.catissuecore.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.util.logger.Logger;


/**
 * TreeViewAction is used to display the query results tree view.
 * @author gautam_shetty
 */
public class TreeViewAction extends Action
{
    
    /**
     * Overrides the execute method in Action class.
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        //Sets the pageOf attribute (for Add,Edit or Query Interface)
        String pageOf  = request.getParameter(Constants.PAGEOF);
        Logger.out.debug("pageOf in treeview........"+pageOf);
        request.setAttribute(Constants.PAGEOF,pageOf);
        
        if (pageOf.equals(Constants.PAGEOF_STORAGE_LOCATION))
        {
        	String storageContainerType = request.getParameter(Constants.STORAGE_CONTAINER_TYPE);
        	request.setAttribute(Constants.STORAGE_CONTAINER_TYPE,storageContainerType);
        	String storageContainerID = request.getParameter(Constants.STORAGE_CONTAINER_TO_BE_SELECTED);
        	request.setAttribute(Constants.STORAGE_CONTAINER_TO_BE_SELECTED,storageContainerID);
        	String position = request.getParameter(Constants.STORAGE_CONTAINER_POSITION);
        	request.setAttribute(Constants.STORAGE_CONTAINER_POSITION,position); 	
        }
        else if (pageOf.equals(Constants.PAGEOF_TISSUE_SITE))
        {
            HttpSession session = request.getSession();
            String cdeName = (String)session.getAttribute(Constants.CDE_NAME);
            session.removeAttribute(Constants.CDE_NAME);
            request.setAttribute(Constants.CDE_NAME, cdeName);
        }
        
        return mapping.findForward(Constants.SUCCESS);
    }

}
