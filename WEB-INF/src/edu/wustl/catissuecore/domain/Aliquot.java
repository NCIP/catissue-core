
package edu.wustl.catissuecore.domain;

import java.io.Serializable;

import edu.wustl.common.actionForm.IValueObject;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.AssignDataException;

/**
 * @author sagar_baldwa
 *
 */
public class Aliquot extends AbstractDomainObject implements Serializable
{

	/**
	 * serialVersionUID.
	 */
	private static final long serialVersionUID = 6305375473974712069L;
	/**
	 * count.
	 */
	private Integer count;
	/**
	 * quantityPerAliquot.
	 */
	private Double quantityPerAliquot;
	/**
	 * specimen.
	 */
	private Specimen specimen;
	/**
	 * specimenPosition.
	 */
	private SpecimenPosition specimenPosition;
	/**
	 * aliquotsInSameContainer.
	 */
	private Boolean aliquotsInSameContainer = true;
	/**
	 * Get Count.
	 * @return Integer Integer.
	 */
	public Integer getCount()
	{
		return count;
	}
	/**
	 * Set Count.
	 * @param count Integer.
	 */
	public void setCount(Integer count)
	{
		this.count = count;
	}
	/**
	 * Get Quantity Per Aliquot.
	 * @return Double Double.
	 */
	public Double getQuantityPerAliquot()
	{
		return quantityPerAliquot;
	}
	/**
	 * Set Quantity Per Aliquot.
	 * @param quantityPerAliquot Double.
	 */
	public void setQuantityPerAliquot(Double quantityPerAliquot)
	{
		this.quantityPerAliquot = quantityPerAliquot;
	}
	/**
	 * Get Quantity Per Aliquot.
	 * @return Boolean Boolean.
	 */
	public Boolean getAliquotsInSameContainer()
	{
		return aliquotsInSameContainer;
	}
	/**
	 * Set Aliquots In Same Container.
	 * @param aliquotsInSameContainer Boolean.
	 */
	public void setAliquotsInSameContainer(Boolean aliquotsInSameContainer)
	{
		this.aliquotsInSameContainer = aliquotsInSameContainer;
	}
	/**
	 * Get Specimen.
	 * @return Specimen Specimen.
	 */
	public Specimen getSpecimen()
	{
		return specimen;
	}
	/**
	 * Set Specimen.
	 * @param specimen Specimen.
	 */
	public void setSpecimen(Specimen specimen)
	{
		this.specimen = specimen;
	}
	/**
	 * Get Specimen Position.
	 * @return SpecimenPosition SpecimenPosition.
	 */
	public SpecimenPosition getSpecimenPosition()
	{
		return specimenPosition;
	}
	/**
	 * Set Specimen Position.
	 * @param specimenPosition SpecimenPosition.
	 */
	public void setSpecimenPosition(SpecimenPosition specimenPosition)
	{
		this.specimenPosition = specimenPosition;
	}
	/**
	 * Get Id.
	 * @return Long Long.
	 */
	@Override
	public Long getId()
	{
		return null;
	}
	/**
	 * Set All Values.
	 * @param arg0 IValueObject.
	 * @throws AssignDataException AssignDataException.
	 */
	@Override
	public void setAllValues(IValueObject arg0) throws AssignDataException
	{
		// TODO Auto-generated method stub
	}
	/**
	 * Set Id.
	 * @param arg0 Long.
	 */
	@Override
	public void setId(Long arg0)
	{
		// TODO Auto-generated method stub
	}
}