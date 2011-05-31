/**
 * <p>Title: User </p>
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import edu.wustl.catissuecore.actionForm.UserForm;
import edu.wustl.catissuecore.util.SearchUtil;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.actionForm.IValueObject;
import edu.wustl.common.bizlogic.IActivityStatus;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.AssignDataException;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.participant.domain.IUser;
import edu.wustl.common.util.global.Status;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.security.exception.SMException;
import edu.wustl.security.manager.SecurityManagerFactory;
import gov.nih.nci.security.authorization.domainobjects.Role;

/**
 * A person who interacts with the caTISSUE Core data system and/or participates
 * in the process of biospecimen collection, processing, or utilization.
 *
 * @hibernate.class table="CATISSUE_USER"
 */
public class User extends AbstractDomainObject implements Serializable, IActivityStatus, IUser
{

	/**
	 * Common Logger for Login Action.
	 */
	private transient final Logger logger = Logger.getCommonLogger(User.class);

	/**
	 * Serial Version ID of the class.
	 */
	private static final long serialVersionUID = 899748215404484663L;

	/**
	 * System generated unique id.
	 */
	protected Long id;

	/**
	 * A string containing the Last Name of the user.
	 */
	protected String lastName;

	/**
	 * A string containing the First Name of the user.
	 */
	protected String firstName;

	/**
	 * A string containing the login name of the user.
	 */
	protected String loginName;

	/**
	 * EmailAddress of the user.
	 */
	protected String emailAddress;

	/**
	 * Old password of this user.
	 */
	protected String oldPassword;

	/**
	 * EmailAddress Address of the user.
	 */
	protected String newPassword;

	/**
	 * Date of user registration.
	 */
	protected Date startDate;

	/**
	 * Institute of the user.
	 */
	protected Institution institution;

	/**
	 * Department of the user.
	 */
	protected Department department;

	/**
	 * Contact address of the User.
	 */
	protected Address address;

	/**
	 * Cancer Research Group to which the user belongs.
	 */
	protected CancerResearchGroup cancerResearchGroup;

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
	protected String roleId = "";

	/**
	 * Set of collection protocol.
	 */
	protected Collection collectionProtocolCollection = new HashSet();

	/**
	 * String.
	 */
	protected String pageOf;

	/**
	 * Identifier of this user in csm user table.
	 */
	protected Long csmUserId;

	/**
	 * whether user is logging for the first time.
	 */
	protected Boolean firstTimeLogin;

	/**
	 * Set of collection protocol.
	 */
	protected Collection<CollectionProtocol> assignedProtocolCollection = new HashSet<CollectionProtocol>();

	/**
	 * Set of Sites.
	 */
	protected Collection<Site> siteCollection = new HashSet<Site>();

	/**
	 * Set of passwod collection for the user.
	 */
	protected Collection passwordCollection = new HashSet();

	/**
	 * WUSTLkey of a user.
	 */
	private String targetIdpLoginName = null;

	private String targetPassword;

	private String targetIdp;

	public String getTargetIdp()
	{
		return targetIdp;
	}

	public void setTargetIdp(final String targetIdp)
	{
		this.targetIdp = targetIdp;
	}

	public String getTargetPassword()
	{
		return targetPassword;
	}

	public void setTargetPassword(final String targetPassword)
	{
		this.targetPassword = targetPassword;
	}

	/**
	 * Initialize a new User instance. Note: Hibernate invokes this constructor
	 * through reflection API.
	 */
	public User()
	{
		super();
	}

	/**
	 * This Constructor Copies the data from an UserForm object to a User
	 * object.
	 *
	 * @param uform
	 *            - user An UserForm object containing the information about the
	 *            user.
	 * @throws AssignDataException
	 *             AssignDataException
	 */
	public User(final UserForm uform) throws AssignDataException
	{
		this();
		setAllValues(uform);
	}

