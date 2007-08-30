package edu.wustl.catissuecore.bizlogic.test;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import com.sun.msv.datatype.xsd.datetime.ParseException;

import edu.wustl.catissuecore.domain.DistributionProtocol;
import edu.wustl.catissuecore.domain.Institution;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.SpecimenRequirement;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.logger.Logger;

public class DistributionProtocolTestCases extends CaTissueBaseTestCase{
	AbstractDomainObject domainObject = null;
	
	public void testAddDistributionProtocol()
	{
		try{
			DistributionProtocol distributionprotocol = BaseTestCaseUtility.initDistributionProtocol();			
			System.out.println(distributionprotocol);
			distributionprotocol = (DistributionProtocol) appService.createObject(distributionprotocol); 
			System.out.println("Object created successfully");
			assertTrue("Object added successfully", true);
		 }
		 catch(Exception e){
			 e.printStackTrace();
			 assertFalse("could not add object", true);
		 }
	}
	
	public void testSearchDistributionProtocol()
	{
			DistributionProtocol distributionprotocol = new DistributionProtocol();
	       	Logger.out.info(" searching domain object");
	    	distributionprotocol.setId(new Long(1));
	   
	         try {
	        	 List resultList = appService.search(DistributionProtocol.class,distributionprotocol);
	        	 for (Iterator resultsIterator = resultList.iterator(); resultsIterator.hasNext();) {
	        		 DistributionProtocol returnedDP = (DistributionProtocol) resultsIterator.next();
	        		 Logger.out.info(" Domain Object is successfully Found ---->  :: " + returnedDP.getTitle());
	        		// System.out.println(" Domain Object is successfully Found ---->  :: " + returnedDepartment.getName());
	             }
	          } 
	          catch (Exception e) {
	           	Logger.out.error(e.getMessage(),e);
	           	e.printStackTrace();
	           	assertFalse("Does not find Domain Object", true);
		 		
	          }
	}
	
	/*public void testUpdateDistributionProtocol()
	{
		
   	
	    try 
	  	{
	    	DistributionProtocol distributionprotocol =  BaseTestCaseUtility.initDistributionProtocol();
	    	Logger.out.info("updating domain object------->"+distributionprotocol);
	    	distributionprotocol = (DistributionProtocol) appService.createObject(distributionprotocol);
	    	BaseTestCaseUtility.updateDistributionProtocol(distributionprotocol);	    	
	    	DistributionProtocol updatedCRG = (DistributionProtocol) appService.updateObject(distributionprotocol);
	       	Logger.out.info("Domain object successfully updated ---->"+updatedCRG);
	       	assertTrue("Domain object successfully updated ---->"+updatedCRG, true);
	    } 
	    catch (Exception e) {
	       	Logger.out.error(e.getMessage(),e);
	 		e.printStackTrace();
	 		assertFalse("failed to update Object", true);
	    }
	}	*/
	
	public void testDistributionProtocolWithEmptyTitle()
	{
		    	
	    try 
	  	{
	    	DistributionProtocol distributionprotocol =  BaseTestCaseUtility.initDistributionProtocol();
	    	distributionprotocol.setTitle("");
	    	Logger.out.info("updating domain object------->"+distributionprotocol);
	    	distributionprotocol = (DistributionProtocol) appService.createObject(distributionprotocol);
	    	Logger.out.info("Domain object eith empty title ---->"+distributionprotocol);
	       	assertFalse("DistributionProtocol should throw exception ---->"+distributionprotocol, true);
	    } 
	    catch (Exception e) {
	       	Logger.out.error(e.getMessage(),e);
	 		e.printStackTrace();
	 		assertTrue("failed to create Distribution Protocol object", true);
	    }
	}
	
	public void testDistributionProtocolWithDuplicateTitle()
	{
		
		try{
			DistributionProtocol distributionprotocol = BaseTestCaseUtility.initDistributionProtocol();	
			DistributionProtocol dupDistributionProtocol = BaseTestCaseUtility.initDistributionProtocol();
			dupDistributionProtocol.setTitle(distributionprotocol.getTitle());
			distributionprotocol = (DistributionProtocol) appService.createObject(distributionprotocol); 
			dupDistributionProtocol = (DistributionProtocol) appService.createObject(dupDistributionProtocol); 
			assertFalse("Test Failed. Duplicate Distribution Protocol name should throw exception", true);
		}
		 catch(Exception e){
			Logger.out.error(e.getMessage(),e);
			e.printStackTrace();
			assertTrue("Submission failed since a Distribution Protocol with the same NAME already exists" , true);
			 
		 }
    	
	}
	
