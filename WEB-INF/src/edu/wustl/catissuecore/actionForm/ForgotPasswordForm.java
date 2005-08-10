/**
 * <p>Title: ForgotPasswordForm Class>
 * <p>Description:	ForgotPasswordForm Class is used to encapsulate all the request parameters passed 
 * from ForgotPassword webpage.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 */

package edu.wustl.catissuecore.actionForm;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.domain.AbstractDomainObject;
import edu.wustl.catissuecore.util.global.ApplicationProperties;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Validator;

/**
 * @author gautam_shetty
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ForgotPasswordForm extends AbstractActionForm
{

    /**
     * EmailAddress Address of the user whose password is to be searched.
     */
    private String emailAddress;

    public long getSystemIdentifier()
    {
        return 0;
    }

    /* (non-Javadoc)
     * @see edu.wustl.catissuecore.actionForm.AbstractActionForm#setSystemIdentifier(long)
     */
    public void setSystemIdentifier(long systemIdentifier)
    {
    }

    /**
     * @return Returns the emailAddress.
     */
    public String getEmailAddress()
    {
        return emailAddress;
    }

    /**
     * @param emailAddress The emailAddress to set.
     */
    public void setEmailAddress(String emailAddress)
    {
        this.emailAddress = emailAddress;
    }

    /* (non-Javadoc)
     * @see edu.wustl.catissuecore.actionForm.AbstractActionForm#getActivityStatus()
     */
    public String getActivityStatus()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see edu.wustl.catissuecore.actionForm.AbstractActionForm#setActivityStatus(java.lang.String)
     */
    public void setActivityStatus(String activityStatus)
    {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see edu.wustl.catissuecore.actionForm.AbstractActionForm#getFormId()
     */
    public int getFormId()
    {
        return Constants.FORGOT_PASSWORD_FORM_ID;
    }

    /* (non-Javadoc)
     * @see edu.wustl.catissuecore.actionForm.AbstractActionForm#isAddOperation()
     */
    public boolean isAddOperation()
    {
        return false;
    }

    /* (non-Javadoc)
     * @see edu.wustl.catissuecore.actionForm.AbstractActionForm#setAllValues(edu.wustl.catissuecore.domain.AbstractDomainObject)
     */
    public void setAllValues(AbstractDomainObject abstractDomain)
    {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see org.apache.struts.action.ActionForm#validate(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
     */
    public ActionErrors validate(ActionMapping arg0, HttpServletRequest arg1)
    {
        ActionErrors errors = new ActionErrors();
        Validator validator = new Validator();

        if (validator.isEmpty(emailAddress))
        {
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
                    "errors.item.required", ApplicationProperties
                            .getValue("user.emailAddress")));
        }
        else
        {
            if (!validator.isValidEmailAddress(emailAddress))
            {
                errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
                        "errors.item.format", ApplicationProperties
                                .getValue("user.emailAddress")));
            }
        }

        return errors;
    }
}