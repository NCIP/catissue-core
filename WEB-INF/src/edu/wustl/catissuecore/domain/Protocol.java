/**
 * <p>Title: Protocol Class>
 * <p>Description:  Models the Protocol information. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 * Created on May 4, 2005
 */
package edu.wustl.catissuecore.domain;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import edu.wustl.catissuecore.actionForm.AbstractActionForm;

/**
 * Models the Protocol information.
 * @hibernate.class table="CATISSUE_PROTOCOL"
 * @author kapil_kaveeshwar
 */
public class Protocol extends AbstractDomainObject implements Serializable
{
	private static final long serialVersionUID = 1234567890L;

	/**
	 * identifier is a unique id assigned to each Participant.
	 */
	protected Long identifier;
	
	/**
	 * Full protocol title.
	 */
	protected String title;
	
	/**
	 * Abbreviated protocol title.
	 */
	protected String shortTitle;
	
	/**
	 * Date protocol is activated.
	 */
	protected Date startDate;
	
	/**
	 * Date protocol ended.
	 */
	protected Date endDate;
	
	/**
	 * IRB protocol approval number.
	 */
	protected String irbIdentifier;
	
	/**
	 * Number of anticipated subjects to accrue.
	 */
	protected Integer enrollment;
	
	/**
	 * Specifies if the protocol involves biospecimen collection.
	 */
	protected Boolean collection;
	
	/**
	 * Specifies if the protocol involves biospecimen collection at multiple sites.
	 */
	protected Boolean multipleSite;
	
	/**
	 * URL to the document containing the actual protocol document.
	 */
	protected String descriptionURL;
	
	/**
	 * The principal investigator of the protocol.
	 */
	protected User principalInvestigator;
	
	/**
	 * The clinical/administrative coordinator for the protocol.
	 */
	protected User coordinator;
	
	/**
	 * The collection of protocol requirements in this protocol.
	 */
	private Collection protocolRequirementCollection = new HashSet();
	
	/**
	 * The collection of accessions associated with in this protocol.
	 */
	private Collection accessionCollection = new HashSet();
	
	/**
	 * The collection of associted study with this protocol.
	 */
	private Collection studyCollection = new HashSet();
	
	/**
	 * Activity Status of specimen, it could be CLOSED, ACTIVE, DISABLED
	 */
	protected ActivityStatus activityStatus;
	
	/**
	 * Returns the identifier assigned to Protocol.
	 * @return Long representing the id assigned to Protocol.
	 * @see #setIdentifier(Long)
	 * @hibernate.id name="identifier" column="IDENTIFIER" type="long" length="30"
	 * unsaved-value="null" generator-class="native"
	 */
	public Long getIdentifier()
	{
		return identifier;
	}

	/**
	 * Sets the identifier assigned to Protocol.
	 * @param identifier the identifier assigned to Protocol.
	 * @see #getIdentifier()
	 */
	public void setIdentifier(Long identifier)
	{
		this.identifier = identifier;
	}

	/**
	 * Returns the full title of this protocol.
	 * @hibernate.property name="title" type="string" column="TITLE" length="50"
	 * @return the full title of this protocol.
	 * @see #setTitle(String)
	 */
	public String getTitle()
	{
		return title;
	}

	/**
	 * Sets the full title of this protocol.
	 * @param title the full title of this protocol.
	 * @see #getTitle()
	 */
	public void setTitle(String title)
	{
		this.title = title;
	}

	/**
	 * Returns the abbreviated protocol title.
	 * @hibernate.property name="shortTitle" type="string" column="SHORT_TITLE" length="50"
	 * @return the abbreviated protocol title.
	 * @see #setShortTitle(String)
	 */
	public String getShortTitle()
	{
		return shortTitle;
	}

	/**
	 * Sets the abbreviated protocol title.
	 * @param shortTitle the abbreviated protocol title.
	 * @see #getShortTitle()
	 */
	public void setShortTitle(String shortTitle)
	{
		this.shortTitle = shortTitle;
	}
	
	/**
	 * Returns the date protocol is activated.
	 * @hibernate.property name="startDate" type="date" column="START_DATE" length="50"
	 * @return the date protocol is activated.
	 * @see #setStartDate(java.util.Date)
	 */
	public java.util.Date getStartDate()
	{
		return startDate;
	}

	/**
	 * Sets the date protocol is activated.
	 * @param startDate the date protocol is activated.
	 * @see #getStartDate()
	 */
	public void setStartDate(java.util.Date startDate)
	{
		this.startDate = startDate;
	}
	
