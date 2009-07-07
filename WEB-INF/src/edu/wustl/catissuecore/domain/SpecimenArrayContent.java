
package edu.wustl.catissuecore.domain;

import edu.wustl.catissuecore.util.SearchUtil;
import edu.wustl.common.actionForm.IValueObject;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.AssignDataException;

/**
 * @author gautam_shetty.
 * @author Ashwin Gupta.
 * @hibernate.class table="CATISSUE_SPECI_ARRAY_CONTENT"
 */
public class SpecimenArrayContent extends AbstractDomainObject
{

	/**
	 * Serial Version ID.
	 */
	private static final long serialVersionUID = 5033957277093287851L;

	/**
	 * System generated id.
	 */
	protected Long id;

	/**
	 * concentrationInMicrogramPerMicroliter.
	 */
	protected Double concentrationInMicrogramPerMicroliter;

	/**
	 * positionDimensionOne.
	 */
	protected Integer positionDimensionOne;

	/**
	 * positionDimensionTwo.
	 */
	protected Integer positionDimensionTwo;

	/**
	 * initialQuantity.
	 */
	protected Double initialQuantity;

	/**
	 * specimenArray.
	 */
	protected SpecimenArray specimenArray;

	/**
	 * specimen.
	 */
	protected Specimen specimen;

	/**
	 * Default Constructor.
	 */
	public SpecimenArrayContent()
	{
		super();
	}

	/**
	 * Set Id.
	 * @param identifier Identifier.
	 * (non-Javadoc)
	 * @see edu.wustl.common.domain.AbstractDomainObject#setId(java.lang.Long)
	 */
	@Override
	public void setId(Long identifier)
	{
		this.id = identifier;
	}

	/**
	 * Set Id.
	 * (non-Javadoc)
	 * @see edu.wustl.common.domain.AbstractDomainObject#getId()
	 * @hibernate.id name="id" column="IDENTIFIER" type="long" length="30"
	 * unsaved-value="null" generator-class="native"
	 * @hibernate.generator-param name="sequence" value="CATISSUE_SPECI_ARRAY_CNTNT_SEQ"
	 * @return Long.
	 */
	@Override
	public Long getId()
	{
		return this.id;
	}

	/**
	 * @return Returns the concentrationInMicrogramPerMicroliter.
	 * @hibernate.property name="concentrationInMicrogramPerMicroliter" type="double"
	 * column="CONC_IN_MICROGM_PER_MICROLTR" length="50"
	 */
	public Double getConcentrationInMicrogramPerMicroliter()
	{
		return this.concentrationInMicrogramPerMicroliter;
	}

	/**
	 * @param concentration - The concentrationInMicrogramPerMicroliter to set.
	 */
	public void setConcentrationInMicrogramPerMicroliter(Double concentration)
	{
		this.concentrationInMicrogramPerMicroliter = concentration;
	}

	/**
	 * @return Returns the initialQuantity.
	 * @hibernate.property name="initialQuantity" type="double"
	 * column="INITIAL_QUANTITY" length="50"
	 */
	public Double getInitialQuantity()
	{
		return this.initialQuantity;
	}

	/**
	 * @param initialQuantity The initialQuantity to set.
	 */
	public void setInitialQuantity(Double initialQuantity)
	{
		this.initialQuantity = initialQuantity;
	}

	/**
	 * @return Returns the positionDimensionOne.
	 * @hibernate.property name="positionDimensionOne" type="int" column="POSITION_DIMENSION_ONE" length="30"
	 */
	public Integer getPositionDimensionOne()
	{
		return this.positionDimensionOne;
	}

	/**
	 * @param positionDimensionOne The positionDimensionOne to set.
	 */
	public void setPositionDimensionOne(Integer positionDimensionOne)
	{
		this.positionDimensionOne = positionDimensionOne;
	}

	/**
	 * @return Returns the positionDimensionTwo.
	 * @hibernate.property name="positionDimensionTwo" type="int" column="POSITION_DIMENSION_TWO" length="30"
	 */
	public Integer getPositionDimensionTwo()
	{
		return this.positionDimensionTwo;
	}

	/**
	 * @param positionDimensionTwo The positionDimensionTwo to set.
	 */
	public void setPositionDimensionTwo(Integer positionDimensionTwo)
	{
		this.positionDimensionTwo = positionDimensionTwo;
	}

	/**
	 * @return Returns the specimen.
	 * @hibernate.many-to-one column="SPECIMEN_ID" class="edu.wustl.catissuecore.domain.Specimen"
	 * constrained="true"
	 */
	public Specimen getSpecimen()
	{
		return this.specimen;
	}

	/**
	 * @param specimen The specimen to set.
	 */
	public void setSpecimen(Specimen specimen)
	{
		this.specimen = specimen;
	}

	/**
	 * @return Returns the specimenArray.
	 * @hibernate.many-to-one column="SPECIMEN_ARRAY_ID" class="edu.wustl.catissuecore.domain.SpecimenArray"
	 * constrained="true"
	 */
	public SpecimenArray getSpecimenArray()
	{
		return this.specimenArray;
	}

	/**
	 * @param specimenArray The specimenArray to set.
	 */
	public void setSpecimenArray(SpecimenArray specimenArray)
	{
		this.specimenArray = specimenArray;
	}

	/**
	 * Set All Values.
	 * (non-Javadoc)
	 * @see edu.wustl.common.domain.AbstractDomainObject#setAllValues(
	 * edu.wustl.common.actionForm.AbstractActionForm)
	 * @param arg0 IValueObject.
	 * @throws AssignDataException AssignDataException.
	 */
	@Override
	public void setAllValues(IValueObject arg0) throws AssignDataException
	{
		//Change for API Search   --- Ashwin 04/10/2006
		if (SearchUtil.isNullobject(this.initialQuantity))
		{
			this.initialQuantity = new Double(0);
		}

		//Change for API Search   --- Ashwin 04/10/2006
		if (SearchUtil.isNullobject(this.specimenArray))
		{
			this.specimenArray = new SpecimenArray();
		}

		//Change for API Search   --- Ashwin 04/10/2006
		if (SearchUtil.isNullobject(this.specimen))
		{
			this.specimen = new Specimen();
		}
	}
}