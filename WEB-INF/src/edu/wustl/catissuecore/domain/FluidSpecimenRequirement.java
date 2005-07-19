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
 * @hibernate.joined-subclass table="CATISSUE_FLUID_SPECIMEN_REQUIREMENT"
 * @hibernate.joined-subclass-key column="IDENTIFIER"
 * @author Mandar Deshmukh
 */
public class FluidSpecimenRequirement extends SpecimenRequirement implements java.io.Serializable
{
	private static final long serialVersionUID = 1234567890L;

	/**
	 * Amount of fluid specimen required.
	 */
	protected Double quantityInMiliLiter;

	/**
	 * Returns the quantity In MiliLiter.
	 * @hibernate.property name="quantityInMiliLiter" type="double"
	 * column="QUANTITY_IN_MILILITER" length="50"
	 * @return Returns the quantity In MiliLiter.
	 */
	public Double getQuantityInMiliLiter()
	{
		return quantityInMiliLiter;
	}

	/**
	 * @param quantityInMiliLiter
	 * Quantity to set.
	 */
	public void setQuantityInMiliLiter(Double quantityInMiliLiter)
	{
		this.quantityInMiliLiter = quantityInMiliLiter;
	}
}