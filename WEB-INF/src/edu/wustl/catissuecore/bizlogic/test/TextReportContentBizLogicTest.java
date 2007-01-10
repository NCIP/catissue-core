package edu.wustl.catissuecore.bizlogic.test;


import com.mockobjects.constraint.Constraint;
import com.mockobjects.constraint.IsAnything;
import com.mockobjects.constraint.IsInstanceOf;
import com.mockobjects.dynamic.FullConstraintMatcher;
import com.mockobjects.dynamic.Mock;

import edu.wustl.catissuecore.bizlogic.StorageTypeBizLogic;
import edu.wustl.catissuecore.bizlogic.TextReportContentBizLogic;
import edu.wustl.catissuecore.domain.StorageType;
import edu.wustl.catissuecore.domain.pathology.TextContent;
import edu.wustl.catissuecore.test.MockDAOFactory;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.dao.DAOFactory;
import edu.wustl.common.dao.HibernateDAO;
import edu.wustl.common.dao.JDBCDAO;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.test.BaseTestCase;
import edu.wustl.common.util.global.Constants;



public class TextReportContentBizLogicTest extends BaseTestCase
{
	Mock hibDAO;
	Mock jdbcDAO;

		/**
		 * Constructor for TextReportContentBizLogicTest.
		 * @param arg0 Name of the test case
		 */
		public TextReportContentBizLogicTest(String name)
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
			TextReportContentBizLogic textReportContentBizLogic = new TextReportContentBizLogic();
			try
			{
				textReportContentBizLogic.insert(new TextContent(),null,Constants.HIBERNATE_DAO);
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
				textReportContentBizLogic.insert(null,new SessionDataBean(),Constants.HIBERNATE_DAO);
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
			TextReportContentBizLogic textReportContentBizLogic = new TextReportContentBizLogic();
			
			SessionDataBean sessionDataBean = new SessionDataBean();
			Constraint[] constraints = {new IsInstanceOf(SessionDataBean.class)};
			hibDAO.expect("commit");
			FullConstraintMatcher fullConstraintMatcher = new FullConstraintMatcher(constraints);
			hibDAO.expect("openSession",fullConstraintMatcher);
			Constraint[] insertConstraints = {new IsAnything(),new IsAnything(),new IsAnything(),new IsAnything()};
			FullConstraintMatcher insertConstraintMatcher = new FullConstraintMatcher(insertConstraints);
			hibDAO.expect("insert",insertConstraintMatcher);
			hibDAO.expect("closeSession");
			
			try
			{
				textReportContentBizLogic.insert(new StorageType(),sessionDataBean,edu.wustl.common.util.global.Constants.HIBERNATE_DAO);
				assertTrue("Text content inserted successfully",true);
				
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
			TextReportContentBizLogic textReportContentBizLogic = new TextReportContentBizLogic();
			
			TextContent textContent=setTextReportContent();
			SessionDataBean sessionDataBean = new SessionDataBean();
			Constraint[] constraints = {new IsInstanceOf(SessionDataBean.class)};
			hibDAO.expect("commit");
			FullConstraintMatcher fullConstraintMatcher = new FullConstraintMatcher(constraints);
			hibDAO.expect("openSession",fullConstraintMatcher);
			Constraint[] insertConstraints = {new IsAnything(),new IsAnything(),new IsAnything(),new IsAnything()};
			FullConstraintMatcher insertConstraintMatcher = new FullConstraintMatcher(insertConstraints);
			hibDAO.expect("insert",insertConstraintMatcher);
			hibDAO.expect("closeSession");
			
			try
			{
				textReportContentBizLogic.insert(textContent,sessionDataBean,edu.wustl.common.util.global.Constants.HIBERNATE_DAO);
				assertTrue("Text Content inserted successfully",true);
				
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
			TextReportContentBizLogic textReportContentBizLogic = new TextReportContentBizLogic();
			try
			{
				textReportContentBizLogic.update(new TextContent(),null,Constants.HIBERNATE_DAO,null);
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
				textReportContentBizLogic.insert(null,new SessionDataBean(),Constants.HIBERNATE_DAO);
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
		
		public void testUpdate()
		{
			TextReportContentBizLogic textReportContentBizLogic = new TextReportContentBizLogic();
			
			TextContent textContent=setTextReportContent();
			SessionDataBean sessionDataBean = new SessionDataBean();
			Constraint[] constraints = {new IsInstanceOf(SessionDataBean.class)};
			hibDAO.expect("commit");
			FullConstraintMatcher fullConstraintMatcher = new FullConstraintMatcher(constraints);
			hibDAO.expect("openSession",fullConstraintMatcher);
			
			Constraint[] updateConstraints = {new IsAnything(),new IsAnything(),new IsAnything(),new IsAnything(),new IsAnything()};
			FullConstraintMatcher updateConstraintMatcher = new FullConstraintMatcher(updateConstraints);
			hibDAO.expect("update",updateConstraintMatcher);
			
			
			Constraint[] auditConstraints = {new IsAnything(),new IsAnything(),new IsAnything(),new IsAnything()};
			FullConstraintMatcher auditConstraintMatcher = new FullConstraintMatcher(auditConstraints);
			hibDAO.expect("audit",auditConstraintMatcher);
			hibDAO.expect("audit",auditConstraintMatcher);
			
			hibDAO.expect("closeSession");
				
			try
			{
				textReportContentBizLogic.update(textContent,textContent,edu.wustl.common.util.global.Constants.HIBERNATE_DAO,sessionDataBean);
				assertTrue("Text Content updated successfully",true);
				
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

		private TextContent setTextReportContent()
		{
			TextContent textContent=new TextContent();
			return textContent;
		}

}
