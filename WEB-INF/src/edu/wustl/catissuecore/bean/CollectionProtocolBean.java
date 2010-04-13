
package edu.wustl.catissuecore.bean;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.common.util.global.Status;

/**
 *  @author janhavi_hasabnis
 *
 */
public class CollectionProtocolBean implements Serializable
{
	/**
	 * serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * identifier.
	 */
	private Long identifier = null;
	/**
	 * protocolCoordinatorIds.
	 */
	private long[] coordinatorIds;
	/**
	 * principalInvestigatorId.
	 */
	private long principalInvestigatorId;

	private boolean generateLabel;

	private boolean defaultLabelGen;

	public boolean isDefaultLabelGen()
	{
		return defaultLabelGen;
	}




	public void setDefaultLabelGen(boolean defaultLabelGen)
	{
		this.defaultLabelGen = defaultLabelGen;
	}

	private String labelFormat;




	/**
	 * irbID.
	 */
	private String irbID;
	/**
	 * descriptionURL.
	 */
	private String descriptionURL;
	/**
	 * title.
	 */
	private String title;
	/**
	 * shortTitle.
	 */
	private String shortTitle;
	/**
	 * startDate.
	 */
	private String startDate;
	/**
	 * endDate.
	 */
	private String endDate;
	/**
	 * enrollment.
	 */
	private String enrollment;
	/**
	 * siteIds.
	 */
	private long[] siteIds;
	/**
	 * isParticiapantReg.
	 */
	private boolean isParticiapantReg = false;

	/**
	 * clinicalDiagnosis values.
	 */
	private String[] clinicalDiagnosis;

	public String getLabelFormat()
	{
		return labelFormat;
	}



	public void setLabelFormat(String labelFormat)
	{
		this.labelFormat = labelFormat;
	}


	public boolean isGenerateLabel()
	{
		return generateLabel;
	}


	public void setGenerateLabel(boolean generateLabel)
	{
		this.generateLabel = generateLabel;
	}

	/**
	 * This will return the clinicalDiagnosis values.
	 * @return clinicalDiagnosis clinicalDiagnosis.
	 */
	public String[] getClinicalDiagnosis()
	{
		return clinicalDiagnosis;
	}

	/**
	 * This will be called to set clinicalDiagnosis.
	 * @param clinicalDiagnosis clinicalDiagnosis.
	 */
	public void setClinicalDiagnosis(String[] clinicalDiagnosis)
	{
		this.clinicalDiagnosis = clinicalDiagnosis;
	}

	/**
	 * @return - boolean
	 */
	public boolean isParticiapantReg()
	{
		return this.isParticiapantReg;
	}

	/**
	 * @param isParticiapantRegParam - isParticiapantRegParam
	 */
	public void setParticiapantReg(boolean isParticiapantRegParam)
	{
		this.isParticiapantReg = isParticiapantRegParam;
	}

	/**
	 * whether Aliquote in same container.
	 */
	private boolean aliqoutInSameContainer = false;
	/**
	* Activity Status.
	*/
	private String activityStatus = Status.ACTIVITY_STATUS_ACTIVE.toString();

	/**
	 * Unsigned Form Url for the Consents.
	 */
	private String unsignedConsentURLName;

	/**
	 * Map for Storing Values of Consent Tiers.
	 */
	private Map<String, String> consentValues = new LinkedHashMap<String, String>();//bug 8905

	/**
	 * No of Consent Tier.
	 */
	private int consentTierCounter;
	/**
	 * CheckBox for consent is checked or not.
	 */
	private boolean consentWaived = false;
	/**
	 * operation.
	 */
	private String operation = "Add";

	/**
	 * @return - array of long.
	 */
	public long[] getCoordinatorIds()
	{
		return this.coordinatorIds;
	}

	/**
	 * @param coordinatorIdsParam - coordinatorIdsParam
	 */
	public void setCoordinatorIds(long[] coordinatorIdsParam)
	{
		this.coordinatorIds = coordinatorIdsParam;
	}

