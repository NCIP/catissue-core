package edu.wustl.catissuecore.bizlogic.test;

import com.mockobjects.constraint.Constraint;
import com.mockobjects.constraint.IsAnything;
import com.mockobjects.constraint.IsInstanceOf;
import com.mockobjects.dynamic.FullConstraintMatcher;
import com.mockobjects.dynamic.Mock;

import edu.wustl.catissuecore.bizlogic.StorageTypeBizLogic;
import edu.wustl.catissuecore.domain.StorageType;
import edu.wustl.catissuecore.test.MockDAOFactory;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.dao.DAOFactory;
import edu.wustl.common.dao.HibernateDAO;
import edu.wustl.common.dao.JDBCDAO;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.test.BaseTestCase;
import edu.wustl.common.util.global.Constants;

public class StorageTypeBizLogicTest extends BaseTestCase
{
	
	Mock hibDAO;
	Mock jdbcDAO;

	/**
	 * Constructor for ParticipantLookupLogicTest.
	 * @param arg0 Name of the test case
	 */
	public StorageTypeBizLogicTest(String name)
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

	public void testNullInputParametersInInsert()
	{
		hibDAO.expect("closeSession");
		StorageTypeBizLogic storageTypeBizLogic = new StorageTypeBizLogic();
		try
		{
			storageTypeBizLogic.insert(new StorageType(),null,Constants.HIBERNATE_DAO);
			fail("When null sessiondataBean is passes, it should throw NullPointerException");
		}
		catch(NullPointerException e)
		{
			e.printStackTrace();
		}
		catch (BizLogicException e)
		{
			e.printStackTrace();
		}
		catch (UserNotAuthorizedException e)
		{
			e.printStackTrace();
		}
		
		hibDAO.expect("closeSession");
		
		Constraint[] constraints = {new IsInstanceOf(SessionDataBean.class)};
		FullConstraintMatcher fullConstraintMatcher = new FullConstraintMatcher(constraints);
		hibDAO.expect("openSession",fullConstraintMatcher);
		
		try
		{
			storageTypeBizLogic.insert(null,new SessionDataBean(),Constants.HIBERNATE_DAO);
			fail("When null sessiondataBean is passes, it should throw NullPointerException");
		}
		catch(NullPointerException e)
		{
			e.printStackTrace();
		}
		catch (BizLogicException e)
		{
		
			e.printStackTrace();
		}
		catch (UserNotAuthorizedException e)
		{
			e.printStackTrace();
		}
	}
	

	public void testInsertWithEmptyObject()
	{
		StorageTypeBizLogic storageTypeBizLogic = new StorageTypeBizLogic();
		
		SessionDataBean sessionDataBean = new SessionDataBean();
		Constraint[] constraints = {new IsInstanceOf(SessionDataBean.class)};
		hibDAO.expect("commit");
		FullConstraintMatcher fullConstraintMatcher = new FullConstraintMatcher(constraints);
		hibDAO.expect("openSession",fullConstraintMatcher);
		Constraint[] insertConstraints = {new IsAnything(),new IsAnything(),new IsAnything(),new IsAnything()};
		FullConstraintMatcher insertConstraintMatcher = new FullConstraintMatcher(insertConstraints);
		hibDAO.expect("insert",insertConstraintMatcher);
		hibDAO.expect("insert",insertConstraintMatcher);
		hibDAO.expect("closeSession");
		
		try
		{
			storageTypeBizLogic.insert(new StorageType(),sessionDataBean,edu.wustl.common.util.global.Constants.HIBERNATE_DAO);
			assertTrue("Storage Type inserted successfully",true);
			
		}
		catch (NullPointerException e) {
				e.printStackTrace();
				fail("Null Pointer Exception");
		}
		catch (BizLogicException e)
		{
			e.printStackTrace();
			fail(" Exception occured");
		}
		catch (UserNotAuthorizedException e)
		{
			e.printStackTrace();
			fail(" Exception occured");
		}
	}


	public void testInsert()
	{
		StorageTypeBizLogic storageTypeBizLogic = new StorageTypeBizLogic();
		
		StorageType storageType=setStorageType();
		SessionDataBean sessionDataBean = new SessionDataBean();
		Constraint[] constraints = {new IsInstanceOf(SessionDataBean.class)};
		hibDAO.expect("commit");
		FullConstraintMatcher fullConstraintMatcher = new FullConstraintMatcher(constraints);
		hibDAO.expect("openSession",fullConstraintMatcher);
		Constraint[] insertConstraints = {new IsAnything(),new IsAnything(),new IsAnything(),new IsAnything()};
		FullConstraintMatcher insertConstraintMatcher = new FullConstraintMatcher(insertConstraints);
		hibDAO.expect("insert",insertConstraintMatcher);
		hibDAO.expect("insert",insertConstraintMatcher);
		hibDAO.expect("closeSession");
		
		try
		{
			storageTypeBizLogic.insert(storageType,sessionDataBean,edu.wustl.common.util.global.Constants.HIBERNATE_DAO);
			assertTrue("Storage Type inserted successfully",true);
			
		}
		catch (NullPointerException e) {
				e.printStackTrace();
				fail("Null Pointer Exception");
		}
		catch (BizLogicException e)
		{
			e.printStackTrace();
			fail(" Exception occured");
		}
		catch (UserNotAuthorizedException e)
		{
			e.printStackTrace();
			fail(" Exception occured");
		}
	}

	

	
	public void testNullInputParametersInUpdate()
	{
		hibDAO.expect("closeSession");
		StorageTypeBizLogic storageTypeBizLogic = new StorageTypeBizLogic();
		try
		{
			storageTypeBizLogic.update(new StorageType(),null,Constants.HIBERNATE_DAO,null);
			fail("When null sessiondataBean is passes, it should throw NullPointerException");
		}
		catch(NullPointerException e)
		{
			e.printStackTrace();
		}
		catch (BizLogicException e)
		{
			e.printStackTrace();
		}
		catch (UserNotAuthorizedException e)
		{
			e.printStackTrace();
		}
		
		hibDAO.expect("closeSession");
		
		Constraint[] constraints = {new IsInstanceOf(SessionDataBean.class)};
		FullConstraintMatcher fullConstraintMatcher = new FullConstraintMatcher(constraints);
		hibDAO.expect("openSession",fullConstraintMatcher);
		
		try
		{
			storageTypeBizLogic.insert(null,new SessionDataBean(),Constants.HIBERNATE_DAO);
			fail("When null sessiondataBean is passes, it should throw NullPointerException");
		}
		catch(NullPointerException e)
		{
			e.printStackTrace();
		}
		catch (BizLogicException e)
		{
		
			e.printStackTrace();
		}
		catch (UserNotAuthorizedException e)
		{
			e.printStackTrace();
		}
	}
	
