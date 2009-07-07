
package edu.wustl.catissuecore.domain.pathology;

import java.util.Date;
import java.util.Set;

import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.actionForm.IValueObject;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.AssignDataException;

/**
 * Represents the surgical pathology report.
 * @hibernate.class table="CATISSUE_PATHOLOGY_REPORT"
 */

public class SurgicalPathologyReport extends AbstractDomainObject
{

	/**
	 *
	 */
	private static final long serialVersionUID = 646696104152129141L;

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
	protected Set pathologyReportReviewParameterCollection;

	/**
	 * Specimen collection group of the report.
	 */
	protected SpecimenCollectionGroup specimenCollectionGroup;

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
	protected Site reportSource;

	/**
	 * constructor.
	 */
	public SurgicalPathologyReport()
	{

	}

	/**
	 * @return activity status of the pathology report.
	 * @hibernate.property name="activityStatus" type="string"
	 *                     column="ACTIVITY_STATUS" length="100"
	 */
	public String getActivityStatus()
	{
		return this.activityStatus;
	}

	/**
	 * Sets activity status of the report.
	 * @param activityStatus
	 *            sets activity status of the pathology report.
	 */
	public void setActivityStatus(String activityStatus)
	{
		this.activityStatus = activityStatus;
	}

	/**
	 * @return the binary content of the pathology report.
	 * @hibernate.one-to-one name="binaryContent"
	 *                       class="edu.wustl.catissuecore.domain.pathology.BinaryContent"
	 *                       property-ref="surgicalPathologyReport"
	 *                       not-null="false" cascade="save-update"
	 */
	public BinaryContent getBinaryContent()
	{
		return this.binaryContent;
	}

	/**
	 * Sets binary text content.
	 * @param binaryContent
	 *            sets binary content of the pathology report.
	 */
	public void setBinaryContent(BinaryContent binaryContent)
	{
		this.binaryContent = binaryContent;
	}

	/**
	 * @return system generated id
	 * @hibernate.id name="id" column="IDENTIFIER" type="long" length="30"
	 *               unsaved-value="null" generator-class="native"
	 * @hibernate.generator-param name="sequence"
	 *                            value="CATISSUE_PATHOLOGY_REPORT_SEQ"
	 */
	@Override
	public Long getId()
	{
		return this.id;
	}

	/**
	 * Sets id.
	 * @param id
	 *            sets system generated id
	 */
	@Override
	public void setId(Long id)
	{
		this.id = id;
	}

	/**
	 * @return the review flag for pathology report.
	 * @hibernate.property name="isFlagForReview" column="REVIEW_FLAG"
	 *                     length="10" not-null="false"
	 **/
	public Boolean getIsFlagForReview()
	{
		return this.isFlagForReview;
	}

	/**
	 * Sets isFlagForReview.
	 * @param isFlagForReview
	 *            Sets review flag for pathology report.
	 */
	public void setIsFlagForReview(Boolean isFlagForReview)
	{
		this.isFlagForReview = isFlagForReview;
	}

	/**
	 * @return collection of pathology report review parameters.
	 * @hibernate.set inverse="false" table="CATISSUE_REVIEWREPORT_PARAM"
	 *                lazy="false" cascade="all"
	 * @hibernate.collection-key column="REPORT_ID"
	 * @hibernate.collection-one-to-many class=
	 * "edu.wustl.catissuecore.domain.pathology.PathologyReportReviewParameter"
	 */
	public Set getPathologyReportReviewParameterCollection()
	{
		return this.pathologyReportReviewParameterCollection;
	}

	/**
	 * Sets a collection of pathology report review parameter.
	 * @param pathologyReportReviewParameterCollection
	 *            sets collection of pathology report review parameters.
	 */
	public void setPathologyReportReviewParameterCollection(
			Set pathologyReportReviewParameterCollection)
	{
		this.pathologyReportReviewParameterCollection = pathologyReportReviewParameterCollection;
	}

	/**
	 * @return reportSource associated with pathology report.
	 * @hibernate.many-to-one name="reportSource"
	 *                        class="edu.wustl.catissuecore.domain.Site"
	 *                        column="SOURCE_ID" not-null="false"
	 */
	public Site getReportSource()
	{
		return this.reportSource;
	}

