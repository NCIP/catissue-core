/**
 * <p>Title: NewSpecimenArrayOrderItem Class>
 * <p>Description:   Class for NewSpecimenArrayOrderItem.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Ashish Gupta
 * @version 1.00
 * Created on November 09,2006
 */

package edu.wustl.catissuecore.domain;

import java.util.Collection;

/**
 * This is the class indicating new biospecimen array order items.
 * @hibernate.joined-subclass table="CATISSUE_NEW_SP_AR_ORDER_ITEM"
 * @hibernate.joined-subclass-key column="IDENTIFIER"
 */
public class NewSpecimenArrayOrderItem extends SpecimenArrayOrderItem
{

	/**
	 * Serial Version ID.
	 */
	private static final long serialVersionUID = -2718441983161014940L;
	/**
	 * The Specimen Array Name.
	 */
	protected String name;
	/**
	 * The Specimen Array type of the new specimen array ordered.
	 */
	protected SpecimenArrayType specimenArrayType;
	/**
	 * The specimens associated with the Array.
	 */
	protected Collection specimenOrderItemCollection;
	/**
	 * The Specimen Array.
	 */
	protected SpecimenArray specimenArray;

	/**
	 * The specimen array associated with the order item in SpecimenArrayOrderItem.
	 * @hibernate.many-to-one column="SPECIMEN_ARRAY_ID" class="edu.wustl.catissuecore.domain.SpecimenArray"
	 * constrained="true"
	 * @return the specimen array.
	 */
	public SpecimenArray getSpecimenArray()
	{
		return specimenArray;
	}

	/**
	 * @param specimenArray the specimenArray to set
	 */
	public void setSpecimenArray(SpecimenArray specimenArray)
	{
		this.specimenArray = specimenArray;
	}

	/**
	 * @hibernate.many-to-one class="edu.wustl.catissuecore.domain.SpecimenArrayType"
	 * column="ARRAY_TYPE_ID" constrained="true"
	 * @return SpecimenArrayType.
	 */
	public SpecimenArrayType getSpecimenArrayType()
	{
		return specimenArrayType;
	}

	/**
	 * @param specimenArrayType the arrayType to set
	 */
	public void setSpecimenArrayType(SpecimenArrayType specimenArrayType)
	{
		this.specimenArrayType = specimenArrayType;
	}

	/**
	 * @hibernate.property column="NAME" type="string" length="100" name="name"
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * @hibernate.collection-one-to-many class="edu.wustl.catissuecore.domain.
	 * SpecimenOrderItem" cascade="save-update" lazy="false"
	 * @hibernate.set name="specimenOrderItemCollection" table="CATISSUE_SPECIMEN_ORDER_ITEM"
	 * @hibernate.collection-key column="ARRAY_ORDER_ITEM_ID"
	 * @return the specimenOrderItemCollection
	 */
	public Collection getSpecimenOrderItemCollection()
	{
		return specimenOrderItemCollection;
	}

	/**
	 * @param specimenOrderItemCollection the specimenOrderItemCollection to set
	 */
	public void setSpecimenOrderItemCollection(Collection specimenOrderItemCollection)
	{
		this.specimenOrderItemCollection = specimenOrderItemCollection;
	}
}