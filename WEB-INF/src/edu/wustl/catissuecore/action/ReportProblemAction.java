/**
 * <p>Title: ParticipantAction Class>
 * <p>Description:  This class initializes the fields in the ReportProblem webpage.</p>
 * Copyright:  Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 * Created on Apr 18, 2005
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
 * This class initializes the fields in the ReportProblem webpage.
 * @author gautam_shetty
 */
public class ReportProblemAction extends Action
{
    /**
     * Overrides the execute method of Action class.
     * Sets the various in ReportProblem webpage.
     * */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException
    {
        return mapping.findForward(Constants.SUCCESS);
    }
}
