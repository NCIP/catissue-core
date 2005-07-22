/**
 * <p>Title: SpecimenProtocol Class</p>
 * <p>Description:  A set of procedures that govern the collection and/or distribution of biospecimens.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Mandar Deshmukh
 * @version 1.00
 * Created on July 12, 2005
 */

package edu.wustl.catissuecore.domain;

import java.util.Date;

/**
 *A set of procedures that govern the collection and/or distribution of biospecimens. 
 * @author mandar_deshmukh
 * 
 * @hibernate.class table="CATISSUE_SPECIMEN_PROTOCOL"
 */
public abstract class SpecimenProtocol implements java.io.Serializable
{
	private static final long serialVersionUID = 1234567890L;

	/**
	 * System generated unique systemIdentifier.
	 */
	protected Long systemIdentifier;
	
	/**
	 * The current principal investigator of the protocol.
	 */
	protected ApplicationUser principalInvestigator;
	
	/**
	 * Full title assigned to the protocol.
	 */
	protected String title;
	
	/**
	 * Abbreviated title assigned to the protocol.
	 */
	protected String shortTitle;
	
	/**
	 * IRB approval number.
	 */
	protected String irbIdentifier;
	
	/**
	 * Date on which the protocol is activated.
	 */
	protected Date startDate;
	
	/**
	 * Date on which the protocol is marked as closed.
	 */
	protected Date endDate;
	
	/**
	 * Number of anticipated cases need for the protocol.
	 */
	protected Integer enrollment;
	
	/**
	 * URL to the document that describes detailed information for the biospecimen protocol.
	 */
	protected String descriptionURL;
	
	/**
	 * Defines whether this SpecimenProtocol record can be queried (Active) or not queried (Inactive) by any actor.
	 */
	protected String activityStatus;

	/**
	 * Returns the systemidentifier of the protocol.
	 * @hibernate.id name="systemIdentifier" column="IDENTIFIER" type="long" length="30"
	 * unsaved-value="null" generator-class="native"
	 * @return Returns the systemIdentifier.
	 */
	public Long getSystemIdentifier()
	{
		return systemIdentifier;
	}

	/**
	 * @param systemIdentifier The systemIdentifier to set.
	 */
	public void setSystemIdentifier(Long systemIdentifier)
	{
		this.systemIdentifier = systemIdentifier;
	}

	/**
	 * Returns the principal investigator of the protocol.
	 * @hibernate.many-to-one column="PRINCIPAL_INVESTIGATOR_ID" class="edu.wustl.catissuecore.domain.ApplicationUser"
	 * constrained="true"
	 * @return the principal investigator of the protocol.
	 * @see #setPrincipalInvestigator(ApplicationUser)
	 */
	public ApplicationUser getPrincipalInvestigator()
	{
		return principalInvestigator;
	}

	/**
	 * @param principalInvestigator The principalInvestigator to set.
	 */
	public void setPrincipalInvestigator(ApplicationUser principalInvestigator)
	{
		this.principalInvestigator = principalInvestigator;
	}

	/**
	 * Returns the title of the protocol.
	 * @hibernate.property name="title" type="string" column="TITLE" length="50" not-null="true" unique="true"
	 * @return Returns the title.
	 */
	public String getTitle()
	{
		return title;
	}

	/**
	 * @param title The title to set.
	 */
	public void setTitle(String title)
	{
		this.title = title;
	}

	/**
	 * Returns the short title of the protocol.
	 * @hibernate.property name="shortTitle" type="string" column="SHORT_TITLE"
	 * length="50"
	 * @return Returns the shortTitle.
	 */
	public String getShortTitle()
	{
		return shortTitle;
	}

	/**
	 * @param shortTitle The shortTitle to set.
	 */
	public void setShortTitle(String shortTitle)
	{
		this.shortTitle = shortTitle;
	}

	/**
	 * Returns the irb systemIdentifier of the protocol.
	 * @hibernate.property name="irbIdentifier" type="string" column="IRB_IDENTIFIER" length="50"
	 * @return Returns the irbIdentifier.
	 */
	public String getIrbIdentifier()
	{
		return irbIdentifier;
	}

	/**
	 * @param irbIdentifier The irbIdentifier to set.
	 */
	public void setIrbIdentifier(String irbIdentifier)
	{
		this.irbIdentifier = irbIdentifier;
	}

	/**
	 * Returns the startdate of the protocol.
	 * @hibernate.property name="startDate" type="date" column="START_DATE" length="50"
	 * @return Returns the startDate.
	 */
	public Date getStartDate()
	{
		return startDate;
	}

	/**
	 * @param startDate The startDate to set.
	 */
	public void setStartDate(Date startDate)
	{
		this.startDate = startDate;
	}

	/**
	 * Returns the enddate of the protocol.
	 * @hibernate.property name="endDate" type="date" column="END_DATE" length="50"
	 * @return Returns the endDate.
	 */
	public Date getEndDate()
	{
		return endDate;
	}

	/**
	 * @param endDate The endDate to set.
	 */
	public void setEndDate(Date endDate)
	{
		this.endDate = endDate;
	}

	/**
	 * Returns the enrollment.
	 * @hibernate.property name="enrollment" type="int" column="ENROLLMENT" length="50"
	 * @return Returns the enrollment.
	 */
	public Integer getEnrollment()
	{
		return enrollment;
	}

	/**
	 * @param enrollment The enrollment to set.
	 */
	public void setEnrollment(Integer enrollment)
	{
		this.enrollment = enrollment;
	}

	/**
	 * Returns the descriptionURL.
	 * @hibernate.property name="descriptionURL" type="string" column="DESCRIPTION_URL" length="200"
	 * @return Returns the descriptionURL.
	 */
	public String getDescriptionURL()
	{
		return descriptionURL;
	}

	/**
	 * @param descriptionURL The descriptionURL to set.
	 */
	public void setDescriptionURL(String descriptionURL)
	{
		this.descriptionURL = descriptionURL;
	}

	/**
	 * Returns the activityStatus.
	 * @hibernate.property name="activityStatus" type="string" column="ACTIVITY_STATUS" length="50"
	 * @return Returns the activityStatus.
	 */
	public String getActivityStatus()
	{
		return activityStatus;
	}

	/**
	 * @param activityStatus The activityStatus to set.
	 */
	public void setActivityStatus(String activityStatus)
	{
		this.activityStatus = activityStatus;
	}
}