	/**
	 * Sets reportSource of the report.
	 * @param reportSource
	 *            sets reportSource of the pathology report.
	 */
	public void setReportSource(Site reportSource)
	{
		this.reportSource = reportSource;
	}

	/**
	 * @return text content of the pathology report.
	 * @hibernate.one-to-one name="textContent"
	 *                       class="edu.wustl.catissuecore.domain.pathology.TextContent"
	 *                       property-ref="surgicalPathologyReport"
	 *                       not-null="false" cascade="save-update"
	 */
	public TextContent getTextContent()
	{
		return this.textContent;
	}

	/**
	 * Sets text content.
	 * @param textContent
	 *            sets text content of the pathology report.
	 */
	public void setTextContent(TextContent textContent)
	{
		this.textContent = textContent;
	}

	/**
	 * @return xml content of the pathology report.
	 *@hibernate.one-to-one name="xmlContent"
	 *                       class="edu.wustl.catissuecore.domain.pathology.XMLContent"
	 *                       property-ref="surgicalPathologyReport"
	 *                       not-null="false" cascade="save-update"
	 */
	public XMLContent getXmlContent()
	{
		return this.xmlContent;
	}

	/**
	 * Set xml content.
	 * @param xmlContent
	 *            sets xml content of the pathology report.
	 */
	public void setXmlContent(XMLContent xmlContent)
	{
		this.xmlContent = xmlContent;
	}

	/**
	 * @return rreport status of the pathology report.
	 * @hibernate.property name="reportStatus" type="string"
	 *                     column="REPORT_STATUS" length="100"
	 */
	public String getReportStatus()
	{
		return this.reportStatus;
	}

	/**
	 * Set status of the report.
	 * @param reportStatus
	 *            sets report status of the pathology report.
	 */
	public void setReportStatus(String reportStatus)
	{
		this.reportStatus = reportStatus;
	}

	/**
	 * @return the collection date and time of current pathology report.
	 * @hibernate.property name="collectionDateTime" type="date"
	 *                     column="COLLECTION_DATE_TIME"
	 */
	public Date getCollectionDateTime()
	{
		return this.collectionDateTime;
	}

	/**
	 * Sets collection date and time of the report.
	 * @param collectionDateTime
	 *            collection date and time of the report.
	 */
	public void setCollectionDateTime(Date collectionDateTime)
	{
		this.collectionDateTime = collectionDateTime;
	}

	/**
	 * Copies all values from the AbstractForm object.
	 * @param abstractForm
	 *            The AbstractForm object
	 * @throws AssignDataException
	 *             exception occured while assigning data to form attributes
	 */
	public void setAllValues(AbstractActionForm abstractForm) throws AssignDataException
	{

	}

	/**
	 * @param valueObject : valueObject
	 * @throws AssignDataException : AssignDataException
	 * @see
	 * edu.wustl.common.domain.AbstractDomainObject#setAllValues(edu.wustl.common
	 * .actionForm.IValueObject)
	 */
	@Override
	public void setAllValues(IValueObject valueObject) throws AssignDataException
	{
		// TODO Auto-generated method stub

	}

	/**
	 * @return specimen collection group of the report.
	 * @hibernate.many-to-one name="specimenCollectionGroup"
	 *                        class="edu.wustl.catissuecore.domain.SpecimenCollectionGroup"
	 *                        column="SCG_ID" not-null="false"
	 */
	public SpecimenCollectionGroup getSpecimenCollectionGroup()
	{
		return this.specimenCollectionGroup;
	}

	/**
	 * @param specimenCollectionGroup
	 *            sets specimen collection group of the identified report.
	 */
	public void setSpecimenCollectionGroup(SpecimenCollectionGroup specimenCollectionGroup)
	{
		this.specimenCollectionGroup = specimenCollectionGroup;
	}

	/**
	 * @return String
	 */
	@Override
	public String getObjectId()
	{
		return SurgicalPathologyReport.class.getName() + "_" + this.getId();
	}

}