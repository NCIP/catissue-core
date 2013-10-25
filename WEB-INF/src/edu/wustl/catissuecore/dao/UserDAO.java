package edu.wustl.catissuecore.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
import edu.wustl.dao.util.DAOUtility;
import edu.wustl.dao.util.NamedQueryParam;


public class UserDAO
{

	private static final Logger LOGGER = Logger.getCommonLogger(UserDAO.class);
	public Long getUserIDFromLoginName(HibernateDAO hibernateDao,String loginName,String activityStatus) throws DAOException, BizLogicException 
	{

		Map<String, NamedQueryParam> params = new HashMap<String, NamedQueryParam>();
		params.put("0", new NamedQueryParam(DBTypes.STRING, loginName));
		params.put("1", new NamedQueryParam(DBTypes.STRING, activityStatus));
		List<Long> userIds = hibernateDao.executeNamedQuery("getUserIdFromLoginName", params);
		if(userIds == null || userIds.isEmpty())
		{
			LOGGER.error("Invalid user details.");
			ErrorKey errorKey = ErrorKey.getErrorKey("errors.invalid");
			throw new BizLogicException(errorKey,null, "User");
		}
		return userIds.get(0);
	}
	
	public List<Long> getAssociatedSiteIds(HibernateDAO hibernateDAO, Long userId) throws DAOException
	{
		List<Long> list = new ArrayList();
		Map<String, NamedQueryParam> params = new HashMap<String, NamedQueryParam>();
		params.put("1", new NamedQueryParam(DBTypes.LONG, userId));
		try {
			ResultSet resultset = hibernateDAO.executeNamedSQLQuery("getSiteIdsByUserId", params);
			List resultList = DAOUtility.getInstance().getListFromRS(resultset);
			if(resultList != null && !resultList.isEmpty())
			{
				for (Object object : resultList) 
				{
					Long val = Long.valueOf(((List)object).get(0).toString());
					list.add(val);
				}
			}
		} catch (SQLException e) {
			LOGGER.error(e);
			throw new DAOException(null, e, null);
		}
		return list;
	}
}
