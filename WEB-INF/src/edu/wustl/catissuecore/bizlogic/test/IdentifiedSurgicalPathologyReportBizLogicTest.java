package edu.wustl.catissuecore.bizlogic.test;

import com.mockobjects.constraint.Constraint;
import com.mockobjects.constraint.IsAnything;
import com.mockobjects.constraint.IsInstanceOf;
import com.mockobjects.dynamic.FullConstraintMatcher;
import com.mockobjects.dynamic.Mock;

import edu.wustl.catissuecore.bizlogic.IdentifiedSurgicalPathologyReportBizLogic;
import edu.wustl.catissuecore.domain.pathology.IdentifiedSurgicalPathologyReport;
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


public class IdentifiedSurgicalPathologyReportBizLogicTest extends BaseTestCase
{
		Mock hibDAO;
		Mock jdbcDAO;

			/**
			 * Constructor for IdentifiedSurgicalPathologyReportBizLogicTest.
			 * @param arg0 Name of the test case
			 */
			public IdentifiedSurgicalPathologyReportBizLogicTest(String name)
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

			public void testPathRptLoadBizLogicNullInputParametersInInsert()
			{
				hibDAO.expect("closeSession");
				IdentifiedSurgicalPathologyReportBizLogic identifiedSurgicalPathologyReportBizLogic = new IdentifiedSurgicalPathologyReportBizLogic();
				try
				{
					identifiedSurgicalPathologyReportBizLogic.insert(new IdentifiedSurgicalPathologyReport(),null,Constants.HIBERNATE_DAO);
					fail("When null sessiondataBean is passes, it should throw NullPointerException");
				}
				catch(NullPointerException e)
				{
					e.printStackTrace();
					assertTrue("When null sessiondataBean is passes, it throws NullPointerException",true);
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
					identifiedSurgicalPathologyReportBizLogic.insert(null,new SessionDataBean(),Constants.HIBERNATE_DAO);
					fail("When null sessiondataBean is passes, it should throw NullPointerException");
				}
				catch(NullPointerException e)
				{
					e.printStackTrace();
					assertTrue("When null sessiondataBean is passes, it throws NullPointerException",true);
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
			

			public void testPathRptLoadBizLogicInsertWithEmptyObject()
			{
				IdentifiedSurgicalPathologyReportBizLogic identifiedSurgicalPathologyReportBizLogic = new IdentifiedSurgicalPathologyReportBizLogic();
				
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
					identifiedSurgicalPathologyReportBizLogic.insert(new IdentifiedSurgicalPathologyReport(),sessionDataBean,edu.wustl.common.util.global.Constants.HIBERNATE_DAO);
					fail("Identified report  inserted successfully");
					
				}
				catch (NullPointerException e) {
						assertTrue("Null Pointer Exception",true);
				}
				catch (BizLogicException e)
				{
					fail(" Exception occured");
				}
				catch (UserNotAuthorizedException e)
				{
					fail(" Exception occured");
				}
			}


			public void testPathRptLoadBizLogicInsert()
			{
				IdentifiedSurgicalPathologyReportBizLogic identifiedSurgicalPathologyReportBizLogic = new IdentifiedSurgicalPathologyReportBizLogic();
				
				IdentifiedSurgicalPathologyReport identifiedSurgicalPathologyReport=setIdentifiedReport();
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
					identifiedSurgicalPathologyReportBizLogic.insert(identifiedSurgicalPathologyReport,sessionDataBean,edu.wustl.common.util.global.Constants.HIBERNATE_DAO);
					fail("Identified report inserted successfully");
					
				}
				catch (NullPointerException e) {
						assertTrue("Null Pointer Exception",true);
				}
				catch (BizLogicException e)
				{
					fail(" Exception occured");
				}
				catch (UserNotAuthorizedException e)
				{
					fail(" Exception occured");
				}
			}

			

			
			public void testPathRptLoadBizLogicNullInputParametersInUpdate()
			{
				hibDAO.expect("closeSession");
				IdentifiedSurgicalPathologyReportBizLogic identifiedSurgicalPathologyReportBizLogic = new IdentifiedSurgicalPathologyReportBizLogic();
				try
				{
					identifiedSurgicalPathologyReportBizLogic.update(new TextContent(),null,Constants.HIBERNATE_DAO,null);
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
					identifiedSurgicalPathologyReportBizLogic.insert(null,new SessionDataBean(),Constants.HIBERNATE_DAO);
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
			
			public void testPathRptLoadBizLogicUpdate()
			{
				IdentifiedSurgicalPathologyReportBizLogic identifiedSurgicalPathologyReportBizLogic = new IdentifiedSurgicalPathologyReportBizLogic();
				
				IdentifiedSurgicalPathologyReport identifiedSurgicalPathologyReport=setIdentifiedReport();
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
					identifiedSurgicalPathologyReportBizLogic.update(identifiedSurgicalPathologyReport,identifiedSurgicalPathologyReport,edu.wustl.common.util.global.Constants.HIBERNATE_DAO,sessionDataBean);
					fail("identified pathology report successfully");
					
				}
				catch (NullPointerException e) 
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

			private IdentifiedSurgicalPathologyReport setIdentifiedReport()
			{
				IdentifiedSurgicalPathologyReport identifiedSurgicalPathologyReport=new IdentifiedSurgicalPathologyReport();
				return identifiedSurgicalPathologyReport;
			}

}



