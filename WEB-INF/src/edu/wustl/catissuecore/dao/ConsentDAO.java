package edu.wustl.catissuecore.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.ConsentTier;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.HibernateDAO;
import edu.wustl.dao.exception.DAOException;


public class ConsentDAO
{
	private static final transient Logger LOGGER = Logger
			.getCommonLogger(ConsentDAO.class);
	public Long getConsentIdFromStatement(String statement, Long cpID) throws ApplicationException
	{
		String query = "select consent.identifier from catissue_consent_tier consent where STATEMENT like '"+statement+"' and COLL_PROTOCOL_ID ="+cpID;
		List idList = AppUtility.executeSQLQuery(query);
		if(idList == null || idList.isEmpty())
		{
			LOGGER.error("Invalid Consent Details.");
			ErrorKey errorKey = ErrorKey.getErrorKey("invalid.consents.response");
			throw new BizLogicException(errorKey, null, null);
		}
		ArrayList list = (ArrayList)idList.get(0);
		return Long.valueOf(list.get(0).toString());
	}
	
	public Collection<ConsentTier> getConsentTierFromCP(HibernateDAO hibernateDAO,Long cpId) throws DAOException
	{
		Collection<ConsentTier> tiers = new HashSet<ConsentTier>();
		String hql = "select cp.consentTierCollection from "+CollectionProtocol.class.getName()+" cp where cp.id = "+cpId;
		List result = hibernateDAO.executeQuery(hql);
		return result;
	}
}
