/**
 * <p>Title: SignUpUser </p>
 * <p>Description: A person who interacts with the caTISSUE Core 
 * data system and/or participates in the process of biospecimen 
 * collection, processing, or utilization.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 */

package edu.wustl.catissuecore.domain;

import java.util.Calendar;
import java.util.Date;
import java.io.Serializable;

import edu.wustl.catissuecore.actionForm.AbstractActionForm;
import edu.wustl.catissuecore.actionForm.UserForm;
import edu.wustl.common.util.logger.Logger;

/**
 * @hibernate.class table="CATISSUE_SIGNUP_USER"
 * @author gautam_shetty
 */
public class SignUpUser extends AbstractDomainObject implements Serializable
{

    /**
     * System generated unique systemIdentifier.
     */
    protected Long systemIdentifier;
    
    /**
     * A string containing the login name of the user.
     */
    protected String loginName = "";

    /**
     * A string containing the Last Name of the user.
     */
    protected String lastName = "";

    /**
     * A string containing the First Name of the user.
     */
    protected String firstName = "";

    /**
     * EmailAddress of the user.
     */
    protected String emailAddress = "";

    /**
     * Date of user registration.
     */
    protected Date startDate;

    /**
     * Institute of the user.
     */
    protected Institution institution = new Institution();

    /**
     * Department of the user.
     */
    protected Department department = new Department();

    /**
	 * Multi-Line Street Address.
	 */
	protected String street;
	
	/**
	 * City
	 */
	protected String city;

	/**
	 * State
	 */
	protected String state;

	/**
	 * Country
	 */
	protected String country;

	/**
	 * Zip code
	 */
	protected String zipCode;

	/**
	 * Phone number
	 */
	protected String phoneNumber;

	/**
	 * Fax number
	 */
	protected String faxNumber;
	
    /**
     * Cancer Research Group to which the user belongs.
     */
    protected CancerResearchGroup cancerResearchGroup = new CancerResearchGroup();

    /**
     * Activity Status of user, it could be CLOSED, ACTIVE, DISABLED.
     */
    protected String activityStatus;

    /**
     * Comments given by the approver.
     */
    protected String comments;

    /**
     * Role id of the user.
     */
    protected String roleId = null;
    
    /**
     * Default constructor.
     */
    public SignUpUser()
    {

    }

    public SignUpUser(UserForm userForm)
    {
        this();
        setAllValues(userForm);
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
     * Returns the firstname assigned to user.
     * @hibernate.property name="firstName" type="string" column="FIRST_NAME" length="50"
     * @return Returns the firstName.
     */
    public String getFirstName()
    {
        return this.firstName;
    }

    /**
     * Returns the lastname assigned to user.
     * @hibernate.property name="lastName" type="string" column="LAST_NAME" length="50"
     * @return Returns the lastName.
     */
    public String getLastName()
    {
        return this.lastName;
    }

    /**
     * Returns the loginname assigned to user.
     * @hibernate.property name="loginName" type="string" column="LOGIN_NAME" length="50" 
     * not-null="true" unique="true"
     * @return Returns the loginName.
     */
    public String getLoginName()
    {
        return this.loginName;
    }

    /**
     * @hibernate.property name="emailAddress" type="string" 
     * column="EMAIL_ADDRESS" length="50" not-null="true" unique="true"
     * Returns the Email Address of the user.
     * @return String representing the emailAddress address of the user.
     */
    public String getEmailAddress()
    {
        return this.emailAddress;
    }

    /**
     * Returns the date when the user registered.
     * @hibernate.property name="startDate" type="date" column="START_DATE"
     * @return Returns the dateAdded.
     */
    public Date getStartDate()
    {
        return this.startDate;
    }
    
    /**
     * @param systemIdentifier The systemIdentifier to set.
     */
    public void setSystemIdentifier(Long systemIdentifier)
    {
        this.systemIdentifier = systemIdentifier;
    }

    /**
     * @param emailAddress The emailAddress to set.
     */
    public void setEmailAddress(String emailAddress)
    {
        this.emailAddress = emailAddress;
    }

    /**
     * @param firstName The firstName to set.
     */
    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }
    
    /**
     * @param lastName The lastName to set.
     */
    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    /**
     * @param loginName The loginName to set.
     */
    public void setLoginName(String loginName)
    {
        this.loginName = loginName;
    }

