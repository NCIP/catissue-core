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

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.actionForm.IValueObject;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.dao.AbstractDAO;
import edu.wustl.common.dao.DAO;
import edu.wustl.common.dao.DAOFactory;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.AssignDataException;
import edu.wustl.common.util.dbManager.DAOException;

/**
 * A required specimen collection event associated with a Collection Protocol.
 * 
 * @hibernate.class table="CATISSUE_COLL_PROT_EVENT"
 * @author Mandar Deshmukh
 */
public class CollectionProtocolEvent extends AbstractDomainObject implements java.io.Serializable, Comparable
{
	private static final long serialVersionUID = 1234567890L;

	/**
	 * System generated unique id.
	 */
	protected Long id;

	/**
	 * Defines the required clinical status of the participant at the time of specimen collection. e.g. Pre-Op, Post-op, Pre-RX etc.
	 */
	protected String clinicalStatus;

	/**
	 * Patch Id : FutureSCG_13
	 * Description : collectionPointLabel attribute added
	 */
	/**
	 * Defines the required collectionPointLabel.
	 */
	protected String collectionPointLabel;

	/**
	 * Defines the relative time point in days, with respect to the registration date of participant on this protocol, when the specimen should be collected from participant.
	 */
	protected Double studyCalendarEventPoint;


	/**
	 * CollectionProtocol associated with the CollectionProtocolEvent.
	 */
	protected CollectionProtocol collectionProtocol;
	protected SpecimenCollectionRequirementGroup requiredCollectionSpecimenGroup;
	
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
	 * @param id The id to set.
	 */
	public void setId(Long id)
	{
		this.id = id;
	}

	/**	
	 * @return specimen collection requirement group.
	 * @hibernate.many-to-one name="requiredCollectionSpecimenGroup"
	 * class="edu.wustl.catissuecore.domain.SpecimenCollectionRequirementGroup"
	 * column="SPECIMEN_COLLECTION_REQ_GROUP_ID" not-null="false"
	 */

	public SpecimenCollectionRequirementGroup getRequiredCollectionSpecimenGroup() {
		return requiredCollectionSpecimenGroup;
	}

	public void setRequiredCollectionSpecimenGroup(
			SpecimenCollectionRequirementGroup requiredCollectionSpecimenGroup) {
		this.requiredCollectionSpecimenGroup = requiredCollectionSpecimenGroup;
	}

	/**
	 * Returns the clinicalStatus.
	 * @hibernate.property name="clinicalStatus" type="string"
	 * column="CLINICAL_STATUS" length="50"
	 * @return Returns the clinicalStatus.
	 */
	public String getClinicalStatus()
	{
		return clinicalStatus;
	}

	/**
	 * @param clinicalStatus The clinicalStatus to set.
	 */
	public void setClinicalStatus(String clinicalStatus)
	{
		this.clinicalStatus = clinicalStatus;
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
	 * @hibernate.many-to-one column="COLLECTION_PROTOCOL_ID" class="edu.wustl.catissuecore.domain.CollectionProtocol"
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

	public String toString()
	{
		return "CPE: "+clinicalStatus+" | "+ studyCalendarEventPoint +" | "+ this.getId();
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.domain.AbstractDomainObject#setAllValues(edu.wustl.catissuecore.actionForm.AbstractActionForm)
	 */
	public void setAllValues(IValueObject abstractForm) throws AssignDataException
	{

	}

	public int compareTo(Object obj)
	{
		if(obj instanceof CollectionProtocolEvent)
		{
			CollectionProtocolEvent collectionProtocolEvent = (CollectionProtocolEvent)obj;
			if(studyCalendarEventPoint.doubleValue()< collectionProtocolEvent.getStudyCalendarEventPoint().doubleValue())
				return -1;
			else if(studyCalendarEventPoint.doubleValue() > collectionProtocolEvent.getStudyCalendarEventPoint().doubleValue())
				return 1;
			else
				return 0;
		}
		return 0;
	}
	/**
	 * Patch Id : FutureSCG_14
	 * Description : collectionPointLabel attribute added
	 */
	/**
	 * Returns the collectionPointLabel
	 * @hibernate.property name="collectionPointLabel" type="string"
	 * column="COLLECTION_POINT_LABEL" length="255"
	 * @return Returns the collectionPointLabel of the participant.
	 * @see #setCollectionPointLabel(String)
	 */
	public String getCollectionPointLabel() {
		return collectionPointLabel;
	}
	/**
	 * Sets collectionPointLabel
	 * @param collectionPointLabel
	 * @see #getCollectionPointLabel()
	 */
	public void setCollectionPointLabel(String collectionPointLabel) 
	{
		this.collectionPointLabel = collectionPointLabel;
	}
}