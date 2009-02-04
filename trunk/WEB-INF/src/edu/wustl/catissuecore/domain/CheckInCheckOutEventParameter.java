/**
 * <p>Title: CheckInCheckOutEventParameter Class</p>
 * <p>Description: A binary event to indicate whether a specimen has been removed from or returned to its recorded storage location. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Mandar Deshmukh
 * @version 1.00
 */

package edu.wustl.catissuecore.domain;

import edu.wustl.catissuecore.actionForm.CheckInCheckOutEventParametersForm;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.util.logger.Logger;

/**
 * A binary event to indicate whether a specimen has been removed from or returned to its recorded storage location.
 * @hibernate.joined-subclass table="CATISSUE_IN_OUT_EVENT_PARAM"
 * @hibernate.joined-subclass-key column="IDENTIFIER" 
 */
public class CheckInCheckOutEventParameter extends SpecimenEventParameters
		implements java.io.Serializable
{
	private static final long serialVersionUID = 1234567890L;

	/**
	 * Type of the movement e.g. Check-in or Check-out.
	 */
	protected String storageStatus;

	/**
	 * Returns the Type of the movement e.g. Check-in or Check-out.
	 * @hibernate.property name="storageStatus" type="string" column="STORAGE_STATUS" length="100"
	 * not-null="true"
	 * @return storageStatus of the EVENT.
	 */
	public String getStorageStatus()
	{
		return storageStatus;
	}

	/**
	 * Sets the storageStatus of the event.
	 * @param storageStatus storageStatus of the event.
	 */
	public void setStorageStatus(String storageStatus)
	{
		this.storageStatus = storageStatus;
	}
	
	/**
	 * NOTE: Do not delete this constructor. Hibernet uses this by reflection API.
	 * */
	public CheckInCheckOutEventParameter()
	{
		
	}
	
	/**	
	 * Parameterized constructor
	 * @param abstractForm
	 */
	public CheckInCheckOutEventParameter(AbstractActionForm abstractForm)
	{
		setAllValues(abstractForm);
	}
	
	/**
     * This function Copies the data from an CheckInCheckOutEventParameterForm object to a CheckInCheckOutEventParameter object.
     * @param checkInCheckOutEventParameterForm An CheckInCheckOutEventParameterForm object containing the information about the CheckInCheckOutEventParameter.  
     * */
    public void setAllValues(AbstractActionForm abstractForm)
    {
        try
        {
//        	//call to event parameters setallvalue method
//        	super.setAllValues(abstractForm);
        	CheckInCheckOutEventParametersForm form = (CheckInCheckOutEventParametersForm) abstractForm;
            this.storageStatus = form.getStorageStatus(); 
           	super.setAllValues(form);
        }
        catch (Exception excp)
        {
            Logger.out.error(excp.getMessage());
        }
    }

}