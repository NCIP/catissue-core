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

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.util.global.Constants;


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
        request.setAttribute(Constants.PAGEOF,pageOf);
        
        String storageContainerType = request.getParameter(Constants.STORAGE_CONTAINER_TYPE);
        request.setAttribute(Constants.STORAGE_CONTAINER_TYPE,storageContainerType);
        
        return mapping.findForward(Constants.SUCCESS);
    }

}
