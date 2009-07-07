
package edu.wustl.catissuecore.domain.pathology;

import java.util.Collection;
import java.util.Date;

import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.common.actionForm.IValueObject;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.AssignDataException;

/**
 * Represents different logical sections of surgical pathology report.
 * @hibernate.class table="CATISSUE_REPORT_QUEUE"
 */
public class ReportLoaderQueue extends AbstractDomainObject
{

	/**
	 *
	 */
	private static final long serialVersionUID = -6046718658802415256L;
	/**
	 * id.
	 */
	protected Long id;
	/**
	 * report text.
	 */
	protected String reportText;
	/**
	 * participantCollection.
	 */
	protected Collection participantCollection;
	/**
	 * status.
	 */
	protected String status;
	/**
	 * specimenCollectionGroup.
	 */
	protected SpecimenCollectionGroup specimenCollectionGroup;
	/**
	 * surgicalPathologyNumber.
	 */
	protected String surgicalPathologyNumber;
	/**
	 * participantName.
	 */
	protected String participantName;
	/**
	 * reportLoadedDate.
	 */
	protected Date reportLoadedDate;
	/**
	 * siteName.
	 */
	protected String siteName;
	/**
	 * reportCollectionDate.
	 */
	protected Date reportCollectionDate;

	/**
	 * @return status information.
	 * @hibernate.property name="status" type="string" column="STATUS"
	 *                     length="10"
	 */
	public String getStatus()
	{
		return this.status;
	}

	/**
	 * Set the status of the queue record.
	 * @param status
	 *            status of the record of queue
	 */
	public void setStatus(String status)
	{
		this.status = status;
	}

	/**
	 * default Constructor.
	 */
	public ReportLoaderQueue()
	{

	}

	/**
	 * Constructor with text as input.
	 * @param text
	 *            report text
	 */
	public ReportLoaderQueue(String text)
	{
		// this.id=new Long(4);
		this.reportText = text;
	}

	/**
	 * @return system generated id
	 * @hibernate.id name="id" column="IDENTIFIER" type="long" length="30"
	 *               unsaved-value="null" generator-class="native"
	 * @hibernate.generator-param name="sequence"
	 *                            value="CATISSUE_REPORT_QUEUE_SEQ"
	 */
	@Override
	public Long getId()
	{
		return this.id;
	}

	/**
	 * Set id of the object.
	 * @param id
	 *            of the object
	 */
	@Override
	public void setId(Long id)
	{
		this.id = id;
	}

	/**
	 * Returns the collection of Studies for this Protocol.
	 * @hibernate.set name="participantCollection"
	 *                table="CATISSUE_REPORT_PARTICIP_REL" cascade="save-update"
	 *                inverse="false" lazy="false"
	 * @hibernate.collection-key column="REPORT_ID"
	 * @hibernate.collection-many-to-many
	 *                                    class="edu.wustl.catissuecore.domain.Participant"
	 *                                    column="PARTICIPANT_ID"
	 * @return Returns the collection of Studies for this Protocol.
	 */
	public Collection getParticipantCollection()
	{
		return this.participantCollection;
	}

	/**
	 * @param collection
	 *            Assign set of participants to current object
	 */
	public void setParticipantCollection(Collection collection)
	{
		this.participantCollection = collection;
	}

	/**
	 * @return reportText information.
	 * @hibernate.property name="reportText" type="string" column="REPORT_TEXT"
	 *                     length="4000"
	 */
	public String getReportText()
	{
		return this.reportText;
	}

	/**
	 * Set report text.
	 *
	 * @param reportText
	 *            report text
	 */
	public void setReportText(String reportText)
	{
		this.reportText = reportText;
	}

	/**
	 * @param abstractForm : abstractForm
	 * @throws AssignDataException : AssignDataException
	 */
	@Override
	public void setAllValues(IValueObject abstractForm) throws AssignDataException
	{

	}

	/**
	 * @return the specimenCollectionGroup
	 * @hibernate.many-to-one
	 *                        class="edu.wustl.catissuecore.domain.SpecimenCollectionGroup"
	 *                        column="SPECIMEN_COLL_GRP_ID"
	 *                        cascade="save-update"
	 */
	public SpecimenCollectionGroup getSpecimenCollectionGroup()
	{
		return this.specimenCollectionGroup;
	}

	/**
	 * @param specimenCollectionGroup
	 *            the specimenCollectionGroup to set
	 */
	public void setSpecimenCollectionGroup(SpecimenCollectionGroup specimenCollectionGroup)
	{
		this.specimenCollectionGroup = specimenCollectionGroup;
	}

	/**
	 * @return surgical pathology number
	 * @hibernate.property name="surgicalPathologyNumber" type="string"
	 *                     column="SURGICAL_PATHOLOGY_NUMBER" length="255"
	 */
	public String getSurgicalPathologyNumber()
	{
		return this.surgicalPathologyNumber;
	}

	/**
	 * @param accessionNumber : accessionNumber
	 */
	public void setSurgicalPathologyNumber(String accessionNumber)
	{
		this.surgicalPathologyNumber = accessionNumber;
	}

	/**
	 * @return participant name
	 * @hibernate.property name="participantName" type="string"
	 *                     column="PARTICIPANT_NAME" length="255"
	 */
	public String getParticipantName()
	{
		return this.participantName;
	}

	/**
	 * @param participantName : participantName
	 */
	public void setParticipantName(String participantName)
	{
		this.participantName = participantName;
	}

	/**
	 * @return report Loaded Date
	 * @hibernate.property name="reportLoadedDate" type="date"
	 *                     column="REPORT_LOADED_DATE"
	 */
	public Date getReportLoadedDate()
	{
		return this.reportLoadedDate;
	}

	/**
	 * @param reportLoadedDate : reportLoadedDate
	 */
	public void setReportLoadedDate(Date reportLoadedDate)
	{
		this.reportLoadedDate = reportLoadedDate;
	}

	/**
	 * @return site name
	 * @hibernate.property name="siteName" type="string" column="SITE_NAME"
	 *                     length="255"
	 */
	public String getSiteName()
	{
		return this.siteName;
	}

	/**
	 * @param siteName : siteName
	 */
	public void setSiteName(String siteName)
	{
		this.siteName = siteName;
	}

	/**
	 * @return report Loaded Date
	 * @hibernate.property name="reportCollectionDate" type="date"
	 *                     column="REPORT_COLLECTION_DATE"
	 */
	public Date getReportCollectionDate()
	{
		return this.reportCollectionDate;
	}

	/**
	 * @param reportCollectionDate : reportCollectionDate
	 */
	public void setReportCollectionDate(Date reportCollectionDate)
	{
		this.reportCollectionDate = reportCollectionDate;
	}
}
