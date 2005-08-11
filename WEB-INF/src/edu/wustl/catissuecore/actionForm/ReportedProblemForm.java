/**
 * <p>Title: ReportedProblemForm Class>
 * <p>Description: ReportedProblemForm Class is used to encapsulate all the request parameters passed 
 * from ReportProblem webpage.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 * Created on Apr 11, 2005
 */

package edu.wustl.catissuecore.actionForm;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.domain.AbstractDomainObject;
import edu.wustl.catissuecore.domain.ReportedProblem;
import edu.wustl.catissuecore.util.global.ApplicationProperties;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Validator;
import edu.wustl.common.util.logger.Logger;

/**
 * ReportedProblemForm Class is used to encapsulate all the request parameters passed 
 * from ReportProblem webpage.
 * @author gautam_shetty
 */
public class ReportedProblemForm extends AbstractActionForm
{

    /**
     * systemIdentifier is a unique id assigned to each reported problem.
     * */
    private long systemIdentifier;

    /**
     * States which operation is to be performed.
     */
    private String operation;

    /**
     * The subject of the reported problem.
     */
    private String subject;

    /**
     * The email id of who reported the problem.
     */
    private String from;

    /**
     * The message body of the reported problem.
     */
    private String messageBody;

    /**
     * States the activity status of the reported problem.
     */
    private String activityStatus = new String();
    
    private String comments;
    

    
    private String nameOfReporter;
    
    private String affiliation;

    /**
     * The affiliation of the user with the reported problem.
     * @return The affiliation of the reported problem.
     * @see #setAffiliation(String affiliation) 
     */
	public String getAffiliation()
	{
		return affiliation;
	}
	/**
	 * @param affiliation The affiliation to set.
	 */
	public void setAffiliation(String affiliation)
	{
		this.affiliation = affiliation;
	}
    /**
     * The name of the user who reported the problem.
     * @return The name of the user who reported the problem.
     * @see #setNameOfReporter(String nameOfReporter) 
     */
	public String getNameOfReporter()
	{
		return nameOfReporter;
	}
	/**
	 * @param nameOfReporter The nameOfReporter to set.
	 */
	public void setNameOfReporter(String nameOfReporter)
	{
		this.nameOfReporter = nameOfReporter;
	}

    
    
    
    
    
    /**
     * Initializes an empty problem.
     */
    public ReportedProblemForm()
    {
        reset();
    }

    /**
     * Resets all the fields.
     */
    protected void reset()
    {
        this.systemIdentifier = -1;
        this.operation = null;
        this.from = null;
        this.subject = null;
        this.messageBody = null;
        this.activityStatus = Constants.APPROVE_USER_PENDING_STATUS;
        this.nameOfReporter = null;
        this.affiliation = null;
    }

    /**
     * Resets all the fields.
     * Overridden method defined in ActionForm.
     */
    public void reset(ActionMapping mapping, HttpServletRequest request)
    {
        reset();
    }

    /**
     * Returns the systemIdentifier assigned to the reported problem.
     * @return the systemIdentifier assigned to the reported problem.
     * @see #setIdentifier(long)
     */
    public long getSystemIdentifier()
    {
        return systemIdentifier;
    }

    /**
     * Sets the systemIdentifier to the reported problem.
     * @param systemIdentifier The systemIdentifier to set.
     * @see #getIdentifier()
     */
    public void setSystemIdentifier(long systemIdentifier)
    {
        this.systemIdentifier = systemIdentifier;
    }

    /**
     * Returns the operation to be performed.
     * @return Returns the operation.
     * @see #setOperation(String)
     */
    public String getOperation()
    {
        return operation;
    }

    /**
     * Sets the operation to be performed.
     * @param operation The operation to set.
     * @see #getOperation()
     */
    public void setOperation(String operation)
    {
        this.operation = operation;
    }

    /**
     * Returns the email id of who reported the problem.
     * @return the email id of who reported the problem.
     * @see #setFrom(String)
     */
    public String getFrom()
    {
        return from;
    }

    /**
     * Sets the email id of who reported the problem.
     * @param from the email id of who reported the problem.
     * @see #getFrom()
     */
    public void setFrom(String from)
    {
        this.from = from;
    }

    /**
     * The message body of the reported problem.
     * @return The message body of the reported problem.
     * @see #setMessageBody(String) 
     */
    public String getMessageBody()
    {
        return messageBody;
    }

