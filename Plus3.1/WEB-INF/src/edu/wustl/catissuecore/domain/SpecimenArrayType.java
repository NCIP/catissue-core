
package edu.wustl.catissuecore.domain;

import java.util.Collection;
import java.util.HashSet;

import edu.wustl.catissuecore.actionForm.SpecimenArrayTypeForm;
import edu.wustl.catissuecore.util.SearchUtil;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.actionForm.IValueObject;
import edu.wustl.common.exception.AssignDataException;

/**
 * @author gautam_shetty.
 * @author Ashwin Gupta.
 * @hibernate.joined-subclass table="CATISSUE_SPECIMEN_ARRAY_TYPE"
 * @hibernate.joined-subclass-key column="IDENTIFIER"
 */
public class SpecimenArrayType extends ContainerType
{

	/**
	 *
	 */
	private static final long serialVersionUID = -6330322793240274619L;

	/**
	 * specimenClass.
	 */
	protected String specimenClass;

	/**
	 * HashSet of specimenTypeCollection.
	 */
	protected Collection specimenTypeCollection = new HashSet();

	/**
	 * Default Constructor.
	 */
	public SpecimenArrayType()
	{
		super();
	}

	/**
	 * Constructor with action form.
	 * @param actionForm abstract action form.
	 * @throws AssignDataException AssignDataException.
	 */
	public SpecimenArrayType(AbstractActionForm actionForm) throws AssignDataException
	{
		super();
		this.setAllValues(actionForm);
	}

	/**
	 * @return Returns the specimenClass.
	 * @hibernate.property name="specimenClass" type="string" column="SPECIMEN_CLASS" length="50"
	 */
	public String getSpecimenClass()
	{
		return this.specimenClass;
	}

	/**
	 * @param specimenClass The specimenClass to set.
	 */
	public void setSpecimenClass(String specimenClass)
	{
		this.specimenClass = specimenClass;
	}

	//    /**
	//     * @return Returns the holdsSpecimenClassCollection.
	//     * @hibernate.set name="holdsSpecimenClassCollection" table="CATISSUE_SPEC_ARY_TYPE_SPEC_CLASS"
	//	 * cascade="save-update" inverse="false" lazy="false"
	//	 * @hibernate.collection-key column="SPECIMEN_ARRAY_TYPE_ID"
	//	 * @hibernate.element type="string" column="NAME" length="30"
	//     */
	//    public Collection getHoldsSpecimenClassCollection()
	//    {
	//        return holdsSpecimenClassCollection;
	//    }
	//
	//    /**
	//     * @param holdsSpecimenClassCollection The holdsSpecimenClassCollection to set.
	//     */
	//    public void setHoldsSpecimenClassCollection(
	//            Collection holdsSpecimenClassCollection)
	//    {
	//        this.holdsSpecimenClassCollection = holdsSpecimenClassCollection;
	//    }

	/**
	 * @return Returns the specimenClass.
	 * @hibernate.many-to-one column="SPECIMEN_CLASS_ID" class="edu.wustl.
	 * catissuecore.domain.SpecimenClass" constrained="true"
	 */
	//    public SpecimenClass getSpecimenClass()
	//    {
	//        return specimenClass;
	//    }
	//
	/**
	 * @param specimenClass The specimenClass to set.
	 */
	//    public void setSpecimenClass(SpecimenClass specimenClass)
	//    {
	//        this.specimenClass = specimenClass;
	//    }
	/**
	 * @return Returns the specimenTypeCollection.
	 * @hibernate.set name="specimenTypeCollection" table="CATISSUE_SPECIMEN_TYPE"
	 * cascade="save-update" inverse="false" lazy="false"
	 * @hibernate.collection-key column="SPECIMEN_ARRAY_TYPE_ID"
	 * @hibernate.element type="string" column="SPECIMEN_TYPE" length="50"
	 */
	public Collection getSpecimenTypeCollection()
	{
		/*	if (specimenTypeCollection == null)
			{
				specimenTypeCollection = new HashSet();
			}
		*/
		return this.specimenTypeCollection;
	}

	/**
	 * @param specimenTypeCollection The specimenTypeCollection to set.
	 */
	public void setSpecimenTypeCollection(Collection specimenTypeCollection)
	{
		this.specimenTypeCollection = specimenTypeCollection;
	}

	/**
	 * Sets values from actionform to domain object.
	 * @param actionForm action form to be used.
	 * @see edu.wustl.common.domain.AbstractDomainObject#setAllValues(
	 * edu.wustl.common.actionForm.AbstractActionForm)
	 * @throws AssignDataException AssignDataException.
	 */
	@Override
	public void setAllValues(IValueObject actionForm) throws AssignDataException
	{
		final SpecimenArrayTypeForm specimenArrayTypeForm = (SpecimenArrayTypeForm) actionForm;

		if (this.specimenTypeCollection == null)
		{
			this.specimenTypeCollection = new HashSet();
		}

		this.id = Long.valueOf(specimenArrayTypeForm.getId());
		this.name = specimenArrayTypeForm.getName();
		// Change for API Search   --- Ashwin 04/10/2006
		if (SearchUtil.isNullobject(this.capacity))
		{
			this.capacity = new Capacity();
		}

		this.capacity.setOneDimensionCapacity(Integer.valueOf(specimenArrayTypeForm
				.getOneDimensionCapacity()));
		this.capacity.setTwoDimensionCapacity(Integer.valueOf(specimenArrayTypeForm
				.getTwoDimensionCapacity()));
		this.comment = specimenArrayTypeForm.getComment();
		this.specimenClass = specimenArrayTypeForm.getSpecimenClass();
		final String[] specimenTypes = specimenArrayTypeForm.getSpecimenTypes();
		if ((specimenTypes != null) && (specimenTypes.length > 0))
		{
			for (final String specimenType : specimenTypes)
			{
				if (specimenType != null)
				{
					this.specimenTypeCollection.add(specimenType);
				}
			}
		}
		this.activityStatus = "Active";
	}
}