package edu.wustl.catissuecore.api.test;

import java.util.Iterator;
import java.util.List;

import edu.wustl.catissuecore.domain.Biohazard;
import edu.wustl.catissuecore.domain.pathology.IdentifiedSurgicalPathologyReport;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.catissuecore.domain.pathology.DeidentifiedSurgicalPathologyReport;


public class DeIdentifiedSurgicalPathologyReportTestCases extends CaTissueBaseTestCase {

	AbstractDomainObject domainObject = null;
	public void testAddDeidentifiedSurgicalPathologyReport()
	{
		try
		{
			DeidentifiedSurgicalPathologyReport deIdentifiedSurgicalPathologyReport= BaseTestCaseUtility.initDeIdentifiedSurgicalPathologyReport();			
			System.out.println(deIdentifiedSurgicalPathologyReport);
			deIdentifiedSurgicalPathologyReport = (DeidentifiedSurgicalPathologyReport) appService.createObject(deIdentifiedSurgicalPathologyReport);
			IdentifiedSurgicalPathologyReport identifiedSurgicalPathologyReport=(IdentifiedSurgicalPathologyReport)TestCaseUtility.getObjectMap(IdentifiedSurgicalPathologyReport.class);
			identifiedSurgicalPathologyReport.setDeIdentifiedSurgicalPathologyReport(deIdentifiedSurgicalPathologyReport);
			identifiedSurgicalPathologyReport = (IdentifiedSurgicalPathologyReport) appService.updateObject(identifiedSurgicalPathologyReport);
			TestCaseUtility.setObjectMap(deIdentifiedSurgicalPathologyReport, DeidentifiedSurgicalPathologyReport.class);
			System.out.println("Object created successfully");
			assertTrue("Object added successfully", true);
		 }
		 catch(Exception e){
			 e.printStackTrace();
			 assertFalse("could not add object", true);
		 }
	}
	
	public void testUpdateDeidentifiedSurgicalPathologyReport()
	{
		DeidentifiedSurgicalPathologyReport deIdentifiedSurgicalPathologyReport=  new DeidentifiedSurgicalPathologyReport();
    	Logger.out.info("updating domain object------->"+deIdentifiedSurgicalPathologyReport);
	    try 
		{
	    	deIdentifiedSurgicalPathologyReport=BaseTestCaseUtility.updateIdentifiedSurgicalPathologyReport(deIdentifiedSurgicalPathologyReport);	
	    	DeidentifiedSurgicalPathologyReport updatedDeIdentifiedSurgicalPathologyReport = (DeidentifiedSurgicalPathologyReport) appService.updateObject(deIdentifiedSurgicalPathologyReport);
	       	Logger.out.info("Domain object successfully updated ---->"+updatedDeIdentifiedSurgicalPathologyReport);
	       	assertTrue("Domain object successfully updated ---->"+updatedDeIdentifiedSurgicalPathologyReport, true);
	    } 
	    catch (Exception e) {
	       	Logger.out.error(e.getMessage(),e);
	 		e.printStackTrace();
	 		assertFalse("failed to update Object", true);
	    }
	}
}
