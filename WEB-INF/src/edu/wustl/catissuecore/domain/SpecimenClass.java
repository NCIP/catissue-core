
package edu.wustl.catissuecore.domain;

import java.io.Serializable;

import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;

/**
 * A single unit of tissue, body fluid, or derivative biological macromolecule 
 * that is collected or created from a Participant
 * @hibernate.class table="CATISSUE_SPECIMEN_CLASS"
 *  
 */
public class SpecimenClass extends AbstractDomainObject implements Serializable
{

	private static final long serialVersionUID = 1234567890L;
	/**
	 * System generated unique systemIdentifier.
	 */
	protected Long systemIdentifier;

	/** name of a specimen class
	 * 
	 */
	protected String name;

	/**
	 * Defines whether this Specimen class record can be queried (ACTIVE) or not queried (INACTIVE) by any actor.
	 */
	protected String activityStatus;
 

	/**
	 * Returns the class of specimen
	 * @hibernate.property name="name" type="string" column="NAME" length="50"
	 * @see #setName(String)
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Sets the class of specimen.
	 * 
	 * @see #getName()
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * Returns the system generated unique systemIdentifier.
	 * @hibernate.id name="systemIdentifier" column="IDENTIFIER" type="long" length="30" 
	 * unsaved-value="null" generator-class="native"
	 * @hibernate.generator-param name="sequence" value="CATISSUE_SPECIMEN_CLASS_SEQ"
	 * @return the system generated unique systemIdentifier.
	 * @see #setSystemIdentifier(Long)
	 * */
	public Long getSystemIdentifier()
	{
		return systemIdentifier;
	}

	/**
	 * Sets the system generated unique systemIdentifier.
	 * @param systemIdentifier the system generated unique systemIdentifier.
	 * @see #getSystemIdentifier()
	 * */
	public void setSystemIdentifier(Long systemIdentifier)
	{
		this.systemIdentifier = systemIdentifier;
	}
	/**
     * Returns the activity status of the specimen class. 
     * @return The activity status of specimen class.
     * @see #setActivityStatus(String)
     * @hibernate.property name="activityStatus" type="string" default="Active"
     * column="ACTIVITY_STATUS" length="30"
     */
	public String getActivityStatus()
	{
		return activityStatus;
	}

	/**
     * Sets the activity status.
     * @param activityStatus the activity status of the storagetype to be set.
     * @see #getActivityStatus()
     */
	public void setActivityStatus(String activityStatus)
	{
		this.activityStatus = activityStatus;
	}
	public void setAllValues(AbstractActionForm abstractForm)
	{
		
	}

}
