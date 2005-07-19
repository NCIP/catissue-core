/**
 * <p>Title: MolecularSpecimenRequirement Class</p>
 * <p>Description: Required  attributes for a molecular specimen associated with a Collection or Distribution Protocol.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Mandar Deshmukh
 * @version 1.00
 * Created on July 12, 2005
 */

package edu.wustl.catissuecore.domain;

import java.io.Serializable;

/**
 * Required  attributes for a molecular specimen associated with a Collection or Distribution Protocol.
 * @hibernate.joined-subclass table="CATISSUE_MOLECULAR_SPECIMEN_REQUIREMENT"
 * @hibernate.joined-subclass-key column="IDENTIFIER"

 * @author Mandar Deshmukh
 */
public class MolecularSpecimenRequirement extends SpecimenRequirement implements Serializable
{
	private static final long serialVersionUID = 1234567890L;
	
	/**
	 * Amount of specimen required.
	 */
	protected Double quantityInMicroGram;
	
	/**
	 * Required concentration of molecular specimen.
	 */
	protected Double concentrationInMicroGramPerMicroLiter;

	/**
	 * Returns the quantity in Micro Gram.
	 * @hibernate.property name="quantityInMicroGram" type="double"
	 * column="QUANTITY_IN_MICRO_GRAM" length="50"
	 * @return Returns the quantity in Micro Gram.
	 */
	public Double getQuantityInMicroGram()
	{
		return quantityInMicroGram;
	}

	/**
	 * @param quantityInMicroGram
	 * Quantity to set.
	 */
	public void setQuantityInMicroGram(Double quantityInMicroGram)
	{
		this.quantityInMicroGram = quantityInMicroGram;
	}

	/**
	 * Returns the concentration in Micro Gram per MicroLiter.
	 * @hibernate.property name="concentrationInMicroGramPerMicroLiter" type="double"
	 * column="CONCENTRATION_IN_MICROGRAM_PER_MICROLITER" length="50"
	 * @return Returns the concentration in Micro Gram.
	 */
	public Double getConcentrationInMicroGramPerMicroLiter()
	{
		return concentrationInMicroGramPerMicroLiter;
	}

	/**
	 * @param concentrationInMicroGramPerMicroLiter
	 * Concentration to set.
	 */
	public void setConcentrationInMicroGramPerMicroLiter(Double concentrationInMicroGramPerMicroLiter)
	{
		this.concentrationInMicroGramPerMicroLiter = concentrationInMicroGramPerMicroLiter;
	}
}