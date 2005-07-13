/**
 * <p>Title: BiospecimenDistribution Class>
 * <p>Description:  BiospecimenDistribution is a dimension table that lists the material distributed.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 * Created on May 5, 2005
 */


package edu.wustl.catissuecore.domain;

import java.io.Serializable;

import edu.wustl.catissuecore.actionForm.AbstractActionForm;

/**
 * BiospecimenDistribution is a dimension table that lists the material distributed.
 * @hibernate.class table="CATISSUE_BIOSPECIMEN_DIST
 * @author gautam_shetty
 */

public class BiospecimenDistribution extends AbstractDomainObject implements Serializable
{

    private static final long serialVersionUID = 1234567890L;

    /**
     * identifier is a unique id assigned to each biospecimen distribution.
     * */
    protected Long identifier;

    /**
     * Biospecimen Type.
     */
    protected String biospecimenType;

    /**
     * Unique Identifier of Biospecimen.
     */
    protected Long bioSpecimenIdentifier;

    /**
     * Quantity of biospecimen distributed.
     */
    protected Double quantity;

    /**
     * The distribution to which this biospecimen distribution is related.
     */
    private Distribution distribution;

    /**
     * Unit of quantity distributed.
     */
    protected String quanityUnit;

    /**
     * Returns the unique identifier assigned to biospecimen distribution.
     * @hibernate.id name="identifier" column="IDENTIFIER" type="long" length="30"
     * unsaved-value="null" generator-class="native"
     * @return the unique identifier assigned to biospecimen distribution.
     * @see #setIdentifier(int)
     * */
    public Long getIdentifier()
    {
        return identifier;
    }

    /**
     * Sets the unique identifier assigned to biospecimen distribution.
     * @param identifier the unique identifier assigned to biospecimen distribution.
     * @see #getIdentifier()
     */
    public void setIdentifier(Long identifier)
    {
        this.identifier = identifier;
    }

    /**
     * Returns the type of biospecimen.
     * @hibernate.property name="biospecimenType" type="string" column="BIOSPECIMEN_TYPE" length="50"
     * @return the type of biospecimen.
     * @see #setBiospecimenType(String)
     */
    public String getBiospecimenType()
    {
        return biospecimenType;
    }

    /**
     * Sets the type of biospecimen.
     * @param biospecimenType the type of biospecimen.
     * @see #getBiospecimenType()
     */
    public void setBiospecimenType(String biospecimenType)
    {
        this.biospecimenType = biospecimenType;
    }

    /**
     * Returns the unique Identifier of the Biospecimen.
     * @hibernate.property name="bioSpecimenIdentifier" type="long" column="BIOSPECIMEN_IDENTIFIER" length="50"
     * @return the unique Identifier of the Biospecimen.
     * @see #setBioSpecimenIdentifier(Long)
     */
    public Long getBioSpecimenIdentifier()
    {
        return bioSpecimenIdentifier;
    }

    /**
     * Sets the unique Identifier of the Biospecimen.
     * @param bioSpecimenIdentifier the unique Identifier of the Biospecimen.
     * @see #getBioSpecimenIdentifier() 
     */
    public void setBioSpecimenIdentifier(Long bioSpecimenIdentifier)
    {
        this.bioSpecimenIdentifier = bioSpecimenIdentifier;
    }

    /**
     * Returns the quantity of biospecimen distributed. 
     * @hibernate.property name="quantity" type="double" column="QUANTITY" length="50"
     * @return the quantity of biospecimen distributed.
     * @see #setQuantity(Double)
     */
    public Double getQuantity()
    {
        return quantity;
    }

    /**
     * Sets the quantity of biospecimen distributed.
     * @param quantity the quantity of biospecimen distributed.
     * @see #getQuanity()
     */
    public void setQuantity(Double quantity)
    {
        this.quantity = quantity;
    }

    /**
     * Returns the unit of quantity distributed.
     * @hibernate.property name="quanityUnit" type="string" column="QUANITY_UNIT" length="50"
     * @return the unit of quantity distributed.
     * @see #setQuanityUnit(String)
     */
    public String getQuanityUnit()
    {
        return quanityUnit;
    }

    /**
     * Sets the unit of quantity distributed.
     * @param quanityUnit the unit of quantity distributed.
     * @see #getQuanityUnit()
     */
    public void setQuanityUnit(String quanityUnit)
    {
        this.quanityUnit = quanityUnit;
    }

    /**
     * Returns the distribution to which this biospecimen distribution is related.
     * @hibernate.many-to-one column="DISTRIBUTION_ID" 
     * class="edu.wustl.catissuecore.domain.Distribution" constrained="true"
     * @return the distribution to which this biospecimen distribution is related.
     */
    public Distribution getDistribution()
    {
        return distribution;
    }

    /**
     * Sets the distribution to which this biospecimen distribution is related.
     * @param distribution the distribution to which this biospecimen distribution is related.
     * @see #getDistribution()
     */
    public void setDistribution(Distribution distribution)
    {
        this.distribution = distribution;
    }

    /* (non-Javadoc)
     * @see edu.wustl.catissuecore.domain.AbstractDomainObject#setAllValues(edu.wustl.catissuecore.actionForm.AbstractActionForm)
     */
    public void setAllValues(AbstractActionForm abstractForm)
    {

    }
}