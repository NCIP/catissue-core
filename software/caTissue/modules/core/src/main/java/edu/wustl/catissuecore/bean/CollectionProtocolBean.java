
package edu.wustl.catissuecore.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import edu.wustl.catissuecore.domain.CPGridGrouperPrivilege;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.common.util.global.Status;

// TODO: Auto-generated Javadoc
/**
 * The Class CollectionProtocolBean.
 *
 * @author janhavi_hasabnis
 */
public class CollectionProtocolBean implements Serializable
{

	/** serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** identifier. */
	private Long identifier = null;

	/** protocolCoordinatorIds. */
	private long[] coordinatorIds;

	/** principalInvestigatorId. */
	private long principalInvestigatorId;
	
	private long irbSiteId;
	
	/** List of grid privileges **/
	private List<CPGridGrouperPrivilege> gridPrivilegeList = new ArrayList<CPGridGrouperPrivilege>();

	/** The default label gen. */
	private boolean defaultLabelGen;

	/**
	 * Checks if is default label gen.
	 *
	 * @return true, if is default label gen
	 */
	public boolean isDefaultLabelGen()
	{
		return defaultLabelGen;
	}




	/**
	 * Sets the default label gen.
	 *
	 * @param defaultLabelGen the new default label gen
	 */
	public void setDefaultLabelGen(boolean defaultLabelGen)
	{
		this.defaultLabelGen = defaultLabelGen;
	}

	/** The label format. */
	private String labelFormat;

	private String derivativeLabelFormat;

	private String aliquotLabelFormat;

	public String getDerivativeLabelFormat()
	{
		return derivativeLabelFormat;
	}

	public void setDerivativeLabelFormat(String derivativeLabelFormat)
	{
		this.derivativeLabelFormat = derivativeLabelFormat;
	}

	public String getAliquotLabelFormat()
	{
		return aliquotLabelFormat;
	}

	public void setAliquotLabelFormat(String aliquotLabelFormat)
	{
		this.aliquotLabelFormat = aliquotLabelFormat;
	}

	/** irbID. */
	private String irbID;

	/** descriptionURL. */
	private String descriptionURL;

	/** title. */
	private String title;

	/** shortTitle. */
	private String shortTitle;

	/** startDate. */
	private String startDate;

	/** endDate. */
	private String endDate;

	/** enrollment. */
	private String enrollment;

	/** siteIds. */
	private long[] siteIds;

	/** isParticiapantReg. */
	private boolean isParticiapantReg = false;

	/** clinicalDiagnosis values. */
	private String[] clinicalDiagnosis;

	/**
	 * Gets the label format.
	 *
	 * @return the label format
	 */
	public String getLabelFormat()
	{
		return labelFormat;
	}



	/**
	 * Sets the label format.
	 *
	 * @param labelFormat the new label format
	 */
	public void setLabelFormat(String labelFormat)
	{
		this.labelFormat = labelFormat;
	}



	/**
	 * This will return the clinicalDiagnosis values.
	 *
	 * @return clinicalDiagnosis clinicalDiagnosis.
	 */
	public String[] getClinicalDiagnosis()
	{
		return clinicalDiagnosis;
	}

	/**
	 * This will be called to set clinicalDiagnosis.
	 *
	 * @param clinicalDiagnosis clinicalDiagnosis.
	 */
	public void setClinicalDiagnosis(String[] clinicalDiagnosis)
	{
		this.clinicalDiagnosis = clinicalDiagnosis;
	}

	/**
	 * Checks if is particiapant reg.
	 *
	 * @return - boolean
	 */
	public boolean isParticiapantReg()
	{
		return this.isParticiapantReg;
	}

	/**
	 * Sets the particiapant reg.
	 *
	 * @param isParticiapantRegParam - isParticiapantRegParam
	 */
	public void setParticiapantReg(boolean isParticiapantRegParam)
	{
		this.isParticiapantReg = isParticiapantRegParam;
	}

