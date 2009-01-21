/**
 * <p>Title: Order Class.</p>
 * <p>Description:   Class for NewSpecimenOrderItem.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Ashish Gupta
 * @version 1.00
 * Created on October 16,2006
 */

package edu.wustl.catissuecore.domain;

/**
 * This class indicates the new specimens associated with the request order.
 * @author ashish_gupta
 */

public class NewSpecimenOrderItem extends SpecimenOrderItem
{
	/**
	 * Serial Version ID.
	 */
	private static final long serialVersionUID = -6534761806407210380L;

	/**
	 * String containing the specimen type of the new/derived specimens.
	 */
	protected String specimenType;

	/**
	 * String containing the specimen class of the new/derived specimens.
	 */
	protected String specimenClass;

	/**
	 * Returns the specimen class of the requested new/derived specimen.
	 * @hibernate.property  name="specimenClass" type="string" length="100" column="SPECIMEN_CLASS"
	 * @return Specimen Class of the new/derived specimen
	 */
	public String getSpecimenClass()
	{
		return specimenClass;
	}

	/**
	 * Sets the specimen class of the requested new/derived specimen.
	 * @param specimenClass String
	 */
	public void setSpecimenClass(String specimenClass)
	{
		this.specimenClass = specimenClass;
	}

	/**
	 * Returns the specimen type of the requested new/derived specimen.
	 * @hibernate.property name="specimenType" length="100" type="string" column="SPECIMEN_TYPE"
	 * @return Specimen Type of the new/derived specimen
	 */
	public String getSpecimenType()
	{
		return specimenType;
	}

	/**
	 * Sets the specimen type of the new/derived specimens.
	 * @param specimenType String
	 */
	public void setSpecimenType(String specimenType)
	{
		this.specimenType = specimenType;
	}
}