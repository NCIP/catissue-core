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

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.domain.AbstractDomainObject;
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
	private static final long serialVersionUID = 1L;

	/**
	 * logger Logger - Generic logger.
	 */
	private static org.apache.log4j.Logger logger = Logger.getLogger(CollectionProtocolForm.class);

	protected long[] protocolCoordinatorIds;

	/**
	 * Counter that contains number of rows in the 'Add More' functionality. outer block
	 */
	private int outerCounter=1;

	/**
	 * Patch Id : Collection_Event_Protocol_Order_4
	 * Description : To get CollectionProtocol Events in order (Changed from HashMap to LinkedHashMap)
	 */
	/**
	 * Counter that contains number of rows in the 'Add More' functionality. inner block
	 */
	protected Map innerLoopValues = new LinkedHashMap();
		
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
	private int consentTierCounter=0;
	/**
	 * CheckBox for consent is checked or not
	 */
	private boolean consentWaived = false;
	//Consent tracking(Virender Mehta)
	
	protected long[] siteIds;
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
    public void setValue(final String key, final Object value) 
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
    public Object getValue(final String key) 
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
	public void setValues(final Map values) 
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
	public void setInnerLoopValues(final Map innerLoopValues)
	{
		this.innerLoopValues = innerLoopValues;
	}

	/**
	 * Associates the specified object with the specified key in the map.
	 * @param key the key to which the object is mapped.
	 * @param value the object which is mapped.
	 */
	public void setIvl(final String key, final Object value)///changes here
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
	public Object getIvl(final String key)
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
	public void setOuterCounter(final int outerCounter)
	{
		this.outerCounter = outerCounter;
	}
		
	/**
	 * Method to set class attributes
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
		final CollectionProtocol cProtocol = (CollectionProtocol)abstractDomain;
		if(cProtocol.getAliquotInSameContainer()!= null) 
		{
			aliqoutInSameContainer = cProtocol.getAliquotInSameContainer().booleanValue();
		}
		
		//For Consent Tracking 
		this.unsignedConsentURLName = cProtocol.getUnsignedConsentDocumentURL();

		if (cProtocol.getConsentsWaived() == null)
		{
			this.consentWaived = false;
		}
		else
		{
			this.consentWaived = cProtocol.getConsentsWaived().booleanValue();
		}
		//this.consentValues = prepareConsentTierMap(cProtocol.getConsentTierCollection());
	}
	/**
	 * For Consent Tracking
	 * Setting the consentValuesMap 
	 * @param consentTierColl This Containes the collection of ConsentTier
	 * @return tempMap
	 */
//	private Map prepareConsentTierMap(Collection consentTierColl)
//	{
//		Map tempMap = new HashMap();
//		if(consentTierColl!=null)
//		{
//			Iterator consentTierCollIter = consentTierColl.iterator();			
//			int i = 0;
//			while(consentTierCollIter.hasNext())
//			{
//				ConsentTier consent = (ConsentTier)consentTierCollIter.next();
//				String statement = "ConsentBean:"+i+"_statement";
//				String preDefinedStatementkey = "ConsentBean:"+i+"_predefinedConsents";
//				String statementkey = "ConsentBean:"+i+"_consentTierID";
//				tempMap.put(statement, consent.getStatement());
//				tempMap.put(preDefinedStatementkey, consent.getStatement());
//				tempMap.put(statementkey, consent.getId());
//				i++;
//			}
//			consentTierCounter = consentTierColl.size();
//		}
//		return tempMap;
//	}

	/**
	 * Overrides the validate method of ActionForm.
	 * @return error ActionErrors instance
	 * @param mapping Actionmapping instance
	 * @param request HttpServletRequest instance
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
		logger.debug("OPERATION : ----- : " + this.getOperation());
		ActionErrors errors = super.validate(mapping, request);
//		Validator validator = new Validator();
		try
		{
				
			//Check for PI can not be coordinator of the protocol.
			if(this.protocolCoordinatorIds != null && this.principalInvestigatorId!=-1)
			{
				for(int ind=0; ind < protocolCoordinatorIds.length;ind++)
				{
				 	if(protocolCoordinatorIds[ind] == this.principalInvestigatorId)
				 	{
						errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.pi.coordinator.same"));
						break;
				 	}
				}
			}
				
			logger.debug("Protocol Coordinators : " + protocolCoordinatorIds); 
		}
		catch (Exception excp)
		{
	    	// use of logger as per bug 79
			logger.error(excp.getMessage(),excp); 
			logger.debug(excp);
			errors = new ActionErrors();
		}
		return errors;
	}
	
	
	
	/**
	 * Returns the id assigned to form bean
	 * @return COLLECTION_PROTOCOL_FORM_ID
	 */
	public int getFormId()
	{
		return Constants.COLLECTION_PROTOCOL_FORM_ID;
	}
	
	/**
	 * This is the main method, main( ) is the method called when a Java application begins
	 * @param args array of instances of the class String.
	 * args receives any command-line arguments present when the program is executed.
	 */
//	public static void main(String[] args)
//	{
//		int maxCount=1;
////		int maxIntCount=1;
//		
//		CollectionProtocolForm collectionProtocolForm = null;
//		
//		Object obj = new Object();//request.getAttribute("collectionProtocolForm");
//		
//		if(obj != null && obj instanceof CollectionProtocolForm)
//		{
//			collectionProtocolForm = (CollectionProtocolForm)obj;
//			maxCount = collectionProtocolForm.getOuterCounter();
//		}
//	
//		for(int counter=1;counter<=maxCount;counter++)
//		{
//			String commonLabel = "value(CollectionProtocolEvent:" + counter;
//			
//			String cid = "ivl(" + counter + ")";
//			String functionName = "insRow('" + commonLabel + "','" + cid +"')";
//			
//			if(collectionProtocolForm!=null)
//			{
//				Object o = collectionProtocolForm.getIvl(cid);
//				maxIntCount = Integer.parseInt(o.toString());
//			}
//		}
//	}
//	
	/**
     * This method sets Identifier of Objects inserted by AddNew activity in Form-Bean which initialized AddNew action
     * @param addNewFor - FormBean ID of the object inserted
     *  @param addObjectIdentifier - Identifier of the Object inserted 
     */
    public void setAddNewObjectIdentifier(String addNewFor, Long addObjectIdentifier)
    {
        if("principalInvestigator".equals(addNewFor))
        {
            setPrincipalInvestigatorId(addObjectIdentifier.longValue());
        }
        else if("protocolCoordinator".equals(addNewFor))
        {
        	final long[] pcoordIDs = { Long.parseLong(addObjectIdentifier.toString()) };
           
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
    public void setConsentValue(final String key, final Object value) 
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
    public Object getConsentValue(final String key) 
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
	public void setConsentValues(final Map consentValues) 
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
	public void setConsentWaived(final boolean consentWaived)
	{
		this.consentWaived = consentWaived;
	}
	//	For Consent Tracking End

	
	public long[] getSiteIds()
	{
		return siteIds;
	}

	
	public void setSiteIds(final long[] siteIds)
	{
		this.siteIds = siteIds;
	}

	
	
	
}