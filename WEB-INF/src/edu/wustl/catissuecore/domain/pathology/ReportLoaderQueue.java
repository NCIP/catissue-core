package edu.wustl.catissuecore.domain.pathology;


import java.util.Collection;

import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.AssignDataException;


/**
 * Represents different logical sections of surgical pathology report.
 * @hibernate.class
 * table="CATISSUE_REPORT_QUEUE"
 */
public class ReportLoaderQueue extends AbstractDomainObject
{

	protected Long id;
	protected String reportText;
	protected Collection participantCollection;
	protected String status;
	protected SpecimenCollectionGroup specimenCollectionGroup;
	
	/**
	 * @return status information. 
     * @hibernate.property name="status"
     * type="string" column="STATUS" 
     * length="10"
     */
	public String getStatus()
	{
		return status;
	}

	
	/**
	 * Set the status of the queue record
	 * @param status status of the record of queue
	 */
	public void setStatus(String status)
	{
		this.status = status;
	}

	/**
	 * default Constructor
	 */
	public ReportLoaderQueue()
	{
		
	}
	
	/**
	 * Constructor with text as input
	 * @param text report text
	 * 
	 */
	public ReportLoaderQueue(String text)
	{
		//this.id=new Long(4);
		this.reportText=text;
	}
	
	
	/**
	 * @return system generated id
	 * @hibernate.id name="id" column="IDENTIFIER" type="long" length="30"
	 * unsaved-value="null" generator-class="native" 
	 * @hibernate.generator-param name="sequence" value="CATISSUE_REPORT_QUEUE_SEQ"
	 */
	public Long getId()
	{
		return id;
	}
	
	/**
	 * Set id of the object
	 * @param id of the object
	 */
	public void setId(Long id)
	{
		this.id = id;
	}
	
	/**
	 * Returns the collection of Studies for this Protocol.
	 * @hibernate.set name="participantCollection" table="CATISSUE_REPORT_PARTICIP_REL" 
	 * cascade="save-update" inverse="false" lazy="false"
	 * @hibernate.collection-key column="REPORT_ID"
	 * @hibernate.collection-many-to-many class="edu.wustl.catissuecore.domain.Participant" column="PARTICIPANT_ID"
	 * @return Returns the collection of Studies for this Protocol.
	 */
	public Collection getParticipantCollection()
	{
		return participantCollection;
	}
	
	/**
	 * @param collection
	 * Assign set of participants to current object
	 */
	public void setParticipantCollection(Collection collection)
	{
		this.participantCollection = collection;
	}
	/**
	 * @return reportText information. 
     * @hibernate.property name="reportText"
     * type="string" column="REPORT_TEXT" 
     * length="4000"
     */	
	public String getReportText()
	{
		return reportText;
	}
	
	/**
	 * Set report text
	 * @param reportText report text 
	 */
	public void setReportText(String reportText)
	{
		this.reportText = reportText;
	}

	public void setAllValues(AbstractActionForm abstractForm) throws AssignDataException
	{		
		
	}
	
	/**
	 * @return the specimenCollectionGroup
	 * @hibernate.many-to-one class="edu.wustl.catissuecore.domain.SpecimenCollectionGroup"  column="SPECIMEN_COLL_GRP_ID" cascade="save-update"
	 *
	 */
	public SpecimenCollectionGroup getSpecimenCollectionGroup()
	{
		return specimenCollectionGroup;
	}
	
	/**
	 * @param specimenCollectionGroup the specimenCollectionGroup to set
	 */
	public void setSpecimenCollectionGroup(SpecimenCollectionGroup specimenCollectionGroup)
	{
		this.specimenCollectionGroup = specimenCollectionGroup;
	}
}
