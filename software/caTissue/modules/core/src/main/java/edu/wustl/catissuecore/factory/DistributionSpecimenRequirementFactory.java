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

import edu.wustl.catissuecore.domain.DistributionProtocol;
import edu.wustl.catissuecore.domain.DistributionSpecimenRequirement;

public class DistributionSpecimenRequirementFactory implements InstanceFactory<DistributionSpecimenRequirement>
{
	private static DistributionSpecimenRequirementFactory distributionSpecimenRequirementFactory;

	private DistributionSpecimenRequirementFactory()
	{
		super();
	}

	public static synchronized DistributionSpecimenRequirementFactory getInstance()
	{
		if(distributionSpecimenRequirementFactory==null){
			distributionSpecimenRequirementFactory = new DistributionSpecimenRequirementFactory();
		}
		return distributionSpecimenRequirementFactory;
	}

	public DistributionSpecimenRequirement createClone(DistributionSpecimenRequirement obj)
	{
		DistributionSpecimenRequirement dsr = createObject();
		return dsr;
	}

	public DistributionSpecimenRequirement createObject()
	{
		DistributionSpecimenRequirement dsr = new DistributionSpecimenRequirement();
		initDefaultValues(dsr);
		return dsr;
	}

	public void initDefaultValues(DistributionSpecimenRequirement obj)
	{
		obj.setQuantity(new Double(0));
		InstanceFactory<DistributionProtocol> instFact = DomainInstanceFactory.getInstanceFactory(DistributionProtocol.class);
		obj.setDistributionProtocol(instFact.createObject());
	}
}