	/**
	 * Returns the date protocol is closed.
	 * @hibernate.property name="endDate" type="date" column="END_DATE" length="50"
	 * @return the date protocol is closed.
	 * @see #setEndDate(java.util.Date)
	 */
	public java.util.Date getEndDate()
	{
		return endDate;
	}

	/**
	 * Sets the date protocol is closed.
	 * @param endDate the date protocol is closed.
	 * @see #getEndDate()
	 */
	public void setEndDate(java.util.Date endDate)
	{
		this.endDate = endDate;
	}
	
	/**
	 * Returns the IRB protocol approval number.
	 * @hibernate.property name="irbIdentifier" type="string" column="IRB_IDENTIFIER" length="50"
	 * @return the IRB protocol approval number.
	 * @see #setIrbIdentifier(String)
	 */
	public String getIrbIdentifier()
	{
		return irbIdentifier;
	}

	/**
	 * Sets the IRB protocol approval number.
	 * @param irbIdentifier the IRB protocol approval number.
	 * @see #getIrbIdentifier()
	 */
	public void setIrbIdentifier(String irbIdentifier)
	{
		this.irbIdentifier = irbIdentifier;
	}

	/**
	 * Returns the number of anticipated subjects to accrue
	 * @hibernate.property name="enrollment" type="int" column="ENROLLMENT" length="50"
	 * @return the number of anticipated subjects to accrue
	 * @see #setEnrollment(java.lang.Integer)
	 */
	public java.lang.Integer getEnrollment()
	{
		return enrollment;
	}

	/**
	 * Sets the number of anticipated subjects to accrue
	 * @param enrollment the number of anticipated subjects to accrue
	 * @see #getEnrollment()
	 */
	public void setEnrollment(java.lang.Integer enrollment)
	{
		this.enrollment = enrollment;
	}
	
	/**
	 * Returns whether the protocol involves biospecimen collection.
	 * @hibernate.property name="collection" type="boolean" column="COLLECTION" length="50"
	 * @return whether the protocol involves biospecimen collection.
	 * @see #setCollection(Boolean)
	 */
	public Boolean getCollection()
	{
		return collection;
	}

	/**
	 * Sets whether the protocol involves biospecimen collection.
	 * @param collection true or false
	 * @see #getCollection()
	 */
	public void setCollection(Boolean collection)
	{
		this.collection = collection;
	}

	/**
	 * Returns whether the protocol involves biospecimen collection at multiple sites.
	 * @hibernate.property name="multipleSite" type="boolean" column="MULTIPLE_SIDE" length="50"
	 * @return the protocol involves biospecimen collection at multiple sites.
	 * @see #setMultipleSite(Boolean)
	 */
	public Boolean getMultipleSite()
	{
		return multipleSite;
	}

	/**
	 * Sets whether the protocol involves biospecimen collection at multiple sites.
	 * @param multipleSite the protocol involves biospecimen collection at multiple sites.
	 * @see #getMultipleSite()
	 */
	public void setMultipleSite(Boolean multipleSite)
	{
		this.multipleSite = multipleSite;
	}

	/**
	 * Returns URL to the document containing the actual protocol document.
	 * @hibernate.property name="descriptionURL" type="string" column="DESCRIPTION_URL" length="100"
	 * @return URL to the document containing the actual protocol document.
	 * @see #setDescriptionURL(String)
	 */
	public String getDescriptionURL()
	{
		return descriptionURL;
	}

	/**
	 * Sets URL to the document containing the actual protocol document
	 * @param descriptionURL URL to the document containing the actual protocol document
	 * @see #getDescriptionURL()
	 */
	public void setDescriptionURL(String descriptionURL)
	{
		this.descriptionURL = descriptionURL;
	}
	
	/**
	 * Returns the principal investigator of the protocol.
	 * @hibernate.many-to-one column="PRINCIPAL_INVESTIGATOR" class="edu.wustl.catissuecore.domain.User"
	 * constrained="true"
	 * @return the principal investigator of the protocol.
	 * @see #setPrincipalInvestigator(User)
	 */
	public User getPrincipalInvestigator()
	{
		return principalInvestigator;
	}

	/**
	 * Sets the principal investigator of the protocol.
	 * @param principalInvestigator the principal investigator of the protocol.
	 * @see #getPrincipalInvestigator()
	 */
	public void setPrincipalInvestigator(User principalInvestigator)
	{
		this.principalInvestigator = principalInvestigator;
	}

