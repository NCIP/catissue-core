package edu.wustl.catissuecore.api.testcases;

import edu.wustl.catissuecore.domain.DistributedItem;
import edu.wustl.catissuecore.domain.Distribution;
import edu.wustl.catissuecore.domain.DistributionProtocol;
import edu.wustl.catissuecore.domain.ExistingSpecimenOrderItem;
import edu.wustl.catissuecore.domain.OrderDetails;
import edu.wustl.catissuecore.domain.OrderItem;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.common.util.global.Status;
import gov.nih.nci.system.applicationservice.ApplicationException;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 * @author Sagar Baldwa
 *         
 */
public class OrderingDistributionTestCases extends AbstractCaCoreApiTestCasesWithRegularAuthentication {    
    
	/**
	 * Create a Order in caTissue
	 * @throws ApplicationException
	 */
    public void testCreateOrder() throws ApplicationException
    {
    	//Create OrderDetails Object
    	OrderDetails orderDetails = new OrderDetails();
    	orderDetails.setName("First Order");
    	orderDetails.setComment("First Order Comments");
    	orderDetails.setStatus("New");
    	
    	//Set Distribution Protocol Object to OrderDetails
    	DistributionProtocol distributionProtocol = new DistributionProtocol();
    	distributionProtocol.setTitle("Distribution Protocol 1");    	
    	
    	//Search the Distribution Protocol and set it to Order Details
    	List<DistributionProtocol> distProtList = searchByExample(DistributionProtocol.class, distributionProtocol);
    	if(distProtList.size() > 0)
    	{
    		DistributionProtocol searchDistProt = new DistributionProtocol();
    		searchDistProt.setId(distProtList.get(0).getId());    		
    		orderDetails.setDistributionProtocol(searchDistProt);    		
    	}
    	else
    	{
    		System.out.println("Error in testCreateOrder! ");
    		assertFalse(distributionProtocol.getTitle() + " Distribution Protocol not found!", distProtList.size() > 0);
    	}
    	
    	//Create OrderItem and add Specimen (1) to it
        ExistingSpecimenOrderItem existingSpecimenOrderItem1 = new ExistingSpecimenOrderItem();
        existingSpecimenOrderItem1.setStatus("New");
        existingSpecimenOrderItem1.setDescription("OI Description " + UniqueKeyGeneratorUtil.getUniqueKey());
        existingSpecimenOrderItem1.setRequestedQuantity(1.0);
        existingSpecimenOrderItem1.setOrderDetails(orderDetails);
        
        //Search the Specimen1 label and set it to Order Details
        Specimen specimen = new Specimen();
    	specimen.setLabel("ABCDE");
    	    	
    	List<Specimen> specimenList = searchByExample(Specimen.class, specimen);
    	if(specimenList.size() > 0)
    	{
    		Specimen searchSpecimen = new Specimen();
    		searchSpecimen.setId(specimenList.get(0).getId());    		
    		existingSpecimenOrderItem1.setSpecimen(searchSpecimen);    		
    	}
    	else
    	{
    		assertFalse(specimen.getLabel() + " Specimen not found!", specimenList.size() > 0);
    	}
        
        //Create OrderItem and add Specimen (2) to it
        ExistingSpecimenOrderItem existingSpecimenOrderItem2 = new ExistingSpecimenOrderItem();
        existingSpecimenOrderItem2.setStatus("New");
        existingSpecimenOrderItem2.setDescription("OI Description " + UniqueKeyGeneratorUtil.getUniqueKey());
        existingSpecimenOrderItem2.setRequestedQuantity(1.0);
        existingSpecimenOrderItem2.setOrderDetails(orderDetails);
        existingSpecimenOrderItem2.setStatus("New");

        //Search the Specimen2 label and set it to Order Details
        Specimen specimen2 = new Specimen();
    	specimen2.setLabel("12345");
    	    	
    	List<Specimen> specimenList2 = searchByExample(Specimen.class, specimen2);
    	if(specimenList2.size() > 0)
    	{
    		Specimen searchSpecimen = new Specimen();
    		searchSpecimen.setId(specimenList2.get(0).getId());    		
    		existingSpecimenOrderItem2.setSpecimen(searchSpecimen);    		
    	}
    	else
    	{
    		assertFalse(specimen2.getLabel() + " Specimen not found!", specimenList2.size() > 0);
    	}
        
        //Set OrderItem Collection to Order Details
        Collection orderItemCollection = new HashSet();
        orderItemCollection.add(existingSpecimenOrderItem1);
        orderItemCollection.add(existingSpecimenOrderItem2);        
        orderDetails.setOrderItemCollection(orderItemCollection);
        
        //Insert OrderDetails Object
        OrderDetails orderDetails1 = insert(orderDetails);

        //New OrderDetails object created
        assertTrue(orderDetails.getId() > 0);
        log.debug(">>> INSERTED ORDER: " + orderDetails.getId());
        System.out.println(">>> INSERTED ORDER: " + orderDetails.getId());
    }
        
