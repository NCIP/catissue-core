/**
 * <p>Title: NewSpecimenForm Class>
 * <p>Description:  NewSpecimenForm Class is used to encapsulate all the request parameters passed 
 * from New Specimen webpage. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00
 */

package edu.wustl.catissuecore.actionForm;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.domain.AbstractDomainObject;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCharacteristics;
import edu.wustl.catissuecore.util.global.ApplicationProperties;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Validator;
import edu.wustl.common.util.logger.Logger;

/**
 * NewSpecimenForm Class is used to encapsulate all the request parameters passed 
 * from New Specimen webpage.
 * @author aniruddha_phadnis
 */
public class NewSpecimenForm extends SpecimenForm
{
    private String specimenCollectionGroupId;
    
     /**
     * Anatomic site from which the specimen was derived.
     */
    private String tissueSite;

    /**
     * For bilateral sites, left or right.
     */
    private String tissueSide;

    /**
     * Histopathological character of the specimen 
     * e.g. Non-Malignant, Malignant, Non-Malignant Diseased, Pre-Malignant.
     */
    private String pathologicalStatus;
    
    /**
     * Type of the biohazard.
     */
    private String biohazardType;
    
    /**
     * Name of the biohazard.
     */
    private String biohazardName;
    
    /**
     * Number of biohazard rows.
     */
    private int bhCounter=1;
    
    private Map biohazard = new HashMap();
    
    
	/**
	 * Associates the specified object with the specified key in the map.
	 * @param key the key to which the object is mapped.
	 * @param value the object which is mapped.
	 */
	public void setBiohazardValue(String key, Object value)
	{
		biohazard.put(key, value);
	}

	/**
	 * Returns the object to which this map maps the specified key.
	 * @param key the required key.
	 * @return the object to which this map maps the specified key.
	 */
	public Object getBiohazardValue(String key)
	{
		return biohazard.get(key);
	}

	/**
	 * @return Returns the values.
	 */
	public Collection getAllBiohazards()
	{
		return biohazard.values();
	}

	/**
	 * @param values
	 * The values to set.
	 */
	public void setBiohazard(Map biohazard)
	{
		this.biohazard = biohazard;
	}

	/**
	 * @param values
	 * Returns the map.
	 */
	public Map getBiohazard()
	{
		return this.biohazard;
	}	
    
    /**
     * @return Returns the pathologicalStatus.
     */
    public String getPathologicalStatus()
    {
        return pathologicalStatus;
    }

    /**
     * @param pathologicalStatus The pathologicalStatus to set.
     */
    public void setPathologicalStatus(String pathologicalStatus)
    {
        this.pathologicalStatus = pathologicalStatus;
    }

    /**
     * @return Returns the specimenCollectionGroupId.
     */
    public String getSpecimenCollectionGroupId()
    {
        return specimenCollectionGroupId;
    }

    /**
     * @param specimenCollectionGroupId The specimenCollectionGroupId to set.
     */
    public void setSpecimenCollectionGroupId(String specimenCollectionGroupId)
    {
        this.specimenCollectionGroupId = specimenCollectionGroupId;
    }

    /**
     * @return Returns the tissueSide.
     */
    public String getTissueSide()
    {
        return tissueSide;
    }

    /**
     * @param tissueSide The tissueSide to set.
     */
    public void setTissueSide(String tissueSide)
    {
        this.tissueSide = tissueSide;
    }

    /**
     * @return Returns the tissueSite.
     */
    public String getTissueSite()
    {
        return tissueSite;
    }

    /**
     * @param tissueSite The tissueSite to set.
     */
    public void setTissueSite(String tissueSite)
    {
        this.tissueSite = tissueSite;
    }

    protected void reset()
    {
        super.reset();
    	this.tissueSite = null;
        this.tissueSide = null;
        this.pathologicalStatus = null;
        this.biohazard = new HashMap();
    }
    
