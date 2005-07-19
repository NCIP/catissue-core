/**
 * <p>Title: DisposalEventParameters Class</p>
 * <p>Description: Attributes related to disposal event of a specimen. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Mandar Deshmukh
 * @version 1.00
 */

package edu.wustl.catissuecore.domain;

/**
 * Attributes related to disposal event of a specimen.
 * @hibernate.joined-subclass table="CATISSUE_DISPOSAL_EVENT_PARAMETERS"
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
}