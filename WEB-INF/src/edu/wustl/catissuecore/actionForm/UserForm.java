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

import edu.wustl.catissuecore.domain.AbstractDomainObject;
import edu.wustl.catissuecore.domain.SignUpUser;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.global.ApplicationProperties;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Validator;
import edu.wustl.common.util.logger.Logger;

/**
 * UserForm Class is used to encapsulate all the request parameters passed 
 * from User Add/Edit webpage.
 * @author gautam_shetty
 * */
public class UserForm extends AbstractActionForm
{

    /**
     * systemIdentifier is a unique id assigned to each User.
     * */
    private long systemIdentifier;

    /**
     * Represents the operation(Add/Edit) to be performed.
     * */
    private String operation;

    /**
     * Represents the page the user has submitted the form from.
     */
    private String pageOf;

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
    private String state;

    /**
     * The Country where the user stays.
     */
    private String country;

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
    private String role;

    /**
     * Cancer Research Group of the user.  
     */
    private long cancerResearchGroupId;

    /**
     * Activity status of the user.
     */
    private String activityStatus;

    /**
     * Comments given by user.
     */
    private String comments;

    /**
     * Status of user in the system.
     */
    private String status;

    /**
     * No argument constructor for UserForm class. 
     */
    public UserForm()
    {
        reset();
    }

    /**
     * Returns the systemIdentifier assigned to User.
     * @return int representing the id assigned to User.
     * @see #setIdentifier(int)
     * */
    public long getSystemIdentifier()
    {
        return (this.systemIdentifier);
    }

    /**
     * Sets an id for the User.
     * @param systemIdentifier id to be assigned to the User.
     * @see #getIdentifier()
     * */
    public void setSystemIdentifier(long systemIdentifier)
    {
        this.systemIdentifier = systemIdentifier;
    }

    /**
     * Returns the operation(Add/Edit) to be performed.
     * @return Returns the operation.
     */
    public String getOperation()
    {
        return operation;
    }

    /**
     * Sets the operation to be performed.
     * @param operation The operation to set.
     */
    public void setOperation(String operation)
    {
        this.operation = operation;
    }

    /**
     * @return Returns the pageOf.
     */
    public String getPageOf()
    {
        return pageOf;
    }

    /**
     * @param pageOf The pageOf to set.
     */
    public void setPageOf(String pageOf)
    {
        this.pageOf = pageOf;
    }

    /**
     * Returns the last name of the user 
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
     * @param institutionId String representing the institutionId of the user.
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
     * @param departmentId String representing departmentId of the user.
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
        return cancerResearchGroupId;
    }

    /**
     * Sets the cancer research group the user belongs.
     * @param cancerResearchGroupId The cancerResearchGroupId to set.
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
     * @param address String representing mailing address of the user.
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
        return zipCode;
    }

    /**
     * Sets the zip code of the user's city.
     * @param zip The zip code to set.
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
        return phoneNumber;
    }

    /**
     * Sets the phone number of the user. 
     * @param phone The phone number to set.
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
     * @param fax The fax to set.
     * @see #getFax()
     */
    public void setFaxNumber(String faxNumber)
    {
        this.faxNumber = faxNumber;
    }

    /**
     * Returns the role of the user.
     * @return the role of the user.
     * @see #setRoleCollection(String)
     */
    public String getRole()
    {
        return role;
    }

    /**
     * Sets the role of the user.
     * @param role the role of the user.
     * @see #getRole()
     */
    public void setRole(String role)
    {
        this.role = role;
    }

    /**
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
     * @return Returns the comments.
     */
    public String getComments()
    {
        return comments;
    }

    /**
     * @param comments The comments to set.
     */
    public void setComments(String comments)
    {
        this.comments = comments;
    }

    /**
     * Checks the operation to be performed is add operation.
     * @return Returns true if operation is equal to "add", else it returns false.
     * */
    public boolean isAddOperation()
    {
        return (getOperation().equals(Constants.ADD));
    }

