/**
 * <p>Title: SpecimenCollectionGroupAction Class>
 * <p>Description:	SpecimenCollectionGroupAction initializes the fields in the 
 * New Specimen Collection Group page.</p>
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
 * SpecimenCollectionGroupAction initializes the fields in the 
 * New Specimen Collection Group page.
 * @author gautam_shetty
 */
public class SpecimenCollectionGroupAction extends Action
{
    
    /**
     * Overrides the execute method of Action class.
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        //Gets the value of the operation parameter.
        String operation = request.getParameter(Constants.OPERATION);

        //Sets the operation attribute to be used in the Add/Edit SpecimenCollectionGroup Page. 
        request.setAttribute(Constants.OPERATION, operation);
        
        request.setAttribute(Constants.PROTOCOL_TITLE_LIST, Constants.PROTOCOL_TITLE_ARRAY);
        
        request.setAttribute(Constants.PARTICIPANT_NAME_LIST,Constants.PARTICIPANT_NAME_ARRAY);
        
        request.setAttribute(Constants.PROTOCOL_PARTICIPANT_NUMBER_LIST,Constants.PROTOCOL_PARTICIPANT_NUMBER_ARRAY);
        
        request.setAttribute(Constants.STUDY_CALENDAR_EVENT_POINT_LIST,Constants.STUDY_CALENDAR_EVENT_POINT_ARRAY);
        
        request.setAttribute(Constants.CLINICAL_STATUS_LIST,Constants.CLINICAL_STATUS_ARRAY);
        
        return mapping.findForward(Constants.SUCCESS);
    }
}
