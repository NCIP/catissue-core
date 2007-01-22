package edu.wustl.catissuecore.domain.pathology;

import java.util.Collection;


/**
 * Represents different logical sections of surgical pathology report.
 * @hibernate.class
 * table="CATISSUE_REPORT_QUEUE"
 */
public class ReportLoaderQueue
{

	protected Long id;
	protected String reportText;
	protected Collection participantCollection;
	protected String status;
	
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
	 * @param status
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
	 * @param text
	 * Constructor with text as input
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
	 * @param id
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
	 * @param reportText
	 */
	public void setReportText(String reportText)
	{
		this.reportText = reportText;
	}
	
	
	
	
}
