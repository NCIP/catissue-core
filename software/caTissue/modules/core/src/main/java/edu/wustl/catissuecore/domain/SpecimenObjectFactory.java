/**
 * <p>Title: SpecimenObjectFactory Class>
 * <p>Description:	SpecimenObjectFactory is a factory for instances of various Specimen domain objects.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Abhishek mehta
 * @version 1.00
 * Created on Oct 26, 2007
 * */

package edu.wustl.catissuecore.domain;

import edu.wustl.catissuecore.factory.DomainInstanceFactory;
import edu.wustl.catissuecore.factory.InstanceFactory;
import edu.wustl.catissuecore.factory.utils.SpecimenUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.AssignDataException;

/**
 * @author sagar_baldwa
 *
 */
public class SpecimenObjectFactory
{

	/**
	 * Get the domain object.
	 * @param specimenType of String type.
	 * @return AbstractDomainObject object.
	 * @throws AssignDataException AssignDataException.
	 */
	public AbstractDomainObject getDomainObject(String specimenType) throws AssignDataException
	{
		AbstractDomainObject abstractDomain = null;
		if (Constants.TISSUE.equals(specimenType))
		{
			InstanceFactory<TissueSpecimen> instFact= DomainInstanceFactory.getInstanceFactory(TissueSpecimen.class);
			abstractDomain = instFact.createObject();
		}
		else if (Constants.FLUID.equals(specimenType))
		{
			InstanceFactory<FluidSpecimen> instFact= DomainInstanceFactory.getInstanceFactory(FluidSpecimen.class);
			abstractDomain = instFact.createObject();
		}
		else if (Constants.CELL.equals(specimenType))
		{
			InstanceFactory<CellSpecimen> instFact= DomainInstanceFactory.getInstanceFactory(CellSpecimen.class);
			abstractDomain = instFact.createObject();
		}
		else if (Constants.MOLECULAR.equals(specimenType))
		{
			InstanceFactory<MolecularSpecimen> instFact= DomainInstanceFactory.getInstanceFactory(MolecularSpecimen.class);
			abstractDomain = instFact.createObject();
		}
		return abstractDomain;
	}

	/**
	 * Get the domain object.
	 * @param specimenType of String type.
	 * @param reqSpecimen of SpecimenRequirement object.
	 * @return AbstractDomainObject object.
	 * @throws AssignDataException AssignDataException.
	 */
	public AbstractDomainObject getDomainObject(String specimenType, SpecimenRequirement reqSpecimen)
			throws AssignDataException
	{
		AbstractDomainObject abstractDomain = null;
		if (Constants.TISSUE.equals(specimenType))
		{
			InstanceFactory<TissueSpecimen> instFact= DomainInstanceFactory.getInstanceFactory(TissueSpecimen.class);
			TissueSpecimen tissueSpecimen=instFact.createObject();
			abstractDomain=SpecimenUtility.initSpecimenFromRequirement(tissueSpecimen, reqSpecimen);
		}
		else if (Constants.FLUID.equals(specimenType))
		{
			InstanceFactory<FluidSpecimen> instFact= DomainInstanceFactory.getInstanceFactory(FluidSpecimen.class);
			FluidSpecimen fluidSpecimen=instFact.createObject();
			abstractDomain=SpecimenUtility.initSpecimenFromRequirement(fluidSpecimen, reqSpecimen);
		}
		else if (Constants.CELL.equals(specimenType))
		{
			InstanceFactory<CellSpecimen> instFact= DomainInstanceFactory.getInstanceFactory(CellSpecimen.class);
			CellSpecimen cellSpecimen=instFact.createObject();
			abstractDomain=SpecimenUtility.initSpecimenFromRequirement(cellSpecimen, reqSpecimen);
		}
		else if (Constants.MOLECULAR.equals(specimenType))
		{
			InstanceFactory<MolecularSpecimen> instFact= DomainInstanceFactory.getInstanceFactory(MolecularSpecimen.class);
			MolecularSpecimen molecularSpecimen=instFact.createObject();
			abstractDomain=SpecimenUtility.initSpecimenFromMolecularRequirement(molecularSpecimen, reqSpecimen);
		}
		return abstractDomain;
	}
}