	/**
	 * Returns the id assigned to user.
	 *
	 * @hibernate.id name="id" column="IDENTIFIER" type="long" length="30"
	 *               unsaved-value="null" generator-class="native"
	 * @hibernate.generator-param name="sequence" value="CATISSUE_USER_SEQ"
	 * @return Returns the id.
	 */
	@Override
	public Long getId()
	{
		return id;
	}

	/**
	 * @param identifier
	 *            The id to set.
	 */
	@Override
	public void setId(final Long identifier)
	{
		id = identifier;
	}

	/**
	 * Returns the password assigned to user.
	 *
	 * @hibernate.property name="emailAddress" type="string"
	 *                     column="EMAIL_ADDRESS" length="255"
	 * @return Returns the password.
	 */
	public String getEmailAddress()
	{
		return emailAddress;
	}

	/**
	 * @param emailAddress
	 *            The emailAddress to set.
	 */
	public void setEmailAddress(final String emailAddress)
	{
		this.emailAddress = emailAddress;
	}

	/**
	 * @return Returns the oldPassword.
	 */
	public String getOldPassword()
	{
		return oldPassword;
	}

	/**
	 * @param oldPassword
	 *            The oldPassword to set.
	 */
	public void setOldPassword(final String oldPassword)
	{
		this.oldPassword = oldPassword;
	}

	// @hibernate.property name="password" type="string" column="PASSWORD"
	// length="50"
	/**
	 * Returns the newPassword assigned to user.
	 *
	 * @return Returns the newPassword.
	 */
	public String getNewPassword()
	{
		return newPassword;
	}

	/**
	 * @param newPassword
	 *            The new Password to set.
	 */
	public void setNewPassword(final String newPassword)
	{
		this.newPassword = newPassword;
	}

	/**
	 * Returns the firstname assigned to user.
	 *
	 * @hibernate.property name="firstName" type="string" column="FIRST_NAME"
	 *                     length="255"
	 * @return Returns the firstName.
	 */
	public String getFirstName()
	{
		return firstName;
	}

	/**
	 * @param firstName
	 *            The firstName to set.
	 */
	public void setFirstName(final String firstName)
	{
		this.firstName = firstName;
	}

	/**
	 * Returns the lastname assigned to user.
	 *
	 * @hibernate.property name="lastName" type="string" column="LAST_NAME"
	 *                     length="255"
	 * @return Returns the lastName.
	 */
	public String getLastName()
	{
		return lastName;
	}

	/**
	 * @param lastName
	 *            The lastName to set.
	 */
	public void setLastName(final String lastName)
	{
		this.lastName = lastName;
	}

	/**
	 * Returns the login name assigned to user.
	 *
	 * @hibernate.property name="loginName" type="string" column="LOGIN_NAME"
	 *                     length="255" not-null="true" unique="true"
	 * @return Returns the loginName.
	 */
	public String getLoginName()
	{
		return loginName;
	}

	/**
	 * @param loginName
	 *            The loginName to set.
	 */
	public void setLoginName(final String loginName)
	{
		this.loginName = loginName;
	}

	/**
	 * Returns the date when the user is added to the system.
	 *
	 * @hibernate.property name="startDate" type="date" column="START_DATE"
	 * @return Returns the dateAdded.
	 */
	public Date getStartDate()
	{
		return startDate;
	}

	/**
	 * @param startDate
	 *            The startDate to set.
	 */
	public void setStartDate(final Date startDate)
	{
		this.startDate = startDate;
	}

	/**
	 * Returns the activity status of the user.
	 *
	 * @hibernate.property name="activityStatus" type="string"
	 *                     column="ACTIVITY_STATUS" length="50"
	 * @return Returns the activityStatus.
	 */
	public String getActivityStatus()
	{
		return activityStatus;
	}

	/**
	 * @param activityStatus
	 *            The activityStatus to set.
	 */
	public void setActivityStatus(final String activityStatus)
	{
		this.activityStatus = activityStatus;
	}

