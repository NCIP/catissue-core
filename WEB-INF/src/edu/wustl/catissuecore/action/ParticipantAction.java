/**
 * <p>Title: ParticipantAction Class>
 * <p>Description:  This class initializes the fields in the Participant Add/Edit webpage. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 * Created on Apr 7, 2005
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


/**
 * This class initializes the fields in the Participant Add/Edit webpage.
 * @author gautam_shetty
 */
public class ParticipantAction extends Action
{
    /**
     * Overrides the execute method of Action class.
     * Sets the various fields in Participant Add/Edit webpage.
     * */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException
    {
        //Gets the value of the operation parameter.
        String operation = request.getParameter(Constants.OPERATION);
        
        //Sets the operation attribute to be used in the Add/Edit Participant Page. 
        request.setAttribute(Constants.OPERATION,operation);
        
        //Sets the genderList attribute to be used in the Add/Edit Participant Page. 
        request.setAttribute(Constants.GENOTYPIC_GENDER_LIST, Constants.GENOTYPIC_GENDER_ARRAY);
        
        request.setAttribute(Constants.ETHNICITY_LIST, Constants.ETHNICITY_VALUES);
        
        request.setAttribute(Constants.PARTICIPANT_MEDICAL_RECORD_SOURCE_LIST,Constants.PARTICIPANT_MEDICAL_RECORD_SOURCE_VALUES);
        
        //Sets the raceList attribute to be used in the Add/Edit Participant Page. 
        request.setAttribute(Constants.RACELIST,Constants.RACEARRAY);
        
        //Sets the pageOf attribute (for Add,Edit or Query Interface)
        String pageName  = request.getParameter(Constants.PAGEOF);
        request.setAttribute(Constants.PAGEOF,pageName);
        
        return mapping.findForward(Constants.SUCCESS);
    }

}
