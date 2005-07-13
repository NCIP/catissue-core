/**
 * <p>Title: ActivityStatus Class>
 * <p>Description:  Models the Activity Status information. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Kapil Kaveeshwar
 * @version 1.00
 */
package edu.wustl.catissuecore.domain;

import edu.wustl.catissuecore.actionForm.AbstractActionForm;


/**
 * Models the Activity Status information.
 * @hibernate.class table="CATISSUE_ACTIVITY_STATUS"
 **/
public class ActivityStatus extends AbstractDomainObject
{
	/**
	 * id used by hibernate for as unique identifier
	 * */
	protected Long identifier;
	
	/**
	 * A string containing the status of various objects.
	 */
	protected String status = "";
	
	/**
	 * Initialize a new ActivityStatus instance. 
	 * Note: Invoked by hibernate through reflection API.  
	 */
	public ActivityStatus()
	{
	}
	
	/**
	 * Returns the identifier assigned to ActivityStatus.
	 * @hibernate.id name="identifier" column="IDENTIFIER" type="long" length="30"
     * unsaved-value="null" generator-class="native"
     * @return returns a unique identifier assigned to the ActivityStatus.
     * @see #setIdentifier(int)
	 * */
	public Long getIdentifier()
	{
		return identifier;
	}
	
	/**
	 * Sets an identifier for the ActivityStatus.
	 * @param identifier Unique identifier to be assigned to the ActivityStatus.
	 * @see #getIdentifier()
	 * */
	public void setIdentifier(Long identifier)
	{
		this.identifier = identifier;
	}
	
	/**
	 * Returns the status value.
	 * @hibernate.property name="status" type="string" 
     * column="STATUS" length="50" not-null="true" unique="true"
	 * @return Returns the status value. 
	 * @see #setStatus(String)
	 */
	public String getStatus()
	{
		return status;
	}
	/**
	 * Sets the status value.
	 * @param status Status of the activity.
	 * @see #getStatus()
	 */
	public void setStatus(String status)
	{
		this.status = status;
	}
	
	
    /* (non-Javadoc)
     * @see edu.wustl.catissuecore.domain.AbstractDomain#setAllValues(edu.wustl.catissuecore.actionForm.AbstractForm)
     */
    public void setAllValues(AbstractActionForm abstractForm)
    {
 
    }
}