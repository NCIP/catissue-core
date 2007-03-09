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
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.domain.Biohazard;
import edu.wustl.catissuecore.domain.ConsentTier;
import edu.wustl.catissuecore.domain.ConsentTierResponse;
import edu.wustl.catissuecore.domain.ConsentTierStatus;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCharacteristics;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;

/**
 * NewSpecimenForm Class is used to encapsulate all the request parameters passed 
 * from New Specimen webpage.
 * @author aniruddha_phadnis
 */
public class NewSpecimenForm extends SpecimenForm implements ConsentTierData
{

	//Consent Tracking Module (Virender Mehta)
	/**
	 * Map for Storing responses for Consent Tiers.
	 */
	protected Map consentResponseForSpecimenValues = new HashMap();
	/**
	 * No of Consent Tier
	 */
	private int consentTierCounter=0;
	/**
	 * Signed Consent URL
	 */
	protected String signedConsentUrl="";
	/**
	 * Witness name that may be PI
	 */
	protected String witnessName;
	
	/**
	 * Consent Date, Date on which Consent is Signed
	 */
	protected String consentDate="";
	
	/**
	 * This will be set in case of withdrawl popup
	 */
	protected String withdrawlButtonStatus = Constants.WITHDRAW_RESPONSE_NOACTION;
	/**
	 * This will be set in case if there is any change in response.
	 */
	protected String applyChangesTo= Constants.APPLY_NONE;
	/**
	 * If user changes the response after submiting response then this string will have 
	 * responseKeys for which response is changed .
	 */
	protected String stringOfResponseKeys="";
	//Consent Tracking Module (Virender Mehta)

	/**
     * Specimen Collection Group ID
     */
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
    
    /**
     * A number that tells how many aliquots to be created.
     */
    private String noOfAliquots;
    
    /**
     * Initial quantity per aliquot.
     */
    private String quantityPerAliquot;
    
    /**
	 * Represents the weather participant Name is selected or not.
	 *
	 */    	
	private boolean checkedButton;
	
	/**
     * If true then this specimen is an aliquot else false.
     */
	//private boolean aliquot;
	
	/**
     * A historical information about the specimen i.e. whether the specimen is a new specimen
     * or a derived specimen or an aliquot.
     */
    private String lineage;

//	-------------Mandar AutoEvents CollectionEvent parameters start
	private long collectionEventId;																											// Mandar : CollectionEvent 10-July-06
	private long collectionEventSpecimenId;
	private long collectionEventUserId;
	private String collectionEventdateOfEvent;
	private String collectionEventTimeInHours;
	private String collectionEventTimeInMinutes;
	private String collectionEventCollectionProcedure;
	private String collectionEventContainer;
	private String collectionEventComments;
	
	//-------------Mandar AutoEvents CollectionEvent parameters end
	
//	-------------Mandar AutoEvents ReceivedEvent parameters start
	private long receivedEventId;
	private long receivedEventSpecimenId;
	private long receivedEventUserId;
	private String receivedEventDateOfEvent;
	private String receivedEventTimeInHours;
	private String receivedEventTimeInMinutes;
	private String receivedEventReceivedQuality;
	private String receivedEventComments;
	
//	-------------Mandar AutoEvents ReceivedEvent parameters end
	
	
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
		if (isMutable())
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
 //   	this.parentPresent = false;
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
       
    	super.setAllValues(abstractDomain);
    	Specimen specimen = (Specimen) abstractDomain;
    	id = specimen.getId().longValue();
    	this.parentPresent = false;
    	SpecimenCollectionGroup specimenCollectionGroup = specimen.getSpecimenCollectionGroup();
    	if(specimenCollectionGroup!=null)
    		this.specimenCollectionGroupId = Utility.toString(specimenCollectionGroup.getId());
    	
    	if(specimen.getParentSpecimen() != null)
    	{
    		Logger.out.debug("ParentSpecimen : -- "+specimen.getParentSpecimen());
    		this.parentSpecimenId = String.valueOf(specimen.getParentSpecimen().getId());
    		this.parentPresent = true;
    	}
    	
//    	this.aliquot = specimen.getIsAliquot().booleanValue();
    	
