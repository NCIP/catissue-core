/**
 * <p>Title: SpunEventParameters Class>
 * <p>Description:  Attributes associated with a spinning event of a specimen. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00
 * Created on Apr 7, 2005
 */
package edu.wustl.catissuecore.domain;

import edu.wustl.catissuecore.actionForm.SpunEventParametersForm;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.actionForm.IValueObject;
import edu.wustl.common.util.logger.Logger;

/**
 * Attributes associated with a spinning event of a specimen.
 * @hibernate.joined-subclass table="CATISSUE_SPUN_EVENT_PARAMETERS"
 * @hibernate.joined-subclass-key column="IDENTIFIER" 
 * @author Aniruddha Phadnis
 */
public class SpunEventParameters extends SpecimenEventParameters implements java.io.Serializable
{
	private static final long serialVersionUID = 1234567890L;

	/**
     * Rotational force applied to specimen.
     */
	protected Double gravityForce;
	
	/**
     * Duration for which specimen is spun.
     */
	protected Integer durationInMinutes;

	/**
     * Returns the rotational force applied to specimen. 
     * @return The rotational force applied to specimen.
     * @see #setGravityForce(Double)
     * @hibernate.property name="gravityForce" type="double" 
     * column="GFORCE" length="30"
     */
	public Double getGravityForce()
	{
		return gravityForce;
	}

	/**
     * Sets the rotational force applied to specimen.
     * @param gForce the rotational force applied to specimen.
     * @see #getGravityForce()
     */
	public void setGravityForce(Double gravityForce)
	{
		this.gravityForce = gravityForce;
	}

	/**
     * Returns duration for which specimen is spun. 
     * @return Duration for which specimen is spun.
     * @see #setDurationInMinutes(Integer)
     * @hibernate.property name="durationInMinutes" type="int" 
     * column="DURATION_IN_MINUTES" length="30"
     */
	public Integer getDurationInMinutes()
	{
		return durationInMinutes;
	}

	/**
     * Sets the duration for which specimen is spun.
     * @param durationInMinutes duration for which specimen is spun.
     * @see #getDurationInMinutes()
     */
	public void setDurationInMinutes(Integer durationInMinutes)
	{
		this.durationInMinutes = durationInMinutes;
	}
	
	
	/**
	 * NOTE: Do not delete this constructor. Hibernet uses this by reflection API.
	 * */
	public SpunEventParameters()
	{
		
	}

	/**
	 *  Parameterised constructor 
	 * @param abstractForm
	 */
	public SpunEventParameters(AbstractActionForm abstractForm)
	{
		setAllValues((IValueObject)abstractForm);
	}
	
	/**
     * This function Copies the data from an SpunEventParametersForm object to a SpunEventParameters object.
     * @param SpunEventParametersForm An SpunEventParametersForm object containing the information about the SpunEventParameters.  
     * */
    public void setAllValues(IValueObject abstractForm)
    {
        try
        {
        	SpunEventParametersForm form = (SpunEventParametersForm) abstractForm;
        	
        	if(form.getGravityForce() != null && form.getGravityForce().trim().length()>0)
        		this.gravityForce = Double.parseDouble(form.getGravityForce() );
        	if(form.getDurationInMinutes() != null && form.getDurationInMinutes().trim().length()>0)
        		this.durationInMinutes = Integer.parseInt(form.getDurationInMinutes() );

        	super.setAllValues(form);
        }
        catch (Exception excp)
        {
            Logger.out.error(excp.getMessage());
        }
    }
}