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
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.util.logger.Logger;

/**
 * Attributes associated with moving specimen from one storage location to another.
 * @hibernate.joined-subclass table="CATISSUE_TRANSFER_EVENT_PARAM"
 * @hibernate.joined-subclass-key column="IDENTIFIER" 
 */
public class TransferEventParameters extends SpecimenEventParameters implements java.io.Serializable
{

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
	 * Returns the Reference to dimensional position one of the specimen in previous storage container before transfer.
	 * @hibernate.property name="fromPositionDimensionOne" type="int" column="FROM_POSITION_DIMENSION_ONE" length="30"
	 * @return fromPositionDimensionOne.
	 */
	public Integer getFromPositionDimensionOne()
	{
		return fromPositionDimensionOne;
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
	 * Returns the Reference to dimensional position two of the specimen in previous storage container before transfer.
	 * @hibernate.property name="fromPositionDimensionTwo" type="int" column="FROM_POSITION_DIMENSION_TWO" length="30"
	 * @return fromPositionDimensionTwo.
	 */
	public Integer getFromPositionDimensionTwo()
	{
		return fromPositionDimensionTwo;
	}

	/**
	 * Sets the fromPositionDimensionTwo. 
	 * 
	 * @param fromPositionDimensionTwo
	 * Reference to dimensional position two of the specimen in previous storage container before transfer.
	 */
	public void setFromPositionDimensionTwo(Integer fromPositionDimensionTwo)
	{
		this.fromPositionDimensionTwo = fromPositionDimensionTwo;
	}

	/**
	 * Returns the Reference to dimensional position one of the specimen in new storage container after transfer.
	 * @hibernate.property name="toPositionDimensionOne" type="int" column="TO_POSITION_DIMENSION_ONE" length="30"
	 * @return toPositionDimensionOne.
	 */
	public Integer getToPositionDimensionOne()
	{
		return toPositionDimensionOne;
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
	 * Returns the Reference to dimensional position two of the specimen in new storage container after transfer.
	 * @hibernate.property name="toPositionDimensionTwo" type="int" column="TO_POSITION_DIMENSION_TWO" length="30"
	 * @return toPositionDimensionTwo.
	 */
	public Integer getToPositionDimensionTwo()
	{
		return toPositionDimensionTwo;
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
		return toStorageContainer;
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

		return fromStorageContainer;
	}

	/**
	 * @param fromStorageContainer
	 *            The fromStorageContainer to set.
	 */
	public void setFromStorageContainer(edu.wustl.catissuecore.domain.StorageContainer fromStorageContainer)
	{
		this.fromStorageContainer = fromStorageContainer;
	}

	/**
	 * NOTE: Do not delete this constructor. Hibernet uses this by reflection API.
	 * */
	public TransferEventParameters()
	{

	}

	/**
	 *  Parameterised constructor 
	 * @param abstractForm
	 */
	public TransferEventParameters(AbstractActionForm abstractForm)
	{
		setAllValues(abstractForm);
	}

	/**
	 * This function Copies the data from an TransferEventParametersForm object to a TransferEventParameters object.
	 * @param TransferEventParametersForm An TransferEventParametersForm object containing the information about the TransferEventParameters.  
	 * */
	public void setAllValues(AbstractActionForm abstractForm)
	{
		try
		{
			TransferEventParametersForm form = (TransferEventParametersForm) abstractForm;
			StorageContainer toObj = new StorageContainer();
			if (form.getStContSelection() == 1)
			{
				this.toPositionDimensionOne = new Integer(form.getPositionDimensionOne());
				this.toPositionDimensionTwo = new Integer(form.getPositionDimensionTwo());
				toObj.setId(new Long(form.getStorageContainer()));
			}
			else
			{
				if (form.getPos1() != null && !form.getPos1().trim().equals("")
						&& form.getPos2() != null && !form.getPos2().trim().equals(""))
				{
				this.toPositionDimensionOne = new Integer(form.getPos1());
				this.toPositionDimensionTwo = new Integer(form.getPos2());
				}
				toObj.setName(form.getSelectedContainerName());
			}

			this.toStorageContainer = toObj;

			if (form.getFromStorageContainerId() != 0)
			{
				StorageContainer fromObj = new StorageContainer();
				fromObj.setId(new Long(form.getFromStorageContainerId()));
				this.fromStorageContainer = fromObj;

				this.fromPositionDimensionOne = new Integer(form.getFromPositionDimensionOne());
				this.fromPositionDimensionTwo = new Integer(form.getFromPositionDimensionTwo());
			}
			else
			{
				this.fromStorageContainer = null;
				this.fromPositionDimensionOne = null;
				this.fromPositionDimensionTwo = null;
			}

			super.setAllValues(form);
		}
		catch (Exception excp)
		{
			Logger.out.error(excp.getMessage());
		}
	}

}