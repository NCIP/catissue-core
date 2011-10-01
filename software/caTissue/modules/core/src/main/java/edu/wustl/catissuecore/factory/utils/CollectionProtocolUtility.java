package edu.wustl.catissuecore.factory.utils;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;

import edu.wustl.catissuecore.bean.ConsentBean;
import edu.wustl.catissuecore.domain.ConsentTier;
import edu.wustl.catissuecore.factory.DomainInstanceFactory;
import edu.wustl.catissuecore.util.CollectionProtocolUtil;
import edu.wustl.common.util.MapDataParser;


public class CollectionProtocolUtility
{
	/**
	 * @param consentTierMap
	 *            Consent Tier Map
	 * @throws Exception : Exception
	 * @return consentStatementColl
	 */
	public static Collection<ConsentTier> prepareConsentTierCollection(Map consentTierMap) throws Exception
	{
		final MapDataParser mapdataParser = new MapDataParser("edu.wustl.catissuecore.bean");
		final Map consentMap = CollectionProtocolUtil.sortConsentMap(consentTierMap);//bug 8905
		final Collection beanObjColl = mapdataParser.generateData(consentMap);//consentTierMap

		//Collection<ConsentTier> consentStatementColl = new HashSet<ConsentTier>();
		final Collection<ConsentTier> consentStatementColl = new LinkedHashSet<ConsentTier>();//bug 8905
		final Iterator iter = beanObjColl.iterator();
		while (iter.hasNext())
		{
			final ConsentBean consentBean = (ConsentBean) iter.next();
			final ConsentTier consentTier = (ConsentTier)DomainInstanceFactory.getInstanceFactory(ConsentTier.class).createObject();//new ConsentTier();
			consentTier.setStatement(consentBean.getStatement());
			//To set ID for Edit case
			if (consentBean.getConsentTierID() != null
					&& consentBean.getConsentTierID().trim().length() > 0)
			{
				consentTier.setId(Long.parseLong(consentBean.getConsentTierID()));
			}
			//Check for empty consents
			if (consentBean.getStatement() != null
					&& consentBean.getStatement().trim().length() > 0)
			{
				consentStatementColl.add(consentTier);
			}
		}
		return consentStatementColl;
	}

}
