package edu.wustl.catissuecore.api.test;

import java.util.Iterator;
import java.util.List;

import edu.wustl.catissuecore.domain.Distribution;
import edu.wustl.catissuecore.domain.DistributionProtocol;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.logger.Logger;


public class DistributionTestCases extends CaTissueBaseTestCase{
	AbstractDomainObject domainObject = null;
	public void testAddDistribution()
	{
		try{
			Specimen specimen = new Specimen();
			specimen.setId(new Long(4));
			Distribution distribution = BaseTestCaseUtility.initDistribution(specimen);			
			System.out.println(distribution);
			distribution = (Distribution) appService.createObject(distribution); 
			System.out.println("Object created successfully");
			assertTrue("Object added successfully", true);
		 }
		 catch(Exception e){
			 e.printStackTrace();
			 assertFalse("could not add object", true);
		 }
	}
	
	/*public void testSearchDistribution()
	{
			Distribution distribution = new Distribution();
	       	Logger.out.info(" searching domain object");
	    	distribution.setId(new Long(1));
	   
	         try {
	        	 List resultList = appService.search(Distribution.class,distribution);
	        	 for (Iterator resultsIterator = resultList.iterator(); resultsIterator.hasNext();) {
	        		 Distribution returnedDistribution = (Distribution) resultsIterator.next();
	        		 Logger.out.info(" Domain Object is successfully Found ---->  :: " + returnedDistribution);
	        		// System.out.println(" Domain Object is successfully Found ---->  :: " + returnedDepartment.getName());
	             }
	          } 
	          catch (Exception e) {
	           	Logger.out.error(e.getMessage(),e);
	           	e.printStackTrace();
	           	assertFalse("Does not find Domain Object", true);
		 		
	          }
	}
	
	public void testNullDomainObjectInInsert()
	{
		domainObject = new DistributionProtocol(); 
		testNullDomainObjectInInsert(domainObject);
	}
	
	public void testNullSessionDataBeanInInsert()
	{
		domainObject = new DistributionProtocol();
		testNullSessionDataBeanInInsert(domainObject);
	}
		
	public void testWrongDaoTypeInInsert()
	{
		domainObject = new Distribution();
		testWrongDaoTypeInInsert(domainObject);
	}
	public void testNullSessionDataBeanInUpdate()
	{
		domainObject = new Distribution();
		testNullSessionDataBeanInUpdate(domainObject);
	}
	
	public void testNullOldDomainObjectInUpdate()
	{
		domainObject = new Distribution();
		testNullOldDomainObjectInUpdate(domainObject);
	}
	
		
	public void testNullCurrentDomainObjectInUpdate()
	{
		domainObject = new Distribution();
		testNullCurrentDomainObjectInUpdate(domainObject);
	}
	
	public void testEmptyCurrentDomainObjectInUpdate()
	{
		domainObject = new Distribution();
		AbstractDomainObject initialisedDomainObject = BaseTestCaseUtility.initDistributionProtocol();
		testEmptyCurrentDomainObjectInUpdate(domainObject, initialisedDomainObject);
	}
	
	public void testEmptyOldDomainObjectInUpdate()
	{
		domainObject = new Distribution();
		AbstractDomainObject initialisedDomainObject = BaseTestCaseUtility.initDistributionProtocol();
		testEmptyOldDomainObjectInUpdate(domainObject,initialisedDomainObject);
	}
	
	public void testNullDomainObjectInRetrieve()
	{
		domainObject = new Distribution();
		testNullCurrentDomainObjectInRetrieve(domainObject);
	}*/
		

}
