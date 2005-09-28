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

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.domain.AbstractDomainObject;
import edu.wustl.catissuecore.util.global.ApplicationProperties;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Validator;

/**
 * AbstractForm class is the superclass of all the formbean classes.
 * @author gautam_shetty
 */
public abstract class AbstractActionForm extends ActionForm
{
    
    /**
     * Specifies whether the system identifier is mutable or not.
     */
    private boolean mutable = true;
    
    /**
     * System generated unique identifier.
     * */
    protected long systemIdentifier;
    
    /**
     * Specifies the page associated with this form bean.
     */
    protected String pageOf;
    
    /**
     * A String containing the operation(Add/Edit) to be performed.
     * */
    protected String operation;
    
    /**
     * Activity Status.
     */
    protected String activityStatus;

    /**
     * @return Returns the mutable.
     */
    public boolean isMutable()
    {
        return mutable;
    }
    
    /**
     * @param mutable The mutable to set.
     */
    public void setMutable(boolean mutable)
    {
        this.mutable = mutable;
    }
    
    /**
     * Returns system generated unique identifier.
     * @return system generated unique identifier.
     * @see #setSystemIdentifier(long)
     * */
	public long getSystemIdentifier()
	{
		return systemIdentifier;
	}

	/**
     * Sets system generated unique identifier.
     * @param identifier system generated unique identifier.
     * @see #getSystemIdentifier()
     * */
	public void setSystemIdentifier(long identifier)
	{
	    if (isMutable())
	        this.systemIdentifier = identifier;
	}
	
    /**
     * Returns the operation(Add/Edit) to be performed.
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
     * Returns the specifies the page associated with this form bean.
     * @return
     */
    public String getPageOf()
    {
        return pageOf;
    }
    
    /**
     * Sets the page associated with this form bean.
     * @param pageOf
     */
    public void setPageOf(String pageOf)
    {
        this.pageOf = pageOf;
    }

    /**
	 * Returns the activity status
	 * @return the activityStatus.
	 * @see #setActivityStatus(String)
	 */
	public String getActivityStatus()
	{
		return activityStatus;
	}
	/**
	 * Sets the activity status.
	 * @param activityStatus activity status.
	 * @see #getActivityStatus()
	 */
	public void setActivityStatus(String activityStatus)
	{
		this.activityStatus = activityStatus;
	}
	
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
    
    /**
     * Resets the values of all the fields.
     * This method defined in ActionForm is overridden in this class.
     */
    public void reset(ActionMapping mapping, HttpServletRequest request)
    {
        systemIdentifier = -1;
        operation = null;
        activityStatus = Constants.ACTIVITY_STATUS_ACTIVE;
        reset();
    }
    
    protected abstract void reset();
}