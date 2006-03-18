/**
 * <p>Title: ShowFramedPageAction Class>
 * <p>Description:	ShowFramedPageAction is used to display the query results view.</p>
 * Copyright:    Copyright (c) year
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

/**
 * ShowFramedPageAction is used to display the query results view
 * @author gautam_shetty
 */
public class ShowFramedPageAction extends Action
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
        
        if (pageOf.equals(Constants.PAGEOF_STORAGE_LOCATION))
        {
            String storageContainerType = request.getParameter(Constants.STORAGE_CONTAINER_TYPE);
            request.setAttribute(Constants.STORAGE_CONTAINER_TYPE,storageContainerType);
            String storageContainerID = request.getParameter(Constants.STORAGE_CONTAINER_TO_BE_SELECTED);
            request.setAttribute(Constants.STORAGE_CONTAINER_TO_BE_SELECTED,storageContainerID);
            String position = request.getParameter(Constants.STORAGE_CONTAINER_POSITION);
            request.setAttribute(Constants.STORAGE_CONTAINER_POSITION,position);
            
        }else if (pageOf.equals(Constants.PAGEOF_TISSUE_SITE))
        {
            String propertyName = request.getParameter(Constants.PROPERTY_NAME);
            request.setAttribute(Constants.PROPERTY_NAME,propertyName);
            
            String cdeName = request.getParameter(Constants.CDE_NAME);
            HttpSession session = request.getSession();
            session.setAttribute(Constants.CDE_NAME, cdeName);
        }
        
        return mapping.findForward(pageOf);
    }
}
