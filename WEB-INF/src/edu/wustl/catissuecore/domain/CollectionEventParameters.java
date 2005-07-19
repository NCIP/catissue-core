/**
 * <p>Title: CollectionEventParameters Class</p>
 * <p>Description: Attributes associated with the collection event of a specimen from participant. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Mandar Deshmukh
 * @version 1.00
 */

package edu.wustl.catissuecore.domain;

/**
 * Attributes associated with the collection event of a specimen from participant.
 * @hibernate.joined-subclass table="CATISSUE_COLLECTION_EVENT_PARAMETERS"
 * @hibernate.joined-subclass-key column="IDENTIFIER" 
 */
public class CollectionEventParameters extends SpecimenEventParameters
		implements java.io.Serializable
{
	private static final long serialVersionUID = 1234567890L;

	/**	
	 *	Method of specimen collection from participant (e.g. needle biopsy, central venous line, bone marrow aspiration)
	 */
	protected String procedure;
	
	/**
	 * Container type in which specimen is collected (e.g. clot tube, KEDTA, ACD, sterile specimen cup)
	 */
	protected String container;

	/**
	 * Returns the procedure of collection.
	 * @hibernate.property name="procedure" type="string" column="PROCEDURE" length="50"
	 * @return procedure of collection.
	 */
	public String getProcedure()
	{
		return procedure;
	}

	/**
	 * Sets the procedure. 
	 * @param procedure procedure of collection.
	 */
	public void setProcedure(String procedure)
	{
		this.procedure = procedure;
	}

	/**
	 * Returns the container type used for collecting the specimen.
	 * @hibernate.property name="container" type="string" column="CONTAINER" length="50"
	 * @return container type used for collecting the specimen. 
	 */
	public String getContainer()
	{
		return container;
	}

	/**
	 * Sets the container 
	 * @param container container type used for collecting the specimen.
	 */
	public void setContainer(String container)
	{
		this.container = container;
	}
}