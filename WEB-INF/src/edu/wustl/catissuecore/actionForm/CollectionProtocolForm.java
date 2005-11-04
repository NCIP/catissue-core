/**
 * <p>Title: CollectionProtocolForm Class>
 * <p>Description:  CollectionProtocolForm Class is used to encapsulate all the request parameters passed 
 * from User Add/Edit webpage. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 * Created on Mar 3, 2005
 */

package edu.wustl.catissuecore.actionForm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.domain.AbstractDomainObject;
import edu.wustl.catissuecore.domain.CellSpecimenRequirement;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolEvent;
import edu.wustl.catissuecore.domain.FluidSpecimenRequirement;
import edu.wustl.catissuecore.domain.MolecularSpecimenRequirement;
import edu.wustl.catissuecore.domain.SpecimenRequirement;
import edu.wustl.catissuecore.domain.TissueSpecimenRequirement;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.global.ApplicationProperties;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.catissuecore.util.global.Validator;
import edu.wustl.common.util.logger.Logger;

/**
 * CollectionProtocolForm Class is used to encapsulate all the request
 * parameters passed from User Add/Edit webpage.
 * 
 * @author gautam_shetty
 */
public class CollectionProtocolForm extends SpecimenProtocolForm
{
	protected long protocolCoordinatorIds[];

	/**
	 * Counter that contains number of rows in the 'Add More' functionality. outer block
	 */
	private int outerCounter=1;

	/**
	 * Counter that contains number of rows in the 'Add More' functionality. inner block
	 */
	protected Map innerLoopValues = new HashMap();
	
	/**
	 * @return Returns the innerLoopValues.
	 */
	public Map getInnerLoopValues()
	{
		return innerLoopValues;
	}
	/**
	 * @param innerLoopValues The innerLoopValues to set.
	 */
	public void setInnerLoopValues(Map innerLoopValues)
	{
		this.innerLoopValues = innerLoopValues;
	}

	/**
	 * Associates the specified object with the specified key in the map.
	 * @param key the key to which the object is mapped.
	 * @param value the object which is mapped.
	 */
	public void setIvl(String key, Object value)
	{
		innerLoopValues.put(key, value);
	}

	/**
	 * Returns the object to which this map maps the specified key.
	 * 
	 * @param key
	 *            the required key.
	 * @return the object to which this map maps the specified key.
	 */
	public Object getIvl(String key)
	{
		return innerLoopValues.get(key);
	}

	/**
	 * @return Returns the outerCounter.
	 */
	public int getOuterCounter()
	{
		return outerCounter;
	}
	/**
	 * @param outerCounter The outerCounter to set.
	 */
	public void setOuterCounter(int outerCounter)
	{
		this.outerCounter = outerCounter;
	}
	
	/**
	 * No argument constructor for CollectionProtocolForm class.
	 */
	public CollectionProtocolForm()
	{
		super();
	}
	
	protected void reset()
	{
//		super.reset();
//		protocolCoordinatorIds = null;
//		this.outerCounter = 1;
//		this.values  = new HashMap();
	}
	
	/**
	 * @return Returns the protocolcoordinator ids.
	 */
	public long[] getProtocolCoordinatorIds()
	{
		return protocolCoordinatorIds;
	}

