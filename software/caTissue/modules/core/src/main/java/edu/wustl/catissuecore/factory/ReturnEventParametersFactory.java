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

import edu.wustl.catissuecore.domain.ReturnEventParameters;

public class ReturnEventParametersFactory extends AbstractSpecimenEventParametersFactory<ReturnEventParameters>
{

	private static ReturnEventParametersFactory returnEventParametersFactory;

	private ReturnEventParametersFactory()
	{
		super();
	}

	public static synchronized ReturnEventParametersFactory getInstance()
	{
		if(returnEventParametersFactory==null){
			returnEventParametersFactory = new ReturnEventParametersFactory();
		}
		return returnEventParametersFactory;
	}
	public ReturnEventParameters createClone(ReturnEventParameters obj)
	{
		ReturnEventParameters returnEventParameters = createObject();
		return returnEventParameters;
	}

	public ReturnEventParameters createObject()
	{
		ReturnEventParameters returnEventParameters = new ReturnEventParameters();
		initDefaultValues(returnEventParameters);
		return returnEventParameters;
	}

	public void initDefaultValues(ReturnEventParameters obj)
	{
	}
}

