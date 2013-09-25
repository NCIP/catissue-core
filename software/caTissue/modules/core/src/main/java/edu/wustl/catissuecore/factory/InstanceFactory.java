/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-core/LICENSE.txt for details.
 */


package edu.wustl.catissuecore.factory;

import edu.wustl.common.domain.AbstractDomainObject;

public interface InstanceFactory<T extends AbstractDomainObject>
{

	T createObject();

	T createClone(T t);

	void initDefaultValues(T t);

}
