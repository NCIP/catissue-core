
package edu.wustl.catissuecore.factory;

import edu.wustl.common.domain.AbstractDomainObject;

public interface InstanceFactory<T extends AbstractDomainObject>
{

	T createObject();

	T createClone(T t);

	void initDefaultValues(T t);

}
