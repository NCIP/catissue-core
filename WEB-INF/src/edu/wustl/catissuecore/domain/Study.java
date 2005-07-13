/**
 * <p>Title: Study Class>
 * <p>Description:  Models the Study information. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 */

package edu.wustl.catissuecore.domain;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import edu.wustl.catissuecore.actionForm.AbstractActionForm;

/**
 * Models the Study information. 
 * @hibernate.class table="CATISSUE_STUDY"
 * @author gautam_shetty
 */
public class Study extends AbstractDomainObject implements java.io.Serializable
{

    private static final long serialVersionUID = 1234567890L;

    /**
     * id used by hibernate for as unique identifier
     * */
    protected Long identifier;

    /**
     * The principal investigator of the study.
     */
    protected User principalInvestigator;
    
    /**
     * The administrative coordinator for the study.
     */
    protected User coordinator;
    
    /**
     * Full study title.
     */
    protected String title;
    
    /**
     * Abbreviated study title.
     */
    protected String shortTitle;
    
    /**
     * IRB study approval number.
     */
    protected String irbIdentifier;
    
    /**
     * Date study is activated.
     */
    protected Date startDate;
    
    /**
     * Date study is closed.
     */
    protected Date endDate;
    
    /**
     * Activity Status of Study, it could be CLOSED, ACTIVE, DISABLED.
     */
    protected ActivityStatus activityStatus;
    
    /**
     * Number of anticipated cases need for the study.
     */
    protected Integer enrollment;
    
    /**
     * URL to the document that describes the study.
     */
    protected String descriptionURL;
    
    /**
     * A dimension table that lists the biospecimen requirements for this study.
     */
    private Collection studyRequirementCollection = new HashSet();

    /**
     * The distributions associated with this study.
     */
    private Collection distributionCollection = new HashSet();
    
    /**
     * The collection of associated protocols with this study.
     */
    private Collection protocolCollection = new HashSet();
    
    /**
     * Returns the unique identifier assigned to study.
     * @hibernate.id name="identifier" column="IDENTIFIER" type="long" length="30"
     * unsaved-value="null" generator-class="native"
     * @return returns a unique identifier assigned to the user.
     * @see #setIdentifier(int)
     * */
    public Long getIdentifier()
    {
        return identifier;
    }

    /**
     * Sets the unique identifier assigned to study.
     * @param identifier the unique identifier assigned to study.
     * @see #getIdentifier()
     */
    public void setIdentifier(Long identifier)
    {
        this.identifier = identifier;
    }

    /**
     * Returns the principal investigator of the study.
     * @hibernate.many-to-one column="PRINCIPAL_INVESTIGATOR" 
     * class="edu.wustl.catissuecore.domain.User" constrained="true"
     * @return the principal investigator of the study.
     * @see #setPrincipalInvestigator(User)
     */
    public User getPrincipalInvestigator()
    {
        return principalInvestigator;
    }

    /**
     * Sets the principal investigator of the study.
     * @param principalInvestigator the principal investigator of the study.
     * @see #getPrincipalInvestigator()
     */
    public void setPrincipalInvestigator(User principalInvestigator)
    {
        this.principalInvestigator = principalInvestigator;
    }

    /**
     * Returns the administrative coordinator for the study.
     * @hibernate.many-to-one column="COORDINATOR_IDENTIFIER" 
     * class="edu.wustl.catissuecore.domain.User" constrained="true"
     * @return the administrative coordinator for the study.
     * @see #setCoordinator(User)
     */
    public User getCoordinator()
    {
        return coordinator;
    }

    /**
     * Sets the administrative coordinator for the study.
     * @param coordinator the administrative coordinator for the study.
     * @see #getCoordinator()
     */
    public void setCoordinator(User coordinator)
    {
        this.coordinator = coordinator;
    }

    /**
     * Returns the full study title.
     * @hibernate.property name="title" type="string" 
     * column="TITLE" length="200"
     * @return the full study title.
     * @see #setTitle(String)
     */
    public String getTitle()
    {
        return title;
    }

    /**
     * Sets the full study title.
     * @param title the full study title.
     * @see #getTitle()
     */
    public void setTitle(String title)
    {
        this.title = title;
    }

    /**
     * Returns the abbreviated study title.
     * @hibernate.property name="shortTitle" type="string" 
     * column="SHORT_TITLE" length="100"
     * @return the abbreviated study title.
     * @see #setShortTitle(String)
     */
    public String getShortTitle()
    {
        return shortTitle;
    }

    /**
     * Sets the abbreviated study title.
     * @param shortTitle the abbreviated study title.
     * @see #getShortTitle()
     */
    public void setShortTitle(String shortTitle)
    {
        this.shortTitle = shortTitle;
    }

    /**
     * Returns the IRB study approval number.
     * @hibernate.property name="irbIdentifier" type="string" 
     * column="IRB_IDENTIFIER" length="100"
     * @return the IRB study approval number.
     * @see #setIrbIdentifier(String)
     */
    public String getIrbIdentifier()
    {
        return irbIdentifier;
    }

    /**
     * Sets the IRB study approval number.
     * @param irbIdentifier the IRB study approval number.
     * @see #getIrbIdentifier()
     */
    public void setIrbIdentifier(String irbIdentifier)
    {
        this.irbIdentifier = irbIdentifier;
    }

    /**
     * Returns the date study is activated.
     * @hibernate.property name="startDate" column="START_DATE" type="date"
     * @return the date study is activated.
     * @see #setStartDate(Date) 
     */
    public Date getStartDate()
    {
        return startDate;
    }

