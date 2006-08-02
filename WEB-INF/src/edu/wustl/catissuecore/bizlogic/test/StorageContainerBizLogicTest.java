package edu.wustl.catissuecore.bizlogic.test;

import com.mockobjects.constraint.Constraint;
import com.mockobjects.constraint.IsAnything;
import com.mockobjects.constraint.IsInstanceOf;
import com.mockobjects.constraint.IsNot;
import com.mockobjects.dynamic.FullConstraintMatcher;
import com.mockobjects.dynamic.Mock;

import edu.wustl.catissuecore.bizlogic.StorageContainerBizLogic;
import edu.wustl.catissuecore.bizlogic.StorageTypeBizLogic;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.StorageType;
import edu.wustl.catissuecore.test.MockDAOFactory;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.dao.DAOFactory;
import edu.wustl.common.dao.HibernateDAO;
import edu.wustl.common.dao.JDBCDAO;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.test.BaseTestCase;


public class StorageContainerBizLogicTest extends BaseTestCase
{
	
	Mock hibDAO;
	Mock jdbcDAO;

	/**
	 * Constructor for ParticipantLookupLogicTest.
	 * @param arg0 Name of the test case
	 */
	public StorageContainerBizLogicTest(String name)
	{
		super(name);

	}
	/**
	 * This method makes an initial set up for test cases.
	 * 
	 * JUnit calls setUp and tearDown for each test so that there can be no side effects among test runs.
	 * Here bizLogicInterfaceMock is instantiated.As this mock depicts the behaviour of BizLogicInterface,this
	 * interface is passed as an input parameter to the constructor.
	 * 
	 * The original instance of BizLogicFactory is replaced with an instance of MockBizLogicFactory.
	 * Whenever a  call to the BizLogicFactory is made,MockBizLogicFactory will come into picture.This 
	 * mock factory will return the instance of bizLogicInterfaceMock.
	 */
	protected void setUp()
	{
		hibDAO = new Mock(HibernateDAO.class);
		jdbcDAO = new Mock(JDBCDAO.class);
		
		MockDAOFactory factory = new MockDAOFactory();
		factory.setHibernateDAO((HibernateDAO) hibDAO.proxy());
		factory.setJDBCDAO((JDBCDAO) jdbcDAO.proxy());
		DAOFactory.setDAOFactory(factory);

	}
	
	
	public void testNullSessionDataBeanInInsert()
	{
		StorageTypeBizLogic storageTypeBizLogic = new StorageTypeBizLogic();
		
		SessionDataBean sessionDataBean = new SessionDataBean();
		Constraint[] constraints = {new IsInstanceOf(SessionDataBean.class)};
		hibDAO.expect("commit");
		FullConstraintMatcher fullConstraintMatcher = new FullConstraintMatcher(constraints);
		hibDAO.expect("openSession",fullConstraintMatcher);
//		hibDAO.expect("closeSession");
//		hibDAO.expect("openSession");
		Constraint[] insertConstraints = {new IsAnything(),new IsAnything(),new IsAnything(),new IsAnything()};
		FullConstraintMatcher insertConstraintMatcher = new FullConstraintMatcher(insertConstraints);
		hibDAO.expect("insert",insertConstraintMatcher);
		hibDAO.expect("insert",insertConstraintMatcher);
		hibDAO.expect("closeSession");
		
		
//		Constraint[] closeConstraints = {};
//		FullConstraintMatcher closeConstraintMatcher = new FullConstraintMatcher(closeConstraints);
//		hibDAO.expect("closeSession",closeConstraintMatcher);
	
		try
		{
			storageTypeBizLogic.insert(new StorageType(),sessionDataBean,edu.wustl.common.util.global.Constants.HIBERNATE_DAO);
//			fail(" Session data is null therefore insert should fail");
		}
		catch (NullPointerException e) {
				assertTrue("SessionData bean is null",true);
				e.printStackTrace();
		}
		catch (BizLogicException e)
		{
			fail(" Exception occured");
			e.printStackTrace();
		}
		catch (UserNotAuthorizedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