	public void testDistributionProtocolWithEmptyShortTitle()
	{
		    	
	    try 
	  	{
	    	DistributionProtocol distributionprotocol =  BaseTestCaseUtility.initDistributionProtocol();
	    	distributionprotocol.setShortTitle("");
	    	Logger.out.info("updating domain object------->"+distributionprotocol);
	    	distributionprotocol = (DistributionProtocol) appService.createObject(distributionprotocol);
	    	Logger.out.info("Distribution Protocol object with empty short title ---->"+distributionprotocol);
	       	assertFalse("DistributionProtocol should throw exception ---->"+distributionprotocol, true);
	    } 
	    catch (Exception e) {
	       	Logger.out.error(e.getMessage(),e);
	 		e.printStackTrace();
	 		assertTrue("Failed to create Distribution Protocol object", true);
	    }
	}
	
	public void testDistributionProtocolWithEmptyStartDate()
	{
		    	
	    try 
	  	{
	    	DistributionProtocol distributionprotocol =  BaseTestCaseUtility.initDistributionProtocol();
	    	
		    distributionprotocol.setStartDate(Utility.parseDate("", Utility
						.datePattern("08/15/1975")));
	    		    	
	    	Logger.out.info("updating domain object------->"+distributionprotocol);
	    	distributionprotocol = (DistributionProtocol) appService.createObject(distributionprotocol);
	    	Logger.out.info("DistributionProtocol object with empty date ---->"+distributionprotocol);
	       	assertFalse("DistributionProtocol should throw exception ---->"+distributionprotocol, true);
	    } 
	    catch (Exception e) {
	       	Logger.out.error(e.getMessage(),e);
	 		e.printStackTrace();
	 		assertTrue("failed to create Distribution Protocol object", true);
	    }
	}
	
	public void testDistributionProtocolWithClosedActivityStatus()
	{
		    	
	    try 
	  	{
	    	DistributionProtocol distributionprotocol =  BaseTestCaseUtility.initDistributionProtocol();
	    	distributionprotocol.setActivityStatus("Closed");
	    		    	
	    	Logger.out.info("updating domain object------->"+distributionprotocol);
	    	distributionprotocol = (DistributionProtocol) appService.createObject(distributionprotocol);
	    	Logger.out.info("DistributionProtocol object with closed activity status ---->"+distributionprotocol);
	       	assertFalse("DistributionProtocol should throw exception ---->"+distributionprotocol, true);
	    } 
	    catch (Exception e) {
	       	Logger.out.error(e.getMessage(),e);
	 		e.printStackTrace();
	 		assertTrue("failed to create Distribution Protocol object", true);
	    }
	}
	
	public void testDistributionProtocolWithInvalidActivityStatus()
	{
		    	
	    try 
	  	{
	    	DistributionProtocol distributionprotocol =  BaseTestCaseUtility.initDistributionProtocol();
	    	distributionprotocol.setActivityStatus("Invalid");
	    		    	
	    	Logger.out.info("updating domain object------->"+distributionprotocol);
	    	distributionprotocol = (DistributionProtocol) appService.createObject(distributionprotocol);
	    	Logger.out.info("DistributionProtocol object with invalid activity status ---->"+distributionprotocol);
	       	assertFalse("DistributionProtocol should throw exception ---->"+distributionprotocol, true);
	    } 
	    catch (Exception e) {
	       	Logger.out.error(e.getMessage(),e);
	 		e.printStackTrace();
	 		assertTrue("failed to create Distribution Protocol object", true);
	    }
	}
	
	public void testDistributionProtocolWithInvalidSpecimenClass()
	{
		    	
	    try 
	  	{
	    	DistributionProtocol distributionprotocol =  BaseTestCaseUtility.initDistributionProtocol();
	    	
	    	SpecimenRequirement specimenRequirement = BaseTestCaseUtility.initSpecimenRequirement();
	    	specimenRequirement.setSpecimenClass("Invalid class");
	    	
	    	Collection specimenRequirementCollection = new HashSet();
			specimenRequirementCollection.add(specimenRequirement);
			distributionprotocol.setSpecimenRequirementCollection(specimenRequirementCollection);	    
			
	    	distributionprotocol = (DistributionProtocol) appService.createObject(distributionprotocol);
	    	Logger.out.info("DistributionProtocol object with invalid specimen class ---->"+distributionprotocol);
	       	assertFalse("DistributionProtocol should throw exception ---->"+distributionprotocol, true);
	    } 
	    catch (Exception e) {
	       	Logger.out.error(e.getMessage(),e);
	 		e.printStackTrace();
	 		assertTrue("failed to create Distribution Protocol object", true);
	    }
	}
	
	public void testDistributionProtocolWithInvalidSpecimenType()
	{
		    	
	    try 
	  	{
	    	DistributionProtocol distributionprotocol =  BaseTestCaseUtility.initDistributionProtocol();
	    	
	    	SpecimenRequirement specimenRequirement = BaseTestCaseUtility.initSpecimenRequirement();
	    	specimenRequirement.setSpecimenClass("Tissue");
	    	specimenRequirement.setSpecimenType("Invalid type");
	    	
	    	Collection specimenRequirementCollection = new HashSet();
			specimenRequirementCollection.add(specimenRequirement);
			distributionprotocol.setSpecimenRequirementCollection(specimenRequirementCollection);	    
			
	    	distributionprotocol = (DistributionProtocol) appService.createObject(distributionprotocol);
	    	Logger.out.info("DistributionProtocol object with SpecimenClass Tissue and invalid specimen class ---->"+distributionprotocol);
	       	assertFalse("DistributionProtocol should throw exception ---->"+distributionprotocol, true);
	    } 
	    catch (Exception e) {
	       	Logger.out.error(e.getMessage(),e);
	 		e.printStackTrace();
	 		assertTrue("failed to create Distribution Protocol object", true);
	    }
	}
	
