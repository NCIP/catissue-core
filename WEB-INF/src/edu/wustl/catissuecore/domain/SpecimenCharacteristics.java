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

import edu.wustl.catissuecore.actionForm.AbstractActionForm;
import edu.wustl.catissuecore.exception.AssignDataException;

/**
 * The combined anatomic state and pathological 
 * disease classification of a specimen.
 * @hibernate.class table="CATISSUE_SPECIMEN_CHARACTERISTICS"
 * @author gautam_shetty
 */
public class SpecimenCharacteristics extends AbstractDomainObject implements java.io.Serializable
{
    private static final long serialVersionUID = 1234567890L;
    
    /**
     * System generated unique systemIdentifier.
     */
    protected Long systemIdentifier;

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
    protected String pathologicalStatus;

    /**
     * Returns the system generated unique systemIdentifier.
     * @hibernate.id name="systemIdentifier" column="IDENTIFIER" type="long" length="30"
     * unsaved-value="null" generator-class="native"
     * @return the system generated unique systemIdentifier.
     * @see #setSystemIdentifier(Long)
     * */
    public Long getSystemIdentifier()
    {
        return systemIdentifier;
    }

    /**
     * Sets the system generated unique systemIdentifier.
     * @param systemIdentifier the system generated unique systemIdentifier.
     * @see #getSystemIdentifier()
     * */
    public void setSystemIdentifier(Long systemIdentifier)
    {
        this.systemIdentifier = systemIdentifier;
    }
    
    /**
     * Returns the anatomical site from which a specimen is derived.
     * @hibernate.property name="tissueSite" type="string" 
     * column="TISSUE_SITE" length="50"
     * @return the anatomical site from which a specimen is derived.
     * @see #setTissueSite(String)
     */
    public String getTissueSite()
    {
        return tissueSite;
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
        return tissueSide;
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
    public String getPathologicalStatus()
    {
        return pathologicalStatus;
    }

    /**
     * Sets the Histoathological character of specimen.
     * e.g. Non-Malignant, Malignant, Non-Malignant Diseased, Pre-Malignant.
     * @param pathologicalStatus the Histoathological character of specimen.
     * @see #getPathologicalStatus()
     */
    public void setPathologicalStatus(String pathologicalStatus)
    {
        this.pathologicalStatus = pathologicalStatus;
    }

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.domain.AbstractDomainObject#setAllValues(edu.wustl.catissuecore.actionForm.AbstractActionForm)
	 */
	public void setAllValues(AbstractActionForm abstractForm) throws AssignDataException
	{
		
	}
}