/**
 * <p>Title: DistributionProtocolForm Class>
 * <p>Description:  DistributionProtocolForm Class is used to encapsulate all the request parameters passed 
 * from User Add/Edit webpage. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 * Created on Mar 3, 2005
 */

package edu.wustl.catissuecore.actionForm;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.domain.CellSpecimenRequirement;
import edu.wustl.catissuecore.domain.DistributionProtocol;
import edu.wustl.catissuecore.domain.FluidSpecimenRequirement;
import edu.wustl.catissuecore.domain.MolecularSpecimenRequirement;
import edu.wustl.catissuecore.domain.SpecimenRequirement;
import edu.wustl.catissuecore.domain.TissueSpecimenRequirement;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;

/**
 * DistributionProtocolForm Class is used to encapsulate all the request parameters passed 
 * from Distribution Add/Edit webpage.
 * @author Mandar Deshmukh
 * */
public class DistributionProtocolForm extends SpecimenProtocolForm
{
	
	/**
	 * Counter that contains number of rows in the 'Add More' functionality.
	 */
	private int counter=1;
	
	/**
	 * @return Returns the counter.
	 */
	public int getCounter()
	{
		return counter;
	}
	/**
	 * @param counter The counter to set.
	 */
	public void setCounter(int counter)
	{
		this.counter = counter;
	}
	
	/**
	 * No argument constructor for DistributionProtocolForm class.
	 */
	public DistributionProtocolForm()
	{
		super();
	}
	
	protected void reset()
	{
		//super.reset();
		//this.counter =1;
	}
	
	/**
	 * Copies the data from an AbstractDomain object to a DistributionProtocolForm object.
	 * @param abstractDomain An AbstractDomain object.
	 */
	public void setAllValues(AbstractDomainObject abstractDomain)
	{
		try
		{
			super.setAllValues(abstractDomain);
			
			DistributionProtocol dProtocol = (DistributionProtocol)abstractDomain;
			
			Collection spcimenProtocolCollection = dProtocol.getSpecimenRequirementCollection();
			
			if(spcimenProtocolCollection != null)
			{
				values = new HashMap();
				
				Iterator it = spcimenProtocolCollection.iterator();
				int i=1;
				counter=0;
				
				while(it.hasNext())
				{
					String key1 = "SpecimenRequirement:" + i +"_specimenClass";
					String key2 = "SpecimenRequirement:" + i +"_unitspan";
					String key3 = "SpecimenRequirement:" + i +"_specimenType";
					String key4 = "SpecimenRequirement:" + i +"_tissueSite";
					String key5 = "SpecimenRequirement:" + i +"_pathologyStatus";
					String key6 = "SpecimenRequirement:" + i +"_quantityIn";
					String key7 = "SpecimenRequirement:" + i +"_systemIdentifier";
					
					SpecimenRequirement requirement = (SpecimenRequirement)it.next();
					values.put(key3,requirement.getSpecimenType());
					values.put(key4,requirement.getTissueSite());
					values.put(key5,requirement.getPathologyStatus());
					values.put(key7,requirement.getSystemIdentifier());
					
					if(requirement instanceof TissueSpecimenRequirement)
					{
						values.put(key1,"Tissue");
						values.put(key2,Constants.UNIT_GM);
						values.put(key6,String.valueOf(((TissueSpecimenRequirement)requirement).getQuantityInGram()));
					}
					else if(requirement instanceof CellSpecimenRequirement)
					{
						values.put(key1,"Cell");
						values.put(key2,Constants.UNIT_CC);
						values.put(key6,String.valueOf(((CellSpecimenRequirement)requirement).getQuantityInCellCount()));
					}
					else if(requirement instanceof MolecularSpecimenRequirement)
					{
						values.put(key1,"Molecular");
						values.put(key2,Constants.UNIT_MG);
						values.put(key6,String.valueOf(((MolecularSpecimenRequirement)requirement).getQuantityInMicrogram()));
					}
					else if(requirement instanceof FluidSpecimenRequirement)
					{
						values.put(key1,"Fluid");
						values.put(key2,Constants.UNIT_ML);
						values.put(key6,String.valueOf(((FluidSpecimenRequirement)requirement).getQuantityInMilliliter()));
					}
					
					i++;
					counter++;
				}
				
				//At least one row should be displayed in ADD MORE therefore
				if(counter == 0)
					counter = 1;
			}
		}
		catch (Exception excp)
		{
	    	Logger.out.error(excp.getMessage(),excp); 
		}
	}
	
