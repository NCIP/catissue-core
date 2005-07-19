/**
 * <p>Title: User Class</p>
 * <p>Description: A person who interacts with the caTISSUE Core data system and/or participates in the process of biospecimen collection, processing, or utilization.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Mandar Deshmukh
 * @version 1.00
 * Created on July 12, 2005
 */

package edu.wustl.catissuecore.domain;

import java.util.Date;

/**
 * A person who interacts with the caTISSUE Core data system and/or participates in the process of biospecimen collection, processing, or utilization.
 * @hibernate.class table="CATISSUE_USER"
 * @author Mandar Deshmukh
 */
public class User implements java.io.Serializable
{
	private static final long serialVersionUID = 1234567890L;
	
	/**
	 * System generated unique identifier.
	 */
	protected Long systemIdentifier;
	
	/**
	 * Last name of the User.
	 */
	protected String lastName;
	
	/**
	 * First Name of the User. 
	 */
	protected String firstName;
	
	/**
	 * User-friendly text string for system log in.
	 */
	protected String loginName;
	
	/**
	 * Encrypted text string to verify the user identity.
	 */
	protected String password;
	
	/**
	 * Date, when user was added to the system.
	 */
	protected Date dateAdded;
	
	/**
	 * Defines whether this user record can be queried (Active) or not queried (Inactive) by any actor
	 */
	protected String activityStatus;
	
	/**
	 * Department to which the user belongs.
	 */
	private Department department;
	
	/**
	 * Cancer Research Group to which the user belongs.
	 */
	private CancerResearchGroup cancerResearchGroup;
	
	/**
	 * Institution to which the user belongs.
	 */
	private Institution institution;
	
	/**
	 * Address of the user.
	 */
	private edu.wustl.catissuecore.domain.Address address;

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
}