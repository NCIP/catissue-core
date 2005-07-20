/**
 * <p>Title: StorageContainerAction Class>
 * <p>Description:	This class initializes the fields of StorageContainer.jsp Page</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00
 * Created on Jul 18, 2005
 */

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

public class StorageContainerAction extends Action
{
    /**
     * Overrides the execute method of Action class.
     * Initializes the various fields in StorageContainer.jsp Page.
     * */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException
    {
        //Gets the value of the operation parameter.
        String operation = request.getParameter(Constants.OPERATION);
        
        //Sets the operation attribute to be used in the Add/Edit Institute Page. 
        request.setAttribute(Constants.OPERATION, operation);
        
        //Sets the storageTypeList attribute to be used in the Storage Container Add/Edit Page.
        String [] storageContainerArray = {"Freezer","Box"};
        request.setAttribute(Constants.STORAGECONTAINERLIST, storageContainerArray);
        
        //Sets the siteList attribute to be used in the Storage Container Add/Edit Page.
        String [] siteArray = {"Barnes/Jewish","Site2"};
        request.setAttribute(Constants.SITELIST, siteArray);
        
        return mapping.findForward(Constants.SUCCESS);
    }
}