	/**
	 * Returns the department of the user.
	 *
	 * @hibernate.many-to-one column="DEPARTMENT_ID"
	 *                        class="edu.wustl.catissuecore.domain.Department"
	 *                        constrained="true"
	 * @return the department of the user.
	 */
	public Department getDepartment()
	{
		return department;
	}

	/**
	 * @param department
	 *            The department to set.
	 */
	public void setDepartment(final Department department)
	{
		this.department = department;
	}

	/**
	 * Returns the cancerResearchGroup of the user.
	 *
	 * @hibernate.many-to-one column="CANCER_RESEARCH_GROUP_ID" class=
	 *                        "edu.wustl.catissuecore.domain.CancerResearchGroup"
	 *                        constrained="true"
	 * @return the cancerResearchGroup of the user.
	 */
	public CancerResearchGroup getCancerResearchGroup()
	{
		return cancerResearchGroup;
	}

	/**
	 * @param cancerResearchGroup
	 *            The cancerResearchGroup to set.
	 */
	public void setCancerResearchGroup(final CancerResearchGroup cancerResearchGroup)
	{
		this.cancerResearchGroup = cancerResearchGroup;
	}

	/**
	 * Returns the institution of the user.
	 *
	 * @hibernate.many-to-one column="INSTITUTION_ID"
	 *                        class="edu.wustl.catissuecore.domain.Institution"
	 *                        constrained="true"
	 * @return the institution of the user.
	 */
	public Institution getInstitution()
	{
		return institution;
	}

	/**
	 * @param institution
	 *            The institution to set.
	 */
	public void setInstitution(final Institution institution)
	{
		this.institution = institution;
	}

	/**
	 * Returns the address of the user.
	 *
	 * @hibernate.many-to-one column="ADDRESS_ID"
	 *                        class="edu.wustl.catissuecore.domain.Address"
	 *                        constrained="true"
	 * @return the address of the user.
	 */
	public Address getAddress()
	{
		return address;
	}

	/**
	 * @param address
	 *            The address to set.
	 */
	public void setAddress(final Address address)
	{
		this.address = address;
	}

	/**
	 * @return Returns the collectionProtocolCollection.
	 * @hibernate.set name="collectionProtocolCollection"
	 *                table="CATISSUE_COLL_COORDINATORS" cascade="save-update"
	 *                inverse="true" lazy="true"
	 * @hibernate.collection-key column="USER_ID"
	 * @hibernate.collection-many-to-many
	 *                                    class="edu.wustl.catissuecore.domain.CollectionProtocol"
	 *                                    column="COLLECTION_PROTOCOL_ID"
	 */
	public Collection getCollectionProtocolCollection()
	{
		return collectionProtocolCollection;
	}

	/**
	 * @param collectionProtocolCollection
	 *            The collectionProtocolCollection to set.
	 */
	public void setCollectionProtocolCollection(final Collection collectionProtocolCollection)
	{
		this.collectionProtocolCollection = collectionProtocolCollection;
	}

	/**
	 * @return Returns the pageOf.
	 */
	public String getPageOf()
	{
		return pageOf;
	}

	/**
	 * @param pageOf
	 *            The pageOf to set.
	 */
	public void setPageOf(final String pageOf)
	{
		this.pageOf = pageOf;
	}

	/**
	 * Returns the password assigned to user.
	 *
	 * @hibernate.property name="csmUserId" type="long" column="CSM_USER_ID"
	 *                     length="20"
	 * @return Returns the password.
	 */
	public Long getCsmUserId()
	{
		return csmUserId;
	}

	/**
	 * @param csmUserId
	 *            The csmUserId to set.
	 */
	public void setCsmUserId(final Long csmUserId)
	{
		this.csmUserId = csmUserId;
	}

