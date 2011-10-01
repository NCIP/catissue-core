package edu.wustl.catissuecore.testcase.bizlogic;

import java.util.Iterator;
import java.util.List;

import edu.wustl.catissuecore.domain.SpecimenArray;
import edu.wustl.catissuecore.testcase.CaTissueSuiteBaseTest;
import edu.wustl.common.util.logger.Logger;


public class SpecimenArrayBizTestCases extends CaTissueSuiteBaseTest
{
	public void testAddSpecimenArray()
	{
		try
		{
			SpecimenArray specimenArray =  BaseTestCaseUtility.initSpecimenArray();
	    	Logger.out.info("Inserting domain object------->"+specimenArray);
	    	specimenArray =  (SpecimenArray) appService.createObject(specimenArray);
			assertTrue("Domain Object is successfully added" , true);
			Logger.out.info(" Specimen Collection Group is successfully added ---->    ID:: " + specimenArray.getId().toString());
		}
		catch(Exception e)
		{
			Logger.out.error(e.getMessage(),e);
			e.printStackTrace();
			fail("Failed to add Domain Object");
		}
	}
	
	
	 public void testSearchSpecimenArray()
	 {
    	try 
    	{
    		SpecimenArray specimenArray = new SpecimenArray();
	     	Logger.out.info(" searching domain object");
	     	String query = "from edu.wustl.catissuecore.domain.SpecimenArray ";
	    	List resultList = appService.search(query);
        	for (Iterator resultsIterator = resultList.iterator(); resultsIterator.hasNext();) 
        	{
        		SpecimenArray returnedSpecimenArray = (SpecimenArray)resultsIterator.next();
        		assertTrue("Specimen Array is successfully Found" , true);
        		Logger.out.info(" Specimen Array is successfully Found ---->  :: " + returnedSpecimenArray.getName());
            }
       } 
       catch (Exception e) 
       {
    	 Logger.out.error(e.getMessage(),e);
 		 e.printStackTrace();
 		 fail("Failed to search Domain Object");
       }
	}
	
//	public void testUpdateSpecimenArray()
//	{
//		try 
//		{
//			SpecimenArray specimenArray = new SpecimenArray();	
//			Logger.out.info("updating Specimen Array------->"+specimenArray);
//			BaseTestCaseUtility.updateSpecimenCollectionGroup(specimenArray);
//			SpecimenCollectionGroup updatedSpecimenCollectionGroup = (SpecimenCollectionGroup) appService.updateObject(specimenArray);
//			assertTrue("Specimen Collection Group is successfully updated" , true);
//			Logger.out.info("Specimen Collection Group successfully updated ---->"+updatedSpecimenCollectionGroup);
//		} 
//		catch (Exception e) {
//			Logger.out.error(e.getMessage(),e);
//	 		e.printStackTrace();
//	 		fail("Failed to update Specimen Collection Group");
//		}
//	}
}