	/**
	 * @return - principalInvestigatorId
	 */
	public long getPrincipalInvestigatorId()
	{
		return this.principalInvestigatorId;
	}

	/**
	 * @param principalInvestigatorIdParam - principalInvestigatorIdParam
	 */
	public void setPrincipalInvestigatorId(long principalInvestigatorIdParam)
	{
		this.principalInvestigatorId = principalInvestigatorIdParam;
	}

	/**
	 * @return - irbID
	 */
	public String getIrbID()
	{
		return this.irbID;
	}

	/**
	 * @param irbIDParam - irbIDParam
	 */
	public void setIrbID(String irbIDParam)
	{
		this.irbID = irbIDParam;
	}

	/**
	 * @return - descriptionURL
	 */
	public String getDescriptionURL()
	{
		return this.descriptionURL;
	}

	/**
	 * @param descriptionURLParam - descriptionURLParam
	 */
	public void setDescriptionURL(String descriptionURLParam)
	{
		this.descriptionURL = descriptionURLParam;
	}

	/**
	 * @return - title
	 */
	public String getTitle()
	{
		return this.title;
	}

	/**
	 * @param titleParam - titleParam
	 */
	public void setTitle(String titleParam)
	{
		this.title = titleParam;
	}

	/**
	 * @return - shortTitle
	 */
	public String getShortTitle()
	{
		return this.shortTitle;
	}

	/**
	 * @param shortTitleParam - shortTitleParam
	 */
	public void setShortTitle(String shortTitleParam)
	{
		this.shortTitle = shortTitleParam;
	}

	/**
	 * @return - startDate
	 */
	public String getStartDate()
	{
		return this.startDate;
	}

	/**
	 * @param startDateParam - startDateParam
	 */
	public void setStartDate(String startDateParam)
	{
		this.startDate = startDateParam;
	}

	/**
	 * @return - enrollment
	 */
	public String getEnrollment()
	{
		return this.enrollment;
	}

	/**
	 * @param enrollmentParam -enrollmentParam
	 */
	public void setEnrollment(String enrollmentParam)
	{
		this.enrollment = enrollmentParam;
	}

	/**
	 * @return - unsignedConsentURLName
	 */
	public String getUnsignedConsentURLName()
	{
		return this.unsignedConsentURLName;
	}

	/**
	 * @param unsignedConsentURLNameParam - unsignedConsentURLNameParam
	 */
	public void setUnsignedConsentURLName(String unsignedConsentURLNameParam)
	{
		this.unsignedConsentURLName = unsignedConsentURLNameParam;
	}

	/**
	 * @return - consentValues
	 */
	public Map<String, String> getConsentValues()
	{
		return this.consentValues;
	}

	/**
	 * @param consentValuesParam - consentValuesParam
	 */
	public void setConsentValues(Map<String, String> consentValuesParam)
	{
		this.consentValues = consentValuesParam;
	}

	/**
	 * @return - consentTierCounter
	 */
	public int getConsentTierCounter()
	{
		return this.consentTierCounter;
	}

	/**
	 * @param consentTierCounterParam - consentTierCounterParam
	 */
	public void setConsentTierCounter(int consentTierCounterParam)
	{
		this.consentTierCounter = consentTierCounterParam;
	}

	/**
	 * @return - consentWaived
	 */
	public boolean isConsentWaived()
	{
		return this.consentWaived;
	}

	/**
	 * @param consentWaivedParam - consentWaivedParam
	 */
	public void setConsentWaived(boolean consentWaivedParam)
	{
		this.consentWaived = consentWaivedParam;
	}

	/**
	 * @return - identifier
	 */
	public Long getIdentifier()
	{
		return this.identifier;
	}

	/**
	 * @param identifierParam - identifierParam
	 */
	public void setIdentifier(Long identifierParam)
	{
		this.identifier = identifierParam;
	}

	/**
	 * @return - operation
	 */
	public String getOperation()
	{
		return this.operation;
	}

	/**
	 * @param operationParam - operationParam
	 */
	public void setOperation(String operationParam)
	{
		this.operation = operationParam;
	}