    /**
	 * Create a Order with Distribution Protocol in caTissue
	 * @throws ApplicationException
	 */
    public void testCreateOrderWithoutDistributionProtocol() throws ApplicationException
    {
    	//Create OrderDetails Object
    	OrderDetails orderDetails = new OrderDetails();
    	orderDetails.setName("Second Order");
    	orderDetails.setComment("Second Order Comments");
    	orderDetails.setStatus("New");
  	
    	//Create OrderItem and add Specimen (1) to it
        ExistingSpecimenOrderItem existingSpecimenOrderItem1 = new ExistingSpecimenOrderItem();
        existingSpecimenOrderItem1.setStatus("New");
        existingSpecimenOrderItem1.setDescription("OI Description " + UniqueKeyGeneratorUtil.getUniqueKey());
        existingSpecimenOrderItem1.setRequestedQuantity(1.0);
        existingSpecimenOrderItem1.setOrderDetails(orderDetails);

        //Search the Specimen1 label and set it to Order Details
        Specimen specimen = new Specimen();
    	specimen.setLabel("ABCDE");
    	    	
    	List<Specimen> specimenList = searchByExample(Specimen.class, specimen);
    	if(specimenList.size() > 0)
    	{
    		Specimen searchSpecimen = new Specimen();
    		searchSpecimen.setId(specimenList.get(0).getId());    		
    		existingSpecimenOrderItem1.setSpecimen(searchSpecimen);    		
    	}
    	else
    	{
    		assertFalse(specimen.getLabel() + " Specimen not found!", specimenList.size() > 0);
    	}

        //Create OrderItem and add Specimen (2) to it
        ExistingSpecimenOrderItem existingSpecimenOrderItem2 = new ExistingSpecimenOrderItem();
        existingSpecimenOrderItem2.setStatus("New");
        existingSpecimenOrderItem2.setDescription("OI Description " + UniqueKeyGeneratorUtil.getUniqueKey());
        existingSpecimenOrderItem2.setRequestedQuantity(1.0);
        existingSpecimenOrderItem2.setOrderDetails(orderDetails);
        existingSpecimenOrderItem2.setStatus("New");
        
        //Search the Specimen2 label and set it to Order Details
        Specimen specimen2 = new Specimen();
    	specimen2.setLabel("12345");
    	    	
    	List<Specimen> specimenList2 = searchByExample(Specimen.class, specimen2);
    	if(specimenList2.size() > 0)
    	{
    		Specimen searchSpecimen = new Specimen();
    		searchSpecimen.setId(specimenList2.get(0).getId());    		
    		existingSpecimenOrderItem2.setSpecimen(searchSpecimen);    		
    	}
    	else
    	{
    		assertFalse(specimen2.getLabel() + " Specimen not found!", specimenList2.size() > 0);
    	}
        
        //Set OrderItem Collection to Order Details
        Collection orderItemCollection = new HashSet();
        orderItemCollection.add(existingSpecimenOrderItem1);
        orderItemCollection.add(existingSpecimenOrderItem2);        
        orderDetails.setOrderItemCollection(orderItemCollection);
        
        //Insert OrderDetails Object
        OrderDetails orderDetails1 = insert(orderDetails);
        
        //New OrderDetails object created
        assertTrue(orderDetails1.getId() > 0);        
        log.debug(">>> INSERTED ORDER: " + orderDetails1.getId());        
        System.out.println(">>> INSERTED ORDER: " + orderDetails1.getId());        
    }
    
