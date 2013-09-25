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

import edu.wustl.catissuecore.domain.ReviewEventParameters;

public abstract class AbstractReviewEventParametersFactory<S extends ReviewEventParameters> implements InstanceFactory<S>
{
	public abstract S createClone(S obj);

	public abstract S createObject();

	public abstract void initDefaultValues(S obj);
}
