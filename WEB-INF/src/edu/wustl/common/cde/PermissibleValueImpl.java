/**
 * <p>Title: PermissibleValueImpl Class
 * <p>Description: This class provides the implementation of the PermissibleValue interface.</p> 
 *<p>It also provides methods to set the values of the PermissibleValue and obtain the inforamtion about the PermissibleValue.</p> 
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Mandar Deshmukh
 * @version 1.00
 * Created on May 26, 2005
 */

package edu.wustl.common.cde;

import java.util.Set;

/**
 * @author mandar_deshmukh
 * 
 * <p>
 * Description: This class provides the implementation of the PermissibleValue
 * interface.
 * </p>
 * <p>
 * It also provides methods to set the values of the PermissibleValue and obtain
 * the inforamtion about the PermissibleValue.
 * </p>
 * @hibernate.class table="CATISSUE_PERMISSIBLE_VALUE"
 */

public class PermissibleValueImpl implements PermissibleValue
{

    /**
     * identifier is a unique id assigned to each PermissibleValue.
     */
    private String identifier;

    /**
     * value is the value of the PermissibleValue object.
     */
    private String value;

    /**
     * defination is the defination of the PermissibleValue.
     */
    private String defination;

    /**
     * evscode is the evs_code of the PermissibleValue.
     */
    private String evscode;

    /**
     * conceptid is the concept code of the PermissibleValue.
     */
    private String conceptid;

    /**
     * subpermissiblevalues is the set of all available sub permissible values
     * of the PermissibleValue.
     */
    private Set subPermissibleValues;

    /**
     * parentPermissibleValue is the PermissibleValue of the Parent of the
     * PermissibleValue.
     */
    private PermissibleValue parentPermissibleValue;

    private CDE cde = new CDEImpl();

    /**
     * getIdentifier method returns the unique id associated with the
     * PermissibleValue
     * @return Returns the identifier.
     * @hibernate.id name="identifier" column="IDENTIFIER" type="string"
     * length="30" unsaved-value="null" generator-class="assigned"
     */
    public String getIdentifier()
    {
        return identifier;
    }

    /**
     * setIdentifier method sets the unique id of the PermissibleValue
     * 
     * @param identifier The identifier to set.
     */
    public void setIdentifier(String identifier)
    {
        this.identifier = identifier;
    }

    /**
     * getConceptid method returns the concept id associated with the
     * PermissibleValue
     * 
     * @return Returns the conceptid.
     * 
     * @hibernate.property name="conceptid" type="string" column="CONCEPT_CODE" length="20"
     */
    public String getConceptid()
    {
        return conceptid;
    }

    /**
     * setConceptid method sets the concept id of the PermissibleValue
     * 
     * @param conceptid
     *            The conceptid to set.
     */
    public void setConceptid(String conceptid)
    {
        this.conceptid = conceptid;
    }

    /**
     * getDefiantion method returns the defination of the PermissibleValue
     * 
     * @return Returns the defination.
     * @hibernate.property name="defination" type="string" column="DEFINITION" length="500"
     */
    public String getDefination()
    {
        return defination;
    }

    /**
     * setDefination method sets the defination of the PermissibleValue
     * 
     * @param defination
     *            The defination to set.
     */
    public void setDefination(String defination)
    {
        this.defination = defination;
    }

    /**
     * getEvscode method returns the evs code associated with the
     * PermissibleValue
     * 
     * @return Returns the evscode.
     * @hibernate.property name="evscode" type="string" column="EVS_CODE" length="500"
     */
    public String getEvscode()
    {
        return evscode;
    }

    /**
     * setEvscode method sets the evs code of the Permissible Value.
     * 
     * @param evscode
     *            The evscode to set.
     */
    public void setEvscode(String evscode)
    {
        this.evscode = evscode;
    }

    /**
     * getParentPermissibleValue method returns the Parent PermissibleValue node
     * of the PermissibleValue
     * 
     * @return Returns the parentPermissibleValue.
     * 
     * @hibernate.many-to-one column="PARENT_IDENTIFIER" 
     * class="edu.wustl.common.cde.PermissibleValueImpl" constrained="true"
     */
    public PermissibleValue getParentPermissibleValue()
    {
        return parentPermissibleValue;
    }

    /**
     * setParentPermissibleValue method sets the Parent PermissibleValue node of
     * the PermissibleValue
     * 
     * @param parentPermissibleValue
     *            The parentPermissibleValue to set.
     */
    public void setParentPermissibleValue(
            PermissibleValue parentPermissibleValue)
    {
        this.parentPermissibleValue = parentPermissibleValue;
    }

    /**
     * getSubPermissibleValues method returns a set of all the sub
     * PermissibleValues of the PermissibleValue
     * 
     * @return Returns the subPermissibleValues.
     * 
     * @hibernate.set name="subPermissibleValues" table="CATISSUE_PERMISSIBLE_VALUE"
     * @hibernate.collection-key column="PARENT_IDENTIFIER"
     * @hibernate.collection-one-to-many class="edu.wustl.common.cde.PermissibleValueImpl"
     */
    public Set getSubPermissibleValues()
    {
        return subPermissibleValues;
    }

    /**
     * setSubPermissibleValues method sets all the PermissibleValues of the
     * PermissibleValue
     * 
     * @param subPermissibleValues
     *            The subPermissibleValues to set.
     */
    public void setSubPermissibleValues(Set subPermissibleValues)
    {
        this.subPermissibleValues = subPermissibleValues;
    }

    /**
     * getValue method returns the Value of the PermissibleValue
     * 
     * @return Returns the value.
     * @hibernate.property name="value" type="string" column="VALUE" length="100"
     */
    public String getValue()
    {
        return value;
    }

    /**
     * setValue method sets the Value of the PermissibleValue
     * 
     * @param value
     *            The value to set.
     * 
     */
    public void setValue(String value)
    {
        this.value = value;
    }

    /**
     * @return Returns the cde.
     * @hibernate.many-to-one column="PUBLIC_ID" 
     * class="edu.wustl.common.cde.CDEImpl" constrained="true"
     */
    public CDE getCde()
    {
        return cde;
    }

    /**
     * @param cde
     *            The cde to set.
     */
    public void setCde(CDE cde)
    {
        this.cde = cde;
    }
}
