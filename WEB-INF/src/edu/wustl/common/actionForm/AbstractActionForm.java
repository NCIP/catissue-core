/**
 * <p>Title: AbstractForm Class>
 * <p>Description:  AbstractForm class is the superclass of all the formbean classes. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 * Created on Apr 21, 2005
 */

package edu.wustl.common.actionForm;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Constants;
import edu.wustl.common.util.global.Validator;

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
    protected String activityStatus = Constants.ACTIVITY_STATUS_ACTIVE;

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
	 * Checks the operation to be performed is add operation.
	 * @return Returns true if operation is equal to "add", else it returns false
	 * */
	public boolean isAddOperation()
	{
		return(operation.equals(Constants.ADD));
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
     * Copies all the values from the Object object.
     * @param obj The Object object.
     */
    public void setAllVal(Object object){};
    
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
//        systemIdentifier = -1;
//        operation = null;
//        activityStatus = Constants.ACTIVITY_STATUS_ACTIVE;
        reset();
    }
    
    protected abstract void reset();
    
    // -------- Add new
    private String redirectTo = null;
       
	/**
	 * @return Returns the redirectTo.
	 */
	public String getRedirectTo() {
		return redirectTo;
	}
	/**
	 * @param redirectTo The redirectTo to set.
	 */
	public void setRedirectTo(String redirectTo) {
		this.redirectTo = redirectTo;
	}
	
	public void setRedirectValue(Validator validator)
	{
     	String cf = getRedirectTo();
     	if(validator.isEmpty(cf ) )
     		cf = "";
     	setRedirectTo(cf );
	}
	
	// -------- For success pages
	
	protected String onSubmit;
	
	/**
	 * @return Returns the onSubmit.
	 */
	public String getOnSubmit() {
		return onSubmit;
	}
	
	/**
	 * @param onSubmit The onSubmit to set.
	 */
	public void setOnSubmit(String onSubmit)
	{
		this.onSubmit = onSubmit;
	}
	
	// ----------------- forwardTo-------------
	protected String forwardTo = "success";
	
	/**
	 * @return Returns the forwardTo.
	 */
	public String getForwardTo() {
		return forwardTo;
	}
	/**
	 * @param forwardTo The forwardTo to set.
	 */
	public void setForwardTo(String forwardTo) {
		this.forwardTo = forwardTo;
	}
}