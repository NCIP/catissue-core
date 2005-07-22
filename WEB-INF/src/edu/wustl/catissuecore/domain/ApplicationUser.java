/**
 * <p>Title: ApplicationUser Class</p>
 * <p>Description: A person who interacts with the caTISSUE Core 
 * data system and/or participates in the process of biospecimen 
 * collection, processing, or utilization.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Mandar Deshmukh
 * @version 1.00
 */

package edu.wustl.catissuecore.domain;

import java.io.Serializable;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;

import edu.wustl.catissuecore.actionForm.AbstractActionForm;
import edu.wustl.catissuecore.actionForm.UserForm;
import edu.wustl.common.util.logger.Logger;

/**
 * A person who interacts with the caTISSUE Core data system 
 * and/or participates in the process of biospecimen collection, 
 * processing, or utilization.
 * @hibernate.class table="CATISSUE_USER"
 */
public class ApplicationUser extends AbstractDomainObject implements Serializable
{
    /**
	 * System generated unique systemIdentifier.
	 */
	protected Long systemIdentifier;
	
	/**
	 * ApplicationUser object in the CSM.
	 */
	protected gov.nih.nci.security.authorization.domainobjects.User user = 
	    		new gov.nih.nci.security.authorization.domainobjects.User(); 

    /**
     * Institute of the user.
     */
    protected Institution institution = new Institution();

    /**
     * Department of the user.
     */
    protected Department department = new Department();

    /**
     * Contact address of the ApplicationUser.
     */
    protected Address address = new Address();

    /**
	 * Cancer Research Group to which the user belongs.
	 */
	private CancerResearchGroup cancerResearchGroup = new CancerResearchGroup();
	
	/**
     * Activity Status of user, it could be CLOSED, ACTIVE, DISABLED
     */
    protected String activityStatus;
    
    /**
     * Comments given by the approver.
     */
    protected Clob commentClob;
    
    /**
     * Comments given by the approver. 
     */
    protected String comments;
    
    /**
     * Set of collection protocol.
     * */
    protected Collection collectionProtocolCollection = new HashSet();
    
    /**
     * @hibernate.many-to-one column="CSM_USER_ID" class="gov.nih.nci.security.authorization.domainobjects.User"
	 * constrained="true"
     * @return Returns the user.
     */
    public gov.nih.nci.security.authorization.domainobjects.User getUser()
    {
        return user;
    }
    /**
     * @param user The user to set.
     */
    public void setUser(
            gov.nih.nci.security.authorization.domainobjects.User user)
    {
        this.user = user;
    }
    /**
     * Initialize a new ApplicationUser instance.
     * Note: Hibernate invokes this constructor through reflection API.  
     */
    public ApplicationUser()
    {
        this.user.setStartDate(Calendar.getInstance().getTime());
    }

    /**
     * This Constructor Copies the data from an UserForm object to a ApplicationUser object.
     * @param user An UserForm object containing the information about the user.  
     */
    public ApplicationUser(UserForm uform)
    {
    	this();
        setAllValues(uform);
    }

    /**
	 * Returns the systemIdentifier assigned to user.
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
	 * Returns the activitystatus of the user.
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

	/**
	 * Returns the department of the user.
	 * @hibernate.many-to-one column="DEPARTMENT_ID" class="edu.wustl.catissuecore.domain.Department"
	 * constrained="true"
	 * @return the department of the user.
	 */
	public Department getDepartment()
	{
		return department;
	}

	/**
	 * @param department The department to set.
	 */
	public void setDepartment(Department department)
	{
		this.department = department;
	}

	/**
	 * Returns the cancerResearchGroup of the user.
	 * @hibernate.many-to-one column="CANCER_RESEARCH_GROUP_ID" class="edu.wustl.catissuecore.domain.CancerResearchGroup"
	 * constrained="true"
	 * @return the cancerResearchGroup of the user.
	 */
	public CancerResearchGroup getCancerResearchGroup()
	{
		return cancerResearchGroup;
	}

	/**
	 * @param cancerResearchGroup The cancerResearchGroup to set.
	 */
	public void setCancerResearchGroup(CancerResearchGroup cancerResearchGroup)
	{
		this.cancerResearchGroup = cancerResearchGroup;
	}

