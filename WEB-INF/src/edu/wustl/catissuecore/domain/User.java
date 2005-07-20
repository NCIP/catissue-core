/**
 * <p>Title: User Class</p>
 * <p>Description: A person who interacts with the caTISSUE Core data system and/or participates in the process of biospecimen collection, processing, or utilization.</p>
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
import java.util.Date;
import java.util.HashSet;

import edu.wustl.catissuecore.actionForm.AbstractActionForm;
import edu.wustl.catissuecore.actionForm.UserForm;
import edu.wustl.common.util.logger.Logger;

/**
 * A person who interacts with the caTISSUE Core data system and/or participates in the process of biospecimen collection, processing, or utilization.
 * @hibernate.class table="CATISSUE_USER"
 */
public class User extends AbstractDomainObject implements Serializable
{
    /**
	 * System generated unique systemIdentifier.
	 */
	protected Long systemIdentifier;

    /**
     * A string containing the Last Name of the user.
     */
    protected String lastName = "";

    /**
     * A string containing the First Name of the user.
     */
    protected String firstName = "";

    /**
     * Institute of the user.
     */
    protected Institution institution = new Institution();

    /**
     * Department of the user.
     */
    protected Department department = new Department();

    /**
     * Contact address of the User.
     */
    protected Address address = new Address();

    /**
     * A string containing the login name of the user.
     */
    protected String loginName = "";

    /**
     * A string containing the password in increpted format of the user.
     */
    protected String password;

    /**
     * Date, when user was added to the system
     */
    protected Date dateAdded;

    /**
     * Activity Status of user, it could be CLOSED, ACTIVE, DISABLED
     */
    protected String activityStatus;

    /**
	 * Cancer Research Group to which the user belongs.
	 */
	private CancerResearchGroup cancerResearchGroup;
    
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
     * Initialize a new User instance. 
     * Note: Hibernate invokes this constructor through reflection API.  
     */
    public User()
    {
    	this.dateAdded = Calendar.getInstance().getTime();
    }

    /**
     * This Constructor Copies the data from an UserForm object to a User object.
     * @param user An UserForm object containing the information about the user.  
     */
    public User(UserForm uform)
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
	 * @param identifier The identifier to set.
	 */
	public void setSystemIdentifier(Long systemIdentifier)
	{
		this.systemIdentifier = systemIdentifier;
	}

	/**
	 * Returns the lastname assigned to user.
	 * @hibernate.property name="lastName" type="string" column="LAST_NAME" length="50"
	 * @return Returns the lastName.
	 */
	public String getLastName()
	{
		return lastName;
	}

	/**
	 * @param lastName The lastName of user.
	 */
	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}

	/**
	 * Returns the firstname assigned to user.
	 * @hibernate.property name="firstName" type="string" column="FIRST_NAME" length="50"
	 * @return Returns the firstName.
	 */
	public String getFirstName()
	{
		return firstName;
	}

	/**
	 * @param firstName The firstName of user.
	 */
	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}

	/**
	 * Returns the loginname assigned to user.
	 * @hibernate.property name="loginName" type="string" column="LOGIN_NAME" length="50" 
	 * not-null="true" unique="true"
	 * @return Returns the loginName.
	 */
	public String getLoginName()
	{
		return loginName;
	}

	/**
	 * @param loginName The loginName of user.
	 */
	public void setLoginName(String loginName)
	{
		this.loginName = loginName;
	}

	/**
	 * Returns the password assigned to user.
	 * @hibernate.property name="password" type="string" column="PASSWORD" length="50"
	 * @return Returns the password.
	 */
	public String getPassword()
	{
		return password;
	}

	/**
	 * @param password The password to set.
	 */
	public void setPassword(String password)
	{
		this.password = password;
	}

	/**
	 * Returns the date when the user is added to the system.
	 * @hibernate.property name="dateAdded" type="date" column="DATE_ADDED"
	 * @return Returns the dateAdded.
	 */
	public Date getDateAdded()
	{
		return dateAdded;
	}

	/**
	 * @param dateAdded The date when user is added to the system.
	 */
	public void setDateAdded(Date dateAdded)
	{
		this.dateAdded = dateAdded;
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
     * This function Copies the data from an UserForm object to a User object.
     * @param user An UserForm object containing the information about the user.  
     * */
    public void setAllValues(AbstractActionForm abstractForm)
    {
        try
        {
            UserForm uform = (UserForm) abstractForm;
            this.systemIdentifier = new Long(uform.getSystemIdentifier());
            this.loginName = uform.getLoginName();
            this.lastName = uform.getLastName();
            this.firstName = uform.getFirstName();
            this.loginName = uform.getLoginName();
            this.institution.setName(uform.getInstitution());
            this.department.setName(uform.getDepartment());
            this.cancerResearchGroup.setName(uform.getCancerResearchGroup());
            
            activityStatus = uform.getActivityStatus();
            address.setStreet(uform.getStreet());
            address.setCity(uform.getCity());
            address.setState(uform.getState());
            address.setCountry(uform.getCountry());
            address.setZipCode(uform.getZipCode());
            address.setPhoneNumber(uform.getPhoneNumber());
            address.setFaxNumber(uform.getFaxNumber());
        }
        catch (Exception excp)
        {
            Logger.out.error(excp.getMessage());
        }
    }
}