    /**
     * Create an Order and Distribute it
     * @throws ApplicationException
     */
    public void testCreateOrderAndDistribute() throws ApplicationException
    {
    	//Create OrderDetails Object
    	OrderDetails orderDetails = new OrderDetails();
    	orderDetails.setName("Third Order" + UniqueKeyGeneratorUtil.getUniqueKey());
    	orderDetails.setComment("Order Comments " + UniqueKeyGeneratorUtil.getUniqueKey());
    	orderDetails.setStatus("New");
    	
    	//Set Distribution Protocol Object to OrderDetails
    	DistributionProtocol distributionProtocol = new DistributionProtocol();
    	distributionProtocol.setTitle("Distribution Protocol 1");
    	DistributionProtocol searchDistProt = new DistributionProtocol();
    	
    	//Search the Distribution Protocol and set it to Order Details
    	List<DistributionProtocol> distProtList = searchByExample(DistributionProtocol.class, distributionProtocol);
    	if(distProtList.size() > 0)
    	{    		
    		searchDistProt.setId(distProtList.get(0).getId());    		
    		orderDetails.setDistributionProtocol(searchDistProt);    		
    	}
    	else
    	{
    		assertFalse(distributionProtocol.getTitle() + " Distribution Protocol not found!", distProtList.size() > 0);
    	}
    	
    	//Create OrderItem and add Specimen (1) to it
    	ExistingSpecimenOrderItem existingSpecimenOrderItem1 = new ExistingSpecimenOrderItem();
        existingSpecimenOrderItem1.setStatus("New");
        existingSpecimenOrderItem1.setDescription("OI Description " + UniqueKeyGeneratorUtil.getUniqueKey());
        existingSpecimenOrderItem1.setRequestedQuantity(1.0);
        existingSpecimenOrderItem1.setOrderDetails(orderDetails);
        
        //Search the Specimen1 label and set it to Order Details
        Specimen specimen = new Specimen();
        Specimen searchSpecimen1 = new Specimen();
    	specimen.setLabel("ABCDE");
    	    	
    	List<Specimen> specimenList = searchByExample(Specimen.class, specimen);
    	if(specimenList.size() > 0)
    	{
    		searchSpecimen1.setId(specimenList.get(0).getId());    		
    		existingSpecimenOrderItem1.setSpecimen(searchSpecimen1);    		
    	}
    	else
    	{
    		assertFalse(specimen.getLabel() + " Specimen not found!", specimenList.size() > 0);
    	}
        
        //Create OrderItem and add Specimen (2) to it
        ExistingSpecimenOrderItem existingSpecimenOrderItem2 = new ExistingSpecimenOrderItem();
        existingSpecimenOrderItem2.setStatus("New");
        existingSpecimenOrderItem2.setDescription("OI Description " + UniqueKeyGeneratorUtil.getUniqueKey());
        existingSpecimenOrderItem2.setRequestedQuantity(1.0);
        existingSpecimenOrderItem2.setOrderDetails(orderDetails);
        existingSpecimenOrderItem2.setStatus("New");
        
        //Search the Specimen2 label and set it to Order Details
        Specimen specimen2 = new Specimen();
        Specimen searchSpecimen2 = new Specimen();
    	specimen2.setLabel("12345");
    	    	
    	List<Specimen> specimenList2 = searchByExample(Specimen.class, specimen2);
    	if(specimenList2.size() > 0)
    	{
    		searchSpecimen2.setId(specimenList2.get(0).getId());    		
    		existingSpecimenOrderItem2.setSpecimen(searchSpecimen2);    		
    	}
    	else
    	{
    		assertFalse(specimen2.getLabel() + " Specimen not found!", specimenList2.size() > 0);
    	}
        
        //Set OrderItem Collection to Order Details
        Collection orderItemCollection = new HashSet();
        orderItemCollection.add(existingSpecimenOrderItem1);
        orderItemCollection.add(existingSpecimenOrderItem2);        
        orderDetails.setOrderItemCollection(orderItemCollection);      
        
        //Insert OrderDetails Object
        OrderDetails addedOrderDetails = insert(orderDetails);
        
        //New OrderDetails object created
        log.debug(">>> INSERTED ORDER: " + addedOrderDetails.getId());
        System.out.println(">>> INSERTED ORDER: " + addedOrderDetails.getId());
        
        //Distribute Starts               
        
        //Create Distribution Object
        Distribution distribution = new Distribution();        	
    	distribution.setActivityStatus(Status.ACTIVITY_STATUS_ACTIVE
				.toString());
    	distribution.setOrderDetails(addedOrderDetails);
    	distribution.setDistributionProtocol(searchDistProt);    	
    	final Date date = new Date();
		distribution.setTimestamp(date);
		distribution.setComment("Distribution Comments " + UniqueKeyGeneratorUtil.getUniqueKey());
		
		//Search the Site object and set it to Distribution Protocol Object
    	Site site = new Site();
    	site.setName("NIH");    	
    	
    	List<Site> siteList = searchByExample(Site.class, site);
    	if(siteList.size() > 0)
    	{
    		Site searchSite = new Site();
    		searchSite.setId(siteList.get(0).getId());    		
    		distribution.setToSite(searchSite);    		
    	}
    	else
    	{
    		assertFalse(site.getName() + " Site not found!", siteList.size() > 0);
    	}
		
		//Add Distribution to OrderDetails Object
		Collection newDistributionColl = new HashSet();
		newDistributionColl.add(distribution);
		addedOrderDetails.setDistributionCollection(newDistributionColl);
        
        //Create Distributed Item(1) object and specimen(1) to it
		DistributedItem distributedItem1 = new DistributedItem();
        distributedItem1.setSpecimen(searchSpecimen1);
        distributedItem1.setQuantity(1.0);
        
        //Create Distributed Item(2) object and specimen(2) to it
        DistributedItem distributedItem2 = new DistributedItem();
        distributedItem2.setSpecimen(searchSpecimen2);
        distributedItem2.setQuantity(1.0);
        
        //Add DistrbutionItems Collection to Distrbution
        Collection distributedItemColl = new HashSet();
        distributedItemColl.add(distributedItem1);
        distributedItemColl.add(distributedItem2);
        distribution.setDistributedItemCollection(distributedItemColl);
        
        //Update the OrderDetails Status
        Collection orderItems = addedOrderDetails.getOrderItemCollection();
        Collection newOrderItems = new HashSet();
        Iterator<OrderItem> iterator = orderItems.iterator();
        while(iterator.hasNext())
        {
        	OrderItem orderItem = iterator.next();
        	orderItem.setStatus("Distributed");
        	newOrderItems.add(orderItem);
        }
        addedOrderDetails.setOrderItemCollection(newOrderItems);
        addedOrderDetails.setStatus("Completed");

        //Update the OrderDetails Object
        OrderDetails updatedOrderDetails = update(addedOrderDetails);
        
        //Order processed and its items are distributed
        assertTrue(updatedOrderDetails.getId() > 0);
        log.debug(">>> DISTRIBUTED ORDER: " + distribution.getId());
        log.debug(">>> DISTRIBUTED ORDER: " + distribution.getActivityStatus());
        System.out.println(">>> DISTRIBUTED ORDER: " + distribution.getId());
    }
	
