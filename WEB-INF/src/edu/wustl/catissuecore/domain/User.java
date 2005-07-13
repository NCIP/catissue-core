/**
 * <p>Title: User Class>
 * <p>Description:  Models the User information. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Kapil Kaveeshwar
 * @version 1.00
 */

package edu.wustl.catissuecore.domain;

import java.io.Serializable;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

import edu.wustl.catissuecore.actionForm.AbstractActionForm;
import edu.wustl.catissuecore.actionForm.UserForm;
import edu.wustl.common.util.logger.Logger;

/**
 * Models the User information.
 * @hibernate.class table="CATISSUE_USER"
 */
public class User extends AbstractDomainObject implements Serializable
{
    /**
     * id used by hibernate for as unique identifier
     */
    protected Long identifier;

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
    protected Institute institute = new Institute();

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
     * Date since the user is Member of the system.
     */
    protected Date memberSince;

    /**
     * Date, when user was added to the system
     */
    protected Date dateAdded;

    /**
     * Activity Status of user, it could be CLOSED, ACTIVE, DISABLED
     */
    protected ActivityStatus activityStatus = new ActivityStatus();

    /**
     * A string containing the Email Address of the user.
     */
    private String emailAddress = null;

    /**
     * A role associated with user. 
     * */
    protected Role role = new Role();
    
    /**
     * Comments given by the approver.
     */
    protected Clob commentClob;
    
    /**
     * Comments given by the approver. 
     */
    protected String commentString;

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
     * Returns the unique identifier assigned to user.
     * @hibernate.id name="identifier" column="IDENTIFIER" type="long" length="30"
     * unsaved-value="null" generator-class="native"
     * @return returns a unique identifier assigned to the user.
     * @see #setIdentifier(int)
     * */
    public Long getIdentifier()
    {
        return (this.identifier);
    }

    /**
     * Sets an identifier for the user.
     * @param identifier identifier for the user.
     * @see #getIdentifier()
     * */
    public void setIdentifier(Long identifier)
    {
        this.identifier = identifier;
    }

    /**
     * Returns the last name of the user
     * @hibernate.property name="lastName" type="string" 
     * column="LAST_NAME" length="50"
     * @return Returns the last name of the user
     * @see #setFirstName(String)
     */
    public String getLastName()
    {
        return (this.lastName);
    }

    /**
     * Sets the last name of the user.
     * @param lastName Last Name of the user
     * @see #getFirstName()
     */
    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    /**
     * Returns the first name of the user.
     * @hibernate.property name="firstName" type="string" 
     * column="FIRST_NAME" length="50"
     * @return Returns the first name of the user.
     * @see #setFirstName(String)
     */
    public String getFirstName()
    {
        return (this.firstName);
    }

    /**
     * Sets the first name of the user.
     * @param firstName First name of the user.
     * @see #getFirstName()
     */
    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    /**
     * Returns the institute of the user.
     * @hibernate.many-to-one column="INSTITUTE_ID" 
     * class="edu.wustl.catissuecore.domain.Institute" constrained="true"
     * @return Returns the institute of the user.
     * @see #setInstitute(Institute)
     */
    public Institute getInstitute()
    {
        return (this.institute);
    }

    /**
     * Sets the institute name of the user.
     * @param institute Institute of the user.
     * @see #getInstitute()
     */
    public void setInstitute(Institute institute)
    {
        this.institute = institute;
    }

    /**
     * Returns the department of the user.
     * @hibernate.many-to-one column="DEPARTMENT_ID" 
     * class="edu.wustl.catissuecore.domain.Department" constrained="true"
     * @return Returns the department of the user.
     * @see #setInstitute(Institute)
     */
    public Department getDepartment()
    {
        return (this.department);
    }

    /**
     * Sets the department of the user.
     * @param department Department of the user.
     * @see #getDepartment()
     */
    public void setDepartment(Department department)
    {
        this.department = department;
    }

    /**
     * Returns the address of the user.
     * @hibernate.many-to-one column="ADDRESS_ID"
     * class="edu.wustl.catissuecore.domain.Address" constrained="true"
     * @return Returns the address of the user.
     * @see #setAddress(Institute)
     */
    public Address getAddress()
    {
        return address;
    }

    /**
     * Sets the address of the user.
     * @param address Address of the user.
     * @see #getAddress()
     */
    public void setAddress(Address address)
    {
        this.address = address;
    }

    /**
     * Returns the login name of the user.
     * @hibernate.property name="login" type="string"
     * column="LOGIN" length="50" not-null="true" unique="true" 
     * @return Returns login name of the user
     * @see #setLoginName(String)
     */
    public String getLoginName()
    {
        return (this.loginName);
    }

    /**
     * Sets the login name of this user
     * @param login login name of the user.
     * @see #getLoginName()
     */
    public void setLoginName(String loginName)
    {
        this.loginName = loginName;
    }

