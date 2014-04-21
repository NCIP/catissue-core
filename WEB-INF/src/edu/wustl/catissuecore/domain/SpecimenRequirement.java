
package edu.wustl.catissuecore.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import edu.wustl.common.util.global.Validator;

/**
 * SpecimenRequirement class.
 */
public class SpecimenRequirement extends AbstractSpecimen
{

	/**
	 * Serial Version ID.
	 */
	private static final long serialVersionUID = -34444448799655L;

	private Date collectionTimestamp;

	/**
	 * User who performs the event.
	 */
	private User collector;

	/**
	 * Text comment on event.
	 */
	private String collectionComments;

	private String collectionProcedure;

	/**
	 * Container type in which specimen is collected (e.g. clot tube, KEDTA, ACD, sterile specimen cup)
	 */
	private String collectionContainer;

	private String receivedQuality;

	private Date receivedTimestamp;
	/**
	 * User who performs the event.
	 */
	private User receiver;
	/**
	 * Text comment on event.
	 */
	private String receivedComments;

	private String storageType;

	/**
	 * collectionProtocolEvent.
	 */
	private CollectionProtocolEvent collectionProtocolEvent;

	/**
	 * specimenCollection.
	 */
	private Collection<Specimen> specimenCollection;

	/**
	 * labelFormat.
	 */
	private String labelFormat;

	private String specimenRequirementLabel;

	/**
	 * Defines the status of Specimen Requirement.
	 */
	private String activityStatus;

	public Date getCollectionTimestamp()
	{
		return collectionTimestamp;
	}

	public void setCollectionTimestamp(Date collectionTimestamp)
	{
		this.collectionTimestamp = collectionTimestamp;
	}

	public User getCollector()
	{
		return collector;
	}

	public void setCollector(User collector)
	{
		this.collector = collector;
	}

	public String getCollectionComments()
	{
		return collectionComments;
	}

	public void setCollectionComments(String collectionComments)
	{
		this.collectionComments = collectionComments;
	}
	
	public String getCollectionProcedure()
	{
		return collectionProcedure;
	}

	public void setCollectionProcedure(String collectionProcedure)
	{
		this.collectionProcedure = collectionProcedure;
	}

	public String getCollectionContainer()
	{
		return collectionContainer;
	}

	public void setCollectionContainer(String collectionContainer)
	{
		this.collectionContainer = collectionContainer;
	}

	public String getReceivedQuality()
	{
		return receivedQuality;
	}

	public void setReceivedQuality(String receivedQuality)
	{
		this.receivedQuality = receivedQuality;
	}

	public Date getReceivedTimestamp()
	{
		return receivedTimestamp;
	}

	public void setReceivedTimestamp(Date receivedTimestamp)
	{
		this.receivedTimestamp = receivedTimestamp;
	}

	public User getReceiver()
	{
		return receiver;
	}

	public void setReceiver(User receiver)
	{
		this.receiver = receiver;
	}

	public String getReceivedComments()
	{
		return receivedComments;
	}

	public void setReceivedComments(String receiverComments)
	{
		this.receivedComments = receiverComments;
	}

	public String getSpecimenRequirementLabel()
	{
		return specimenRequirementLabel;
	}

	public void setSpecimenRequirementLabel(String title)
	{
		this.specimenRequirementLabel = title;
	}

	/**
	 * Get CollectionProtocolEvent.
	 * @return CollectionProtocolEvent.
	 */
	public CollectionProtocolEvent getCollectionProtocolEvent()
	{
		return this.collectionProtocolEvent;
	}

	/**
	 * @param collectionProtocolEvent CollectionProtocolEvent.
	 */
	public void setCollectionProtocolEvent(CollectionProtocolEvent collectionProtocolEvent)
	{
		this.collectionProtocolEvent = collectionProtocolEvent;
	}

	/**
	 * Get StorageType.
	 * @return String.
	 */
	public String getStorageType()
	{
		return this.storageType;
	}

	/**
	 * Set StorageType.
	 * @param storageType String.
	 */
	public void setStorageType(String storageType)
	{
		this.storageType = storageType;
	}