		public void testUpdateWithEmptyObject()
	{
		StorageTypeBizLogic storageTypeBizLogic = new StorageTypeBizLogic();
		
		SessionDataBean sessionDataBean = new SessionDataBean();
		Constraint[] constraints = {new IsInstanceOf(SessionDataBean.class)};
		hibDAO.expect("commit");
		FullConstraintMatcher fullConstraintMatcher = new FullConstraintMatcher(constraints);
		hibDAO.expect("openSession",fullConstraintMatcher);
		Constraint[] updateConstraints = {new IsAnything(),new IsAnything(),new IsAnything(),new IsAnything(),new IsAnything()};
		FullConstraintMatcher updateConstraintMatcher = new FullConstraintMatcher(updateConstraints);
		hibDAO.expect("update",updateConstraintMatcher);
		hibDAO.expect("update",updateConstraintMatcher);
		
		Constraint[] auditConstraints = {new IsAnything(),new IsAnything(),new IsAnything(),new IsAnything()};
		FullConstraintMatcher auditConstraintMatcher = new FullConstraintMatcher(auditConstraints);
		hibDAO.expect("audit",auditConstraintMatcher);
		hibDAO.expect("audit",auditConstraintMatcher);
		
		hibDAO.expect("closeSession");
		
		try
		{
			storageTypeBizLogic.update(new StorageType(),new StorageType(),edu.wustl.common.util.global.Constants.HIBERNATE_DAO,sessionDataBean);
			assertTrue("Storage Type updated successfully",true);
			
		}
		catch (NullPointerException e) {
			fail("Null Pointer Exception");
		}
		catch (BizLogicException e)
		{
			fail("Bizlogic Exception occured");
		}
		catch (UserNotAuthorizedException e)
		{
			fail("UserNotAuthorized Exception occured");
		}
	}

	public void testUpdate()
	{
		StorageTypeBizLogic storageTypeBizLogic = new StorageTypeBizLogic();
		
		StorageType storageType=setStorageType();
		SessionDataBean sessionDataBean = new SessionDataBean();
		Constraint[] constraints = {new IsInstanceOf(SessionDataBean.class)};
		hibDAO.expect("commit");
		FullConstraintMatcher fullConstraintMatcher = new FullConstraintMatcher(constraints);
		hibDAO.expect("openSession",fullConstraintMatcher);
		
		Constraint[] updateConstraints = {new IsAnything(),new IsAnything(),new IsAnything(),new IsAnything(),new IsAnything()};
		FullConstraintMatcher updateConstraintMatcher = new FullConstraintMatcher(updateConstraints);
		hibDAO.expect("update",updateConstraintMatcher);
		hibDAO.expect("update",updateConstraintMatcher);
		
		Constraint[] auditConstraints = {new IsAnything(),new IsAnything(),new IsAnything(),new IsAnything()};
		FullConstraintMatcher auditConstraintMatcher = new FullConstraintMatcher(auditConstraints);
		hibDAO.expect("audit",auditConstraintMatcher);
		hibDAO.expect("audit",auditConstraintMatcher);
		
		hibDAO.expect("closeSession");
			
		try
		{
			storageTypeBizLogic.update(storageType,storageType,edu.wustl.common.util.global.Constants.HIBERNATE_DAO,sessionDataBean);
			assertTrue("Storage Type updated successfully",true);
			
		}
		catch (NullPointerException e) {
				e.printStackTrace();
				fail("Null Pointer Exception");
		}
		catch (BizLogicException e)
		{
			e.printStackTrace();
			fail("Biz Logic Exception occured");
		}
		catch (UserNotAuthorizedException e)
		{
			e.printStackTrace();
			fail(" User Not Authorised Exception occured");
		}
	}

	private StorageType setStorageType()
	{
		StorageType storageType=new StorageType();
/*		storageType.setActivityStatus("Active");
		
		StorageContainerCapacity storageContCapacity=new StorageContainerCapacity();
		storageContCapacity.setOneDimensionCapacity(new Integer("1"));
		storageContCapacity.setTwoDimensionCapacity(new Integer("3"));
		
		storageType.setDefaultStorageCapacity(storageContCapacity);
		storageType.setDefaultTempratureInCentigrade(new Double("34"));
		storageType.setOneDimensionLabel("aaa");
		storageType.setTwoDimensionLabel("bbb");
		
		Collection specimenClassCollection=new HashSet();
		storageType.setSpecimenClassCollection(specimenClassCollection);
		
		Collection storageTypeCollection=new HashSet();
		storageType.setStorageTypeCollection(storageTypeCollection);
		
		storageType.setType("Freeze");*/
		return storageType;
	}
	
}
