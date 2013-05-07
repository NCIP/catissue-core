/**
 * <p>Title: ExistingSpecimenArrayOrderItem Class>
 * <p>Description:  Parent Class for ExistingSpecimenArrayOrderItem.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Ashish Gupta
 * @version 1.00
 * Created on November 09,2006
 */

package edu.wustl.catissuecore.domain;

/**
 * This is  the class indicating the existing biospecimens arrays.
 * @hibernate.joined-subclass table="CATISSUE_SP_ARRAY_ORDER_ITEM"
 * @hibernate.joined-subclass-key
 * column="IDENTIFIER"
 */
public class ExistingSpecimenArrayOrderItem extends SpecimenArrayOrderItem
{

	/**
	 * Serial Version ID.
	 */
	private static final long serialVersionUID = 2990852138791973161L;
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
		return this.specimenArray;
	}

	/**
	 * @param specimenArray the specimenArray to set
	 */
	public void setSpecimenArray(SpecimenArray specimenArray)
	{
		this.specimenArray = specimenArray;
	}
}