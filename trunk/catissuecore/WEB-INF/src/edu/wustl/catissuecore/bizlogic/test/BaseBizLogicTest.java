/*
 * <p>Title: SpecimenArrayTypeBizLogicTest Class </p>
 * <p>Description:This class is base class for all biz class test.It is used to setup initial environment.</p>
 * Copyright: Copyright (c) year 2006
 * Company: Washington University, School of Medicine, St. Louis.
 * @version 1.1
 * Created on August 31,2006
 */
package edu.wustl.catissuecore.bizlogic.test;

import com.mockobjects.dynamic.Mock;

import edu.wustl.catissuecore.test.MockDAOFactory;
import edu.wustl.common.dao.DAOFactory;
import edu.wustl.common.dao.HibernateDAO;
import edu.wustl.common.dao.JDBCDAO;
import edu.wustl.common.test.BaseTestCase;

/**
 * <p>This class is base class for all biz class test.It is used to setup initial environment.</p>
 * @author Ashwin Gupta
 * @version 1.1
 */
public class BaseBizLogicTest extends BaseTestCase {

	/**
	 * Specify the hibernateDAOMock field 
	 */
	protected Mock hibernateDAOMock;
	
	/**
	 * Specify the jdbcDAOMock field 
	 */
	protected Mock jdbcDAOMock;
	
	/**
	 * Specify the daoFactoryMock field 
	 */
	protected MockDAOFactory daoFactoryMock;

	/**
	 * Constructor for BaseBizLogicTest.
	 * @param arg0 Name of the test case
	 */
	public BaseBizLogicTest(String name)
	{
		super(name);

	}
	/**
	 * This method makes an initial set up for test cases.
	 * 
	 * JUnit calls setUp and tearDown for each test so that there can be no side effects among test runs.
	 * Here hibernateDAOMock is instantiated.As this mock depicts the behaviour of HibernateDAO,this
	 * interface is passed as an input parameter to the constructor.
	 * 
	 * The original instance of DAOFactory is replaced with an instance of MockDAOFactory.
	 * Whenever a  call to the DAOFactory is made,MockDAOFactory will come into picture.This 
	 * mock factory will return the instance of HibernateDAO/JDBCDAO.
	 */
	
	protected void setUp()
	{
		hibernateDAOMock = new Mock(HibernateDAO.class);
		jdbcDAOMock = new Mock(JDBCDAO.class);
		daoFactoryMock = new MockDAOFactory();
		daoFactoryMock.setHibernateDAO((HibernateDAO) hibernateDAOMock.proxy());
		daoFactoryMock.setJDBCDAO((JDBCDAO) jdbcDAOMock.proxy());
		DAOFactory.setDAOFactory(daoFactoryMock);
	}
	
	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		hibernateDAOMock = null;
		jdbcDAOMock = null;
		daoFactoryMock = null;
	}
	
	
}
