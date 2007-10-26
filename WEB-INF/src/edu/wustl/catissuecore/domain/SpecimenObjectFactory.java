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

import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.AssignDataException;

public class SpecimenObjectFactory {

	public AbstractDomainObject getDomainObject(String specimenType) throws AssignDataException
    {
		AbstractDomainObject abstractDomain = null;
		if(specimenType.equals("Tissue"))
    	{
    		abstractDomain = new TissueSpecimen();
    	}
    	else if(specimenType.equals("Fluid"))
    	{
    		abstractDomain = new FluidSpecimen();
    	}
    	else if(specimenType.equals("Cell"))
    	{
    		abstractDomain = new CellSpecimen();
    	}
    	else if(specimenType.equals("Molecular"))
    	{
    		abstractDomain = new MolecularSpecimen();
    	}
		return abstractDomain;
    }
}
