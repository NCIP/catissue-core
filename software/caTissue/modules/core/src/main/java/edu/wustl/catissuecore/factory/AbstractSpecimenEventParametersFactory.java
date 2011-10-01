package edu.wustl.catissuecore.factory;

import edu.wustl.catissuecore.domain.SpecimenEventParameters;

public abstract class AbstractSpecimenEventParametersFactory<S extends SpecimenEventParameters> implements InstanceFactory<S>
{
	public abstract S createClone(S obj);

	public abstract S createObject();

	public abstract void initDefaultValues(S obj);
}