	public void testDistributionProtocolWithInvalidTissueSite()
	{
		    	
	    try 
	  	{
	    	DistributionProtocol distributionprotocol =  BaseTestCaseUtility.initDistributionProtocol();
	    	
	    	SpecimenRequirement specimenRequirement = BaseTestCaseUtility.initSpecimenRequirement();
	    	specimenRequirement.setTissueSite("Invalid Tissue site");
	    		    	
	    	Collection specimenRequirementCollection = new HashSet();
			specimenRequirementCollection.add(specimenRequirement);
			distributionprotocol.setSpecimenRequirementCollection(specimenRequirementCollection);	    
			
	    	distributionprotocol = (DistributionProtocol) appService.createObject(distributionprotocol);
	    	Logger.out.info("DistributionProtocol object with SpecimenClass Tissue and invalid specimen class ---->"+distributionprotocol);
	       	assertFalse("DistributionProtocol should throw exception ---->"+distributionprotocol, true);
	    } 
	    catch (Exception e) {
	       	Logger.out.error(e.getMessage(),e);
	 		e.printStackTrace();
	 		assertTrue("failed to create Distribution Protocol object", true);
	    }
	}
	
	public void testDistributionProtocolWithInvalidPathologicalStatus()
	{
		    	
	    try 
	  	{
	    	DistributionProtocol distributionprotocol =  BaseTestCaseUtility.initDistributionProtocol();
	    	
	    	SpecimenRequirement specimenRequirement = BaseTestCaseUtility.initSpecimenRequirement();
	    	specimenRequirement.setPathologyStatus("Invalid Pathological status");
	    		    	
	    	Collection specimenRequirementCollection = new HashSet();
			specimenRequirementCollection.add(specimenRequirement);
			distributionprotocol.setSpecimenRequirementCollection(specimenRequirementCollection);	    
			
	    	distributionprotocol = (DistributionProtocol) appService.createObject(distributionprotocol);
	    	Logger.out.info("DistributionProtocol object with invalid Pathological status ---->"+distributionprotocol);
	       	assertFalse("DistributionProtocol should throw exception ---->"+distributionprotocol, true);
	    } 
	    catch (Exception e) {
	       	Logger.out.error(e.getMessage(),e);
	 		e.printStackTrace();
	 		assertTrue("failed to create Distribution Protocol object", true);
	    }
	}
	
	public void testNullDomainObjectInInsert_DistributionProtocol()
	{
		domainObject = new DistributionProtocol(); 
		testNullDomainObjectInInsert(domainObject);
	}
	
	public void testNullSessionDataBeanInInsert_DistributionProtocol()
	{
		domainObject = new DistributionProtocol();
		testNullSessionDataBeanInInsert(domainObject);
	}
		
	public void testWrongDaoTypeInInsert_DistributionProtocol()
	{
		domainObject = new DistributionProtocol();
		testWrongDaoTypeInInsert(domainObject);
	}
	public void testNullSessionDataBeanInUpdate_DistributionProtocol()
	{
		domainObject = new DistributionProtocol();
		testNullSessionDataBeanInUpdate(domainObject);
	}
	
	public void testNullOldDomainObjectInUpdate_DistributionProtocol()
	{
		domainObject = new DistributionProtocol();
		testNullOldDomainObjectInUpdate(domainObject);
	}
	
		
	public void testNullCurrentDomainObjectInUpdate_DistributionProtocol()
	{
		domainObject = new DistributionProtocol();
		testNullCurrentDomainObjectInUpdate(domainObject);
	}
	
	public void testEmptyCurrentDomainObjectInUpdate_DistributionProtocol()
	{
		domainObject = new DistributionProtocol();
		AbstractDomainObject initialisedDomainObject = BaseTestCaseUtility.initDistributionProtocol();
		testEmptyCurrentDomainObjectInUpdate(domainObject, initialisedDomainObject);
	}
	
	public void testEmptyOldDomainObjectInUpdate_DistributionProtocol()
	{
		domainObject = new DistributionProtocol();
		AbstractDomainObject initialisedDomainObject = BaseTestCaseUtility.initDistributionProtocol();
		testEmptyOldDomainObjectInUpdate(domainObject,initialisedDomainObject);
	}
	
	public void testNullDomainObjectInRetrieve_DistributionProtocol()
	{
		domainObject = new DistributionProtocol();
		testNullCurrentDomainObjectInRetrieve(domainObject);
	}
		
		
	
}
