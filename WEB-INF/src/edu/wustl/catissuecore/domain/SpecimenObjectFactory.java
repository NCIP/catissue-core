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
		return new Specimen();
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
		return new Specimen(reqSpecimen);
	}
}