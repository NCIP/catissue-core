/**
 * <p>Title: FrozenEventParametersForm Class</p>
 * <p>Description:  This Class handles the frozen event parameters..
 * <p> It extends the EventParametersForm class.    
 * Copyright:    Copyright (c) 2005
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Mandar Deshmukh
 * @version 1.00
 * Created on July 28th, 2005
 */

package edu.wustl.catissuecore.actionForm;

import edu.wustl.catissuecore.domain.AbstractDomainObject;
import edu.wustl.catissuecore.domain.FrozenEventParameters;
import edu.wustl.catissuecore.util.global.Constants;

/**
 * @author mandar_deshmukh
 *
 * Description:  This Class handles the frozen event parameters..
 */
public class FrozenEventParametersForm extends EventParametersForm
{
	
	/**
     * Method applied on specimen to freeze it.
     */
	private String method;
	
	 /**
     * A String containing the operation(Add/Edit) to be performed.
     * */
    private String operation = null;

	

	/**
	 * @return Returns the method applied on specimen to freeze it.
	 */
	public String getMethod()
	{
		return method;
	}
	
	/**
	 * @param method The method to set.
	 */
	public void setMethod(String method)
	{
		this.method = method;
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
	
	// ----- SUPERCLASS METHODS
	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.actionForm.AbstractActionForm#getFormId()
	 */
	public int getFormId()
	{
		return Constants.FROZEN_EVENT_PARAMETERS_FORM_ID;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.actionForm.AbstractActionForm#setAllValues(edu.wustl.catissuecore.domain.AbstractDomainObject)
	 */
	public void setAllValues(AbstractDomainObject abstractDomain)
	{
		FrozenEventParameters frozenEventParametersObject = (FrozenEventParameters)abstractDomain ;
		this.method = frozenEventParametersObject.getMethod();

		this.comments  = frozenEventParametersObject.getComments();
		this.systemIdentifier = frozenEventParametersObject.getSystemIdentifier().longValue() ;
		this.timeInHours = ""+frozenEventParametersObject.getTimestamp().getHours();
		this.timeInMinutes = "" + frozenEventParametersObject.getTimestamp().getMinutes();
		this.userId = frozenEventParametersObject.getUser().getSystemIdentifier().longValue() ;
		this.dateOfEvent = frozenEventParametersObject.getTimestamp().getMonth()+"-"+frozenEventParametersObject.getTimestamp().getDay()+"-"+frozenEventParametersObject.getTimestamp().getYear() ;     
	}

	/**
     * Checks the operation to be performed is add operation.
     * @return Returns true if operation is equal to "add", else it returns false
     * */
    public boolean isAddOperation()
    {
        return(getOperation().equals(Constants.ADD));
    }
    
	
	
}