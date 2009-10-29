/**
 * <p>Title: SiteForm Class>
 * <p>Description:  This Class is used to encapsulate all the request parameters passed 
 * from Site.jsp page. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00
 * Created on Jul 18, 2005
 */

package edu.wustl.catissuecore.actionForm;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.DefaultValueManager;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;

/**
 * This Class is used to encapsulate all the request parameters passed from Site.jsp page.
 * @author aniruddha_phadnis
 * */
public class SiteForm extends AbstractActionForm
{

	private static final long serialVersionUID = 1L;

	/**
	 * logger Logger - Generic logger.
	 */
	private static Logger logger = Logger.getCommonLogger(SiteForm.class);
	/**
	 * Name of the site.
	 */
	private String name;

	/**
	 * EmailAddress Address of the site.
	 */
	private String emailAddress;

	/**
	* Street Address of the site.
	*/
	private String street;

	/**
	 * The City in which the site is.
	 */
	private String city;

	/**
	 * Name : Virender Mehta
	 * Reviewer: Sachin Lale
	 * Bug ID: defaultValueConfiguration_BugID
	 * Patch ID:defaultValueConfiguration_BugID_4
	 * Description: Configuration for default value for Type, State and country
	 */

	/**
	 * A string containing the type of the storage.
	 */
	private String type = (String) DefaultValueManager.getDefaultValue(Constants.DEFAULT_SITE_TYPE);

	private String state = (String) DefaultValueManager.getDefaultValue(Constants.DEFAULT_STATES);

	private String country = (String) DefaultValueManager
			.getDefaultValue(Constants.DEFAULT_COUNTRY);

	/**
	 * The zip code of city where the site is.
	 */
	private String zipCode;

	/**
	 * Phone number of the site.
	 * */
	private String phoneNumber;

	/**
	 * Fax number of the site.
	 */
	private String faxNumber;

	/**
	 * Id of the coordinator associated with the site.
	 */
	private long coordinatorId;

	/**
	 * No argument constructor for StorageTypeForm class 
	 */
	public SiteForm()
	{
		this.reset();
	}

	/**
	 * This function Copies the data from an site object to a SiteForm object.
	 * @param abstractDomain An object containing the information about site.  
	 */
	public void setAllValues(AbstractDomainObject abstractDomain)
	{
		final Site site = (Site) abstractDomain;

		this.setId(site.getId().longValue());
		this.name = site.getName();
		this.type = site.getType();
		this.emailAddress = site.getEmailAddress();
		this.street = site.getAddress().getStreet();
		this.city = site.getAddress().getCity();
		this.state = site.getAddress().getState();
		this.country = site.getAddress().getCountry();
		this.zipCode = site.getAddress().getZipCode();
		this.phoneNumber = site.getAddress().getPhoneNumber();
		this.faxNumber = site.getAddress().getFaxNumber();
		this.setActivityStatus(site.getActivityStatus());
		this.coordinatorId = site.getCoordinator().getId().longValue();
	}

	/**
	 * Returns the name of the site.
	 * @return the name of the site.
	 * @see #setName(String)
	 */
	public String getName()
	{
		return this.name;
	}

	/**
	 * Sets the name of the site.
	 * @param name the name to of the site.
	 * @see #getName()
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * Returns the type of the site.
	 * @return the type of the site.
	 * @see #setType(String) 
	 */
	public String getType()
	{
		return this.type;
	}

	/**
	 * Sets the type of the site.
	 * @param type the type of the site.
	 * @see #getType()
	 */
	public void setType(String type)
	{
		this.type = type;
	}

	/**
	 * Returns the id of the coordinator.
	 * @return the id of the coordinator.
	 * @see #setCoordinatorId(long) 
	 */
	public long getCoordinatorId()
	{
		return this.coordinatorId;
	}

	/**
	 * @param coordinatorId The coordinatorId to set.
	 */
	public void setCoordinatorId(long coordinatorId)
	{
		this.coordinatorId = coordinatorId;
	}

	/**
	 * Returns the emailAddress Address of the site.
	 * @return String representing the emailAddress address of the site.
	 */
	public String getEmailAddress()
	{
		return this.emailAddress;
	}

	/**
	 * Sets the emailAddress address of the site.
	 * @param emailAddress String representing emailAddress address of the site.
	 * @see #getEmailAddress()
	 */
	public void setEmailAddress(String emailAddress)
	{
		this.emailAddress = emailAddress;
	}

	/**
	 * Returns the Street Address of the site.
	 * @return String representing mailing address of the site.
	 * @see #setStreet(String)
	 */
	public String getStreet()
	{
		return this.street;
	}

	/**
	 * Sets the Street Address of the site.
	 * @param street String representing mailing address of the site.
	 * @see #getStreet()
	 */
	public void setStreet(String street)
	{
		this.street = street;
	}

	/**
	 * Returns the City where the site is.
	 * @return String representing city where the site is.
	 * @see #setCity(String)
	 */
	public String getCity()
	{
		return this.city;
	}

	/**
	 * Sets the City where the site is.
	 * @param city String name of the city where the site is.
	 * @see #getCity()
	 */
	public void setCity(String city)
	{
		this.city = city;
	}

	/**
	 * Returns the State where the site is.
	 * @return String representing state where the site is.
	 * @see #setState(String)
	 */
	public String getState()
	{
		return this.state;
	}

	/**
	 * Sets the State where the site is.
	 * @param state String representing state where the site is.
	 * @see #getState()
	 */
	public void setState(String state)
	{
		this.state = state;
	}

	/**
	 * Returns the Country where the site is.
	 * @return String representing country where the site is.
	 * @see #setCountry(String)
	 */
	public String getCountry()
	{
		return this.country;
	}

