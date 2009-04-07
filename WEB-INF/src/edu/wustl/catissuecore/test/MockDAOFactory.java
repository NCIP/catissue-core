
package edu.wustl.catissuecore.test;

import edu.wustl.common.dao.DAO;
import edu.wustl.common.dao.DAOFactory;
import edu.wustl.common.dao.HibernateDAO;
import edu.wustl.common.dao.JDBCDAO;
import edu.wustl.common.util.global.Constants;

/**
 * This is a mock class for DAOFactory.This class returns mock for  HibernateDAO & JDBC DAO interface.
 * Through testcase we put DAOInterfaceMock in the MockDAOFactory.
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
   public DAO getDAO(int daoType)
    {
        DAO dao = null;
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

	
   /** 
    * This Method set the HibernateDAO
    * @param hibDAO
    */
	public void setHibernateDAO(HibernateDAO hibDAO)
	{
		this.hibDAO = hibDAO;
	}
	
	/**
	 * This Method sets the JDBCDAO
	 * 
	 * @param jdbcDAO
	 */
	public void setJDBCDAO(JDBCDAO jdbcDAO)
	{
		this.jdbcDAO = jdbcDAO;
	}
}