/**
 * <p>Title: NewSpecimenAction Class>
 * <p>Description:	NewSpecimenAction initializes the fields in the New Specimen page.</p>
 * Copyright:    Copyright (c) year
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
 * NewSpecimenAction initializes the fields in the New Specimen page.
 * @author gautam_shetty
 */
public class NewSpecimenAction extends Action
{
    
    /**
     * Overrides the execute method of Action class.
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        //Gets the value of the operation parameter.
        String operation = request.getParameter(Constants.OPERATION);

        //Sets the operation attribute to be used in the Add/Edit User Page. 
        request.setAttribute(Constants.OPERATION, operation);
        
        String [] specimenCollectionGroupArray = {"1","2","3","4"}; 
        request.setAttribute(Constants.SPECIMEN_COLLECTION_GROUP_LIST,specimenCollectionGroupArray);
        
        request.setAttribute(Constants.SPECIMEN_TYPE_LIST, Constants.SPECIMEN_TYPE_VALUES);
        
        request.setAttribute(Constants.SPECIMEN_SUB_TYPE_LIST, Constants.SPECIMEN_SUB_TYPE_VALUES);
        
        request.setAttribute(Constants.TISSUE_SITE_LIST,Constants.TISSUE_SITE_VALUES);
        
        request.setAttribute(Constants.TISSUE_SIDE_LIST,Constants.TISSUE_SIDE_VALUES);
        
        request.setAttribute(Constants.PATHOLOGICAL_STATUS_LIST, Constants.PATHOLOGICAL_STATUS_VALUES);
        
        request.setAttribute(Constants.BIOHAZARD_NAME_LIST, Constants.BIOHAZARD_NAME_VALUES);
        
        request.setAttribute(Constants.BIOHAZARD_TYPE_LIST, Constants.BIOHAZARD_TYPE_VALUES);
        
        return mapping.findForward(Constants.SUCCESS);
    }

}
