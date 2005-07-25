/**
 * <p>Title: AbstractForm Class>
 * <p>Description:  AbstractForm class is the superclass of all the formbean classes. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 * Created on Apr 21, 2005
 */

package edu.wustl.catissuecore.actionForm;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;

import edu.wustl.catissuecore.domain.AbstractDomainObject;
import edu.wustl.catissuecore.util.global.ApplicationProperties;
import edu.wustl.catissuecore.util.global.Validator;

/**
 * AbstractForm class is the superclass of all the formbean classes.
 * @author gautam_shetty
 */
public abstract class AbstractActionForm extends ActionForm
{

    /**
     * Returns the systemIdentifier assigned to Participant.
     * @return int representing the id assigned to User.
     * @see #setIdentifier(int)
     * */
    public abstract long getSystemIdentifier();

    /**
     * Sets an id for the Participant.
     * @param systemIdentifier id to be assigned to the Participant.
     * @see #getIdentifier()
     * */
    public abstract void setSystemIdentifier(long systemIdentifier);

    /**
     * Returns the id assigned to form bean.
     * @return the id assigned to form bean
     */
    public abstract int getFormId();
    
    /**
     * Copies all the values from the Object object.
     * @param obj The Object object.
     */
    public abstract void setAllValues(AbstractDomainObject abstractDomain);
    
    /**
     * Returns true if the operation is Add, else returns false.
     * @return true if the operation is Add, else returns false.
     */
    public abstract boolean isAddOperation();
    
    /**
     * @return Returns the activityStatus.
     */
    public abstract String getActivityStatus();
    
    /**
     * @param activityStatus The activityStatus to set.
     */
    public abstract void setActivityStatus(String activityStatus);
    
    /**
     * Checks the validity of string value of the component and adds an ActionError object in the ActionErrors object.
     * @param componentName Component which is to be checked.
     * @param labelName Label of the component on the jsp page which is checked.
     * @param errors ActionErrors Object.
     */
    protected void checkValidString(String componentName, String labelName, ActionErrors errors,Validator validator)
    {
        if (validator.isEmpty(componentName))
        {
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue(labelName)));
        }
        else
        {
            if(!validator.isAlpha(componentName))
            {
                errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.format",ApplicationProperties.getValue(labelName)));
            }
        }
    }

    /**
     * Checks the validity of numeric value of the component and adds an ActionError object in the ActionErrors object.
     * @param componentName Component which is to be checked.
     * @param labelName Label of the component on the jsp page which is checked.
     * @param errors ActionErrors Object.
     */
    protected void checkValidNumber(String componentName, String labelName, ActionErrors errors, Validator validator)
    {
        if (validator.isEmpty(componentName))
        {
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue(labelName)));
        }
        else
        {
            if(!validator.isNumeric(componentName))
            {
                errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.format",ApplicationProperties.getValue(labelName)));
            }
        }
    }
}