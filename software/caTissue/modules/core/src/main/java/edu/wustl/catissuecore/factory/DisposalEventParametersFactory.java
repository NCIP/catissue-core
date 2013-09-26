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

import edu.wustl.catissuecore.domain.DisposalEventParameters;

public class DisposalEventParametersFactory extends
		AbstractSpecimenEventParametersFactory<DisposalEventParameters>
{

	private static DisposalEventParametersFactory disposalEventParametersFactory;

	private DisposalEventParametersFactory()
	{
		super();
	}

	public static synchronized DisposalEventParametersFactory getInstance()
	{
		if(disposalEventParametersFactory==null){
			disposalEventParametersFactory = new DisposalEventParametersFactory();
		}
		return disposalEventParametersFactory;
	}

	public DisposalEventParameters createClone(DisposalEventParameters obj)
	{
		DisposalEventParameters disposalEventParameters = createObject();
		return disposalEventParameters;
	}

	public DisposalEventParameters createObject()
	{
		DisposalEventParameters disposalEventParameters = new DisposalEventParameters();
		initDefaultValues(disposalEventParameters);
		return disposalEventParameters;
	}

	public void initDefaultValues(DisposalEventParameters obj)
	{
	}

}
