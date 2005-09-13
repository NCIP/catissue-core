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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.domain.AbstractDomainObject;
import edu.wustl.catissuecore.domain.Biohazard;
import edu.wustl.catissuecore.domain.CellSpecimenReviewParameters;
import edu.wustl.catissuecore.domain.CheckInCheckOutEventParameter;
import edu.wustl.catissuecore.domain.CollectionEventParameters;
import edu.wustl.catissuecore.domain.DisposalEventParameters;
import edu.wustl.catissuecore.domain.EmbeddedEventParameters;
import edu.wustl.catissuecore.domain.EventParameters;
import edu.wustl.catissuecore.domain.FixedEventParameters;
import edu.wustl.catissuecore.domain.FluidSpecimenReviewEventParameters;
import edu.wustl.catissuecore.domain.FrozenEventParameters;
import edu.wustl.catissuecore.domain.MolecularSpecimenReviewParameters;
import edu.wustl.catissuecore.domain.ProcedureEventParameters;
import edu.wustl.catissuecore.domain.ReceivedEventParameters;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCharacteristics;
import edu.wustl.catissuecore.domain.SpunEventParameters;
import edu.wustl.catissuecore.domain.ThawEventParameters;
import edu.wustl.catissuecore.domain.TissueSpecimenReviewEventParameters;
import edu.wustl.catissuecore.domain.TransferEventParameters;
import edu.wustl.catissuecore.util.global.ApplicationProperties;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.catissuecore.util.global.Validator;
import edu.wustl.common.security.SecurityManager;
import edu.wustl.common.util.logger.Logger;
import gov.nih.nci.security.authorization.domainobjects.User;

/**
 * NewSpecimenForm Class is used to encapsulate all the request parameters passed 
 * from New Specimen webpage.
 * @author aniruddha_phadnis
 */
public class NewSpecimenForm extends SpecimenForm
{
    private String specimenCollectionGroupId;
    
    /**
     * Identifier of the Parent Speciemen if present.
     */
    private String parentSpecimenId;
    
    /**
     * If "True" then Parent is present else Parent is absent.
     */
    private boolean parentPresent;
    
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
    
    private String specimenEventParameter;
    
    List gridData;
    
    /**
     * Returns an identifier of the Parent Speciemen.
     * @return String an identifier of the Parent Speciemen.
     * @see #setParentSpecimenId(String)
     * */
    public String getParentSpecimenId()
    {
        return parentSpecimenId;
    }

    /**
     * Sets an identifier of the Parent Speciemen.
     * @param parentSpecimenId an identifier of the Parent Speciemen.
     * @see #getParentSpecimenId()
     * */
    public void setParentSpecimenId(String parentSpecimenId)
    {
        this.parentSpecimenId = parentSpecimenId;
    }
	
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
//        super.reset();
//    	this.tissueSite = null;
//        this.tissueSide = null;
//        this.pathologicalStatus = null;
//        this.biohazard = new HashMap();
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
            super.setAllValues(abstractDomain);
            
        	Specimen specimen = (Specimen) abstractDomain;
        	
        	if(specimen.getSpecimenCollectionGroup() != null)
        	{
        		this.specimenCollectionGroupId = String.valueOf(specimen.getSpecimenCollectionGroup().getSystemIdentifier());
        		this.parentPresent = false;
        	}
        	else
        	{
        		this.parentSpecimenId = String.valueOf(specimen.getParentSpecimen().getSystemIdentifier());
        		this.parentPresent = true;
        	}
            
            SpecimenCharacteristics characteristic = specimen.getSpecimenCharacteristics();
            this.pathologicalStatus = characteristic.getPathologicalStatus();
            this.tissueSide = characteristic.getTissueSide();
            this.tissueSite = characteristic.getTissueSite();
            
            Collection biohazardCollection = specimen.getBiohazardCollection();
            bhCounter = 1;
            
            if(biohazardCollection != null)
            {
            	biohazard = new HashMap();
            	
            	int i=1;
            	
            	Iterator it = biohazardCollection.iterator();
            	
            	while(it.hasNext())
            	{
            		String key1 = "Biohazard:" + i +"_type";
    				String key2 = "Biohazard:" + i +"_systemIdentifier";
    				
    				Biohazard hazard = (Biohazard)it.next();
    				
    				biohazard.put(key1,hazard.getType());
    				biohazard.put(key2,hazard.getSystemIdentifier());
    				
    				i++;
            	}
            	
            	bhCounter = biohazardCollection.size();
            }
            
            //Setting Specimen Event Parameters' Grid            
            Collection specimenEventCollection = specimen.getSpecimenEventCollection();
            