    /**
     * @param startDate The startDate to set.
     */
    public void setStartDate(Date startDate)
    {
        this.startDate = startDate;
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
	 * Returns the Street of the address.
	 * @hibernate.property name="street" type="string" column="STREET" length="50"
	 * @return Street of the address.
	 */
	public String getStreet()
	{
		return street;
	}

	/**
	 * @param street
	 * Sets the street of the address
	 */
	public void setStreet(String street)
	{
		this.street = street;
	}

	/**
	 * Returns the City of the address.
	 * @hibernate.property name="city" type="string" column="CITY" length="50"
	 * @return City of the address.
	 */
	public String getCity()
	{
		return city;
	}

	/**
	 * @param city
	 * set the City of the address.
	 */
	public void setCity(String city)
	{
		this.city = city;
	}

	/**
	 * Returns the state of the address.
	 * @hibernate.property name="state" type="string" column="STATE" length="50"
	 * @return state of the address.
	 */
	public String getState()
	{
		return state;
	}

	/**
	 * @param state
	 * set the state of the address.
	 */
	public void setState(String state)
	{
		this.state = state;
	}

	/**
	 * Returns the Country of the address.
	 * @hibernate.property name="country" type="string" column="COUNTRY" length="50"
	 * @return country of the address.
	 */
	public String getCountry()
	{
		return country;
	}

	/**
	 * @param country
	 * set the country of the address.
	 */
	public void setCountry(String country)
	{
		this.country = country;
	}

	/**
	 * Returns the zipcode of the address.
	 * @hibernate.property name="zipCode" type="string" column="ZIPCODE" length="30"
	 * @return zipCode of the address.
	 */
	public String getZipCode()
	{
		return zipCode;
	}

	/**
	 * @param zipCode
	 * set the zipCode of the address.
	 */
	public void setZipCode(String zipCode)
	{
		this.zipCode = zipCode;
	}

	/**
	 * Returns the associated phonenumber.
	 * @hibernate.property name="phoneNumber" type="string" column="PHONE_NUMBER" length="50"
	 * @return phoneNumber of the address.
	 */
	public String getPhoneNumber()
	{
		return phoneNumber;
	}

	/**
	 * @param phoneNumber
	 * set the phoneNumber of the address.
	 */
	public void setPhoneNumber(String phoneNumber)
	{
		this.phoneNumber = phoneNumber;
	}

	/**
	 * Returns the faxnumber of the address.
	 * @hibernate.property name="faxNumber" type="string" column="FAX_NUMBER" length="50"
	 * @return faxNumber of the address.
	 */
	public String getFaxNumber()
	{
		return faxNumber;
	}

	/**
	 * @param faxNumber associated fax Number. 
	 * set the faxNumber of the address.
	 */
	public void setFaxNumber(String faxNumber)
	{
		this.faxNumber = faxNumber;
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
     * @return Returns the roleId.
     */
    public String getRoleId()
    {
        return roleId;
    }

    /**
     * @param roleId The roleId to set.
     */
    public void setRoleId(String roleId)
    {
        this.roleId = roleId;
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
            
            this.setLoginName(uform.getEmailAddress());
            this.setLastName(uform.getLastName());
            this.setFirstName(uform.getFirstName());
            this.setEmailAddress(uform.getEmailAddress());
            this.institution.setSystemIdentifier(new Long(uform
                    .getInstitutionId()));

            this.department.setSystemIdentifier(new Long(uform
                    .getDepartmentId()));
            this.cancerResearchGroup.setSystemIdentifier(new Long(uform
                    .getCancerResearchGroupId()));

            this.activityStatus = uform.getActivityStatus();
            this.roleId = uform.getRole();
            this.setStreet(uform.getStreet());
            this.setCity(uform.getCity());
            this.setState(uform.getState());
            this.setCountry(uform.getCountry());
            this.setZipCode(uform.getZipCode());
            this.setPhoneNumber(uform.getPhoneNumber());
            this.setFaxNumber(uform.getFaxNumber());
            this.comments = uform.getComments();
            this.setStartDate(Calendar.getInstance().getTime());
        }
        catch (Exception excp)
        {
            Logger.out.error(excp.getMessage());
        }
    }
}