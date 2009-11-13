/**
 * <p>Title: UserForm Class>
 * <p>Description:  UserForm Class is used to encapsulate all the request parameters passed
 * from User Add/Edit webpage. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 * Created on Mar 3, 2005
 */

package edu.wustl.catissuecore.actionForm;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.DefaultValueManager;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.CommonUtilities;
import edu.wustl.common.util.global.PasswordManager;
import edu.wustl.common.util.global.Status;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.security.exception.SMException;
import edu.wustl.security.manager.SecurityManagerFactory;

/**
 * UserForm Class is used to encapsulate all the request parameters passed.
 * from User Add/Edit webpage.
 * @author gautam_shetty
 * */
public class UserForm extends AbstractActionForm
{

	/**
	 * Common Logger for Login Action.
	 */
	private static final Logger logger = Logger.getCommonLogger(UserForm.class);

	/**
	 *  Serial Version ID of the class.
	 */
	private static final long serialVersionUID = 9121703882368803846L;

	/**
	 * Last Name of the user.
	 */
	private String lastName;

	/**
	 * First Name of the user.
	 */
	private String firstName;

	/**
	 * Institution name of the user.
	 */
	private long institutionId;

	/**
	 * EmailAddress Address of the user.
	 */
	private String emailAddress;

	/**
	 * Old Password of the user.
	 */
	private String oldPassword;

	/**
	 * New Password of the user.
	 */
	private String newPassword;

	/**
	 * Confirmed new password of the user.
	 */
	private String confirmNewPassword;

	/**
	 * Department name of the user.
	 */
	private long departmentId;

	/**
	 * Street Address of the user.
	 */
	private String street;

	/**
	 * The City where the user stays.
	 */
	private String city;

	/**
	 * The State where the user stays.
	 */
	private String state = (String) DefaultValueManager.getDefaultValue(Constants.DEFAULT_STATES);

	/**
	 * The Country where the user stays.
	 */
	private String country = (String) DefaultValueManager
			.getDefaultValue(Constants.DEFAULT_COUNTRY);

	/**
	 * The zip code of city where the user stays.
	 * */
	private String zipCode;

	/**
	 * Phone number of the user.
	 * */
	private String phoneNumber;

	/**
	 * Fax number of the user.
	 * */
	private String faxNumber;

	/**
	 * Role of the user.
	 * */
	private String role = "";

	/**
	 * Cancer Research Group of the user.
	 */
	private long cancerResearchGroupId;

	/**
	 * Comments given by user.
	 */
	private String comments;

	/**
	 * Status of user in the system.
	 */
	private String status;
	/**
	 * CSM User Id.
	 */
	private Long csmUserId;

	/**
	 * WUSTLkey of a user.
	 */
	private String wustlKey = null;

	/**
	 * COnfirm EmailAddress of the user.
	 */
	private String confirmEmailAddress;

	/**
	 * Site ID's.
	 */
	private String[] siteIds;

	/**
	 * @return String[]
	 */
	public String[] getSiteIds()
	{
		return this.siteIds;
	}

	/**
	 * Set Site ID's.
	 * @param siteIds ID's of Site
	 */
	public void setSiteIds(String[] siteIds)
	{
		this.siteIds = siteIds;
	}

	/**
	 * No argument constructor for UserForm class.
	 */
	public UserForm()
	{
		//		reset();
	}

	//Mandar : 24-Apr-06 : bug id : 972
	/**
	 * @return Returns the confirmEmailAddress.
	 */
	public String getConfirmEmailAddress()
	{
		return this.confirmEmailAddress;
	}

	/**
	 * @param confirmEmailAddress The confirmEmailAddress to set.
	 */
	public void setConfirmEmailAddress(String confirmEmailAddress)
	{
		this.confirmEmailAddress = confirmEmailAddress;
	}

	/**
	 * Returns the last name of the user.
	 * @return String representing the last name of the user.
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
	 * @return String representing the first name of the user.
	 * @see #setFirstName(String)
	 */
	public String getFirstName()
	{
		return (this.firstName);
	}