        SpecimenCharacteristics characteristic = specimen.getSpecimenCharacteristics();
        this.pathologicalStatus = specimen.getPathologicalStatus();
        this.tissueSide = characteristic.getTissueSide();
        this.tissueSite = characteristic.getTissueSite();
        this.lineage = specimen.getLineage();
        
        Collection biohazardCollection = specimen.getBiohazardCollection();
        bhCounter = 1;
        
        if(biohazardCollection != null && biohazardCollection.size() != 0)
        {
        	biohazard = new HashMap();
        	
        	int i=1;
        	
        	Iterator it = biohazardCollection.iterator();
        	while(it.hasNext())
        	{
        		String key1 = "Biohazard:" + i + "_type";
				String key2 = "Biohazard:" + i + "_id";
				String key3 = "Biohazard:" + i + "_persisted";
				
				Biohazard hazard = (Biohazard)it.next();
				
				biohazard.put(key1,hazard.getType());
				biohazard.put(key2,hazard.getId());
				
				//boolean for showing persisted value
				biohazard.put(key3,"true");
				
				i++;
        	}
        	
        	bhCounter = biohazardCollection.size();
        }
        
        /**
         * For Consent Tracking setting UI attributes (Virender Mehta)
         */
    	User witness= specimenCollectionGroup.getCollectionProtocolRegistration().getConsentWitness();
		if(witness==null||witness.getFirstName()==null)
		{
			this.witnessName="";
		}
		else
		{
			this.witnessName=Utility.toString(witness.getFirstName());
		}
		this.signedConsentUrl=Utility.toString(specimenCollectionGroup.getCollectionProtocolRegistration().getSignedConsentDocumentURL());
		this.consentDate=Utility.parseDateToString(specimenCollectionGroup.getCollectionProtocolRegistration().getConsentSignatureDate(), Constants.DATE_PATTERN_MM_DD_YYYY);
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
             	
