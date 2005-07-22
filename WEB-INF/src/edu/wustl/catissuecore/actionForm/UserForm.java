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
     * login name of the user.
     */
    private String loginName;

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
    private String institution;

    /**
     * EmailAddress Address of the user.
     */
    private String emailAddress;

    /**
     * Department name of the user.
     */
    private String department;

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
    private String cancerResearchGroup;

    /**
     * Activity status of the user.
     */
    private String activityStatus;

    /**
     * Comments given by user.
     */
    private String comments;

    /**
     * No argument constructor for UserForm class. 
     */
    public UserForm()
    {
        reset();
    }

    /**
     * Copies the data from an AbstractDomain object to a UserForm object.
     * @param user An AbstractDomain object.  
     */
    public void setAllValues(AbstractDomainObject abstractDomain)
    {
        try
        {
            User user = (User) abstractDomain;
            this.systemIdentifier = user.getSystemIdentifier().longValue();
            this.loginName = user.getUser().getLoginName();
            this.lastName = user.getUser().getLastName();
            this.firstName = user.getUser().getFirstName();
            this.institution = user.getInstitution().getName();
            this.emailAddress = user.getAddress().getEmailAddress();
            this.department = user.getDepartment().getName();
            this.street = user.getAddress().getStreet();
            this.city = user.getAddress().getCity();
            this.state = user.getAddress().getState();
            this.country = user.getAddress().getCountry();
            this.zipCode = user.getAddress().getZipCode();
            this.phoneNumber = user.getAddress().getPhoneNumber();
            this.faxNumber = user.getAddress().getFaxNumber();
            //            this.role = user.getRole().getName();
        }
        catch (Exception excp)
        {
            excp.printStackTrace();
            Logger.out.error(excp.getMessage());
        }
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
     * Returns the login name of the user.
     * @return String representing the login name of the user
     * @see #setLoginName(String)
     */
    public String getLoginName()
    {
        return (this.loginName);
    }

    /**
     * Sets the login name of this user
     * @param loginName login name of the user.
     * @see #getLoginName()
     */
    public void setLoginName(String loginName)
    {
        this.loginName = loginName;
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
     * Returns the institution name of the user.
     * @return String representing the institution of the user. 
     * @see #setinstitution(String)
     */
    public String getInstitution()
    {
        return (this.institution);
    }

    /**
     * Sets the institution name of the user.
     * @param institution String representing the institution of the user.
     * @see #getinstitution()
     */
    public void setInstitution(String institution)
    {
        this.institution = institution;
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
     * @return String representing department of the user.
     * @see #getDepartment()
     */
    public String getDepartment()
    {
        return (this.department);
    }

    /**
     * Sets the Department Name of the user.
     * @param department String representing department of the user.
     * @see #getDepartment()
     */
    public void setDepartment(String department)
    {
        this.department = department;
    }

    /**
     * Returns the cancer research group the user belongs.
     * @return Returns the cancerResearchGroup.
     * @see #setCancerResearchGroup(String)
     */
    public String getCancerResearchGroup()
    {
        return cancerResearchGroup;
    }

    /**
     * Sets the cancer research group the user belongs.
     * @param cancerResearchGroup The cancerResearchGroup to set.
     * @see #getCancerResearchGroup()
     */
    public void setCancerResearchGroup(String cancerResearchGroup)
    {
        this.cancerResearchGroup = cancerResearchGroup;
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
     * @return Returns true if operation is equal to "add", else it returns false
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
        return Constants.USER_FORM_ID;
    }

    /**
     * Resets the values of all the fields.
     * This method defined in ActionForm is overridden in this class.
     */
    public void reset(ActionMapping mapping, HttpServletRequest request)
    {
        reset();
    }

    /**
     * Resets the values of all the fields.
     * Is called by the overridden reset method defined in ActionForm.  
     * */
    private void reset()
    {
        this.systemIdentifier = -1;
        this.operation = null;
        this.loginName = null;
        this.lastName = null;
        this.firstName = null;
        this.institution = null;
        this.emailAddress = null;
        this.department = null;
        this.street = null;
        this.city = null;
        this.state = null;
        this.country = null;
        this.zipCode = null;
        this.phoneNumber = null;
        this.faxNumber = null;
        this.role = null;
        this.cancerResearchGroup = null;
        this.activityStatus = Constants.ACTIVITY_STATUS_NEW;
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
            if (operation.equals(Constants.SEARCH))
            {
                checkValidNumber(new Long(systemIdentifier).toString(),
                        "user.systemIdentifier", errors, validator);
            }
            if (operation.equals(Constants.ADD)
                    || operation.equals(Constants.EDIT))
            {
                if (validator.isEmpty(loginName))
                {
                    errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
                            "errors.item.required", ApplicationProperties
                                    .getValue("user.loginName")));
                }
                else
                {
                    if (!Character.isLetter(loginName.charAt(0)))
                    {
                        errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
                                "errors.item.format", ApplicationProperties
                                        .getValue("user.loginName")));
                    }
                }

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

                if (validator.isEmpty(street))
                {
                    errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
                            "errors.item.required", ApplicationProperties
                                    .getValue("user.address")));
                }

                if (validator.isEmpty(emailAddress))
                {
                    errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
                            "errors.item.required", ApplicationProperties
                                    .getValue("user.email")));
                }
                else
                {
                    if (!validator.isValidEmailAddress(emailAddress))
                    {
                        errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
                                "errors.item.format", ApplicationProperties
                                        .getValue("user.email")));
                    }
                }

                checkValidString(city, "user.city", errors, validator);
                checkValidNumber(zipCode, "user.zip", errors, validator);

//                if (role == null)
//                {
//                    errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
//                            "errors.item.required", ApplicationProperties
//                                    .getValue("user.role")));
//                }
            }
            if (operation.equals(Constants.FORGOT_PASSWORD))
            {
                if ((validator.isEmpty(loginName))
                        && (validator.isEmpty(emailAddress)))
                {
                    errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
                            "errors.forgotpassword.required"));
                }
                else
                {
                    if (!validator.isValidEmailAddress(emailAddress)
                            && (!validator.isEmpty(emailAddress)))
                    {
                        errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
                                "errors.item.format", ApplicationProperties
                                        .getValue("user.emailAddress")));
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