            if(specimenEventCollection != null)
            {
            	gridData = new ArrayList();
            	Iterator it = specimenEventCollection.iterator();
            	//int i=1;
            	
            	while(it.hasNext())
            	{
            		List rowData = new ArrayList();
            		EventParameters eventParameters = (EventParameters)it.next();
            		            		
            		if(eventParameters != null)
            		{
            			String [] events = getEvent(eventParameters);
            			rowData.add(String.valueOf(eventParameters.getSystemIdentifier()));
            			rowData.add(events[0]);//Event Name
            			            			
            			User user = SecurityManager.getInstance(NewSpecimenForm.class).getUserById(String.valueOf(eventParameters.getUser().getSystemIdentifier()));
						
            			rowData.add(user.getLastName() + ", " + user.getFirstName());
            			rowData.add(Utility.parseDateToString(eventParameters.getTimestamp(),Constants.DATE_PATTERN_MM_DD_YYYY));
            			rowData.add(events[1]);//pageOf
            			gridData.add(rowData);
            		}
            	}
            }
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
             	if (tissueSite.equals("-1"))
                {
                    errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("specimen.tissueSite")));
                }
             	
             	if (tissueSide.equals("-1"))
                {
                    errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("specimen.tissueSide")));
                }
             	
             	if (pathologicalStatus.equals("-1"))
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
	
	
	/**
	 * @return Returns the parentPresent.
	 */
	public boolean isParentPresent()
	{
		return parentPresent;
	}
	/**
	 * @param parentPresent The parentPresent to set.
	 */
	public void setParentPresent(boolean parentPresent)
	{
		this.parentPresent = parentPresent;
	}
	
	/**
	 * @return Returns the specimenEventParameter.
	 */
	public String getSpecimenEventParameter()
	{
		return specimenEventParameter;
	}
	
	/**
	 * @param specimenEventParameter The specimenEventParameter to set.
	 */
	public void setSpecimenEventParameter(String specimenEventParameter)
	{
		this.specimenEventParameter = specimenEventParameter;
	}
	
	/**
	 * @return Returns the gridData.
	 */
	public List getGridData()
	{
		return gridData;
	}
	
	/**
	 * @param gridData The gridData to set.
	 */
	public void setGridData(List gridData)
	{
		this.gridData = gridData;
	}
	
	private String[] getEvent(EventParameters eventParameters)
	{
		String [] events = new String[2];
				
		if(eventParameters instanceof CellSpecimenReviewParameters)
		{
			events[0] = "Cell Specimen Review";
			events[1] = "pageOfCellSpecimenReviewParameters";
		}
		else if(eventParameters instanceof CheckInCheckOutEventParameter)
		{
			events[0] = "Check In Check Out";
			events[1] = "pageOfCheckInCheckOutEventParameters";
		}
		else if(eventParameters instanceof CollectionEventParameters)
		{
			events[0] = "Collection";
			events[1] = "pageOfCollectionEventParameters";
		}
		else if(eventParameters instanceof DisposalEventParameters)
		{
			events[0] = "Disposal";
			events[1] = "pageOfDisposalEventParameters";
		}
		else if(eventParameters instanceof EmbeddedEventParameters)
		{
			events[0] = "Embedded";
			events[1] = "pageOfEmbeddedEventParameters";
		}
		else if(eventParameters instanceof FixedEventParameters)
		{
			events[0] = "Fixed";
			events[1] = "pageOfFixedEventParameters";
		}
		else if(eventParameters instanceof FluidSpecimenReviewEventParameters)
		{
			events[0] = "Fluid Specimen Review";
			events[1] = "pageOfFluidSpecimenReviewParameters";
		}
		else if(eventParameters instanceof FrozenEventParameters)
		{
			events[0] = "Frozen";
			events[1] = "pageOfFrozenEventParameters";
		}
		else if(eventParameters instanceof MolecularSpecimenReviewParameters)
		{
			events[0] = "Molecular Specimen Review";
			events[1] = "pageOfMolecularSpecimenReviewParameters";
		}
		else if(eventParameters instanceof ProcedureEventParameters)
		{
			events[0] = "Procedure Event";
			events[1] = "pageOfProcedureEventParameters";
		}
		else if(eventParameters instanceof ReceivedEventParameters)
		{
			events[0] = "Received Event";
			events[1] = "pageOfReceivedEventParameters";
		}
		else if(eventParameters instanceof SpunEventParameters)
		{
			events[0] = "Spun";
			events[1] = "pageOfSpunEventParameters";
		}
		else if(eventParameters instanceof ThawEventParameters)
		{
			events[0] = "Thaw";
			events[1] = "pageOfThawEventParameters";
		}
		else if(eventParameters instanceof TissueSpecimenReviewEventParameters)
		{
			events[0] = "Tissue Specimen Review";
			events[1] = "pageOfTissueSpecimenReviewParameters";
		}
		else if(eventParameters instanceof TransferEventParameters)
		{
			events[0] = "Transfer";
			events[1] = "pageOfTransferEventParameters";
		}
		
		return events;
	}
}