	/**
	 * Get SpecimenCollection.
	 * @return Collection of Specimen type.
	 */
	public Collection<Specimen> getSpecimenCollection()
	{
		return this.specimenCollection;
	}

	/**
	 * Set SpecimenCollection.
	 * @param specimenCollection of Collection containing specimen.
	 */
	public void setSpecimenCollection(Collection<Specimen> specimenCollection)
	{
		this.specimenCollection = specimenCollection;
	}

	/**
	 * Get LabelFormat.
	 * @return String.
	 */
	public String getLabelFormat()
	{
		return this.labelFormat;
	}

	/**
	 * Set LabelFormat.
	 * @param labelFormat of String type.
	 */
	public void setLabelFormat(String labelFormat)
	{
		this.labelFormat = labelFormat;
	}

	/**
	 * Returns whether this specimen requirement is active or disabled.
	 * @hibernate.property name="activityStatus" type="string" column="ACTIVITY_STATUS" length="50"
	 * @see #setActivityStatus(String)
	 */
	@Override
	public String getActivityStatus()
	{
		return this.activityStatus;
	}

	/**
	 * Sets activity status of the specimen requirement.
	 * @param activityStatus "Active" if this specimen requirement is not deleted else disabled.
	 * @see #getActivityStatus()
	 */
	@Override
	public void setActivityStatus(String activityStatus)
	{
		this.activityStatus = activityStatus;
	}

	@Override
	public Collection<SpecimenEventParameters> getSpecimenEventCollection()
	{
		if(this.collector == null && this.receiver == null && Validator.isEmpty(this.collectionContainer) && Validator.isEmpty(this.collectionProcedure)
				&& Validator.isEmpty(this.receivedQuality))
		{
			return new ArrayList<SpecimenEventParameters>();
		}
		CollectionEventParameters collectionEventParam = new CollectionEventParameters();
		collectionEventParam.setComment(this.getCollectionComments());
		collectionEventParam.setSpecimen(this);
		collectionEventParam.setTimestamp(this.getCollectionTimestamp());
		collectionEventParam.setUser(this.getCollector());
		collectionEventParam.setContainer(this.getCollectionContainer());
		collectionEventParam.setCollectionProcedure(this.getCollectionProcedure());

		ReceivedEventParameters receivedEventParam = new ReceivedEventParameters();
		receivedEventParam.setComment(this.getReceivedComments());
		receivedEventParam.setReceivedQuality(this.getReceivedQuality());
		receivedEventParam.setSpecimen(this);
		receivedEventParam.setTimestamp(this.getReceivedTimestamp());
		receivedEventParam.setUser(this.getReceiver());
		List<SpecimenEventParameters> eventParameters = new ArrayList<SpecimenEventParameters>();
		eventParameters.add(receivedEventParam);
		eventParameters.add(collectionEventParam);
		return eventParameters;
	}

	/**
	 * Sets the collection of Specimen Event Parameters associated with this specimen.
	 * @param specimenEventCollection the collection of Specimen Event Parameters
	 * associated with this specimen.
	 * @see #getSpecimenEventCollection()
	 */
	@Override
	public void setSpecimenEventCollection(final Collection specimenEventCollection)
	{
		for (Object event : specimenEventCollection)
		{
			SpecimenEventParameters specimenEventParameters = (SpecimenEventParameters) event;
			if (specimenEventParameters instanceof CollectionEventParameters)
			{
				this.collectionComments = specimenEventParameters.getComment();
				this.collectionContainer = ((CollectionEventParameters) specimenEventParameters).getContainer();
				this.collectionProcedure = ((CollectionEventParameters) specimenEventParameters).getCollectionProcedure();
				this.collectionTimestamp = specimenEventParameters.getTimestamp();
				this.collector = specimenEventParameters.getUser();
			}
			else if (specimenEventParameters instanceof ReceivedEventParameters)
			{
				this.receivedQuality = ((ReceivedEventParameters) specimenEventParameters).getReceivedQuality();
				this.receivedTimestamp = specimenEventParameters.getTimestamp();
				this.receiver = specimenEventParameters.getUser();
				this.receivedComments = specimenEventParameters.getComment();
			}
		}
	}

}