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

import edu.wustl.catissuecore.domain.ConsentTier;
import edu.wustl.catissuecore.domain.ConsentTierStatus;

public class ConsentTierStatusFactory implements InstanceFactory<ConsentTierStatus>
{
	private static ConsentTierStatusFactory consentTierStatusFactory;

	private ConsentTierStatusFactory()
	{
		super();
	}

	public static synchronized ConsentTierStatusFactory getInstance()
	{
		if(consentTierStatusFactory==null){
			consentTierStatusFactory = new ConsentTierStatusFactory();
		}
		return consentTierStatusFactory;
	}

	public ConsentTierStatus createClone(ConsentTierStatus obj)
	{
		ConsentTierStatus consentTierStatus = createObject();
		consentTierStatus.setStatus(obj.getStatus());
		InstanceFactory<ConsentTier> instFact= DomainInstanceFactory.getInstanceFactory(ConsentTier.class);
		consentTierStatus.setConsentTier(instFact.createClone(obj.getConsentTier()));
		return consentTierStatus;
	}

	public ConsentTierStatus createObject()
	{
		ConsentTierStatus consentTierStatus = new ConsentTierStatus();
		initDefaultValues(consentTierStatus);
		return consentTierStatus;
	}

	public void initDefaultValues(ConsentTierStatus obj)
	{
	}
}