             	if(parentPresent && !validator.isValidOption(parentSpecimenId))
             	{
             		errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("createSpecimen.parent")));
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
             	
             	//Mandar 18-July-06: AutoEvents: 
             	if(operation.equalsIgnoreCase(Constants.ADD  ) )
             	{
             		//CollectionEvent validation.
            		validateCollectionEvent(errors, validator);
            		//ReceivedEvent validation
            		validateReceivedEvent(errors, validator);
             	}
 
             	/*if(checkedButton)
             	{
             		if(!validator.isNumeric(noOfAliquots))
                    {
                    	errors.add(ActionErrors.GLOBAL_ERROR,new ActionError("errors.item.format",ApplicationProperties.getValue("aliquots.noOfAliquots")));
                    }
             		
             		if(quantityPerAliquot != null && quantityPerAliquot.trim().length() != 0)
                    {
            			if(Utility.isQuantityDouble(className,type))
            			{
            		        if(!validator.isDouble(quantityPerAliquot.trim()))
            		        {
            		        	errors.add(ActionErrors.GLOBAL_ERROR,new ActionError("errors.item.format",ApplicationProperties.getValue("aliquots.qtyPerAliquot")));
            		        }
            			}
            			else
            			{
            				if(!validator.isNumeric(quantityPerAliquot.trim()))
            		        {
            		        	errors.add(ActionErrors.GLOBAL_ERROR,new ActionError("errors.item.format",ApplicationProperties.getValue("aliquots.qtyPerAliquot")));
            		        }
            			}
                    }
             	}*/

             	//Validations for Biohazard Add-More Block
                String className = "Biohazard:";
                String key1 = "_type";
                String key2 = "_" + Constants.SYSTEM_IDENTIFIER;
                String key3 = "_persisted";
                int index = 1;
                
                while(true)
                {
                	String keyOne = className + index + key1;
					String keyTwo = className + index + key2;
					String keyThree = className + index + key3;
					
                	String value1 = (String)biohazard.get(keyOne);
                	String value2 = (String)biohazard.get(keyTwo);
                	String value3 = (String)biohazard.get(keyThree);
                	
                	if(value1 == null  && value2 == null && value3 == null)
                	{
                		break;
                	}
                	else if(!validator.isValidOption(value1) && !validator.isValidOption(value2))
                	{
                		biohazard.remove(keyOne);
                		biohazard.remove(keyTwo);
                		biohazard.remove(keyThree);
                	}
                	else if((validator.isValidOption(value1) && !validator.isValidOption(value2)) 
                			|| (!validator.isValidOption(value1) && validator.isValidOption( value2)))   		
                	{
                		errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.newSpecimen.biohazard.missing",ApplicationProperties.getValue("newSpecimen.msg")));
                		break;
                	}
                	index++;
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
     * This method sets Identifier of Objects inserted by AddNew activity in Form-Bean which initialized AddNew action
     * @param formBeanId - FormBean ID of the object inserted
     *  @param addObjectIdentifier - Identifier of the Object inserted 
     */
	public void setAddNewObjectIdentifier(String addNewFor, Long addObjectIdentifier)
    {
        if(addNewFor.equals("specimenCollectionGroupId"))
        {
            Logger.out.debug("Setting SCG ID in NewSpecimenForm#####"+ addObjectIdentifier);
            
            setSpecimenCollectionGroupId(addObjectIdentifier.toString());
        }
    }
	
	/**
	 * Returns the no. of aliquots to be created.
	 * @return The no. of aliquots to be created.
	 * @see #setNoOfAliquots(String)
	 */
	public String getNoOfAliquots()
	{
		return noOfAliquots;
	}
	
	/**
     * Sets the no. of aliquots to be created.
     * @param noOfAliquots The no. of aliquots to be created.
     * @see #getNoOfAliquots()
     */
	public void setNoOfAliquots(String noOfAliquots)
	{
		this.noOfAliquots = noOfAliquots;
	}
	
	/**
 	 * Returns the initial quantity per aliquot.
 	 * @return The initial quantity per aliquot.
 	 * @see #setQuantityPerAliquot(String)
 	 */
	public String getQuantityPerAliquot()
	{
		return quantityPerAliquot;
	}
	
	/**
     * Sets the initial quantity per aliquot.
     * @param quantityPerAliquot The initial quantity per aliquot.
     * @see #getQuantityPerAliquot()
     */
	public void setQuantityPerAliquot(String quantityPerAliquot)
	{
		this.quantityPerAliquot = quantityPerAliquot;
	}
	
	/**
 	 * Tells whether the button is checked ot unchecked.
 	 * @return True if button is checked else false.
 	 * @see #setCheckedButton(boolean)
 	 */
	public boolean isCheckedButton()
	{
		return checkedButton;
	}

	/**
     * Sets/Resets the checked button.
     * @param checkedButton The value of checked button.
     * @see #isCheckedButton()
     */
	public void setCheckedButton(boolean checkedButton)
	{
		this.checkedButton = checkedButton;
	}

	// Mandar: 10-july-06 AutoEvents : Collection Event start
			
			/**
			 * @return Returns the collectionEventCollectionProcedure.
			 */
			public String getCollectionEventCollectionProcedure() {
				return collectionEventCollectionProcedure;
			}
			/**
			 * @param collectionEventCollectionProcedure The collectionEventCollectionProcedure to set.
			 */
			public void setCollectionEventCollectionProcedure(
					String collectionEventCollectionProcedure) {
				this.collectionEventCollectionProcedure = collectionEventCollectionProcedure;
			}
			/**
			 * @return Returns the collectionEventComments.
			 */
			public String getCollectionEventComments() {
				return collectionEventComments;
			}
			/**
			 * @param collectionEventComments The collectionEventComments to set.
			 */
			public void setCollectionEventComments(String collectionEventComments) {
				this.collectionEventComments = collectionEventComments;
			}
			/**
			 * @return Returns the collectionEventContainer.
			 */
			public String getCollectionEventContainer() {
				return collectionEventContainer;
			}
			/**
			 * @param collectionEventContainer The collectionEventContainer to set.
			 */
			public void setCollectionEventContainer(String collectionEventContainer) {
				this.collectionEventContainer = collectionEventContainer;
			}
			/**
			 * @return Returns the collectionEventdateOfEvent.
			 */
			public String getCollectionEventdateOfEvent() {
				return collectionEventdateOfEvent;
			}
			/**
			 * @param collectionEventdateOfEvent The collectionEventdateOfEvent to set.
			 */
			public void setCollectionEventdateOfEvent(String collectionEventdateOfEvent) {
				this.collectionEventdateOfEvent = collectionEventdateOfEvent;
			}
			/**
			 * @return Returns the collectionEventSpecimenId.
			 */
			public long getCollectionEventSpecimenId() {
				return collectionEventSpecimenId;
			}
			/**
			 * @param collectionEventSpecimenId The collectionEventSpecimenId to set.
			 */
			public void setCollectionEventSpecimenId(long collectionEventSpecimenId) {
				this.collectionEventSpecimenId = collectionEventSpecimenId;
			}
		/**
		 * @return Returns the collectionEventId.
		 */
		public long getCollectionEventId() 
		{
			return collectionEventId;
		}
		/**
		 * @param collectionEventId The collectionEventId to set.
		 */
		public void setCollectionEventId(long collectionEventId) 
		{
			this.collectionEventId = collectionEventId;
		}
			/**
			 * @return Returns the collectionEventTimeInHours.
			 */
			public String getCollectionEventTimeInHours() {
				return collectionEventTimeInHours;
			}
			/**
			 * @param collectionEventTimeInHours The collectionEventTimeInHours to set.
			 */
			public void setCollectionEventTimeInHours(String collectionEventTimeInHours) {
				this.collectionEventTimeInHours = collectionEventTimeInHours;
			}
			/**
			 * @return Returns the collectionEventTimeInMinutes.
			 */
			public String getCollectionEventTimeInMinutes() {
				return collectionEventTimeInMinutes;
			}
			/**
			 * @param collectionEventTimeInMinutes The collectionEventTimeInMinutes to set.
			 */
			public void setCollectionEventTimeInMinutes(
					String collectionEventTimeInMinutes) {
				this.collectionEventTimeInMinutes = collectionEventTimeInMinutes;
			}
			/**
			 * @return Returns the collectionEventUserId.
			 */
			public long getCollectionEventUserId() {
				return collectionEventUserId;
			}
			/**
			 * @param collectionEventUserId The collectionEventUserId to set.
			 */
			public void setCollectionEventUserId(long collectionEventUserId) {
				this.collectionEventUserId = collectionEventUserId;
			}
	// Mandar: 10-july-06 AutoEvents : Collection Event end
			
	//Mandar : 11-July-06 AutoEvents : CollectionEvent validation
			private void validateCollectionEvent(ActionErrors errors, Validator validator)
			{
	           	if ((collectionEventUserId) == -1L)
	            {
	           		errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required","Collection Event's user"));
	            }
	           	if (!validator.checkDate(collectionEventdateOfEvent) )
	           	{
	           		errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required","Collection Event's date"));
	           	}

	         	// checks the collectionProcedure
	          	if (!validator.isValidOption( collectionEventCollectionProcedure ) )
	            {
	           		errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("collectioneventparameters.collectionprocedure")));
	            }
	
			}

	//Mandar : 11-july-06 AutoEvents : ReceivedEvent start
			
	/**
	 * @return Returns the receivedEventComments.
	 */
	public String getReceivedEventComments() {
		return receivedEventComments;
	}
	/**
	 * @param receivedEventComments The receivedEventComments to set.
	 */
	public void setReceivedEventComments(String receivedEventComments) {
		this.receivedEventComments = receivedEventComments;
	}
	/**
	 * @return Returns the receivedEventDateOfEvent.
	 */
	public String getReceivedEventDateOfEvent() {
		return receivedEventDateOfEvent;
	}
	/**
	 * @param receivedEventDateOfEvent The receivedEventDateOfEvent to set.
	 */
	public void setReceivedEventDateOfEvent(String receivedEventDateOfEvent) {
		this.receivedEventDateOfEvent = receivedEventDateOfEvent;
	}
	/**
	 * @return Returns the receivedEventReceivedQuality.
	 */
	public String getReceivedEventReceivedQuality() {
		return receivedEventReceivedQuality;
	}
	/**
	 * @param receivedEventReceivedQuality The receivedEventReceivedQuality to set.
	 */
	public void setReceivedEventReceivedQuality(
			String receivedEventReceivedQuality) {
		this.receivedEventReceivedQuality = receivedEventReceivedQuality;
	}
	/**
	 * @return Returns the receivedEventSpecimenId.
	 */
	public long getReceivedEventSpecimenId() {
		return receivedEventSpecimenId;
	}
	/**
	 * @param receivedEventSpecimenId The receivedEventSpecimenId to set.
	 */
	public void setReceivedEventSpecimenId(long receivedEventSpecimenId) {
		this.receivedEventSpecimenId = receivedEventSpecimenId;
	}
	/**
	 * @return Returns the receivedEventId.
	 */
	public long getReceivedEventId() 
	{
		return receivedEventId;
	}
	/**
	 * @param receivedEventId The receivedEventId to set.
	 */
	public void setReceivedEventId(long receivedEventId) 
	{
		this.receivedEventId = receivedEventId;
	}
	/**
	 * @return Returns the receivedEventTimeInHours.
	 */
	public String getReceivedEventTimeInHours() {
		return receivedEventTimeInHours;
	}
	/**
	 * @param receivedEventTimeInHours The receivedEventTimeInHours to set.
	 */
	public void setReceivedEventTimeInHours(String receivedEventTimeInHours) {
		this.receivedEventTimeInHours = receivedEventTimeInHours;
	}
	/**
	 * @return Returns the receivedEventTimeInMinutes.
	 */
	public String getReceivedEventTimeInMinutes() {
		return receivedEventTimeInMinutes;
	}
	/**
	 * @param receivedEventTimeInMinutes The receivedEventTimeInMinutes to set.
	 */
	public void setReceivedEventTimeInMinutes(String receivedEventTimeInMinutes) {
		this.receivedEventTimeInMinutes = receivedEventTimeInMinutes;
	}
	/**
	 * @return Returns the receivedEventUserId.
	 */
	public long getReceivedEventUserId() {
		return receivedEventUserId;
	}
	/**
	 * @param receivedEventUserId The receivedEventUserId to set.
	 */
	public void setReceivedEventUserId(long receivedEventUserId) {
		this.receivedEventUserId = receivedEventUserId;
	}

	//Mandar : 18-July-06 ReceivedEvent validation
	private void validateReceivedEvent(ActionErrors errors, Validator validator)
	{
       	if ((receivedEventUserId) == -1L)
        {
       		errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required","Received Event's user"));
        }
       	if (!validator.checkDate(receivedEventDateOfEvent) )
       	{
       		errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required","Received Event's date"));
       	}

     	// checks the collectionProcedure
      	if (!validator.isValidOption( receivedEventReceivedQuality ) )
        {
       		errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("receivedeventparameters.receivedquality")));
        }

				
	}
	//Mandar : 11-july-06 AutoEvents : ReceivedEvent end
	
	/**
	 * Returns the historical information about the specimen.
	 * @return The historical information about the specimen.
	 * @see #setLineage(String)
	 */
	public String getLineage()
	{
		return lineage;
	}
	
	/**
     * Sets the historical information about the specimen.
     * @param label The historical information about the specimen.
     * @see #getLineage()
     */
	public void setLineage(String lineage)
	{
		this.lineage = lineage;
	}

	//Consent Tracking Module Virender mehta	
	/**
	 * @return consentResponseForSpecimenValues  The comments associated with Response at Specimen level.
	 */	
	public Map getConsentResponseForSpecimenValues()
	{
		return consentResponseForSpecimenValues;
	}
	
	/**
	 * @param consentResponseForSpecimenValues  The comments associated with Response at Specimen level
	 */	
	public void setConsentResponseForSpecimenValues(Map consentResponseForSpecimenValues) 
	{
		this.consentResponseForSpecimenValues = consentResponseForSpecimenValues;
	}
	
	/**
     *@param key Key prepared for saving data.
     *@param value Values correspponding to key
     */
    public void setConsentResponseForSpecimenValue(String key, Object value) 
    {
   	 if (isMutable())
   		consentResponseForSpecimenValues.put(key, value);
    }

    /**
     * @param key Key prepared for saving data.
     * @return consentResponseForSpecimenValues.get(key)
     */
    public Object getConsentResponseForSpecimenValue(String key) 
    {
        return consentResponseForSpecimenValues.get(key);
    }
    
	/**
	 * @return values in map consentResponseForSpecimenValues
	 */
	public Collection getAllConsentResponseForSpecimenValue() 
	{
		return consentResponseForSpecimenValues.values();
	}

	/**
	 * @return consentDate The Date on Which Consent is Signed
     */	
	public String getConsentDate()
	{
		return consentDate;
	}
	
	/**
	 * @param consentDate The Date on Which Consent is Signed
	 */
	public void setConsentDate(String consentDate)
	{
		this.consentDate = consentDate;
	}

	/**
	 *@return consentTierCounter  This will keep track of count of Consent Tier
	 */
	public int getConsentTierCounter()
	{
		return consentTierCounter;
	}
	
	/**
	 *@param consentTierCounter  This will keep track of count of Consent Tier
	 */
	public void setConsentTierCounter(int consentTierCounter)
	{
		this.consentTierCounter = consentTierCounter;
	}

	/**
	 * @return signedConsentUrl The reference to the electric signed document(eg PDF file)
	 */	
	public String getSignedConsentUrl()
	{
		return signedConsentUrl;
	}

	/**
	 * @param signedConsentUrl The reference to the electric signed document(eg PDF file)
	 */	
	public void setSignedConsentUrl(String signedConsentUrl)
	{
		this.signedConsentUrl = signedConsentUrl;
	}

	/**
	 * @return witnessName The name of the witness to the consent Signature(PI or coordinator of the Collection Protocol)
	 */
	public String getWitnessName()
	{
		return witnessName;
	}
	
	/**
	 * @param witnessName The name of the witness to the consent Signature(PI or coordinator of the Collection Protocol)
	 */	
	public void setWitnessName(String witnessName)
	{
		this.witnessName = witnessName;
	}
	
	/**
	 * It returns status of button(return,discard,reset)
	 * @return withdrawlButtonStatus
	 */
	public String getWithdrawlButtonStatus()
	{
		return withdrawlButtonStatus;
	}

	/**
	 * It returns status of button(return,discard,reset)
	 * @param withdrawlButtonStatus return,discard,reset
	 */
	public void setWithdrawlButtonStatus(String withdrawlButtonStatus)
	{
		this.withdrawlButtonStatus = withdrawlButtonStatus;
	}
	
	/**
	 * @param applyChangesTo 
	 */
	public String getApplyChangesTo()
	{
		return applyChangesTo;
	}

	/**
	 * @param applyChangesTo 
	 */
	public void setApplyChangesTo(String applyChangesTo)
	{
		this.applyChangesTo = applyChangesTo;
	}
	
	/**
	 * @return stringOfResponseKeys
	 */
	public String getStringOfResponseKeys()
	{
		return stringOfResponseKeys;
	}

	/**
	 * @param stringOfResponseKeys
	 */
	public void setStringOfResponseKeys(String stringOfResponseKeys)
	{
		this.stringOfResponseKeys = stringOfResponseKeys;
	}
	
	/**
	 * This function creates Array of String of keys and add them into the consentTiersList.
	 * @return consentTiersList
	 */
	public Collection getConsentTiers()
	{
		Collection consentTiersList=new ArrayList();
		String [] strArray = null;
		int noOfConsents =this.getConsentTierCounter();
		for(int counter=0;counter<noOfConsents;counter++)
		{	
			strArray = new String[6];
			strArray[0]="consentResponseForSpecimenValues(ConsentBean:"+counter+"_consentTierID)";
			strArray[1]="consentResponseForSpecimenValues(ConsentBean:"+counter+"_statement)";
			strArray[2]="consentResponseForSpecimenValues(ConsentBean:"+counter+"_participantResponse)";
			strArray[3]="consentResponseForSpecimenValues(ConsentBean:"+counter+"_participantResponseID)";
			strArray[4]="consentResponseForSpecimenValues(ConsentBean:"+counter+"_specimenLevelResponse)";
			strArray[5]="consentResponseForSpecimenValues(ConsentBean:"+counter+"_specimenLevelResponseID)";
			consentTiersList.add(strArray);
		}
		return consentTiersList;
	}	
	
	/**
	 * This funtion returns the format of the response Key prepared. 
	 * @return consentResponseForSpecimenValues(ConsentBean:`_participantResponse)
	 */	
	public String getConsentTierMap()
	{
		return "consentResponseForSpecimenValues(ConsentBean:`_specimenLevelResponse)";
	}
	//Consent Tracking Module Virender mehta
}