/**
 * <p>Title: DisposalEventParameters Class</p>
 * <p>Description: Attributes related to disposal event of a specimen. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Mandar Deshmukh
 * @version 1.00
 */

package edu.wustl.catissuecore.domain;

import edu.wustl.catissuecore.actionForm.DisposalEventParametersForm;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.util.logger.Logger;

/**
 * Attributes related to disposal event of a specimen.
 * @hibernate.joined-subclass table="CATISSUE_DISPOSAL_EVENT_PARAM"
 * @hibernate.joined-subclass-key column="IDENTIFIER" 
 */
public class DisposalEventParameters extends SpecimenEventParameters
		implements java.io.Serializable
{
	private static final long serialVersionUID = 1234567890L;

	/**
	 * The reason for which the specimen is disposed.
	 */
	protected String reason;

	/**
	 * Returns the reason of disposal.
	 * @hibernate.property name="reason" type="string" column="REASON" length="50"
	 * not-null="true"
	 * @return reason of disposal.
	 */
	public String getReason()
	{
		return reason;
	}

	/**
	 * Sets the reason. 
	 * @param reason reason of disposal.
	 */
	public void setReason(String reason)
	{
		this.reason = reason;
	}
	

	/**
	 * NOTE: Do not delete this constructor. Hibernet uses this by reflection API.
	 * */
	public DisposalEventParameters()
	{
		
	}

	/**
	 *  Parameterised constructor 
	 * @param abstractForm
	 */
	public DisposalEventParameters(AbstractActionForm abstractForm)
	{
		setAllValues(abstractForm);
	}
	
	/**
     * This function Copies the data from an DisposalEventParametersForm object to a DisposalEventParameters object.
     * @param DisposalEventParametersForm An DisposalEventParametersForm object containing the information about the DisposalEventParameters.  
     * */
    public void setAllValues(AbstractActionForm abstractForm)
    {
        try
        {
        	DisposalEventParametersForm form = (DisposalEventParametersForm) abstractForm;
        	this.reason = form.getReason(); 
        	super.setAllValues(form);
        }
        catch (Exception excp)
        {
            Logger.out.error(excp.getMessage());
        }
    }
   
}