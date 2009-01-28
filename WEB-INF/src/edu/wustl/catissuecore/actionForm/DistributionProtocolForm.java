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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.domain.DistributionProtocol;
import edu.wustl.catissuecore.domain.DistributionSpecimenRequirement;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.DomainBeanIdentifierComparator;
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
	private static final long serialVersionUID = 1L;

	/**
	 * logger Logger - Generic logger.
	 */
	private static org.apache.log4j.Logger logger = Logger.getLogger(DistributionProtocolForm.class);


	
	/**
	 * Counter that contains number of rows in the 'Add More' functionality.
	 */
	private int counter=0;
	
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
	/**
     * Resets the values of all the fields.
     * This method defined in ActionForm is overridden in this class.
     */
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
		super.setAllValues(abstractDomain);
		
		DistributionProtocol dProtocol = (DistributionProtocol)abstractDomain;
		
		Collection<DistributionSpecimenRequirement> distSpcimenReqquirementCollection = dProtocol.getDistributionSpecimenRequirementCollection();
		
		//Added by Abhishek
		List<DistributionSpecimenRequirement> spcimenProtocolCollectionList = new ArrayList<DistributionSpecimenRequirement>(distSpcimenReqquirementCollection);
		DomainBeanIdentifierComparator domainBeanIdentifierComparator = new DomainBeanIdentifierComparator();
		Collections.sort(spcimenProtocolCollectionList, domainBeanIdentifierComparator);
		
		if(spcimenProtocolCollectionList != null)
		{
			values = new LinkedHashMap();
			counter=0;
			
			int i=1;
			Iterator it = spcimenProtocolCollectionList.iterator();
			while(it.hasNext())
			{
				DistributionSpecimenRequirement distSpecimenRequirement = (DistributionSpecimenRequirement)it.next();

				String[] key = {
				        "DistributionSpecimenRequirement:" + i +"_specimenClass",
				        "DistributionSpecimenRequirement:" + i +"_unitspan",
				        "DistributionSpecimenRequirement:" + i +"_specimenType",
				        "DistributionSpecimenRequirement:" + i +"_tissueSite",
				        "DistributionSpecimenRequirement:" + i +"_pathologyStatus",
				        "DistributionSpecimenRequirement:" + i +"_quantity",
				        "DistributionSpecimenRequirement:" + i +"_id"
					};
				setSpecimenRequirement(key , distSpecimenRequirement);
				i++;
				counter++;
			}
			
			//At least one row should be displayed in ADD MORE therefore
			if(counter == 0)
			{
				counter = 1;
			}
		}
	}
	
	 
	/**
	 * Overrides the validate method of ActionForm.
	 * @return error ActionErrors instance
	 * @param mapping Actionmapping instance
	 * @param request HttpServletRequest instance
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
		ActionErrors errors = super.validate(mapping, request);
		Validator validator = new Validator();
		try
		{
                
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
    				logger.debug(key+ " : " + value);
    				
    				if(!bSpecimenClass)
    				{
    					if(key.indexOf("specimenClass")!=-1 && !validator.isValidOption(value))
    					{
    						errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.selected",ApplicationProperties.getValue("collectionprotocol.specimenclass")));
    						bSpecimenClass = true;
    					}
    				}
    				
    				if(!bSpecimenType)
    				{
    					if(key.indexOf("specimenType")!=-1 && !validator.isValidOption(value))
    					{
    						errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.selected",ApplicationProperties.getValue("collectionprotocol.specimetype")));
    						bSpecimenType = true;
    					}
    				}				

    				if(!bTissueSite)
    				{
    					if(key.indexOf("tissueSite")!=-1 && !validator.isValidOption(value))
    					{
    						errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.selected",ApplicationProperties.getValue("collectionprotocol.specimensite")));
    						bTissueSite = true;
    					}
    				}

    				if(!bPathologyStatus)
    				{
    					if(key.indexOf("pathologyStatus")!=-1 && !validator.isValidOption(value))
    					{
    						errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.selected",ApplicationProperties.getValue("collectionprotocol.specimenstatus")));
    						bPathologyStatus = true; 
    					}
    				}
    					
    				if(!bQuantity )
    				{
	    				if((key.indexOf("_quantity"))!=-1)
	    				{
	    					// check for empty quantity
//	    					if(validator.isEmpty(value))
//	    					{
//            					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("collectionprotocol.quantity")));
//            					bQuantity = true;
//	    					}
//	    					else
//	    					{
		    					String classKey = key.substring(0,key.indexOf("_"));
		    					classKey = classKey + "_specimenClass";
		    					String classValue = (String)getValue(classKey);
		    					
	    						// -------Mandar: 20-12-2005
	    						String typeKey = key.substring(0,key.indexOf("_"));
	    						typeKey = typeKey + "_specimenType";
	    						String typeValue = (String)getValue(typeKey);
	    						logger.debug("TypeKey : "+ typeKey  + " : Type Value : " + typeValue);
	    						
	    						try
								{
	    							value = new BigDecimal(value).toPlainString();	    						
		    						/*
		    						 *  if class is cell or type is slide,paraffinblock, 
		    						 *  frozen block then qty is in integer
		    						 */
			    					if (classValue.trim().equals("Cell") || typeValue.trim().equals(Constants.FROZEN_TISSUE_SLIDE) 
			    							||typeValue.trim().equals(Constants.FIXED_TISSUE_BLOCK) || typeValue.trim().equals(Constants.FROZEN_TISSUE_BLOCK ) || typeValue.trim().equals(Constants.FIXED_TISSUE_SLIDE))
			    					{
			            				if(!validator.isNumeric(value,0))
			            				{
			            					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.format",ApplicationProperties.getValue("distributionprotocol.quantity")));
			            					bQuantity = true;
			            				}
			    					}
			    					else
			    					{
			    						if(!validator.isDouble(value ,true))
				        				{
				        					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("distributionprotocol.quantity")));
				        					bQuantity = true;
				        				}
			    					}
								}
	    						catch (NumberFormatException exp)
						        {    		  
									errors.add(ActionErrors.GLOBAL_ERROR,new ActionError("errors.item.format",ApplicationProperties.getValue("distributionprotocol.quantity")));
								}		
	    					}
//	    				} // if  quantity
    				}
   				}
		}
		catch (Exception excp)
		{
	    	logger.error(excp.getMessage(),excp); 
			errors = new ActionErrors();
		}
		return errors;
	}
	
	/**
	 * @return DISTRIBUTIONPROTOCOL_FORM_ID Returns the id assigned to form bean
	 */
	public int getFormId()
	{
		return Constants.DISTRIBUTIONPROTOCOL_FORM_ID;
	}
	
	/**
     * This method sets Identifier of Objects inserted by AddNew activity in Form-Bean which initialized AddNew action
     * @param addNewFor - FormBean ID of the object inserted
     *  @param addObjectIdentifier - Identifier of the Object inserted 
     */
	public void setAddNewObjectIdentifier(String addNewFor, Long addObjectIdentifier)
    {
        if(addNewFor.equals("principalInvestigator"))
        {
            setPrincipalInvestigatorId(addObjectIdentifier.longValue());
        }
    }
}