	// /**
	// * Returns the comments given by the approver.
	// * @return the comments given by the approver.
	// * @see #setCommentClob(String)
	// */
	// public Clob getCommentClob()
	// {
	// return commentClob;
	// }
	//
	// /**
	// * Sets the comments given by the approver.
	// * @param comments the comments given by the approver.
	// * @see #getCommentClob()
	// */
	// public void setCommentClob(Clob commentClob) throws SQLException
	// {
	// if (commentClob == null)
	// {
	// comments = "";
	// commentClob = null;
	// }
	// else
	// {
	// this.commentClob = commentClob;
	// this.comments = commentClob.getSubString(1L,(int)commentClob.length());
	// }
	// }

	/**
	 * Returns the comments given by the approver.
	 *
	 * @hibernate.property name="comments" type="string" column="STATUS_COMMENT"
	 *                     length="2000"
	 * @return the comments given by the approver.
	 * @see #setComments(String)
	 */
	public String getComments()
	{
		return comments;
	}

	/**
	 * Sets the comments given by the approver.
	 *
	 * @param commentString
	 *            The comments to set.
	 * @see #getComments()
	 */
	public void setComments(String commentString)
	{
		if (commentString == null)
		{
			commentString = Constants.DOUBLE_QUOTES;
		}
		else
		{
			comments = commentString;
		}
	}

	/**
	 * @return Returns the roleId.
	 */
	public String getRoleId()
	{
		String roleId = this.roleId;

		try
		{
			if (roleId.equals(Constants.DOUBLE_QUOTES) && id != null && id != 0
					&& csmUserId != null)
			{
				final Role role = SecurityManagerFactory.getSecurityManager()
						.getUserRole(csmUserId);
				if (role != null && role.getId() != null)
				{
					roleId = role.getId().toString();
				}
			}
		}
		catch (final SMException e)
		{
			logger.error(e.getMessage(), e);
		}

		return roleId;
	}

	/**
	 * @param roleId
	 *            The roleId to set.
	 */
	public void setRoleId(final String roleId)
	{
		if (roleId != null && roleId.equalsIgnoreCase("-1"))
		{
			this.roleId = Constants.NON_ADMIN_USER;
		}
		else if (roleId != null && roleId.equalsIgnoreCase(Constants.ADMIN_USER))
		{
			this.roleId = Constants.SUPER_ADMIN_USER;
		}
		else
		{
			this.roleId = roleId;
		}
	}

	/**
	 * @hibernate.set name="passwordCollection" table="CATISSUE_PASSWORD"
	 *                cascade="save-update" inverse="true" lazy="true"
	 * @hibernate.collection-key column="USER_ID"
	 * @hibernate.collection-one-to-many
	 *                                   class="edu.wustl.catissuecore.domain.Password"
	 * @return Collection.
	 */
	public Collection getPasswordCollection()
	{
		return passwordCollection;
	}

	/**
	 * @param passwordCollection
	 *            of Collection type.
	 */
	public void setPasswordCollection(final Collection passwordCollection)
	{
		this.passwordCollection = passwordCollection;
	}