	/**
	 * Query for an order based on name and distribute it.
	 * @throws ApplicationException
	 */
    public void testQueryByOrderNameAndDistributeIt() throws ApplicationException
    {
    	//Create an oder details object and set the order name to it to search
    	OrderDetails orderDetails = new OrderDetails();
    	orderDetails.setName("First Order");   	
    	
    	//Search OrderDetails
    	List<OrderDetails> orderDetailsList = searchByExample(OrderDetails.class, orderDetails);
    	
    	log.debug(">>> ORDER FOUND" + "\n");        
        System.out.println(">>> ORDER FOUND" + "\n");
    	
        //Iterate over OrderDetails List
    	Iterator<OrderDetails> iterator = orderDetailsList.iterator();
    	while(iterator.hasNext())
    	{
    		OrderDetails orderDetails2 = iterator.next();
    		
    		//Create a new OrderItem and set the identified from search OrderItem to it
    		OrderItem orderItem = new OrderItem();
    		
    		//Set the OrderDetails object reference to new OrderItem
    		orderItem.setOrderDetails(orderDetails2);
    		
    		//Create Distribution Object
            Distribution distribution = new Distribution();        	
        	distribution.setActivityStatus(Status.ACTIVITY_STATUS_ACTIVE.toString());
        	distribution.setOrderDetails(orderDetails2);
        	final Date date = new Date();
    		distribution.setTimestamp(date);
    		distribution.setComment("Distribution Comments " + UniqueKeyGeneratorUtil.getUniqueKey());
    		
    		//Search the Site object and set it to Distribution Protocol Object
        	Site site = new Site();
        	site.setName("NIH");    	
        	
        	List<Site> siteList = searchByExample(Site.class, site);
        	if(siteList.size() > 0)
        	{
        		Site searchSite = new Site();
        		searchSite.setId(siteList.get(0).getId());    		
        		distribution.setToSite(searchSite);    		
        	}
        	else
        	{
        		assertFalse(site.getName() + " Site not found!", siteList.size() > 0);
        	}
        	
    		//Set Distribution Protocol Object to OrderDetails
        	DistributionProtocol distributionProtocol = new DistributionProtocol();
        	distributionProtocol.setTitle("Distribution Protocol 1");    	
        	
        	//Search the Distribution Protocol and set it to Order Details
        	List<DistributionProtocol> distProtList = searchByExample(DistributionProtocol.class, distributionProtocol);
        	if(distProtList.size() > 0)
        	{
        		DistributionProtocol searchDistProt = new DistributionProtocol();
        		searchDistProt.setId(distProtList.get(0).getId());    		
        		distribution.setDistributionProtocol(searchDistProt);    		
            	orderDetails2.setDistributionProtocol(searchDistProt);
        	}
        	else
        	{
        		assertFalse(distributionProtocol.getTitle() + " Distribution Protocol not found!", distProtList.size() > 0);
        	}
    		
    		//Created new empty collection for OrderItems and Distribution Items. It is required because the current code has issues with Hibernate Invocation. 
        	Collection<OrderItem> orderItemsCollection = new HashSet<OrderItem>();
    		Collection<DistributedItem> distributedItemsCollection = new HashSet<DistributedItem>();
    		
    		//Search OrderItem
    		List<OrderItem> orderItemList = searchByExample(OrderItem.class, orderItem);
    		
    		//Iterate OrderItem List
    		Iterator<OrderItem> iterator2 = orderItemList.iterator();
    		while(iterator2.hasNext())
    		{
    			System.out.println(">>> ORDER ITEM FOUND" + "\n");
    			OrderItem orderItem2 = iterator2.next();
    			//Set OrderItem object status and add it to new OrderItemCollection list
    			orderItem2.setStatus("Distributed");
    			orderItemsCollection.add(orderItem2);
    			
    			//Create a DistrbutedItem object for every OrderItem    			
                DistributedItem distributedItem1 = new DistributedItem();                
                distributedItem1.setSpecimen(((ExistingSpecimenOrderItem)orderItem2).getSpecimen());
                distributedItem1.setQuantity(1.0);
                //Add the DistributedItem to new DistributedItemCollection list
                distributedItemsCollection.add(distributedItem1);
    		}
    		
    		//Set the DistributedItemColleciton to new Distribution object created above
    		distribution.setDistributedItemCollection(distributedItemsCollection);
    		
    		//Add Distribution object reference to OrderDetails Object
    		Collection newDistributionColl = new HashSet();
    		newDistributionColl.add(distribution);
    		orderDetails2.setDistributionCollection(newDistributionColl);    		
    		
    		//Set OrderDetails status to Distributed    		
    		orderDetails2.setStatus("Distributed");
    		//Set the new OrderItemCollection to new OrderDetails Object 
    		orderDetails2.setOrderItemCollection(orderItemsCollection);
    		    		
    		//Update the OrderDetails object and distribute the order
    		OrderDetails updatedOrder = update(orderDetails2);
    		
    		System.out.println(">>> UPDATED ORDER ID: " + updatedOrder.getId());
    		log.debug(">>> UPDATED ORDER ID: " + updatedOrder.getId());
    	}    	
        assertTrue(orderDetailsList.size() > 0);
        System.out.println(">>> TOTAL ORDERS UPDATED: " + orderDetailsList.size());
		log.debug(">>> TOTAL ORDERS UPDATED: " + orderDetailsList.size());        
    }
    
