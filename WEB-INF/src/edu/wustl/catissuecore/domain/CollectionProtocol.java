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

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import edu.wustl.catissuecore.actionForm.CollectionProtocolForm;
import edu.wustl.catissuecore.bean.ConsentBean;
import edu.wustl.common.actionForm.AbstractActionForm;
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
	 * Collection of users associated with the CollectionProtocol.
	 */
	protected Collection coordinatorCollection = new HashSet();
	
	/**
	 * Collection of CollectionProtocolEvents associated with the CollectionProtocol.
	 */
	protected Collection collectionProtocolEventCollection = new HashSet();
	
	/**
	 * whether Aliquote in same container
	 */
	protected Boolean aliqoutInSameContainer;
	
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
	 * @hibernate.collection-one-to-many class="edu.wustl.catissuecore.domain.ConsentTier" cascade="save-update" lazy="false"
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
	 * Returns the collection of Users(ProtocolCoordinators) for this Protocol.
	 * @hibernate.set name="userCollection" table="CATISSUE_COLL_COORDINATORS" 
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
	 * @param userCollection The userCollection to set.
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
    public void setAllValues(AbstractActionForm abstractForm)
    {
        try
        {
        	super.setAllValues(abstractForm);
        	
        	CollectionProtocolForm cpForm = (CollectionProtocolForm) abstractForm;
        	
        	coordinatorCollection.clear();
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
        	aliqoutInSameContainer = new Boolean(cpForm.isAliqoutInSameContainer());
	        Map map = cpForm.getValues();
	        Logger.out.debug("PRE FIX MAP "+map);
	        //map = fixMap(map);
	        Logger.out.debug("POST FIX MAP "+map);
	        
	        MapDataParser parser = new MapDataParser("edu.wustl.catissuecore.domain");
	        this.collectionProtocolEventCollection = parser.generateData(map);
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
     * @param consentTierMap Consent Tier Map
     * @return consentStatementColl
     * @throws Exception - Exception 
     */
    private Collection prepareConsentTierCollection(Map consentTierMap) throws Exception 
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
	 * @return Returns the aliqoutInSameContainer.
	 * @hibernate.property name="aliqoutInSameContainer" type="boolean" column="ALIQUOT_IN_SAME_CONTAINER"
	 */
	public Boolean getAliqoutInSameContainer() 
	{
		return aliqoutInSameContainer;
	}

	/**
	 * @param aliqoutInSameContainer The aliqoutInSameContainer to set.
	 */
	public void setAliqoutInSameContainer(Boolean aliqoutInSameContainer) 
	{
		this.aliqoutInSameContainer = aliqoutInSameContainer;
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