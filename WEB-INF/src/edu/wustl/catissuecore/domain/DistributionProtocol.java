/**
 * <p>Title: DistributionProtocol Class</p>
 * <p>Description: An abbreviated set of written procedures that describe how a previously collected specimen will be utilized.  Note that specimen may be collected with one collection protocol and then later utilized by multiple different studies (Distribution protocol).</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Mandar Deshmukh
 * @version 1.00
 * Created on July 12, 2005
 */

package edu.wustl.catissuecore.domain;

import java.util.Collection;
import java.util.HashSet;

/**
 * An abbreviated set of written procedures that describe how a previously collected specimen will be utilized.  Note that specimen may be collected with one collection protocol and then later utilized by multiple different studies (Distribution protocol).
 * @hibernate.joined-subclass table="CATISSUE_DISTRIBUTION_PROTOCOL"
 * @hibernate.joined-subclass-key column="IDENTIFIER" 
 * @author Mandar Deshmukh
 */
public class DistributionProtocol extends SpecimenProtocol implements java.io.Serializable
{
	private static final long serialVersionUID = 1234567890L;

	/**
	 * Collection of specimenRequirements associated with the DistributionProtocol.
	 */
	private Collection specimenRequirementCollection = new HashSet();
	
	/**
	 * Collection of protocols(CollectionProtocols) associated with the DistributionProtocol.
	 */
	private Collection collectionProtocolCollection = new HashSet();
	
	// ---- Method section
	/**
	 * Returns the collection of SpecimenRequirements for this Protocol.
	 * @hibernate.set name="specimenRequirementCollection" table="CATISSUE_DISTRIBUTION_SPECIMEN_REQUIREMENT" 
	 * cascade="save-update" inverse="true" lazy="false"
	 * @hibernate.collection-key column="DISTRIBUTION_PROTOCOL_EVENT_ID"
	 * @hibernate.collection-one-to-many class="edu.wustl.catissuecore.domain.SpecimenRequirement" column="SPECIMEN_REQUIREMENT_ID"
	 * @return Returns the collection of SpecimenRequirements for this Protocol.
	 */
	public Collection getSpecimenRequirementCollection()
	{
		return specimenRequirementCollection;
	}

	/**
	 * @param specimenRequirementCollection
	 *  The specimenRequirementCollection to set.
	 */
	public void setSpecimenRequirementCollection(Collection specimenRequirementCollection)
	{
		this.specimenRequirementCollection = specimenRequirementCollection;
	}

	/**
	 * Returns the collection of Collectionprotocols for this DistributionProtocol.
	 * @hibernate.set name="collectionProtocolCollection" table="CATISSUE_COLLECTION_DISTRIBUTION_RELATION" 
	 * cascade="save-update" inverse="true" lazy="false"
	 * @hibernate.collection-key column="DISTRIBUTION_PROTOCOL_ID"
	 * @hibernate.collection-many-to-many class="edu.wustl.catissuecore.domain.CollectionProtocol" column="COLLECTION_PROTOCOL_ID"
	 * @return Returns the collection of Collectionprotocols for this DistributionProtocol.
	 */
	public Collection getCollectionProtocolCollection()
	{
		return collectionProtocolCollection;
	}

	/**
	 * @param collectionProtocolCollection
	 *  The collectionProtocolCollection to set.
	 */
	public void setCollectionProtocolCollection(Collection protocolCollection)
	{
		this.collectionProtocolCollection = protocolCollection;
	}
}