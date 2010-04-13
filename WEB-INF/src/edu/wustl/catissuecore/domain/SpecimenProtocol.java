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

import edu.wustl.catissuecore.actionForm.SpecimenProtocolForm;
import edu.wustl.catissuecore.util.SearchUtil;
import edu.wustl.common.actionForm.IValueObject;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.AssignDataException;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.util.global.CommonUtilities;
import edu.wustl.common.util.logger.Logger;

/**
 * A set of procedures that govern the collection and/or distribution of biospecimens.
 * @author mandar_deshmukh
 * @hibernate.class table="CATISSUE_SPECIMEN_PROTOCOL"
 */
public abstract class SpecimenProtocol extends AbstractDomainObject implements java.io.Serializable
{

	/**
	 * logger Logger - Generic logger.
	 */
	private static Logger logger = Logger.getCommonLogger(SpecimenProtocol.class);

	/**
	 * Serial Version ID.
	 */
	private static final long serialVersionUID = 1234567890L;

	/**
	 * System generated unique id.
	 */
	protected Long id = null;

	//Change for API Search   --- Ashwin 04/10/2006
	/**
	 * The current principal investigator of the protocol.
	 */
	protected User principalInvestigator;

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
	 * Defines whether this SpecimenProtocol record can be queried (Active) or not
	 * queried (Inactive) by any actor.
	 */
	protected String activityStatus;

	/**
	 * Default Constructor.
	 * NOTE: Do not delete this constructor. Hibernet uses this by reflection API.
	 */
	public SpecimenProtocol()
	{
		super();
		// Default Constructor, required for Hibernate
	}

	/**
	 * Returns the id of the protocol.
	 * @hibernate.id name="id" column="IDENTIFIER" type="long" length="30"
	 * unsaved-value="null" generator-class="native"
	 * @hibernate.generator-param name="sequence" value="CATISSUE_SPECIMEN_PROTOCOL_SEQ"
	 * @return Returns the id.
	 */
	@Override
	public Long getId()
	{
		return this.id;
	}

	/**
	 * @param identifier The id to set.
	 */
	@Override
	public void setId(Long identifier)
	{
		this.id = identifier;
	}

	/**
	 * Returns the principal investigator of the protocol.
	 * @hibernate.many-to-one column="PRINCIPAL_INVESTIGATOR_ID" class="edu.wustl.catissuecore.domain.User"
	 * constrained="true"
	 * @return the principal investigator of the protocol.
	 * @see #setPrincipalInvestigator(User)
	 */
	public User getPrincipalInvestigator()
	{
		return this.principalInvestigator;
	}

	/**
	 * @param principalInvestigator The principalInvestigator to set.
	 */
	public void setPrincipalInvestigator(User principalInvestigator)
	{
		this.principalInvestigator = principalInvestigator;
	}

	/**
	 * Returns the title of the protocol.
	 * @hibernate.property name="title" type="string" column="TITLE" length="255" not-null="true" unique="true"
	 * @return Returns the title.
	 */
	public String getTitle()
	{
		return this.title;
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
	 * length="255"
	 * @return Returns the shortTitle.
	 */
	public String getShortTitle()
	{
		return this.shortTitle;
	}

	/**
	 * @param shortTitle The shortTitle to set.
	 */
	public void setShortTitle(String shortTitle)
	{
		this.shortTitle = shortTitle;
	}

	/**
	 * Returns the irb id of the protocol.
	 * @hibernate.property name="irbIdentifier" type="string" column="IRB_IDENTIFIER" length="255"
	 * @return Returns the irbIdentifier.
	 */
	public String getIrbIdentifier()
	{
		return this.irbIdentifier;
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
		return this.startDate;
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
		return this.endDate;
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
		return this.enrollment;
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
	 * @hibernate.property name="descriptionURL" type="string" column="DESCRIPTION_URL" length="255"
	 * @return Returns the descriptionURL.
	 */
	public String getDescriptionURL()
	{
		return this.descriptionURL;
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
		return this.activityStatus;
	}

	/**
	 * @param activityStatus The activityStatus to set.
	 */
	public void setActivityStatus(String activityStatus)
	{
		this.activityStatus = activityStatus;
	}

	/**
	 * Set All Values in Form.
	 * @param abstractForm IValueObject.
	 * @throws AssignDataException : AssignDataException
	 */
	@Override
	public void setAllValues(IValueObject abstractForm) throws AssignDataException
	{
		logger.debug("SpecimenProtocol: setAllValues ");
		try
		{
			//Change for API Search   --- Ashwin 04/10/2006
			if (SearchUtil.isNullobject(this.principalInvestigator))
			{
				this.principalInvestigator = new User();
			}

			final SpecimenProtocolForm spForm = (SpecimenProtocolForm) abstractForm;

			this.title = spForm.getTitle();
			this.shortTitle = spForm.getShortTitle();
			this.irbIdentifier = spForm.getIrbID();

			this.startDate = CommonUtilities.parseDate(spForm.getStartDate(), CommonUtilities
					.datePattern(spForm.getStartDate()));
			this.endDate = CommonUtilities.parseDate(spForm.getEndDate(), CommonUtilities
					.datePattern(spForm.getEndDate()));

			if (spForm.getEnrollment() != null && spForm.getEnrollment().trim().length() > 0)
			{
				this.enrollment = Integer.valueOf(spForm.getEnrollment());
			}

			this.descriptionURL = spForm.getDescriptionURL();
			this.activityStatus = spForm.getActivityStatus();

			this.principalInvestigator = new User();
			this.principalInvestigator.setId(Long.valueOf(spForm.getPrincipalInvestigatorId()));
		}
		catch (final Exception excp)
		{
			SpecimenProtocol.logger.error(excp.getMessage(), excp);
			excp.printStackTrace();
			final ErrorKey errorKey = ErrorKey.getErrorKey("assign.data.error");
			throw new AssignDataException(errorKey, null, "SpecimenProtocol.java :");
		}
	}
}