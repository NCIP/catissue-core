package edu.wustl.catissuecore.bean;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import edu.wustl.common.util.global.Constants;


public class CollectionProtocolBean implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private Long identifier =null;
	
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
	
	
	public boolean isParticiapantReg()
	{
		return isParticiapantReg;
	}

	
	public void setParticiapantReg(boolean isParticiapantReg)
	{
		this.isParticiapantReg = isParticiapantReg;
	}


	/**
	 * whether Aliquote in same container
	 */
	protected boolean aliqoutInSameContainer = false;
	 /**
     * Activity Status.
     */
    protected String activityStatus = Constants.ACTIVITY_STATUS_ACTIVE;
	
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
	
	public long[] getProtocolCoordinatorIds()
	{
		return protocolCoordinatorIds;
	}
	
	public void setProtocolCoordinatorIds(long[] protocolCoordinatorIds)
	{
		this.protocolCoordinatorIds = protocolCoordinatorIds;
	}
	
	public long getPrincipalInvestigatorId()
	{
		return principalInvestigatorId;
	}
	
	public void setPrincipalInvestigatorId(long principalInvestigatorId)
	{
		this.principalInvestigatorId = principalInvestigatorId;
	}
	
	public String getIrbID()
	{
		return irbID;
	}
	
	public void setIrbID(String irbID)
	{
		this.irbID = irbID;
	}
	
	public String getDescriptionURL()
	{
		return descriptionURL;
	}
	
	public void setDescriptionURL(String descriptionURL)
	{
		this.descriptionURL = descriptionURL;
	}
	
	public String getTitle()
	{
		return title;
	}
	
	public void setTitle(String title)
	{
		this.title = title;
	}
	
	public String getShortTitle()
	{
		return shortTitle;
	}
	
	public void setShortTitle(String shortTitle)
	{
		this.shortTitle = shortTitle;
	}
	
	public String getStartDate()
	{
		return startDate;
	}
	
	public void setStartDate(String startDate)
	{
		this.startDate = startDate;
	}
	
	public String getEnrollment()
	{
		return enrollment;
	}
	
	public void setEnrollment(String enrollment)
	{
		this.enrollment = enrollment;
	}
	
	public String getUnsignedConsentURLName()
	{
		return unsignedConsentURLName;
	}
	
	public void setUnsignedConsentURLName(String unsignedConsentURLName)
	{
		this.unsignedConsentURLName = unsignedConsentURLName;
	}
	
	public Map getConsentValues()
	{
		return consentValues;
	}
	
	public void setConsentValues(Map consentValues)
	{
		this.consentValues = consentValues;
	}
	
	public int getConsentTierCounter()
	{
		return consentTierCounter;
	}
	
	public void setConsentTierCounter(int consentTierCounter)
	{
		this.consentTierCounter = consentTierCounter;
	}
	
	public boolean isConsentWaived()
	{
		return consentWaived;
	}
	
	public void setConsentWaived(boolean consentWaived)
	{
		this.consentWaived = consentWaived;
	}

	
	public Long getIdentifier()
	{
		return identifier;
	}

	
	public void setIdentifier(Long identifier)
	{
		this.identifier = identifier;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) 
	{
		this.operation = operation;
	}

	
	public String getActivityStatus()
	{
		return activityStatus;
	}

	
	public void setActivityStatus(String activityStatus)
	{
		this.activityStatus = activityStatus;
	}

	
	public String getEndDate()
	{
		return endDate;
	}

	
	public void setEndDate(String endDate)
	{
		this.endDate = endDate;
	}

	
	public boolean isAliqoutInSameContainer()
	{
		return aliqoutInSameContainer;
	}

	
	public void setAliqoutInSameContainer(boolean aliqoutInSameContainer)
	{
		this.aliqoutInSameContainer = aliqoutInSameContainer;
	}

	
	public long[] getSiteIds()
	{
		return siteIds;
	}

	
	public void setSiteIds(long[] siteIds)
	{
		this.siteIds = siteIds;
	}
	
	

}
