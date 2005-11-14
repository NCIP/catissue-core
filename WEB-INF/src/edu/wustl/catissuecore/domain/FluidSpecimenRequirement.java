/**
 * <p>Title: FluidSpecimenRequirement Class</p>
 * <p>Description: Required  attributes for a fluid Specimen associated with a Collection or Distribution Protocol.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Mandar Deshmukh
 * @version 1.00
 * Created on July 12, 2005
 */

package edu.wustl.catissuecore.domain;

/**
 * Required  attributes for a fluid Specimen associated with a Collection or Distribution Protocol.
 * @hibernate.subclass name="FluidSpecimenRequirement" discriminator-value="Fluid"
 */ 

public class FluidSpecimenRequirement extends SpecimenRequirement
{
	private static final long serialVersionUID = 1234567890L;

	/**
	 * Amount of fluid specimen required.
	 */
	protected Double quantityInMilliliter;

	/**
	 * Returns the quantity In MiliLiter.
	 * @hibernate.property name="quantityInMilliliter" type="double"
	 * column="QUANTITY" length="50"
	 * @return Returns the quantity In MiliLiter.
	 */
	public Double getQuantityInMilliliter()
	{
		return quantityInMilliliter;
	}

	/**
	 * @param quantityInMiliLiter
	 * Quantity to set.
	 */
	public void setQuantityInMilliliter(Double quantityInMiliLiter)
	{
		this.quantityInMilliliter = quantityInMiliLiter;
	}
}