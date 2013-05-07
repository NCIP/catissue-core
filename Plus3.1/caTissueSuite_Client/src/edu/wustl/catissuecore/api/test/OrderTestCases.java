package edu.wustl.catissuecore.api.test;

import java.util.Iterator;
import java.util.List;

import edu.wustl.catissuecore.domain.OrderDetails;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.logger.Logger;


public class OrderTestCases extends CaTissueBaseTestCase {
	AbstractDomainObject domainObject = null;
	static Long OrderId;
		
	public void testAddOrderDetails()
	{
		try {
			OrderDetails order = BaseTestCaseUtility.initOrder();			
			System.out.println(order);
			order = (OrderDetails) appService.createObject(order); 
			System.out.println("Object created successfully");
			//OrderId = order.getId();
			assertTrue("Object added successfully", true);
		 }
		 catch(Exception e){
			 Logger.out.error(e.getMessage(),e);
			 e.printStackTrace();
			 assertFalse("could not add object", true);
		 }
	}
	
	/*public void testSearchOrderDetails()
	{
		//OrderDetails order = BaseTestCaseUtility.initOrder();
		OrderDetails order = new OrderDetails();
      	Logger.out.info(" searching domain object");
      //	System.out.println("ID:"+OrderId.intValue());
      	//order.setId(OrderId);
      	order.setId(new Long(1));
          try {
        	 List resultList = appService.search(OrderDetails.class,order);
        	 for (Iterator resultsIterator = resultList.iterator(); resultsIterator.hasNext();) 
        	 {
        		 OrderDetails returnedOrder = (OrderDetails) resultsIterator.next();
        		 Logger.out.info(" Domain Object is successfully Found ---->  :: " + returnedOrder.getName());
        	 }
          } 
          catch (Exception e) {
           	Logger.out.error(e.getMessage(),e);
           	e.printStackTrace();
           	assertFalse("Does not find Domain Object", true);	 		
          }
	}*/
	
	public void testUpdateOrderDetails()
	{		
	    try 
		{
	    	OrderDetails order = BaseTestCaseUtility.initOrder();		
	    	Logger.out.info("updating domain object------->"+order);
	    	order = (OrderDetails) appService.createObject(order);
	    	BaseTestCaseUtility.updateOrderDetails(order);	    	
	    	OrderDetails updatedOrder = (OrderDetails) appService.updateObject(order);
	       	Logger.out.info("Domain object successfully updated ---->"+updatedOrder);
	       	assertTrue("Domain object successfully updated ---->"+updatedOrder, true);
	    } 
	    catch (Exception e) {
	       	Logger.out.error(e.getMessage(),e);
	 		e.printStackTrace();
	 		assertFalse("failed to update Object", true);
	    }
	}
	
	/*public void testWithEmptyOrderName()
	{
		try{
			OrderDetails dept = BaseTestCaseUtility.initOrder();			
			dept.setName("");
			System.out.println(dept);
			dept = (OrderDetails) appService.createObject(dept); 
			assertFalse("Empty Department name added successfully", true);
		 }
		 catch(Exception e){
			 e.printStackTrace();
			 assertTrue("Name is required", true);
		 }
	}
	public void testWithDuplicateOrderName()
	{
		try{
			OrderDetails order = BaseTestCaseUtility.initOrder();
			OrderDetails dupOrder = BaseTestCaseUtility.initOrder();
			dupOrder.setName(dept.getName());
			order = (OrderDetails) appService.createObject(order); 
			dupOrder = (OrderDetails) appService.createObject(dupOrder); 
			assertFalse("Test Failed. Duplicate dept name should throw exception", true);
		}
		 catch(Exception e){
			e.printStackTrace();
			assertTrue("Submission failed since a Department with the same NAME already exists" , true);
		 }
	}*/
	
//	public void testNullDomainObjectInInsert()
//	{
//		domainObject = new OrderDetails(); 
//		testNullDomainObjectInInsert(domainObject);
//	}
//	
//	public void testNullSessionDataBeanInInsert()
//	{
//		domainObject = new OrderDetails();
//		testNullSessionDataBeanInInsert(domainObject);
//	}
//		
//	public void testWrongDaoTypeInInsert()
//	{
//		domainObject = new OrderDetails();
//		testWrongDaoTypeInInsert(domainObject);
//	}
//	public void testNullSessionDataBeanInUpdate()
//	{
//		domainObject = new OrderDetails();
//		testNullSessionDataBeanInUpdate(domainObject);
//	}
//	
//	public void testNullOldDomainObjectInUpdate()
//	{
//		domainObject = new OrderDetails();
//		testNullOldDomainObjectInUpdate(domainObject);
//	}
//	
//		
//	public void testNullCurrentDomainObjectInUpdate()
//	{
//		domainObject = new OrderDetails();
//		testNullCurrentDomainObjectInUpdate(domainObject);
//	}
//	
//	/*public void testEmptyCurrentDomainObjectInUpdate()
//	{
//		domainObject = new OrderDetails();
//		AbstractDomainObject initialisedDomainObject = BaseTestCaseUtility.initDepartment();
//		testEmptyCurrentDomainObjectInUpdate(domainObject, initialisedDomainObject);
//	}
//	
//	public void testEmptyOldDomainObjectInUpdate()
//	{
//		domainObject = new OrderDetails();
//		AbstractDomainObject initialisedDomainObject = BaseTestCaseUtility.initDepartment();
//		testEmptyOldDomainObjectInUpdate( domainObject , initialisedDomainObject);
//		
//	}*/
//	
//	public void testNullDomainObjectInRetrieve()
//	{
//		domainObject = new OrderDetails();
//		testNullCurrentDomainObjectInRetrieve(domainObject);
//	}
//	
//	
//	
}
