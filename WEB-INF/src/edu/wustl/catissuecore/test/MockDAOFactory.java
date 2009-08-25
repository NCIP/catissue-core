
package edu.wustl.catissuecore.test;

import edu.wustl.dao.DAO;
import edu.wustl.dao.HibernateDAO;
import edu.wustl.dao.JDBCDAO;
import edu.wustl.dao.daofactory.DAOFactory;

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
			case 0 :
				dao = this.hibDAO;
				break;

			case 1 :
				dao = this.jdbcDAO;
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