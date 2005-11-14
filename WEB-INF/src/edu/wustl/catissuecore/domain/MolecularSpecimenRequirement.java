/**
 * <p>Title: MolecularSpecimenRequirement Class</p>
 * <p>Description: Required  attributes for a molecular specimen associated with a Collection or Distribution Protocol.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Mandar Deshmukh
 * @version 1.00
 * Created on July 12, 2005
 */

package edu.wustl.catissuecore.domain;


/**
 * Required  attributes for a molecular specimen associated with a Collection or Distribution Protocol.
 * @hibernate.subclass name="MolecularSpecimenRequirement" discriminator-value="Molecular"
 */
public class MolecularSpecimenRequirement extends SpecimenRequirement
{
	private static final long serialVersionUID = 1234567890L;
	
	/**
	 * Amount of specimen required.
	 */
	protected Double quantityInMicrogram;
	
	/**
	 * Returns the quantity in Micro Gram.
	 * @hibernate.property name="quantityInMicrogram" type="double"
	 * column="QUANTITY" length="50"
	 * @return Returns the quantity in Micro Gram.
	 */
	public Double getQuantityInMicrogram()
	{
		return quantityInMicrogram;
	}

	/**
	 * @param quantityInMicroGram
	 * Quantity to set.
	 */
	public void setQuantityInMicrogram(Double quantityInMicrogram)
	{
		this.quantityInMicrogram = quantityInMicrogram;
	}
}