    /**
	 * Query for Distribution Protocol and its 'Pending' orders.
	 * @throws ApplicationException
	 */
    public void testQueryDistributionProtocolandItsPendingOrder() throws ApplicationException
    {
    	//Create OrderDetails object. Set Distribution Protocol object and order status to 'Pending'.
    	OrderDetails orderDetails = new OrderDetails();    	
    	orderDetails.setStatus("Pending");
    	
    	//Set Distribution Protocol Object to OrderDetails
    	DistributionProtocol distributionProtocol = new DistributionProtocol();
    	distributionProtocol.setTitle("Distribution Protocol 1");    	
    	
    	//Search the Distribution Protocol and set it to Order Details
    	List<DistributionProtocol> distProtList = searchByExample(DistributionProtocol.class, distributionProtocol);
    	if(distProtList.size() > 0)
    	{
    		DistributionProtocol searchDistProt = new DistributionProtocol();
    		searchDistProt.setId(distProtList.get(0).getId());    		
    		orderDetails.setDistributionProtocol(searchDistProt);
    	}
    	else
    	{
    		assertFalse(distributionProtocol.getTitle() + " Distribution Protocol not found!", distProtList.size() > 0);
    	}
    	
    	//Search the OrderDetails
    	List<OrderDetails> orderDetailsList = searchByExample(OrderDetails.class, orderDetails);
    	
    	log.debug(">>> TOTAL ORDERS FOUND: " + orderDetailsList.size() + "\n");
    	System.out.println(">>> TOTAL ORDERS FOUND: " + orderDetailsList.size() + "\n");
    	
    	//Iterate over all the OrderDetails found    	
    	Iterator<OrderDetails> iterator = orderDetailsList.iterator();
    	while(iterator.hasNext())
    	{
    		OrderDetails orderDetails2 = iterator.next();
    		//Output Console
    		System.out.println(">>> ORDER ID: " + orderDetails2.getId() + "  >>> ORDER NAME: " + orderDetails2.getName()
    					+ "  >>> ORDER STATUS: " + orderDetails2.getStatus());
    		log.debug(">>> ORDER ID: " + orderDetails2.getId() + "  >>> ORDER NAME: " + orderDetails2.getName()
    					+ "  >>> ORDER STATUS: " + orderDetails2.getStatus());
    	}
        assertTrue(orderDetailsList.size() > 0);        
    }
    