	/** whether Aliquote in same container. */
	private boolean aliqoutInSameContainer = false;

	/** Activity Status. */
	private String activityStatus = Status.ACTIVITY_STATUS_ACTIVE.toString();

	/** Unsigned Form Url for the Consents. */
	private String unsignedConsentURLName;

	/** Map for Storing Values of Consent Tiers. */
	private Map<String, String> consentValues = new LinkedHashMap<String, String>();//bug 8905

	/** No of Consent Tier. */
	private int consentTierCounter;

	/** CheckBox for consent is checked or not. */
	private boolean consentWaived = false;

	/** operation. */
	private String operation = "Add";

	/**
	 * Gets the coordinator ids.
	 *
	 * @return - array of long.
	 */
	public long[] getCoordinatorIds()
	{
		return this.coordinatorIds;
	}

	/**
	 * Sets the coordinator ids.
	 *
	 * @param coordinatorIdsParam - coordinatorIdsParam
	 */
	public void setCoordinatorIds(long[] coordinatorIdsParam)
	{
		this.coordinatorIds = coordinatorIdsParam;
	}

	/**
	 * Gets the principal investigator id.
	 *
	 * @return - principalInvestigatorId
	 */
	public long getPrincipalInvestigatorId()
	{
		return this.principalInvestigatorId;
	}

	/**
	 * Sets the principal investigator id.
	 *
	 * @param principalInvestigatorIdParam - principalInvestigatorIdParam
	 */
	public void setPrincipalInvestigatorId(long principalInvestigatorIdParam)
	{
		this.principalInvestigatorId = principalInvestigatorIdParam;
	}

	/**
	 * Gets the irb id.
	 *
	 * @return - irbID
	 */
	public String getIrbID()
	{
		return this.irbID;
	}

	/**
	 * Sets the irb id.
	 *
	 * @param irbIDParam - irbIDParam
	 */
	public void setIrbID(String irbIDParam)
	{
		this.irbID = irbIDParam;
	}

	/**
	 * Gets the description url.
	 *
	 * @return - descriptionURL
	 */
	public String getDescriptionURL()
	{
		return this.descriptionURL;
	}

	/**
	 * Sets the description url.
	 *
	 * @param descriptionURLParam - descriptionURLParam
	 */
	public void setDescriptionURL(String descriptionURLParam)
	{
		this.descriptionURL = descriptionURLParam;
	}

	/**
	 * Gets the title.
	 *
	 * @return - title
	 */
	public String getTitle()
	{
		return this.title;
	}

	/**
	 * Sets the title.
	 *
	 * @param titleParam - titleParam
	 */
	public void setTitle(String titleParam)
	{
		this.title = titleParam;
	}

	/**
	 * Gets the short title.
	 *
	 * @return - shortTitle
	 */
	public String getShortTitle()
	{
		return this.shortTitle;
	}

	/**
	 * Sets the short title.
	 *
	 * @param shortTitleParam - shortTitleParam
	 */
	public void setShortTitle(String shortTitleParam)
	{
		this.shortTitle = shortTitleParam;
	}

	/**
	 * Gets the start date.
	 *
	 * @return - startDate
	 */
	public String getStartDate()
	{
		return this.startDate;
	}

	/**
	 * Sets the start date.
	 *
	 * @param startDateParam - startDateParam
	 */
	public void setStartDate(String startDateParam)
	{
		this.startDate = startDateParam;
	}

	/**
	 * Gets the enrollment.
	 *
	 * @return - enrollment
	 */
	public String getEnrollment()
	{
		return this.enrollment;
	}

	/**
	 * Sets the enrollment.
	 *
	 * @param enrollmentParam -enrollmentParam
	 */
	public void setEnrollment(String enrollmentParam)
	{
		this.enrollment = enrollmentParam;
	}

	/**
	 * Gets the unsigned consent url name.
	 *
	 * @return - unsignedConsentURLName
	 */
	public String getUnsignedConsentURLName()
	{
		return this.unsignedConsentURLName;
	}

