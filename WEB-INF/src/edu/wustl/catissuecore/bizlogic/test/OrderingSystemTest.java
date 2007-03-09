/**
 * 
 */
package edu.wustl.catissuecore.bizlogic.test;

import java.text.ParseException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import com.mockobjects.constraint.Constraint;
import com.mockobjects.constraint.IsAnything;
import com.mockobjects.constraint.IsInstanceOf;
import com.mockobjects.dynamic.FullConstraintMatcher;
import com.mockobjects.dynamic.Mock;

import edu.wustl.catissuecore.bizlogic.OrderBizLogic;
import edu.wustl.catissuecore.domain.DerivedSpecimenOrderItem;
import edu.wustl.catissuecore.domain.DistributionProtocol;
import edu.wustl.catissuecore.domain.ExistingSpecimenArrayOrderItem;
import edu.wustl.catissuecore.domain.ExistingSpecimenOrderItem;
import edu.wustl.catissuecore.domain.NewSpecimenArrayOrderItem;
import edu.wustl.catissuecore.domain.OrderDetails;
import edu.wustl.catissuecore.domain.OrderItem;
import edu.wustl.catissuecore.domain.PathologicalCaseOrderItem;
import edu.wustl.catissuecore.domain.Quantity;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenArray;
import edu.wustl.catissuecore.domain.SpecimenArrayType;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.SpecimenOrderItem;
import edu.wustl.catissuecore.test.MockDAOFactory;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.dao.DAOFactory;
import edu.wustl.common.dao.HibernateDAO;
import edu.wustl.common.dao.JDBCDAO;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.test.BaseTestCase;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.global.Constants;
import edu.wustl.common.util.logger.Logger;



/**
 * @author ashish_gupta
 *
 */
public class OrderingSystemTest extends BaseTestCase
{
	Mock hibDAO;
	Mock jdbcDAO;
	/**
	 * @param name
	 */
	public OrderingSystemTest(String name)
	{
		super(name);
	}