	/**
	 * @param protocolCoordinatorIds The protocolCoordinatorIds to set.
	 */
	public void setProtocolCoordinatorIds(long[] protocolCoordinatorIds)
	{
		this.protocolCoordinatorIds = protocolCoordinatorIds;
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
			
			CollectionProtocol cProtocol = (CollectionProtocol)abstractDomain;
			Collection protocolEventCollection = cProtocol.getCollectionProtocolEventCollection(); 
			
			if(protocolEventCollection != null)
			{
				List eventList = new ArrayList(protocolEventCollection);
				Collections.sort(eventList);
				protocolEventCollection = eventList;
				
				values = new HashMap();
				innerLoopValues = new HashMap();
				
				int i = 1;
				Iterator it = protocolEventCollection.iterator();
				while(it.hasNext())
				{
					CollectionProtocolEvent cpEvent = (CollectionProtocolEvent)it.next();
					
					String keyClinicalStatus = "CollectionProtocolEvent:" + i + "_clinicalStatus";
					String keyStudyCalendarEventPoint = "CollectionProtocolEvent:" + i + "_studyCalendarEventPoint";
					String keyCPESystemIdentifier = "CollectionProtocolEvent:" + i + "_systemIdentifier";
					
					values.put(keyClinicalStatus,Utility.toString(cpEvent.getClinicalStatus()));
					values.put(keyStudyCalendarEventPoint, Utility.toString(cpEvent.getStudyCalendarEventPoint()));
					values.put(keyCPESystemIdentifier,Utility.toString(cpEvent.getSystemIdentifier()));
					
					Collection specimenRequirementCollection = cpEvent.getSpecimenRequirementCollection();
					
					populateSpecimenRequirement(specimenRequirementCollection, i);
					
					i++;
				}
				
				outerCounter = protocolEventCollection.size();
			}
			
			//At least one outer row should be displayed in ADD MORE therefore
			if(outerCounter == 0)
				outerCounter = 1;
			
			//Populating the user-id array
			Collection userCollection = cProtocol.getUserCollection();
			
			if(userCollection != null)
			{
				protocolCoordinatorIds = new long[userCollection.size()];
				int i=0;

				Iterator it = userCollection.iterator();
				while(it.hasNext())
				{
					User user = (User)it.next();
					protocolCoordinatorIds[i] = user.getSystemIdentifier().longValue();
					i++;
				}
			}
		}
		catch (Exception excp)
		{
	    	Logger.out.error(excp.getMessage(),excp); 
		}
	}
	
	private void populateSpecimenRequirement(Collection specimenRequirementCollection, int counter)
	{
		int innerCounter = 0;
		if(specimenRequirementCollection != null)
		{
			int j = 1;

			Iterator iterator = specimenRequirementCollection.iterator();
			while(iterator.hasNext())
			{
				SpecimenRequirement requirement = (SpecimenRequirement)iterator.next();
				
				String key1 = "CollectionProtocolEvent:" + counter + "_SpecimenRequirement:" + j +"_specimenClass";
				String key3 = "CollectionProtocolEvent:" + counter + "_SpecimenRequirement:" + j +"_specimenType";
				String key4 = "CollectionProtocolEvent:" + counter + "_SpecimenRequirement:" + j +"_tissueSite";
				String key5 = "CollectionProtocolEvent:" + counter + "_SpecimenRequirement:" + j +"_pathologyStatus";
				String key6 = "CollectionProtocolEvent:" + counter + "_SpecimenRequirement:" + j +"_quantityIn";
				String key7 = "CollectionProtocolEvent:" + counter + "_SpecimenRequirement:" + j +"_systemIdentifier";
				String key2 = "CollectionProtocolEvent:" + counter + "_SpecimenRequirement:" + j +"_unitspan";
				
				values.put(key3,requirement.getSpecimenType());
				values.put(key4,requirement.getTissueSite());
				values.put(key5,requirement.getPathologyStatus());
				values.put(key7,requirement.getSystemIdentifier());
				
				if(requirement instanceof TissueSpecimenRequirement)
				{
					values.put(key1,"Tissue");
					values.put(key2,Constants.UNIT_GM);
					values.put(key6,Utility.toString(((TissueSpecimenRequirement)requirement).getQuantityInGram()));
				}
				else if(requirement instanceof CellSpecimenRequirement)
				{
					values.put(key1,"Cell");
					values.put(key2,Constants.UNIT_CC);
					values.put(key6,Utility.toString(((CellSpecimenRequirement)requirement).getQuantityInCellCount()));
				}
				else if(requirement instanceof MolecularSpecimenRequirement)
				{
					values.put(key1,"Molecular");
					values.put(key2,Constants.UNIT_MG);
					values.put(key6,Utility.toString(((MolecularSpecimenRequirement)requirement).getQuantityInMicrogram()));
				}
				else if(requirement instanceof FluidSpecimenRequirement)
				{
					values.put(key1,"Fluid");
					values.put(key2,Constants.UNIT_ML);
					values.put(key6,Utility.toString(((FluidSpecimenRequirement)requirement).getQuantityInMilliliter()));
				}
				
				j++;
			}
			
			innerCounter = specimenRequirementCollection.size();
		}
		
		//At least one inner row should be displayed in ADD MORE therefore
		if(innerCounter == 0)
			innerCounter = 1;
		
		String innerCounterKey = String.valueOf(counter);
		innerLoopValues.put(innerCounterKey,String.valueOf(innerCounter));
	}
	
	/**
	 * Overrides the validate method of ActionForm.
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
		Logger.out.debug("OPERATION : ----- : " + operation );
		ActionErrors errors = super.validate(mapping, request );
		Validator validator = new Validator();
		try
		{
			setRedirectValue(validator);
			// ---------START --------------------------------------
				if(values.isEmpty() )
				{
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.one.item.required",ApplicationProperties.getValue("collectionprotocol.eventtitle")));
				}
			// check for atleast 1 specimen requirement per CollectionProtocol Event
				for(int i=1;i<=outerCounter;i++ )
				{
					String className = "CollectionProtocolEvent:"+i+"_SpecimenRequirement:1_specimenClass";
					Object obj = getValue( className  );
					if(obj == null )
					{
						errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.one.item.required",ApplicationProperties.getValue("collectionprotocol.specimenreq")));
					}
				}
			// ---------END-----------------------------------------
			 if(this.protocolCoordinatorIds == null)
				{
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.selected",ApplicationProperties.getValue("collectionprotocol.protocolcoordinator")));
				}
			 else
			 {
			 	if( this.protocolCoordinatorIds.length <1)
			 	{
			 		errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.selected",ApplicationProperties.getValue("collectionprotocol.protocolcoordinator")));
			 	}
				 for(int ind=0; ind < protocolCoordinatorIds.length ; ind++ )
				 {
				 	if(protocolCoordinatorIds[ind] == -1 )
				 	{
						errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.selected",ApplicationProperties.getValue("collectionprotocol.protocolcoordinator")));
				 	}
				 }
			 }
			 Logger.out.debug("Protocol Coordinators : " + protocolCoordinatorIds ); 
			Iterator it = this.values.keySet().iterator();
			
			boolean bClinicalStatus = false;
			boolean bStudyPoint = false;
			boolean bSpecimenClass = false;
			boolean bSpecimenType = false;
			boolean bTissueSite = false;
			boolean bPathologyStatus = false;
			
			while (it.hasNext())
			{
				String key = (String)it.next();
				String value = (String)values.get(key);
				
				if(!bClinicalStatus)
				{
					if(key.indexOf("clinicalStatus")!=-1 && !validator.isValidOption( value))
					{
						errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.selected",ApplicationProperties.getValue("collectionprotocol.clinicalstatus")));
						bClinicalStatus = true;
					}
				}				
				if(!bStudyPoint)
				{
					if(key.indexOf("studyCalendarEventPoint")!=-1 )
					{
						if(validator.isEmpty(value))
						{
							errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("collectionprotocol.studycalendartitle")));
							bStudyPoint = true;
						}
						else
						{
							 if(!validator.isDouble(value,1))
							 {
								errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.studycalendarpoint",ApplicationProperties.getValue("collectionprotocol.studycalendartitle")));
								bStudyPoint = true;
							 }
						}
					}
				}
				
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
				
				
				if(key.indexOf("quantityIn")!=-1)
				{
					String classKey = key.substring(0,key.lastIndexOf("_") );
					classKey = classKey + "_specimenClass";
					String classValue = (String)getValue(classKey );
					if (classValue.trim().equals("Cell"))
					{
        				if(!validator.isEmpty(value) && !validator.isNumeric(value ))
        				{
        					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("collectionprotocol.quantity")));
        				}
					}
					else
					{
						if(!validator.isEmpty(value) && !validator.isDouble(value ))
        				{
        					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("collectionprotocol.quantity")));
        				}
					}
				} // if  quantity
			}
		}
		catch (Exception excp)
		{
	    	// use of logger as per bug 79
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
		return Constants.COLLECTION_PROTOCOL_FORM_ID;
	}
	
	public static void main(String[] args)
	{
		int maxCount=1;
		int maxIntCount=1;
		
		CollectionProtocolForm collectionProtocolForm = null;
		
		Object obj = new Object();//request.getAttribute("collectionProtocolForm");
		
		if(obj != null && obj instanceof CollectionProtocolForm)
		{
			collectionProtocolForm = (CollectionProtocolForm)obj;
			maxCount = collectionProtocolForm.getOuterCounter();
		}
	
		for(int counter=1;counter<=maxCount;counter++)
		{
			String commonLabel = "value(CollectionProtocolEvent:" + counter;
			
			String cid = "ivl(" + counter + ")";
			String functionName = "insRow('" + commonLabel + "','" + cid +"')";
			
			if(collectionProtocolForm!=null)
			{
				Object o = collectionProtocolForm.getIvl(cid);
				maxIntCount = Integer.parseInt(o.toString());
			}
		}

	}
}