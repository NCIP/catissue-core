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

import edu.wustl.catissuecore.actionForm.EmbeddedEventParametersForm;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.actionForm.IValueObject;
import edu.wustl.common.exception.AssignDataException;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.util.logger.Logger;

/**
 * An abbreviated set of written procedures that describe how a previously collected specimen will be
 * utilized.  Note that specimen may be collected with one collection protocol and then later utilized
 * by multiple different studies (Distribution protocol).
 * @hibernate.joined-subclass table="CATISSUE_EMBEDDED_EVENT_PARAM"
 * @hibernate.joined-subclass-key column="IDENTIFIER"
 * @author Aniruddha Phadnis
 */
public class EmbeddedEventParameters extends SpecimenEventParameters
		implements
			java.io.Serializable
{

	/**
	 * logger Logger - Generic logger.
	 */
	private static org.apache.log4j.Logger logger = Logger.getLogger(EmbeddedEventParameters.class);

	/**
	 * Serial Version ID.
	 */
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
		return this.embeddingMedium;
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

	/**
	 * Default Constructor.
	 * NOTE: Do not delete this constructor. Hibernate uses this by reflection API.
	 */
	public EmbeddedEventParameters()
	{
		super();
	}

	/**
	 *  Parameterized constructor.
	 * @param abstractForm AbstractActionForm.
	 * @throws AssignDataException : AssignDataException
	 */
	public EmbeddedEventParameters(AbstractActionForm abstractForm) throws AssignDataException
	{
		super();
		this.setAllValues(abstractForm);
	}

	/**
	 * This function Copies the data from an EmbeddedEventParametersForm object to
	 * a EmbeddedEventParameters object.
	 * @param abstractForm An EmbeddedEventParametersForm object
	 * containing the information about the EmbeddedEventParameters.
	 * @throws AssignDataException : AssignDataException
	 */
	@Override
	public void setAllValues(IValueObject abstractForm) throws AssignDataException
	{
		try
		{
			final EmbeddedEventParametersForm form = (EmbeddedEventParametersForm) abstractForm;
			this.embeddingMedium = form.getEmbeddingMedium();
			super.setAllValues(form);
		}
		catch (final Exception excp)
		{
			logger.error(excp.getMessage());
			final ErrorKey errorKey = ErrorKey.getErrorKey("assign.data.error");
			throw new AssignDataException(errorKey, null, "EmbeddedEventParameters.java :");
		
		}
	}
}