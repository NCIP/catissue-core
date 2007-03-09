/**
 * <p>Title: CollectionProtocolForm Class>
 * <p>Description:  CollectionProtocolForm Class is used to encapsulate all the request parameters passed 
 * from User Add/Edit webpage. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Mandar Deshmukh
 * @author Gautam Shetty
 * @version 1.00
 * Created on Mar 3, 2005
 */

package edu.wustl.catissuecore.actionForm;

import java.math.BigDecimal;
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

import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolEvent;
import edu.wustl.catissuecore.domain.ConsentTier;
import edu.wustl.catissuecore.domain.SpecimenRequirement;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;

/**
 * CollectionProtocolForm Class is used to encapsulate all the request
 * parameters passed from collection protocol Add/Edit webpage.
 * 
 * @author Mandar Deshmukh
 * @author gautam_shetty
 */
public class CollectionProtocolForm extends SpecimenProtocolForm
{		
	protected long []protocolCoordinatorIds;

	/**
	 * Counter that contains number of rows in the 'Add More' functionality. outer block
	 */
	private int outerCounter=1;

	/**
	 * @return Returns the innerLoopValues.
	 * 
	 */
	protected Map innerLoopValues = new HashMap();
		
	/**
	 * whether Aliquote in same container
	 */
	protected boolean aliqoutInSameContainer = false;
	
	//Consent tracking(Virender Mehta)
	/**
	 * Unsigned Form Url for the Consents
	 */
	protected String unsignedConsentURLName;

	/**
	 * Map for Storing Values of Consent Tiers.
	 */
	protected Map consentValues = new HashMap();
	
	/**
	 * No of Consent Tier
	 */
	private int consentTierCounter=1;
	/**
	 * CheckBox for consent is checked or not
	 */
	private boolean consentWaived = false;
	//Consent tracking(Virender Mehta)
	/**
	 * No argument constructor for CollectionProtocolForm class.
	 */
	public CollectionProtocolForm()
	{
		super();
	}

	/**
     * @param key  Value of Key 
     * @param value Value corrosponding to the Key
     */
    public void setValue(String key, Object value) 
    {
    	if (isMutable())
   	 	{
    		values.put(key, value);
   	 	}
    }
    
    /**
     * @return This is used to get corresponding Value from the Map
     * @param key This is used to get corresponding Value from the Map   
     */
    public Object getValue(String key) 
    {
        return values.get(key);
    }
    
	/**
	 * @return values in map
	 */
	public Collection getAllValues() 
	{
		return values.values();
	}
	
	/**
	 * @return values
	 */
	public Map getValues() 
	{
		return values;
	}
	
	/**
	 * @param values Set the values
	 */
	public void setValues(Map values) 
	{
		this.values = values;
	}	
	/**
	 * @return innerLoopValues
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
	public void setIvl(String key, Object value)///changes here
	{
	    if (isMutable())
	    {
	        innerLoopValues.put(key, value);
	    }
	}

	/**
	 * Returns the object to which this map maps the specified key.
	 * 
	 * @param key the required key.
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
	 * for reset
	 */
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
				String keyCPEId = "CollectionProtocolEvent:" + i + "_id";
				
				values.put(keyClinicalStatus,Utility.toString(cpEvent.getClinicalStatus()));
				values.put(keyStudyCalendarEventPoint, Utility.toString(cpEvent.getStudyCalendarEventPoint()));
				values.put(keyCPEId,Utility.toString(cpEvent.getId()));
				Logger.out.debug("In Form keyCPEId..............."+values.get(keyCPEId));
				Collection specimenRequirementCollection = cpEvent.getSpecimenRequirementCollection();
				
				populateSpecimenRequirement(specimenRequirementCollection, i);
				
