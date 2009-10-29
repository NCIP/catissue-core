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
import edu.wustl.catissuecore.util.CollectionProtocolUtil;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.actionForm.IValueObject;
import edu.wustl.common.bizlogic.IActivityStatus;
import edu.wustl.common.exception.AssignDataException;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.util.KeyComparator;
import edu.wustl.common.util.MapDataParser;
import edu.wustl.common.util.logger.Logger;

/**
 * A set of written procedures that describe how a biospecimen is prospectively
 * collected.
 * @hibernate.joined-subclass table="CATISSUE_COLLECTION_PROTOCOL"
 * @hibernate.joined-subclass-key column="IDENTIFIER"
 * @author Mandar Deshmukh
 */
public class CollectionProtocol extends SpecimenProtocol
		implements
			java.io.Serializable,
			Comparable,
			IActivityStatus
{

	/**
	 * logger Logger - Generic logger.
	 */
	private static Logger logger = Logger.getCommonLogger(CollectionProtocol.class);

	/**
	 * Serial Version Id of the class.
	 */
	private static final long serialVersionUID = 1234567890L;

	/**
	 * Patch Id : Collection_Event_Protocol_Order_3 (Changed From HashSet to
	 * LinkedHashSet) Description : To get the specimen requirement in order
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
	 * Collection of CollectionProtocolEvents associated with the
	 * CollectionProtocol.
	 */
	protected Collection collectionProtocolEventCollection = new LinkedHashSet();

	/**
	 * Collection of CollectionProtocol associated with the Parent
	 * CollectionProtocol.
	 */
	protected Collection<CollectionProtocol> childCollectionProtocolCollection = new LinkedHashSet<CollectionProtocol>();

	/**
	 * Parent Collection Protocol.
	 */
	protected CollectionProtocol parentCollectionProtocol;

	/**
	 * Sequence Number.
	 */
	protected Integer sequenceNumber;
	/**
	 * Collection Protocol type - Arm, Cycle, Phase.
	 */
	protected String type;

	/**
	 * Defines the relative time point in days.
	 */
	protected Double studyCalendarEventPoint;
	/**
	 * whether Aliquote in same container.
	 */
	protected Boolean aliquotInSameContainer;

	// protected Collection storageContainerCollection=new HashSet();

	// -----For Consent Tracking. Ashish 22/11/06
	/**
	 * The collection of consent tiers associated with the collection protocol.
	 */
	protected Collection<ConsentTier> consentTierCollection;
	/**
	 * The unsigned document URL for the collection protocol.
	 */
	protected String unsignedConsentDocumentURL;

	/**
	 * whether consents are waived?
	 */
	protected Boolean consentsWaived;

	/**
	 * A collection of registration of a Participant to a Collection Protocol.
	 */
	protected Collection collectionProtocolRegistrationCollection = new HashSet();

	/**
	 * assignedProtocolUserCollection.
	 */
	protected Collection<User> assignedProtocolUserCollection = new HashSet<User>();

	/**
	 * siteCollection.
	 */
	protected Collection<Site> siteCollection = new HashSet<Site>();

	/**
	 * @return the unsignedConsentDocumentURL
	 * @hibernate.property name="unsignedConsentDocumentURL" type="string"
	 *                     length="1000" column="UNSIGNED_CONSENT_DOC_URL"
	 */
	public String getUnsignedConsentDocumentURL()
	{
		return this.unsignedConsentDocumentURL;
	}

	/**
	 * @param unsignedConsentDocumentURL
	 *            the unsignedConsentDocumentURL to set
	 */
	public void setUnsignedConsentDocumentURL(String unsignedConsentDocumentURL)
	{
		this.unsignedConsentDocumentURL = unsignedConsentDocumentURL;
	}

	/**
	 * @return the consentTierCollection
	 * @hibernate.collection-one-to-many
	 *                                   class="edu.wustl.catissuecore.domain.ConsentTier"
	 *                                   cascade="save-update" lazy="true"
	 * @hibernate.set table="CATISSUE_CONSENT_TIER" inverse="false"
	 *                name="consentTierCollection"
	 * @hibernate.collection-key column="COLL_PROTOCOL_ID"
	 */
	public Collection<ConsentTier> getConsentTierCollection()
	{
		return this.consentTierCollection;
	}

	/**
	 * @param consentTierCollection
	 *            the consentTierCollection to set
	 */
	public void setConsentTierCollection(Collection<ConsentTier> consentTierCollection)
	{
		this.consentTierCollection = consentTierCollection;
	}

	// -----Consent Tracking End

	/**
	 * NOTE: Do not delete this constructor. Hibernet uses this by reflection
	 * API.
	 * */
	public CollectionProtocol()
	{
		super();
		// Default Constructor, required for Hibernate
	}

	/**
	 * Parameterized Constructor.
	 * @param form
	 *            This is abstract action form
	 */
	public CollectionProtocol(AbstractActionForm form) throws AssignDataException
	{
		super();
		this.setAllValues(form);
	}

	/**
	 * Returns the collection of Studies for this Protocol.
	 * @hibernate.set name="distributionProtocolCollection"
	 *                table="CATISSUE_COLL_DISTRIBUTION_REL"
	 *                cascade="save-update" inverse="false" lazy="false"
	 * @hibernate.collection-key column="COLLECTION_PROTOCOL_ID"
	 * @hibernate.collection-many-to-many class="edu.wustl.catissuecore.domain.
	 *                                    DistributionProtocol
	 *                                    " column="DISTRIBUTION_PROTOCOL_ID"
	 * @return Returns the collection of Studies for this Protocol.
	 */
	public Collection getDistributionProtocolCollection()
	{
		return this.distributionProtocolCollection;
	}

	/**
	 * @param distributionProtocolCollection
	 *            The studyCollection to set.
	 */
	public void setDistributionProtocolCollection(Collection distributionProtocolCollection)
	{
		this.distributionProtocolCollection = distributionProtocolCollection;
	}

	/**
	 * Returns the collection of Users(ProtocolCoordinators) for this Protocol.
	 * @hibernate.set name="coordinatorCollection"
	 *                table="CATISSUE_COLL_COORDINATORS" cascade="none"
	 *                inverse="false" lazy="false"
	 * @hibernate.collection-key column="COLLECTION_PROTOCOL_ID"
	 * @hibernate.collection-many-to-many
	 *                                    class="edu.wustl.catissuecore.domain.User"
	 *                                    column="USER_ID"
	 * @return The collection of Users(ProtocolCoordinators) for this Protocol.
	 */
	public Collection getCoordinatorCollection()
	{
		return this.coordinatorCollection;
	}

	/**
	 * @param coordinatorCollection
	 *            The coordinatorCollection to set.
	 */
	public void setCoordinatorCollection(Collection coordinatorCollection)
	{
		this.coordinatorCollection = coordinatorCollection;
	}

	/**
	 * Returns the collection of CollectionProtocolEvents for this Protocol.
	 * @hibernate.set name="collectionProtocolEventCollection"
	 *                table="CATISSUE_COLL_PROT_EVENT" cascade="save-update"
	 *                inverse="true" lazy="false"
	 * @hibernate.collection-key column="COLLECTION_PROTOCOL_ID"
	 * @hibernate.collection-one-to-many
	 *                                   class="edu.wustl.catissuecore.domain.CollectionProtocolEvent"
	 * @return The collection of CollectionProtocolEvents for this Protocol.
	 */
	public Collection getCollectionProtocolEventCollection()
	{
		return this.collectionProtocolEventCollection;
	}

	/**
	 * @param collectionProtocolEventCollection
	 *            The collectionProtocolEventCollection to set.
	 */
	public void setCollectionProtocolEventCollection(Collection collectionProtocolEventCollection)
	{
		this.collectionProtocolEventCollection = collectionProtocolEventCollection;
	}

	/**
	 * Returns collection of collection protocol registrations of this
	 * collection protocol.
	 * @return collection of collection protocol registrations of this
	 *         collection protocol.
	 * @hibernate.set name="collectionProtocolRegistrationCollection"
	 *                table="CATISSUE_COLL_PROT_REG"
	 * @hibernate.collection-key column="COLLECTION_PROTOCOL_ID"
	 * @hibernate.collection-one-to-many
	 * class="edu.wustl.catissuecore.domain.CollectionProtocolRegistration"
	 * @see setCollectionProtocolRegistrationCollection(Collection)
	 */
	public Collection getCollectionProtocolRegistrationCollection()
	{

		return this.collectionProtocolRegistrationCollection;
	}

	/**
	 * Sets the collection protocol registrations of this participant.
	 * @param collectionProtocolRegistrationCollection
	 *            collection of collection protocol registrations of this
	 *            participant.
	 * @see #getCollectionProtocolRegistrationCollection()
	 */
	public void setCollectionProtocolRegistrationCollection(
			Collection collectionProtocolRegistrationCollection)
	{
		this.collectionProtocolRegistrationCollection = collectionProtocolRegistrationCollection;
	}

	/*
	 * Returns the collection of Containers for this Protocol.
	 * @hibernate.set name="storageContainerCollection"
	 * table="CATISSUE_CONTAINER_CP_REL" cascade="none" inverse="false"
	 * lazy="false"
	 * @hibernate.collection-key column="COLLECTION_PROTOCOL_ID"
	 * @hibernate.collection-many-to-many class="edu.wustl.catissuecore.domain.
	 * StorageContainer" column="STORAGE_CONTAINER_ID"
	 * @return The collection of Storage Containers for this Protocol.
	 */
	/*
	 * public Collection getStorageContainerCollection() { return
	 * storageContainerCollection; }
	 */
	/*
	 * @param storageContainerCollection The storageContainerCollection to set.
	 */
	/*
	 * public void setStorageContainerCollection(Collection
	 * storageContainerCollection) { this.storageContainerCollection =
	 * storageContainerCollection; }
	 */

	/**
	 * This function Copies the data from an CollectionProtocolForm object to a
	 * CollectionProtocol object.
	 * @param abstractForm An CollectionProtocolForm object containing the information
	 * about the CollectionProtocol.
	 * @throws AssignDataException : AssignDataException
	 */
	@Override
	public void setAllValues(IValueObject abstractForm) throws AssignDataException
	{
		try
		{
			super.setAllValues(abstractForm);

			final CollectionProtocolForm cpForm = (CollectionProtocolForm) abstractForm;

			this.coordinatorCollection.clear();
			this.siteCollection.clear();
			this.collectionProtocolEventCollection.clear();
			final long[] coordinatorsArr = cpForm.getProtocolCoordinatorIds();
			if (coordinatorsArr != null)
			{
				for (final long element : coordinatorsArr)
				{
					if (element != -1)
					{
						final User coordinator = new User();
						coordinator.setId(new Long(element));
						this.coordinatorCollection.add(coordinator);
					}
				}
			}

			final long[] siteArr = cpForm.getSiteIds();
			if (siteArr != null)
			{
				for (final long element : siteArr)
				{
					if (element != -1)
					{
						final Site site = new Site();
						site.setId(new Long(element));
						this.siteCollection.add(site);
					}
				}
			}
			this.aliquotInSameContainer = new Boolean(cpForm.isAliqoutInSameContainer());
			final Map map = cpForm.getValues();

			/**
			 * Name : Abhishek Mehta
			 * Reviewer Name : Poornima
			 * Bug ID: Collection_Event_Protocol_Order
			 * Patch ID: Collection_Event_Protocol_Order_1
			 * See also: 1-8
			 * Description: To get the collection event protocols in their insertion order.
			 */
			logger.debug("PRE FIX MAP " + map);
			final Map sortedMap = this.sortMapOnKey(map);
			logger.debug("POST FIX MAP " + map);

			final MapDataParser parser = new MapDataParser("edu.wustl.catissuecore.domain");

			final ArrayList cpecList = (ArrayList) parser.generateData(sortedMap, true);
			for (int i = 0; i < cpecList.size(); i++)
			{
				this.collectionProtocolEventCollection.add(cpecList.get(i));
			}
			logger.debug("collectionProtocolEventCollection "
					+ this.collectionProtocolEventCollection);

			//Setting the unsigned doc url.
			this.unsignedConsentDocumentURL = cpForm.getUnsignedConsentURLName();
			//Setting the consent tier collection.
			this.consentTierCollection = this.prepareConsentTierCollection(cpForm
					.getConsentValues());

			this.consentsWaived = new Boolean(cpForm.isConsentWaived());
		}
		catch (final Exception excp)
		{
			// use of logger as per bug 79
			CollectionProtocol.logger.error(excp.getMessage(), excp);
			excp.printStackTrace();
			final ErrorKey errorKey = ErrorKey.getErrorKey("assign.data.error");
			throw new AssignDataException(errorKey, null, "CollectionProtocol.java :");
		}
	}

	/**
	 * Patch ID: Collection_Event_Protocol_Order_2 Description: To get the
	 * collection event protocols in their insertion order.
	 */

	/**
	 * This function will sort the map based on their keys.
	 * @param map Map.
	 * @return LinkedHashMap.
	 */
	private Map sortMapOnKey(Map map)
	{
		final Object[] mapKeySet = map.keySet().toArray();
		final int size = mapKeySet.length;
		final ArrayList<String> mList = new ArrayList<String>();
		for (int i = 0; i < size; i++)
		{
			final String key = (String) mapKeySet[i];
			mList.add(key);
		}

		final KeyComparator keyComparator = new KeyComparator();
		Collections.sort(mList, keyComparator);

		final LinkedHashMap<String, String> sortedMap = new LinkedHashMap<String, String>();
		for (int i = 0; i < size; i++)
		{
			final String key = mList.get(i);
			final String value = (String) map.get(key);
			sortedMap.put(key, value);
		}
		return sortedMap;
	}

	/**
	 * @param consentTierMap
	 *            Consent Tier Map
	 * @throws Exception : Exception
	 * @return consentStatementColl
	 */
	public Collection prepareConsentTierCollection(Map consentTierMap) throws Exception
	{
		final MapDataParser mapdataParser = new MapDataParser("edu.wustl.catissuecore.bean");
		final Map consentMap = CollectionProtocolUtil.sortConsentMap(consentTierMap);//bug 8905
		final Collection beanObjColl = mapdataParser.generateData(consentMap);//consentTierMap

		//Collection<ConsentTier> consentStatementColl = new HashSet<ConsentTier>();
		final Collection<ConsentTier> consentStatementColl = new LinkedHashSet<ConsentTier>();//bug 8905
		final Iterator iter = beanObjColl.iterator();
		while (iter.hasNext())
		{
			final ConsentBean consentBean = (ConsentBean) iter.next();
			final ConsentTier consentTier = new ConsentTier();
			consentTier.setStatement(consentBean.getStatement());
			//To set ID for Edit case
			if (consentBean.getConsentTierID() != null
					&& consentBean.getConsentTierID().trim().length() > 0)
			{
				consentTier.setId(Long.parseLong(consentBean.getConsentTierID()));
			}
			//Check for empty consents
			if (consentBean.getStatement() != null
					&& consentBean.getStatement().trim().length() > 0)
			{
				consentStatementColl.add(consentTier);
			}
		}
		return consentStatementColl;
	}

	/**
	 * Returns message label to display on success add or edit.
	 * @return String
	 */
	@Override
	public String getMessageLabel()
	{
		return this.title;
	}

	/**
	 * @param object
	 * For equalizing
	 * @return boolean
	 */
	@Override
	public boolean equals(Object object)
	{
		boolean equals = false;
		if (this.getClass().getName().equals(object.getClass().getName()))
		{
			final CollectionProtocol collectionProtocol = (CollectionProtocol) object;
			if (this.getId().longValue() == collectionProtocol.getId().longValue())
			{
				equals = true;
			}
		}
		return equals;
	}

	/**
	 * @param object
	 *            For comparing
	 * @return int
	 */
	public int compareTo(Object object)
	{
		int result = 0;
		if (this.getClass().getName().equals(object.getClass().getName()))
		{
			final CollectionProtocol collectionProtocol = (CollectionProtocol) object;
			result = this.getId().compareTo(collectionProtocol.getId());
		}
		return result;
	}

	/**
	 * Method overridden to return hashcode of Id if available.
	 * @return hashcode
	 */
	@Override
	public int hashCode()
	{
		int hashCode = super.hashCode();

		if (this.getId() != null)
		{
			hashCode = this.getId().hashCode();
		}
		return hashCode;
	}

	/**
	 * @return Returns the aliquotInSameContainer.
	 * @hibernate.property name="aliquotInSameContainer" type="boolean"
	 *                     column="ALIQUOT_IN_SAME_CONTAINER"
	 */
	public Boolean getAliquotInSameContainer()
	{
		return this.aliquotInSameContainer;
	}

	/**
	 * @param aliquotInSameContainer
	 * The aliquotInSameContainer to set.
	 */
	public void setAliquotInSameContainer(Boolean aliquotInSameContainer)
	{
		this.aliquotInSameContainer = aliquotInSameContainer;
	}

	// -Mandar : 25-Jan-07 ---------- start
	/**
	 * @return Returns the consentsWaived.
	 * @hibernate.property name="consentsWaived" type="boolean"
	 * column="CONSENTS_WAIVED"
	 */
	public Boolean getConsentsWaived()
	{
		return this.consentsWaived;
	}

	/**
	 * Sets the consent waived value.
	 * @param consentsWaived Value to be set to the consentsWaived attribute.
	 */
	public void setConsentsWaived(Boolean consentsWaived)
	{
		this.consentsWaived = consentsWaived;
	}

	/**
	 * Get the type.
	 * @return String type.
	 */
	public String getType()
	{
		return this.type;
	}

	/**
	 * Set the collection type.
	 * @param type of String type.
	 */
	public void setType(String type)
	{
		this.type = type;
	}

	/**
	 * Get the Parent Collection Protocol.
	 * @return CollectionProtocol object.
	 */
	public CollectionProtocol getParentCollectionProtocol()
	{
		return this.parentCollectionProtocol;
	}

	/**
	 * Set the Parent Collection Protocol.
	 * @param parentCollectionProtocol of CollectionProtocol object.
	 */
	public void setParentCollectionProtocol(CollectionProtocol parentCollectionProtocol)
	{
		this.parentCollectionProtocol = parentCollectionProtocol;
	}

	/**
	 * Get the Child Collection Protocol.
	 * @return Collection.
	 */
	public Collection getChildCollectionProtocolCollection()
	{
		return this.childCollectionProtocolCollection;
	}

	/**
	 * Set the child collection protocol.
	 * @param childCollectionProtocolCollection which is a Collection.
	 */
	public void setChildCollectionProtocolCollection(Collection childCollectionProtocolCollection)
	{
		this.childCollectionProtocolCollection = childCollectionProtocolCollection;
	}

	/**
	 * @return Returns the sequenceNumber.
	 */
	public Integer getSequenceNumber()
	{
		return this.sequenceNumber;
	}

	/**
	 * @param sequenceNumber
	 * The sequenceNumber to set.
	 */
	public void setSequenceNumber(Integer sequenceNumber)
	{
		this.sequenceNumber = sequenceNumber;
	}

	/**
	 * @return Returns the studyCalendarEventPoint.
	 */
	public Double getStudyCalendarEventPoint()
	{
		return this.studyCalendarEventPoint;
	}

	/**
	 * @param studyCalendarEventPoint
	 * The studyCalendarEventPoint to set.
	 */
	public void setStudyCalendarEventPoint(Double studyCalendarEventPoint)
	{
		this.studyCalendarEventPoint = studyCalendarEventPoint;
	}

	/**
	 * Returns the collection of Users(ProtocolCoordinators) for this Protocol.
	 * @hibernate.set name="coordinatorCollection"
	 * table="CATISSUE_USER_COLLECTION_PROTOCOLS" cascade="none"
	 * inverse="false" lazy="false"
	 * @hibernate.collection-key column="COLLECTION_PROTOCOL_ID"
	 * @hibernate.collection-many-to-many
	 * class="edu.wustl.catissuecore.domain.User"
	 * column="USER_ID"
	 * @return The collection of Users(ProtocolCoordinators) for this Protocol.
	 */
	public Collection<User> getAssignedProtocolUserCollection()
	{
		return this.assignedProtocolUserCollection;
	}

	/**
	 * Set the assigned protocol to user collection.
	 * @param assignedProtocolUserCollection of User type.
	 */
	public void setAssignedProtocolUserCollection(Collection<User> assignedProtocolUserCollection)
	{
		this.assignedProtocolUserCollection = assignedProtocolUserCollection;
	}

	/**
	 * Returns the collection of Users(ProtocolCoordinators) for this Protocol.
	 * @hibernate.set name="coordinatorCollection"
	 * table="CATISSUE_SITE_COLLECTION_PROTOCOLS" cascade="none"
	 * inverse="false" lazy="false"
	 * @hibernate.collection-key column="COLLECTION_PROTOCOL_ID"
	 * @hibernate.collection-many-to-many
	 * class="edu.wustl.catissuecore.domain.Site"
	 * column="SITE_ID"
	 * @return The collection of sites for this Protocol.
	 */
	public Collection<Site> getSiteCollection()
	{
		return this.siteCollection;
	}

	/**
	 * Sets the site collection.
	 * @param siteCollection of Site type.
	 */
	public void setSiteCollection(Collection<Site> siteCollection)
	{
		this.siteCollection = siteCollection;
	}
}