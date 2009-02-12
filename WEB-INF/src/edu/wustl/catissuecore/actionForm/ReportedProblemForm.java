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

import edu.wustl.catissuecore.domain.ReportedProblem;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;

/**
 * ReportedProblemForm Class is used to encapsulate all the request parameters passed 
 * from ReportProblem webpage.
 * @author gautam_shetty
 */
public class ReportedProblemForm extends AbstractActionForm
{

	private static final long serialVersionUID = 1L;

	/**
	 * logger Logger - Generic logger.
	 */
	private static org.apache.log4j.Logger logger = Logger.getLogger(ReportedProblemForm.class);
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
    	activityStatus = Constants.ACTIVITY_STATUS_PENDING;
    	clear();
    }

    private void clear()
    {
    	reset();
    }
    /**
     * Resets all the fields.
     */
    protected void reset()
    {
        this.from = null;
        this.subject = null;
        this.messageBody = null;
        this.nameOfReporter = null;
        this.affiliation = null;
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
        return Constants.REPORTED_PROBLEM_FORM_ID;
    }

  
    /**
     *@param abstractDomain An AbstractDomain Object  
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
	 * Overrides the validate method of ActionForm.
	 * @return error ActionErrors instance
	 * @param mapping Actionmapping instance
	 * @param request HttpServletRequest instance
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
                                            .getValue("user.emailAddress")));
                        }
                    }
                 	// Mandar 10-apr-06 : bugid :353 
                	// Error messages should be in the same sequence as the sequence of fields on the page.

                    if (validator.isEmpty(nameOfReporter))
                    {
                        errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
                                "errors.item.required", ApplicationProperties
                                        .getValue("fields.nameofreporter")));
                    }
                    if (validator.isEmpty(affiliation))
                    {
                        errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
                                "errors.item.required", ApplicationProperties
                                        .getValue("fields.affiliation")));
                    }

                    if (validator.isEmpty(subject))
                    {
                        errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
                                "errors.item.required", ApplicationProperties
                                        .getValue("fields.title")));
                    }


                    
                    if (validator.isEmpty(messageBody))
                    {
                        errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
                                "errors.item.required", ApplicationProperties
                                        .getValue("fields.message")));
                    }
                    
                    //to fix bug:1678
                    if (messageBody == null || messageBody.trim().length() >= Constants.MESSAGE_LENGTH)
                    {
                        errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
                                "reportedProblem.error.message",ApplicationProperties
                                .getValue("fields.message"), Integer.valueOf(Constants.MESSAGE_LENGTH)));
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
        	logger.error(excp.getMessage(), excp);
        }
        return errors;
    }
    
}