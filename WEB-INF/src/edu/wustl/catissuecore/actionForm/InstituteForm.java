/**
 * <p>Title: InstituteForm Class>
 * <p>Description:  This Class is used to encapsulate all the request parameters passed 
 * from Institute.jsp page. </p>
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
import edu.wustl.catissuecore.domain.Institution;
import edu.wustl.catissuecore.util.global.ApplicationProperties;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Validator;
import edu.wustl.common.util.logger.Logger;

/**
 * This Class is used to encapsulate all the request parameters passed from Institute.jsp page.
 * @author kapil_kaveeshwar
 * */
public class InstituteForm extends AbstractActionForm
{
    /**
     * systemIdentifier is a unique id assigned to each Institute.
     * */
    private long systemIdentifier = -1;

    /**
     * A String containing the operation(Add/Edit) to be performed.
     * */
    private String operation = null;
    
    /**
     * A string containing the name of the institute.
     */
    private String name = null;  

    /**
     * A string containing the type of the institute.
     */
    private String type = null;

    /**
     * A string containing the street Address of the institute.
     */
    private String street = null;

    /**
     * A string containing the City of the institute.
     */
    private String city = null;

    /**
     * A string containing the name of the State of the institute.
     */
    private String state = null;

    /**
     * A string containing the name of the Country of the institute.
     */
    private String country = null;

    /**
     * A String containing the zip code of the institute.
     * */
    private String zip = null;

    /**
     * A String containing the phone number of the institute.
     * */
    private String phone = null;

    /**
     * A String containing the fax number of the institute.
     * */
    private String fax = null;
    
    /**
     * No argument constructor for InstituteForm class 
     */
    public InstituteForm()
    {
        reset();
    }

    /**
     * This function Copies the data from an institute object to a InstituteForm object.
     * @param institute An Institute object containing the information about the institute.  
     */
    public void setAllValues(AbstractDomainObject abstractDomain)
    {
        try
        {
            Institution institute = (Institution) abstractDomain;
            this.systemIdentifier = institute.getSystemIdentifier().longValue();
            this.name = institute.getName();
        }
        catch (Exception excp)
        {
            Logger.out.error(excp.getMessage(),excp);
        }
    }

    /**
     * Returns the systemIdentifier assigned to Institute.
     * @return int representing the id assigned to Institute.
     * @see #setSystemIdentifier(int)
     * */
    public long getSystemIdentifier()
    {
        return (this.systemIdentifier);
    }

