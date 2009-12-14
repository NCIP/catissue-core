package edu.wustl.catissuecore.testcase.bizlogic;

import edu.wustl.catissuecore.domain.Biohazard;
import edu.wustl.catissuecore.testcase.CaTissueSuiteBaseTest;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.logger.Logger;


public class BioHazardBizTestCases extends CaTissueSuiteBaseTest {

	AbstractDomainObject domainObject = null;
	public void testAddBioHazard()
	{
		try{
			Biohazard biohazard= BaseTestCaseUtility.initBioHazard();			
			System.out.println(biohazard);
			biohazard = (Biohazard) appService.createObject(biohazard); 
			TestCaseUtility.setObjectMap(biohazard, Biohazard.class);
			System.out.println("Object created successfully");
			Logger.out.info(" Domain Object added successfully");
			assertTrue("Object added successfully", true);
		 }
		 catch(Exception e){
			 e.printStackTrace();
			 assertFalse("Could not add object", true);
		 }
	}
	
	/*public void testSearchBioHazard()
	{
        try {
        	Biohazard biohazard = new Biohazard();
    		Biohazard cachedBiohazard = (Biohazard) TestCaseUtility.getObjectMap(Biohazard.class);
        	Logger.out.info("searching domain object");
        	//biohazard.setId((Long) cachedBiohazard.getId());
        	biohazard.setId(new Long(2));
        	List resultList = appService.search(Biohazard.class,biohazard);
        	 for (Iterator resultsIterator = resultList.iterator(); resultsIterator.hasNext();) 
        	 {
	    		 Biohazard returnedBiohazard = (Biohazard) resultsIterator.next();
	    		 Logger.out.info(" Domain Object is successfully Found ---->  :: " + returnedBiohazard.getName());
	    		 System.out.println(" Domain Object is successfully Found ---->  :: " + returnedBiohazard.getName());
	    		 assertTrue("Object added successfully", true);
             }
          } 
          catch (Exception e) {
           	Logger.out.error(e.getMessage(),e);
            System.out.println(e.getMessage());
           	e.printStackTrace();
           	fail("Does not find Domain Object");
	 		
          }
	}*/
	
	public void testUpdateBioHazard()
	{
		Biohazard biohazard =  BaseTestCaseUtility.initBioHazard();
    	Logger.out.info("updating domain object------->"+biohazard);
	    try 
		{
	    	biohazard = (Biohazard) appService.createObject(biohazard);
	    	BaseTestCaseUtility.updateBiohazard(biohazard);	
	    	Biohazard updatedBiohazard = (Biohazard) appService.updateObject(biohazard);
	       	Logger.out.info("Domain object successfully updated ---->"+updatedBiohazard);
	       	assertTrue("Domain object successfully updated ---->"+updatedBiohazard, true);
	    } 
	    catch (Exception e) {
	       	Logger.out.error(e.getMessage(),e);
	 		e.printStackTrace();
	 		assertFalse("failed to update Object", true);
	    }
	}
	
	public void testWithEmptyBioHazardName()
	{
		try{
			Biohazard biohazard =  BaseTestCaseUtility.initBioHazard();	
			biohazard.setName("");
			System.out.println(biohazard);
			biohazard = (Biohazard) appService.createObject(biohazard);
			assertFalse("Empty biohazard name should thorw Exception", true);
		 }
		 catch(Exception e){
			 Logger.out.error(e.getMessage(),e);
			 e.printStackTrace();
			 assertTrue("Name is required", true);
		 }
	}
	
	public void testWithDuplicateBioHazardName()
	{
		try{
			Biohazard biohazard =  BaseTestCaseUtility.initBioHazard();	
			Biohazard dupBiohazard =  BaseTestCaseUtility.initBioHazard();	
			//te.setId(new Long("1"));
			dupBiohazard.setName(biohazard.getName());
			System.out.println(biohazard);
			biohazard = (Biohazard) appService.createObject(biohazard);
			dupBiohazard = (Biohazard) appService.createObject(dupBiohazard); 
			assertFalse("Test Failed. Duplicate biohazard name should throw exception", true);
		}
		 catch(Exception e){
			Logger.out.error(e.getMessage(),e);
			e.printStackTrace();
			assertTrue("Submission failed since a Site with the same NAME already exists" , true);
			 
		 }
	}
	
	public void testInvalidBioHazardType()
	{
		try{
			Biohazard biohazard =  BaseTestCaseUtility.initBioHazard();
			biohazard.setType("Invalid");
			System.out.println(biohazard);
			biohazard = (Biohazard) appService.createObject(biohazard);
			assertFalse("Test Failed. Invalid biohazard type should throw exception", true);
		}
		 catch(Exception e){
			Logger.out.error(e.getMessage(),e);
			e.printStackTrace();
			assertTrue("Invalid Biohazard type " , true);
			 
		 }		
	}
}