	/**
	 * Sets the Country where the site is.
	 * @param country String representing country where the site is.
	 * @see #getCountry()
	 */
	public void setCountry(String country)
	{
		this.country = country;
	}

	/**
	 * Returns the zip code of the city where the site is. 
	 * @return Returns the zip.
	 * @see #setZip(String)
	 */
	public String getZipCode()
	{
		return this.zipCode;
	}

	/**
	 * Sets the zip code of the city where the site is.
	 * @param zipCode The zip code of the city where the site is.
	 * @see #getZip()
	 */
	public void setZipCode(String zipCode)
	{
		this.zipCode = zipCode;
	}

	/**
	 * Returns the phone number of the site.
	 * @return Returns the phone number.
	 * @see #setPhone(String)
	 */
	public String getPhoneNumber()
	{
		return this.phoneNumber;
	}

	/**
	 * Sets the phone number of the site. 
	 * @param phoneNumber The phone number to site.
	 * @see #getphoneNumber()
	 */
	public void setPhoneNumber(String phoneNumber)
	{
		this.phoneNumber = phoneNumber;
	}

	/**
	 * Returns the fax number of the site.
	 * @return Returns the fax.
	 * @see #setFax(String)
	 */
	public String getFaxNumber()
	{
		return this.faxNumber;
	}

	/**
	 * Sets the fax number of the site.
	 * @param faxNumber The fax number of the site.
	 * @see #getFax()
	 */
	public void setFaxNumber(String faxNumber)
	{
		this.faxNumber = faxNumber;
	}

	/**
	 * @return Returns the id assigned to form bean.
	 */
	@Override
	public int getFormId()
	{
		return Constants.SITE_FORM_ID;
	}

	/**
	 * Resets the values of all the fields.
	 * Is called by the overridden reset method defined in ActionForm.  
	 * */
	@Override
	protected void reset()
	{

		//    	  this.country = Constants.DEFAULT_COUNTRY_NAME;
		//    	  this.name			= null;
		//        this.type			= null;
		//        this.emailAddress	= null;
		//        this.street			= null;
		//        this.city			= null;
		//        this.state			= null;
		//        this.zipCode		= null;
		//        this.phoneNumber	= null;
		//        this.faxNumber		= null;
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
			this.setRedirectValue(validator);

			// Mandar 10-apr-06 : bugid :353 
			// Error messages should be in the same sequence as the sequence of fields on the page.

			if (Validator.isEmpty(this.name))
			{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",
						ApplicationProperties.getValue("site.name")));
			}

			//         	if (validator.isEmpty(type))
			//            {
			//                errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("site.type")));
			//            }
			if (!validator.isValidOption(this.type))
			{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",
						ApplicationProperties.getValue("site.type")));
			}
			if (this.coordinatorId == -1L)
			{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",
						ApplicationProperties.getValue("site.coordinator")));
			}

			if (!Validator.isEmpty(this.emailAddress)
					&& !validator.isValidEmailAddress(this.emailAddress))
			{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.format",
						ApplicationProperties.getValue("site.emailAddress")));
			}

			if (Validator.isEmpty(this.street))
			{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",
						ApplicationProperties.getValue("site.street")));
			}
			if (Validator.isEmpty(this.city))
			{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",
						ApplicationProperties.getValue("site.city")));
			}
			if (!validator.isValidOption(this.state))
			{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",
						ApplicationProperties.getValue("site.state")));
			}

			if (!validator.isValidOption(this.country))
			{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",
						ApplicationProperties.getValue("site.country")));
			}

			//checkValidNumber(zipCode, "site.zipCode", errors, validator); // commented as validation is done in following code
			// added for zip code , phone and fax number validation
			/*
			 *  Commented by Geeta to remove the mask on zipcode
			 if (validator.isEmpty(zipCode))
			 {
				 errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
			            "errors.item.required", ApplicationProperties
			                    .getValue("site.zipCode")));
			 }	
			 else
			 {
				 if(!validator.isValidZipCode(zipCode))
				 {
					 errors.add(ActionErrors.GLOBAL_ERROR,
			                new ActionError("errors.zipCode.format",
			                        ApplicationProperties.getValue("site.zipCode")));
				 }	
			 }
			
			*/
			//             if(!validator.isEmpty(phoneNumber)&& !validator.isValidPhoneNumber(phoneNumber))
			//         	 {
			//         		errors.add(ActionErrors.GLOBAL_ERROR,
			//                         new ActionError("errors.phoneNumber.format",
			//                                 ApplicationProperties.getValue("site.phoneNumber")));
			//         	 }
			//             if(!validator.isEmpty(faxNumber)&& !validator.isValidPhoneNumber(faxNumber))
			//             {
			//             	errors.add(ActionErrors.GLOBAL_ERROR,
			//                         new ActionError("errors.phoneNumber.format",
			//                                 ApplicationProperties.getValue("site.faxNumber")));
			//             }
			if (this.getOperation().equals(Constants.EDIT)
					&& !validator.isValidOption(this.getActivityStatus()))
			{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",
						ApplicationProperties.getValue("site.activityStatus")));
			}
		}
		catch (final Exception excp)
		{
			SiteForm.logger.error(excp.getMessage(),excp);
			excp.printStackTrace() ;
		}
		return errors;
	}

	/**
	 * This method sets Identifier of Objects inserted by AddNew activity in Form-Bean which initialized AddNew action
	 * @param addNewFor - FormBean ID of the object inserted
	 *  @param addObjectIdentifier - Identifier of the Object inserted 
	 */
	@Override
	public void setAddNewObjectIdentifier(String addNewFor, Long addObjectIdentifier)
	{
		if (addNewFor.equals("coordinator"))
		{
			this.setCoordinatorId(addObjectIdentifier.longValue());
		}
	}

}