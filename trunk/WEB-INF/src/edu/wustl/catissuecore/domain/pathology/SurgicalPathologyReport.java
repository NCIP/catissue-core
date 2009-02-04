package edu.wustl.catissuecore.domain.pathology;

import java.util.Date;
import java.util.Set;

import edu.wustl.catissuecore.domain.Site;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.AssignDataException;

/**
 * Represents the surgical pathology report.
 * @hibernate.class
 * table="CATISSUE_PATHOLOGY_REPORT"
 */

public class SurgicalPathologyReport extends AbstractDomainObject
{

	/**
	 * Activity status of a pathology report.
	 */
	protected String activityStatus;
	
	/**
	 * Report status of a pathology report.
	 */
	protected String reportStatus;
	
	
	/**
	 * Collection date and time of the report.
	 */
	protected Date collectionDateTime;
	/**
	 * System generated unique ID.
	 */
	protected Long id;
	
	/**
	 * Review status of the pathology report.
	 */
	protected Boolean isFlagForReview;
	
	/**
	 * Collection of review items for current pathology report.
	 */
	protected Set pathologyReportReviewParameterSetCollection;
	
	/**
	 * XML content of the pathology report.
	 */
	protected XMLContent xmlContent;
	
	/**
	 * Text Content of the pathology report.
	 */
	protected TextContent textContent;
	
	/**
	 * Binary content of the pathology report.
	 */
	protected BinaryContent binaryContent;
	
	/**
	 * Soure (site) of the pathology report.
	 */
	protected Site source;

	
	/**
	 * constructor
	 */
	public SurgicalPathologyReport()
	{

	}
		
	/**
	 * @return activity status of the pathology report.
     * @hibernate.property name="activityStatus"
     * type="string" column="ACTIVITY_STATUS" 
     * length="100"
     */
	public String getActivityStatus()
	{
		return activityStatus;
	}

	/**
	 * Sets activity status of the report
	 * @param activityStatus sets activity status of the pathology report.
	 */
	public void setActivityStatus(String activityStatus)
	{
		this.activityStatus = activityStatus;
	}
	
	/**
	 * @return the binary content of the pathology report.	
	 * @hibernate.one-to-one  name="binaryContent"
	 * class="edu.wustl.catissuecore.domain.pathology.BinaryContent"
	 * property-ref="surgicalPathologyReport" not-null="false" cascade="save-update"
	 */
	public BinaryContent getBinaryContent()
	{
		return binaryContent;
	}

	/**
	 * Sets binary text content
	 * @param binaryContent sets binary content of the pathology report.
	 */
	public void setBinaryContent(BinaryContent binaryContent)
	{
		this.binaryContent = binaryContent;
	}

	/**
    * @return system generated id 
    * @hibernate.id name="id" column="IDENTIFIER" type="long" length="30"
    * unsaved-value="null" generator-class="native" 
    * @hibernate.generator-param name="sequence" value="CATISSUE_PATHOLOGY_REPORT_SEQ"
    */
	public Long getId()
	{
		return id;
	}

	/**
	 * Sets id
	 * @param id sets system generated id
	 */
	public void setId(Long id)
	{
		this.id = id;
	}

	/**
    * @return the review flag for pathology report. 
	* @hibernate.property 	name="isFlagForReview"
	* column="REVIEW_FLAG" length="10"
	* not-null="false"
	**/
	public Boolean getIsFlagForReview()
	{
		return isFlagForReview;
	}
	
	/**
	 * Sets isFlagForReview
	 * @param isFlagForReview Sets review flag for pathology report.
	 */
	public void setIsFlagForReview(Boolean isFlagForReview)
	{
		this.isFlagForReview = isFlagForReview;
	}
	
	/**
    * @return collection of pathology report review parameters.
    * @hibernate.set inverse="false" table="CATISSUE_REVIEWREPORT_PARAM"
	* lazy="false" cascade="all" 
	* @hibernate.collection-key column="REPORT_ID"
	* @hibernate.collection-one-to-many class="edu.wustl.catissuecore.domain.pathology.PathologyReportReviewParameter"
	*/
	public Set getPathologyReportReviewParameterSetCollection()
	{
		return pathologyReportReviewParameterSetCollection;
	}
	
	/**
	 * Sets a collection of pathology report review parameter
	 * @param pathologyReportReviewParameterSetCollection sets collection of pathology report review parameters.
	 */
	public void setPathologyReportReviewParameterSetCollection(
			Set pathologyReportReviewParameterSetCollection)
	{
		this.pathologyReportReviewParameterSetCollection = pathologyReportReviewParameterSetCollection;
	}

	/**
	 * @return source associated with pathology report.
	 * @hibernate.many-to-one name="source"
	 * class="edu.wustl.catissuecore.domain.Site"
	 * column="SOURCE_ID" not-null="false"
	 */
	public Site getSource()
	{
		return source;
	}

	/**
	 * Sets source of the report
	 * @param source sets source of the pathology report.
	 */
	public void setSource(Site source)
	{
		this.source = source;
	}
	
	/**
	 * @return text content of the pathology report.
	 * @hibernate.one-to-one name="textContent"
	 * class="edu.wustl.catissuecore.domain.pathology.TextContent"
	 * property-ref="surgicalPathologyReport" not-null="false" cascade="save-update"
	 */
	public TextContent getTextContent()
	{
		return textContent;
	}

	/**
	 * Sets text content
	 * @param textContent sets text content of the pathology report.
	 */
	public void setTextContent(TextContent textContent)
	{
		this.textContent = textContent;
	}
	
	/**	
	 * @return xml content of the pathology report.
	 *@hibernate.one-to-one name="xmlContent"
	 *class="edu.wustl.catissuecore.domain.pathology.XMLContent"
	 *property-ref="surgicalPathologyReport" not-null="false" cascade="save-update"
	 */
	public XMLContent getXmlContent()
	{
		return xmlContent;
	}

	/**
	 * Set xml content
	 * @param xmlContent sets xml content of the pathology report.
	 */
	public void setXmlContent(XMLContent xmlContent)
	{
		this.xmlContent = xmlContent;
	}

	
	/**
	 * @return rreport status of the pathology report.
     * @hibernate.property name="reportStatus"
     * type="string" column="REPORT_STATUS" 
     * length="100"
	 */
	public String getReportStatus()
	{
		return reportStatus;
	}

	
	/**
	 * Set status of the report
	 * @param reportStatus sets report status of the pathology report.
	 */
	public void setReportStatus(String reportStatus)
	{
		this.reportStatus = reportStatus;
	}

	/**
	 * @return the collection date and time of current pathology report.
     * @hibernate.property name="collectionDateTime"
     * type="date" column="COLLECTION_DATE_TIME" 
     */
	public Date getCollectionDateTime()
	{
		return collectionDateTime;
	}


	
	/**
	 * Sets collection date and time of the report
	 * @param collectionDateTime collection date and time of the report.
	 */
	public void setCollectionDateTime(Date collectionDateTime)
	{
		this.collectionDateTime = collectionDateTime;
	}
	
	/**
     * Copies all values from the AbstractForm object
     * @param abstractForm The AbstractForm object
     * @throws AssignDataException exception occured while assigning data to form attributes
     */
    public void setAllValues(AbstractActionForm abstractForm) throws AssignDataException
    {
    	
    }
    
	
}