    /**
     * Sets the date study is activated. 
     * @param startDate the date study is activated.
     * @see #getStartDate()
     */
    public void setStartDate(Date startDate)
    {
        this.startDate = startDate;
    }

    /**
     * Returns the date study is closed.
     * @hibernate.property name="endDate" column="END_DATE" type="date"
     * @return the date study is closed.
     * @see #setEndDate(Date) 
     */
    public Date getEndDate()
    {
        return endDate;
    }

    /**
     * Sets the date study is closed.
     * @param endDate the date study is closed.
     * @see #getEndDate()
     */
    public void setEndDate(Date endDate)
    {
        this.endDate = endDate;
    }

    /**
     * Returns the activity status of the study.
     * @hibernate.many-to-one column="ACTIVITY_STATUS_ID" 
     * class="edu.wustl.catissuecore.domain.ActivityStatus" constrained="true"
     * @return Returns the activity status of the study.
     * @see #setActivityStatus(ActivityStatus)
     */
    public ActivityStatus getActivityStatus()
    {
        return activityStatus;
    }

    /**
     * Sets the activity status of the study.
     * @param activityStatus activity status of the study.
     * @see #ActivityStatus()
     */
    public void setActivityStatus(ActivityStatus activityStatus)
    {
        this.activityStatus = activityStatus;
    }

    /**
     * Returns the number of anticipated cases need for the study.
	 * @hibernate.property name="enrollment" type="int" column="ENROLLMENT" length="50"
	 * @return the number of anticipated cases need for the study.
	 * @see #setEnrollment(Integer)
	 */
    public Integer getEnrollment()
    {
        return enrollment;
    }

    /**
     * Sets the number of anticipated cases need for the study.
     * @param enrollment the number of anticipated cases need for the study.
     * @see #getEnrollment()
     */
    public void setEnrollment(Integer enrollment)
    {
        this.enrollment = enrollment;
    }

    /**
     * Returns the URL to the document that describes the study.
	 * @hibernate.property name="descriptionURL" type="string" column="DESCRIPTION_URL" length="100"
	 * @return the URL to the document that describes the study.
	 * @see #setDescriptionURL(String)
	 */
    public String getDescriptionURL()
    {
        return descriptionURL;
    }

    /**
     * Sets the URL to the document that describes the study.
     * @param descriptionURL the URL to the document that describes the study.
     * @see #getDescriptionURL()
     */
    public void setDescriptionURL(String descriptionURL)
    {
        this.descriptionURL = descriptionURL;
    }

    /**
     * Returns the dimension table that lists the biospecimen requirements for this study.
     * @hibernate.set name="studyRequirementCollection" table="CATISSUE_STUDY_REQUIREMENT"
	 * cascade="save-update" inverse="true" lazy="false"
	 * @hibernate.collection-key column="STUDY_REQUIREMENT_ID"
	 * @hibernate.collection-one-to-many class="edu.wustl.catissuecore.domain.StudyRequirement"
     * @return A dimension table that lists the biospecimen requirements for this study.
     * @see #setStudyRequirementCollection(Collection)
     */
    public Collection getStudyRequirementCollection()
    {

        return studyRequirementCollection;
    }

    /**
     * Sets the dimension table that lists the biospecimen requirements for this study.
     * @param studyRequirementCollection the dimension table that lists the biospecimen requirements for this study.
     * @see #getStudyRequirementCollection()
     */
    public void setStudyRequirementCollection(
            Collection studyRequirementCollection)
    {
        this.studyRequirementCollection = studyRequirementCollection;
    }

    /**
	 * Returns the collection of distribution made for this study.
	 * @return the collection of distribution made for this study.
	 * @hibernate.set name="distributionCollection" table="CATISSUE_DISTRIBUTION"
	 * cascade="save-update" inverse="true" lazy="false"
	 * @hibernate.collection-key column="DISTRIBUTION_ID"
	 * @hibernate.collection-one-to-many class="edu.wustl.catissuecore.domain.Distribution"
	 * @see #setDistributionCollection(Collection)
	 */
    public Collection getDistributionCollection()
    {
        return distributionCollection;
    }

    /**
     * Sets the collection of distribution made for this study.
     * @param distributionCollection the collection of distribution made for this study.
     * @see #getDistributionCollection()
     */
    public void setDistributionCollection(Collection distributionCollection)
    {
        this.distributionCollection = distributionCollection;
    }

    /**
	 * Returns the collection of associated protocols with this study.
	 * @return the collection of associated protocols with this study.
	 * @hibernate.set name="protocolCollection" table="CATISSUE_PROTOCOL_STUDY"
	 * cascade="save-update" inverse="true" lazy="false"
	 * @hibernate.collection-key column="PROTOCOL_ID"
	 * @hibernate.collection-many-to-many class="edu.wustl.catissuecore.domain.Protocol" column="PROTOCOL_ID"
	 * @see #setProtocolCollection(Collection)
	 */
    public Collection getProtocolCollection()
    {

        return protocolCollection;
    }

    /**
     * Sets the collection of associated protocols with this study.
     * @param protocolCollection the collection of associated protocols with this study.
     * @see #getProtocolCollection()
     */
    public void setProtocolCollection(Collection protocolCollection)
    {
        this.protocolCollection = protocolCollection;
    }
    
    /* (non-Javadoc)
     * @see edu.wustl.catissuecore.domain.AbstractDomainObject#setAllValues(edu.wustl.catissuecore.actionForm.AbstractActionForm)
     */
    public void setAllValues(AbstractActionForm abstractForm)
    {
     
    }
}