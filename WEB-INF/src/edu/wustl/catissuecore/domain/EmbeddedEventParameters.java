/**
 * <p>Title: EmbeddedEventParameters Class>
 * <p>Description:  An abbreviated set of written procedures that describe how a previously collected 
 * specimen will be utilized.  Note that specimen may be collected with one collection protocol and then 
 * later utilized by multiple different studies (Distribution protocol). </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00
 * Created on Apr 7, 2005
 */
package edu.wustl.catissuecore.domain;

/**
 * An abbreviated set of written procedures that describe how a previously collected specimen will be 
 * utilized.  Note that specimen may be collected with one collection protocol and then later utilized 
 * by multiple different studies (Distribution protocol).
 * @hibernate.joined-subclass table="CATISSUE_EMBEDDED_EVENT_PARAMETERS"
 * @hibernate.joined-subclass-key column="IDENTIFIER" 
 * @author Aniruddha Phadnis
 */
public class EmbeddedEventParameters extends SpecimenEventParameters
		implements java.io.Serializable
{
	private static final long serialVersionUID = 1234567890L;

	/**
     * Type of medium in which specimen is embedded.
     */
	protected String embeddingMedium;

	/**
     * Returns the type of medium in which specimen is embedded.
     * @return The type of medium in which specimen is embedded.
     * @see #setEmbeddingMedium(String)
     * @hibernate.property name="embeddingMedium" type="string" 
     * column="EMBEDDING_MEDIUM" length="50"
     */
	public String getEmbeddingMedium()
	{
		return embeddingMedium;
	}

	/**
     * Sets the type of medium in which specimen is embedded.
     * @param embeddingMedium the type of medium in which specimen is embedded.
     * @see #getEmbeddingMedium()
     */
	public void setEmbeddingMedium(String embeddingMedium)
	{
		this.embeddingMedium = embeddingMedium;
	}
}