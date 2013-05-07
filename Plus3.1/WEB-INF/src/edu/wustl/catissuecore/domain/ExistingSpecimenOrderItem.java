/**
 * <p>Title: ExistingSpecimenOrderItem Class>
 * <p>Description:  Parent Class for ExistingSpecimenOrderItem.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Ashish Gupta
 * @version 1.00
 * Created on October 16,2006
 */

package edu.wustl.catissuecore.domain;

import java.util.Collection;

/**
 * This is  abstract class indicating the order of existing biospecimens.
 * * Represents  Pathology Order Item.
 * @hibernate.joined-subclass table="CATISSUE_EXISTING_SP_ORD_ITEM"
 * @hibernate.joined-subclass-key
 * column="IDENTIFIER"
 * @author ashish_gupta
 */
public class ExistingSpecimenOrderItem extends SpecimenOrderItem
{

	/**
	 * Serial Version ID.
	 */
	private static final long serialVersionUID = 9141787148538325935L;

	/**
	 * The specimen associated with the order item.
	 */
	protected Specimen specimen;

	/**
	 * consentTierStatusCollection.
	 */
	private Collection consentTierStatusCollection = null;

	/**
	 * The specimen associated with the order item in SpecimenOrderItem.
	 * @hibernate.many-to-one column="SPECIMEN_ID" class="edu.wustl.catissuecore.domain.Specimen"
	 * constrained="true"
	 * @return the specimen
	 */
	public Specimen getSpecimen()
	{
		return this.specimen;
	}

	/**
	 * @param specimen the specimen to get.
	 * @see #getSpecimen()
	 */
	public void setSpecimen(Specimen specimen)
	{
		this.specimen = specimen;
	}

	/**
	 * Get ConsentTierStatus Collection.
	 * @return Collection.
	 */
	public Collection getConsentTierStatusCollection()
	{
		return this.consentTierStatusCollection;
	}

	/**
	 * Set ConsentTierStatus Collection.
	 * @param consentTierStatusCollection Collection.
	 */
	public void setConsentTierStatusCollection(Collection consentTierStatusCollection)
	{
		this.consentTierStatusCollection = consentTierStatusCollection;
	}
}