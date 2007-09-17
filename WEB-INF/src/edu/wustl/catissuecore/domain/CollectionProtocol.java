										  							   /**
 * <p>Title: CollectionProtocol Class</p>
 * <p>Description:  A set of written procedures that describe how a biospecimen is prospectively collected.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Mandar Deshmukh
 * @version 1.00
 * Created on July 12, 2005
 */

package edu.wustl.catissuecore.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;

import edu.wustl.catissuecore.actionForm.CollectionProtocolForm;
import edu.wustl.catissuecore.bean.ConsentBean;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.actionForm.IValueObject;
import edu.wustl.common.util.KeyComparator;
import edu.wustl.common.util.MapDataParser;
import edu.wustl.common.util.logger.Logger;

/**
 * A set of written procedures that describe how a biospecimen is prospectively collected.
 * @hibernate.joined-subclass table="CATISSUE_COLLECTION_PROTOCOL"
 * @hibernate.joined-subclass-key column="IDENTIFIER" 
 * @author Mandar Deshmukh
 */
public class CollectionProtocol extends SpecimenProtocol implements java.io.Serializable,Comparable
{
	private static final long serialVersionUID = 1234567890L;
	
	/**
	 * Patch Id : Collection_Event_Protocol_Order_3 (Changed From HashSet to LinkedHashSet)
	 * Description : To get the specimen requirement in order
	 */
	/**
	 * Collection of studies associated with the CollectionProtocol.
	 */
	protected Collection distributionProtocolCollection = new LinkedHashSet();
	
	/**
	 * Collection of users associated with the CollectionProtocol.
	 */
	protected Collection coordinatorCollection = new LinkedHashSet();
	
	/**
	 * Collection of CollectionProtocolEvents associated with the CollectionProtocol.
	 */
	protected Collection collectionProtocolEventCollection = new LinkedHashSet();
	
	/**
	 * whether Aliquote in same container
	 */
	protected Boolean aliquotInSameContainer;
	
	//protected Collection storageContainerCollection=new HashSet();
	
	//-----For Consent Tracking. Ashish 22/11/06
	/**
	 * The collection of consent tiers associated with the collection protocol.
	 */
	protected Collection consentTierCollection;
	/**
	 * The unsigned document URL for the collection protocol.
	 */
	protected String unsignedConsentDocumentURL;
	
	/**
	 * whether consents are waived?
	 */
	protected Boolean consentsWaived = new Boolean(false);
	
	/**
     * A collection of registration of a Participant to a Collection Protocol. 
     * */
	protected Collection collectionProtocolRegistrationCollection = new HashSet();
	/**
	 * @return the unsignedConsentDocumentURL
	 * @hibernate.property name="unsignedConsentDocumentURL" type="string" length="1000" column="UNSIGNED_CONSENT_DOC_URL"
	 */
	public String getUnsignedConsentDocumentURL()
	{
		return unsignedConsentDocumentURL;
	}

	
	/**
	 * @param unsignedConsentDocumentURL the unsignedConsentDocumentURL to set
	 */
	public void setUnsignedConsentDocumentURL(String unsignedConsentDocumentURL)
	{
		this.unsignedConsentDocumentURL = unsignedConsentDocumentURL;
	}

	/**
	 * @return the consentTierCollection
	 * @hibernate.collection-one-to-many class="edu.wustl.catissuecore.domain.ConsentTier" cascade="save-update" lazy="true"
	 * @hibernate.set table="CATISSUE_CONSENT_TIER" inverse="false" name="consentTierCollection"
	 * @hibernate.collection-key column="COLL_PROTOCOL_ID"
	 */
	public Collection getConsentTierCollection()
	{
		return consentTierCollection;
	}
	
	/**
	 * @param consentTierCollection the consentTierCollection to set
	 */
	public void setConsentTierCollection(Collection consentTierCollection)
	{
		this.consentTierCollection = consentTierCollection;
	}
	//-----Consent Tracking End
	
	/**
	 * NOTE: Do not delete this constructor. Hibernet uses this by reflection API.
	 * */
	public CollectionProtocol()
	{
		super();
	}
	
