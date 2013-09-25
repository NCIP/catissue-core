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

import edu.wustl.catissuecore.domain.CancerResearchGroup;

public class CancerResearchGroupFactory implements InstanceFactory<CancerResearchGroup>
{

	private static CancerResearchGroupFactory cancerResearchGroupFactory;

	private  CancerResearchGroupFactory()
	{
		super();
	}

	public static synchronized CancerResearchGroupFactory getInstance()
	{
		if(cancerResearchGroupFactory==null){
			cancerResearchGroupFactory = new CancerResearchGroupFactory();
		}
		return cancerResearchGroupFactory;
	}

	public CancerResearchGroup createClone(CancerResearchGroup obj)
	{
		CancerResearchGroup cancerResearchGroup = createObject();
		cancerResearchGroup.setName(obj.getName());
		return cancerResearchGroup;
	}

	public CancerResearchGroup createObject()
	{
		CancerResearchGroup cancerResearchGroup = new CancerResearchGroup();
		initDefaultValues(cancerResearchGroup);
		return cancerResearchGroup;
	}

	public void initDefaultValues(CancerResearchGroup obj)
	{

	}
}