	/**
	 * Overrides the validate method of ActionForm.
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
		ActionErrors errors = super.validate(mapping, request );
		Validator validator = new Validator();
		try
		{
//                if(this.principalInvestigatorId == -1)
//				{
//					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("distributionprotocol.principalinvestigator")));
//				}
//                if (validator.isEmpty(this.title))
//                {
//                	errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("distributionprotocol.protocoltitle")));
//                }
//                
//                // as per bug 326
//                // check for valid enrollments if present
//                if (!validator.isEmpty(this.enrollment) && !validator.isNumeric(this.enrollment ))
//                {
//                	errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("distributionprotocol.participants")));
//                }
				if (this.values.keySet().isEmpty())
				{
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.one.item.required",ApplicationProperties.getValue("distributionprotocol.specimenreq")));
				}
                
				boolean bSpecimenClass = false;
				boolean bSpecimenType = false;
				boolean bTissueSite = false;
				boolean bPathologyStatus = false;
				boolean bQuantity = false;
				
    			Iterator it = this.values.keySet().iterator();
    			while (it.hasNext())
    			{
    				String key = (String)it.next();
    				String value = (String)values.get(key);
    				System.out.println(key+ " : " + value);
    				
    				if(!bSpecimenClass)
    				{
    					if(key.indexOf("specimenClass")!=-1 && !validator.isValidOption( value))
    					{
    						errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.selected",ApplicationProperties.getValue("collectionprotocol.specimenclass")));
    						bSpecimenClass = true;
    					}
    				}
    				
    				if(!bSpecimenType )
    				{
    					if(key.indexOf("specimenType")!=-1 && !validator.isValidOption( value))
    					{
    						errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.selected",ApplicationProperties.getValue("collectionprotocol.specimetype")));
    						bSpecimenType = true;
    					}
    				}				

    				if(!bTissueSite)
    				{
    					if(key.indexOf("tissueSite")!=-1 && !validator.isValidOption( value))
    					{
    						errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.selected",ApplicationProperties.getValue("collectionprotocol.specimensite")));
    						bTissueSite = true;
    					}
    				}

    				if(!bPathologyStatus )
    				{
    					if(key.indexOf("pathologyStatus")!=-1 && !validator.isValidOption( value))
    					{
    						errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.selected",ApplicationProperties.getValue("collectionprotocol.specimenstatus")));
    						bPathologyStatus = true; 
    					}
    				}
    					
    				if(!bQuantity )
    				{
	    				if(key.indexOf("quantityIn")!=-1)
	    				{
	    					// check for empty quantity
	    					if(validator.isEmpty(value))
	    					{
            					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("collectionprotocol.quantity")));
            					bQuantity = true;
	    					}
	    					else
	    					{
		    					String classKey = key.substring(0,key.lastIndexOf("_") );
		    					classKey = classKey + "_specimenClass";
		    					String classValue = (String)getValue(classKey );
		    					
	    						// -------MD: 20-12-2005
	    						String typeKey = key.substring(0,key.lastIndexOf("_") );
	    						typeKey = typeKey + "_specimenType";
	    						String typeValue = (String)getValue(typeKey );
	    						Logger.out.debug("TypeKey : "+ typeKey  + " : Type Value : " + typeValue  );
	    						
	    						/*
	    						 *  if class is cell or type is slide,paraffinblock, 
	    						 *  frozen block then qty is in integer
	    						 */
		    					if (classValue.trim().equals("Cell") || typeValue.trim().equals(Constants.SLIDE) ||
		    							typeValue.trim().equals(Constants.PARAFFIN_BLOCK) || typeValue.trim().equals(Constants.FROZEN_BLOCK ))
		    					{
		            				if(!validator.isNumeric(value ))
		            				{
		            					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.format",ApplicationProperties.getValue("distributionprotocol.quantity")));
		            					bQuantity = true;
		            				}
		    					}
		    					else
		    					{
		    						if(!validator.isDouble(value ))
			        				{
			        					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("distributionprotocol.quantity")));
			        					bQuantity = true;
			        				}
		    					}
	    					}
	    				} // if  quantity
    				}
   				}
		}
		catch (Exception excp)
		{
	    	Logger.out.error(excp.getMessage(),excp); 
			errors = new ActionErrors();
		}
		return errors;
	}
	
	/**
	 * Returns the id assigned to form bean
	 */
	public int getFormId()
	{
		return Constants.DISTRIBUTIONPROTOCOL_FORM_ID;
	}
}