    /**
     * Sets an id for the Institute.
     * @param systemIdentifier id to be assigned to the Institute.
     * @see #getSystemIdentifier()
     * */
    public void setSystemIdentifier(long identifier)
    {
        this.systemIdentifier = identifier;
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
     * Returns the login name of the institute.
     * @return String representing the login name of the institute
     * @see #setLoginName(String)
     */
    public String getName()
    {
        return (this.name);
    }

    /**
     * Sets the login name of this institute
     * @param loginName login name of the institute.
     * @see #getLoginName()
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Returns the type of the institute.
     * @return String representing the type of the institute.
     */
    public String getType()
    {
        return (this.type);
    }

    /**
     * Sets the type of the institute.
     * @param email String representing type of the institute
     * @see #getType()
     */
    public void setType(String type)
    {
        this.type = type;
    }

    /**
     * Returns the Mailing Address of the institute.
     * @return String representing mailing address of the institute.
     * @see #setAddress(String)
     */
    public String getStreet()
    {
        return (this.street);
    }

    /**
     * Sets the Mailing Address of the institute.
     * @param address String representing mailing address of the institute.
     * @see #getAddress()
     */
    public void setStreet(String street)
    {
        this.street = street;
    }

    /**
     * Returns the City where the institute stays.
     * @return String representing city of the institute.
     * @see #setCity(String)
     */
    public String getCity()
    {
        return (this.city);
    }

    /**
     * Sets the City where the institute stays.
     * @param city String representing city of the institute.
     * @see #getCity()
     */
    public void setCity(String city)
    {
        this.city = city;
    }

    /**
     * Returns the State where the institute stays.
     * @return String representing state of the institute.
     * @see #setState(String)
     */
    public String getState()
    {
        return (this.state);
    }

    /**
     * Sets the State where the institute stays.
     * @param state String representing state of the institute.
     * @see #getState()
     */
    public void setState(String state)
    {
        this.state = state;
    }

    /**
     * Returns the Country where the institute stays.
     * @return String representing country of the institute.
     * @see #setCountry(String)
     */
    public String getCountry()
    {
        return (this.country);
    }

    /**
     * Sets the Country where the institute stays.
     * @param country String representing country of the institute.
     * @see #getCountry()
     */
    public void setCountry(String country)
    {
        this.country = country;
    }

    /**
     * Returns the zip code of the institute's city. 
     * @return Returns the zip.
     * @see #setZip(String)
     */
    public String getZip()
    {
        return zip;
    }

    /**
     * Sets the zip code of the institute's city.
     * @param zip The zip code to set.
     * @see #getZip()
     */
    public void setZip(String zip)
    {
        this.zip = zip;
    }

    /**
     * Returns the phone number of the institute.
     * @return Returns the phone number.
     * @see #setPhone(String)
     */
    public String getPhone()
    {
        return phone;
    }

    /**
     * Sets the phone number of the institute. 
     * @param phone The phone number to set.
     * @see #getPhone()
     */
    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    /**
     * Returns the fax number of the institute.
     * @return Returns the fax.
     * @see #setFax(String)
     */
    public String getFax()
    {
        return fax;
    }

    /**
     * Sets the fax number of the institute.
     * @param fax The fax to set.
     * @see #getFax()
     */
    public void setFax(String fax)
    {
        this.fax = fax;
    }

    
    /* (non-Javadoc)
     * @see edu.wustl.catissuecore.actionForm.AbstractActionForm#getActivityStatus()
     */
    public String getActivityStatus()
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    
    /* (non-Javadoc)
     * @see edu.wustl.catissuecore.actionForm.AbstractActionForm#setActivityStatus(java.lang.String)
     */
    public void setActivityStatus(String activityStatus)
    {
        // TODO Auto-generated method stub

    }
    
    /**
     * Checks the operation to be performed is add or not.
     * @return Returns true if operation is equal to "add", else it returns false
     * */
    public boolean isAddOperation()
    {
        return(getOperation().equals(Constants.ADD));
    }
    
    /**
     * Returns the id assigned to form bean
     */
    public int getFormId()
    {
        return Constants.INSTITUTION_FORM_ID;
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
        this.name = null;
        this.type = null;
        this.street = null;
        this.city = null;
        this.state = null;
        this.country = null;
        this.zip = null;
        this.phone = null;
        this.fax = null;
    }

    /**
    * Overrides the validate method of ActionForm.
    * */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
    {
        ActionErrors errors = new ActionErrors();
        Validator validator = new Validator();
        
        try
        {
            if (operation.equals(Constants.SEARCH))
            {
                checkValidNumber(new Long(systemIdentifier).toString(),"institute.systemIdentifier",errors,validator);
            }
            if (operation.equals(Constants.ADD) || operation.equals(Constants.EDIT))
            {
             
                if (validator.isEmpty(name))
                {
                    errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("institute.name")));
                }
                
                if (validator.isEmpty(type))
                {
                    errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("institute.type")));
                }
                
                if (validator.isEmpty(street))
                {
                    errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("institute.street")));
                }

                checkValidString(city,"institute.city",errors,validator);
                checkValidNumber(zip,"institute.zip",errors,validator);
                
//                System.out.println("FAX........"+fax);
//                if (!(validator.isNumeric(fax)) && (fax != null))
//                {
//                    errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.format",ApplicationProperties.getValue("institute.fax")));
//                }
                
            }
        }
        catch(Exception excp)
        {
            Logger.out.error(excp.getMessage());
        }
        return errors;
     }
}