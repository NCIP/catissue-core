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
 * UserForm Class is used to encapsulate all the request parameters passed. from
 * User Add/Edit webpage.
 *
 * @author gautam_shetty
 * */
public class UserForm extends AbstractActionForm
{

    /**
     * Common Logger for Login Action.
     */
    private static final Logger logger = Logger.getCommonLogger(UserForm.class);

    /**
     * Serial Version ID of the class.
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
     * Password of the target domain of the user.
     */
    private String targetPassword;

    /**
     * Login Id of the target domain of the user.
     */
    private String targetLoginName;

    /**
     * Yes or No selection for question
     * "Do you have a login and password to any of the following?".
     */
    private String idpSelection;

    /**
     * Name of the target idp.
     */
    private String targetIdp;

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
    private String country = (String) DefaultValueManager.getDefaultValue(Constants.DEFAULT_COUNTRY);

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
        return siteIds;
    }

    /**
     * Set Site ID's.
     *
     * @param siteIds
     *            ID's of Site
     */
    public void setSiteIds(final String[] siteIds)
    {
        this.siteIds = siteIds;
    }

    /**
     * No argument constructor for UserForm class.
     */
    public UserForm()
    {
        // reset();
    }

    public String getTargetPassword()
    {
        return targetPassword;
    }

    public void setTargetPassword(final String targetPassword)
    {
        this.targetPassword = targetPassword;
    }

    public String getTargetLoginName()
    {
        return targetLoginName;
    }

    public void setTargetLoginName(final String targetLoginName)
    {
        this.targetLoginName = targetLoginName;
    }

    public String getIdpSelection()
    {
        return idpSelection;
    }

    public void setIdpSelection(final String idpSelection)
    {
        this.idpSelection = idpSelection;
    }

    public String getTargetIdp()
    {
        return targetIdp;
    }

    public void setTargetIdp(final String targetIdp)
    {
        this.targetIdp = targetIdp;
    }

    // Mandar : 24-Apr-06 : bug id : 972
    /**
     * @return Returns the confirmEmailAddress.
     */
    public String getConfirmEmailAddress()
    {
        return confirmEmailAddress;
    }

    /**
     * @param confirmEmailAddress
     *            The confirmEmailAddress to set.
     */
    public void setConfirmEmailAddress(final String confirmEmailAddress)
    {
        this.confirmEmailAddress = confirmEmailAddress;
    }

    /**
     * Returns the last name of the user.
     *
     * @return String representing the last name of the user.
     * @see #setFirstName(String)
     */
    public String getLastName()
    {
        return (lastName);
    }

    /**
     * Sets the last name of the user.
     *
     * @param lastName
     *            Last Name of the user
     * @see #getFirstName()
     */
    public void setLastName(final String lastName)
    {
        this.lastName = lastName;
    }

    /**
     * Returns the first name of the user.
     *
     * @return String representing the first name of the user.
     * @see #setFirstName(String)
     */
    public String getFirstName()
    {
        return (firstName);
    }

    /**
     * Sets the first name of the user.
     *
     * @param firstName
     *            String representing the first name of the user.
     * @see #getFirstName()
     */
    public void setFirstName(final String firstName)
    {
        this.firstName = firstName;
    }

    /**
     * Returns the institutionId name of the user.
     *
     * @return String representing the institutionId of the user.
     * @see #setinstitution(String)
     */
    public long getInstitutionId()
    {
        return (institutionId);
    }

    /**
     * Sets the institutionId name of the user.
     *
     * @param institution
     *            String representing the institutionId of the user.
     * @see #getinstitution()
     */
    public void setInstitutionId(final long institution)
    {
        institutionId = institution;
    }

    /**
     * Returns the emailAddress Address of the user.
     *
     * @return String representing the emailAddress address of the user.
     */
    public String getEmailAddress()
    {
        return (emailAddress);
    }

    /**
     * Sets the emailAddress address of the user.
     *
     * @param emailAddress
     *            String representing emailAddress address of the user
     * @see #getEmailAddress()
     */
    public void setEmailAddress(final String emailAddress)
    {
        this.emailAddress = emailAddress;
    }

    /**
     * @return Returns the confirmNewPassword.
     */
    public String getConfirmNewPassword()
    {
        return confirmNewPassword;
    }

    /**
     * @return Returns the newPassword.
     */
    public String getNewPassword()
    {
        return newPassword;
    }

    /**
     * @return Returns the oldPassword.
     */
    public String getOldPassword()
    {
        return oldPassword;
    }

    /**
     * @param confirmNewPassword
     *            The confirmNewPassword to set.
     */
    public void setConfirmNewPassword(final String confirmNewPassword)
    {
        this.confirmNewPassword = confirmNewPassword;
    }

    /**
     * @param newPassword
     *            The newPassword to set.
     */
    public void setNewPassword(final String newPassword)
    {
        this.newPassword = newPassword;
    }

    /**
     * @param oldPassword
     *            The oldPassword to set.
     */
    public void setOldPassword(final String oldPassword)
    {
        this.oldPassword = oldPassword;
    }

    /**
     * Returns the Department Name of the user.
     *
     * @return String representing departmentId of the user.
     * @see #getDepartmentId()
     */
    public long getDepartmentId()
    {
        return (departmentId);
    }

    /**
     * Sets the Department Name of the user.
     *
     * @param department
     *            String representing departmentId of the user.
     * @see #getDepartmentId()
     */
    public void setDepartmentId(final long department)
    {
        departmentId = department;
    }

    /**
     * Returns the cancer research group the user belongs.
     *
     * @return Returns the cancerResearchGroupId.
     * @see #setCancerResearchGroupId(String)
     */
    public long getCancerResearchGroupId()
    {
        return cancerResearchGroupId;
    }

    /**
     * Sets the cancer research group the user belongs.
     *
     * @param cancerResearchGroup
     *            The cancerResearchGroupId to set.
     * @see #getCancerResearchGroupId()
     */
    public void setCancerResearchGroupId(final long cancerResearchGroup)
    {
        cancerResearchGroupId = cancerResearchGroup;
    }

    /**
     * Returns the Street Address of the user.
     *
     * @return String representing mailing address of the user.
     * @see #setStreet(String)
     */
    public String getStreet()
    {
        return (street);
    }

    /**
     * Sets the Street Address of the user.
     *
     * @param street
     *            String representing mailing address of the user.
     * @see #getStreet()
     */
    public void setStreet(final String street)
    {
        this.street = street;
    }

    /**
     * Returns the City where the user stays.
     *
     * @return String representing city of the user.
     * @see #setCity(String)
     */
    public String getCity()
    {
        return (city);
    }

    /**
     * Sets the City where the user stays.
     *
     * @param city
     *            String representing city of the user.
     * @see #getCity()
     */
    public void setCity(final String city)
    {
        this.city = city;
    }

    /**
     * Returns the State where the user stays.
     *
     * @return String representing state of the user.
     * @see #setState(String)
     */
    public String getState()
    {
        return (state);
    }

    /**
     * Sets the State where the user stays.
     *
     * @param state
     *            String representing state of the user.
     * @see #getState()
     */
    public void setState(final String state)
    {
        this.state = state;
    }

    /**
     * Returns the Country where the user stays.
     *
     * @return String representing country of the user.
     * @see #setCountry(String)
     */
    public String getCountry()
    {
        return (country);
    }

    /**
     * Sets the Country where the user stays.
     *
     * @param country
     *            String representing country of the user.
     * @see #getCountry()
     */
    public void setCountry(final String country)
    {
        this.country = country;
    }

    /**
     * Returns the zip code of the user's city.
     *
     * @return Returns the zip.
     * @see #setZip(String)
     */
    public String getZipCode()
    {
        return zipCode;
    }

    /**
     * Sets the zip code of the user's city.
     *
     * @param zipCode
     *            The zip code to set.
     * @see #getZip()
     */
    public void setZipCode(final String zipCode)
    {
        this.zipCode = zipCode;
    }

    /**
     * Returns the phone number of the user.
     *
     * @return Returns the phone number.
     * @see #setPhone(String)
     */
    public String getPhoneNumber()
    {
        return phoneNumber;
    }

    /**
     * Sets the phone number of the user.
     *
     * @param phoneNumber
     *            The phone number to set.
     * @see #getphoneNumber()
     */
    public void setPhoneNumber(final String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Returns the fax number of the user.
     *
     * @return Returns the fax.
     * @see #setFax(String)
     */
    public String getFaxNumber()
    {
        return faxNumber;
    }

    /**
     * Sets the fax number of the user.
     *
     * @param faxNumber
     *            The fax to set.
     * @see #getFax()
     */
    public void setFaxNumber(final String faxNumber)
    {
        this.faxNumber = faxNumber;
    }

    /**
     * Returns the role of the user.
     *
     * @return the role of the user.
     */
    public String getRole()
    {
        return role;
    }

    /**
     * Sets the role of the user.
     *
     * @param role
     *            the role of the user.
     * @see #getRole()
     */
    public void setRole(final String role)
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
        return comments;
    }

    /**
     * @param comments
     *            The comments to set.
     */
    public void setComments(final String comments)
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
        if ((getPageOf() != null) && !(Constants.PAGE_OF_APPROVE_USER.equals(getPageOf())))
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
        return status;
    }

    /**
     * @param status
     *            The status to set.
     */
    public void setStatus(final String status)
    {
        this.status = status;
    }

    /**
     * @return Returns the csmUserId.
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

    /**
     * Resets the values of all the fields. Is called by the overridden reset
     * method defined in ActionForm.
     * */
    @Override
    protected void reset()
    {
        // this.lastName = null;
        // this.firstName = null;
        // this.institutionId = -1;
        // this.emailAddress = null;
        // this.departmentId = -1;
        // this.street = null;
        // this.city = null;
        // /**
        // * Name : Virender Mehta
        // * Reviewer: Sachin Lale
        // * Bug ID: defaultValueConfiguration_BugID
        // * Patch ID:defaultValueConfiguration_BugID_5
        // * Description: Configuration for default value for State and country
        // */
        // this.state =
        // (String)DefaultValueManager.getDefaultValue(Constants.DEFAULT_STATES);
        // this.country =(String)DefaultValueManager.
        // getDefaultValue(Constants.DEFAULT_COUNTRY);
        // this.zipCode = null;
        // this.phoneNumber = null;
        // this.faxNumber = null;
        // this.role = null;
        // this.cancerResearchGroupId = -1;
        // this.status = Constants.ACTIVITY_STATUS_NEW;
        // this.activityStatus = Constants.ACTIVITY_STATUS_NEW;
        // //Mandar : 24-Apr-06 : bug 972:
        // this.confirmEmailAddress = null;

    }

    /**
     * Copies the data from an AbstractDomain object to a UserForm object.
     *
     * @param abstractDomain
     *            An AbstractDomain object.
     */
    public void setAllValues(final AbstractDomainObject abstractDomain)
    {
        if (!Constants.PAGE_OF_CHANGE_PASSWORD.equals(getPageOf()))
        {
            final User user = (User) abstractDomain;

            setId(user.getId().longValue());
            lastName = user.getLastName();
            firstName = user.getFirstName();

            targetLoginName = user.getTargetIdpLoginName();

            setInstId(user);
            setInstId(user);
            emailAddress = user.getEmailAddress();

            // Mandar : 24-Apr-06 : bug id 972 : confirmEmailAddress
            confirmEmailAddress = emailAddress;
            setDptCRG(user);
            setAddr(user);
            // Populate the activity status, comments and role for approve user
            // and user edit.
            setUserData(user);
            setDptCRG(user);
            setAddr(user);
            // Populate the activity status, comments and role for approve user
            // and user edit.
            setUserData(user);
            if (Constants.PAGE_OF_USER_ADMIN.equals(getPageOf()))
            {
                setCsmUserId(user.getCsmUserId());
                try
                {
                    setPwd();
                }
                catch (final SMException e)
                {
                    UserForm.logger.error(e.getMessage(), e);
                    e.printStackTrace();
                }
            }
            if (Constants.PAGE_OF_USER_PROFILE.equals(getPageOf()))
            {
                role = user.getRoleId();
            }
        }
        logger.debug("this.activityStatus............." + getActivityStatus());
        logger.debug("this.comments" + comments);
        logger.debug("this.role" + role);
        logger.debug("this.status" + status);
        logger.debug("this.csmUserid" + csmUserId);
    }

    /**
     * @throws SMException
     *             SMException
     */
    private void setPwd() throws SMException
    {
        if (csmUserId != null) // in case user not approved
        {
            final gov.nih.nci.security.authorization.domainobjects.User csmUser = SecurityManagerFactory
                    .getSecurityManager().getUserById(getCsmUserId().toString());
            if (csmUser != null)
            {
                setNewPassword(csmUser.getPassword());
                setConfirmNewPassword(csmUser.getPassword());
            }
        }
        else
        {
            setNewPassword("");
            setConfirmNewPassword("");
        }
    }

    /**
     * @param user
     *            Set User
     */
    private void setUserData(final User user)
    {
        if ((getFormId() == Constants.APPROVE_USER_FORM_ID)
                || ((getPageOf() != null) && (Constants.PAGE_OF_USER_ADMIN.equals(getPageOf()))))
        {
            setActivityStatus(user.getActivityStatus());

            setCmts(user);
            setUserRole(user.getRoleId());
            if (getFormId() == Constants.APPROVE_USER_FORM_ID)
            {
                status = user.getActivityStatus();
                setStats();
            }
        }
    }

    /**
     * Setting the ActivityStatus.
     */
    private void setStats()
    {
        if (getActivityStatus().equals(Status.ACTIVITY_STATUS_ACTIVE.toString()))
        {
            status = Status.APPROVE_USER_APPROVE_STATUS.toString();
        }
        else if (getActivityStatus().equals(Status.ACTIVITY_STATUS_CLOSED.toString()))
        {
            status = Status.APPROVE_USER_REJECT_STATUS.toString();
        }
        else if (getActivityStatus().equals(Status.ACTIVITY_STATUS_NEW.toString()))
        {
            status = Status.APPROVE_USER_PENDING_STATUS.toString();
        }
    }

    /**
     * @param user
     *            User Object
     */
    private void setCmts(final User user)
    {
        if (!CommonUtilities.isNull(user.getComments()))
        {
            comments = user.getComments();
        }
    }

    /**
     * @param user
     *            User Object
     */
    private void setAddr(final User user)
    {
        if (!CommonUtilities.isNull(user.getAddress()))
        {
            street = user.getAddress().getStreet();
            city = user.getAddress().getCity();
            state = user.getAddress().getState();
            country = user.getAddress().getCountry();
            zipCode = user.getAddress().getZipCode();
            phoneNumber = user.getAddress().getPhoneNumber();
            faxNumber = user.getAddress().getFaxNumber();
        }
    }

    /**
     * @param user
     *            User Object
     */
    private void setDptCRG(final User user)
    {
        if (!CommonUtilities.isNull(user.getDepartment()))
        {
            departmentId = user.getDepartment().getId().longValue();
        }

        if (!CommonUtilities.isNull(user.getCancerResearchGroup()))
        {
            cancerResearchGroupId = user.getCancerResearchGroup().getId().longValue();
        }
    }

    /**
     * @param user
     *            User object
     */
    private void setInstId(final User user)
    {
        if (!CommonUtilities.isNull(user.getInstitution()))
        {
            institutionId = user.getInstitution().getId().longValue();
        }
    }

    /**
     * @param roleId
     *            Role Id
     */
    private void setUserRole(final String roleId)
    {
        role = "";
        if (roleId != null && roleId.equalsIgnoreCase(Constants.ADMIN_USER))
        {
            role = Constants.SUPER_ADMIN_USER;
        }
        if (roleId != null && roleId.equalsIgnoreCase(Constants.SUPER_ADMIN_USER))
        {
            role = Constants.SUPER_ADMIN_USER;
        }
    }

    /**
     * @param errors
     *            Object of ActionErrors
     * @param validator
     *            Object of Validator
     * @param key
     *            error key
     * @param value
     *            value of error key
     */
    private void chkEmpty(final ActionErrors errors, final Validator validator, final String key,
            final String value)
    {
        if (Validator.isEmpty(value))
        {
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required", ApplicationProperties
                    .getValue(key)));
        }

    }

    /**
     * Overrides the validate method of ActionForm.
     *
     * @return error ActionErrors instance
     * @param mapping
     *            Actionmapping instance
     * @param request
     *            HttpServletRequest instance
     */
    @Override
    public ActionErrors validate(final ActionMapping mapping, final HttpServletRequest request)
    {
        final ActionErrors errors = new ActionErrors();
        final Validator validator = new Validator();
        try
        {
            if (getOperation() != null)
            {
                if (getPageOf().equals(Constants.PAGE_OF_CHANGE_PASSWORD))
                {
                    chkEmpty(errors, validator, "user.oldPassword", oldPassword);
                    chkEmpty(errors, validator, "user.newPassword", newPassword);
                    chkEmpty(errors, validator, "user.confirmNewPassword", confirmNewPassword);
                    chkNwOdPwds(errors, validator);
                    chkNOPwds1(request, errors, validator);
                }
                else
                {
                    setRedirectValue(validator);
                    logger.debug("user form ");
                    ifAddEdit(errors, validator);

                    if (Constants.PAGE_OF_SIGNUP.equals(getPageOf()))
                    {
                        if ("yes".equalsIgnoreCase(idpSelection))
                        {
                            validateForEmptyMigrationFields(errors);
                        }
                    }

                    if (getPageOf().equals(Constants.PAGE_OF_APPROVE_USER))
                    {
                        chkValOpt(errors, validator, "user.approveOperation", status);
                        chkValOpt(errors, validator, "user.approveOperation", status);
                    }
                    // Bug- 1516:
                    adminEdit(request, errors, validator);
                    // Mandar 10-apr-06 : bugid :353 end
                    // Bug- 1516:
                    adminEdit(request, errors, validator);
                    // Mandar 10-apr-06 : bugid :353 end
                }
               // validateWashUEmailAddress(errors);
            }
        }
        catch (final Exception excp)
        {
            UserForm.logger.error(excp.getMessage(), excp);
            excp.printStackTrace();
        }
        return errors;
    }

    private void validateForEmptyMigrationFields(final ActionErrors errors)
    {
        if (Validator.isEmpty(idpSelection))
        {
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("app.target.idp.question.not.answered", ""));
        }
        else if ("yes".equalsIgnoreCase(idpSelection))
        {
            if (Validator.isEmpty(targetLoginName) && Validator.isEmpty(targetPassword))
            {
                errors.add(ActionErrors.GLOBAL_ERROR,
                        new ActionError("app.target.loginname.password.required", ""));
            }
            if (Validator.isEmpty(targetLoginName))
            {
                errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",
                        ApplicationProperties.getValue("user.loginName")));
            }
            if (Validator.isEmpty(targetPassword))
            {
                errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",
                        ApplicationProperties.getValue("user.password")));
            }
        }
    }

