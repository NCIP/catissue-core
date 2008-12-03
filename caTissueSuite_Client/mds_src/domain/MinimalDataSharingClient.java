package domain;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.logger.Logger;
import gov.nih.nci.system.applicationservice.ApplicationService;
import gov.nih.nci.system.applicationservice.ApplicationServiceProvider;

import java.text.ParseException;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

/**
 *
 * MinimalDataSharingClient.java shows various ways to execute searches with and without
 * using Application Service Layer (convenience layer that abstracts building criteria
 * @author virender_mehta
 *
 */
public class MinimalDataSharingClient extends TestCase
{
	/**
	 * Test case for searching report object
	 */
	public static void testsearchReport()
	{
		ApplicationService appService = ApplicationServiceProvider.getApplicationService();
		Logger.configure("");
		ReportingPeriod rp = new ReportingPeriod();
    	try 
    	{
        	 List resultList = appService.search(ReportingPeriod.class,rp);
        	 for (Iterator resultsIterator = resultList.iterator(); resultsIterator.hasNext();)
        	 {
        		 ReportingPeriod returnedReportingPeriod = (ReportingPeriod) resultsIterator.next();
        		 System.out.println("End Date:"+returnedReportingPeriod.getEnd());
        		 System.out.println("Start Date:"+returnedReportingPeriod.getStart());
             }
        	 assertTrue("Successfully searched", true);
          } 
          catch (Exception e) 
          {
	 		e.printStackTrace();
	 		System.out.println("MinimalDataSharingClient.testsearchReport()"+e);
	 		assertFalse("Error in searching  Report object", true);
          }
	}
	
	/**
	 * Test for searching SpecimenInventory
	 */
	public static void testsearchSpecimenInv()
	{
		ApplicationService appService = ApplicationServiceProvider.getApplicationService();
		Logger.configure("");
		SpecimenInventory specimenInv = new SpecimenInventory();
	   	try 
    	{
        	 List resultList = appService.search(SpecimenInventory.class,specimenInv);
        	 for (Iterator resultsIterator = resultList.iterator(); resultsIterator.hasNext();)
        	 {
        		 SpecimenInventory returnedReportingPeriod = (SpecimenInventory) resultsIterator.next();
        		 System.out.println("Collected for Cp id:"+returnedReportingPeriod.getCollectedFor());
             }
        	 assertTrue("Successfully searched", true);
          } 
          catch (Exception e) 
          {
	 		e.printStackTrace();
	 		System.out.println("MinimalDataSharingClient.testsearchSpecimen()"+ e);
	 		assertFalse("Error in searching object", true);
          }
	}
	
	/**
	 * Test case for searching reporting period
	 */
	public static void testsearchSpecimenDistributed()
	{
		ApplicationService appService = ApplicationServiceProvider.getApplicationService();
		Logger.configure("");
		ReportingPeriod rp = new ReportingPeriod();
		try
		{
			rp.setStart(Utility.parseDate("10/01/2008", Utility.datePattern("10/01/2008")));
			rp.setEnd(Utility.parseDate("12/31/2008", Utility.datePattern("12/31/2008")));
		}
		catch (ParseException e1)
		{
			e1.printStackTrace();
		}
    	try 
    	{
        	 List resultList = appService.search(ReportingPeriod.class,rp);
        	 for (Iterator resultsIterator = resultList.iterator(); resultsIterator.hasNext();)
        	 {
        		 ReportingPeriod returnedReportingPeriod = (ReportingPeriod) resultsIterator.next();
        		 System.out.println(returnedReportingPeriod.getId());
             }
        	 assertTrue("Successfully searched", true);
          } 
          catch (Exception e) 
          {
	 		e.printStackTrace();
	 		System.out.println("MinimalDataSharingClient.testsearchSpecimenDistributed()" + e);
	 		assertFalse("Error in searching object", true);
          }
	}
}
