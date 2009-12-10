package edu.wustl.catissuecore.testcase.bizlogic;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import edu.wustl.catissuecore.domain.DistributionProtocol;
import edu.wustl.catissuecore.domain.DistributionSpecimenRequirement;
import edu.wustl.catissuecore.testcase.CaTissueSuiteBaseTest;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.logger.Logger;

public class DistributionProtocolBizTestCases extends CaTissueSuiteBaseTest{
	AbstractDomainObject domainObject = null;
	
	public void testAddDistributionProtocol()
	{
		try{
			DistributionProtocol distributionprotocol = BaseTestCaseUtility.initDistributionProtocol();			
			System.out.println(distributionprotocol);
			SessionDataBean bean = (SessionDataBean)getSession().getAttribute("sessionData");
			distributionprotocol = (DistributionProtocol) appService.createObject(distributionprotocol,bean);
			TestCaseUtility.setObjectMap(distributionprotocol, DistributionProtocol.class);
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
		try {		
		    DistributionProtocol distributionProtocol = new DistributionProtocol();
			DistributionProtocol cachedDistributionProtocol = new DistributionProtocol();
			Logger.out.info(" searching domain object");
	    	distributionProtocol.setId((Long) cachedDistributionProtocol.getId());
	    	String query  = "from edu.wustl.catissuecore.domain.DistributionProtocol as distributionProtocol where "
				+ "distributionProtocol.id= "+cachedDistributionProtocol.getId();	
	        List resultList = appService.search(query);
	         for (Iterator resultsIterator = resultList.iterator(); resultsIterator.hasNext();) {
	        		 DistributionProtocol returnedDP = (DistributionProtocol) resultsIterator.next();
	        		 Logger.out.info(" Domain Object is successfully Found ---->  :: " + returnedDP.getTitle());
	        		// System.out.println(" Domain Object is successfully Found ---->  :: " + returnedDepartment.getName());
	             }
	          } 
	          catch (Exception e) {
	           	Logger.out.error(e.getMessage(),e);
	           	e.printStackTrace();
	       	 assertFalse("could not add object", true);
		 		
	          }
	}
	
	/*public void testSearchDistributionProtocolWithspecimenRequirment()
	{
		try {		
		    DistributionProtocol distributionProtocol = new DistributionProtocol();
		
	   
	       
	     	DistributionSpecimenRequirement distributionSpecimenRequirement =BaseTestCaseUtility.initDistributionSpecimenRequirement();
			Collection<DistributionSpecimenRequirement> distributionSpecimenRequirementCollection = new HashSet<DistributionSpecimenRequirement>();
			distributionSpecimenRequirementCollection.add(distributionSpecimenRequirement);
			distributionProtocol.setDistributionSpecimenRequirementCollection(distributionSpecimenRequirementCollection);
            String query = "select from DistributionSpecimenRequirement as distributionSpecimenRequirement "+
            "where distributionSpecimenRequirement.distributionProtocol "
		     List resultList = appService.search(DistributionProtocol.class,distributionProtocol);
	         	if(resultList.size()==1)
	         	{
	         		assertTrue("Distribution protocol search successfully",true);
	         	}
			}
	          catch (Exception e) {
	           	Logger.out.error(e.getMessage(),e);
	           	e.printStackTrace();
	           	assertFalse("Does not find Distribution protocol Object",true);
		 		
	          }
	}*/
	
	public void testUpdateDistributionProtocol()
	{
	    try 
	  	{
	    	DistributionProtocol distributionProtocol = (DistributionProtocol) TestCaseUtility.getObjectMap(DistributionProtocol.class);
	    	Logger.out.info("updating domain object------->"+distributionProtocol);
	    	//distributionprotocol = (DistributionProtocol) appService.createObject(distributionprotocol);
	    	BaseTestCaseUtility.updateDistributionProtocol(distributionProtocol);	
	    	SessionDataBean bean = (SessionDataBean)getSession().getAttribute("sessionData");
	    	DistributionProtocol updatedDistributionProtocol = (DistributionProtocol) appService.updateObject(distributionProtocol,bean);
	       	Logger.out.info("Domain object successfully updated ---->"+updatedDistributionProtocol);
	       	assertTrue("Domain object successfully updated ---->"+updatedDistributionProtocol, true);
	    } 
	    catch (Exception e) {
	       	Logger.out.error(e.getMessage(),e);
	 		e.printStackTrace();
	 		assertFalse("failed to update Object", true);
	    }
	}
	
	public void testDistributionProtocolWithEmptyTitle()
	{
		    	
	    try 
	  	{
	    	DistributionProtocol distributionprotocol =  BaseTestCaseUtility.initDistributionProtocol();
	    	distributionprotocol.setTitle("");
	    	Logger.out.info("updating domain object------->"+distributionprotocol);
	    	SessionDataBean bean = (SessionDataBean)getSession().getAttribute("sessionData");
	    	distributionprotocol = (DistributionProtocol) appService.createObject(distributionprotocol,bean);
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
			SessionDataBean bean = (SessionDataBean)getSession().getAttribute("sessionData");
			distributionprotocol = (DistributionProtocol) appService.createObject(distributionprotocol,bean); 
			dupDistributionProtocol = (DistributionProtocol) appService.createObject(dupDistributionProtocol,bean); 
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
	    	SessionDataBean bean = (SessionDataBean)getSession().getAttribute("sessionData");
	    	distributionprotocol = (DistributionProtocol) appService.createObject(distributionprotocol,bean);
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
	    	SessionDataBean bean = (SessionDataBean)getSession().getAttribute("sessionData");
		    distributionprotocol.setStartDate(Utility.parseDate("", Utility
						.datePattern("08/15/1975")));
	    		    	
	    	Logger.out.info("updating domain object------->"+distributionprotocol);
	    	distributionprotocol = (DistributionProtocol) appService.createObject(distributionprotocol,bean);
	    	Logger.out.info("DistributionProtocol object with empty date ---->"+distributionprotocol);
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
	    	SessionDataBean bean = (SessionDataBean)getSession().getAttribute("sessionData");
	    	distributionprotocol = (DistributionProtocol) appService.createObject(distributionprotocol,bean);
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
	    	
	    	DistributionSpecimenRequirement distributionSpecimenRequirement = BaseTestCaseUtility.initDistributionSpecimenRequirement();
	    	distributionSpecimenRequirement.setSpecimenClass("Invalid class");
	    	
	    	Collection distributionSpecimenRequirementCollection = new HashSet();
	    	distributionSpecimenRequirementCollection.add(distributionSpecimenRequirement);
			distributionprotocol.setDistributionSpecimenRequirementCollection(distributionSpecimenRequirementCollection);	    
			
			SessionDataBean bean = (SessionDataBean)getSession().getAttribute("sessionData");
	    	distributionprotocol = (DistributionProtocol) appService.createObject(distributionprotocol,bean);
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
	    	
	    	DistributionSpecimenRequirement distributionSpecimenRequirement = BaseTestCaseUtility.initDistributionSpecimenRequirement();
	    	distributionSpecimenRequirement.setSpecimenClass("Tissue");
	    	distributionSpecimenRequirement.setSpecimenType("Invalid type");
	    	
	    	Collection distributionSpecimenRequirementCollection = new HashSet();
	    	distributionSpecimenRequirementCollection.add(distributionSpecimenRequirement);
			distributionprotocol.setDistributionSpecimenRequirementCollection(distributionSpecimenRequirementCollection);	    
			
			SessionDataBean bean = (SessionDataBean)getSession().getAttribute("sessionData");
	    	distributionprotocol = (DistributionProtocol) appService.createObject(distributionprotocol,bean);
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
	    	
	    	DistributionSpecimenRequirement distributionSpecimenRequirement = BaseTestCaseUtility.initDistributionSpecimenRequirement();
	    	distributionSpecimenRequirement.setTissueSite("Invalid Tissue site");
	    		    	
	    	Collection distributionSpecimenRequirementCollection = new HashSet();
	    	distributionSpecimenRequirementCollection.add(distributionSpecimenRequirement);
			distributionprotocol.setDistributionSpecimenRequirementCollection(distributionSpecimenRequirementCollection);	    
			
			SessionDataBean bean = (SessionDataBean)getSession().getAttribute("sessionData");
	    	distributionprotocol = (DistributionProtocol) appService.createObject(distributionprotocol,bean);
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
	    	
	    	DistributionSpecimenRequirement distributionSpecimenRequirement = BaseTestCaseUtility.initDistributionSpecimenRequirement();
	    	distributionSpecimenRequirement.setPathologyStatus("Invalid Pathological status");
	    		    	
	    	Collection distributionSpecimenRequirementCollection = new HashSet();
	    	distributionSpecimenRequirementCollection.add(distributionSpecimenRequirement);
			distributionprotocol.setDistributionSpecimenRequirementCollection(distributionSpecimenRequirementCollection);	    
			SessionDataBean bean = (SessionDataBean)getSession().getAttribute("sessionData");
	    	distributionprotocol = (DistributionProtocol) appService.createObject(distributionprotocol,bean);
	    	Logger.out.info("DistributionProtocol object with invalid Pathological status ---->"+distributionprotocol);
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
	    	Logger.out.info("updating domain object------->"+distributionprotocol);
	    	SessionDataBean bean = (SessionDataBean)getSession().getAttribute("sessionData");
	    	distributionprotocol = (DistributionProtocol) appService.createObject(distributionprotocol,bean);
	    	distributionprotocol.setActivityStatus("Closed");
	 	    DistributionProtocol updatedDistributionprotocol = (DistributionProtocol) appService.updateObject(distributionprotocol, bean);
	    	Logger.out.info("DistributionProtocol object with closed activity status ---->"+distributionprotocol);
	       	assertTrue("DistributionProtocol should throw exception ---->"+distributionprotocol, true);
	    } 
	    catch (Exception e) {
	       	Logger.out.error(e.getMessage(),e);
	 		e.printStackTrace();
	 		assertFalse("Failed to close Distribution Protocol object", true);
	    }
	}
	
	public void testDistributionProtocolWithDisabledActivityStatus()
	{
		    	
	    try 
	  	{
	    	DistributionProtocol distributionprotocol =  BaseTestCaseUtility.initDistributionProtocol();
	    	Logger.out.info("updating domain object------->"+distributionprotocol);
	    	SessionDataBean bean = (SessionDataBean)getSession().getAttribute("sessionData");
	    	distributionprotocol = (DistributionProtocol) appService.createObject(distributionprotocol, bean);
	    	distributionprotocol.setActivityStatus("Disabled");
	    	DistributionProtocol updatedDistributionprotocol = (DistributionProtocol) appService.updateObject(distributionprotocol,bean);
	    	Logger.out.info("DistributionProtocol object with closed activity status ---->"+distributionprotocol);
	       	assertTrue("DistributionProtocol successfully updated  ---->"+distributionprotocol, true);
	    } 
	    catch (Exception e) {
	       	Logger.out.error(e.getMessage(),e);
	 		e.printStackTrace();
	 		assertFalse("Failed to disable Distribution Protocol object", true);
	    }
	}
	
	public void testDistributionProtocolWithNoDistributionSpecimenRequirement()
	{
	    try 
	  	{
	    	DistributionProtocol distributionprotocol =  BaseTestCaseUtility.initDistributionProtocol();
	    	distributionprotocol.setDistributionSpecimenRequirementCollection(new HashSet<DistributionSpecimenRequirement>());
	    	Logger.out.info("creating domain object------->"+distributionprotocol);
	    	SessionDataBean bean = (SessionDataBean)getSession().getAttribute("sessionData");
	    	distributionprotocol = (DistributionProtocol) appService.createObject(distributionprotocol, bean);
	    	Logger.out.info("DistributionProtocol object with no Distribution Specimen Requirement ---->"+distributionprotocol);
	       	assertTrue("DistributionProtocol successfully created  ---->"+distributionprotocol, true);
	    } 
	    catch (Exception e) {
	       	Logger.out.error(e.getMessage(),e);
	 		e.printStackTrace();
	 		assertFalse("Failed to create Distribution Protocol object", true);
	    }
	}
}
