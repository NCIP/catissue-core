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

import edu.wustl.catissuecore.domain.pathology.SemanticType;


public class SemanticTypeFactory implements InstanceFactory<SemanticType>
{

	private static SemanticTypeFactory semanticTypeFactory;

	private SemanticTypeFactory()
	{
		super();
	}

	public static synchronized SemanticTypeFactory getInstance()
	{
		if(semanticTypeFactory==null)
		{
			semanticTypeFactory = new SemanticTypeFactory();
		}
		return semanticTypeFactory;
	}

	public SemanticType createClone(SemanticType t)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public SemanticType createObject()
	{
		SemanticType semanticType = new SemanticType();
		initDefaultValues(semanticType);
		return semanticType;
	}

	public void initDefaultValues(SemanticType t)
	{
		// TODO Auto-generated method stub

	}

}