	/**
	 * This function Copies the data from an UserForm object to a User object.
	 *
	 * @param abstractForm
	 *            - user An UserForm object containing the information about the
	 *            user.
	 * @throws AssignDataException
	 *             : AssignDataException
	 * */
	@Override
	public void setAllValues(final IValueObject abstractForm) throws AssignDataException
	{
		try
		{
			if (SearchUtil.isNullobject(lastName))
			{
				lastName = Constants.DOUBLE_QUOTES;
			}

			if (SearchUtil.isNullobject(firstName))
			{
				firstName = Constants.DOUBLE_QUOTES;
			}

			if (SearchUtil.isNullobject(loginName))
			{
				loginName = Constants.DOUBLE_QUOTES;
			}

			if (SearchUtil.isNullobject(emailAddress))
			{
				emailAddress = Constants.DOUBLE_QUOTES;
			}

			if (SearchUtil.isNullobject(address))
			{
				address = new Address();
			}

			if (SearchUtil.isNullobject(institution))
			{
				institution = new Institution();
			}

			if (SearchUtil.isNullobject(department))
			{
				department = new Department();
			}

			if (SearchUtil.isNullobject(cancerResearchGroup))
			{
				cancerResearchGroup = new CancerResearchGroup();
			}

			if (SearchUtil.isNullobject(firstTimeLogin))
			{
				firstTimeLogin = Boolean.TRUE;
			}

			final UserForm uform = (UserForm) abstractForm;
			pageOf = uform.getPageOf();

			if (pageOf.equals(Constants.PAGE_OF_CHANGE_PASSWORD))
			{
				newPassword = uform.getNewPassword();
				oldPassword = uform.getOldPassword();
			}
			else
			{

				if (!pageOf.equalsIgnoreCase("pageOfSignUp"))
				{
					final String[] siteIds = uform.getSiteIds();
					if (siteIds != null && siteIds.length != 0)
					{
						final Collection newSiteCollection = new HashSet();
						for (final String siteId : siteIds)
						{
							final Site site = new Site();
							site.setId(Long.valueOf(siteId));
							newSiteCollection.add(site);
						}

						getSiteCollection().clear();
						getSiteCollection().addAll(newSiteCollection);
					}
				}
				id = Long.valueOf(uform.getId());
				setLoginName(uform.getEmailAddress());
				setLastName(uform.getLastName());
				setFirstName(uform.getFirstName());
				setEmailAddress(uform.getEmailAddress());
				setRoleId(uform.getRole());
				institution.setId(Long.valueOf(uform.getInstitutionId()));

				department.setId(Long.valueOf(uform.getDepartmentId()));
				cancerResearchGroup.setId(Long.valueOf(uform.getCancerResearchGroupId()));
				if (Constants.PAGE_OF_USER_PROFILE.equals(pageOf) == Boolean.FALSE)
				{
					activityStatus = uform.getActivityStatus();
				}

				if (pageOf.equals(Constants.PAGE_OF_SIGNUP))
				{
					if ("yes".equalsIgnoreCase(uform.getIdpSelection()))
					{
						targetIdp = uform.getTargetIdp();
						targetIdpLoginName = uform.getTargetLoginName();
						targetPassword = uform.getTargetPassword();
					}
					setStartDate(Calendar.getInstance().getTime());
				}

				if (!pageOf.equals(Constants.PAGE_OF_SIGNUP)
						&& !pageOf.equals(Constants.PAGE_OF_USER_PROFILE))
				{
					comments = uform.getComments();
				}

				if (uform.getPageOf().equals(Constants.PAGE_OF_USER_ADMIN)
						&& uform.getOperation().equals(Constants.ADD))
				{
					activityStatus = Status.ACTIVITY_STATUS_ACTIVE.toString();
					setStartDate(Calendar.getInstance().getTime());
				}

				// Bug-1516: Jitendra
				if (uform.getPageOf().equals(Constants.PAGE_OF_USER_ADMIN)
						&& uform.getOperation().equals(Constants.EDIT))
				{
					newPassword = uform.getNewPassword();
				}

				if (uform.getPageOf().equals(Constants.PAGE_OF_APPROVE_USER))
				{
					if (uform.getStatus().equals(Status.APPROVE_USER_APPROVE_STATUS.toString()))
					{
						activityStatus = Status.ACTIVITY_STATUS_ACTIVE.toString();
					}
					else if (uform.getStatus().equals(Status.APPROVE_USER_REJECT_STATUS.toString()))
					{
						activityStatus = Status.ACTIVITY_STATUS_REJECT.toString();
					}
					else
					{
						activityStatus = Status.ACTIVITY_STATUS_PENDING.toString();
					}
				}

				roleId = uform.getRole();
				address.setStreet(uform.getStreet());
				address.setCity(uform.getCity());
				address.setState(uform.getState());
				address.setCountry(uform.getCountry());
				address.setZipCode(uform.getZipCode());
				address.setPhoneNumber(uform.getPhoneNumber());
				address.setFaxNumber(uform.getFaxNumber());

				if (Constants.PAGE_OF_USER_ADMIN.equals(pageOf))
				{
					csmUserId = uform.getCsmUserId();
				}

			}
		}
		catch (final Exception excp)
		{
			logger.error(excp.getMessage(), excp);
			final ErrorKey errorKey = ErrorKey.getErrorKey("assign.data.error");
			throw new AssignDataException(errorKey, null, "User.java :");
		}
	}

