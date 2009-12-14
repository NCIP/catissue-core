package edu.wustl.catissuecore.testcase.bizlogic;

import java.util.Iterator;
import java.util.List;

import edu.wustl.catissuecore.domain.SpecimenArray;
import edu.wustl.catissuecore.domain.SpecimenArrayType;
import edu.wustl.catissuecore.testcase.CaTissueSuiteBaseTest;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.util.logger.Logger;


public class SpecimenArrayTypeBizTestCases extends CaTissueSuiteBaseTest
{
	public void testAddSpecimenArrayType()
	{
		try
		{
			SpecimenArrayType specimenArrayType =  BaseTestCaseUtility.initSpecimenSpecimenArrayType();
	    	Logger.out.info("Inserting domain object------->"+specimenArrayType);
	    	specimenArrayType =  (SpecimenArrayType) appService.createObject(specimenArrayType);
			assertTrue("Domain Object is successfully added" , true);
			Logger.out.info(" SpecimenSpecimenArrayType is successfully added ---->    ID:: " + specimenArrayType.getId().toString());
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
    		SpecimenArrayType specimenArrayType = new SpecimenArrayType();
    		specimenArrayType.setId(new Long(3));
	     	Logger.out.info(" searching domain object");
	     	String query = "from edu.wustl.catissuecore.domain.SpecimenArray as specimenArray where "
				+ "specimenArray.id= 3";	
	    	List resultList = appService.search(query);
        	for (Iterator resultsIterator = resultList.iterator(); resultsIterator.hasNext();) 
        	{
        		SpecimenArray returnedSpecimenArray = (SpecimenArray)resultsIterator.next();
        		assertTrue("Specimen Array Type is successfully Found" , true);
        		Logger.out.info(" Specimen Array type is successfully Found ---->  :: " + returnedSpecimenArray.getName());
            }
       } 
       catch (Exception e) 
       {
    	 Logger.out.error(e.getMessage(),e);
 		 e.printStackTrace();
 		 fail("Failed to search Domain Object");
       }
	}
	 public void testUpdateSpecimenArray()
	 {
		try 
		{
			SpecimenArrayType specimenArrayType = new SpecimenArrayType();	
			Logger.out.info("updating Specimen Array Type------->"+specimenArrayType);
			specimenArrayType.setId(new Long(3));
			BaseTestCaseUtility.updateSpecimenSpecimenArrayType(specimenArrayType);
			SpecimenArrayType updateSpecimenSpecimenArrayType = (SpecimenArrayType) appService.updateObject(specimenArrayType);
			assertTrue("updateSpecimenSpecimenArrayType is successfully updated" , true);
			Logger.out.info("updateSpecimenSpecimenArrayType successfully updated ---->"+updateSpecimenSpecimenArrayType);
		} 
		catch (Exception e) 
		{
			Logger.out.error(e.getMessage(),e);
	 		e.printStackTrace();
	 		fail("Failed to update Specimen Collection Group");
		}
	}
}

