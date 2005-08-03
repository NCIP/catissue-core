/**
 * <p>Title: ReportedProblemPendingCloseAction Class>
 * <p>Description:  ReportedProblemPendingCloseAction Class is used to close and keep pending a reported problem.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 */

package edu.wustl.catissuecore.action;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.DomainObjectListForm;
import edu.wustl.catissuecore.bizlogic.AbstractBizLogic;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.domain.AbstractDomainObject;
import edu.wustl.catissuecore.domain.ReportedProblem;
import edu.wustl.catissuecore.util.global.Constants;


/**
 * ReportedProblemPendingCloseAction Class is used to close and keep pending a reported problem.
 * @author gautam_shetty
 */
public class ReportedProblemPendingCloseAction extends Action
{
    
    /**
     * Overrides the execute method in Action.
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception
    {
        DomainObjectListForm domainObjectListForm = (DomainObjectListForm)form;
        
        //Gets the collection of problems to be closed/kept pending.
        Collection col = domainObjectListForm.getAllValues();
        String target = null;
        
        Iterator iterator = col.iterator();

        AbstractBizLogic bizLogic = BizLogicFactory.getBizLogic(Constants.REPORTEDPROBLEM_FORM_ID);

        List list = null;
        String activityStatus = null;

        while (iterator.hasNext())
        {
            String identifier = (String) iterator.next();
            String objName = AbstractDomainObject.getDomainObjectName(Constants.REPORTEDPROBLEM_FORM_ID);
            list = bizLogic.retrieve(objName, Constants.IDENTIFIER, new Long(identifier));

            if (list.size() != 0)
            {
                ReportedProblem reportedProblem = (ReportedProblem) list.get(0);

                //Sets the comments given by the resolver.
                if (domainObjectListForm.getComments() != null)
                {
                    reportedProblem.setComments(domainObjectListForm.getComments());
                }

                if (domainObjectListForm.getOperation().equals(Constants.ACTIVITY_STATUS_CLOSED))
                {

                    //Change the Activity Status for closed status.
                    activityStatus = Constants.ACTIVITY_STATUS_CLOSED;

                }
                else
                {
                    //Change the Activity Status for pending status.
                    activityStatus = Constants.APPROVE_USER_PENDING_STATUS;
                }

                //Sets the activity status.
                reportedProblem.setActivityStatus(activityStatus);

                //Updates reported problem's information in the database.
                bizLogic.update(reportedProblem);
                
                target = new String(Constants.SUCCESS);
            }
        }
        
        return mapping.findForward(target);
    }
}
