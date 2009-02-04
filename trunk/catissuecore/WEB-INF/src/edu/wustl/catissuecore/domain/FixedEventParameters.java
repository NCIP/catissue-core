/**
 * <p>Title: FixedEventParameters Class</p>
 * <p>Description: Attributes associated with a fixation event of a specimen. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Mandar Deshmukh
 * @version 1.00
 */

package edu.wustl.catissuecore.domain;

import edu.wustl.catissuecore.actionForm.FixedEventParametersForm;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.util.logger.Logger;

/**
 * Attributes associated with a fixation event of a specimen.
 * @hibernate.joined-subclass table="CATISSUE_FIXED_EVENT_PARAM"
 * @hibernate.joined-subclass-key column="IDENTIFIER" 
 */
public class FixedEventParameters extends SpecimenEventParameters implements java.io.Serializable
{
	private static final long serialVersionUID = 1234567890L;

	/**
	 * Type of the fixation.
	 */
	protected String fixationType;

	/**
	 * Duration, measured in minutes, for which fixation is performed on specimen.
	 */
	protected Integer durationInMinutes;

	/**
	 * Returns the Type of the fixation.
	 * @hibernate.property name="fixationType" type="string" column="FIXATION_TYPE" length="50"
	 * not-null="true"
	 * @return fixationType.
	 */
	public String getFixationType()
	{
		return fixationType;
	}

	/**
	 * Sets the fixationType. 
	 * @param fixationType fixationType of specimen.
	 */
	public void setFixationType(String fixationType)
	{
		this.fixationType = fixationType;
	}

	/**
	 * Returns the  duration In Minutes.
	 * @hibernate.property name="durationInMinutes" type="int" column="DURATION_IN_MINUTES" length="30"
	 * @return durationInMinutes.
	 */
	public Integer getDurationInMinutes()
	{
		return durationInMinutes;
	}

	/**
	 * Sets the durationInMinutes. 
	 * @param durationInMinutes durationInMinutes required for fixation.
	 */
	public void setDurationInMinutes(Integer durationInMinutes)
	{
		this.durationInMinutes = durationInMinutes;
	}
	
	/**
	 * NOTE: Do not delete this constructor. Hibernet uses this by reflection API.
	 * */
	public FixedEventParameters()
	{
		
	}

	/**
	 *  Parameterised constructor 
	 * @param abstractForm
	 */
	public FixedEventParameters(AbstractActionForm abstractForm)
	{
		setAllValues(abstractForm);
	}
	
	/**
     * This function Copies the data from an FixedEventParametersForm object to a FixedEventParameters object.
     * @param FixedEventParametersForm An FixedEventParametersForm object containing the information about the FixedEventParameters.  
     * */
    public void setAllValues(AbstractActionForm abstractForm)
    {
        try
        {
        	FixedEventParametersForm form = (FixedEventParametersForm) abstractForm;
        	
        	super.setAllValues(form);
        	
        	
        	this.fixationType = form.getFixationType();
        	this.durationInMinutes = new Integer(form.getDurationInMinutes() );
        }
        catch (Exception excp)
        {
            Logger.out.error(excp.getMessage());
        }
    }
	
}