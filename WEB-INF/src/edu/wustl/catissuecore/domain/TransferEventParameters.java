/**
 * <p>Title: TransferEventParameters Class</p>
 * <p>Description: Attributes associated with moving specimen from one storage location to another. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Mandar Deshmukh
 * @version 1.00
 */

package edu.wustl.catissuecore.domain;

import edu.wustl.catissuecore.actionForm.TransferEventParametersForm;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.actionForm.IValueObject;
import edu.wustl.common.exception.AssignDataException;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.util.logger.Logger;

/**
 * Attributes associated with moving specimen from one storage location to another.
 * @hibernate.joined-subclass table="CATISSUE_TRANSFER_EVENT_PARAM"
 * @hibernate.joined-subclass-key column="IDENTIFIER"
 */
public class TransferEventParameters extends SpecimenEventParameters
		implements
			java.io.Serializable
{

	/**
	 * logger Logger - Generic logger.
	 */
	private static org.apache.log4j.Logger logger = Logger.getLogger(TransferEventParameters.class);
	/**
	 * Serial Version ID of the class.
	 */
	private static final long serialVersionUID = 1234567890L;

	/**
	 * Reference to dimensional position one of the specimen in previous storage container before transfer.
	 */
	protected Integer fromPositionDimensionOne;

	/**
	 * Reference to dimensional position two of the specimen in previous storage container before transfer.
	 */
	protected Integer fromPositionDimensionTwo;

	/**
	 * Reference to dimensional position one of the specimen in new storage container after transfer.
	 */
	protected Integer toPositionDimensionOne;

	/**
	 * Reference to dimensional position two of the specimen in new storage container after transfer.
	 */
	protected Integer toPositionDimensionTwo;

	/**
	 * Storage Container to which the transfer is made.
	 */
	protected StorageContainer toStorageContainer;

	/**
	 * Storage Container from which the transfer is made.
	 */
	protected StorageContainer fromStorageContainer;

	/**
	 * Returns the Reference to dimensional position one of the specimen in previous
	 * storage container before transfer.
	 * @hibernate.property name="fromPositionDimensionOne" type="int"
	 * column="FROM_POSITION_DIMENSION_ONE" length="30"
	 * @return fromPositionDimensionOne.
	 */
	public Integer getFromPositionDimensionOne()
	{
		return this.fromPositionDimensionOne;
	}

	/**
	 * Sets the fromPositionDimensionOne.
	 * @param fromPositionDimensionOne
	 * Reference to dimensional position one of the specimen in previous storage container before transfer.
	 */
	public void setFromPositionDimensionOne(Integer fromPositionDimensionOne)
	{
		this.fromPositionDimensionOne = fromPositionDimensionOne;
	}

	/**
	 * Returns the Reference to dimensional position two of the specimen in previous
	 * storage container before transfer.
	 * @hibernate.property name="fromPositionDimensionTwo" type="int"
	 * column="FROM_POSITION_DIMENSION_TWO" length="30"
	 * @return fromPositionDimensionTwo.
	 */
	public Integer getFromPositionDimensionTwo()
	{
		return this.fromPositionDimensionTwo;
	}

	/**
	 * Sets the fromPositionDimensionTwo.
	 * @param fromPositionDimensionTwo
	 * Reference to dimensional position two of the specimen in previous storage container before transfer.
	 */
	public void setFromPositionDimensionTwo(Integer fromPositionDimensionTwo)
	{
		this.fromPositionDimensionTwo = fromPositionDimensionTwo;
	}

	/**
	 * Returns the Reference to dimensional position one of the specimen in new storage
	 * container after transfer.
	 * @hibernate.property name="toPositionDimensionOne" type="int"
	 * column="TO_POSITION_DIMENSION_ONE" length="30"
	 * @return toPositionDimensionOne.
	 */
	public Integer getToPositionDimensionOne()
	{
		return this.toPositionDimensionOne;
	}

	/**
	 * Sets the toPositionDimensionOne.
	 * @param toPositionDimensionOne
	 * Reference to dimensional position one of the specimen in new storage container after transfer.
	 */
	public void setToPositionDimensionOne(Integer toPositionDimensionOne)
	{
		this.toPositionDimensionOne = toPositionDimensionOne;
	}

	/**
	 * Returns the Reference to dimensional position two of the specimen in new
	 * storage container after transfer.
	 * @hibernate.property name="toPositionDimensionTwo" type="int"
	 * column="TO_POSITION_DIMENSION_TWO" length="30"
	 * @return toPositionDimensionTwo.
	 */
	public Integer getToPositionDimensionTwo()
	{
		return this.toPositionDimensionTwo;
	}

	/**
	 * Sets the toPositionDimensionTwo.
	 * @param toPositionDimensionTwo
	 * Reference to dimensional position two of the specimen in new storage container after transfer.
	 */
	public void setToPositionDimensionTwo(Integer toPositionDimensionTwo)
	{
		this.toPositionDimensionTwo = toPositionDimensionTwo;
	}

	/**
	 * Returns the new StorageContainer.
	 * @hibernate.many-to-one column="TO_STORAGE_CONTAINER_ID"
	 * class="edu.wustl.catissuecore.domain.StorageContainer"
	 * constrained="true"
	 * @return the new StorageContainer.
	 */
	public StorageContainer getToStorageContainer()
	{
		return this.toStorageContainer;
	}

	/**
	 * @param toStorageContainer
	 *            The toStorageContainer to set.
	 */
	public void setToStorageContainer(StorageContainer toStorageContainer)
	{
		this.toStorageContainer = toStorageContainer;
	}

	/**
	 * Returns the old StorageContainer.
	 * @hibernate.many-to-one column="FROM_STORAGE_CONTAINER_ID"
	 * class="edu.wustl.catissuecore.domain.StorageContainer"
	 * constrained="true"
	 * @return the old StorageContainer.
	 */
	public edu.wustl.catissuecore.domain.StorageContainer getFromStorageContainer()
	{

		return this.fromStorageContainer;
	}

	/**
	 * @param fromStorageContainer
	 *            The fromStorageContainer to set.
	 */
	public void setFromStorageContainer(
			edu.wustl.catissuecore.domain.StorageContainer fromStorageContainer)
	{
		this.fromStorageContainer = fromStorageContainer;
	}

	/**
	 * NOTE: Do not delete this constructor. Hibernet uses this by reflection API.
	 * */
	public TransferEventParameters()
	{
		super();
	}

	/**
	 * Parameterised constructor.
	 * @param abstractForm of AbstractActionForm type.
	 * @throws AssignDataException : AssignDataException
	 */
	public TransferEventParameters(AbstractActionForm abstractForm) throws AssignDataException
	{
		super();
		this.setAllValues(abstractForm);
	}

	/**
	 * This function Copies the data from an TransferEventParametersForm object to a
	 * TransferEventParameters object.
	 * @param abstractForm - TransferEventParametersForm An TransferEventParametersForm object
	 * containing the information about the TransferEventParameters.
	 * @throws AssignDataException : AssignDataException
	 * */
	@Override
	public void setAllValues(IValueObject abstractForm) throws AssignDataException
	{
		try
		{
			final TransferEventParametersForm form = (TransferEventParametersForm) abstractForm;
			final StorageContainer toObj = new StorageContainer();
			if (form.getStContSelection() == 1)
			{
				this.toPositionDimensionOne = Integer.valueOf(form.getPositionDimensionOne());
				this.toPositionDimensionTwo = Integer.valueOf(form.getPositionDimensionTwo());
				toObj.setId(Long.valueOf(form.getStorageContainer()));
			}
			else
			{
				if (form.getPos1() != null
						&& !form.getPos1().trim().equals(Constants.DOUBLE_QUOTES)
						&& form.getPos2() != null
						&& !form.getPos2().trim().equals(Constants.DOUBLE_QUOTES))
				{
					this.toPositionDimensionOne = Integer.valueOf(form.getPos1());
					this.toPositionDimensionTwo = Integer.valueOf(form.getPos2());
				}
				toObj.setName(form.getSelectedContainerName());
			}

			this.toStorageContainer = toObj;

			if (form.getFromStorageContainerId() == 0)
			{
				this.fromStorageContainer = null;
				this.fromPositionDimensionOne = null;
				this.fromPositionDimensionTwo = null;
			}
			else
			{
				final StorageContainer fromObj = new StorageContainer();
				fromObj.setId(Long.valueOf(form.getFromStorageContainerId()));
				this.fromStorageContainer = fromObj;

				this.fromPositionDimensionOne = Integer.valueOf(form.getFromPositionDimensionOne());
				this.fromPositionDimensionTwo = Integer.valueOf(form.getFromPositionDimensionTwo());
			}
			super.setAllValues(form);
		}
		catch (final Exception excp)
		{
			logger.error(excp.getMessage());
			final ErrorKey errorKey = ErrorKey.getErrorKey("assign.data.error");
			throw new AssignDataException(errorKey, null, "TransferEventParameters.java :");
		}
	}
}