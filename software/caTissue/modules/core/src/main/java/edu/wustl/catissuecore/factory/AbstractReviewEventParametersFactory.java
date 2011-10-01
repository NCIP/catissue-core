package edu.wustl.catissuecore.factory;

import edu.wustl.catissuecore.domain.ReviewEventParameters;

public abstract class AbstractReviewEventParametersFactory<S extends ReviewEventParameters> implements InstanceFactory<S>
{
	public abstract S createClone(S obj);

	public abstract S createObject();

	public abstract void initDefaultValues(S obj);
}
