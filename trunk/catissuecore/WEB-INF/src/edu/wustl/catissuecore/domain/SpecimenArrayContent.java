package edu.wustl.catissuecore.domain;

import edu.wustl.catissuecore.util.SearchUtil;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.AssignDataException;

/**
 * @author gautam_shetty
 * @author Ashwin Gupta 
 * @hibernate.class table="CATISSUE_SPECI_ARRAY_CONTENT"
 */
public class SpecimenArrayContent extends AbstractDomainObject 
{
    
    protected Long id;
    
	protected Double concentrationInMicrogramPerMicroliter;
	
	protected Integer positionDimensionOne;
	
	protected Integer positionDimensionTwo;
	
	//Change for API Search   --- Ashwin 04/10/2006
	protected Quantity initialQuantity;
	
	//Change for API Search   --- Ashwin 04/10/2006
	protected SpecimenArray specimenArray;
	
	//Change for API Search   --- Ashwin 04/10/2006
	protected Specimen specimen;
	
	public SpecimenArrayContent()
	{
	}
	
	/**
     * (non-Javadoc)
     * @see edu.wustl.common.domain.AbstractDomainObject#setId(java.lang.Long)
     */
    public void setId(Long id)
    {
        this.id = id;
    }
    
    /**
     * (non-Javadoc)
     * @see edu.wustl.common.domain.AbstractDomainObject#getId()
     * @hibernate.id name="id" column="IDENTIFIER" type="long" length="30"
     * unsaved-value="null" generator-class="native" 
     * @hibernate.generator-param name="sequence" value="CATISSUE_SPECI_ARRAY_CNTNT_SEQ"
     */
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
        return concentrationInMicrogramPerMicroliter;
    }
    
    /**
     * @param concentrationInMicrogramPerMicroliter The concentrationInMicrogramPerMicroliter to set.
     */
    public void setConcentrationInMicrogramPerMicroliter(Double concentration)
    {
        this.concentrationInMicrogramPerMicroliter = concentration;
    }
    
    /**
     * @return Returns the initialQuantity.
     * @hibernate.many-to-one column="INITIAL_QUANTITY_ID" class="edu.wustl.catissuecore.domain.Quantity"
     * constrained="true"
     */
    public Quantity getInitialQuantity()
    {
        return initialQuantity;
    }
    
    /**
     * @param initialQuantity The initialQuantity to set.
     */
    public void setInitialQuantity(Quantity initialQuantity)
    {
        this.initialQuantity = initialQuantity;
    }
    
    /**
     * @return Returns the positionDimensionOne.
     * @hibernate.property name="positionDimensionOne" type="int" column="POSITION_DIMENSION_ONE" length="30"
     */
    public Integer getPositionDimensionOne()
    {
        return positionDimensionOne;
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
        return positionDimensionTwo;
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
        return specimen;
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
        return specimenArray;
    }
    
    /**
     * @param specimenArray The specimenArray to set.
     */
    public void setSpecimenArray(SpecimenArray specimenArray)
    {
        this.specimenArray = specimenArray;
    }
    
    
    /**
     * (non-Javadoc)
     * @see edu.wustl.common.domain.AbstractDomainObject#setAllValues(edu.wustl.common.actionForm.AbstractActionForm)
     */
    public void setAllValues(AbstractActionForm arg0)
            throws AssignDataException
    {
    	//Change for API Search   --- Ashwin 04/10/2006
    	if (SearchUtil.isNullobject(initialQuantity))
    	{
    		initialQuantity = new Quantity();
    	}
    	
    	//Change for API Search   --- Ashwin 04/10/2006
    	if (SearchUtil.isNullobject(specimenArray))
    	{
    		specimenArray = new SpecimenArray();
    	}
    	
    	//Change for API Search   --- Ashwin 04/10/2006
    	if (SearchUtil.isNullobject(specimen))
    	{
    		 specimen = new Specimen();
    	}
    }
    
}