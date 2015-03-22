package krishagni.catissueplus.util;

import edu.wustl.common.util.logger.Logger;


public class DAOUtil
{
	private static final Logger LOGGER = Logger.getCommonLogger(DAOUtil.class);
	/**
	 * @param dao DAO object
	 * @throws BizLogicException :Generic BizLogic Exception- session not closed.
	 */
//	public static void closeDAOSession(DAO dao) throws BizLogicException
//	{
//		try
//		{
//			if(dao != null)
//			{
//				dao.closeSession();
//			}
//		}
//		catch (DAOException exception)
//		{
//			LOGGER.error("Not able to close DAO session.", exception);
//			throw new BizLogicException(exception);
//		}
//	}
//	
//	public static HibernateDAO openDAOSession(final SessionDataBean sessionDataBean)
//			throws BizLogicException
//	{
//		DAO dao = null;
//		try
//		{
//			final String applicationName = CommonServiceLocator.getInstance()
//					.getAppName();
//			dao = DAOConfigFactory.getInstance().getDAOFactory(applicationName)
//					.getDAO();
//			dao.openSession(sessionDataBean);
//		}
//		catch (final DAOException exception)
//		{
//			LOGGER.error("Not able to open DAO session.", exception);
//			throw new BizLogicException(exception);
//		}
//		return (HibernateDAO)dao;
//	}
}
