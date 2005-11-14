/**
 * <p>Title: CellSpecimenRequirement Class</p>
 * <p>Description: Required attributes for a cell Specimen associated with a Collection or Distribution Protocol.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Mandar Deshmukh
 * @version 1.00
 * Created on July 12, 2005
 */
package edu.wustl.catissuecore.domain;

/**
 * Required attributes for a cell Specimen associated with a Collection or Distribution Protocol.
 * @hibernate.subclass name="CellSpecimenRequirement" discriminator-value = "Cell"
 */
public class CellSpecimenRequirement extends SpecimenRequirement
{
	private static final long serialVersionUID = 1234567890L;

	/**
	 * Absolute number of cells required.
	 */
	protected Integer quantityInCellCount;

	/**
	 * Returns the studyCalendarEventPoint.
	 * @hibernate.property name="quantityInCellCount" type="int"
	 * column="QUANTITY" length="50"
	 * @return Returns the studyCalendarEventPoint.
	 */
	public Integer getQuantityInCellCount()
	{
		return quantityInCellCount;
	}

	/**
	 * @param quantityInCellCount
	 * CellCount Quantity to set.
	 */
	public void setQuantityInCellCount(Integer quantityInCellCount)
	{
		this.quantityInCellCount = quantityInCellCount;
	}
}