	/**
	 * Returns message label to display on success add or edit.
	 *
	 * @return String
	 */
	@Override
	public String getMessageLabel()
	{
		return edu.wustl.catissuecore.util.global.AppUtility.getlLabel(lastName, firstName);
	}

	/**
	 * Get the latest password.
	 *
	 * @return Sting.
	 */
	public String getLatestPassword()
	{
		String password = null;
		final List pwdList = new ArrayList(getPasswordCollection());
		if (pwdList != null)
		{
			Collections.sort(pwdList);
			if (!pwdList.isEmpty())
			{
				password = ((Password) pwdList.get(0)).getPassword();
			}
		}
		return password;
	}

	/**
	 * Returns true if a new user is added.
	 *
	 * @hibernate.property name="firstTimeLogin" type="boolean"
	 *                     column="FIRST_TIME_LOGIN"
	 * @return Boolean true if a new user is created
	 * @see #setFirstTimeLogin(Boolean)
	 */
	public Boolean getFirstTimeLogin()
	{
		return firstTimeLogin;
	}

	/**
	 * @param firstTimeLogin
	 *            The firstTimeLogin to set.
	 */
	public void setFirstTimeLogin(final Boolean firstTimeLogin)
	{
		this.firstTimeLogin = firstTimeLogin;
	}

	/**
	 * @return Returns the userCollectionProtocolCollection.
	 * @hibernate.set name="userCollectionProtocolCollection"
	 *                table="CATISSUE_USER_COLLECTION_PROTOCOLS" cascade="none"
	 *                inverse="true" lazy="true"
	 * @hibernate.collection-key column="USER_ID"
	 * @hibernate.collection-many-to-many
	 *                                    class="edu.wustl.catissuecore.domain.CollectionProtocol"
	 *                                    column="COLLECTION_PROTOCOL_ID"
	 */
	public Collection<CollectionProtocol> getAssignedProtocolCollection()
	{
		return assignedProtocolCollection;
	}

	/**
	 * @param userCollectionProtocolCollection
	 *            Collection of CollectionProtocol.
	 */
	public void setAssignedProtocolCollection(
			final Collection<CollectionProtocol> userCollectionProtocolCollection)
	{
		assignedProtocolCollection = userCollectionProtocolCollection;
	}

	/**
	 * @return Returns the siteCollection.
	 * @hibernate.set name="siteCollection" table="CATISSUE_SITE_USERS"
	 *                cascade="none" inverse="true" lazy="true"
	 * @hibernate.collection-key column="USER_ID"
	 * @hibernate.collection-many-to-many
	 *                                    class="edu.wustl.catissuecore.domain.Site"
	 *                                    column="SITE_ID"
	 */
	public Collection<Site> getSiteCollection()
	{
		return siteCollection;
	}

	/**
	 * @param siteCollection
	 *            of Collection Site.
	 */
	public void setSiteCollection(final Collection<Site> siteCollection)
	{
		this.siteCollection = siteCollection;
	}

	public String getTargetIdpLoginName()
	{
		return targetIdpLoginName;
	}

	public void setTargetIdpLoginName(final String targetIdpLoginName)
	{
		this.targetIdpLoginName = targetIdpLoginName;
	}

	public Boolean getAdminuser()
	{
		// TODO Auto-generated method stub
		return null;
	}

	public void setAdminuser(Boolean arg0)
	{
		// TODO Auto-generated method stub

	}

}