	/**
	 * Returns the institution of the user.
	 * @hibernate.many-to-one column="INSTITUTION_ID" class="edu.wustl.catissuecore.domain.Institution"
	 * constrained="true"
	 * @return the institution of the user.
	 */
	public Institution getInstitution()
	{
		return institution;
	}

	/**
	 * @param institution The institution to set.
	 */
	public void setInstitution(Institution institution)
	{
		this.institution = institution;
	}

	/**
	 * Returns the address of the user.
	 * @hibernate.many-to-one column="ADDRESS_ID" class="edu.wustl.catissuecore.domain.Address"
	 * constrained="true"
	 * @return the address of the user.
	 */

	public Address getAddress()
	{
		return address;
	}

	/**
	 * @param address The address to set.
	 */
	public void setAddress(Address address)
	{
		this.address = address;
	}

	/**
	 * @return Returns the collectionProtocolCollection.
	 * @hibernate.set name="collectionProtocolCollection" table="CATISSUE_COLLECTION_COORDINATORS" 
	 * cascade="save-update" inverse="true" lazy="false"
	 * @hibernate.collection-key column="USER_ID"
	 * @hibernate.collection-many-to-many class="edu.wustl.catissuecore.domain.CollectionProtocol" column="COLLECTION_PROTOCOL_ID"
	 */
	public Collection getCollectionProtocolCollection() 
	{
		return collectionProtocolCollection;
	}
	
	/**
	 * @param collectionProtocolCollection The collectionProtocolCollection to set.
	 */
	public void setCollectionProtocolCollection(Collection collectionProtocolCollection) 
	{
		this.collectionProtocolCollection = collectionProtocolCollection;
	}
	
	/**
     * Returns the comments given by the approver. 
     * @return the comments given by the approver.
     * @see #setCommentClob(String)
     */
    public Clob getCommentClob()
    {
        return commentClob;
    }
    
    /**
     * Sets the comments given by the approver.
     * @param comments the comments given by the approver.
     * @see #getCommentClob() 
     */
    public void setCommentClob(Clob commentClob) throws SQLException
    {
        if (commentClob == null)
        {
            comments = "";
            commentClob = null;
        }
        else
        {
            this.commentClob = commentClob;
            this.comments = commentClob.getSubString(1L,(int)commentClob.length());
        }
    }
    
    /**
     * Returns the comments given by the approver. 
     * @hibernate.property name="comments" type="string" 
     * column="STATUS_COMMENT" length="2000" 
     * @return the comments given by the approver.
     * @see #setComments(String)
     */
    public String getComments()
    {
        return comments;
    }
    
    /**
     * Sets the commnets given by the approver.
     * @param comments The comments to set.
     * @see #getComments()
     */
    public void setComments(String commentString)
    {
        if (commentString == null)
        {
            commentString = "";
        }
        else
        {
            this.comments = commentString;
        }
    }
    
    /**
     * This function Copies the data from an UserForm object to a ApplicationUser object.
     * @param user An UserForm object containing the information about the user.  
     * */
    public void setAllValues(AbstractActionForm abstractForm)
    {
        try
        {
            UserForm uform = (UserForm) abstractForm;
            this.systemIdentifier = new Long(uform.getSystemIdentifier());
            
            this.user.setLoginName(uform.getLoginName());
            this.user.setLastName(uform.getLastName());
            this.user.setFirstName(uform.getFirstName());
            this.user.setEmailId(uform.getEmailAddress());
            this.institution.setName(uform.getInstitution());
            
            this.department.setName(uform.getDepartment());
            this.cancerResearchGroup.setName(uform.getCancerResearchGroup());
            
            this.activityStatus = uform.getActivityStatus();
            this.address.setStreet(uform.getStreet());
            this.address.setCity(uform.getCity());
            this.address.setState(uform.getState());
            this.address.setCountry(uform.getCountry());
            this.address.setZipCode(uform.getZipCode());
            this.address.setPhoneNumber(uform.getPhoneNumber());
            this.address.setFaxNumber(uform.getFaxNumber());
        }
        catch (Exception excp)
        {
            Logger.out.error(excp.getMessage());
        }
    }
}