    /**
	 * Reject All Pending Orders for a Distribution Protocol
	 * @throws ApplicationException
	 */
    public void testRejectAllPendingOrdersForDP() throws ApplicationException
    {
    	//Create OrderDetails object. Set DstributionProtocol object reference and OrderDetails status to 'Pending'.
    	OrderDetails orderDetails = new OrderDetails();    	
    	orderDetails.setStatus("Pending");
    	
    	//Set Distribution Protocol Object to OrderDetails
    	DistributionProtocol distributionProtocol = new DistributionProtocol();
    	distributionProtocol.setTitle("Distribution Protocol 1");    	
    	
    	//Search the Distribution Protocol and set it to Order Details
    	List<DistributionProtocol> distProtList = searchByExample(DistributionProtocol.class, distributionProtocol);
    	if(distProtList.size() > 0)
    	{
    		DistributionProtocol searchDistProt = new DistributionProtocol();
    		searchDistProt.setId(distProtList.get(0).getId());    		
    		orderDetails.setDistributionProtocol(searchDistProt);    		
    	}
    	else
    	{
    		assertFalse(distributionProtocol.getTitle() + " Distribution Protocol not found!", distProtList.size() > 0);
    	}
    	
    	//Search OrderDetails
    	List<OrderDetails> orderDetailsList = searchByExample(OrderDetails.class, orderDetails);
    	
    	log.debug(">>> TOTAL ORDERS FOUND: " + orderDetailsList.size() + "\n");        
        System.out.println(">>> TOTAL ORDERS FOUND: " + orderDetailsList.size() + "\n");
    	
    	//Iterate the OrderDetailsList 
        Iterator<OrderDetails> iterator = orderDetailsList.iterator();
    	while(iterator.hasNext())
    	{
    		OrderDetails orderDetails2 = iterator.next();
    		
    		//Set the OrderDetails status to "Rejected - Inappropriate Request".
    		orderDetails2.setStatus("Rejected - Inappropriate Request");   		
    		
    		//Find the OrderItem Object from the OrderDetails
    		OrderDetails orderDetails3 = new OrderDetails();
    		orderDetails3.setId(orderDetails2.getId());
    		OrderItem orderItem = new OrderItem();
    		orderItem.setOrderDetails(orderDetails3);
    		
    		//Create a new OrderItemsCollection    		
    		Collection<OrderItem> orderItemsCollection = new HashSet<OrderItem>();
    		
    		//Search OrderItem
    		List<OrderItem> orderItemList = searchByExample(OrderItem.class, orderItem);
    		
    		//Iterate over the OrderItem list 
    		Iterator<OrderItem> iterator2 = orderItemList.iterator();
    		while(iterator2.hasNext())
    		{
    			OrderItem orderItem2 = iterator2.next();
    			//Set the OrderItem status to "Rejected - Inappropriate Request"
    			orderItem2.setStatus("Rejected - Inappropriate Request");

    			//Add the OrderItem object to the new OrderItemsCollection 
    			orderItemsCollection.add(orderItem2);
    		}
    		
    		//Set the OrderItemsCollection to OrderDetails
    		orderDetails2.setOrderItemCollection(orderItemList);
    		
    		//Added for OrderingBizLogic Line 254. To avoid the Invocation Exception
    		orderDetails2.setDistributionCollection(new HashSet());
    		
    		//Update the OrderDetails object
    		OrderDetails updatedOrder = update(orderDetails2);
    		
    		//Output Console
    		System.out.println(">>> UPDATED ORDER ID: " + updatedOrder.getId());
    		log.debug(">>> UPDATED ORDER ID: " + updatedOrder.getId());
    	}    	
        assertTrue(orderDetailsList.size() > 0);
        System.out.println(">>> TOTAL ORDERS UPDATED: " + orderDetailsList.size());
		log.debug(">>> TOTAL ORDERS UPDATED: " + orderDetailsList.size());        
    }
    
