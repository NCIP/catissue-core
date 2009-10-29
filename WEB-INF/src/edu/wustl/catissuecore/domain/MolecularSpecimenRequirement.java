/**
 * <p>Title: MolecularSpecimen Class>
 * <p>Description:  A molecular derivative (I.e. RNA / DNA / Protein Lysate)
 * obtained from a specimen.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author virender_mehta
 * @version caTissueSuite V1.1
 */

package edu.wustl.catissuecore.domain;

import java.io.Serializable;

import edu.wustl.catissuecore.actionForm.SpecimenForm;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.actionForm.IValueObject;
import edu.wustl.common.exception.AssignDataException;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.util.logger.Logger;

/**
 * A molecular derivative (I.e. RNA / DNA / Protein Lysate) obtained from a specimen.
 * @hibernate.subclass name="MolecularRequirementSpecimen" discriminator-value="Molecular"
 */
public class MolecularSpecimenRequirement extends SpecimenRequirement implements Serializable
{

	/**
	 * logger Logger - Generic logger.
	 */
	private static Logger logger = Logger.getCommonLogger(MolecularSpecimenRequirement.class);

	/**
	 * Serial Version ID.
	 */
	private static final long serialVersionUID = 123456789000L;

	/**
	 * Concentration of liquid molecular specimen measured in microgram per microlitter.
	 */
	protected Double concentrationInMicrogramPerMicroliter;

	/**
	 * Default Constructor.
	 */
	public MolecularSpecimenRequirement()
	{
		super();
	}

	/**
	 * Initial amount of specimen created from another specimen.
	 * @param form AbstractActionForm.
	 * @throws AssignDataException : AssignDataException
	 */
	public MolecularSpecimenRequirement(AbstractActionForm form) throws AssignDataException
	{
		super();
		this.setAllValues(form);
	}

	/**
	 * This function Copies the data from an NewSpecimenForm object to a MolecularSpecimenRequirement bean
	 * @param abstractForm An SiteForm object.
	 * @throws AssignDataException :DataException
	 * */
	@Override
	public void setAllValues(IValueObject abstractForm) throws AssignDataException
	{
		try
		{
			super.setAllValues(abstractForm);
			final SpecimenForm form = (SpecimenForm) abstractForm;
			//check for concentration.
			if (Constants.DOUBLE_QUOTES.equals(form.getConcentration()))
			{
				logger.debug("Concentration is " + form.getConcentration());
			}
			else
			{
				this.concentrationInMicrogramPerMicroliter = new Double(form.getConcentration());
			}
		}
		catch (final Exception exception)
		{
			MolecularSpecimenRequirement.logger.error(exception.getMessage(),exception);
			exception.printStackTrace();
			final ErrorKey errorKey = ErrorKey.getErrorKey("assign.data.error");
			throw new AssignDataException(errorKey, null, "MolecularSpecimenRequirement.java :");
		}
	}

	/**
	 * Parameterized Constructor.
	 * @param molecularRequirementSpecimen MolecularSpecimenRequirement.
	 */
	public MolecularSpecimenRequirement(MolecularSpecimenRequirement molecularRequirementSpecimen)
	{
		super();
		this.concentrationInMicrogramPerMicroliter = molecularRequirementSpecimen.concentrationInMicrogramPerMicroliter;
	}

	/**
	 * Returns the concentration of liquid molecular specimen measured
	 * in microgram per microlitre.
	 * @hibernate.property name="concentrationInMicrogramPerMicroliter" type="double"
	 * column="CONCENTRATION" length="50"
	 * @return the concentration of liquid molecular specimen measured
	 * in microgram per microlitre.
	 * directly collected from participant or created from another specimen.
	 * @see #setConcentrationInMicrogramPerMicroLiter(Double)
	 */
	public Double getConcentrationInMicrogramPerMicroliter()
	{
		return this.concentrationInMicrogramPerMicroliter;
	}

	/**
	 * Sets the concentration of liquid molecular specimen measured
	 * in microgram per microlitter.
	 * @param concentrationInMicrogramPerMicroliter the concentration of
	 * liquid molecular specimen measuredin microgram per microlitter.
	 * @see #getConcentrationInMicrogramPerMicroLiter()
	 */
	public void setConcentrationInMicrogramPerMicroliter(
			Double concentrationInMicrogramPerMicroliter)
	{
		this.concentrationInMicrogramPerMicroliter = concentrationInMicrogramPerMicroliter;
	}
}