	/**
	 * Sets the first name of the user.
	 * @param firstName String representing the first name of the user.
	 * @see #getFirstName()
	 */
	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}

	/**
	 * Returns the institutionId name of the user.
	 * @return String representing the institutionId of the user.
	 * @see #setinstitution(String)
	 */
	public long getInstitutionId()
	{
		return (this.institutionId);
	}

	/**
	 * Sets the institutionId name of the user.
	 * @param institution String representing the institutionId of the user.
	 * @see #getinstitution()
	 */
	public void setInstitutionId(long institution)
	{
		this.institutionId = institution;
	}

	/**
	 * Returns the emailAddress Address of the user.
	 * @return String representing the emailAddress address of the user.
	 */
	public String getEmailAddress()
	{
		return (this.emailAddress);
	}

	/**
	 * Sets the emailAddress address of the user.
	 * @param emailAddress String representing emailAddress address of the user
	 * @see #getEmailAddress()
	 */
	public void setEmailAddress(String emailAddress)
	{
		this.emailAddress = emailAddress;
	}

	/**
	 * @return Returns the confirmNewPassword.
	 */
	public String getConfirmNewPassword()
	{
		return this.confirmNewPassword;
	}

	/**
	 * @return Returns the newPassword.
	 */
	public String getNewPassword()
	{
		return this.newPassword;
	}

	/**
	 * @return Returns the oldPassword.
	 */
	public String getOldPassword()
	{
		return this.oldPassword;
	}

	/**
	 * @param confirmNewPassword The confirmNewPassword to set.
	 */
	public void setConfirmNewPassword(String confirmNewPassword)
	{
		this.confirmNewPassword = confirmNewPassword;
	}

	/**
	 * @param newPassword The newPassword to set.
	 */
	public void setNewPassword(String newPassword)
	{
		this.newPassword = newPassword;
	}

	/**
	 * @param oldPassword The oldPassword to set.
	 */
	public void setOldPassword(String oldPassword)
	{
		this.oldPassword = oldPassword;
	}

	/**
	 * Returns the Department Name of the user.
	 * @return String representing departmentId of the user.
	 * @see #getDepartmentId()
	 */
	public long getDepartmentId()
	{
		return (this.departmentId);
	}

	/**
	 * Sets the Department Name of the user.
	 * @param department String representing departmentId of the user.
	 * @see #getDepartmentId()
	 */
	public void setDepartmentId(long department)
	{
		this.departmentId = department;
	}

	/**
	 * Returns the cancer research group the user belongs.
	 * @return Returns the cancerResearchGroupId.
	 * @see #setCancerResearchGroupId(String)
	 */
	public long getCancerResearchGroupId()
	{
		return this.cancerResearchGroupId;
	}

	/**
	 * Sets the cancer research group the user belongs.
	 * @param cancerResearchGroup The cancerResearchGroupId to set.
	 * @see #getCancerResearchGroupId()
	 */
	public void setCancerResearchGroupId(long cancerResearchGroup)
	{
		this.cancerResearchGroupId = cancerResearchGroup;
	}

	/**
	 * Returns the Street Address of the user.
	 * @return String representing mailing address of the user.
	 * @see #setStreet(String)
	 */
	public String getStreet()
	{
		return (this.street);
	}

	/**
	 * Sets the Street Address of the user.
	 * @param street String representing mailing address of the user.
	 * @see #getStreet()
	 */
	public void setStreet(String street)
	{
		this.street = street;
	}

	/**
	 * Returns the City where the user stays.
	 * @return String representing city of the user.
	 * @see #setCity(String)
	 */
	public String getCity()
	{
		return (this.city);
	}

	/**
	 * Sets the City where the user stays.
	 * @param city String representing city of the user.
	 * @see #getCity()
	 */
	public void setCity(String city)
	{
		this.city = city;
	}

	/**
	 * Returns the State where the user stays.
	 * @return String representing state of the user.
	 * @see #setState(String)
	 */
	public String getState()
	{
		return (this.state);
	}

	/**
	 * Sets the State where the user stays.
	 * @param state String representing state of the user.
	 * @see #getState()
	 */
	public void setState(String state)
	{
		this.state = state;
	}

	/**
	 * Returns the Country where the user stays.
	 * @return String representing country of the user.
	 * @see #setCountry(String)
	 */
	public String getCountry()
	{
		return (this.country);
	}

	/**
	 * Sets the Country where the user stays.
	 * @param country String representing country of the user.
	 * @see #getCountry()
	 */
	public void setCountry(String country)
	{
		this.country = country;
	}

	/**
	 * Returns the zip code of the user's city.
	 * @return Returns the zip.
	 * @see #setZip(String)
	 */
	public String getZipCode()
	{
		return this.zipCode;
	}

	/**
	 * Sets the zip code of the user's city.
	 * @param zipCode The zip code to set.
	 * @see #getZip()
	 */
	public void setZipCode(String zipCode)
	{
		this.zipCode = zipCode;
	}

	/**
	 * Returns the phone number of the user.
	 * @return Returns the phone number.
	 * @see #setPhone(String)
	 */
	public String getPhoneNumber()
	{
		return this.phoneNumber;
	}

	/**
	 * Sets the phone number of the user.
	 * @param phoneNumber The phone number to set.
	 * @see #getphoneNumber()
	 */
	public void setPhoneNumber(String phoneNumber)
	{
		this.phoneNumber = phoneNumber;
	}

	/**
	 * Returns the fax number of the user.
	 * @return Returns the fax.
	 * @see #setFax(String)
	 */
	public String getFaxNumber()
	{
		return this.faxNumber;
	}

	/**
	 * Sets the fax number of the user.
	 * @param faxNumber The fax to set.
	 * @see #getFax()
	 */
	public void setFaxNumber(String faxNumber)
	{
		this.faxNumber = faxNumber;
	}

	/**
	 * Returns the role of the user.
	 * @return the role of the user.
	 */
	public String getRole()
	{
		return this.role;
	}

	/**
	 * Sets the role of the user.
	 * @param role the role of the user.
	 * @see #getRole()
	 */
	public void setRole(String role)
	{
		if (role != null && role.equalsIgnoreCase("-1"))
		{
			// this.role = Constants.NON_ADMIN_USER;
			this.role = "";
		}
		else
		{
			this.role = role;
		}
	}

	/**
	 * @return Returns the comments.
	 */
	public String getComments()
	{
		return this.comments;
	}

	/**
	 * @param comments The comments to set.
	 */
	public void setComments(String comments)
	{
		this.comments = comments;
	}

	/**
	 * @return Returns the id assigned to form bean.
	 */
	@Override
	public int getFormId()
	{
		int formId = Constants.APPROVE_USER_FORM_ID;
		if ((this.getPageOf() != null)
				&& !(Constants.PAGE_OF_APPROVE_USER.equals(this.getPageOf())))
		{
			formId = Constants.USER_FORM_ID;
		}
		logger.debug("................formId...................." + formId);
		return formId;
	}

	/**
	 * @return Returns the status.
	 */
	public String getStatus()
	{
		return this.status;
	}

	/**
	 * @param status The status to set.
	 */
	public void setStatus(String status)
	{
		this.status = status;
	}

	/**
	 * @return Returns the csmUserId.
	 */
	public Long getCsmUserId()
	{
		return this.csmUserId;
	}

	/**
	 * @param csmUserId The csmUserId to set.
	 */
	public void setCsmUserId(Long csmUserId)
	{
		this.csmUserId = csmUserId;
	}

	/**
	 * Resets the values of all the fields.
	 * Is called by the overridden reset method defined in ActionForm.
	 * */
	@Override
	protected void reset()
	{
		//		this.lastName = null;
		//		this.firstName = null;
		//		this.institutionId = -1;
		//		this.emailAddress = null;
		//		this.departmentId = -1;
		//		this.street = null;
		//		this.city = null;
		//		/**
		//	     * Name : Virender Mehta
		//	     * Reviewer: Sachin Lale
		//	     * Bug ID: defaultValueConfiguration_BugID
		//	     * Patch ID:defaultValueConfiguration_BugID_5
		//	     * Description: Configuration for default value for State and country
		//	    */
		//		this.state = (String)DefaultValueManager.getDefaultValue(Constants.DEFAULT_STATES);
		//		this.country =(String)DefaultValueManager.
		//getDefaultValue(Constants.DEFAULT_COUNTRY);
		//		this.zipCode = null;
		//		this.phoneNumber = null;
		//		this.faxNumber = null;
		//		this.role = null;
		//		this.cancerResearchGroupId = -1;
		//		this.status = Constants.ACTIVITY_STATUS_NEW;
		//		this.activityStatus = Constants.ACTIVITY_STATUS_NEW;
		//		//Mandar : 24-Apr-06 : bug 972:
		//		this.confirmEmailAddress = null;

	}

	/**
	 * Copies the data from an AbstractDomain object to a UserForm object.
	 * @param abstractDomain An AbstractDomain object.
	 */
	public void setAllValues(AbstractDomainObject abstractDomain)
	{
		if (!Constants.PAGE_OF_CHANGE_PASSWORD.equals(this.getPageOf()))
		{
			final User user = (User) abstractDomain;

			this.setId(user.getId().longValue());
			this.lastName = user.getLastName();
			this.firstName = user.getFirstName();

			this.wustlKey = user.getWustlKey();
			this.setInstId(user);
			this.setInstId(user);
			this.emailAddress = user.getEmailAddress();

			//Mandar : 24-Apr-06 : bug id 972 : confirmEmailAddress
			this.confirmEmailAddress = this.emailAddress;
			this.setDptCRG(user);
			this.setAddr(user);
			//Populate the activity status, comments and role for approve user and user edit.
			this.setUserData(user);
			this.setDptCRG(user);
			this.setAddr(user);
			//Populate the activity status, comments and role for approve user and user edit.
			this.setUserData(user);
			if (Constants.PAGE_OF_USER_ADMIN.equals(this.getPageOf()))
			{
				this.setCsmUserId(user.getCsmUserId());
				try
				{
					this.setPwd();
				}
				catch (final SMException e)
				{
					UserForm.logger.error(e.getMessage(), e);
					e.printStackTrace();
				}
			}
			if (Constants.PAGE_OF_USER_PROFILE.equals(this.getPageOf()))
			{
				this.role = user.getRoleId();
			}
		}
		logger.debug("this.activityStatus............." + this.getActivityStatus());
		logger.debug("this.comments" + this.comments);
		logger.debug("this.role" + this.role);
		logger.debug("this.status" + this.status);
		logger.debug("this.csmUserid" + this.csmUserId);
	}

	/**
	 * @throws SMException SMException
	 */
	private void setPwd() throws SMException
	{
		if (this.csmUserId != null) //in case user not approved
		{
			final gov.nih.nci.security.authorization.domainobjects.User csmUser =
				SecurityManagerFactory.getSecurityManager().
				getUserById(this.getCsmUserId().toString());
			if (csmUser != null)
			{
				this.setNewPassword(csmUser.getPassword());
				this.setConfirmNewPassword(csmUser.getPassword());
			}
		}
		else
		{
			this.setNewPassword("");
			this.setConfirmNewPassword("");
		}
	}

	/**
	 * @param user Set User
	 */
	private void setUserData(User user)
	{
		if ((this.getFormId() == Constants.APPROVE_USER_FORM_ID)
				|| ((this.getPageOf() != null) && (Constants.PAGE_OF_USER_ADMIN.equals(this
						.getPageOf()))))
		{
			this.setActivityStatus(user.getActivityStatus());

			this.setCmts(user);
			this.setUserRole(user.getRoleId());
			if (this.getFormId() == Constants.APPROVE_USER_FORM_ID)
			{
				this.status = user.getActivityStatus();
				this.setStats();
			}
		}
	}

	/**
	 * Setting the ActivityStatus.
	 */
	private void setStats()
	{
		if (this.getActivityStatus().equals(Status.ACTIVITY_STATUS_ACTIVE.toString()))
		{
			this.status = Status.APPROVE_USER_APPROVE_STATUS.toString();
		}
		else if (this.getActivityStatus().equals(Status.ACTIVITY_STATUS_CLOSED.toString()))
		{
			this.status = Status.APPROVE_USER_REJECT_STATUS.toString();
		}
		else if (this.getActivityStatus().equals(Status.ACTIVITY_STATUS_NEW.toString()))
		{
			this.status = Status.APPROVE_USER_PENDING_STATUS.toString();
		}
	}

	/**
	 * @param user User Object
	 */
	private void setCmts(User user)
	{
		if (!CommonUtilities.isNull(user.getComments()))
		{
			this.comments = user.getComments();
		}
	}

	/**
	 * @param user User Object
	 */
	private void setAddr(User user)
	{
		if (!CommonUtilities.isNull(user.getAddress()))
		{
			this.street = user.getAddress().getStreet();
			this.city = user.getAddress().getCity();
			this.state = user.getAddress().getState();
			this.country = user.getAddress().getCountry();
			this.zipCode = user.getAddress().getZipCode();
			this.phoneNumber = user.getAddress().getPhoneNumber();
			this.faxNumber = user.getAddress().getFaxNumber();
		}
	}

	/**
	 * @param user User Object
	 */
	private void setDptCRG(User user)
	{
		if (!CommonUtilities.isNull(user.getDepartment()))
		{
			this.departmentId = user.getDepartment().getId().longValue();
		}

		if (!CommonUtilities.isNull(user.getCancerResearchGroup()))
		{
			this.cancerResearchGroupId = user.getCancerResearchGroup().getId().longValue();
		}
	}

	/**
	 * @param user User object
	 */
	private void setInstId(User user)
	{
		if (!CommonUtilities.isNull(user.getInstitution()))
		{
			this.institutionId = user.getInstitution().getId().longValue();
		}
	}

	/**
	 * @param roleId Role Id
	 */
	private void setUserRole(String roleId)
	{
		this.role = "";
		if (roleId != null && roleId.equalsIgnoreCase(Constants.ADMIN_USER))
		{
			this.role = Constants.SUPER_ADMIN_USER;
		}
		if (roleId != null && roleId.equalsIgnoreCase(Constants.SUPER_ADMIN_USER))
		{
			this.role = Constants.SUPER_ADMIN_USER;
		}
	}
	/**
	 * @param errors Object of ActionErrors
	 * @param validator Object of Validator
	 * @param key error key
	 * @param value value of error key
	 */
	private void chkEmpty(ActionErrors errors, Validator validator, String key, String value)
	{
		if (Validator.isEmpty(value))
		{
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",
					ApplicationProperties.getValue(key)));
		}

	}

	/**
	 * Overrides the validate method of ActionForm.
	 * @return error ActionErrors instance
	 * @param mapping Actionmapping instance
	 * @param request HttpServletRequest instance
	 */
	@Override
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
		final ActionErrors errors = new ActionErrors();
		final Validator validator = new Validator();
		try
		{
			if (this.getOperation() != null)
			{
				if (this.getPageOf().equals(Constants.PAGE_OF_CHANGE_PASSWORD))
				{
					this.chkEmpty(errors, validator, "user.oldPassword", this.oldPassword);
					this.chkEmpty(errors, validator, "user.newPassword", this.newPassword);
					this.chkEmpty(errors, validator, "user.confirmNewPassword",
							this.confirmNewPassword);
					this.chkNwOdPwds(errors, validator);
					this.chkNOPwds1(request, errors, validator);
				}
				else
				{
					this.setRedirectValue(validator);
					logger.debug("user form ");
					this.ifAddEdit(errors, validator);

					if (this.getPageOf().equals(Constants.PAGE_OF_APPROVE_USER))
					{
						this.chkValOpt(errors, validator,
								"user.approveOperation", this.status);
						this.chkValOpt(errors, validator, "user.approveOperation",
						this.status);
					}
					//Bug- 1516:
					this.adminEdit(request, errors, validator);
					// Mandar 10-apr-06 : bugid :353 end
					//Bug- 1516:
					this.adminEdit(request, errors, validator);
					// Mandar 10-apr-06 : bugid :353 end
				}
				validateWashUEmailAddress(errors);
			}
		}
		catch (final Exception excp)
		{
			UserForm.logger.error(excp.getMessage(), excp);
			excp.printStackTrace();
		}
		return errors;
	}
	/**
	 * @param errors ActionErrors
	 */
	private void validateWashUEmailAddress(final ActionErrors errors)
	{
		if(!"".equals(wustlKey))
		{
		  if(!emailAddress.endsWith(edu.wustl.wustlkey.util.global.Constants.WUSTL_EDU)
		  && !emailAddress.endsWith(edu.wustl.wustlkey.util.global.Constants.WUSTL_EDU_CAPS))
		  {
		        errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",
		        ApplicationProperties.getValue("email.washu.user")));
		  }
		  if(!confirmEmailAddress.endsWith(edu.wustl.wustlkey.util.global.Constants.WUSTL_EDU)
		  && !confirmEmailAddress.endsWith(edu.wustl.wustlkey.util.global.Constants.WUSTL_EDU_CAPS))
		  {
		        errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",
		        ApplicationProperties.getValue("confirm.washu.user")));
		  }
		}
	}

	/**
	 * @param errors Object of ActionErrors
	 * @param validator Object of Validator
	 */
	private void ifAddEdit(ActionErrors errors, Validator validator)
	{
		if (this.getOperation().equals(Constants.ADD) || this.getOperation().equals(Constants.EDIT))
		{
			this.chkSites(errors, validator);
			this.chkMail(errors, validator, "user.emailAddress", this.emailAddress);

			// Mandar : 24-Apr-06 Bugid:972 confirmEmailAddress start
			if (!this.getPageOf().equals(Constants.PAGE_OF_USER_PROFILE))
			{
				this.chkMail(errors, validator, "user.confirmemailAddress",
						this.confirmEmailAddress);
				this.compMail(errors);
			}
			//Mandar : 24-Apr-06 Bugid:972 confirmEmailAddress end

			this.chkNames(errors, validator, "user.lastName", this.lastName);
			this.chkNames(errors, validator, "user.firstName", this.firstName);
			this.chkEmpty(errors, validator, "user.city", this.city);
			if (edu.wustl.catissuecore.util.global.Variables.isStateRequired)
			{
				this.chkValOpt(errors, validator, "user.state", this.state);
			}
			this.chkValOpt(errors, validator, "user.country", this.country);
			this.chkValOpt(errors, validator, "user.institution", String
					.valueOf(this.institutionId));
			this.chkValOpt(errors, validator, "user.department", String.valueOf(this.departmentId));
			this.chkValOpt(errors, validator, "user.cancerResearchGroup", String
					.valueOf(this.cancerResearchGroupId));
			this.chkValOpt(errors, validator, "user.activityStatus", this.getActivityStatus());
		}
	}

	/**
	 * @param errors Object of ActionErrors
	 * @param validator Object of Validator
	 * @param key error key
	 * @param value value of error key
	 */
	private void chkNames(ActionErrors errors, Validator validator, String key, String value)
	{
		if (Validator.isEmpty(value))
		{
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",
					ApplicationProperties.getValue(key)));
		}
		else if (Validator.isXssVulnerable(value))
		{ //Bug:7976 & 7977: added check for xxs vulnerable
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.xss.invalid",
					ApplicationProperties.getValue(key)));
		}
	}

	/**
	 * @param errors Object of ActionErrors
	 * @param validator Object of Validator
	 * @param request HttpServletRequest
	 */
	private void adminEdit(HttpServletRequest request, ActionErrors errors, Validator validator)
	{
		if (this.getPageOf().equals(Constants.PAGE_OF_USER_ADMIN)
				&& this.getOperation().equals(Constants.EDIT))
		{
			final String pageFrom = request.getParameter("pageFrom");
			if (!"ApproveUser".equals(pageFrom))
			{
				this.chkEmpty(errors, validator, "user.newPassword", this.newPassword);
				this
						.chkEmpty(errors, validator, "user.confirmNewPassword",
								this.confirmNewPassword);

				this.chkNwOdPwds(errors, validator);
				//Bug-7979
				this.chkPwd(request, errors);
			}
		}
	}

	/**
	 * @param request HttpServletRequest
	 * @param errors Object of ActionErrors
	 */
	private void chkPwd(HttpServletRequest request, ActionErrors errors)
	{
		int result = PasswordManager.SUCCESS;
		if (!Constants.DUMMY_PASSWORD.equals(this.newPassword))
		{
			final Boolean isPasswordChanged = (Boolean) request.getSession().getAttribute(
					Constants.PASSWORD_CHANGE_IN_SESSION);
			result = PasswordManager.validatePasswordOnFormBean(this.newPassword, this.oldPassword,
					isPasswordChanged);
		}

		if (result != PasswordManager.SUCCESS)
		{
			// get error message of validation failure where param is result of validate() method
			final String errorMessage = PasswordManager.getErrorMessage(result);
			logger.debug("error from Password validate " + errorMessage);
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item", errorMessage));
		}
	}

	/**
	 * @param errors Object of ActionErrors
	 * @param validator Object of Validator
	 * @param key error key
	 * @param value value of error key
	 */
	private void chkValOpt(ActionErrors errors, Validator validator, String key, String value)
	{
		if (!validator.isValidOption(value))
		{
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",
					ApplicationProperties.getValue(key)));
		}
	}

	/**
	 * @param errors Object of ActionErrors
	 */
	private void compMail(ActionErrors errors)
	{
		if (!this.confirmEmailAddress.equals(this.emailAddress))
		{
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.email.mismatch"));
		}
	}

	/**
	 * @param errors Object of ActionErrors
	 * @param validator Object of Validator
	 * @param key error key
	 * @param value value of error key
	 */
	private void chkMail(ActionErrors errors, Validator validator, String key, String value)
	{
		if (Validator.isEmpty(value))
		{
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",
					ApplicationProperties.getValue(key)));
		}
		else
		{
			if (!validator.isValidEmailAddress(value))
			{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.format",
						ApplicationProperties.getValue(key)));
			}
		}
	}

	/**
	 * @param errors Object of ActionErrors
	 * @param validator Object of Validator
	 */
	private void chkSites(ActionErrors errors, Validator validator)
	{
		if (this.siteIds != null && this.siteIds.length > 0 && Validator.isEmpty(this.role))
		{
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",
					ApplicationProperties.getValue("user.roleIsRequired")));
		}
	}

	/**
	 * @param request Object of HttpServletRequest
	 * @param errors Object of ActionErrors
	 * @param validator Object of Validator
	 */
	private void chkNOPwds1(HttpServletRequest request, ActionErrors errors, Validator validator)
	{
		if (!Validator.isEmpty(this.newPassword) && !Validator.isEmpty(this.oldPassword))
		{
			// Call static method PasswordManager.validatePasswordOnFormBean() where params are
			// new password,old password,user name
			final Boolean isPasswordChanged = (Boolean) request.getSession().getAttribute(
					Constants.PASSWORD_CHANGE_IN_SESSION);
			final int result = PasswordManager.validatePasswordOnFormBean(this.newPassword,
					this.oldPassword, isPasswordChanged);

			if (result != PasswordManager.SUCCESS)
			{
				final String errorMessage = PasswordManager.getErrorMessage(result);
				logger.debug("error from Password validate " + errorMessage);
				errors.add(ActionErrors.GLOBAL_ERROR,
						new ActionError("errors.item", errorMessage));
			}
		}
	}

	/**
	 * @param errors
	 * 			Object of ActionErrors
	 * @param validator
	 * 			Object of validator
	 */
	private void chkNwOdPwds(ActionErrors errors, Validator validator)
	{
		if (!Validator.isEmpty(this.newPassword) && !Validator.isEmpty(this.confirmNewPassword))
		{
			if (!this.newPassword.equals(this.confirmNewPassword))
			{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
						"errors.confirmNewPassword.reType"));
			}
		}
	}

	/**
	 * This method sets Identifier of Objects inserted by AddNew activity in
	 * Form-Bean which initialized AddNew action.
	 * @param addNewFor - FormBean ID of the object inserted
	 * @param addObjectIdentifier - Identifier of the Object inserted
	 */
	@Override
	public void setAddNewObjectIdentifier(String addNewFor, Long addObjectIdentifier)
	{
		if (addNewFor.equals("institution"))
		{
			this.setInstitutionId(addObjectIdentifier.longValue());
		}
		else if (addNewFor.equals("department"))
		{
			this.setDepartmentId(addObjectIdentifier.longValue());
		}
		else if (addNewFor.equals("cancerResearchGroup"))
		{
			this.setCancerResearchGroupId(addObjectIdentifier.longValue());
		}
	}

	/**
	 * WUSTLKey of the WashU User.
	 * @return wustlKey
	 */
	public String getWustlKey()
	{
		return this.wustlKey;
	}

	/**
	 * Set WUSTLKey of the WashU User.
	 * @param wustlKey WustlKey of a user
	 */
	public void setWustlKey(String wustlKey)
	{
		this.wustlKey = wustlKey;
	}

}