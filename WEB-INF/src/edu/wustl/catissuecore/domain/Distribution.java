/**
 * <p>Title: Distribution Class>
 * <p>Description:  Models the Distribution information. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 */

package edu.wustl.catissuecore.domain;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import edu.wustl.catissuecore.actionForm.AbstractActionForm;

/**
 * Models the Distribution information.
 * @hibernate.class table="CATISSUE_DISTRIBUTION"
 * @author gautam_shetty
 */
public class Distribution extends AbstractDomainObject implements Serializable
{

    private static final long serialVersionUID = 1234567890L;

    /**
     * identifier is a unique id assigned to each distribution.
     * */
    protected Long identifier;

    /**
     * Date of distribution.
     */
    protected Date distributionDate;

    /**
     * technician who distributed the biospecimen.
     */
    protected User distributedBy;

    /**
     * The tracking number of distribution shipment.
     */
    protected String trackNumber;

    /**
     * The mode through which the biospecimens was distributed(by hand, courier, FedEX, UPS).
     */
    protected String mode;

    /**
     * The site to which the biospecimens are distributed.
     */
    private Site toSite;

    /**
     * The collection of dimensions of the items distributed.
     */
    private Collection itemDistributionCollection = new HashSet();

    /**
     * The study for which this distribution is done.
     */
    private Study study;

    /**
     * The site from which the biospecimens are distributed.
     */
    private Site fromSite;

    /**
     * Returns the unique identifier assigned to distribution.
     * @hibernate.id name="identifier" column="IDENTIFIER" type="long" length="30"
     * unsaved-value="null" generator-class="native"
     * @return returns a unique identifier assigned to the distribution.
     * @see #setIdentifier(int)
     * */
    public Long getIdentifier()
    {
        return identifier;
    }

    /**
     * Sets an identifier for the distribution.
     * @param identifier identifier for the distribution.
     * @see #getIdentifier()
     * */
    public void setIdentifier(Long identifier)
    {
        this.identifier = identifier;
    }

    /**
     * Returns the date of distribution.
     * @hibernate.property name="distributionDate" type="date" column="DISTRIBUTION_DATE" length="50"
     * @return the date of distribution.
     * @see #setDate(Date)
     */
    public Date getDate()
    {
        return distributionDate;
    }

    /**
     * Sets the date of distribution.
     * @param date the date of distribution.
     * @see #getDate()
     */
    public void setDate(Date date)
    {
        this.distributionDate = date;
    }

    /**
     * Returns the technician who disributed the Biospecimen.
     * @hibernate.many-to-one column="DISTRIBUTED_BY" 
     * class="edu.wustl.catissuecore.domain.User" constrained="true"
     * @return the technician who disributed the Biospecimen.
     * @see #setDistributedBy(User)
     */
    public User getDistributedBy()
    {
        return distributedBy;
    }

    /**
     * Sets the technician who disributed the Biospecimen.
     * @param distributedBy the technician who disributed the Biospecimen.
     */
    public void setDistributedBy(User distributedBy)
    {
        this.distributedBy = distributedBy;
    }

    /**
     * Returns the tracking number of distribution shipment.
     * @hibernate.property name="trackNumber" type="string"
     * column="TRACK_NUMBER" length="50"
     * @return the tracking number of distribution shipment.
     * @see #setTrackNumber(String)
     */
    public String getTrackNumber()
    {
        return trackNumber;
    }

    /**
     * Sets the tracking number of distribution shipment.
     * @param trackNumber the tracking number of distribution shipment.
     * @see #getTrackNumber()
     */
    public void setTrackNumber(String trackNumber)
    {
        this.trackNumber = trackNumber;
    }

    /**
     * Returns the mode through which the biospecimens was distributed(by hand, courier, FedEX, UPS).
     * @hibernate.property name="mode" type="string"
     * column="DISTRIBUTION_MODE" length="50"
     * @return the mode through which the biospecimens was distributed(by hand, courier, FedEX, UPS).
     * @see #setMode(String)
     */
    public String getMode()
    {
        return mode;
    }

    /**
     * Sets the mode through which the biospecimens was distributed(by hand, courier, FedEX, UPS).
     * @param mode the mode through which the biospecimens was distributed(by hand, courier, FedEX, UPS).
     * @see #getMode()
     */
    public void setMode(String mode)
    {
        this.mode = mode;
    }

    /**
     * Returns the Site to which the Biospecimens are sent.
     * @hibernate.many-to-one column="TO_SITE_IDENTIFIER" 
     * class="edu.wustl.catissuecore.domain.Site" constrained="true"
     * @return the Site to which the Biospecimens are sent.
     */
    public Site getToSite()
    {

        return toSite;
    }

    /**
     * Sets the Site to which the Biospecimens are sent.
     * @param toSite the Site to which the Biospecimens are sent.
     * @see #getToSite()
     */
    public void setToSite(Site toSite)
    {
        this.toSite = toSite;
    }

    /**
     * Returns the collection of dimensions of the items distributed.
     * @return the collection of dimensions of the items distributed.
     * @hibernate.set name="itemDistributionCollection" table="CATISSUE_BIOSPECIMEN_DISTRIBUTION"
     * cascade="save-update" inverse="true" lazy="false"
     * @hibernate.collection-key column="DISTRIBUTION_ID"
     * @hibernate.collection-one-to-many class="edu.wustl.catissuecore.domain.BiospecimenDistribution"
     * @see #setItemDistributionCollection(Collection)
     */
    public Collection getItemDistributionCollection()
    {
        return itemDistributionCollection;
    }

    /**
     * Sets the collection of dimensions of the items distributed.
     * @param itemDistributionCollection the collection of dimensions of the items distributed.
     * @see #getItemDistributionCollection()
     */
    public void setItemDistributionCollection(
            Collection itemDistributionCollection)
    {
        this.itemDistributionCollection = itemDistributionCollection;
    }

    /**
     * Returns the study for which this distribution is done.
     * @hibernate.many-to-one column="STUDY_IDENTIFIER" 
     * class="edu.wustl.catissuecore.domain.Study" constrained="true"
     * @return the study for which this distribution is done.
     * @see #setStudy(Study)
     */
    public Study getStudy()
    {
        return study;
    }

    /**
     * Sets the study for which this distribution is done.
     * @param study the study for which this distribution is done.
     * @see #getStudy()
     */
    public void setStudy(Study study)
    {
        this.study = study;
    }

    /**
     * Returns the Site from which the Biospecimens are sent.
     * @hibernate.many-to-one column="FROM_SITE_IDENTIFIER" 
     * class="edu.wustl.catissuecore.domain.Site" constrained="true"
     * @return the Site from which the Biospecimens are sent.
     * @see #setFromSite(Site)
     */
    public Site getFromSite()
    {

        return fromSite;
    }

    /**
     * Sets the Site from which the Biospecimens are sent.
     * @param fromSite the Site from which the Biospecimens are sent.
     * @see #getFromSite()
     */
    public void setFromSite(Site fromSite)
    {
        this.fromSite = fromSite;
    }

    /* (non-Javadoc)
     * @see edu.wustl.catissuecore.domain.AbstractDomainObject#setAllValues(edu.wustl.catissuecore.actionForm.AbstractActionForm)
     */
    public void setAllValues(AbstractActionForm abstractForm)
    {

    }
}