//    /**
//     * @param errors
//     *            ActionErrors
//     */
//    private void validateWashUEmailAddress(final ActionErrors errors)
//    {
//        if (targetLoginName != null && !"".equals(targetLoginName))
//        {
//            if (!emailAddress.endsWith(edu.wustl.wustlkey.util.global.Constants.WUSTL_EDU)
//                    && !emailAddress.endsWith(edu.wustl.wustlkey.util.global.Constants.WUSTL_EDU_CAPS))
//            {
//                errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",
//                        ApplicationProperties.getValue("email.washu.user")));
//            }
//            if (!confirmEmailAddress.endsWith(edu.wustl.wustlkey.util.global.Constants.WUSTL_EDU)
//                    && !confirmEmailAddress.endsWith(edu.wustl.wustlkey.util.global.Constants.WUSTL_EDU_CAPS))
//            {
//                errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",
//                        ApplicationProperties.getValue("confirm.washu.user")));
//            }
//        }
//    }

    /**
     * @param errors
     *            Object of ActionErrors
     * @param validator
     *            Object of Validator
     */
    private void ifAddEdit(final ActionErrors errors, final Validator validator)
    {
        if (getOperation().equals(Constants.ADD) || getOperation().equals(Constants.EDIT))
        {
            chkSites(errors, validator);
            chkMail(errors, validator, "user.emailAddress", emailAddress);

            // Mandar : 24-Apr-06 Bugid:972 confirmEmailAddress start
            if (!getPageOf().equals(Constants.PAGE_OF_USER_PROFILE))
            {
                chkMail(errors, validator, "user.confirmemailAddress", confirmEmailAddress);
                compMail(errors);
            }
            // Mandar : 24-Apr-06 Bugid:972 confirmEmailAddress end

            chkNames(errors, validator, "user.lastName", lastName);
            chkNames(errors, validator, "user.firstName", firstName);
            chkEmpty(errors, validator, "user.city", city);
            if (edu.wustl.catissuecore.util.global.Variables.isStateRequired)
            {
                chkValOpt(errors, validator, "user.state", state);
            }
            chkValOpt(errors, validator, "user.country", country);
            chkValOpt(errors, validator, "user.institution", String.valueOf(institutionId));
            chkValOpt(errors, validator, "user.department", String.valueOf(departmentId));
            chkValOpt(errors, validator, "user.cancerResearchGroup", String.valueOf(cancerResearchGroupId));
            chkValOpt(errors, validator, "user.activityStatus", getActivityStatus());
        }
    }

    /**
     * @param errors
     *            Object of ActionErrors
     * @param validator
     *            Object of Validator
     * @param key
     *            error key
     * @param value
     *            value of error key
     */
    private void chkNames(final ActionErrors errors, final Validator validator, final String key,
            final String value)
    {
        if (Validator.isEmpty(value))
        {
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required", ApplicationProperties
                    .getValue(key)));
        }
        else if (Validator.isXssVulnerable(value))
        { // Bug:7976 & 7977: added check for xxs vulnerable
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.xss.invalid", ApplicationProperties
                    .getValue(key)));
        }
    }

    /**
     * @param errors
     *            Object of ActionErrors
     * @param validator
     *            Object of Validator
     * @param request
     *            HttpServletRequest
     */
    private void adminEdit(final HttpServletRequest request, final ActionErrors errors, final Validator validator)
    {
        if (getPageOf().equals(Constants.PAGE_OF_USER_ADMIN) && getOperation().equals(Constants.EDIT))
        {
            final String pageFrom = request.getParameter("pageFrom");
            if (!"ApproveUser".equals(pageFrom))
            {
                chkEmpty(errors, validator, "user.newPassword", newPassword);
                chkEmpty(errors, validator, "user.confirmNewPassword", confirmNewPassword);

                chkNwOdPwds(errors, validator);
                // Bug-7979
                chkPwd(request, errors);
            }
        }
    }

    /**
     * @param request
     *            HttpServletRequest
     * @param errors
     *            Object of ActionErrors
     */
    private void chkPwd(final HttpServletRequest request, final ActionErrors errors)
    {
        int result = PasswordManager.SUCCESS;
        if (!Constants.DUMMY_PASSWORD.equals(newPassword))
        {
            final Boolean isPasswordChanged = (Boolean) request.getSession().getAttribute(
                    Constants.PASSWORD_CHANGE_IN_SESSION);
            result = PasswordManager.validatePasswordOnFormBean(newPassword, oldPassword, isPasswordChanged);
        }

        if (result != PasswordManager.SUCCESS)
        {
            // get error message of validation failure where param is result of
            // validate() method
            final String errorMessage = PasswordManager.getErrorMessage(result);
            logger.debug("error from Password validate " + errorMessage);
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item", errorMessage));
        }
    }

    /**
     * @param errors
     *            Object of ActionErrors
     * @param validator
     *            Object of Validator
     * @param key
     *            error key
     * @param value
     *            value of error key
     */
    private void chkValOpt(final ActionErrors errors, final Validator validator, final String key,
            final String value)
    {
        if (!validator.isValidOption(value))
        {
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required", ApplicationProperties
                    .getValue(key)));
        }
    }

    /**
     * @param errors
     *            Object of ActionErrors
     */
    private void compMail(final ActionErrors errors)
    {
        if (!confirmEmailAddress.equals(emailAddress))
        {
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.email.mismatch"));
        }
    }

    /**
     * @param errors
     *            Object of ActionErrors
     * @param validator
     *            Object of Validator
     * @param key
     *            error key
     * @param value
     *            value of error key
     */
    private void chkMail(final ActionErrors errors, final Validator validator, final String key, final String value)
    {
        if (Validator.isEmpty(value))
        {
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required", ApplicationProperties
                    .getValue(key)));
        }
        else
        {
            if (!validator.isValidEmailAddress(value))
            {
                errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.format", ApplicationProperties
                        .getValue(key)));
            }
        }
    }

    /**
     * @param errors
     *            Object of ActionErrors
     * @param validator
     *            Object of Validator
     */
    private void chkSites(final ActionErrors errors, final Validator validator)
    {
        if (siteIds != null && siteIds.length > 0 && Validator.isEmpty(role))
        {
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required", ApplicationProperties
                    .getValue("user.roleIsRequired")));
        }
    }

    /**
     * @param request
     *            Object of HttpServletRequest
     * @param errors
     *            Object of ActionErrors
     * @param validator
     *            Object of Validator
     */
    private void chkNOPwds1(final HttpServletRequest request, final ActionErrors errors, final Validator validator)
    {
        if (!Validator.isEmpty(newPassword) && !Validator.isEmpty(oldPassword))
        {
            // Call static method PasswordManager.validatePasswordOnFormBean()
            // where params are
            // new password,old password,user name
            final Boolean isPasswordChanged = (Boolean) request.getSession().getAttribute(
                    Constants.PASSWORD_CHANGE_IN_SESSION);
            final int result = PasswordManager.validatePasswordOnFormBean(newPassword, oldPassword,
                    isPasswordChanged);

            if (result != PasswordManager.SUCCESS)
            {
                final String errorMessage = PasswordManager.getErrorMessage(result);
                logger.debug("error from Password validate " + errorMessage);
                errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item", errorMessage));
            }
        }
    }

    /**
     * @param errors
     *            Object of ActionErrors
     * @param validator
     *            Object of validator
     */
    private void chkNwOdPwds(final ActionErrors errors, final Validator validator)
    {
        if (!Validator.isEmpty(newPassword) && !Validator.isEmpty(confirmNewPassword))
        {
            if (!newPassword.equals(confirmNewPassword))
            {
                errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.confirmNewPassword.reType"));
            }
        }
    }

    /**
     * This method sets Identifier of Objects inserted by AddNew activity in
     * Form-Bean which initialized AddNew action.
     *
     * @param addNewFor
     *            - FormBean ID of the object inserted
     * @param addObjectIdentifier
     *            - Identifier of the Object inserted
     */
    @Override
    public void setAddNewObjectIdentifier(final String addNewFor, final Long addObjectIdentifier)
    {
        if (addNewFor.equals("institution"))
        {
            setInstitutionId(addObjectIdentifier.longValue());
        }
        else if (addNewFor.equals("department"))
        {
            setDepartmentId(addObjectIdentifier.longValue());
        }
        else if (addNewFor.equals("cancerResearchGroup"))
        {
            setCancerResearchGroupId(addObjectIdentifier.longValue());
        }
    }

}