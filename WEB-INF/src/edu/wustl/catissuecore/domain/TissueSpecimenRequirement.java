/**
 * <p>Title: TissueSpecimenRequirement Class</p>
 * <p>Description: Required  attributes for a tissue Specimen associated with a Collection or Distribution Protocol.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Mandar Deshmukh
 * @version 1.00
 * Created on July 12, 2005
 */

package edu.wustl.catissuecore.domain;

/**
 * Required  attributes for a tissue Specimen associated with a Collection or Distribution Protocol.
 * @hibernate.subclass name="TissueSpecimenRequirement" discriminator-value="Tissue"
 **/
public class TissueSpecimenRequirement extends SpecimenRequirement
{
	private static final long serialVersionUID = 1234567890L;
	
	/**
	 * Amount of tissue specimen required.
	 */
	protected Double quantityInGram;

	/**
	 * Returns the quantity in grams.
	 * @hibernate.property name="quantityInGram" type="double"
	 * column="QUANTITY" length="50"
	 * @return Returns the quantity in grams.
	 */
	public Double getQuantityInGram()
	{
		return quantityInGram;
	}

	/**
	 * @param quantityInGram
	 * Quantity to set.
	 */
	public void setQuantityInGram(Double quantityInGram)
	{
		this.quantityInGram = quantityInGram;
	}
}