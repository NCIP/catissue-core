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

import edu.wustl.catissuecore.domain.SpecimenEventParameters;

public abstract class AbstractSpecimenEventParametersFactory<S extends SpecimenEventParameters> implements InstanceFactory<S>
{
	public abstract S createClone(S obj);

	public abstract S createObject();

	public abstract void initDefaultValues(S obj);
}
