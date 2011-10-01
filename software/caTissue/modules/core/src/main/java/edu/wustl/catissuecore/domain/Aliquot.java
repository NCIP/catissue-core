
package edu.wustl.catissuecore.domain;

import java.io.Serializable;
import java.util.Collection;

import edu.wustl.common.domain.AbstractDomainObject;

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
	private Collection<SpecimenPosition> specimenPositionCollection;
	/**
	 * aliquotsInSameContainer.
	 */
	private Boolean aliquotsInSameContainer;
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
	 * Get Specimen Position Collection.
	 * @return Collection of SpecimenPosition.
	 */
	public Collection<SpecimenPosition> getSpecimenPositionCollection()
	{
		return specimenPositionCollection;
	}
	/**
	 * Set Specimen Position Collection.
	 * @param specimenPositionCollection Collection of SpecimenPosition.
	 */
	public void setSpecimenPositionCollection(Collection<SpecimenPosition> specimenPositionCollection)
	{
		this.specimenPositionCollection = specimenPositionCollection;
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
	 * Get Id.
	 * @return Long Long.
	 */
	@Override
	public Long getId()
	{
		return null;
	}
	@Override
	public void setId(Long arg0)
	{
	}
}