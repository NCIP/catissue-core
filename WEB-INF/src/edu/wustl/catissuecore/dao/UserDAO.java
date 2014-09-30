package edu.wustl.catissuecore.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.wustl.catissuecore.bizlogic.UserBizLogic;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.domain.LoginDetails;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.global.Status;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.HibernateDAO;
import edu.wustl.dao.JDBCDAO;
import edu.wustl.dao.daofactory.DAOConfigFactory;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.dao.query.generator.ColumnValueBean;
import edu.wustl.dao.query.generator.DBTypes;
import edu.wustl.dao.util.DAOUtility;
import edu.wustl.dao.util.NamedQueryParam;
import edu.wustl.domain.LoginResult;


public class UserDAO
{

	private static final Logger LOGGER = Logger.getCommonLogger(UserDAO.class);
	public Long getUserIDFromLoginName(HibernateDAO hibernateDao,String loginName,String activityStatus) throws DAOException, BizLogicException 
	{
		Long userId = null;
		Map<String, NamedQueryParam> params = new HashMap<String, NamedQueryParam>();
		params.put("0", new NamedQueryParam(DBTypes.STRING, loginName));
		params.put("1", new NamedQueryParam(DBTypes.STRING, activityStatus));
		List<Long> userIds = hibernateDao.executeNamedQuery("getUserIdFromLoginName", params);
		if(userIds == null || userIds.isEmpty())
		{
			//Will modify this after FC
			String sql = "select identifier from catissue_user u join csm_migrate_user mu "
					+ " on u.login_Name = mu.login_Name where mu.WUSTLKEY = '"+loginName+"' and u.activity_status='"+Constants.ACTIVITY_STATUS_LOCKED+"'";
			JDBCDAO dao = null;
			try {
				//revisit
			dao = AppUtility.openJDBCSession();
			List ids = new ArrayList();
			ids = dao.executeQuery(sql);
			if(ids != null && !ids.isEmpty()){
				List res = (List)ids.get(0);
				userId = Long.valueOf(res.get(0).toString());
			}
			
			}
			catch (ApplicationException e) {
				LOGGER.error("Invalid user details.");
			ErrorKey errorKey = ErrorKey.getErrorKey("errors.invalid");
			throw new BizLogicException(errorKey,null, "User");
			}finally{
				try {
					AppUtility.closeJDBCSession(dao);
				}
				catch (ApplicationException e) {
					LOGGER.error("Invalid user details.");
					ErrorKey errorKey = ErrorKey.getErrorKey("errors.invalid");
					throw new BizLogicException(errorKey,null, "User");
				}
			}
//			LOGGER.error("Invalid user details.");
//			ErrorKey errorKey = ErrorKey.getErrorKey("errors.invalid");
//			throw new BizLogicException(errorKey,null, "User");
		}
		else{
			userId = userIds.get(0);
		}
		return userId;
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
	public String getUserNameById(final Long userId)
			throws BizLogicException, DAOException
	{
		HibernateDAO hibernateDAO = null;
		StringBuffer userName = new StringBuffer(100);
			try
			{
				hibernateDAO = (HibernateDAO)AppUtility.openDAOSession(null);
			
		
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
			}
			catch (ApplicationException e)
			{
				LOGGER.error(e);
				throw new DAOException(e.getErrorKey(), e, e.getMsgValues());
			}
			finally{
				try {
					AppUtility.closeDAOSession(hibernateDAO);
				}
				catch (ApplicationException e) {
					LOGGER.error(e);
					throw new DAOException(e.getErrorKey(), e, e.getMsgValues());
				}
			}
		return userName.toString();
	}
	
    public void updateUserActivityStatus(HibernateDAO hibernateDAO, Long userId, String activityStatus)
            throws DAOException
    {
        Map<String, NamedQueryParam> params = new HashMap<String, NamedQueryParam>();
        params.put("0", new NamedQueryParam(DBTypes.STRING, activityStatus));
        params.put("1", new NamedQueryParam(DBTypes.LONG, userId));

        hibernateDAO.executeUpdateWithNamedQuery("updateUserActivityStatus", params);

    }

    public User getUserFromLoginName(HibernateDAO hibernateDao, String loginName) throws DAOException,
            BizLogicException
    {

        Map<String, NamedQueryParam> params = new HashMap<String, NamedQueryParam>();
        params.put("0", new NamedQueryParam(DBTypes.STRING, loginName));

        params.put("1", new NamedQueryParam(DBTypes.STRING , Status.ACTIVITY_STATUS_ACTIVE.toString()));
        List<User> userList = hibernateDao.executeNamedQuery("getUserFromLoginName", params);
        if (userList == null || userList.isEmpty())
        {
            LOGGER.error("Invalid user details.");
            ErrorKey errorKey = ErrorKey.getErrorKey("errors.invalid");
            throw new BizLogicException(errorKey, null, "User");
        }
        return userList.get(0);
    }

    public LoginDetails getLoginDetails(HibernateDAO hibernateDao, String loginName, String remoteAddr)
            throws DAOException
    {

        Map<String, NamedQueryParam> params = new HashMap<String, NamedQueryParam>();
        params.put("0", new NamedQueryParam(DBTypes.STRING, Status.ACTIVITY_STATUS_ACTIVE.toString()));
        params.put("1", new NamedQueryParam(DBTypes.STRING, loginName.toUpperCase()));
        LoginDetails loginDetails = null;
        final List UserIds = hibernateDao.executeNamedQuery("getLoginDetails", params);
        if (UserIds != null && !UserIds.isEmpty())
        {
            final Object[] obj = (Object[]) UserIds.get(0);
            loginDetails = new LoginDetails((Long) obj[0], (Long) obj[1], remoteAddr);
        }

        return loginDetails;

    }
	   
    public void populateLoginResult(HibernateDAO hibernateDAO, LoginDetails loginDetails, LoginResult loginResult)
            throws DAOException
    {
         Map<String, NamedQueryParam> params = new HashMap<String, NamedQueryParam>();
        params.put("0", new NamedQueryParam(DBTypes.LONG, loginDetails.getUserLoginId()));
        params.put("1",
                new NamedQueryParam(DBTypes.LONG,  loginDetails.getUserLoginId()));
        try{
           
        List result = hibernateDAO.executeNamedQuery("getUserLoginResult", params);
        if (result != null && result.size() > 0)
        {
            Object[] obj = (Object[]) result.get(0);
            loginResult.setLastLoginActivityStatus(Boolean.valueOf(obj[3].toString()));
            Timestamp timestamp = (Timestamp) obj[4];
            SimpleDateFormat sdf = new SimpleDateFormat(ApplicationProperties.getValue("date.pattern.timestamp"));
            String dateVal = sdf.format(timestamp);
            loginResult.setLastLoginTime(dateVal);
        }
        }catch(Exception ex){
            ex.printStackTrace();
            throw new DAOException(null,null,null);
        }

    }

}
