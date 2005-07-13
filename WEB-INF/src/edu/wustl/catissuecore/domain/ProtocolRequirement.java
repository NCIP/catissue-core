/**
 * <p>Title: ProtocolRequirement Class>
 * <p>Description:  Models the Protocol Requirement information. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 * Created on May 5, 2005
 */

package edu.wustl.catissuecore.domain;

import java.io.Serializable;

import com.sun.mail.iap.Protocol;

import edu.wustl.catissuecore.actionForm.AbstractActionForm;

/**
 * Models the Protocol Requirement information.
 * @hibernate.class table="CATISSUE_PROTOCOL_REQUIREMENT"
 * @author gautam_shetty
 */
public class ProtocolRequirement extends AbstractDomainObject implements Serializable
{

    private static final long serialVersionUID = 1234567890L;

    /**
	 * identifier is a unique id assigned to each protocol requirement.
	 */
    protected Long identifier;

    /**
     * Specimen type.
     */
    protected String specimenType;

    /**
     * Specimen quantity.
     */
    protected Double quantity;

    /**
     * Unit of specimen quantity.
     */
    protected String quantityUnit;

    /**
     * Disease status.
     */
    protected String diseaseStatus;

    /**
     * The protocol for which this specimen requirements belong.
     */
    private Protocol protocol;

    /**
     * Returns the unique identifier assigned to this protocol requirement.
     * @hibernate.id name="identifier" column="IDENTIFIER" type="long" length="30"
     * unsaved-value="null" generator-class="native"
     * @return the unique identifier assigned to this protocol requirement.
     * @see #setIdentifier(int)
     * */
    public Long getIdentifier()
    {
        return identifier;
    }

    /**
     * Sets the unique identifier assigned to this protocol requirement.
     * @param identifier the unique identifier assigned to this protocol requirement.
     * @see #getIdentifier()
     */
    public void setIdentifier(Long identifier)
    {
        this.identifier = identifier;
    }

    /**
     * Returns the specimen type.
	 * @hibernate.property name="specimenType" type="string" column="SPECIMEN_TYPE" length="50"
     * @return the specimen type.
     * @see #setSpecimenType(String)
     */
    public String getSpecimenType()
    {
        return specimenType;
    }

    /**
     * Sets the specimen type.
     * @param specimenType the specimen type.
     * @see #getSpecimenType()
     */
    public void setSpecimenType(String specimenType)
    {
        this.specimenType = specimenType;
    }

    /**
     * Returns the specimen quantity.
     * @hibernate.property name="quantity" type="double" column="QUANTITY" length="50"
     * @return the specimen quantity.
     * @see #setQuantity(Double)
     */
    public Double getQuantity()
    {
        return quantity;
    }

    /**
     * Sets the specimen quantity.
     * @param quantity the specimen quantity.
     * @see #getQuantity()
     */
    public void setQuantity(Double quantity)
    {
        this.quantity = quantity;
    }

    /**
     * Returns the unit of biospecimen quantity.
     * @hibernate.property name="quantityUnit" type="string"
     * column="QUANTITY_UNIT" length="50"
     * @return the unit of biospecimen quantity.
     * @see #setQuantityUnit(Double)
     */
    public String getQuantityUnit()
    {
        return quantityUnit;
    }

    /**
     * Sets the unit of biospecimen quantity.
     * @param quantityUnit the unit of biospecimen quantity.
     * @see #getQuantityUnit()
     */
    public void setQuantityUnit(String quantityUnit)
    {
        this.quantityUnit = quantityUnit;
    }

    /**
     * Returns the disease status.
     * @hibernate.property name="diseaseStatus" type="string"
     * column="DISEASE_STATUS" length="50"
     * @return the disease status.
     * @see #setDiseaseStatus(Double)
     */
    public String getDiseaseStatus()
    {
        return diseaseStatus;
    }

    /**
     * Sets the disease status.
     * @param diseaseStatus the disease status.
     * @see #getDiseaseStatus()
     */
    public void setDiseaseStatus(String diseaseStatus)
    {
        this.diseaseStatus = diseaseStatus;
    }

    /**
     * Returns the protocol for which this specimen requirements belong.
     * @hibernate.many-to-one column="STUDY_IDENTIFIER" 
     * class="edu.wustl.catissuecore.domain.Study" constrained="true"
     * @return the protocol for which this specimen requirements belong.
     * @see #setProtocol(Protocol)
     */
    public Protocol getProtocol()
    {
        return protocol;
    }

    /**
     * Sets the protocol for which this specimen requirements belong.
     * @param study the protocol for which this specimen requirements belong.
     * @see #getProtocol()
     */
    public void setProtocol(Protocol protocol)
    {
        this.protocol = protocol;
    }

    /* (non-Javadoc)
     * @see edu.wustl.catissuecore.domain.AbstractDomainObject#setAllValues(edu.wustl.catissuecore.actionForm.AbstractActionForm)
     */
    public void setAllValues(AbstractActionForm abstractForm)
    {

    }
}