				i++;
			}
			
			outerCounter = protocolEventCollection.size();
		}
		
		//At least one outer row should be displayed in ADD MORE therefore
		if(outerCounter == 0)
		{
			outerCounter = 1;
		}
		
		//Populating the user-id array
		Collection userCollection = cProtocol.getCoordinatorCollection();
		
		if(userCollection != null)
		{
			protocolCoordinatorIds = new long[userCollection.size()];
			int i=0;

			Iterator it = userCollection.iterator();
			while(it.hasNext())
			{
				User user = (User)it.next();
				protocolCoordinatorIds[i] = user.getId().longValue();
				i++;
			}
		}
		if(cProtocol.getAliqoutInSameContainer()!= null) 
		{
			aliqoutInSameContainer = cProtocol.getAliqoutInSameContainer().booleanValue();
		}
		
		//For Consent Tracking 
		this.unsignedConsentURLName = cProtocol.getUnsignedConsentDocumentURL();
		this.consentWaived = cProtocol.getConsentsWaived().booleanValue();   
		//this.consentValues = prepareConsentTierMap(cProtocol.getConsentTierCollection());
	}
	/**
	 * For Consent Tracking
	 * Setting the consentValuesMap 
	 * @param consentTierColl This Containes the collection of ConsentTier
	 * @return tempMap
	 */
	private Map prepareConsentTierMap(Collection consentTierColl)
	{
		Map tempMap = new HashMap();
		if(consentTierColl!=null)
		{
			Iterator consentTierCollIter = consentTierColl.iterator();			
			int i = 0;
			while(consentTierCollIter.hasNext())
			{
				ConsentTier consent = (ConsentTier)consentTierCollIter.next();
				String statement = "ConsentBean:"+i+"_statement";
				String preDefinedStatementkey = "ConsentBean:"+i+"_predefinedConsents";
				String statementkey = "ConsentBean:"+i+"_consentTierID";
				tempMap.put(statement, consent.getStatement());
				tempMap.put(preDefinedStatementkey, consent.getStatement());
				tempMap.put(statementkey, consent.getId());
				i++;
			}
			consentTierCounter = consentTierColl.size();
		}
		return tempMap;
	}
	
	/**
	 * 
	 * @param specimenRequirementCollection This contains the Collection of specimen Requirement
	 * @param counter This is for tracking counts
	 */
	private void populateSpecimenRequirement(Collection specimenRequirementCollection, int counter)
	{
		int innerCounter = 0;
		if(specimenRequirementCollection != null)
		{
			int i = 1;

			Iterator iterator = specimenRequirementCollection.iterator();
			while(iterator.hasNext())
			{
			    SpecimenRequirement specimenRequirement = (SpecimenRequirement)iterator.next();
				String []key = {
					        "CollectionProtocolEvent:" + counter + "_SpecimenRequirement:" + i +"_specimenClass",
					        "CollectionProtocolEvent:" + counter + "_SpecimenRequirement:" + i +"_unitspan",
					        "CollectionProtocolEvent:" + counter + "_SpecimenRequirement:" + i +"_specimenType",
					        "CollectionProtocolEvent:" + counter + "_SpecimenRequirement:" + i +"_tissueSite",
					        "CollectionProtocolEvent:" + counter + "_SpecimenRequirement:" + i +"_pathologyStatus",
					        "CollectionProtocolEvent:" + counter + "_SpecimenRequirement:" + i +"_quantity_value",
					        "CollectionProtocolEvent:" + counter + "_SpecimenRequirement:" + i +"_id"
				        };
				setSpecimenRequirement(key,specimenRequirement);
				i++;
			}
			innerCounter = specimenRequirementCollection.size();
		}
		
		//At least one inner row should be displayed in ADD MORE therefore
		if(innerCounter == 0)
		{
			innerCounter = 1;
		}
		
		String innerCounterKey = String.valueOf(counter);
		innerLoopValues.put(innerCounterKey,String.valueOf(innerCounter));
	}
	
	/**
	 * 
	 * Overrides the validate method of ActionForm.
	 * @param mapping ActionMapping
	 * @param request HttpServletRequest
	 * @return errors
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
		Logger.out.debug("OPERATION : ----- : " + operation);
		ActionErrors errors = super.validate(mapping, request);
		Validator validator = new Validator();
		try
		{
			setRedirectValue(validator);
			// ---------START --------------------------------------			
				if(values.isEmpty())
				{
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.one.item.required",ApplicationProperties.getValue("collectionprotocol.eventtitle")));
				}
			// check for atleast 1 specimen requirement per CollectionProtocol Event
				for(int i=1;i<=outerCounter;i++)
				{
					String className = "CollectionProtocolEvent:"+i+"_SpecimenRequirement:1_specimenClass";
					Object obj = getValue(className);
					if(obj == null)
					{
						errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.one.item.required",ApplicationProperties.getValue("collectionprotocol.specimenreq")));
					}
				}
			// ---------END-----------------------------------------
      	// Mandar 10-apr-06 : bugid :353 :- Error messages should be in the same sequence as the sequence of fields on the page.
			//Check for PI can not be coordinator of the protocol.
			if(this.protocolCoordinatorIds != null && this.principalInvestigatorId!=-1)
			{
				for(int ind=0; ind < protocolCoordinatorIds.length; ind++)
				{
				 	if(protocolCoordinatorIds[ind] == this.principalInvestigatorId)
				 	{
						errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.pi.coordinator.same"));
						break;
				 	}
				}
			}
			Logger.out.debug("Protocol Coordinators : " + protocolCoordinatorIds); 
			
			boolean bClinicalStatus = false;
			boolean bStudyPoint = false;
			boolean bSpecimenClass = false;
			boolean bSpecimenType = false;
			boolean bTissueSite = false;
			boolean bPathologyStatus = false;
			
			Iterator it = this.values.keySet().iterator();
			while (it.hasNext())
			{
				String key = (String)it.next();
				String value = (String)values.get(key);
				
				if(!bClinicalStatus)
				{
					if(key.indexOf("clinicalStatus")!=-1 && !validator.isValidOption(value))
					{
						errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.selected",ApplicationProperties.getValue("collectionprotocol.clinicalstatus")));
						bClinicalStatus = true;
					}
				}				
				if(!bStudyPoint)
				{
					if(key.indexOf("studyCalendarEventPoint")!=-1)
					{
						//As study Calendar Event Point can be an empty value
						if(validator.isEmpty(value))
						{
							errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("collectionprotocol.studycalendartitle")));
							bStudyPoint = true;
						}
						else
						{
							 //Allow study Calendar Event Point as -ve value
						 	if(!validator.isDouble(value,false))
							{
								errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.studycalendarpoint",ApplicationProperties.getValue("collectionprotocol.studycalendartitle")));
								bStudyPoint = true;
							}
						}
					}
				}
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
				int ind = key.indexOf("_quantity_value");
				if((key.indexOf("_quantity_value"))!=-1)
				{
					if(!validator.isEmpty(value))
					{
						String classKey = key.substring(0,key.lastIndexOf("_quantity_value"));
						classKey = classKey + "_specimenClass";
						String classValue = (String)getValue(classKey);
						try
						{
							value = new BigDecimal(value).toPlainString();
							if (classValue.trim().equals("Cell"))
							{
								if(!validator.isNumeric(value,0))
		        				{
		        					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.format",ApplicationProperties.getValue("collectionprotocol.quantity")));
		        				}
							}
							else
							{
								// -------Mandar: 19-12-2005
								String typeKey = key.substring(0,key.lastIndexOf("_quantity_value"));
								typeKey = typeKey + "_specimenType";
								String typeValue = (String)getValue(typeKey);
								Logger.out.debug("TypeKey : "+ typeKey  + " : Type Value : " + typeValue);							
								if (typeValue.trim().equals(Constants.FROZEN_TISSUE_SLIDE) || typeValue.trim().equals(Constants.FIXED_TISSUE_BLOCK) || typeValue.trim().equals(Constants.FROZEN_TISSUE_BLOCK) || typeValue.trim().equals(Constants.FIXED_TISSUE_SLIDE))
								{
			        				if(!validator.isNumeric(value,0))
			        				{
			        					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.format",ApplicationProperties.getValue("collectionprotocol.quantity")));
			        				}
								}
								else
								{
									if(!validator.isDouble(value,true))
			        				{
			        					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.format",ApplicationProperties.getValue("collectionprotocol.quantity")));
			        				}
								}
							}
						}
						catch (NumberFormatException exp)
				        {    		  
							errors.add(ActionErrors.GLOBAL_ERROR,new ActionError("errors.item.format",ApplicationProperties.getValue("collectionprotocol.quantity")));
						}		
					}
				} // if  quantity
			}
		}
		catch (Exception excp)
		{
	    	// use of logger as per bug 79
	    	Logger.out.error(excp.getMessage(),excp); 
	    	Logger.out.debug(excp);
			errors = new ActionErrors();
		}
		return errors;
	}
		
	/**
	 *@return Returns the id assigned to form bean
	 */
	public int getFormId()
	{
		return Constants.COLLECTION_PROTOCOL_FORM_ID;
	}
	
	/**
	 * This is the main Class that creates object of CollectionProtocolForm 
	 * 	@param args cmd line arguments
	 */
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
	
	/**
     * This method sets Identifier of Objects inserted by AddNew activity in Form-Bean which initialized AddNew action
     * @param addNewFor - FormBean ID of the object inserted
     * @param addObjectIdentifier - Identifier of the Object inserted 
     */
    public void setAddNewObjectIdentifier(String addNewFor, Long addObjectIdentifier)
    {
        if(addNewFor.equals("principalInvestigator"))
        {
            setPrincipalInvestigatorId(addObjectIdentifier.longValue());
        }
        else if(addNewFor.equals("protocolCoordinator"))
        {
            long []pcoordIDs = {Long.parseLong(addObjectIdentifier.toString())};           
			setProtocolCoordinatorIds(pcoordIDs);
        } 
    }
	
	/**
	 * @return Returns the aliqoutInSameContainer.
	 */
	public boolean isAliqoutInSameContainer()
	{
		return aliqoutInSameContainer;
	}
	/**
	 * @param aliqoutInSameContainer The aliqoutInSameContainer to set.
	 */
	public void setAliqoutInSameContainer(boolean aliqoutInSameContainer)
	{
		this.aliqoutInSameContainer = aliqoutInSameContainer;
	}
	
	
	//	For Consent Tracking Start
	
	/**
	 * @return unsignedConsentURLName  Get Unsigned Signed URL name  
	 */
	public String getUnsignedConsentURLName()
	{
		return unsignedConsentURLName;
	}
	
	/**
	 * @param unsignedConsentURLName  Set Unsigned Signed URL name
	 */
	public void setUnsignedConsentURLName(String unsignedConsentURLName)
	{
		this.unsignedConsentURLName = unsignedConsentURLName;
	}	
	
	/**
     * @param key Key
     * @param value Value
     */
    public void setConsentValue(String key, Object value) 
    {
   	 	if (isMutable())
   	 	{
   	 		consentValues.put(key, value);
   	 	}
    }

    /**
     * @param key Key
     * @return Statements
     */
    public Object getConsentValue(String key) 
    {
        return consentValues.get(key);
    }

    /**
     * 
     * @return consentValues   Set Consents into the Map
     */
    public Map getConsentValues() 
	{
		return consentValues;
	}
	
    /**
     * @param consentValues Set Consents into the Map
     */
	public void setConsentValues(Map consentValues) 
	{
		this.consentValues = consentValues;
	}

	/**
	 *@return consentTierCounter  This will keep track of count of Consent Tier
	 */
	public int getConsentTierCounter()
	{
		return consentTierCounter;
	}
	
	/**
	 * 
	 * @param consentTierCounter  This will keep track of count of Consent Tier
	 */
	public void setConsentTierCounter(int consentTierCounter)
	{
		this.consentTierCounter = consentTierCounter;
	}
	
	/**
	 * If consent waived is true then no need to check consents prior to distribution
	 * @return consentWaived
	 */
	public boolean isConsentWaived()
	{
		return consentWaived;
	}

	/**
	 * If consent waived is true then no need to check consents prior to distribution
	 * @param consentWaived If consent waived is true then no need to check consents prior to distribution
	 */
	public void setConsentWaived(boolean consentWaived)
	{
		this.consentWaived = consentWaived;
	}
	//	For Consent Tracking End
}