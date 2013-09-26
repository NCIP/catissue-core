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

public class ConsentTierFactory implements InstanceFactory<ConsentTier>
{
	private static ConsentTierFactory consentTierFactory;

	private ConsentTierFactory()
	{
		super();
	}

	public static synchronized ConsentTierFactory getInstance()
	{
		if(consentTierFactory==null){
			consentTierFactory = new ConsentTierFactory();
		}
		return consentTierFactory;
	}

	public ConsentTier createClone(ConsentTier obj)
	{
		ConsentTier consentTier = createObject();
		if (obj.getId() != null && obj.getId().toString().trim().length() > 0)
		{
			consentTier.setId(obj.getId());
		}
		consentTier.setStatement(obj.getStatement());

		return consentTier;
	}

	public ConsentTier createObject()
	{
		ConsentTier consentTier = new ConsentTier();
		initDefaultValues(consentTier);
		return consentTier;
	}

	public void initDefaultValues(ConsentTier obj)
	{
	}
}
