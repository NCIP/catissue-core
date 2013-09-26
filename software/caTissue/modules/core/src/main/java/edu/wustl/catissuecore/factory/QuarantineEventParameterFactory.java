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

import edu.wustl.catissuecore.domain.pathology.QuarantineEventParameter;


public class QuarantineEventParameterFactory implements InstanceFactory<QuarantineEventParameter>
{
	private static QuarantineEventParameterFactory quarantineEventParameterFactory;

	private QuarantineEventParameterFactory()
	{
		super();
	}

	public static synchronized QuarantineEventParameterFactory getInstance()
	{
		if(quarantineEventParameterFactory==null){
			quarantineEventParameterFactory = new QuarantineEventParameterFactory();
		}
		return quarantineEventParameterFactory;
	}

	public QuarantineEventParameter createClone(QuarantineEventParameter t)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public QuarantineEventParameter createObject()
	{
		QuarantineEventParameter eventParameter= new QuarantineEventParameter();
		initDefaultValues(eventParameter);
		return eventParameter;
	}

	public void initDefaultValues(QuarantineEventParameter t)
	{
		// TODO Auto-generated method stub

	}

}