	/**
	 * Sets the unsigned consent url name.
	 *
	 * @param unsignedConsentURLNameParam - unsignedConsentURLNameParam
	 */
	public void setUnsignedConsentURLName(String unsignedConsentURLNameParam)
	{
		this.unsignedConsentURLName = unsignedConsentURLNameParam;
	}

	/**
	 * Gets the consent values.
	 *
	 * @return - consentValues
	 */
	public Map<String, String> getConsentValues()
	{
		return this.consentValues;
	}

	/**
	 * Sets the consent values.
	 *
	 * @param consentValuesParam - consentValuesParam
	 */
	public void setConsentValues(Map<String, String> consentValuesParam)
	{
		this.consentValues = consentValuesParam;
	}

	/**
	 * Gets the consent tier counter.
	 *
	 * @return - consentTierCounter
	 */
	public int getConsentTierCounter()
	{
		return this.consentTierCounter;
	}

	/**
	 * Sets the consent tier counter.
	 *
	 * @param consentTierCounterParam - consentTierCounterParam
	 */
	public void setConsentTierCounter(int consentTierCounterParam)
	{
		this.consentTierCounter = consentTierCounterParam;
	}

	/**
	 * Checks if is consent waived.
	 *
	 * @return - consentWaived
	 */
	public boolean isConsentWaived()
	{
		return this.consentWaived;
	}

	/**
	 * Sets the consent waived.
	 *
	 * @param consentWaivedParam - consentWaivedParam
	 */
	public void setConsentWaived(boolean consentWaivedParam)
	{
		this.consentWaived = consentWaivedParam;
	}

	/**
	 * Gets the identifier.
	 *
	 * @return - identifier
	 */
	public Long getIdentifier()
	{
		return this.identifier;
	}

	/**
	 * Sets the identifier.
	 *
	 * @param identifierParam - identifierParam
	 */
	public void setIdentifier(Long identifierParam)
	{
		this.identifier = identifierParam;
	}

	/**
	 * Gets the operation.
	 *
	 * @return - operation
	 */
	public String getOperation()
	{
		return this.operation;
	}

	/**
	 * Sets the operation.
	 *
	 * @param operationParam - operationParam
	 */
	public void setOperation(String operationParam)
	{
		this.operation = operationParam;
	}

	/**
	 * Gets the activity status.
	 *
	 * @return - activityStatus
	 */
	public String getActivityStatus()
	{
		return this.activityStatus;
	}

	/**
	 * Sets the activity status.
	 *
	 * @param activityStatusParam - activityStatusParam
	 */
	public void setActivityStatus(String activityStatusParam)
	{
		this.activityStatus = activityStatusParam;
	}

	/**
	 * Gets the end date.
	 *
	 * @return - endDate
	 */
	public String getEndDate()
	{
		return this.endDate;
	}

	/**
	 * Sets the end date.
	 *
	 * @param endDateParam - endDateParam
	 */
	public void setEndDate(String endDateParam)
	{
		this.endDate = endDateParam;
	}

	/**
	 * Checks if is aliqout in same container.
	 *
	 * @return - aliqoutInSameContainer
	 */
	public boolean isAliqoutInSameContainer()
	{
		return this.aliqoutInSameContainer;
	}

	/**
	 * Sets the aliqout in same container.
	 *
	 * @param aliqoutInSameContainerParam - aliqoutInSameContainerParam
	 */
	public void setAliqoutInSameContainer(boolean aliqoutInSameContainerParam)
	{
		this.aliqoutInSameContainer = aliqoutInSameContainerParam;
	}

	/**
	 * Gets the site ids.
	 *
	 * @return - siteIds
	 */
	public long[] getSiteIds()
	{
		return this.siteIds;
	}

	/**
	 * Sets the site ids.
	 *
	 * @param siteIdsParam - siteIdsParam
	 */
	public void setSiteIds(long[] siteIdsParam)
	{
		this.siteIds = siteIdsParam;
	}