	/* (non-Javadoc)
	 * @see edu.wustl.common.test.BaseTestCase#setUp()
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
	/**
	 * 
	 */
	private void initJunitForInsertArguments()
	{
		hibDAO.expect("closeSession");		
		Constraint[] constraints = {new IsInstanceOf(SessionDataBean.class)};
		FullConstraintMatcher fullConstraintMatcher = new FullConstraintMatcher(constraints);
		hibDAO.expect("openSession",fullConstraintMatcher);		
	}
	/**
	 * 
	 */
	public void testNullDomainObjectInInsert()
	{
		OrderBizLogic orderBizLogic = new OrderBizLogic();
		initJunitForInsertArguments();
		try
		{
			orderBizLogic.insert(null,new SessionDataBean(),Constants.HIBERNATE_DAO);
			fail("When null collection protocol object is passed , it should throw NullPointerException");
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
	/**
	 * 
	 */
	public void testNullSessionDtatBeanInInsert()
	{
		OrderBizLogic orderBizLogic = new OrderBizLogic();
		initJunitForInsertArguments();
		try
		{
			orderBizLogic.insert(new OrderDetails(),null,Constants.HIBERNATE_DAO);
			fail("When null sessiondataBean is passed, it should throw NullPointerException");
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
	/**
	 * 
	 */
	public void testWrongDaoTypeInInsert()
	{
		OrderBizLogic orderBizLogic = new OrderBizLogic();
		initJunitForInsertArguments();
		try
		{
			orderBizLogic.insert(new OrderDetails(),new SessionDataBean(),0);
			fail("When null sessiondataBean is passed, it should throw NullPointerException");
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
	/**
	 * 
	 */
	private void initJunitForInsert()
	{
		Constraint[] constraints = {new IsInstanceOf(SessionDataBean.class)};
		hibDAO.expect("commit");
		FullConstraintMatcher fullConstraintMatcher = new FullConstraintMatcher(constraints);
		hibDAO.expect("openSession",fullConstraintMatcher);
		Constraint[] insertConstraints = {new IsAnything(),new IsAnything(),new IsAnything(),new IsAnything()};
		FullConstraintMatcher insertConstraintMatcher = new FullConstraintMatcher(insertConstraints);
		hibDAO.expect("insert",insertConstraintMatcher);
		Constraint[] retrieveConstraints = {new IsAnything(),new IsAnything(),new IsAnything()};
		FullConstraintMatcher retrieveConstraintMatcher = new FullConstraintMatcher(retrieveConstraints);
		
		hibDAO.expect("retrieve",retrieveConstraintMatcher);
		hibDAO.expect("closeSession");
		
	}
	/**
	 * 
	 */
	public void testInsertWithEmptyObject()
	{
		OrderBizLogic orderBizLogic = new OrderBizLogic();
		initJunitForInsert();		
		
		try
		{
			orderBizLogic.insert(new OrderDetails(),new SessionDataBean(),edu.wustl.common.util.global.Constants.HIBERNATE_DAO);
			assertTrue("Order Details inserted successfully",true);			
		}
		catch (NullPointerException e)
		{
			e.printStackTrace();				
		}
		catch (BizLogicException e)
		{
			e.printStackTrace();
			fail(" BizLogicException occured");
		}
		catch (UserNotAuthorizedException e)
		{
			e.printStackTrace();
			fail(" UserNotAuthorizedException occured");
		}
	}
	/**
	 * 
	 */
	public void testInsertForExistingSpecimensWithNullDistributionProtocol()
	{
		OrderDetails orderDetails = initOrder();
		OrderDetails order = initOrderForExistingBioSpecimens(orderDetails);
		order.setDistributionProtocol(null);
		
		OrderBizLogic orderBizLogic = new OrderBizLogic();
		initJunitForInsert();		
		
		try
		{
			orderBizLogic.insert(order,new SessionDataBean(),edu.wustl.common.util.global.Constants.HIBERNATE_DAO);
			assertTrue("Order Details inserted successfully",true);	
		}
		catch (NullPointerException e)
		{
			e.printStackTrace();
			fail("Null Pointer Exception thrown");
		}
		catch (BizLogicException e)
		{
			e.printStackTrace();
			fail(" BizLogicException occured : Distribution Protocol is mandatory");
		}
		catch (UserNotAuthorizedException e)
		{
			e.printStackTrace();
			fail(" UserNotAuthorizedException occured");
		}
	}
	/**
	 * 
	 */
	public void testInsertForExistingSpecimensWithNullQuantity()
	{
		OrderDetails orderDetails = initOrder();
		OrderDetails order = initOrderForExistingBioSpecimens(orderDetails);
		Collection orderItemColl = order.getOrderItemCollection();
		//Setting null quantity
		if(orderItemColl != null)
		{
			Iterator<OrderItem> iter = orderItemColl.iterator();
			while(iter.hasNext())
			{
				OrderItem orderItem = (OrderItem)iter.next();
				orderItem.setRequestedQuantity(null);
			}
		}
					
		OrderBizLogic orderBizLogic = new OrderBizLogic();
		initJunitForInsert();		
		
		try
		{
			orderBizLogic.insert(order,new SessionDataBean(),edu.wustl.common.util.global.Constants.HIBERNATE_DAO);
			assertTrue("Order Details inserted successfully",true);			
		}
		catch (NullPointerException e)
		{
			e.printStackTrace();
			fail("Null Pointer Exception thrown");
		}
		catch (BizLogicException e)
		{
			e.printStackTrace();
			fail(" BizLogicException occured:: Quantity is required");
		}
		catch (UserNotAuthorizedException e)
		{
			e.printStackTrace();
			fail(" UserNotAuthorizedException occured");
		}
	}
	/**
	 * 
	 */
	public void testInsertForExistingSpecimensWithNonNumericQuantity()
	{
		OrderDetails orderDetails = initOrder();
		OrderDetails order = initOrderForExistingBioSpecimens(orderDetails);
		Collection orderItemColl = order.getOrderItemCollection();
		//Setting null quantity
		if(orderItemColl != null)
		{
			Iterator<OrderItem> iter = orderItemColl.iterator();
			while(iter.hasNext())
			{
				OrderItem orderItem = (OrderItem)iter.next();
				try
				{
					orderItem.setRequestedQuantity(new Quantity("abc"));
				}
				catch (NumberFormatException e)
				{
					e.printStackTrace();
					assertTrue(" Quantity should be numeric",true);
				}
			}
		}
					
		OrderBizLogic orderBizLogic = new OrderBizLogic();
		initJunitForInsert();		
		
		try
		{
			orderBizLogic.insert(order,new SessionDataBean(),edu.wustl.common.util.global.Constants.HIBERNATE_DAO);
			assertTrue("Order Details inserted successfully",true);			
		}		
		catch (NullPointerException e)
		{
			e.printStackTrace();
			fail("Null Pointer Exception thrown");
		}
		catch (BizLogicException e)
		{
			e.printStackTrace();
			fail(" BizLogicException occured");
		}
		catch (UserNotAuthorizedException e)
		{
			e.printStackTrace();
			fail(" UserNotAuthorizedException occured");
		}
		
	}
	/**
	 * 
	 */
	public void testInsertForExistingSpecimens()
	{
		OrderBizLogic orderBizLogic = new OrderBizLogic();
		OrderDetails orderDetails = initOrder();
		OrderDetails order = initOrderForExistingBioSpecimens(orderDetails);
		
		SessionDataBean sessionDataBean = new SessionDataBean();
		initJunitForInsert();		
		try
		{
			orderBizLogic.insert(order,sessionDataBean,edu.wustl.common.util.global.Constants.HIBERNATE_DAO);
			assertTrue("Order Details inserted successfully",true);			
		}
		catch (NullPointerException e)
		{
				e.printStackTrace();
				fail("Null Pointer Exception");
		}
		catch (BizLogicException e)
		{
			e.printStackTrace();
			fail(" BizLogicException Exception occured");
		}
		catch (UserNotAuthorizedException e)
		{
			e.printStackTrace();
			fail("UserNotAuthorizedException Exception occured");
		}
	}
	/**
	 * 
	 */
	public void testInsertForDerivedSpecimens()
	{
		OrderBizLogic orderBizLogic = new OrderBizLogic();
		OrderDetails orderDetails = initOrder();
		OrderDetails order = initOrderForDerivedBioSpecimens(orderDetails);
		
		SessionDataBean sessionDataBean = new SessionDataBean();
		initJunitForInsert();		
		try
		{
			orderBizLogic.insert(order,sessionDataBean,edu.wustl.common.util.global.Constants.HIBERNATE_DAO);
			assertTrue("Order Details inserted successfully",true);			
		}
		catch (NullPointerException e)
		{
				e.printStackTrace();
				fail("Null Pointer Exception");
		}
		catch (BizLogicException e)
		{
			e.printStackTrace();
			fail(" BizLogicException Exception occured");
		}
		catch (UserNotAuthorizedException e)
		{
			e.printStackTrace();
			fail("UserNotAuthorizedException Exception occured");
		}
	}
	/**
	 * 
	 */
	public void testInsertForDerivedSpecimensWithNullDistributionProtocol()
	{
		OrderDetails orderDetails = initOrder();
		OrderDetails order = initOrderForDerivedBioSpecimens(orderDetails);
		order.setDistributionProtocol(null);
		
		OrderBizLogic orderBizLogic = new OrderBizLogic();
		initJunitForInsert();		
		
		try
		{
			orderBizLogic.insert(order,new SessionDataBean(),edu.wustl.common.util.global.Constants.HIBERNATE_DAO);
			assertTrue("Order Details inserted successfully",true);	
		}
		catch (NullPointerException e)
		{
			e.printStackTrace();
			fail("Null Pointer Exception thrown");
		}
		catch (BizLogicException e)
		{
			e.printStackTrace();
			fail(" BizLogicException occured : Distribution Protocol is mandatory");
		}
		catch (UserNotAuthorizedException e)
		{
			e.printStackTrace();
			fail(" UserNotAuthorizedException occured");
		}
	}
	/**
	 * 
	 */
	public void testInsertForDerivedSpecimensWithNullQuantity()
	{
		OrderDetails orderDetails = initOrder();
		OrderDetails order = initOrderForDerivedBioSpecimens(orderDetails);
		Collection orderItemColl = order.getOrderItemCollection();
		//Setting null quantity
		if(orderItemColl != null)
		{
			Iterator<OrderItem> iter = orderItemColl.iterator();
			while(iter.hasNext())
			{
				OrderItem orderItem = (OrderItem)iter.next();
				orderItem.setRequestedQuantity(null);
			}
		}
					
		OrderBizLogic orderBizLogic = new OrderBizLogic();
		initJunitForInsert();		
		
		try
		{
			orderBizLogic.insert(order,new SessionDataBean(),edu.wustl.common.util.global.Constants.HIBERNATE_DAO);
			assertTrue("Order Details inserted successfully",true);			
		}
		catch (NullPointerException e)
		{
			e.printStackTrace();
			fail("Null Pointer Exception thrown");
		}
		catch (BizLogicException e)
		{
			e.printStackTrace();
			fail(" BizLogicException occured:: Quantity is required");
		}
		catch (UserNotAuthorizedException e)
		{
			e.printStackTrace();
			fail(" UserNotAuthorizedException occured");
		}
	}
	/**
	 * 
	 */
	public void testInsertForDerivedSpecimensWithNullSpecimenClass()
	{
		OrderDetails orderDetails = initOrder();
		OrderDetails order = initOrderForDerivedBioSpecimens(orderDetails);
		Collection orderItemColl = order.getOrderItemCollection();
		//Setting null quantity
		if(orderItemColl != null)
		{
			Iterator<OrderItem> iter = orderItemColl.iterator();
			while(iter.hasNext())
			{
				DerivedSpecimenOrderItem orderItem = (DerivedSpecimenOrderItem)iter.next();
				orderItem.setSpecimenClass(null);
			}
		}
					
		OrderBizLogic orderBizLogic = new OrderBizLogic();
		initJunitForInsert();		
		
		try
		{
			orderBizLogic.insert(order,new SessionDataBean(),edu.wustl.common.util.global.Constants.HIBERNATE_DAO);
			assertTrue("Order Details inserted successfully",true);			
		}
		catch (NullPointerException e)
		{
			e.printStackTrace();
			fail("Null Pointer Exception thrown");
		}
		catch (BizLogicException e)
		{
			e.printStackTrace();
			fail(" BizLogicException occured:: Quantity is required");
		}
		catch (UserNotAuthorizedException e)
		{
			e.printStackTrace();
			fail(" UserNotAuthorizedException occured");
		}
	}
	/**
	 * 
	 */
	public void testInsertForDerivedSpecimensWithNullSpecimenType()
	{
		OrderDetails orderDetails = initOrder();
		OrderDetails order = initOrderForDerivedBioSpecimens(orderDetails);
		Collection orderItemColl = order.getOrderItemCollection();
		//Setting null quantity
		if(orderItemColl != null)
		{
			Iterator<OrderItem> iter = orderItemColl.iterator();
			while(iter.hasNext())
			{
				DerivedSpecimenOrderItem orderItem = (DerivedSpecimenOrderItem)iter.next();
				orderItem.setSpecimenType(null);
			}
		}
					
		OrderBizLogic orderBizLogic = new OrderBizLogic();
		initJunitForInsert();		
		
		try
		{
			orderBizLogic.insert(order,new SessionDataBean(),edu.wustl.common.util.global.Constants.HIBERNATE_DAO);
			assertTrue("Order Details inserted successfully",true);			
		}
		catch (NullPointerException e)
		{
			e.printStackTrace();
			fail("Null Pointer Exception thrown");
		}
		catch (BizLogicException e)
		{
			e.printStackTrace();
			fail(" BizLogicException occured:: Quantity is required");
		}
		catch (UserNotAuthorizedException e)
		{
			e.printStackTrace();
			fail(" UserNotAuthorizedException occured");
		}
	}
	/**
	 * 
	 */
	public void testInsertForDerivedSpecimensWithNonNumericQuantity()
	{
		OrderDetails orderDetails = initOrder();
		OrderDetails order = initOrderForDerivedBioSpecimens(orderDetails);
		Collection orderItemColl = order.getOrderItemCollection();
		//Setting null quantity
		if(orderItemColl != null)
		{
			Iterator<OrderItem> iter = orderItemColl.iterator();
			while(iter.hasNext())
			{
				OrderItem orderItem = (OrderItem)iter.next();
				try
				{
					orderItem.setRequestedQuantity(new Quantity("abc"));
				}
				catch (NumberFormatException e)
				{
					e.printStackTrace();
					assertTrue(" Quantity should be numeric",true);
				}
			}
		}
					
		OrderBizLogic orderBizLogic = new OrderBizLogic();
		initJunitForInsert();		
		
		try
		{
			orderBizLogic.insert(order,new SessionDataBean(),edu.wustl.common.util.global.Constants.HIBERNATE_DAO);
			assertTrue("Order Details inserted successfully",true);			
		}		
		catch (NullPointerException e)
		{
			e.printStackTrace();
			fail("Null Pointer Exception thrown");
		}
		catch (BizLogicException e)
		{
			e.printStackTrace();
			fail(" BizLogicException occured");
		}
		catch (UserNotAuthorizedException e)
		{
			e.printStackTrace();
			fail(" UserNotAuthorizedException occured");
		}
		
	}
	/**
	 * 
	 */
	public void testInsertForPathoCase()
	{
		OrderBizLogic orderBizLogic = new OrderBizLogic();
		OrderDetails orderDetails = initOrder();
		OrderDetails order = initOrderForPathoCase(orderDetails);
		
		SessionDataBean sessionDataBean = new SessionDataBean();
		initJunitForInsert();		
		try
		{
			orderBizLogic.insert(order,sessionDataBean,edu.wustl.common.util.global.Constants.HIBERNATE_DAO);
			assertTrue("Order Details inserted successfully",true);			
		}
		catch (NullPointerException e)
		{
				e.printStackTrace();
				fail("Null Pointer Exception");
		}
		catch (BizLogicException e)
		{
			e.printStackTrace();
			fail(" BizLogicException Exception occured");
		}
		catch (UserNotAuthorizedException e)
		{
			e.printStackTrace();
			fail("UserNotAuthorizedException Exception occured");
		}
	}
	/**
	 * 
	 */
	public void testInsertForPathoCaseWithNullDistributionProtocol()
	{
		OrderDetails orderDetails = initOrder();
		OrderDetails order = initOrderForPathoCase(orderDetails);
		order.setDistributionProtocol(null);
		
		OrderBizLogic orderBizLogic = new OrderBizLogic();
		initJunitForInsert();		
		
		try
		{
			orderBizLogic.insert(order,new SessionDataBean(),edu.wustl.common.util.global.Constants.HIBERNATE_DAO);
			assertTrue("Order Details inserted successfully",true);	
		}
		catch (NullPointerException e)
		{
			e.printStackTrace();
			fail("Null Pointer Exception thrown");
		}
		catch (BizLogicException e)
		{
			e.printStackTrace();
			fail(" BizLogicException occured : Distribution Protocol is mandatory");
		}
		catch (UserNotAuthorizedException e)
		{
			e.printStackTrace();
			fail(" UserNotAuthorizedException occured");
		}
	}
	/**
	 * 
	 */
	public void testInsertForPathoCaseWithNullQuantity()
	{
		OrderDetails orderDetails = initOrder();
		OrderDetails order = initOrderForPathoCase(orderDetails);
		Collection orderItemColl = order.getOrderItemCollection();
		//Setting null quantity
		if(orderItemColl != null)
		{
			Iterator<OrderItem> iter = orderItemColl.iterator();
			while(iter.hasNext())
			{
				OrderItem orderItem = (OrderItem)iter.next();
				orderItem.setRequestedQuantity(null);
			}
		}
					
		OrderBizLogic orderBizLogic = new OrderBizLogic();
		initJunitForInsert();		
		
		try
		{
			orderBizLogic.insert(order,new SessionDataBean(),edu.wustl.common.util.global.Constants.HIBERNATE_DAO);
			assertTrue("Order Details inserted successfully",true);			
		}
		catch (NullPointerException e)
		{
			e.printStackTrace();
			fail("Null Pointer Exception thrown");
		}
		catch (BizLogicException e)
		{
			e.printStackTrace();
			fail(" BizLogicException occured:: Quantity is required");
		}
		catch (UserNotAuthorizedException e)
		{
			e.printStackTrace();
			fail(" UserNotAuthorizedException occured");
		}
	}
	/**
	 * 
	 */
	public void testInsertForPathoCaseWithNonNumericQuantity()
	{
		OrderDetails orderDetails = initOrder();
		OrderDetails order = initOrderForPathoCase(orderDetails);
		Collection orderItemColl = order.getOrderItemCollection();
		//Setting null quantity
		if(orderItemColl != null)
		{
			Iterator<OrderItem> iter = orderItemColl.iterator();
			while(iter.hasNext())
			{
				OrderItem orderItem = (OrderItem)iter.next();
				try
				{
					orderItem.setRequestedQuantity(new Quantity("abc"));
				}
				catch (NumberFormatException e)
				{
					e.printStackTrace();
					assertTrue(" Quantity should be numeric",true);
				}
			}
		}
					
		OrderBizLogic orderBizLogic = new OrderBizLogic();
		initJunitForInsert();		
		
		try
		{
			orderBizLogic.insert(order,new SessionDataBean(),edu.wustl.common.util.global.Constants.HIBERNATE_DAO);
			assertTrue("Order Details inserted successfully",true);			
		}		
		catch (NullPointerException e)
		{
			e.printStackTrace();
			fail("Null Pointer Exception thrown");
		}
		catch (BizLogicException e)
		{
			e.printStackTrace();
			fail(" BizLogicException occured");
		}
		catch (UserNotAuthorizedException e)
		{
			e.printStackTrace();
			fail(" UserNotAuthorizedException occured");
		}
		
	}
	/**
	 * 
	 */
	public void testInsertForPathoCaseWithNullPathologicalStatus()
	{
		OrderDetails orderDetails = initOrder();
		OrderDetails order = initOrderForPathoCase(orderDetails);
		
		Collection orderItemColl = order.getOrderItemCollection();		
		if(orderItemColl != null)
		{
			Iterator<OrderItem> iter = orderItemColl.iterator();
			while(iter.hasNext())
			{
				PathologicalCaseOrderItem orderItem = (PathologicalCaseOrderItem)iter.next();				
				orderItem.setPathologicalStatus(null);				
			}
		}
		
		OrderBizLogic orderBizLogic = new OrderBizLogic();
		initJunitForInsert();		
		
		try
		{
			orderBizLogic.insert(order,new SessionDataBean(),edu.wustl.common.util.global.Constants.HIBERNATE_DAO);
			assertTrue("Order Details inserted successfully",true);	
		}
		catch (NullPointerException e)
		{
			e.printStackTrace();
			fail("Null Pointer Exception thrown");
		}
		catch (BizLogicException e)
		{
			e.printStackTrace();
			fail(" BizLogicException occured : Distribution Protocol is mandatory");
		}
		catch (UserNotAuthorizedException e)
		{
			e.printStackTrace();
			fail(" UserNotAuthorizedException occured");
		}
	}
	/**
	 * 
	 */
	public void testInsertForPathoCaseWithNullTissueSite()
	{
		OrderDetails orderDetails = initOrder();
		OrderDetails order = initOrderForPathoCase(orderDetails);
		
		Collection orderItemColl = order.getOrderItemCollection();		
		if(orderItemColl != null)
		{
			Iterator<OrderItem> iter = orderItemColl.iterator();
			while(iter.hasNext())
			{
				PathologicalCaseOrderItem orderItem = (PathologicalCaseOrderItem)iter.next();				
				orderItem.setTissueSite(null);				
			}
		}
		
		OrderBizLogic orderBizLogic = new OrderBizLogic();
		initJunitForInsert();		
		
		try
		{
			orderBizLogic.insert(order,new SessionDataBean(),edu.wustl.common.util.global.Constants.HIBERNATE_DAO);
			assertTrue("Order Details inserted successfully",true);	
		}
		catch (NullPointerException e)
		{
			e.printStackTrace();
			fail("Null Pointer Exception thrown");
		}
		catch (BizLogicException e)
		{
			e.printStackTrace();
			fail(" BizLogicException occured : Distribution Protocol is mandatory");
		}
		catch (UserNotAuthorizedException e)
		{
			e.printStackTrace();
			fail(" UserNotAuthorizedException occured");
		}
	}
	/**
	 * 
	 */
	public void testInsertForExistingArray()
	{
		OrderBizLogic orderBizLogic = new OrderBizLogic();
		OrderDetails orderDetails = initOrder();
		OrderDetails order = initOrderForExistingSpecimenArrayOrderItem(orderDetails);
		
		SessionDataBean sessionDataBean = new SessionDataBean();
		initJunitForInsert();		
		try
		{
			orderBizLogic.insert(order,sessionDataBean,edu.wustl.common.util.global.Constants.HIBERNATE_DAO);
			assertTrue("Order Details inserted successfully",true);			
		}
		catch (NullPointerException e)
		{
				e.printStackTrace();
				fail("Null Pointer Exception");
		}
		catch (BizLogicException e)
		{
			e.printStackTrace();
			fail(" BizLogicException Exception occured");
		}
		catch (UserNotAuthorizedException e)
		{
			e.printStackTrace();
			fail("UserNotAuthorizedException Exception occured");
		}
	}
	/**
	 * 
	 */
	public void testInsertForExistingArrayWithNullDistributionProtocol()
	{
		OrderDetails orderDetails = initOrder();
		OrderDetails order = initOrderForExistingSpecimenArrayOrderItem(orderDetails);
		order.setDistributionProtocol(null);
		
		OrderBizLogic orderBizLogic = new OrderBizLogic();
		initJunitForInsert();		
		
		try
		{
			orderBizLogic.insert(order,new SessionDataBean(),edu.wustl.common.util.global.Constants.HIBERNATE_DAO);
			assertTrue("Order Details inserted successfully",true);	
		}
		catch (NullPointerException e)
		{
			e.printStackTrace();
			fail("Null Pointer Exception thrown");
		}
		catch (BizLogicException e)
		{
			e.printStackTrace();
			fail(" BizLogicException occured : Distribution Protocol is mandatory");
		}
		catch (UserNotAuthorizedException e)
		{
			e.printStackTrace();
			fail(" UserNotAuthorizedException occured");
		}
	}
	/**
	 * 
	 */
	public void testInsertForCaseWithNullQuantity()
	{
		OrderDetails orderDetails = initOrder();
		OrderDetails order = initOrderForExistingSpecimenArrayOrderItem(orderDetails);
		Collection orderItemColl = order.getOrderItemCollection();
		//Setting null quantity
		if(orderItemColl != null)
		{
			Iterator<OrderItem> iter = orderItemColl.iterator();
			while(iter.hasNext())
			{
				OrderItem orderItem = (OrderItem)iter.next();
				orderItem.setRequestedQuantity(null);
			}
		}
					
		OrderBizLogic orderBizLogic = new OrderBizLogic();
		initJunitForInsert();		
		
		try
		{
			orderBizLogic.insert(order,new SessionDataBean(),edu.wustl.common.util.global.Constants.HIBERNATE_DAO);
			assertTrue("Order Details inserted successfully",true);			
		}
		catch (NullPointerException e)
		{
			e.printStackTrace();
			fail("Null Pointer Exception thrown");
		}
		catch (BizLogicException e)
		{
			e.printStackTrace();
			fail(" BizLogicException occured:: Quantity is required");
		}
		catch (UserNotAuthorizedException e)
		{
			e.printStackTrace();
			fail(" UserNotAuthorizedException occured");
		}
	}
	/**
	 * 
	 */
	public void testInsertForExistingArrayWithNonNumericQuantity()
	{
		OrderDetails orderDetails = initOrder();
		OrderDetails order = initOrderForExistingSpecimenArrayOrderItem(orderDetails);
		Collection orderItemColl = order.getOrderItemCollection();
		//Setting null quantity
		if(orderItemColl != null)
		{
			Iterator<OrderItem> iter = orderItemColl.iterator();
			while(iter.hasNext())
			{
				OrderItem orderItem = (OrderItem)iter.next();
				try
				{
					orderItem.setRequestedQuantity(new Quantity("abc"));
				}
				catch (NumberFormatException e)
				{
					e.printStackTrace();
					assertTrue(" Quantity should be numeric",true);
				}
			}
		}
					
		OrderBizLogic orderBizLogic = new OrderBizLogic();
		initJunitForInsert();		
		
		try
		{
			orderBizLogic.insert(order,new SessionDataBean(),edu.wustl.common.util.global.Constants.HIBERNATE_DAO);
			assertTrue("Order Details inserted successfully",true);			
		}		
		catch (NullPointerException e)
		{
			e.printStackTrace();
			fail("Null Pointer Exception thrown");
		}
		catch (BizLogicException e)
		{
			e.printStackTrace();
			fail(" BizLogicException occured");
		}
		catch (UserNotAuthorizedException e)
		{
			e.printStackTrace();
			fail(" UserNotAuthorizedException occured");
		}
		
	}
	/**
	 * 
	 */
	public void testInsertForNewArray()
	{
		OrderBizLogic orderBizLogic = new OrderBizLogic();
		OrderDetails orderDetails = initOrder();
		OrderDetails order = initOrderForNewSpecimenArrayOrderItem(orderDetails);
		
		SessionDataBean sessionDataBean = new SessionDataBean();
		initJunitForInsert();		
		try
		{
			orderBizLogic.insert(order,sessionDataBean,edu.wustl.common.util.global.Constants.HIBERNATE_DAO);
			assertTrue("Order Details inserted successfully",true);			
		}
		catch (NullPointerException e)
		{
				e.printStackTrace();
				fail("Null Pointer Exception");
		}
		catch (BizLogicException e)
		{
			e.printStackTrace();
			fail(" BizLogicException Exception occured");
		}
		catch (UserNotAuthorizedException e)
		{
			e.printStackTrace();
			fail("UserNotAuthorizedException Exception occured");
		}
	}
	/**
	 * 
	 */
	public void testInsertForNewArrayWithNullDistributionProtocol()
	{
		OrderDetails orderDetails = initOrder();
		OrderDetails order = initOrderForNewSpecimenArrayOrderItem(orderDetails);
		order.setDistributionProtocol(null);
		
		OrderBizLogic orderBizLogic = new OrderBizLogic();
		initJunitForInsert();		
		
		try
		{
			orderBizLogic.insert(order,new SessionDataBean(),edu.wustl.common.util.global.Constants.HIBERNATE_DAO);
			assertTrue("Order Details inserted successfully",true);	
		}
		catch (NullPointerException e)
		{
			e.printStackTrace();
			fail("Null Pointer Exception thrown");
		}
		catch (BizLogicException e)
		{
			e.printStackTrace();
			fail(" BizLogicException occured : Distribution Protocol is mandatory");
		}
		catch (UserNotAuthorizedException e)
		{
			e.printStackTrace();
			fail(" UserNotAuthorizedException occured");
		}
	}
	
	
	
	/*-------------------------------------------------UPDATE---------------------------------------------*/
	/**
	 * 
	 */
	public void testNullSessionDataBeanInUpdate()
	{
		OrderBizLogic orderBizLogic = new OrderBizLogic();
		initJunitForInsertArguments();
		try
		{
			orderBizLogic.update(new OrderDetails(),new OrderDetails(),Constants.HIBERNATE_DAO,null);
			fail("When null sessiondataBean is passed, it should throw NullPointerException");
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
	/**
	 * 
	 */
	public void testNullOldDomainObjectInUpdate()
	{
		OrderBizLogic orderBizLogic = new OrderBizLogic();
		initJunitForInsertArguments();
		try
		{
			orderBizLogic.update(new OrderDetails(),null,Constants.HIBERNATE_DAO,new SessionDataBean());
			fail("When null sessiondataBean is passed, it should throw NullPointerException");
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
	/**
	 * 
	 */
	public void testNullCurrentDomainObjectInUpdate()
	{
		OrderBizLogic orderBizLogic = new OrderBizLogic();
		initJunitForInsertArguments();
		try
		{
			orderBizLogic.update(null,new OrderDetails(),Constants.HIBERNATE_DAO,new SessionDataBean());
			fail("When null sessiondataBean is passed, it should throw NullPointerException");
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
	/**
	 * 
	 */
	public void testWrongDaoTypeInUpdate()
	{
		OrderBizLogic orderBizLogic = new OrderBizLogic();
		initJunitForInsertArguments();
		try
		{
			orderBizLogic.update(new OrderDetails(),new OrderDetails(),0,new SessionDataBean());
			fail("When null sessiondataBean is passed, it should throw NullPointerException");
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
	/**
	 * 
	 */
	public void testEmptyCurrentDomainObjectInUpdate()
	{
		OrderBizLogic orderBizLogic = new OrderBizLogic();
		OrderDetails orderDetails = initOrder();
		initJunitForInsertArguments();
		try
		{
			orderBizLogic.update(new OrderDetails(),orderDetails,Constants.HIBERNATE_DAO,new SessionDataBean());
			fail("When null sessiondataBean is passed, it should throw NullPointerException");
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
	/**
	 * 
	 */
	public void testEmptyOldDomainObjectInUpdate()
	{
		OrderBizLogic orderBizLogic = new OrderBizLogic();
		OrderDetails orderDetails = initOrder();
		initJunitForInsertArguments();
		try
		{
			orderBizLogic.update(orderDetails,new OrderDetails(),Constants.HIBERNATE_DAO,new SessionDataBean());
			fail("When null sessiondataBean is passed, it should throw NullPointerException");
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
	/**
	 * 
	 */
	private void initJunitForUpdate()
	{
		Constraint[] constraints = {new IsInstanceOf(SessionDataBean.class)};
		hibDAO.expect("commit");
		FullConstraintMatcher fullConstraintMatcher = new FullConstraintMatcher(constraints);
		hibDAO.expect("openSession",fullConstraintMatcher);		
		Constraint[] updateConstraints = {new IsAnything(),new IsAnything(),new IsAnything(),new IsAnything()};
		FullConstraintMatcher updateConstraintMatcher = new FullConstraintMatcher(updateConstraints);
		hibDAO.expect("update",updateConstraintMatcher);		
		Constraint[] auditConstraints = {new IsAnything(),new IsAnything(),new IsAnything(),new IsAnything()};
		FullConstraintMatcher auditConstraintMatcher = new FullConstraintMatcher(auditConstraints);
		hibDAO.expect("audit",auditConstraintMatcher);
		hibDAO.expect("audit",auditConstraintMatcher);		
		hibDAO.expect("closeSession");
	}
	/**
	 * 
	 */
//	public void testUpdateForExistingSpecimens()
//	{
//		OrderBizLogic orderBizLogic = new OrderBizLogic();		
//		OrderDetails orderDetailsold = initOrder();
//		OrderDetails orderDetailsnew = initOrder();
//		OrderDetails order = initOrderForExistingBioSpecimens(orderDetailsnew);
//		
//		initJunitForUpdate();
//		SessionDataBean sessionDataBean = new SessionDataBean();
//		try
//		{
//			orderBizLogic.update(order,orderDetailsold,Constants.HIBERNATE_DAO,sessionDataBean);
//			assertTrue("Order Details updated successfully",true);			
//		}
//		catch (NullPointerException e)
//		{
//			e.printStackTrace();
//			fail("Null Pointer Exception");
//		}
//		catch (BizLogicException e)
//		{
//			e.printStackTrace();
//			fail("Biz Logic Exception occured");
//		}
//		catch (UserNotAuthorizedException e)
//		{
//			e.printStackTrace();
//			fail(" User Not Authorised Exception occured");
//		}
//	}
	
	
	
	/*--------------------------Common----------------------------------------*/
	
	/**
	 * @param order
	 * @return
	 */
	private OrderDetails initOrderForExistingBioSpecimens(OrderDetails order)
	{
		Collection orderItemCollection = new HashSet();	
		ExistingSpecimenOrderItem exSpOrderItem = new ExistingSpecimenOrderItem();
		
		ExistingSpecimenOrderItem orderItem =(ExistingSpecimenOrderItem) initOrderItem(exSpOrderItem);
		Specimen specimen = setSpecimenId();
		orderItem.setSpecimen(specimen);
		
		orderItemCollection.add(orderItem);
		order.setOrderItemCollection(orderItemCollection);
		return order;
	}
	/**
	 * @param order
	 * @return
	 */
//	------------For DerivedSpecimenOrderItem -----------------
	private OrderDetails initOrderForDerivedBioSpecimens(OrderDetails order)
	{
		Collection orderItemCollection = new HashSet();	
		DerivedSpecimenOrderItem drSpOrderItem = new DerivedSpecimenOrderItem();
		
		DerivedSpecimenOrderItem orderItem =(DerivedSpecimenOrderItem) initOrderItem(drSpOrderItem);
		Specimen specimen = setSpecimenId();
		orderItem.setParentSpecimen(specimen);
		
		orderItem.setSpecimenClass("Tissue");
		orderItem.setSpecimenType("DNA");
		
		orderItemCollection.add(orderItem);
		order.setOrderItemCollection(orderItemCollection);
		
		return order;
	}
	/**
	 * @param order
	 * @return
	 */
	//------------------PathologicalCaseOrderItem---------//
	private OrderDetails initOrderForPathoCase(OrderDetails order)
	{
		Collection orderItemCollection = new HashSet();	
		PathologicalCaseOrderItem pathoCaseOrderItem = new PathologicalCaseOrderItem();
		
		PathologicalCaseOrderItem orderItem =(PathologicalCaseOrderItem) initOrderItem(pathoCaseOrderItem);
		SpecimenCollectionGroup  specimenCollectionGroup = new SpecimenCollectionGroup();
		specimenCollectionGroup.setId(new Long("1"));
		orderItem.setSpecimenCollectionGroup(specimenCollectionGroup);
		
		orderItem.setSpecimenClass("Tissue");
		orderItem.setSpecimenType("DNA");
		
		orderItem.setPathologicalStatus("Incomplete");
		orderItem.setTissueSite("DC");
		
		orderItemCollection.add(orderItem);
		order.setOrderItemCollection(orderItemCollection);
		
		return order;
	}
	/**
	 * @param orderItem
	 * @return
	 */
	private OrderItem initOrderItem(OrderItem orderItem)
	{
		orderItem.setDescription("Junit");
		orderItem.setStatus("New");
		orderItem.setRequestedQuantity(new Quantity("10"));
		
		return orderItem;
	}
	/**
	 * @return
	 */
//	------------For ExistingSpecimenOrderItem and  DerivedSpecimenOrderItem---------//
	private Specimen setSpecimenId()
	{
		Specimen specimen = new Specimen();
		specimen.setId(new Long(3));
		return specimen;
	}
	/**
	 * @return
	 */
	public OrderDetails initOrder()
	{		
		OrderDetails order = new OrderDetails();	
		
		order.setComment("Comment");
		DistributionProtocol distributionProtocol = new DistributionProtocol();
		distributionProtocol.setId(new Long(2));
		order.setDistributionProtocol(distributionProtocol);
		
		order.setName("Request12");
		order.setStatus("New");
		try
		{
			order.setRequestedDate(Utility.parseDate("04-02-1984", Constants.DATE_PATTERN_MM_DD_YYYY));
		}
		catch (ParseException e)
		{
			Logger.out.debug(""+e);
		}
		return order;
	}
//	-----------------For New Specimen Array Order Item---------
	/**
	 * @param order
	 * @return
	 */
	private OrderDetails initOrderForNewSpecimenArrayOrderItem(OrderDetails order)
	{
		Collection orderItemCollection = new HashSet();	
		NewSpecimenArrayOrderItem newSpArOrderItem = new NewSpecimenArrayOrderItem();
		
		NewSpecimenArrayOrderItem orderItem =(NewSpecimenArrayOrderItem) initOrderItem(newSpArOrderItem);
		SpecimenArrayType specimenArrayType = new SpecimenArrayType();
		specimenArrayType.setId(new Long(3));
		orderItem.setSpecimenArrayType(specimenArrayType);	
		orderItem.setName("Array Order");
		
		Collection specimenOrderItemCollection = new HashSet();
		SpecimenOrderItem specimenOrderItem = new SpecimenOrderItem();
		specimenOrderItem.setId(new Long(14));
		specimenOrderItem.setNewSpecimenArrayOrderItem(orderItem);
		specimenOrderItemCollection.add(specimenOrderItem);
		orderItem.setSpecimenOrderItemCollection(specimenOrderItemCollection);
		
		orderItemCollection.add(orderItem);
		order.setOrderItemCollection(orderItemCollection);
		
		return order;		
	}
	/**
	 * @param order
	 * @return
	 */
	//------------------------ For ExistingSpecimenArrayOrderItem ---------------
	private OrderDetails initOrderForExistingSpecimenArrayOrderItem(OrderDetails order)
	{
		Collection orderItemCollection = new HashSet();	
		ExistingSpecimenArrayOrderItem exSpArOrderItem = new ExistingSpecimenArrayOrderItem();
		
		ExistingSpecimenArrayOrderItem orderItem =(ExistingSpecimenArrayOrderItem) initOrderItem(exSpArOrderItem);
				
		SpecimenArray specimenArray = new SpecimenArray();
		specimenArray.setId(new Long(1));
		orderItem.setSpecimenArray(specimenArray);
				
		orderItemCollection.add(orderItem);
		order.setOrderItemCollection(orderItemCollection);
		
		return order;		
	}
}
