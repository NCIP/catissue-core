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
import java.util.Map;

import edu.wustl.catissuecore.actionForm.DistributionProtocolForm;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.actionForm.IValueObject;
import edu.wustl.common.bizlogic.IActivityStatus;
import edu.wustl.common.util.MapDataParser;
import edu.wustl.common.util.logger.Logger;

/**
 * An abbreviated set of written procedures that describe how a previously collected specimen will be utilized.  Note that specimen may be collected with one collection protocol and then later utilized by multiple different studies (Distribution protocol).
 * @hibernate.joined-subclass table="CATISSUE_DISTRIBUTION_PROTOCOL"
 * @hibernate.joined-subclass-key column="IDENTIFIER" 
 * @author Mandar Deshmukh
 */
public class DistributionProtocol extends SpecimenProtocol implements java.io.Serializable,IActivityStatus
{
	private static final long serialVersionUID = 1234567890L;

	/**
	 * Collection of specimenRequirements associated with the DistributionProtocol.
	 */
	protected Collection<DistributionSpecimenRequirement> distributionSpecimenRequirementCollection = new HashSet<DistributionSpecimenRequirement>();
	
	/**
	 * Collection of protocols(CollectionProtocols) associated with the DistributionProtocol.
	 */
	protected Collection collectionProtocolCollection = new HashSet();
	
	public DistributionProtocol()
	{
	}
	
	public DistributionProtocol(AbstractActionForm form)
	{
		setAllValues(form);
	}
	
	// ---- Method section
	/**
	 * Returns the collection of SpecimenRequirements for this Protocol.
	 * @hibernate.set name="specimenRequirementCollection" table="CATISSUE_DISTRIBUTION_SPEC_REQ" 
	 * cascade="save-update" inverse="false" lazy="false"
	 * @hibernate.collection-key column="DISTRIBUTION_PROTOCOL_ID"
	 * @hibernate.collection-many-to-many class="edu.wustl.catissuecore.domain.SpecimenRequirement" column="DISTRIBUTION_SPECIMEN_REQ_ID"
	 * @return Returns the collection of SpecimenRequirements for this Protocol.
	 */
	public Collection<DistributionSpecimenRequirement> getDistributionSpecimenRequirementCollection()
	{
		return distributionSpecimenRequirementCollection;
	}

	/**
	 * @param specimenRequirementCollection
	 *  The specimenRequirementCollection to set.
	 */
	public void setDistributionSpecimenRequirementCollection(Collection<DistributionSpecimenRequirement> distributionSpecimenRequirementCollection)
	{
		this.distributionSpecimenRequirementCollection = distributionSpecimenRequirementCollection;
	}

	/**
	 * Returns the collection of Collectionprotocols for this DistributionProtocol.
	 * @hibernate.set name="collectionProtocolCollection" table="CATISSUE_COLL_DISTRIBUTION_REL" 
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
	
	   /**
     * This function Copies the data from an CollectionProtocolForm object to a CollectionProtocol object.
     * @param CollectionProtocol An CollectionProtocolForm object containing the information about the CollectionProtocol.  
     * */
    public void setAllValues(IValueObject abstractForm)
    {
        try
        {
        	super.setAllValues(abstractForm);
        	
	    	DistributionProtocolForm dpForm = (DistributionProtocolForm) abstractForm;
	    	
	    	Map map = dpForm.getValues();
	        //map = fixMap(map);
	        Logger.out.debug("MAP "+map);
	        MapDataParser parser = new MapDataParser("edu.wustl.catissuecore.domain");
	        this.distributionSpecimenRequirementCollection = new HashSet(parser.generateData(map));
	        Logger.out.debug("specimenRequirementCollection "+this.distributionSpecimenRequirementCollection);
        }
        catch (Exception excp)
        {
        	Logger.out.error(excp.getMessage(),excp);
        }
    }
    
    public String toString()
    {
    	return title+" "+distributionSpecimenRequirementCollection ;
    }
    
    /**
     * Returns message label to display on success add or edit
     * @return String
     */
	public String getMessageLabel() {		
		return this.title;
	}
}