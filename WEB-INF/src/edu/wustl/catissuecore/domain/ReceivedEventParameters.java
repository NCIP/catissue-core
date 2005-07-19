/**
 * <p>Title: ReceivedEventParameters Class</p>
 * <p>Description: Attributes associated with the received event of a specimen. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Mandar Deshmukh
 * @version 1.00
 */

package edu.wustl.catissuecore.domain;

/**
 * Attributes associated with the received event of a specimen.
 * @hibernate.joined-subclass table="CATISSUE_RECEIVED_EVENT_PARAMETERS"
 * @hibernate.joined-subclass-key column="IDENTIFIER" 
 */
public class ReceivedEventParameters extends SpecimenEventParameters
		implements java.io.Serializable
{
	private static final long serialVersionUID = 1234567890L;

	/**
	 * Grossly evaluated quality of the received specimen.
	 */
	protected String receivedQuality;

	/**
	 * Returns the receivedQuality of the specimen.
	 * @hibernate.property name="receivedQuality" type="string" column="RECEIVED_QUALITY" length=50"
	 * @return receivedQuality of the specimen.
	 */
	public String getReceivedQuality()
	{
		return receivedQuality;
	}

	/**
	 * Sets the receivedQuality of the SPECIMEN.
	 * @param receivedQuality receivedQuality of the SPECIMEN.
	 */
	public void setReceivedQuality(String receivedQuality)
	{
		this.receivedQuality = receivedQuality;
	}
}