    /**
     * Sets the message body of the reported problem.
     * @param messageBody he message body of the reported problem.
     * @see #getMessageBody()
     */
    public void setMessageBody(String messageBody)
    {
        this.messageBody = messageBody;
    }

    /**
     * Returns the subject of the reported problem.
     * @return the subject of the reported problem.
     * @see #setSubject(String)
     */
    public String getSubject()
    {
        return subject;
    }

    /**
     * Sets the subject of the reported problem.
     * @param subject The subject to set.
     * @see #getSubject()
     */
    public void setSubject(String subject)
    {
        this.subject = subject;
    }

    /**
     * Returns the activity status of the participant.
     * @return Returns the activity status of the participant.
     * @see #setActivityStatus(ActivityStatus)
     */
    public String getActivityStatus()
    {
        return activityStatus;
    }

    /**
     * Sets the activity status of the participant.
     * @param activityStatus activity status of the participant.
     * @see #getActivityStatus()
     */
    public void setActivityStatus(String activityStatus)
    {
        this.activityStatus = activityStatus;
    }

    /**
     * @return Returns the comments.
     */
    public String getComments()
    {
        return comments;
    }
    /**
     * @param comments The comments to set.
     */
    public void setComments(String comments)
    {
        this.comments = comments;
    }
    
    /** 
     * Returns the form id.
     * @return the form id.
     * @see AbstractActionForm#getFormId()
     */
    public int getFormId()
    {
        return Constants.REPORTEDPROBLEM_FORM_ID;
    }

    /**
     * Returns true if the operation is add else returns false.
     * @return true if the operation is add else returns false.
     */
    public boolean isAddOperation()
    {
        return operation.equals(Constants.ADD);
    }

    /**
     * (non-Javadoc)
     * @see edu.wustl.catissuecore.actionForm.AbstractActionForm#setAllValues(edu.wustl.catissuecore.domain.AbstractDomain)
     */
    public void setAllValues(AbstractDomainObject abstractDomain)
    {
        ReportedProblem reportedProblem = (ReportedProblem)abstractDomain; 
        this.from = reportedProblem.getFrom();
        this.subject = reportedProblem.getSubject();
        this.messageBody = reportedProblem.getMessageBody();
        this.comments = reportedProblem.getComments();
        this.activityStatus = reportedProblem.getActivityStatus();
        this.affiliation = reportedProblem.getAffiliation();
        this.nameOfReporter = reportedProblem.getNameOfReporter() ;
    }

    /**
     * Overrides the validate method of Action class.
     */
    public ActionErrors validate(ActionMapping mapping,
            HttpServletRequest request)
    {
        ActionErrors errors = new ActionErrors();
        Validator validator = new Validator();

        try
        {
            if (operation != null)
            {
                if (operation.equals(Constants.ADD))
                {
                    if (validator.isEmpty(from))
                    {
                        errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
                                "errors.item.required", ApplicationProperties
                                        .getValue("fields.from")));
                    }
                    else
                    {
                        if (!validator.isValidEmailAddress(from))
                        {
                            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
                                    "errors.item.format", ApplicationProperties
                                            .getValue("user.emailAddress")
                                            + " in From Field"));
                        }
                    }

                    if (validator.isEmpty(subject))
                    {
                        errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
                                "errors.item.required", ApplicationProperties
                                        .getValue("fields.title")));
                    }

                    if (validator.isEmpty(nameOfReporter ))
                    {
                        errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
                                "errors.item.required", ApplicationProperties
                                        .getValue("fields.nameofreporter")));
                    }
                    if (validator.isEmpty(affiliation ))
                    {
                        errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
                                "errors.item.required", ApplicationProperties
                                        .getValue("fields.affiliation")));
                    }

                    
                    if (validator.isEmpty(messageBody))
                    {
                        errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
                                "errors.item.required", ApplicationProperties
                                        .getValue("fields.message")));
                    }
                }
                
                if (operation.equals(Constants.EDIT))
                {
                    if (activityStatus.trim().equals(Constants.SELECT_OPTION))
                    {
                        errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
                                "errors.item.required", ApplicationProperties
                                        .getValue("reportProblem.status")));
                    }
                }
            }
        }
        catch (Exception excp)
        {
            Logger.out.error(excp.getMessage(), excp);
        }
        return errors;
    }
    
}