	/**
	 *
	 * @param form This is abstract action form
	 */
	public CollectionProtocol(AbstractActionForm form)
	{
		setAllValues(form);
	}
	
	/**
	 * Returns the collection of Studies for this Protocol.
	 * @hibernate.set name="distributionProtocolCollection" table="CATISSUE_COLL_DISTRIBUTION_REL" 
	 * cascade="save-update" inverse="false" lazy="false"
	 * @hibernate.collection-key column="COLLECTION_PROTOCOL_ID"
	 * @hibernate.collection-many-to-many class="edu.wustl.catissuecore.domain.DistributionProtocol" column="DISTRIBUTION_PROTOCOL_ID"
	 * @return Returns the collection of Studies for this Protocol.
	 */
	public Collection getDistributionProtocolCollection()
	{
		return distributionProtocolCollection;
	}

	/**
	 * @param studyCollection The studyCollection to set.
	 */
	public void setDistributionProtocolCollection(Collection distributionProtocolCollection)
	{
		this.distributionProtocolCollection = distributionProtocolCollection;
	}

	/**
	 * Returns the collection of Users(ProtocolCoordinators) for this Protocol.
	 * @hibernate.set name="coordinatorCollection" table="CATISSUE_COLL_COORDINATORS" 
	 * cascade="none" inverse="false" lazy="false"
	 * @hibernate.collection-key column="COLLECTION_PROTOCOL_ID"
	 * @hibernate.collection-many-to-many class="edu.wustl.catissuecore.domain.User" column="USER_ID"
	 * @return The collection of Users(ProtocolCoordinators) for this Protocol.
	 */
	public Collection getCoordinatorCollection()
	{
		return coordinatorCollection;
	}

	/**
	 * @param coordinatorCollection The coordinatorCollection to set.
	 */
	public void setCoordinatorCollection(Collection coordinatorCollection)
	{
		this.coordinatorCollection = coordinatorCollection;
	}


	/**
	 * Returns the collection of CollectionProtocolEvents for this Protocol.
	 * @hibernate.set name="collectionProtocolEventCollection"
	 * table="CATISSUE_COLL_PROT_EVENT" cascade="save-update"
	 * inverse="true" lazy="false"
	 * @hibernate.collection-key column="COLLECTION_PROTOCOL_ID"
	 * @hibernate.collection-one-to-many class="edu.wustl.catissuecore.domain.CollectionProtocolEvent"
	 * @return The collection of CollectionProtocolEvents for this Protocol.
	 */
	public Collection getCollectionProtocolEventCollection()
	{
		return collectionProtocolEventCollection;
	}

	/**
	 * @param collectionProtocolEventCollection
	 * The collectionProtocolEventCollection to set.
	 */
	public void setCollectionProtocolEventCollection(Collection collectionProtocolEventCollection)
	{
		this.collectionProtocolEventCollection = collectionProtocolEventCollection;
	}
	
	/**
	 * Returns collection of collection protocol registrations of this collection protocol.
	 * @return collection of collection protocol registrations of this collection protocol.
	 * @hibernate.set name="collectionProtocolRegistrationCollection" table="CATISSUE_COLL_PROT_REG"
	 * @hibernate.collection-key column="COLLECTION_PROTOCOL_ID"
	 * @hibernate.collection-one-to-many class="edu.wustl.catissuecore.domain.CollectionProtocolRegistration"
	 * @see setCollectionProtocolRegistrationCollection(Collection)
	 */
	public Collection getCollectionProtocolRegistrationCollection()
	{

		return collectionProtocolRegistrationCollection;
	}