    /**
     * Returns the id assigned to form bean
     */
    public int getFormId()
    {
        int formId = Constants.SIGNUP_FORM_ID;
        if (pageOf != null)
        {
            if (pageOf.equals(Constants.PAGEOF_APPROVE_USER))
            {
                formId = Constants.APPROVE_USER_FORM_ID;
                if (this.status.equals(Constants.APPROVE_USER_PENDING_STATUS))
                    formId = Constants.SIGNUP_FORM_ID;
            }
            else if (pageOf.equals(Constants.PAGEOF_USER_ADMIN))
                formId = Constants.USER_FORM_ID;
        }
        
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
     * @param status The status to set.
     */
    public void setStatus(String status)
    {
        this.status = status;
    }
   
    /**
     * Resets the values of all the fields.
     * Is called by the overridden reset method defined in ActionForm.  
     * */
    protected void reset()
    {
        this.systemIdentifier = -1;
        this.operation = null;
        this.lastName = null;
        this.firstName = null;
        this.institutionId = -1;
        this.emailAddress = null;
        this.departmentId = -1;
        this.street = null;
        this.city = null;
        this.state = null;
        this.country = null;
        this.zipCode = null;
        this.phoneNumber = null;
        this.faxNumber = null;
        this.role = null;
        this.cancerResearchGroupId = -1;
        this.status = Constants.ACTIVITY_STATUS_NEW;
    }
    
    /**
     * Copies the data from an AbstractDomain object to a UserForm object.
     * @param user An AbstractDomain object.  
     */
    public void setAllValues(AbstractDomainObject abstractDomain)
    {
        try
        {
            if (getFormId() == Constants.SIGNUP_FORM_ID)
            {
                SignUpUser signUpUser = (SignUpUser)abstractDomain;
                
                this.systemIdentifier = signUpUser
                			.getSystemIdentifier().longValue();
                this.lastName = signUpUser.getLastName();
                this.firstName = signUpUser.getFirstName();
                this.institutionId = signUpUser.getInstitution()
                			.getSystemIdentifier().longValue();
                this.emailAddress = signUpUser.getEmailAddress();
                this.departmentId = signUpUser.getDepartment()
                			.getSystemIdentifier().longValue();
                this.cancerResearchGroupId = signUpUser.getCancerResearchGroup()
                			.getSystemIdentifier().longValue();

                this.street = signUpUser.getStreet();
                this.city = signUpUser.getCity();
                this.state = signUpUser.getState();
                this.country = signUpUser.getCountry();
                this.zipCode = signUpUser.getZipCode();
                this.phoneNumber = signUpUser.getPhoneNumber();
                this.faxNumber = signUpUser.getFaxNumber();
                
                this.activityStatus = signUpUser.getActivityStatus();
                if (activityStatus.equals(Constants.ACTIVITY_STATUS_ACTIVE))
                {
                    this.status = Constants.APPROVE_USER_APPROVE_STATUS;
                }
                
                else if (activityStatus.equals(Constants.ACTIVITY_STATUS_CLOSED))
                {
                    this.status = Constants.APPROVE_USER_REJECT_STATUS;
                }
                else
                {
                    this.status = Constants.APPROVE_USER_PENDING_STATUS;
                }
                
                this.role = signUpUser.getRoleId();
                this.comments = signUpUser.getComments();
            }
            else
            {
                User user = (User) abstractDomain;
                
                this.systemIdentifier = user.getSystemIdentifier().longValue();
                this.lastName = user.getLastName();
                this.firstName = user.getFirstName();
                this.institutionId = user.getInstitution().getSystemIdentifier()
                        .longValue();
                this.emailAddress = user.getEmailAddress();
                this.departmentId = user.getDepartment().getSystemIdentifier()
                        .longValue();
                this.cancerResearchGroupId = user.getCancerResearchGroup()
                .getSystemIdentifier().longValue();
                
                this.street = user.getAddress().getStreet();
                this.city = user.getAddress().getCity();
                this.state = user.getAddress().getState();
                this.country = user.getAddress().getCountry();
                this.zipCode = user.getAddress().getZipCode();
                this.phoneNumber = user.getAddress().getPhoneNumber();
                this.faxNumber = user.getAddress().getFaxNumber();
                
                this.activityStatus = user.getActivityStatus();
                if (activityStatus.equals(Constants.ACTIVITY_STATUS_ACTIVE))
                {
                    this.status = Constants.APPROVE_USER_APPROVE_STATUS;
                }
                else if (activityStatus.equals(Constants.ACTIVITY_STATUS_NEW))
                {
                    this.status = Constants.APPROVE_USER_PENDING_STATUS;
                }
                else if (activityStatus.equals(Constants.ACTIVITY_STATUS_CLOSED))
                {
                    this.status = Constants.APPROVE_USER_REJECT_STATUS;
                }
                
                this.role = user.getRoleId();
                this.comments = user.getComments();
            }
        }
        catch (Exception excp)
        {
            Logger.out.error(excp.getMessage());
        }
    }
    
    /**
     * Overrides the validate method of ActionForm.
     * */
    public ActionErrors validate(ActionMapping mapping,
            HttpServletRequest request)
    {
        ActionErrors errors = new ActionErrors();
        Validator validator = new Validator();
        try
        {
            if (operation != null)
            {
                if (operation.equals(Constants.ADD)
                        || operation.equals(Constants.EDIT))
                {
                    if (validator.isEmpty(lastName))
                    {
                        errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
                                "errors.item.required", ApplicationProperties
                                        .getValue("user.lastName")));
                    }

                    if (validator.isEmpty(firstName))
                    {
                        errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
                                "errors.item.required", ApplicationProperties
                                        .getValue("user.firstName")));
                    }

                    if (validator.isEmpty(city))
                    {
                        errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
                                "errors.item.required", ApplicationProperties
                                        .getValue("user.city")));
                    }

                    if (state.trim().equals(Constants.SELECT_OPTION))
                    {
                        errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
                                "errors.item.required", ApplicationProperties
                                        .getValue("user.state")));
                    }

                    if (validator.isEmpty(zipCode))
                    {
                        errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
                                "errors.item.required", ApplicationProperties
                                        .getValue("user.zipCode")));
                    }

                    if (country.trim().equals(Constants.SELECT_OPTION))
                    {
                        errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
                                "errors.item.required", ApplicationProperties
                                        .getValue("user.country")));
                    }
                    
                    if (validator.isEmpty(phoneNumber))
                    {
                        errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
                                "errors.item.required", ApplicationProperties
                                        .getValue("user.phoneNumber")));
                    }

                    if (validator.isEmpty(emailAddress))
                    {
                        errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
                                "errors.item.required", ApplicationProperties
                                        .getValue("user.emailAddress")));
                    }
                    else
                    {
                        if (!validator.isValidEmailAddress(emailAddress))
                        {
                            errors.add(ActionErrors.GLOBAL_ERROR,
                                            new ActionError("errors.item.format",
                                                    ApplicationProperties.getValue("user.emailAddress")));
                        }
                    }

                    if (institutionId == -1)
                    {
                        errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
                                "errors.item.required", ApplicationProperties
                                        .getValue("user.institution")));
                    }

                    if (departmentId == -1)
                    {
                        errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
                                "errors.item.required", ApplicationProperties
                                        .getValue("user.department")));
                    }
                    
                    if (cancerResearchGroupId == -1)
                    {
                        errors.add(ActionErrors.GLOBAL_ERROR,
                                        new ActionError("errors.item.required",
                                                ApplicationProperties.getValue("user.cancerResearchGroup")));
                    }

                }

                if (pageOf.equals(Constants.PAGEOF_USER_ADMIN) || pageOf.equals(Constants.PAGEOF_APPROVE_USER))
                {
                    if (role != null)
                    {
                        if (role.trim().equals("0"))
	                    {
	                        errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
	                                "errors.item.required", ApplicationProperties
	                                        .getValue("user.role")));
	                    }
                    }
                }

                if (pageOf.equals(Constants.PAGEOF_APPROVE_USER))
                {
                    if (status.trim().equals(Constants.SELECT_OPTION))
                    {
                        errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
                                "errors.item.required", ApplicationProperties
                                        .getValue("user.approveOperation")));
                    }
                }
            }
        }
        catch (Exception excp)
        {
            Logger.out.error(excp.getMessage(), excp);
        }
        
        return errors;
    }
}