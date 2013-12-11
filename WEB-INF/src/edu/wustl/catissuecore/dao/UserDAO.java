package edu.wustl.catissuecore.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.wustl.catissuecore.bizlogic.UserBizLogic;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;
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
	
	/**
	 * This method returns the user name of user of given id (format of name:
	 * LastName,FirstName)
	 * 
	 * @param userId
	 *            user id of which user name has to return
	 * @return userName in the given format
	 * @throws BizLogicException
	 *             BizLogic Exception
	 * @throws DAOException
	 *             generic DAO Exception
	 */
	public String getUserNameById(final Long userId, HibernateDAO hibernateDAO)
			throws BizLogicException, DAOException
	{
		
		if(hibernateDAO == null)
		{
			try
			{
				hibernateDAO = (HibernateDAO)AppUtility.openDAOSession(null);
			}
			catch (ApplicationException e)
			{
				LOGGER.error(e);
				throw new DAOException(e.getErrorKey(), e, e.getMsgValues());
			}
		}
		StringBuffer userName = new StringBuffer(100);
		Map<String, NamedQueryParam> params = new HashMap<String, NamedQueryParam>();
		params.put("0", new NamedQueryParam(DBTypes.LONG, userId));
		List userNames = hibernateDAO.executeNamedQuery("getUserNameFromID", params);
		if(userNames != null || !userNames.isEmpty())
		{
			Object[] names = (Object[])userNames.get(0);
			if(names[1] != null)
			{
				userName.append(names[1].toString());
			}
			userName.append(", ");
			if(names[0] != null)
			{
				userName.append(names[0].toString());
			}
		}
		return userName.toString();
	}
}