	/**
	 * Sets the collection protocol registrations of this participant.
	 * @param protocolRegistrationCollection collection of collection protocol registrations of this participant.
	 * @see #getCollectionProtocolRegistrationCollection()
	 */
	public void setCollectionProtocolRegistrationCollection(Collection collectionProtocolRegistrationCollection)
	{
		this.collectionProtocolRegistrationCollection = collectionProtocolRegistrationCollection;
	}
	/*
	 * Returns the collection of Containers for this Protocol.
	 * @hibernate.set name="storageContainerCollection" table="CATISSUE_CONTAINER_CP_REL" 
	 * cascade="none" inverse="false" lazy="false"
	 * @hibernate.collection-key column="COLLECTION_PROTOCOL_ID"
	 * @hibernate.collection-many-to-many class="edu.wustl.catissuecore.domain.StorageContainer" column="STORAGE_CONTAINER_ID"
	 * @return The collection of Storage Containers for this Protocol.*/
	 /*
	public Collection getStorageContainerCollection()
	{
		return storageContainerCollection;
	}
*/
	/*
	 * @param storageContainerCollection The storageContainerCollection to set.
	 */
	/*public void setStorageContainerCollection(Collection storageContainerCollection)
	{
		this.storageContainerCollection = storageContainerCollection;
	}*/

	   /**
     * This function Copies the data from an CollectionProtocolForm object to a CollectionProtocol object.
     * @param abstractForm An CollectionProtocolForm object containing the information about the CollectionProtocol.  
     * */
    public void setAllValues(IValueObject abstractForm)
    {
        try
        {
        	super.setAllValues(abstractForm);
        	
        	CollectionProtocolForm cpForm = (CollectionProtocolForm) abstractForm;
        	
        	coordinatorCollection.clear();
        	this.collectionProtocolEventCollection.clear();
        	long [] coordinatorsArr = cpForm.getProtocolCoordinatorIds();
        	if(coordinatorsArr!=null)
        	{
	        	for (int i = 0; i < coordinatorsArr.length; i++)
				{
	        		if(coordinatorsArr[i]!=-1)
	        		{
		        		User coordinator = new User();
		        		coordinator.setId(new Long(coordinatorsArr[i]));
		        		coordinatorCollection.add(coordinator);
	        		}
				}
        	}
        	aliquotInSameContainer = new Boolean(cpForm.isAliqoutInSameContainer());
	        Map map = cpForm.getValues();
	        
	        /**
	         * Name : Abhishek Mehta
	         * Reviewer Name : Poornima
	         * Bug ID: Collection_Event_Protocol_Order
	         * Patch ID: Collection_Event_Protocol_Order_1 
	         * See also: 1-8
	         * Description: To get the collection event protocols in their insertion order. 
	         */
	        Logger.out.debug("PRE FIX MAP "+map);
	        Map sortedMap = sortMapOnKey(map);
	        Logger.out.debug("POST FIX MAP "+map);
	        
	        MapDataParser parser = new MapDataParser("edu.wustl.catissuecore.domain");
	        
	        ArrayList cpecList = (ArrayList)parser.generateData(sortedMap,true);
	        for(int i = 0 ; i < cpecList.size() ; i++)
	        {
	        	this.collectionProtocolEventCollection.add(cpecList.get(i));
	        }
	        Logger.out.debug("collectionProtocolEventCollection "+this.collectionProtocolEventCollection);
	        
	        //For Consent Tracking ----Ashish 1/12/06
	        //Setting the unsigned doc url.
	        this.unsignedConsentDocumentURL = cpForm.getUnsignedConsentURLName();
	        //Setting the consent tier collection.
	        this.consentTierCollection = prepareConsentTierCollection(cpForm.getConsentValues());
	        
	        //Mandar : 25-Jan-07
	        consentsWaived = new Boolean(cpForm.isConsentWaived());
        }
        catch (Exception excp)
        {
	    	// use of logger as per bug 79
	    	Logger.out.error(excp.getMessage(),excp); 
        }
    }
    				
/**
     * Patch ID: Collection_Event_Protocol_Order_2 
     * Description: To get the collection event protocols in their insertion order. 
     */

