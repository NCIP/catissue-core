/**
 * <p>Title: SpecimenCharacteristics Class>
 * <p>Description: The combined anatomic state and pathological
 * disease classification of a specimen.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 */

package edu.wustl.catissuecore.domain;

import edu.wustl.common.actionForm.IValueObject;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.AssignDataException;

/**
 * The combined anatomic state and pathological
 * disease classification of a specimen.
 * @hibernate.class table="CATISSUE_SPECIMEN_CHAR"
 * @author gautam_shetty
 */
public class SpecimenCharacteristics extends AbstractDomainObject implements java.io.Serializable
{

	/**
	 * Serial Version ID.
	 */
	private static final long serialVersionUID = 1234567890L;

	/**
	 * System generated unique id.
	 */
	protected Long id;

	/**
	 * The anatomical site from which a specimen is derived.
	 */
	protected String tissueSite;

	/**
	 * For bilateral sites, left or right.
	 */
	protected String tissueSide;

	/**
	 * Histoathological character of specimen.
	 * e.g. Non-Malignant, Malignant, Non-Malignant Diseased, Pre-Malignant.
	 */
	//protected String pathologicalStatus;
	/**
	 * Returns the system generated unique id.
	 * @hibernate.id name="id" column="IDENTIFIER" type="long" length="30"
	 * unsaved-value="null" generator-class="native"
	 * @hibernate.generator-param name="sequence" value="CATISSUE_SPECIMEN_CHAR_SEQ"
	 * @return the system generated unique id.
	 * @see #setId(Long)
	 * */
	@Override
	public Long getId()
	{
		return this.id;
	}

	/**
	 * Sets the system generated unique id.
	 * @param identifier the system generated unique id.
	 * @see #getId()
	 * */
	@Override
	public void setId(Long identifier)
	{
		this.id = identifier;
	}

	/**
	 * Returns the anatomical site from which a specimen is derived.
	 * @hibernate.property name="tissueSite" type="string"
	 * column="TISSUE_SITE" length="150"
	 * @return the anatomical site from which a specimen is derived.
	 * @see #setTissueSite(String)
	 */
	public String getTissueSite()
	{
		return this.tissueSite;
	}

	/**
	 * Sets the anatomical site from which a specimen is derived.
	 * @param tissueSite the anatomical site from which a specimen is derived.
	 * @see #getTissueSite()
	 */
	public void setTissueSite(String tissueSite)
	{
		this.tissueSite = tissueSite;
	}

	/**
	 * Returns the tissue side. For bilateral sites, left or right.
	 * @hibernate.property name="tissueSide" type="string"
	 * column="TISSUE_SIDE" length="50"
	 * @return the tissue side. For bilateral sites, left or right.
	 * @see #setTissueSide(String)
	 */
	public String getTissueSide()
	{
		return this.tissueSide;
	}

	/**
	 * Set the tissue side. For bilateral sites, left or right.
	 * @param tissueSide the tissue side. For bilateral sites, left or right.
	 * @see #getTissueSide()
	 */
	public void setTissueSide(String tissueSide)
	{
		this.tissueSide = tissueSide;
	}

	/**
	 * Returns the Histoathological character of specimen.
	 * e.g. Non-Malignant, Malignant, Non-Malignant Diseased, Pre-Malignant.
	 * @hibernate.property name="pathologicalStatus" type="string"
	 * column="PATHOLOGICAL_STATUS" length="50"
	 * @return the Histoathological character of specimen.
	 * @see #setPathologicalStatus(String)
	 */
	//    public String getPathologicalStatus()
	//    {
	//        return pathologicalStatus;
	//    }
	/**
	 * Sets the Histoathological character of specimen.
	 * e.g. Non-Malignant, Malignant, Non-Malignant Diseased, Pre-Malignant.
	 * @param pathologicalStatus the Histoathological character of specimen.
	 * @see #getPathologicalStatus()
	 */
	//    public void setPathologicalStatus(String pathologicalStatus)
	//    {
	//        this.pathologicalStatus = pathologicalStatus;
	//    }
	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.domain.AbstractDomainObject#setAllValues(
	 * edu.wustl.catissuecore.actionForm.AbstractActionForm)
	 */

	/**
	 * Set All Values.
	 * @param abstractForm of IValueObject type.
	 * @throws AssignDataException AssignDataException.
	 */
	@Override
	public void setAllValues(IValueObject abstractForm) throws AssignDataException
	{
		//
	}

	/**
	 * Default Constructor.
	 */
	public SpecimenCharacteristics()
	{
		super();
	}

	/**
	 * Parameterized Constructor.
	 * @param specimenCharacteristics SpecimenCharacteristics.
	 */
	public SpecimenCharacteristics(SpecimenCharacteristics specimenCharacteristics)
	{
		super();
		this.tissueSide = specimenCharacteristics.getTissueSide();
		this.tissueSite = specimenCharacteristics.getTissueSite();
	}
}