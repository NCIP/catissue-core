/**
 * <p>Title: Order Class>
 * <p>Description:   Class for NewSpecimenOrderItem.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Ashish Gupta
 * @version 1.00
 * Created on October 16,2006
 */

package edu.wustl.catissuecore.domain;

/**
 * This is abstract class indicating the derived specimens from existing ones for the request order
 * * Represents  Pathology Order Item.
 * @hibernate.joined-subclass table="CATISSUE_DERIEVED_SP_ORD_ITEM"
 * @hibernate.joined-subclass-key
 * column="IDENTIFIER"
 *
 * @author ashish_gupta
 */
public class DerivedSpecimenOrderItem extends NewSpecimenOrderItem
{

	/**
	 * Serial Version ID.
	 */
	private static final long serialVersionUID = 6670417163722020858L;
	/**
	 * Specimen associated with the particular order item.
	 */
	protected Specimen parentSpecimen;

	/**
	 * The specimen associated with the order item in SpecimenOrderItem.
	 * @hibernate.many-to-one column="SPECIMEN_ID" class="edu.wustl.catissuecore.domain.Specimen"
	 * constrained="true"
	 * @return the parentSpecimen
	 */
	public Specimen getParentSpecimen()
	{
		return parentSpecimen;
	}

	/**
	 * @param parentSpecimen the specimen to get.
	 * @see #getSpecimen()
	 */
	public void setParentSpecimen(Specimen parentSpecimen)
	{
		this.parentSpecimen = parentSpecimen;
	}
}