	/**
	 * This function will sort the map based on their keys.
	 */
    private LinkedHashMap sortMapOnKey(Map map)
    {
    	Object[] mapKeySet = map.keySet().toArray();
    	int size = mapKeySet.length;
    	ArrayList <String> mList = new ArrayList <String>();
    	for(int i = 0 ; i < size ; i++)
		{
    		String key = (String)mapKeySet[i];
    		mList.add(key);
		}
    	
    	KeyComparator keyComparator = new KeyComparator();
		Collections.sort(mList, keyComparator);
		
    	LinkedHashMap <String, String> sortedMap = new LinkedHashMap<String, String>();
		for(int i = 0 ; i < size ; i++)
		{
			String key = (String)mList.get(i);
			String value = (String)map.get(key);
			sortedMap.put(key, value);
		}
		return sortedMap;
    }
   
    /**
     * @param consentTierMap Consent Tier Map
     * @return consentStatementColl
     * @throws Exception - Exception 
     */
    public Collection prepareConsentTierCollection(Map consentTierMap) throws Exception 
    {
    	MapDataParser mapdataParser = new MapDataParser("edu.wustl.catissuecore.bean");
    	Collection beanObjColl = mapdataParser.generateData(consentTierMap);
    	
    	Collection<ConsentTier> consentStatementColl = new HashSet<ConsentTier>();
    	Iterator iter = beanObjColl.iterator();        
    	while(iter.hasNext())
    	{
    		ConsentBean consentBean = (ConsentBean)iter.next();
    		ConsentTier consentTier = new ConsentTier();
    		consentTier.setStatement(consentBean.getStatement());
    		//To set ID for Edit case
    		if(consentBean.getConsentTierID()!=null&&consentBean.getConsentTierID().trim().length()>0)
    		{
    			consentTier.setId(Long.parseLong(consentBean.getConsentTierID()));
    		}
    		//Check for empty consents
    		if(consentBean.getStatement().trim().length()>0)
    		{
    			consentStatementColl.add(consentTier);
    		}
    	}	
    	return consentStatementColl;
    }
    
    /**
     * Returns message label to display on success add or edit
     * @return String
     */
	public String getMessageLabel()
	{		
		return this.title;
	}
	/**
	 * @param object For equalizing
	 * @return boolean
	 */
	public boolean equals(Object object)
    {
		
    	
    	if(this.getClass().getName().equals(object.getClass().getName()))
    	{
    		CollectionProtocol collectionProtocol = (CollectionProtocol)object;
    		if(this.getId().longValue() == collectionProtocol.getId().longValue())
    		{
    			return true;
    	}
    	}
    	return false;
    }
	/**
	 * 
	 * @param object For comparing 
	 * @return int
	 */
	public int compareTo(Object object)
	{
		
    	if(this.getClass().getName().equals(object.getClass().getName()))
    	{
    		CollectionProtocol collectionProtocol = (CollectionProtocol)object;
    		return this.getId().compareTo(collectionProtocol.getId());
    	}
    	return 0;
	}
	/**
	 * Method overridden to return hashcode of Id if available.
	 * @return hashcode
	 */
	public int hashCode()
	{
		if(this.getId() != null)
		{
			return this.getId().hashCode();
		}
		return super.hashCode();
	}


	/**
	 * @return Returns the aliquotInSameContainer.
	 * @hibernate.property name="aliquotInSameContainer" type="boolean" column="ALIQUOT_IN_SAME_CONTAINER"
	 */
	public Boolean getAliquotInSameContainer() 
	{
		return aliquotInSameContainer;
	}

	/**
	 * @param aliquotInSameContainer The aliquotInSameContainer to set.
	 */
	public void setAliquotInSameContainer(Boolean aliquotInSameContainer) 
	{
		this.aliquotInSameContainer = aliquotInSameContainer;
	}

	//-Mandar : 25-Jan-07 ---------- start
	/**
	 * @return Returns the consentsWaived.
	 * @hibernate.property name="consentsWaived" type="boolean" column="CONSENTS_WAIVED"
	 */
	public Boolean getConsentsWaived() 
	{
		return consentsWaived;
	}
	/**
	 * Sets the consent waived value.
	 * @param consentsWaived Value to be set to the consentsWaived attribute.
	 */
	public void setConsentsWaived(Boolean consentsWaived)
	{
		this.consentsWaived = consentsWaived;
	}
	//-Mandar : 25-Jan-07 ---------- end


}