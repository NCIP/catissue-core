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

import edu.wustl.catissuecore.actionForm.FluidSpecimenReviewEventParametersForm;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.util.logger.Logger;

/**
 * Attributes associated with a review event of a fluid specimen.
 * @hibernate.joined-subclass table="CATISSUE_FLUID_SPE_EVENT_PARAM"
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

	/**
	 * NOTE: Do not delete this constructor. Hibernet uses this by reflection API.
	 * */
	public FluidSpecimenReviewEventParameters()
	{
		
	}

	/**
	 *  Parameterised constructor 
	 * @param abstractForm
	 */
	public FluidSpecimenReviewEventParameters(AbstractActionForm abstractForm)
	{
		setAllValues(abstractForm);
	}
	
	/**
     * This function Copies the data from an FluidSpecimenReviewEventParametersForm object to a FluidSpecimenReviewEventParameters object.
     * @param fluidSpecimenReviewEventParametersForm An FluidSpecimenReviewEventParametersForm object containing the information about the fluidSpecimenReviewEventParameters.  
     * */
    public void setAllValues(AbstractActionForm abstractForm)
    {
        try
        {
        	FluidSpecimenReviewEventParametersForm form = (FluidSpecimenReviewEventParametersForm) abstractForm;
        	Logger.out.debug("############DomainObject################## : ");
        	Logger.out.debug(form.getCellCount());
        	Logger.out.debug("############################## ");
           	if(form.getCellCount() != null && form.getCellCount().trim().length()>0 )
           		this.cellCount = new Double(form.getCellCount()); 
           	super.setAllValues(form);
        }
        catch (Exception excp)
        {
            Logger.out.error(excp.getMessage());
        }
    }
	
}