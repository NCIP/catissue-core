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

import edu.wustl.catissuecore.actionForm.AbstractActionForm;
import edu.wustl.catissuecore.actionForm.DistributionProtocolForm;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.common.util.MapDataParser;
import edu.wustl.common.util.logger.Logger;

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
	protected Collection specimenRequirementCollection = new HashSet();
	
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
	 * @hibernate.set name="specimenRequirementCollection" table="CATISSUE_DISTRIBUTION_SPECIMEN_REQUIREMENT" 
	 * cascade="save-update" inverse="false" lazy="false"
	 * @hibernate.collection-key column="DISTRIBUTION_PROTOCOL_ID"
	 * @hibernate.collection-many-to-many class="edu.wustl.catissuecore.domain.SpecimenRequirement" column="SPECIMEN_REQUIREMENT_ID"
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
	
	   /**
     * This function Copies the data from an CollectionProtocolForm object to a CollectionProtocol object.
     * @param CollectionProtocol An CollectionProtocolForm object containing the information about the CollectionProtocol.  
     * */
    public void setAllValues(AbstractActionForm abstractForm)
    {
        try
        {
        	super.setAllValues(abstractForm);
        	
	    	DistributionProtocolForm form = (DistributionProtocolForm) abstractForm;
	    	
	    	this.systemIdentifier = new Long(form.getSystemIdentifier());
	    	this.activityStatus = form.getActivityStatus();
	    	this.principalInvestigator.setSystemIdentifier(new Long(form.getPrincipalInvestigatorId()));
	    	this.title = form.getTitle();
	    	this.shortTitle = form.getShortTitle();
	    	if(form.getStartDate() != null)
	    		this.startDate = Utility.parseDate(form.getStartDate(),Constants.DATE_PATTERN_MM_DD_YYYY);
	    	if(form.getEndDate() != null)
	    		this.endDate = Utility.parseDate(form.getEndDate(),Constants.DATE_PATTERN_MM_DD_YYYY);
	    	this.irbIdentifier = form.getIrbID();
	    	this.enrollment = new Integer(form.getEnrollment());
	    	this.descriptionURL = form.getDescriptionURL();
	    	
	    	Map map = form.getValues();
	        map = fixMap(map);
	        Logger.out.debug("MAP "+map);
	        MapDataParser parser = new MapDataParser("edu.wustl.catissuecore.domain");
	        this.specimenRequirementCollection = new HashSet(parser.generateData(map));
	        Logger.out.debug("specimenRequirementCollection "+this.specimenRequirementCollection);
        }
        catch (Exception excp)
        {
        	Logger.out.error(excp.getMessage(),excp);
        }
    }
    
    public String toString()
    {
    	return title+" "+specimenRequirementCollection ;
    }
}