	/**
	 * @return - activityStatus
	 */
	public String getActivityStatus()
	{
		return this.activityStatus;
	}

	/**
	 * @param activityStatusParam - activityStatusParam
	 */
	public void setActivityStatus(String activityStatusParam)
	{
		this.activityStatus = activityStatusParam;
	}

	/**
	 * @return - endDate
	 */
	public String getEndDate()
	{
		return this.endDate;
	}

	/**
	 * @param endDateParam - endDateParam
	 */
	public void setEndDate(String endDateParam)
	{
		this.endDate = endDateParam;
	}

	/**
	 * @return - aliqoutInSameContainer
	 */
	public boolean isAliqoutInSameContainer()
	{
		return this.aliqoutInSameContainer;
	}

	/**
	 * @param aliqoutInSameContainerParam - aliqoutInSameContainerParam
	 */
	public void setAliqoutInSameContainer(boolean aliqoutInSameContainerParam)
	{
		this.aliqoutInSameContainer = aliqoutInSameContainerParam;
	}

	/**
	 * @return - siteIds
	 */
	public long[] getSiteIds()
	{
		return this.siteIds;
	}

	/**
	 * @param siteIdsParam - siteIdsParam
	 */
	public void setSiteIds(long[] siteIdsParam)
	{
		this.siteIds = siteIdsParam;
	}

	/**
	 * parentCollectionProtocol.
	 * @return parentCollectionProtocol.
	 */
	public CollectionProtocol getParentCollectionProtocol()
	{
		return this.parentCollectionProtocol;
	}

	/**
	 * sequenceNumber.
	 * @return sequenceNumber.
	 */
	public Integer getSequenceNumber()
	{
		return this.sequenceNumber;
	}

	/**
	 * type.
	 * @return type.
	 */
	public String getType()
	{
		return this.type;
	}

	/**
	 * studyCalendarEventPoint.
	 * @return studyCalendarEventPoint.
	 */
	public Double getStudyCalendarEventPoint()
	{
		return this.studyCalendarEventPoint;
	}

	/**
	 * parentCollectionProtocol.
	 * @param parentCollectionProtocol parentCollectionProtocol.
	 */
	public void setParentCollectionProtocol(CollectionProtocol parentCollectionProtocol)
	{
		this.parentCollectionProtocol = parentCollectionProtocol;
	}

	/**
	 * sequenceNumber.
	 * @param sequenceNumber sequenceNumber.
	 */
	public void setSequenceNumber(Integer sequenceNumber)
	{
		this.sequenceNumber = sequenceNumber;
	}

	/**
	 * type.
	 * @param type type.
	 */
	public void setType(String type)
	{
		this.type = type;
	}

	/**
	 * setStudyCalendarEventPoint .
	 * @param studyCalendarEventPoint studyCalendarEventPoint.
	 */
	public void setStudyCalendarEventPoint(Double studyCalendarEventPoint)
	{
		this.studyCalendarEventPoint = studyCalendarEventPoint;
	}

	/**
	 * Parent Collection Protocol.
	 */
	private CollectionProtocol parentCollectionProtocol;

	/**
	 * Sequence Number.
	 */
	private Integer sequenceNumber;
	/**
	 * Collection Protocol type - Arm, Cycle, Phase.
	 */
	private String type;

	/**
	 * Defines the relative time point in days.
	 */
	private Double studyCalendarEventPoint;

	/**
	 * Parent collection protocol Identifier.
	 */
	private Long parentCollectionProtocolId;

	/**
	 * This method will be called to get parentCollectionProtocolId.
	 * @return parentCollectionProtocolId.
	 */
	public Long getParentCollectionProtocolId()
	{
		return this.parentCollectionProtocolId;
	}

	/**
	 * This method will be called to set parentCollectionProtocolId.
	 * @param parentCollectionProtocolId parentCollectionProtocolId.
	 */
	public void setParentCollectionProtocolId(Long parentCollectionProtocolId)
	{
		this.parentCollectionProtocolId = parentCollectionProtocolId;
	}
}