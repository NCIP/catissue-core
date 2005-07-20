/**
 * <p>Title: FluidSpecimenReviewEventParameters Class
 * <p>Description:  Attributes associated with a review event of a fluid specimen. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00
 * Created on Apr 7, 2005
 */
package edu.wustl.catissuecore.domain;

/**
 * Attributes associated with a review event of a fluid specimen.
 * @hibernate.joined-subclass table="CATISSUE_FLUID_SPECIMEN_REVIEW_EVENT_PARAMETERS"
 * @hibernate.joined-subclass-key column="IDENTIFIER" 
 * @author Aniruddha Phadnis
 */
public class FluidSpecimenReviewEventParameters extends ReviewEventParameters
		implements
			java.io.Serializable
{

	private static final long serialVersionUID = 1234567890L;

	/**
     * Cell Count.
     */
	protected Double cellCount;

	/**
     * Returns the cell count. 
     * @return The cell count.
     * @see #setCellCount(Double)
     * @hibernate.property name="cellCount" type="double" 
     * column="CELL_COUNT" length="30"
     */
	public Double getCellCount()
	{
		return cellCount;
	}

	/**
     * Sets the cell count.
     * @param cellCount the cell count.
     * @see #getCellCount()
     */
	public void setCellCount(Double cellCount)
	{
		this.cellCount = cellCount;
	}

}