	/**
	 * Returns the clinical/administrative coordinator for the protocol.
	 * @hibernate.many-to-one column="COORDINATOR" class="edu.wustl.catissuecore.domain.User"
	 * constrained="true"
	 * @return the clinical/administrative coordinator for the protocol.
	 * @see #setCoordinator(User)
	 */
	public User getCoordinator()
	{
		return coordinator;
	}

	/**
	 * Sets the clinical/administrative coordinator for the protocol.
	 * @param coordinator the clinical/administrative coordinator for the protocol.
	 * @see #getCoordinator()
	 */
	public void setCoordinator(User coordinator)
	{
		this.coordinator = coordinator;
	}

	/**
	 * Returns the collection of protocol requirement in this protocol.
	 * @return the collection of protocol requirement in this protocol.
	 * @hibernate.set name="protocolRequirementCollection" table="CATISSUE_PROTOCOL_REQUIREMENT"
	 * cascade="save-update" inverse="true" lazy="false"
	 * @hibernate.collection-key column="PROTOCOL_ID"
	 * @hibernate.collection-one-to-many class="edu.wustl.catissuecore.domain.ProtocolRequirement"
	 * @see #setProtocolRequirementCollection(Collection)
	 */
	public Collection getProtocolRequirementCollection()
	{
		return protocolRequirementCollection;
	}

	/**
	 * Sets the collection of protocol requirement in this protocol.
	 * @param protocolRequirementCollection the collection of protocol requirement in this protocol.
	 * @see #getProtocolRequirementCollection()
	 */
	public void setProtocolRequirementCollection(java.util.Collection protocolRequirementCollection)
	{
		this.protocolRequirementCollection = protocolRequirementCollection;
	}

	/**
	 * Returns the collection of associted study with this protocol.
	 * @return the collection of associted study with this protocol.
	 * @hibernate.set name="studyCollection" table="CATISSUE_PROTOCOL_STUDY"
	 * cascade="save-update" inverse="true" lazy="false"
	 * @hibernate.collection-key column="PROTOCOL_ID"
	 * @hibernate.collection-many-to-many class="edu.wustl.catissuecore.domain.Study" column="STUDY_ID"
	 * @see #setProtocolRequirementCollection(Collection)
	 */
	public Collection getStudyCollection()
	{
		return studyCollection;
	}

	/**
	 * Sets the collection of associted study with this protocol.
	 * @param studyCollection the collection of associted study with this protocol.
	 * @see #getStudyCollection()
	 */
	public void setStudyCollection(Collection studyCollection)
	{
		this.studyCollection = studyCollection;
	}
	
	/**
	 * Returns the collection of accessions associated with in this protocol.
	 * @return the collection of accessions associated with in this protocol.
	 * @hibernate.set name="accessionCollection" table="CATISSUE_ACCESSION"
	 * cascade="save-update" inverse="true" lazy="false"
	 * @hibernate.collection-key column="PROTOCOL_ID"
	 * @hibernate.collection-one-to-many class="edu.wustl.catissuecore.domain.Accession"
	 * @see #setProtocolRequirementCollection(Collection)
	 */
	public java.util.Collection getAccessionCollection()
	{
		return accessionCollection;
	}

	/**
	 * Sets the collection of accessions associated with in this protocol.
	 * @param accessionCollection the collection of accessions associated with in this protocol.
	 * @see #getAccessionCollection()
	 */
	public void setAccessionCollection(java.util.Collection accessionCollection)
	{
		this.accessionCollection = accessionCollection;
	}
	
	/**
	 * Returns the activity status of the Protocol.
	 * @hibernate.many-to-one column="ACTIVITY_STATUS_ID" class="edu.wustl.catissuecore.domain.ActivityStatus" constrained="true"
	 * @return Returns the activity status of the Protocol.
	 * @see #setActivityStatus(ActivityStatus)
	 */
	public ActivityStatus getActivityStatus()
	{
		return activityStatus;
	}

	/**
	 * Sets the activity status of the Protocol.
	 * @param activityStatus activity status of the Protocol.
	 * @see #getActivityStatus()
	 */
	public void setActivityStatus(ActivityStatus activityStatus)
	{
		this.activityStatus = activityStatus;
	}
	
	
    /* (non-Javadoc)
     * @see edu.wustl.catissuecore.domain.AbstractDomainObject#setAllValues(edu.wustl.catissuecore.actionForm.AbstractActionForm)
     */
    public void setAllValues(AbstractActionForm abstractForm)
    {
     
    }
    
}