	/**
	 * parentCollectionProtocol.
	 *
	 * @return parentCollectionProtocol.
	 */
	public CollectionProtocol getParentCollectionProtocol()
	{
		return this.parentCollectionProtocol;
	}

	/**
	 * sequenceNumber.
	 *
	 * @return sequenceNumber.
	 */
	public Integer getSequenceNumber()
	{
		return this.sequenceNumber;
	}

	/**
	 * type.
	 *
	 * @return type.
	 */
	public String getType()
	{
		return this.type;
	}

	/**
	 * studyCalendarEventPoint.
	 *
	 * @return studyCalendarEventPoint.
	 */
	public Double getStudyCalendarEventPoint()
	{
		return this.studyCalendarEventPoint;
	}

	/**
	 * parentCollectionProtocol.
	 *
	 * @param parentCollectionProtocol parentCollectionProtocol.
	 */
	public void setParentCollectionProtocol(CollectionProtocol parentCollectionProtocol)
	{
		this.parentCollectionProtocol = parentCollectionProtocol;
	}

	/**
	 * sequenceNumber.
	 *
	 * @param sequenceNumber sequenceNumber.
	 */
	public void setSequenceNumber(Integer sequenceNumber)
	{
		this.sequenceNumber = sequenceNumber;
	}

	/**
	 * type.
	 *
	 * @param type type.
	 */
	public void setType(String type)
	{
		this.type = type;
	}

	/**
	 * setStudyCalendarEventPoint .
	 *
	 * @param studyCalendarEventPoint studyCalendarEventPoint.
	 */
	public void setStudyCalendarEventPoint(Double studyCalendarEventPoint)
	{
		this.studyCalendarEventPoint = studyCalendarEventPoint;
	}

	/** Parent Collection Protocol. */
	private CollectionProtocol parentCollectionProtocol;

	/** Sequence Number. */
	private Integer sequenceNumber;

	/** Collection Protocol type - Arm, Cycle, Phase. */
	private String type;

	/** Defines the relative time point in days. */
	private Double studyCalendarEventPoint;

	/** Parent collection protocol Identifier. */
	private Long parentCollectionProtocolId;

	/**
	 * This method will be called to get parentCollectionProtocolId.
	 *
	 * @return parentCollectionProtocolId.
	 */
	public Long getParentCollectionProtocolId()
	{
		return this.parentCollectionProtocolId;
	}

	/**
	 * This method will be called to set parentCollectionProtocolId.
	 *
	 * @param parentCollectionProtocolId parentCollectionProtocolId.
	 */
	public void setParentCollectionProtocolId(Long parentCollectionProtocolId)
	{
		this.parentCollectionProtocolId = parentCollectionProtocolId;
	}




	/**
	 * @return the irbSiteId
	 */
	public long getIrbSiteId() {
		return irbSiteId;
	}




	/**
	 * @param irbSiteId the irbSiteId to set
	 */
	public void setIrbSiteId(long irbSiteId) {
		this.irbSiteId = irbSiteId;
	}

	private long remoteId;

	private boolean dirtyEditFlag;

	private boolean remoteManagedFlag;
	
	public long getRemoteId() {
		return remoteId;
	}

	public void setRemoteId(long remoteId) {
		this.remoteId = remoteId;
	}

	public boolean isDirtyEditFlag() {
		return dirtyEditFlag;
	}

	public void setDirtyEditFlag(boolean dirtyEditFlag) {
		this.dirtyEditFlag = dirtyEditFlag;
	}

	public boolean isRemoteManagedFlag() {
		return remoteManagedFlag;
	}

	public void setRemoteManagedFlag(boolean remoteManagedFlag) {
		this.remoteManagedFlag = remoteManagedFlag;
	}




	public List<CPGridGrouperPrivilege> getGridPrivilegeList() {
		return gridPrivilegeList;
	}




	public void setGridPrivilegeList(List<CPGridGrouperPrivilege> gridPrivilegeList) {
		this.gridPrivilegeList = gridPrivilegeList;
	}

}