    /**
     * Returns the activity status of the user.
     * @hibernate.many-to-one column="ACTIVITY_STATUS_ID" 
     * class="edu.wustl.catissuecore.domain.ActivityStatus" constrained="true"
     * @return Returns the activity status of the user.
     * @see #setActivityStatus(ActivityStatus)
     */
    public ActivityStatus getActivityStatus()
    {
        return activityStatus;
    }

    /**
     * Sets the activity status of the user.
     * @param activityStatus activity status of the user.
     * @see #getActivityStatus()
     */
    public void setActivityStatus(ActivityStatus activityStatus)
    {
        this.activityStatus = activityStatus;
    }

    /**
     * Returns the date when the user is added to the system.
     * @hibernate.property name="dateAdded" column="DATE_ADDED" type="date" 
     * @return Returns the date when the user is added to the system.  
     */
    public Date getDateAdded()
    {
        return dateAdded;
    }

    /**
     * Sets the date when the user is added to the system.
     * @param startDate Date of on which user is added to the system.
     */
    public void setDateAdded(Date dateAdded)
    {
        this.dateAdded = dateAdded;
    }

    /**
     * Returns the date when the user is added to the system.
     * @hibernate.property name="memberSince" column="MEMBER_SINCE" type="date" 
     * @return Returns the date since user is a member of system.  
     */
    public Date getMemberSince()
    {
        return memberSince;
    }

    /**
     * Sets the date since user is a member of the system.
     * @param memberSince Date since user is a member of the system.
     */
    public void setMemberSince(Date memberSince)
    {
        this.memberSince = memberSince;
    }

    /**
     * Returns the password of the user
     * @hibernate.property name="password" type="string" 
     * column="PASSWORD" length="50"
     * @return Returns the password of the user
     * @see #setPassword(String)
     */
    public String getPassword()
    {
        return password;
    }

    /**
     * Sets the password of the user.
     * @param password Password of the user.
     * @see #getPassword()
     */
    public void setPassword(String password)
    {
        this.password = password;
    }

    /**
     * @hibernate.property name="emailAddress" type="string" 
     * column="EMAIL" length="50" not-null="true" unique="true"
     * Returns the Email Address of the user.
     * @return String representing the emailAddress address of the user.
     */
    public String getEmailAddress()
    {
        return (this.emailAddress);
    }

    /**
     * Sets the emailAddress address of the user.
     * @param emailAddress String representing emailAddress address of the user.
     * @see #getEmailAddress()
     */
    public void setEmailAddress(String email)
    {
        this.emailAddress = email;
    }

    /**
     * Returns the role associated with user.
     * @hibernate.many-to-one column="ROLE_ID" 
     * class="edu.wustl.catissuecore.domain.Role" constrained="true"
     * @return the role associated with user.
     * @see #setRole(Role)
     */
    public Role getRole()
    {
        return role;
    }

    /**
     * Sets a collection of roles belongs to the user.
     * @param roleCollection Collection of roles belongs to the user.
     * @see #getRole()
     */
    public void setRole(Role role)
    {
        this.role = role;
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
            commentString = "";
            commentClob = null;
        }
        else
        {
            this.commentClob = commentClob;
            this.commentString = commentClob.getSubString(1L,(int)commentClob.length());
        }
    }
    
    /**
     * Returns the comments given by the approver. 
     * @hibernate.property name="commentString" type="string" 
     * column="STATUSCOMMENT" length="2000" 
     * @return the comments given by the approver.
     * @see #setCommentString(String)
     */
    public String getCommentString()
    {
        return commentString;
    }
    
    /**
     * Sets the commnets given by the approver.
     * @param commentString The commentString to set.
     * @see #getCommentString()
     */
    public void setCommentString(String commentString)
    {
        if (commentString == null)
        {
            commentString = "";
        }
        else
        {
            this.commentString = commentString;
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
            this.identifier = new Long(uform.getIdentifier());
            this.loginName = uform.getLoginName();
            this.lastName = uform.getLastName();
            this.firstName = uform.getFirstName();
            this.loginName = uform.getLoginName();
            this.institute.setName(uform.getInstitution());
            this.department.setName(uform.getDepartment());
            this.role.setName(uform.getRole());
            this.emailAddress = uform.getEmailAddress();
            
            activityStatus.status = uform.getActivityStatus();
            address.setStreet(uform.getStreet());
            address.setCity(uform.getCity());
            address.setState(uform.getState());
            address.setCountry(uform.getCountry());
            address.setZip(uform.getZipCode());
            address.setPhone(uform.getPhoneNumber());
            address.setFax(uform.getFaxNumber());
        }
        catch (Exception excp)
        {
            Logger.out.error(excp.getMessage());
        }
    }
}