    /**
     * Resets all fields.
     */
    public void reset(ActionMapping mapping, HttpServletRequest request)
    {
        reset();
    }
    
    /**
     * Returns the id assigned to form bean.
     */
    public int getFormId()
    {
        return Constants.NEW_SPECIMEN_FORM_ID;
    }
    
    /**
     * This function Copies the data from an site object to a SiteForm object.
     * @param site An object containing the information about site.  
     */
    public void setAllValues(AbstractDomainObject abstractDomain)
    {
        try
        {
            Specimen specimen = (Specimen) abstractDomain;
            
            SpecimenCharacteristics characteristic = specimen.getSpecimenCharacteristics();
            this.pathologicalStatus = characteristic.getPathologicalStatus();
            this.tissueSide = characteristic.getTissueSide();
            this.tissueSite = characteristic.getTissueSite();
        }
        catch (Exception excp)
        {
            Logger.out.error(excp.getMessage(),excp);
        }
    }
    
	/**
	 * @return Returns the biohazardType.
	 */
	public String getBiohazardType()
	{
		return biohazardType;
	}
	/**
	 * @param biohazardType The biohazardType to set.
	 */
	public void setBiohazardType(String biohazardType)
	{
		this.biohazardType = biohazardType;
	}
	
	/**
	 * @return Returns the biohazardName.
	 */
	public String getBiohazardName()
	{
		return biohazardName;
	}
	/**
	 * @param biohazardName The biohazardName to set.
	 */
	public void setBiohazardName(String biohazardName)
	{
		this.biohazardName = biohazardName;
	}	
	
	/**
     * Overrides the validate method of ActionForm.
     * */
     public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
     {
         ActionErrors errors = super.validate(mapping,request);
         Validator validator = new Validator();
         
         try
         {
             if (operation.equals(Constants.ADD) || operation.equals(Constants.EDIT))
             {             
             	if (specimenCollectionGroupId.equals("-1"))
                {
                    errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("specimen.specimenCollectionGroupId")));
                }
             	if (tissueSite.equals(Constants.SELECT_OPTION))
                {
                    errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("specimen.tissueSite")));
                }
             	
             	if (tissueSide.equals(Constants.SELECT_OPTION))
                {
                    errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("specimen.tissueSide")));
                }
             	
             	if (pathologicalStatus.equals(Constants.SELECT_OPTION))
                {
                    errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("specimen.pathologicalStatus")));
                }

             	//Validations for Biohazard Add-More Block
                String className = "Biohazard:";
                String key1 = "_type";
                String key2 = "_systemIdentifier";
                int index = 1;
                boolean isError = false;
                
                while(true)
                {
                	String keyOne = className + index + key1;
					String keyTwo = className + index + key2;
                	String value1 = (String)biohazard.get(keyOne);
                	String value2 = (String)biohazard.get(keyTwo);
                	
                	if(value1 == null || value2 == null)
                	{
                		break;
                	}
                	else if(value1.equals(Constants.SELECT_OPTION) && value2.equals("-1"))
                	{
                		biohazard.remove(keyOne);
                		biohazard.remove(keyTwo);
                	}
                	else if((!value1.equals(Constants.SELECT_OPTION) && value2.equals("-1")) || (value1.equals(Constants.SELECT_OPTION) && !value2.equals("-1")))
                	{
                		isError = true;
                		break;
                	}
                	index++;
                }
                
                if(isError)
                {
                	errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.newSpecimen.biohazard.missing",ApplicationProperties.getValue("newSpecimen.msg")));
                }
             }
         }
         catch(Exception excp)
         {
             Logger.out.error(excp.getMessage());
         }
         return errors;
      }
     
	/**
	 * @return Returns the bhCounter.
	 */
	public int getBhCounter()
	{
		return bhCounter;
	}
	
	/**
	 * @param bhCounter The bhCounter to set.
	 */
	public void setBhCounter(int bhCounter)
	{
		this.bhCounter = bhCounter;
	}
}