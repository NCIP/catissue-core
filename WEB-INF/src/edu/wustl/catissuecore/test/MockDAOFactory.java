
package edu.wustl.catissuecore.test;

import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.common.dao.AbstractDAO;
import edu.wustl.common.dao.DAOFactory;
import edu.wustl.common.dao.HibernateDAO;
import edu.wustl.common.dao.JDBCDAO;
import edu.wustl.common.util.global.Constants;

/**
 * This is a mock class for BizLogicFactory.This class returns mock for  BizLogicInterface.
 * Through testcase we put bizLogicInterfaceMock in the MockBizLogicFactory.
 * @author sujay_narkar
 *
 */
public class MockDAOFactory extends DAOFactory
{
	private HibernateDAO hibDAO;
	private JDBCDAO jdbcDAO;

	/**
	 * Empty Constructor
	 */
	public MockDAOFactory()
	{
	}

	/**
	 * This method returns particular DAO interface
	 * @param daoType type of DAO
	 * @return DAO Interface 
	 */
   public AbstractDAO getDAO(int daoType)
    {
        AbstractDAO dao = null;
        switch (daoType)
        {
            case Constants.HIBERNATE_DAO :
                dao = hibDAO;
                break;

            case Constants.JDBC_DAO :
                dao = jdbcDAO;
            default :
                break;
        }
        return dao;
    }

	
	public void setHibernateDAO(HibernateDAO hibDAO)
	{
		this.hibDAO = hibDAO;
	}
	
	public void setJDBCDAO(JDBCDAO jdbcDAO)
	{
		this.jdbcDAO = jdbcDAO;
	}
}