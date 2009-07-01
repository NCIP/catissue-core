/**
 * <p>Title: ConflictCommonForm Class>
 * <p>Description:	This class contains attributes to display on ConflictCommonView.jsp Page</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @version 1.00
 * @author kalpana_thakur
 * Created on Feb 27,2007
 */

package edu.wustl.catissuecore.actionForm;

import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;

/**
 * @author kalpana_thakur
 *
 */
public class ConflictCommonForm extends AbstractActionForm
{

	private static final long serialVersionUID = 1L;

	/**
	 * String for participant Name
	 */
	protected String participantName;

	/**
	 * String for surgicalPathologyNumber of report
	 */
	protected String surgicalPathologyNumber;

	/**
	 * String for report Date
	 */
	protected String reportDate;

	/**
	 * String for report collectionDate
	 */
	protected String reportCollectionDate;

	/**
	 * String for siteName 
	 */
	protected String siteName;

	/**
	 * String for socialSecurityNumber of participant
	 */
	protected String socialSecurityNumber;

	/**
	 * String for birth Date participant
	 */
	protected String birthDate;

	public int getFormId()
	{
		return 0;
	}

	protected void reset()
	{

	}

	public void setAllValues(AbstractDomainObject abstractDomain)
	{

	}

	/**
	 * This mettod is called to set the surgicalPathologyNumber
	 * @param surgicalPathologyNumber the surgicalPathologyNumber to set
	 */
	public void setSurgicalPathologyNumber(String surgicalPathologyNumber)
	{
		this.surgicalPathologyNumber = surgicalPathologyNumber;
	}

	/**
	 * @return the surgicalPathologyNumber
	 */
	public String getSurgicalPathologyNumber()
	{
		return surgicalPathologyNumber;
	}

	/**
	 * T
	 * @return
	 */
	public String getSiteName()
	{
		return siteName;
	}

	/**
	 * This mettod is called to set the site Name
	 * @param siteName
	 */
	public void setSiteName(final String siteName)
	{
		this.siteName = siteName;
	}

	/** 
	 * @return report Date
	 */
	public String getReportDate()
	{
		return reportDate;
	}

	/**
	 * This mettod is called to set the site Name
	 * @param siteName
	 */
	public void setReportDate(final String reportDate)
	{
		this.reportDate = reportDate;
	}

	/**
	 * @return name of the participant
	 */
	public String getParticipantName()
	{
		return participantName;
	}

	/**
	 * This mettod is called to set the participant Name
	 * @param participantName
	 */
	public void setParticipantName(final String participantName)
	{
		this.participantName = participantName;
	}

	/**
	 * @return the birth date of the participant
	 */
	public String getBirthDate()
	{
		return birthDate;
	}

	/**
	 * This mettod is called to set the birth Date
	 * @param birthDate
	 */
	public void setBirthDate(final String birthDate)
	{
		this.birthDate = birthDate;
	}

	/**
	 * @return the social security number of the participant
	 */
	public String getSocialSecurityNumber()
	{
		return socialSecurityNumber;
	}

	/**
	 * This mettod is called to set the socialSecurityNumber 
	 * @param socialSecurityNumber
	 */
	public void setSocialSecurityNumber(String socialSecurityNumber)
	{
		this.socialSecurityNumber = socialSecurityNumber;
	}

	/**
	 * This method called to get the collection date
	 * @return
	 */

	public String getReportCollectionDate()
	{
		return reportCollectionDate;
	}

	/**
	 * This method called to set the collection date
	 * @param reportCollectionDate
	 */

	public void setReportCollectionDate(String reportCollectionDate)
	{
		this.reportCollectionDate = reportCollectionDate;
	}

	@Override
	public void setAddNewObjectIdentifier(String arg0, Long arg1)
	{
		// TODO Auto-generated method stub

	}

}
