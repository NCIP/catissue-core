/**
 * <p>Title: CollectionEventParameters Class</p>
 * <p>Description: Attributes associated with the collection event of a specimen from participant. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Mandar Deshmukh
 * @version 1.00
 */

package edu.wustl.catissuecore.domain;

import edu.wustl.catissuecore.actionForm.CollectionEventParametersForm;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.actionForm.IValueObject;
import edu.wustl.common.exception.AssignDataException;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;

/**
 * Attributes associated with the collection event of a specimen from participant.
 * @hibernate.joined-subclass table="CATISSUE_COLL_EVENT_PARAM"
 * @hibernate.joined-subclass-key column="IDENTIFIER"
 */
public class CollectionEventParameters extends SpecimenEventParameters
		implements
			java.io.Serializable
{

	/**
	 * logger Logger - Generic logger.
	 */
	private static org.apache.log4j.Logger logger = Logger
			.getLogger(CollectionEventParameters.class);

	/**
	 * Serial Version Id of the class.
	 */
	private static final long serialVersionUID = 1234567890L;

	/**
	 * Method of specimen collection from participant (e.g. needle biopsy, central
	 * venous line, bone marrow aspiration)
	 */
	protected String collectionProcedure;

	/**
	 * Container type in which specimen is collected (e.g. clot tube, KEDTA, ACD, sterile specimen cup)
	 */
	protected String container;

	/**
	 * Returns the procedure of collection.
	 * @hibernate.property name="collectionProcedure" type="string" column="COLLECTION_PROCEDURE" length="50"
	 * @return procedure of collection.
	 */
	public String getCollectionProcedure()
	{
		return this.collectionProcedure;
	}

	/**
	 * Sets the procedure.
	 * @param collectionProcedure procedure of collection.
	 */
	public void setCollectionProcedure(String collectionProcedure)
	{
		this.collectionProcedure = collectionProcedure;
	}

	/**
	 * Returns the container type used for collecting the specimen.
	 * @hibernate.property name="container" type="string" column="CONTAINER" length="50"
	 * @return container type used for collecting the specimen.
	 */
	public String getContainer()
	{
		return this.container;
	}

	/**
	 * Sets the container.
	 * @param container container type used for collecting the specimen.
	 */
	public void setContainer(String container)
	{
		this.container = container;
	}

	/**
	 * NOTE: Do not delete this constructor. Hibernet uses this by reflection API.
	 */
	public CollectionEventParameters()
	{
		super();
	}

	/**
	 * Parameterized constructor.
	 * @param abstractForm AbstractActionForm.
	 * @throws AssignDataException : AssignDataException
	 */
	public CollectionEventParameters(AbstractActionForm abstractForm) throws AssignDataException
	{
		super();
		this.setAllValues(abstractForm);
	}

	/**
	 * This function Copies the data from an CollectionEventParameters object.
	 * @param abstractForm CollectionEventParametersForm An CollectionEventParametersForm
	 * object containing the information about the CollectionEventParameters.
	 * @throws AssignDataException : AssignDataException
	 */
	@Override
	public void setAllValues(IValueObject abstractForm) throws AssignDataException
	{
		try
		{
			final CollectionEventParametersForm form = (CollectionEventParametersForm) abstractForm;
			final Validator validator = new Validator();

			if (validator.isValidOption(form.getContainer()))
			{
				this.container = form.getContainer();
			}
			else
			{
				this.container = null; //Purposefully set null for Edit Mode
			}

			this.collectionProcedure = form.getCollectionProcedure();

			super.setAllValues(form);
		}
		catch (final Exception excp)
		{
			logger.error(excp.getMessage());
			final ErrorKey errorKey = ErrorKey.getErrorKey("assign.data.error");
			throw new AssignDataException(errorKey, null, "CollectionEventParameters.java :");
		}
	}

	/**
	 * Parameterized Constructor.
	 * @param obj of type CollectionEventParameters.
	 */
	public CollectionEventParameters(CollectionEventParameters obj)
	{
		super();
		this.collectionProcedure = obj.collectionProcedure;
		this.container = obj.container;
		this.comment = obj.comment;
	}
}