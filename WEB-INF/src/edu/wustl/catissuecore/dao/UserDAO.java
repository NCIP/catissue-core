package edu.wustl.catissuecore.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.HibernateDAO;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.dao.query.generator.DBTypes;
import edu.wustl.dao.util.NamedQueryParam;


public class UserDAO
{

	private static final Logger LOGGER = Logger.getCommonLogger(UserDAO.class);
	public Long getUserIDFromLoginName(HibernateDAO hibernateDao,String loginName) throws DAOException, BizLogicException 
	{

		Map<String, NamedQueryParam> params = new HashMap<String, NamedQueryParam>();
		params.put("0", new NamedQueryParam(DBTypes.STRING, loginName));
		params.put("1", new NamedQueryParam(DBTypes.STRING, Constants.ACTIVITY_STATUS_ACTIVE));
		List<Long> userIds = hibernateDao.executeNamedQuery("getUserIdFromLoginName", params);
		if(userIds == null || userIds.isEmpty())
		{
			LOGGER.error("Invalid user details.");
			ErrorKey errorKey = ErrorKey.getErrorKey("errors.invalid");
			throw new BizLogicException(errorKey,null, "User");
		}
		return userIds.get(0);
	}
}