    /**
	 * Query for an order with a particular status.  
	 * @throws ApplicationException
	 * 
	 * This test case can be used for Scientist User.
	 */
    public void testSearchForParticularStatusOfOrder() throws ApplicationException
    {
    	//Create OrderDetails Object and set the status to it for search 
    	OrderDetails orderDetails = new OrderDetails();    	
    	orderDetails.setStatus("New");   	
    	
    	//Search the OrderDetails
    	List<OrderDetails> orderDetailsList = searchByExample(OrderDetails.class, orderDetails);
    	
    	log.debug(">>> TOTAL ORDERS FOUND: " + orderDetailsList.size() + "\n");
    	System.out.println(">>> TOTAL ORDERS FOUND: " + orderDetailsList.size() + "\n");
    	
    	//Iterate over all the OrderDetails found
    	Iterator<OrderDetails> iterator = orderDetailsList.iterator();
    	while(iterator.hasNext())
    	{
    		OrderDetails orderDetails2 = iterator.next();
    		//Output Console
    		System.out.println(">>> ORDER ID: " + orderDetails2.getId() + "  >>> ORDER NAME: " + orderDetails2.getName()
    				 + "  >>> ORDER STATUS: " + orderDetails2.getStatus());
    		log.debug(">>> ORDER ID: " + orderDetails2.getId() + "  >>> ORDER NAME: " + orderDetails2.getName()
    				 + "  >>> ORDER STATUS: " + orderDetails2.getStatus());
    	}
        assertTrue(orderDetailsList.size() > 0);        
    }
}
