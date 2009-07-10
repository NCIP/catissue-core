
package edu.wustl.catissuecore.bean;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import edu.wustl.common.util.global.Status;
/**
 *  @author janhavi_hasabnis
 *
 */
public class CollectionProtocolBean implements Serializable
{

	private static final long serialVersionUID = 1L;

	private Long identifier = null;

	private long[] protocolCoordinatorIds;

	private long principalInvestigatorId;

	private String irbID;

	private String descriptionURL;

	private String title;

	private String shortTitle;

	private String startDate;

	protected String endDate;

	private String enrollment;

	private long[] siteIds;

	private boolean isParticiapantReg = false;
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
	 * whether Aliquote in same container
	 */
	protected boolean aliqoutInSameContainer = false;
	/**
	* Activity Status.
	*/
	protected String activityStatus = Status.ACTIVITY_STATUS_ACTIVE.toString();

	/**
	 * Unsigned Form Url for the Consents
	 */
	private String unsignedConsentURLName;

	/**
	 * Map for Storing Values of Consent Tiers.
	 */
	private Map consentValues = new LinkedHashMap();//bug 8905

	/**
	 * No of Consent Tier
	 */
	private int consentTierCounter;
	/**
	 * CheckBox for consent is checked or not
	 */
	private boolean consentWaived = false;

	private String operation = "Add";
    /**
     * @return - array of long
     */
	public long[] getProtocolCoordinatorIds()
	{
		return this.protocolCoordinatorIds;
	}
    /**
     * @param protocolCoordinatorIdsParam - protocolCoordinatorIdsParam
     */
	public void setProtocolCoordinatorIds(long[] protocolCoordinatorIdsParam)
	{
		this.protocolCoordinatorIds = protocolCoordinatorIdsParam;
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
	public Map getConsentValues()
	{
		return this.consentValues;
	}
    /**
     * @param consentValuesParam - consentValuesParam
     */
	public void setConsentValues(Map consentValuesParam)
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

}
