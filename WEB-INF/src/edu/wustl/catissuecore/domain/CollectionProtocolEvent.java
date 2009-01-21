/**
 * <p>Title: CollectionProtocolEvent Class</p>
 * <p>Description: A required specimen collection event associated with a Collection Protocol. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Mandar Deshmukh
 * @version 1.00
 * Created on July 12, 2005
 */

package edu.wustl.catissuecore.domain;

import java.util.Collection;
import java.util.LinkedHashSet;
import edu.wustl.common.actionForm.IValueObject;
import edu.wustl.common.exception.AssignDataException;
import edu.wustl.common.exception.BizLogicException;

/**
 * A required specimen collection event associated with a Collection Protocol.
 * @hibernate.class table="CATISSUE_COLL_PROT_EVENT"
 * @author Mandar Deshmukh
 */
public class CollectionProtocolEvent extends AbstractSpecimenCollectionGroup
	implements java.io.Serializable, Comparable
{
	/**
	 * Serial Version id of the class.
	 */
	private static final long serialVersionUID = 1234567890L;

	/**
	 * System generated unique id.
	 */
	protected Long id;

	/**
	 * Patch Id : FutureSCG_13
	 * Description : collectionPointLabel attribute added
	 */
	/**
	 * Defines the required collectionPointLabel.
	 */
	protected String collectionPointLabel;

	/**
	 * Defines the relative time point in days, with respect to the registration date
	 * of participant on this protocol, when the specimen should be collected from
	 * participant.
	 */
	protected Double studyCalendarEventPoint;


	/**
	 * CollectionProtocol associated with the CollectionProtocolEvent.
	 */
	protected CollectionProtocol collectionProtocol;

	/**
	 * specimenRequirementCollection.
	 */
	protected Collection<SpecimenRequirement> specimenRequirementCollection  =
		new LinkedHashSet<SpecimenRequirement>();

	/**
	 * specimenCollectionGroupCollection.
	 */
	protected Collection specimenCollectionGroupCollection;

	/**
	 * For SCG labeling,this will be exposed through API and not in the model.
	 */
	private String labelFormat;

	/**
	 * Returns the id.
	 * @hibernate.id name="id" column="IDENTIFIER" type="long" length="30"
	 * unsaved-value="null" generator-class="native"
	 * @hibernate.generator-param name="sequence" value="CATISSUE_COLL_PROT_EVENT_SEQ"
	 * @return Returns the id.
	 */
	public Long getId()
	{
		return id;
	}

	/**
	 * @param identifier The id to set.
	 */
	public void setId(Long identifier)
	{
		this.id = identifier;
	}

	/**
	 * Returns the studyCalendarEventPoint.
	 * @hibernate.property name="studyCalendarEventPoint" type="double"
	 * column="STUDY_CALENDAR_EVENT_POINT" length="50"
	 * @return Returns the studyCalendarEventPoint.
	 */
	public Double getStudyCalendarEventPoint()
	{
		return studyCalendarEventPoint;
	}

	/**
	 * @param studyCalendarEventPoint The studyCalendarEventPoint to set.
	 */
	public void setStudyCalendarEventPoint(Double studyCalendarEventPoint)
	{
		this.studyCalendarEventPoint = studyCalendarEventPoint;
	}

	/**
	 * Returns the collectionProtocol.
	 * @hibernate.many-to-one column="COLLECTION_PROTOCOL_ID" class="edu.wustl.
	 * catissuecore.domain.CollectionProtocol"
	 * constrained="true"
	 * @return Returns the collectionProtocol.
	 */
	public CollectionProtocol getCollectionProtocol()
	{
		return collectionProtocol;
	}

	/**
	 * @param collectionProtocol
	 *  The collectionProtocol to set.
	 */
	public void setCollectionProtocol(CollectionProtocol collectionProtocol)
	{
		this.collectionProtocol = collectionProtocol;
	}

	/**
	 * Convert to String method.
	 * @return String type.
	 */
	public String toString()
	{
		return "CPE: "+clinicalStatus+" | "+ studyCalendarEventPoint +" | "+ this.getId();
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.domain.AbstractDomainObject#setAllValues
	 * (edu.wustl.catissuecore.actionForm.AbstractActionForm)
	 */
	/**
	 * Set all values.
	 * @param abstractForm of IValueObject type.
	 * @throws AssignDataException when some problem in assigning the data.
	 */
	public void setAllValues(IValueObject abstractForm) throws AssignDataException
	{
		//
	}

	/**
	 * Compare objects.
	 * @param obj of Object type.
	 * @return int type.
	 */
	public int compareTo(Object obj)
	{
		int returnValue = 0;
		if(obj instanceof CollectionProtocolEvent)
		{
			CollectionProtocolEvent collectionProtocolEvent = (CollectionProtocolEvent)obj;
			if(studyCalendarEventPoint.doubleValue() <
					collectionProtocolEvent.getStudyCalendarEventPoint().doubleValue())
			{
				returnValue = -1;
			}
			else if(studyCalendarEventPoint.doubleValue() >
			collectionProtocolEvent.getStudyCalendarEventPoint().doubleValue())
			{
				returnValue = 1;
			}
			else
			{
				returnValue = 0;
			}
		}
		return returnValue;
	}
	/**
	 * Patch Id : FutureSCG_14
	 * Description : collectionPointLabel attribute added
	 */
	/**
	 * Returns the collectionPointLabel.
	 * @hibernate.property name="collectionPointLabel" type="string"
	 * column="COLLECTION_POINT_LABEL" length="255"
	 * @return Returns the collectionPointLabel of the participant.
	 * @see #setCollectionPointLabel(String)
	 */
	public String getCollectionPointLabel()
	{
		return collectionPointLabel;
	}
	/**
	 * Sets collectionPointLabel.
	 * @param collectionPointLabel of String type.
	 * @see #getCollectionPointLabel()
	 */
	public void setCollectionPointLabel(String collectionPointLabel)
	{
		this.collectionPointLabel = collectionPointLabel;
	}

	/**
	 * Get SpecimenCollectionGroup Collection.
	 * @return Collection of SpecimenCollectionGroup.
	 */
	public Collection getSpecimenCollectionGroupCollection()
	{
		return specimenCollectionGroupCollection;
	}

	/**
	 * Set the SpecimenCollectionGroup Collection.
	 * @param specimenCollectionGroupCollection of Collectio type.
	 */
	public void setSpecimenCollectionGroupCollection(Collection specimenCollectionGroupCollection)
	{
		this.specimenCollectionGroupCollection = specimenCollectionGroupCollection;
	}

	/**
	 * Get SpecimenRequirement Collection.
	 * @return Collection of SpecimenRequirement objects.
	 */
	public Collection<SpecimenRequirement> getSpecimenRequirementCollection()
	{
		return specimenRequirementCollection;
	}

	/**
	 * Set the SpecimenRequirement Collection.
	 * @param requirementSpecimenCollection which is Collection of
	 * SpecimenRequirement objects.
	 */
	public void setSpecimenRequirementCollection(Collection<SpecimenRequirement> requirementSpecimenCollection)
	{
		this.specimenRequirementCollection = requirementSpecimenCollection;
	}

	/**
	 * Get the CollectionProtocolRegistration.
	 * @return CollectionProtocolRegistration type.
	 */
	@Override
	public CollectionProtocolRegistration getCollectionProtocolRegistration()
	{
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Get the Group Name.
	 * @return String type.
	 */
	@Override
	public String getGroupName()
	{
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Set the Group Name.
	 * @param name of String type.
	 * @throws BizLogicException if some problem is setting the group name.
	 */
	@Override
	protected void setGroupName(String name) throws BizLogicException
	{
		//
	}

	/**
	 * Get tha label format.
	 * @return String type.
	 */
	public String getLabelFormat()
	{
		return labelFormat;
	}

	/**
	 * Set the Label format.
	 * @param labelFormat of String type.
	 */
	public void setLabelFormat(String labelFormat)
	{
		this.labelFormat = labelFormat;
	}
}